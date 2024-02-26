/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


package oscar.eform.actions;

import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionRedirect;

import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.enumerator.DocumentType;
import org.oscarehr.documentManager.DocumentAttachmentManager;
import org.oscarehr.managers.DemographicManager;

import org.oscarehr.managers.EformDataManager;
import org.oscarehr.managers.FaxManager.TransactionType;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.match.IMatchManager;
import org.oscarehr.match.MatchManager;
import org.oscarehr.match.MatchManagerException;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.PDFGenerationException;
import org.oscarehr.util.SpringUtils;

import oscar.eform.EFormLoader;
import oscar.eform.EFormUtil;
import oscar.eform.data.DatabaseAP;
import oscar.eform.data.EForm;
import oscar.oscarEncounter.data.EctProgram;
import oscar.util.StringUtils;


public class AddEFormAction extends Action {

	private static final Logger logger=MiscUtils.getLogger();
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	private EformDataManager eformDataManager = SpringUtils.getBean( EformDataManager.class );
	private DocumentAttachmentManager documentAttachmentManager = SpringUtils.getBean(DocumentAttachmentManager.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_eform", "w", null)) {
			throw new SecurityException("missing required security object (_eform)");
		}
		
		logger.debug("==================SAVING ==============");
		HttpSession se = request.getSession();

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

		boolean fax = "true".equals(request.getParameter("faxEForm"));
		boolean print = "true".equals(request.getParameter("print"));
		boolean saveAsEdoc = "true".equals( request.getParameter("saveAsEdoc") );
		boolean isDownloadEForm = "true".equals(request.getParameter("saveAndDownloadEForm"));

		@SuppressWarnings("unchecked")
		Enumeration<String> paramNamesE = request.getParameterNames();
		//for each name="fieldname" value="myval"
		ArrayList<String> paramNames = new ArrayList<String>();  //holds "fieldname, ...."
		ArrayList<String> paramValues = new ArrayList<String>(); //holds "myval, ...."
		String fid = request.getParameter("efmfid");
		String demographic_no = request.getParameter("efmdemographic_no");
		String eform_link = request.getParameter("eform_link");
		String subject = request.getParameter("subject");
		
		/*
		 * An eform developer may add these to the eForm in order to auto 
		 * populate fax information. 
		 */
		String recipient = request.getParameter("recipient");
		String recipientFaxNumber = request.getParameter("recipientFaxNumber");
		String letterheadFax = request.getParameter("letterheadFax");

		boolean doDatabaseUpdate = false;

		List<String> oscarUpdateFields = new ArrayList<String>();

		if (request.getParameter("_oscardodatabaseupdate") != null && request.getParameter("_oscardodatabaseupdate").equalsIgnoreCase("on"))
			doDatabaseUpdate = true;

		ActionMessages updateErrors = new ActionMessages();

		// The fields in the _oscarupdatefields parameter are separated by %s.
		if (!print && !fax && doDatabaseUpdate && request.getParameter("_oscarupdatefields") != null) {

			oscarUpdateFields = Arrays.asList(request.getParameter("_oscarupdatefields").split("%"));

			boolean validationError = false;

			for (String field : oscarUpdateFields) {
				EFormLoader.getInstance();
				// Check for existence of appropriate databaseap
				DatabaseAP currentAP = EFormLoader.getAP(field);
				if (currentAP != null) {
					if (!currentAP.isInputField()) {
						// Abort! This field can't be updated
						updateErrors.add(field, new ActionMessage("errors.richeForms.noInputMethodError", field));
						validationError = true;
					}
				} else {
					// Field doesn't exit
					updateErrors.add(field, new ActionMessage("errors.richeForms.noSuchFieldError", field));
					validationError = true;
				}
			}

			if (!validationError) {
				for (String field : oscarUpdateFields) {
					EFormLoader.getInstance();
					DatabaseAP currentAP = EFormLoader.getAP(field);
					// We can add more of these later...
					if (currentAP != null) {
						String inSQL = currentAP.getApInSQL();

						inSQL = DatabaseAP.parserReplace("demographic", demographic_no, inSQL);
						inSQL = DatabaseAP.parserReplace("provider", providerNo, inSQL);
						inSQL = DatabaseAP.parserReplace("fid", fid, inSQL);

						inSQL = DatabaseAP.parserReplace("value", request.getParameter(field), inSQL);

						//if(currentAP.getArchive() != null && currentAP.getArchive().equals("demographic")) {
						//	demographicArchiveDao.archiveRecord(demographicManager.getDemographic(loggedInInfo,demographic_no));
						//}

						// Run the SQL query against the database
						//TODO: do this a different way.
						MiscUtils.getLogger().error("Error",new Exception("EForm is using disabled functionality for updating fields..update not performed"));
					}
				}
			}
		}

		if (subject == null) subject="";
		String curField = "";
		while (paramNamesE.hasMoreElements()) {
			curField = paramNamesE.nextElement();
			if( curField.equalsIgnoreCase("parentAjaxId"))
				continue;
			if(request.getParameter(curField) != null && (!request.getParameter(curField).trim().equals("")) )
			{
				paramNames.add(curField);
				paramValues.add(request.getParameter(curField));
			}
			
		}

		EForm curForm = new EForm(fid, demographic_no, providerNo);

		//add eform_link value from session attribute
		ArrayList<String> openerNames = curForm.getOpenerNames();
		ArrayList<String> openerValues = new ArrayList<String>();
		for (String name : openerNames) {
			String lnk = providerNo+"_"+demographic_no+"_"+fid+"_"+name;
			String val = (String)se.getAttribute(lnk);
			openerValues.add(val);
			if (val!=null) se.removeAttribute(lnk);
		}

		//----names parsed
		ActionMessages errors = curForm.setMeasurements(paramNames, paramValues);
		curForm.setFormSubject(subject);
		curForm.setValues(paramNames, paramValues);
		if (!openerNames.isEmpty()) curForm.setOpenerValues(openerNames, openerValues);
		if (eform_link!=null) curForm.setEformLink(eform_link);
		curForm.setImagePath();
		curForm.setAction();
		curForm.setNowDateTime();
		if (!errors.isEmpty()) {
			saveErrors(request, errors);
			request.setAttribute("curform", curForm);
			request.setAttribute("page_errors", "true");
			return mapping.getInputForward();
		}

		//Check if eform same as previous, if same -> not saved
		String prev_fdid = (String)se.getAttribute("eform_data_id");
		se.removeAttribute("eform_data_id");
		boolean sameform = false;
		if (StringUtils.filled(prev_fdid)) {
			EForm prevForm = new EForm(prev_fdid);
			if (prevForm!=null) {
				sameform = curForm.getFormHtml().equals(prevForm.getFormHtml());
			}			
		}
		if (!sameform) { //save eform data
			
			String fdid = eformDataManager.saveEformData( loggedInInfo, curForm ) + "";

			EFormUtil.addEFormValues(paramNames, paramValues, new Integer(fdid), new Integer(fid), new Integer(demographic_no)); //adds parsed values

			String[] attachedDocuments = (request.getParameterValues("docNo") != null ? request.getParameterValues("docNo") : new String[0]);
			String[] attachedLabs = (request.getParameterValues("labNo") != null ? request.getParameterValues("labNo") : new String[0]);
			String[] attachedForms = (request.getParameterValues("formNo") != null ? request.getParameterValues("formNo") : new String[0]);
			String[] attachedEForms = (request.getParameterValues("eFormNo") != null ? request.getParameterValues("eFormNo") : new String[0]);
			String[] attachedHRMDocuments = (request.getParameterValues("hrmNo") != null ? request.getParameterValues("hrmNo") : new String[0]);

			documentAttachmentManager.attachToEForm(loggedInInfo, DocumentType.DOC, attachedDocuments, providerNo, Integer.valueOf(fdid), Integer.valueOf(demographic_no));
			documentAttachmentManager.attachToEForm(loggedInInfo, DocumentType.LAB, attachedLabs, providerNo, Integer.valueOf(fdid), Integer.valueOf(demographic_no));
			documentAttachmentManager.attachToEForm(loggedInInfo, DocumentType.FORM, attachedForms, providerNo, Integer.valueOf(fdid), Integer.valueOf(demographic_no));
			documentAttachmentManager.attachToEForm(loggedInInfo, DocumentType.EFORM, attachedEForms, providerNo, Integer.valueOf(fdid), Integer.valueOf(demographic_no));
			documentAttachmentManager.attachToEForm(loggedInInfo, DocumentType.HRM, attachedHRMDocuments, providerNo, Integer.valueOf(fdid), Integer.valueOf(demographic_no));

			//post fdid to {eform_link} attribute
			if (eform_link!=null) {
				se.setAttribute(eform_link, fdid);
			}

			request.setAttribute("fdid", fdid);
			request.setAttribute("demographicId", demographic_no);

			if(saveAsEdoc) {
				try {
					documentAttachmentManager.saveEFormAsEDoc(request, response);
				} catch (PDFGenerationException e) {
					logger.error(e.getMessage(), e);
					String errorMessage = "This eForm (and attachments, if applicable) could not be added to this patient’s documents. \\n\\n" + e.getMessage();
					request.setAttribute("errorMessage", errorMessage);
					return mapping.findForward("error");
				}				
			}
			
			if (fax) {
				ActionRedirect faxForward = new ActionRedirect(mapping.findForward("fax"));
				faxForward.addParameter("method", "prepareFax");
				faxForward.addParameter("transactionId", fdid);
				faxForward.addParameter("transactionType", TransactionType.EFORM.name());
				faxForward.addParameter("demographicNo", demographic_no);
				
				/*
				 * Added incase the eForm developer adds these elements to the 
				 * eform.
				 */
				faxForward.addParameter("recipient", recipient);
				faxForward.addParameter("recipientFaxNumber", recipientFaxNumber);
				faxForward.addParameter("letterheadFax", letterheadFax);
				return faxForward;
			}
			
			else if (print) {
				return(mapping.findForward("print"));
			}

			else if (isDownloadEForm) {
				/*
				 * For now, this download code is added here and will be moved to the appropriate place after refactoring is done.
				 */
				ActionForward printForward = mapping.findForward("download");
				String path = printForward.getPath() + "?fdid=" + fdid + "&parentAjaxId=eforms";
				printForward = new ActionForward(path);

				String fileName = generateFileName(loggedInInfo, Integer.parseInt(demographic_no));
				String pdfBase64 = "";
				try {
					Path eFormPdfPath = documentAttachmentManager.renderEFormWithAttachments(request, response);
					pdfBase64 = documentAttachmentManager.convertPDFToBase64(eFormPdfPath);
				} catch (PDFGenerationException e) {
					logger.error(e.getMessage(), e);
					String errorMessage = "This eForm (and attachments, if applicable) could not be downloaded. \\n\\n" + e.getMessage();
					request.setAttribute("errorMessage", errorMessage);
					return mapping.findForward("error");
				}

				request.setAttribute("eFormPDF", pdfBase64);
				request.setAttribute("eFormPDFName", fileName);
				request.setAttribute("isDownload", "true");

				return printForward;
			}

			else {
				//write template message to echart
				String program_no = new EctProgram(se).getProgram(providerNo);
				String path = request.getRequestURL().toString();
				String uri = request.getRequestURI();
				path = path.substring(0, path.indexOf(uri));
				path += request.getContextPath();
	
				EFormUtil.writeEformTemplate(LoggedInInfo.getLoggedInInfoFromSession(request),paramNames, paramValues, curForm, fdid, program_no, path);
			}
			
		}
		else {
			logger.debug("Warning! Form HTML exactly the same, new form data not saved.");
			request.setAttribute("fdid", prev_fdid);
			request.setAttribute("demographicId", demographic_no);

			if (fax) {
				/*
				 * This form id is sent to the fax action to render it as a faxable PDF.
				 * A preview is returned to the user once the form is rendered.
				 */
				ActionRedirect faxForward = new ActionRedirect(mapping.findForward("fax"));
				faxForward.addParameter("method", "prepareFax");
				faxForward.addParameter("transactionId", prev_fdid);
				faxForward.addParameter("transactionType", TransactionType.EFORM.name());
				faxForward.addParameter("demographicNo", demographic_no);
				
				/*
				 * Added incase the eForm developer adds these elements to the 
				 * eform.
				 */
				faxForward.addParameter("recipient", recipient);
				faxForward.addParameter("recipientFaxNumber", recipientFaxNumber);
				faxForward.addParameter("letterheadFax", letterheadFax);
				return faxForward;
			}
			
			else if (print) {
				return(mapping.findForward("print"));
			}

			else if (isDownloadEForm) {
				/*
				 * For now, this download code is added here and will be moved to the appropriate place after refactoring is done.
				 */
				ActionForward printForward = mapping.findForward("download");
				String path = printForward.getPath() + "?fdid=" + prev_fdid + "&parentAjaxId=eforms";
				printForward = new ActionForward(path);

				String fileName = generateFileName(loggedInInfo, Integer.parseInt(demographic_no));
				String pdfBase64 = "";
				try {
					Path eFormPdfPath = documentAttachmentManager.renderEFormWithAttachments(request, response);
					pdfBase64 = documentAttachmentManager.convertPDFToBase64(eFormPdfPath);
				} catch (PDFGenerationException e) {
					logger.error(e.getMessage(), e);
					String errorMessage = "This eForm (and attachments, if applicable) could not be downloaded. \\n\\n" + e.getMessage();
					request.setAttribute("errorMessage", errorMessage);
					return mapping.findForward("error");
				}

				request.setAttribute("eFormPDF", pdfBase64);
				request.setAttribute("eFormPDFName", fileName);
				request.setAttribute("isDownload", "true");

				return printForward;
			}
			
			if(saveAsEdoc) {
				try {
					documentAttachmentManager.saveEFormAsEDoc(request, response);
				} catch (PDFGenerationException e) {
					logger.error(e.getMessage(), e);
					String errorMessage = "This eForm (and attachments, if applicable) could not be added to this patient’s documents. \\n\\n" + e.getMessage();
					request.setAttribute("errorMessage", errorMessage);
					return mapping.findForward("error");
				}				
			}
		}
		
		if (demographic_no != null) {
			IMatchManager matchManager = new MatchManager();
			DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);
			Demographic client = demographicManager.getDemographic(loggedInInfo,demographic_no);
			try {
	            matchManager.<Demographic>processEvent(client, IMatchManager.Event.CLIENT_CREATED);
            } catch (MatchManagerException e) {
            	MiscUtils.getLogger().error("Error while processing MatchManager.processEvent(Client)",e);
            }
		}
		

		return(mapping.findForward("close"));
	}
	
	private String generateFileName(LoggedInInfo loggedInInfo, int demographicNo) {
		DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);
		String demographicLastName = demographicManager.getDemographicFormattedName(loggedInInfo, demographicNo).split(", ")[0];

		Date currentDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
		String formattedDate = dateFormat.format(currentDate);

		return formattedDate + "_" + demographicLastName + ".pdf";
	}

}
