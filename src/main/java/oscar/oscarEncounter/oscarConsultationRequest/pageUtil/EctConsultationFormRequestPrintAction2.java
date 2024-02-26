/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

/*
 * EctConsultationFormRequestPrintAction.java
 *
 * Created on November 19, 2007, 4:05 PM
 */

package oscar.oscarEncounter.oscarConsultationRequest.pageUtil;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.DocumentException;

import org.apache.logging.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.model.EFormData;
import org.oscarehr.hospitalReportManager.HRMPDFCreator;
import org.oscarehr.managers.ConsultationManager;
import org.oscarehr.managers.FaxManager;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import org.oscarehr.documentManager.EDoc;
import org.oscarehr.documentManager.EDocUtil;
import oscar.form.util.FormTransportContainer;
import oscar.oscarEncounter.data.EctFormData;
import oscar.oscarLab.ca.all.pageUtil.LabPDFCreator;
import oscar.oscarLab.ca.on.CommonLabResultData;
import oscar.oscarLab.ca.on.LabResultData;
import oscar.util.ConcatPDF;
import oscar.util.UtilDateUtilities;

import com.sun.xml.messaging.saaj.util.ByteInputStream;
import com.sun.xml.messaging.saaj.util.ByteOutputStream;

/**
 *
 * Convert submitted preventions into pdf and return file
 */
public class EctConsultationFormRequestPrintAction2 extends Action {
    
    private static final Logger logger = MiscUtils.getLogger();
    private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);

	private ConsultationManager consultationManager = SpringUtils.getBean(ConsultationManager.class);

	private static FaxManager faxManager = SpringUtils.getBean(FaxManager.class);

	public EctConsultationFormRequestPrintAction2() {
    }
    
    @Override
    public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response){
    	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
    	
    	if(!securityInfoManager.hasPrivilege(loggedInInfo, "_con", "r", null)) {
			throw new SecurityException("missing required security object (_con)");
		}
    	
    	String reqId = (String) request.getAttribute("reqId");
    	if (request.getParameter("reqId")!=null) reqId = request.getParameter("reqId");
    	
		String demoNo = request.getParameter("demographicNo");
		ArrayList<EDoc> docs = EDocUtil.listDocs(loggedInInfo, demoNo, reqId, EDocUtil.ATTACHED);
		String path = OscarProperties.getInstance().getProperty("DOCUMENT_DIR");
		if(! path.endsWith(File.separator))
		{
			path = path + File.separator;
		}
		ArrayList<Object> alist = new ArrayList<Object>();
		byte[] buffer;
		ByteInputStream bis;
		ByteOutputStream bos;
		CommonLabResultData consultLabs = new CommonLabResultData();
		ArrayList<InputStream> streams = new ArrayList<InputStream>();

		ArrayList<LabResultData> labs = consultLabs.populateLabResultsData(loggedInInfo, demoNo, reqId, CommonLabResultData.ATTACHED);
		String error = "";
		Exception exception = null;
		try {

			bos = new ByteOutputStream();
			ConsultationPDFCreator cpdfc = new ConsultationPDFCreator(request,bos);
			cpdfc.printPdf(loggedInInfo);
			
			buffer = bos.getBytes();
			bis = new ByteInputStream(buffer, bos.getCount());
			bos.close();
			streams.add(bis);
			alist.add(bis);

			// attached eForms
			List<EFormData> eForms = consultationManager.getAttachedEForms(reqId);

			for(EFormData eFormItem : eForms) {
				Path attachedForm = faxManager.renderFaxDocument(loggedInInfo, FaxManager.TransactionType.EFORM, eFormItem.getId(), eFormItem.getDemographicId());
				alist.add(Files.newInputStream(attachedForm));
			}

			//attached docs
			for (int i = 0; i < docs.size(); i++) {
				EDoc doc = docs.get(i);  
				if (doc.isPrintable()) {
					if (doc.isImage()) {
						bos = new ByteOutputStream();
						request.setAttribute("imagePath", path + doc.getFileName());
						request.setAttribute("imageTitle", doc.getDescription());
						ImagePDFCreator ipdfc = new ImagePDFCreator(request, bos);
						ipdfc.printPdf();
						
						buffer = bos.getBytes();
						bis = new ByteInputStream(buffer, bos.getCount());
						bos.close();
						streams.add(bis);
						alist.add(bis);

					}
					else if (doc.isPDF()) {
						alist.add(path + doc.getFileName());
					}
					else {
						logger.error("EctConsultationFormRequestPrintAction: " + doc.getType() + " is marked as printable but no means have been established to print it.");	
					}
				}
			}

			// Iterating over requested labs.
			for (int i = 0; labs != null && i < labs.size(); i++) {
				File tempLabPDF = File.createTempFile("lab" + labs.get(i).segmentID, "pdf");

				// Storing the lab in PDF format inside a byte stream.
				try (
						FileOutputStream fileOutputStream = new FileOutputStream(tempLabPDF);
						ByteOutputStream byteOutputStream = new ByteOutputStream();
				) {
					request.setAttribute("segmentID", labs.get(i).segmentID);
					LabPDFCreator labPDFCreator = new LabPDFCreator(request, fileOutputStream);
					labPDFCreator.printPdf();
					labPDFCreator.addEmbeddedDocuments(tempLabPDF, byteOutputStream);

					// Transferring PDF to an input stream to be concatenated with
					// the rest of the documents.
					buffer = byteOutputStream.getBytes();
					bis = new ByteInputStream(buffer, byteOutputStream.getCount());
					streams.add(bis);
					alist.add(bis);
				}
				tempLabPDF.delete();
			}

			// attached HRMs
			ArrayList<HashMap<String,? extends Object>> attachedHRMDocuments = consultationManager.getAttachedHRMDocuments(loggedInInfo, demoNo, reqId);
			for (HashMap<String,? extends Object> attachedHRMDocument : attachedHRMDocuments) {
				bos = new ByteOutputStream();
				HRMPDFCreator hrmPdf = new HRMPDFCreator(bos, (Integer)attachedHRMDocument.get("id"), loggedInInfo);
				hrmPdf.printPdf();
				buffer = bos.getBytes();
				bis = new ByteInputStream(buffer, bos.getCount());
				bos.close();
				streams.add(bis);
				alist.add(bis);
			}

			// attached forms
			List<EctFormData.PatientForm> forms = consultationManager.getAttachedForms(loggedInInfo, Integer.parseInt(reqId), Integer.parseInt(demoNo));

			for(EctFormData.PatientForm  formItem : forms) {
				FormTransportContainer formTransportContainer = new FormTransportContainer(
						response, request, mapping.findForward("attachform").getPath()
						+ "?method=fetch&formname="
						+ formItem.getFormName()
						+ "&demographic_no="
						+ formItem.getDemoNo()
						+ "&formId="
						+ formItem.getFormId());
				formTransportContainer.setDemographicNo( demoNo );
				formTransportContainer.setProviderNo( loggedInInfo.getLoggedInProviderNo() );
				formTransportContainer.setSubject( formItem.getFormName() + " Form ID " + formItem.getFormId() );
				formTransportContainer.setFormName( formItem.getFormName() );
				formTransportContainer.setRealPath( getServlet().getServletContext().getRealPath( File.separator ) );
				Path attachedForm = faxManager.renderFaxDocument(loggedInInfo, FaxManager.TransactionType.FORM, formTransportContainer);
				alist.add(Files.newInputStream(attachedForm));
			}

			if (alist.size() > 0) {

				bos = new ByteOutputStream();
				ConcatPDF.concat(alist, bos);
				response.setContentType("application/pdf"); // octet-stream
				response.setHeader(
						"Content-Disposition",
						"inline; filename=\"combinedPDF-"
								+ UtilDateUtilities.getToday("yyyy-mm-dd.hh.mm.ss")
								+ ".pdf\"");
				response.getOutputStream().write(bos.getBytes(), 0, bos.getCount());
			}

		} catch (com.lowagie.text.DocumentException de) {
			error = "DocumentException";
			exception = de;
		} catch (DocumentException de) {
			error = "DocumentException";
			exception = de;
		} catch (IOException ioe) {
			error = "IOException";
			exception = ioe;
		} catch (ServletException e) {
			throw new RuntimeException(e);
		} finally {
			// Cleaning up InputStreams created for concatenation.
			for (InputStream is : streams) {
				try {
					is.close();
				} catch (IOException e) {
					error = "IOException";
				}
			}
		}
		if (!error.equals("")) {
			logger.error(error + " occured insided ConsultationPrintAction", exception);
			request.setAttribute("printError", new Boolean(true));
			return mapping.findForward("error");
		}
		return null;
		
    }
}
