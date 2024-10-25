/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

package oscar.oscarEncounter.oscarConsultationRequest.pageUtil;

import com.itextpdf.text.DocumentException;
import org.apache.logging.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.dao.ClinicDAO;
import org.oscarehr.common.dao.FaxConfigDao;
import org.oscarehr.common.dao.FaxJobDao;
import org.oscarehr.common.model.Clinic;
import org.oscarehr.common.model.FaxConfig;
import org.oscarehr.common.model.FaxJob;
import org.oscarehr.documentManager.DocumentAttachmentManager;
import org.oscarehr.documentManager.EDocUtil;
import org.oscarehr.fax.core.FaxAccount;
import org.oscarehr.fax.core.FaxRecipient;
import org.oscarehr.managers.FaxManager;
import org.oscarehr.managers.FaxManager.TransactionType;
import org.oscarehr.managers.NioFileManager;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.PDFGenerationException;
import org.oscarehr.util.SpringUtils;
import oscar.log.LogAction;
import oscar.log.LogConst;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class EctConsultationFormFaxAction extends Action {

	private static final Logger logger = MiscUtils.getLogger();
	private final SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	private final FaxJobDao faxJobDao = SpringUtils.getBean(FaxJobDao.class);
	private final FaxConfigDao faxConfigDao = SpringUtils.getBean(FaxConfigDao.class);
	private final FaxManager faxManager = SpringUtils.getBean(FaxManager.class);
	private final ClinicDAO clinicDAO = SpringUtils.getBean(ClinicDAO.class);

	private final DocumentAttachmentManager documentAttachmentManager = SpringUtils.getBean(DocumentAttachmentManager.class);

	private final NioFileManager nioFileManager = SpringUtils.getBean(NioFileManager.class);

	public EctConsultationFormFaxAction() {
	}
	    
    @Override
    public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response){
        
    	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
    	
    	if(!securityInfoManager.hasPrivilege(loggedInInfo, "_con", "r", null)) {
			throw new SecurityException("missing required security object (_con)");
		}
    	
    	EctConsultationFaxForm ectConsultationFaxForm = (EctConsultationFaxForm) form;

		if ("cancel".equals(ectConsultationFaxForm.getMethod())){
			return mapping.findForward("cancel");
		}

    	ectConsultationFaxForm.setRequest(request);
	   	String reqId = ectConsultationFaxForm.getRequestId();
		String demoNo = ectConsultationFaxForm.getDemographicNo();
		String faxNumber = ectConsultationFaxForm.getSenderFaxNumber();
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

		/*
		 * This is a temporary solution until the fax code is refactored and added to their respective manager classes.
		 */
		String provider_no = loggedInInfo.getLoggedInProviderNo();
		String error = "";
 		Exception exception = null;

		request.setAttribute("reqId", reqId);
		request.setAttribute("demographicId", demoNo);
		Path faxPdf = null;
		try {
			faxPdf = documentAttachmentManager.renderConsultationFormWithAttachments(request, response);
		} catch (PDFGenerationException e) {
			logger.error(e.getMessage(), e);
			String errorMessage = "This fax could not be sent. \\n\\n" + e.getMessage();
			request.setAttribute("errorMessage", errorMessage);
			return mapping.findForward("error");
		}
		String faxPdfPath = nioFileManager.copyFileToOscarDocuments(faxPdf.toString());
		faxPdf = Paths.get(faxPdfPath);
		Path pdfToFax;
		List<FaxConfig> faxConfigs = faxConfigDao.findAll(null, null);
		boolean validFaxNumber;
		int count = 0;
		Set<FaxRecipient> faxRecipients = ectConsultationFaxForm.getAllFaxRecipients();
		try {
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
				faxManager.logFaxJob(loggedInInfo, faxJob, TransactionType.CONSULTATION, Integer.parseInt(reqId));
				// FaxClientLog faxClientLog = new FaxClientLog();
				// faxClientLog.setFaxId(faxJob.getId()); // IMPORTANT! this is the id of the FaxJobID from the Faxes table. A 1:1 cardinality.
				// faxClientLog.setProviderNo(faxJob.getOscarUser()); // the provider that sent this fax
				// faxClientLog.setStartTime(new Date(System.currentTimeMillis())); // the exact time the fax was sent
				// faxClientLog.setRequestId(Integer.parseInt(reqId));
				// faxClientLogDao.persist(faxClientLog);

				count++;
			}

			LogAction.addLog(provider_no, LogConst.SENT, LogConst.CON_FAX, "CONSULT "+ reqId);
			request.setAttribute("faxSuccessful", true);
			return mapping.findForward("success");
		} catch (DocumentException de) {
			error = "DocumentException";
			exception = de;
		} catch (IOException ioe) {
			error = "IOException";
			exception = ioe;
		}
		if (!error.equals("")) {
			logger.error(error + " occured insided ConsultationPrintAction", exception);
			request.setAttribute("printError", new Boolean(true));
			return mapping.findForward("error");
		}
		return null;		
    }
}
