/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

package oscar.oscarEncounter.oscarConsultationRequest.pageUtil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.dao.ClinicDAO;
import org.oscarehr.common.dao.FaxClientLogDao;
import org.oscarehr.common.dao.FaxConfigDao;
import org.oscarehr.common.dao.FaxJobDao;
import org.oscarehr.common.model.Clinic;
import org.oscarehr.common.model.FaxClientLog;
import org.oscarehr.common.model.FaxConfig;
import org.oscarehr.common.model.FaxJob;
import org.oscarehr.fax.core.FaxAccount;
import org.oscarehr.fax.core.FaxRecipient;

import org.oscarehr.managers.FaxManager;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.dms.EDoc;
import oscar.dms.EDocUtil;
import oscar.log.LogAction;
import oscar.log.LogConst;
import oscar.oscarLab.ca.all.pageUtil.LabPDFCreator;
import oscar.oscarLab.ca.on.CommonLabResultData;
import oscar.oscarLab.ca.on.LabResultData;
import oscar.util.ConcatPDF;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.DocumentException;
import com.sun.xml.messaging.saaj.util.ByteInputStream;
import com.sun.xml.messaging.saaj.util.ByteOutputStream;

public class EctConsultationFormFaxAction extends Action {

	private static final Logger logger = MiscUtils.getLogger();
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
    private static FaxClientLogDao faxClientLogDao = SpringUtils.getBean(FaxClientLogDao.class);
	private static FaxJobDao faxJobDao = SpringUtils.getBean(FaxJobDao.class);				
	private static FaxConfigDao faxConfigDao = SpringUtils.getBean(FaxConfigDao.class);
	private static FaxManager faxManager = SpringUtils.getBean(FaxManager.class);
	private static ClinicDAO clinicDAO = SpringUtils.getBean(ClinicDAO.class);
    
	public EctConsultationFormFaxAction() {
	}
	    
    @Override
    public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response){
        
    	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
    	
    	if(!securityInfoManager.hasPrivilege(loggedInInfo, "_con", "r", null)) {
			throw new SecurityException("missing required security object (_con)");
		}
    	
    	EctConsultationFaxForm ectConsultationFaxForm = (EctConsultationFaxForm) form;
    	ectConsultationFaxForm.setRequest(request);
	   	String reqId = ectConsultationFaxForm.getRequestId();
		String demoNo = ectConsultationFaxForm.getDemographicNo();
		String faxNumber = ectConsultationFaxForm.getSendersFax();
		String consultResponsePage = request.getParameter("consultResponsePage");
		boolean doCoverPage = ectConsultationFaxForm.isCoverpage();
		String note = "";
		if( doCoverPage ) {
			note = request.getParameter("note") == null ? "" : request.getParameter("note");
			// dont ask!
			if (note.isEmpty()) {
				note = ectConsultationFaxForm.getComments();
			}
		}
		FaxAccount sender = ectConsultationFaxForm.getSender();
		Clinic clinic = clinicDAO.getClinic();
		sender.setSubText(clinic.getClinicName());
		sender.setAddress(clinic.getClinicAddress());
		sender.setFacilityName(clinic.getClinicName());

		ArrayList<EDoc> docs;
		if (consultResponsePage==null) {
			docs = EDocUtil.listDocs(loggedInInfo, demoNo, reqId, EDocUtil.ATTACHED);
		} else {
			docs = EDocUtil.listResponseDocs(loggedInInfo, demoNo, reqId, EDocUtil.ATTACHED);
		}
		
		String path = OscarProperties.getInstance().getProperty("DOCUMENT_DIR");
		if(! path.endsWith(File.separator))
		{
			path = path + File.separator;
		}
		ArrayList<Object> pdfDocumentList = new ArrayList<Object>();
		byte[] buffer;
		ByteInputStream bis;
		ByteOutputStream bos;
		CommonLabResultData consultLabs = new CommonLabResultData();
		ArrayList<InputStream> streams = new ArrayList<InputStream>();
		String provider_no = loggedInInfo.getLoggedInProviderNo();		

		ArrayList<LabResultData> labs;
		if (consultResponsePage==null) {
			labs = consultLabs.populateLabResultsData(loggedInInfo, demoNo, reqId, CommonLabResultData.ATTACHED);
		} else {
			labs = consultLabs.populateLabResultsDataConsultResponse(loggedInInfo, demoNo, reqId, CommonLabResultData.ATTACHED);
		}
		
		String error = "";
		Exception exception = null;
		try {

			if (consultResponsePage==null) { //fax for consultation request
				bos = new ByteOutputStream();
				// dont ask! Didnt want to break anything related.
				request.setAttribute("reqId", reqId);				
				ConsultationPDFCreator cpdfc = new ConsultationPDFCreator(ectConsultationFaxForm, bos);
				cpdfc.printPdf(loggedInInfo);
				
				buffer = bos.getBytes();
				bis = new ByteInputStream(buffer, bos.getCount());
				bos.close();
				streams.add(bis);
				pdfDocumentList.add(bis);
			}
			else { //fax for consultation response
				String consultRespoonsePDF = ConsultResponsePDFCreator.create(consultResponsePage);
				pdfDocumentList.add(consultRespoonsePDF);
			}

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
						pdfDocumentList.add(bis);
						
					}
					else if (doc.isPDF()) {
						pdfDocumentList.add(path + doc.getFileName());
					}
					else {
						logger.error("EctConsultationFormRequestPrintAction: " + doc.getType() + " is marked as printable but no means have been established to print it.");	
					}
				}
			}

			// Iterating over requested labs.
			for (int i = 0; labs != null && i < labs.size(); i++) {
				// Storing the lab in PDF format inside a byte stream.
				bos = new ByteOutputStream();
				request.setAttribute("segmentID", labs.get(i).segmentID);
				LabPDFCreator lpdfc = new LabPDFCreator(request, bos);
				lpdfc.printPdf();

				// Transferring PDF to an input stream to be concatenated with
				// the rest of the documents.
				buffer = bos.getBytes();
				bis = new ByteInputStream(buffer, bos.getCount());
				bos.close();
				streams.add(bis);
				pdfDocumentList.add(bis);

			}
			
			if (pdfDocumentList.size() > 0) {
 
				// Writing consultation request to disk as a pdf.
				String faxPath = path;
				String filename = "Consult_" + reqId + System.currentTimeMillis() + ".pdf";
//				String faxPdf = String.format("%s%s%s", faxPath, File.separator, filename);
				Path faxPdf = Paths.get(faxPath, filename);
				try(FileOutputStream pdfOut = new FileOutputStream(faxPdf.toString())) {
					ConcatPDF.concat(pdfDocumentList, pdfOut);
				}

				Path pdfToFax;
				List<FaxConfig> faxConfigs = faxConfigDao.findAll(null, null);
				boolean validFaxNumber;
				int count = 0;
				Set<FaxRecipient> faxRecipients = ectConsultationFaxForm.getAllFaxRecipients();
				for (FaxRecipient faxRecipient : faxRecipients) {

					// reset target pdf.
					pdfToFax = faxPdf;

				    String faxNo = faxRecipient.getFax();
				    
				    if(faxNo == null) {
				    	faxNo = "";
				    }

				    if (faxNo.length() < 7) { 
				    	throw new DocumentException("Document target fax number '"+faxNo+"' is invalid."); 
				    }
				    
				    faxNo = faxNo.trim().replaceAll("\\D", "");
				    
				    logger.info("Setting up fax to: " + faxRecipient.getName() + " at " + faxRecipient.getFax());
				
				    validFaxNumber = false;
				    
				    FaxJob faxJob = new FaxJob();
		    		faxJob.setDestination(faxNo);
		    		faxJob.setRecipient(faxRecipient.getName());
		    		faxJob.setFax_line(faxNumber);
		    		faxJob.setStamp(new Date());
		    		faxJob.setOscarUser(provider_no);
		    		faxJob.setDemographicNo(Integer.parseInt(demoNo));
				    
				    inner : for( FaxConfig faxConfig : faxConfigs ) {
				    	if( faxConfig.getFaxNumber().equals(faxNumber) ) {
				    						    		
				    		faxJob.setStatus(FaxJob.STATUS.WAITING);
				    		faxJob.setUser(faxConfig.getFaxUser());
							sender.setFaxNumberOwner(faxConfig.getAccountName());
				    		validFaxNumber = true;
				    		break inner;
				    	}
				    }
				    
				    if( !validFaxNumber ) {
				    	
				    	faxJob.setStatus(FaxJob.STATUS.ERROR);
				    	faxJob.setStatusString("Document outgoing fax number '"+faxNumber+"' is invalid.");
				    	logger.error("PROBLEM CREATING FAX JOB", new DocumentException("Document outgoing fax number '"+faxNumber+"' is invalid."));
				    }
				    else {
				    	// redundant, but, what the heck!
				    	faxJob.setStatus(FaxJob.STATUS.WAITING);
				    }

					//todo rethink this process.  It takes up too much disc space.
					if( doCoverPage ) {
						pdfToFax = faxManager.addCoverPage(loggedInInfo, note, faxRecipient, sender, faxPdf);

						// delete the source file to save some disc space
						if(count == (faxRecipients.size() -1)) {
							Files.deleteIfExists(faxPdf);
						}
					}

					int numPages = EDocUtil.getPDFPageCount(pdfToFax.toString());

					faxJob.setFile_name(pdfToFax.getFileName().toString());
					faxJob.setNumPages(numPages);

				    faxJobDao.persist(faxJob);
				    
				    // start up a log track each time the CLIENT was run.
					FaxClientLog faxClientLog = new FaxClientLog();
					faxClientLog.setFaxId(faxJob.getId()); // IMPORTANT! this is the id of the FaxJobID from the Faxes table. A 1:1 cardinality.
					faxClientLog.setProviderNo(faxJob.getOscarUser()); // the provider that sent this fax
					faxClientLog.setStartTime(new Date(System.currentTimeMillis())); // the exact time the fax was sent
					faxClientLog.setRequestId(Integer.parseInt(reqId));
					faxClientLogDao.persist(faxClientLog);

					count++;
				}

				LogAction.addLog(provider_no, LogConst.SENT, LogConst.CON_FAX, "CONSULT "+ reqId);
				request.setAttribute("faxSuccessful", true);
				return mapping.findForward("success");
			}

		} catch (DocumentException de) {
			error = "DocumentException";
			exception = de;
		} catch (IOException ioe) {
			error = "IOException";
			exception = ioe;
		} catch (com.lowagie.text.DocumentException e) {
			logger.error("error", e);
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
