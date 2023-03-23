/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */
package oscar.eform.actions;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.dao.EFormDataDao;
import org.oscarehr.common.model.EFormData;
import org.oscarehr.hospitalReportManager.HRMPDFCreator;
import org.oscarehr.hospitalReportManager.dao.HRMDocumentToDemographicDao;
import org.oscarehr.hospitalReportManager.model.HRMDocumentToDemographic;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.util.WKHtmlToPdfUtils;

import com.sun.xml.messaging.saaj.util.ByteInputStream;
import com.sun.xml.messaging.saaj.util.ByteOutputStream;

import oscar.OscarProperties;
import oscar.dms.EDoc;
import oscar.dms.EDocUtil;
import oscar.eform.EFormUtil;
import oscar.oscarEncounter.oscarConsultationRequest.pageUtil.ImagePDFCreator;
import oscar.oscarLab.ca.all.pageUtil.LabPDFCreator;
import oscar.oscarLab.ca.on.CommonLabResultData;
import oscar.oscarLab.ca.on.LabResultData;
import oscar.util.ConcatPDF;
import oscar.util.UtilDateUtilities;

public class PrintAction extends Action {

	private static final Logger logger = MiscUtils.getLogger();

	private String localUri = null;

	private HttpServletResponse response;
	
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
		
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_eform", "r", null)) {
			throw new SecurityException("missing required security object (_eform)");
		}
		
		localUri = getEformRequestUrl(request);
		this.response = response;
		String id  = (String)request.getAttribute("fdid");
		try {
			printForm(request, LoggedInInfo.getLoggedInInfoFromSession(request), id, response);
		} catch (Exception e) {
			logger.error("",e);
			return mapping.findForward("error");
		}
		return mapping.findForward("success");
	}
	
	/**
	 * This method is a copy of Apache Tomcat's ApplicationHttpRequest getRequestURL method with the exception that the uri is removed and replaced with our eform viewing uri. Note that this requires that the remote url is valid for local access. i.e. the
	 * host name from outside needs to resolve inside as well. The result needs to look something like this : https://127.0.0.1:8443/oscar/eformViewForPdfGenerationServlet?fdid=2&parentAjaxId=eforms
	 */
    public static String getEformRequestUrl(HttpServletRequest request) {
		StringBuilder url = new StringBuilder();
		String scheme = request.getScheme();
		Integer port;
		try { port = new Integer(OscarProperties.getInstance().getProperty("oscar_port")); }
	    catch (Exception e) { port = 8443; }
		if (port < 0) port = 80; // Work around java.net.URL bug

		url.append(scheme);
		url.append("://");
		//url.append(request.getServerName());
		url.append("127.0.0.1");
		
		if ((scheme.equals("http") && (port != 80)) || (scheme.equals("https") && (port != 443))) {
			url.append(':');
			url.append(port);
		}
		url.append(request.getContextPath());
		url.append("/EFormViewForPdfGenerationServlet?parentAjaxId=eforms&providerId=");
		url.append(request.getParameter("providerId"));
		url.append("&fdid=");

		return (url.toString());
	}

	//Converts to byte input stream and adds to list.
	private static void convertAndAddToList(ArrayList<Object> alist, ByteOutputStream bos) {
		byte[] buffer = bos.getBytes();
		try(ByteInputStream bis = new ByteInputStream(buffer, bos.getCount())) {
			alist.add(bis);
		} catch (IOException e) {
			logger.error("Exception printing PDF ", e);
			throw new RuntimeException(e);
		} finally{
			bos.close();
		}
	}

	// Adds attached eDocs converted to pdf byte input stream to attachment list.
	private static void returnConvertedAttachedDocs(ArrayList<Object> alist, String formId, LoggedInInfo loggedInInfo, HttpServletRequest request, EFormData eformData) {
		//getting documents
		ArrayList<EDoc> docs = EDocUtil.listDocsAttachedToEForm(loggedInInfo, String.valueOf(eformData.getDemographicId()), formId, EDocUtil.ATTACHED);
		//converting
		String documentDirectory = OscarProperties.getInstance().getProperty("DOCUMENT_DIR");
		try {
			for (int i = 0; i < docs.size(); i++) {
				EDoc doc = docs.get(i);
				// Documents will be converted if they are an image or a pdf. Other types will be ignored.
				if (doc.isPrintable()) {
					if (doc.isImage()) {
						try(ByteOutputStream bos = new ByteOutputStream()) {
							request.setAttribute("imagePath", documentDirectory + doc.getFileName());
							request.setAttribute("imageTitle", doc.getDescription());
							ImagePDFCreator ipdfc = new ImagePDFCreator(request, bos);
							ipdfc.printPdf();
							convertAndAddToList(alist, bos);
						}
					} else if (doc.isPDF()) {
						alist.add(documentDirectory + doc.getFileName());
					} else {
						logger.error("EctConsultationFormRequestPrintAction: " + doc.getType() + " is marked as printable but no means have been established to print it.");
					}
				}
			}
		} catch (com.itextpdf.text.DocumentException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		}
	}

	// Adds attached labs converted to pdf byte input stream to attachment list.
	private static void returnConvertedAttachedLabs(ArrayList<Object> alist, String formId, LoggedInInfo loggedInInfo, HttpServletRequest request, EFormData eformData) {
		//getting documents
		CommonLabResultData consultLabs = new CommonLabResultData();
		ArrayList<LabResultData> labs = consultLabs.populateLabResultsDataEForm(loggedInInfo, String.valueOf(eformData.getDemographicId()), formId, CommonLabResultData.ATTACHED);

		// converting
		for (int i = 0; labs != null && i < labs.size(); i++) {
			try(ByteOutputStream bos = new ByteOutputStream()) {
				request.setAttribute("segmentID", labs.get(i).segmentID);
				LabPDFCreator lpdfc = new LabPDFCreator(request, bos);
				lpdfc.printPdf();
				convertAndAddToList(alist, bos);
			} catch (Exception e) {
				logger.error("", e);
			}
		}

	}

	// Adds attached HRM reports converted to pdf byte input stream to attachment list.
	private static void returnConvertedAttachedHRM(ArrayList<Object> alist, String formId, LoggedInInfo loggedInInfo, HttpServletRequest request, EFormData eformData) {
		//getting documents
		HRMDocumentToDemographicDao hrmDocumentToDemographicDao = SpringUtils.getBean(HRMDocumentToDemographicDao.class);
		List<HRMDocumentToDemographic> attachedHRMReports = hrmDocumentToDemographicDao.findHRMDocumentsAttachedToEForm(formId);
		//converting
		for (HRMDocumentToDemographic attachedHRM : attachedHRMReports) {
			try(ByteOutputStream bos = new ByteOutputStream()) {
				HRMPDFCreator hrmPdfCreator = new HRMPDFCreator(bos, attachedHRM.getHrmDocumentId(), loggedInInfo);
				hrmPdfCreator.printPdf();
				convertAndAddToList(alist, bos);
			}
		}
	}

	// Adds attached eForms converted to pdf byte input stream to attachment list.
	private static void returnConvertedAttachedEForms(ArrayList<Object> alist, String formId, LoggedInInfo loggedInInfo, HttpServletRequest request, EFormData eformData) throws IOException {
		//getting documents
		List<EFormData> eForms = EFormUtil.listPatientEformsCurrentAttachedToEForm(formId);
		//converting
		for (EFormData eForm : eForms) {
			byte[] buffer = WKHtmlToPdfUtils.convertToPdf(PrintAction.getEformRequestUrl(request) + eForm.getId());
			try(ByteInputStream bis = new ByteInputStream(buffer, buffer.length)) {
				alist.add(bis);
			} catch (IOException e) {
				logger.error("", e);
			}
		}
	}

	// Returns a list of all attached documents converted to a byte input stream.
	private static ArrayList<Object> returnAttachedDocs(String formId, LoggedInInfo loggedInInfo, HttpServletRequest request) throws IOException {
		ArrayList<Object> alist = new ArrayList<Object>();
		EFormDataDao efmDataDao = SpringUtils.getBean(EFormDataDao.class);
		EFormData eformData = efmDataDao.find(Integer.parseInt(formId));
		byte[] buffer = WKHtmlToPdfUtils.convertToPdf(getEformRequestUrl(request) + eformData.getId());
		try(ByteInputStream bis = new ByteInputStream(buffer, buffer.length)){
			// adding main eForm
			alist.add(bis);
			// Added attached docs, labs, hrm, eForms.
			returnConvertedAttachedDocs(alist, formId, loggedInInfo, request, eformData);
			returnConvertedAttachedLabs(alist, formId, loggedInInfo, request, eformData);
			returnConvertedAttachedHRM(alist, formId, loggedInInfo, request, eformData);
			returnConvertedAttachedEForms(alist, formId, loggedInInfo, request, eformData);
		} catch (IOException e) {
			logger.error("", e);
		}
		return alist;
	}

	// Return eForm attachments pdf onto response.
	public static void printForm(HttpServletRequest request, LoggedInInfo loggedInInfo, String formId, HttpServletResponse response) throws IOException {
		ArrayList<Object> alist = returnAttachedDocs(formId, loggedInInfo, request);
		try(ByteOutputStream bos = new ByteOutputStream();) {
			if (alist.size() > 0) {
				ConcatPDF.concat(alist, bos);
				response.setContentType("application/pdf"); // octet-stream
				response.setHeader(
						"Content-Disposition",
						"inline; filename=\"combinedPDF-"
								+ UtilDateUtilities.getToday("yyyy-mm-dd.hh.mm.ss")
								+ ".pdf\"");
				response.getOutputStream().write(bos.getBytes(), 0, bos.getCount());
			}
		} catch (IOException e) {
			logger.error("", e);
		}
	}
}
