//CHECKSTYLE:OFF
/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 * <p>
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


package oscar.eform.actions;

import org.apache.logging.log4j.Logger;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.EmailConfig;
import org.oscarehr.common.model.EmailConfig.EmailProvider;
import org.oscarehr.common.model.EmailConfig.EmailType;
import org.oscarehr.common.model.enumerator.DocumentType;
import org.oscarehr.documentManager.DocumentAttachmentManager;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.managers.EformDataManager;
import org.oscarehr.managers.EmailManager;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;


import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

public class AddEForm2Action extends ActionSupport {
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();


    private static final Logger logger = MiscUtils.getLogger();
    private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
    private EformDataManager eformDataManager = SpringUtils.getBean(EformDataManager.class);
    private DocumentAttachmentManager documentAttachmentManager = SpringUtils.getBean(DocumentAttachmentManager.class);
    private EmailManager emailManager = SpringUtils.getBean(EmailManager.class);

    public String execute() {

        if (!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_eform", "w", null)) {
            throw new SecurityException("missing required security object (_eform)");
        }

        logger.debug("==================SAVING ==============");
        HttpSession se = request.getSession();

        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
        String providerNo = loggedInInfo.getLoggedInProviderNo();

        boolean fax = "true".equals(request.getParameter("faxEForm"));
        boolean print = "true".equals(request.getParameter("print"));
        boolean saveAsEdoc = "true".equals(request.getParameter("saveAsEdoc"));
        boolean isDownloadEForm = "true".equals(request.getParameter("saveAndDownloadEForm"));
        boolean isEmailEForm = "true".equals(request.getParameter("emailEForm"));

        String[] attachedDocuments = (request.getParameterValues("docNo") != null ? request.getParameterValues("docNo") : new String[0]);
        String[] attachedLabs = (request.getParameterValues("labNo") != null ? request.getParameterValues("labNo") : new String[0]);
        String[] attachedForms = (request.getParameterValues("formNo") != null ? request.getParameterValues("formNo") : new String[0]);
        String[] attachedEForms = (request.getParameterValues("eFormNo") != null ? request.getParameterValues("eFormNo") : new String[0]);
        String[] attachedHRMDocuments = (request.getParameterValues("hrmNo") != null ? request.getParameterValues("hrmNo") : new String[0]);

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
         * Part 2 of "counter hack for a hack" initialized in Javascript file
         * eform_floating_toolbar.js
         */
        String[] imagePathPlaceHolders = request.getParameterValues("openosp-image-link");

        /*
         * An eform developer may add these to the eForm in order to auto
         * populate fax information.
         */
        String recipient = request.getParameter("recipient");
        String recipientFaxNumber = request.getParameter("recipientFaxNumber");
        String letterheadFax = request.getParameter("letterheadFax");

        boolean doDatabaseUpdate = false;
        List<String> oscarUpdateFields = new ArrayList<>();
        if (request.getParameter("_oscardodatabaseupdate") != null && request.getParameter("_oscardodatabaseupdate").equalsIgnoreCase("on")) {
            doDatabaseUpdate = true;
        }

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
                        addActionError(getText("errors.richeForms.noInputMethodError", new String[]{field}));
                        validationError = true;
                    }
                } else {
                    // Field doesn't exit
                    addActionError(getText("errors.richeForms.noSuchFieldError", new String[]{field}));
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
                        MiscUtils.getLogger().error("Error", new Exception("EForm is using disabled functionality for updating fields..update not performed"));
                    }
                }
            }
        }

        if (subject == null) subject = "";
        String curField = "";
        while (paramNamesE.hasMoreElements()) {
            curField = paramNamesE.nextElement();
            if (curField.equalsIgnoreCase("parentAjaxId")) {
                continue;
            }

            /*
             * Remove these parameters from the request after the imagePathPlaceHolders variable is set.
             * These values do not need to be saved into the eform_values table.
             */
            if (curField.equalsIgnoreCase("openosp-image-link")) {
                continue;
            }

            if (request.getParameter(curField) != null && (!request.getParameter(curField).trim().equals(""))) {
                paramNames.add(curField);
                paramValues.add(request.getParameter(curField));
            }

        }

        EForm curForm = new EForm(fid, demographic_no, providerNo);

        //add eform_link value from session attribute
        ArrayList<String> openerNames = curForm.getOpenerNames();
        ArrayList<String> openerValues = new ArrayList<String>();
        for (String name : openerNames) {
            String lnk = providerNo + "_" + demographic_no + "_" + fid + "_" + name;
            String val = (String) se.getAttribute(lnk);
            openerValues.add(val);
            if (val != null) se.removeAttribute(lnk);
        }

        //----names parsed
        //ActionMessages errors = curForm.setMeasurements(paramNames, paramValues);
        curForm.setFormSubject(subject);
        curForm.setValues(paramNames, paramValues);
        if (!openerNames.isEmpty()) curForm.setOpenerValues(openerNames, openerValues);
        if (eform_link != null) curForm.setEformLink(eform_link);
        curForm.setImagePath();
        curForm.setAction();
        curForm.setNowDateTime();
//TODO        if (!errors.isEmpty()) {
//            saveErrors(request, errors);
//            request.setAttribute("curform", curForm);
//            request.setAttribute("page_errors", "true");
//            return mapping.getInputForward();
//        }

        //Check if eform same as previous, if same -> not saved
        String prev_fdid = (String) se.getAttribute("eform_data_id");
        se.removeAttribute("eform_data_id");
        boolean sameform = false;
        if (StringUtils.filled(prev_fdid)) {
            EForm prevForm = new EForm(prev_fdid);
            if (prevForm != null) {
                sameform = curForm.getFormHtml().equals(prevForm.getFormHtml());
            }
        }
        if (!sameform) { //save eform data

            /*
             * Part 2 of "counter hack for a hack" initialized in Javascript file
             * eform_floating_toolbar.js
             * Grab the image path placeholders from the form submission and then
             * feed them into the EForm object.
             * Doing this ensures the image links get saved correctly into the HTML
             * of the eform_data database table.
             */
            curForm.addImagePathPlaceholders(imagePathPlaceHolders);

            String fdid = eformDataManager.saveEformData(loggedInInfo, curForm) + "";

            EFormUtil.addEFormValues(paramNames, paramValues, new Integer(fdid), new Integer(fid), new Integer(demographic_no)); //adds parsed values

            attachToEForm(loggedInInfo, attachedEForms, attachedDocuments, attachedLabs, attachedHRMDocuments, attachedForms, fdid, demographic_no, providerNo);

            //post fdid to {eform_link} attribute
            if (eform_link != null) {
                se.setAttribute(eform_link, fdid);
            }

            request.setAttribute("fdid", fdid);
            request.setAttribute("demographicId", demographic_no);

            if (saveAsEdoc) {
                try {
                    documentAttachmentManager.saveEFormAsEDoc(request, response);
                } catch (PDFGenerationException e) {
                    logger.error(e.getMessage(), e);
                    String errorMessage = "This eForm (and attachments, if applicable) could not be added to this patient’s documents. \\n\\n" + e.getMessage();
                    request.setAttribute("errorMessage", errorMessage);
                    return "error";
                }
            }

            if (fax) {
                StringBuilder faxForward = new StringBuilder("/fax/faxAction.do");
                faxForward.append("method=").append("prepareFax");
                faxForward.append("transactionId=").append(fdid);
                faxForward.append("transactionType=").append(TransactionType.EFORM.name());
                faxForward.append("demographicNo=").append(demographic_no);

                /*
                 * Added incase the eForm developer adds these elements to the
                 * eform.
                 */
                faxForward.append("recipient=").append(recipient);
                faxForward.append("recipientFaxNumber=").append(recipientFaxNumber);
                faxForward.append("letterheadFax=").append(letterheadFax);
                try {
                    response.sendRedirect(faxForward.toString());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return NONE;
            } else if (print) {
                return "print";
            } else if (isDownloadEForm) {
                /*
                 * For now, this download code is added here and will be moved to the appropriate place after refactoring is done.
                 */
                String path = request.getContextPath() + "/eform/efmshowform_data.jsp?fdid=" + fdid + "&parentAjaxId=eforms";
                String fileName = generateFileName(loggedInInfo, Integer.parseInt(demographic_no));
                String pdfBase64 = "";
                try {
                    Path eFormPdfPath = documentAttachmentManager.renderEFormWithAttachments(request, response);
                    pdfBase64 = documentAttachmentManager.convertPDFToBase64(eFormPdfPath);
                } catch (PDFGenerationException e) {
                    logger.error(e.getMessage(), e);
                    String errorMessage = "This eForm (and attachments, if applicable) could not be downloaded. \\n\\n" + e.getMessage();
                    request.setAttribute("errorMessage", errorMessage);
                    return "error";
                }

                request.setAttribute("eFormPDF", pdfBase64);
                request.setAttribute("eFormPDFName", fileName);
                request.setAttribute("isDownload", "true");

                try {
                    response.sendRedirect(path);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return NONE;
            } else if (isEmailEForm) {
                String path = "/email/emailComposeAction.do?method=prepareComposeEFormMailer";
                addEmailAttachments(request, attachedEForms, attachedDocuments, attachedLabs, attachedHRMDocuments, attachedForms);
                try {
                    response.sendRedirect(path);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return NONE;
            } else {
                //write template message to echart
                String program_no = new EctProgram(se).getProgram(providerNo);
                String path = request.getRequestURL().toString();
                String uri = request.getRequestURI();
                path = path.substring(0, path.indexOf(uri));
                path += request.getContextPath();

                EFormUtil.writeEformTemplate(LoggedInInfo.getLoggedInInfoFromSession(request), paramNames, paramValues, curForm, fdid, program_no, path);
            }

        } else {
            logger.debug("Warning! Form HTML exactly the same, new form data not saved.");
            request.setAttribute("fdid", prev_fdid);
            request.setAttribute("demographicId", demographic_no);

            attachToEForm(loggedInInfo, attachedEForms, attachedDocuments, attachedLabs, attachedHRMDocuments, attachedForms, prev_fdid, demographic_no, providerNo);

            if (fax) {
                /*
                 * This form id is sent to the fax action to render it as a faxable PDF.
                 * A preview is returned to the user once the form is rendered.
                 */
                StringBuilder faxForward = new StringBuilder("/fax/faxAction.do");
                faxForward.append("method=").append("prepareFax");
                faxForward.append("transactionId=").append(prev_fdid);
                faxForward.append("transactionType=").append(TransactionType.EFORM.name());
                faxForward.append("demographicNo=").append(demographic_no);

                /*
                 * Added incase the eForm developer adds these elements to the
                 * eform.
                 */
                faxForward.append("recipient=").append(recipient);
                faxForward.append("recipientFaxNumber=").append(recipientFaxNumber);
                faxForward.append("letterheadFax=").append(letterheadFax);
                try {
                    response.sendRedirect(faxForward.toString());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return NONE;
            } else if (print) {
                return "print";
            } else if (isDownloadEForm) {
                /*
                 * For now, this download code is added here and will be moved to the appropriate place after refactoring is done.
                 */
                String path = request.getContextPath() + "/eform/efmshowform_data.jsp?fdid=" + prev_fdid + "&parentAjaxId=eforms";
                String fileName = generateFileName(loggedInInfo, Integer.parseInt(demographic_no));
                String pdfBase64 = "";
                try {
                    Path eFormPdfPath = documentAttachmentManager.renderEFormWithAttachments(request, response);
                    pdfBase64 = documentAttachmentManager.convertPDFToBase64(eFormPdfPath);
                } catch (PDFGenerationException e) {
                    logger.error(e.getMessage(), e);
                    String errorMessage = "This eForm (and attachments, if applicable) could not be downloaded. \\n\\n" + e.getMessage();
                    request.setAttribute("errorMessage", errorMessage);
                    return "error";
                }

                request.setAttribute("eFormPDF", pdfBase64);
                request.setAttribute("eFormPDFName", fileName);
                request.setAttribute("isDownload", "true");

                try {
                    response.sendRedirect(path);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return NONE;
            } else if (isEmailEForm) {
                String path = "/email/emailComposeAction.do?method=prepareComposeEFormMailer";
                addEmailAttachments(request, attachedEForms, attachedDocuments, attachedLabs, attachedHRMDocuments, attachedForms);
                try {
                    response.sendRedirect(path);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return NONE;
            }

            if (saveAsEdoc) {
                try {
                    documentAttachmentManager.saveEFormAsEDoc(request, response);
                } catch (PDFGenerationException e) {
                    logger.error(e.getMessage(), e);
                    String errorMessage = "This eForm (and attachments, if applicable) could not be added to this patient’s documents. \\n\\n" + e.getMessage();
                    request.setAttribute("errorMessage", errorMessage);
                    return "error";
                }
            }
        }

        if (demographic_no != null) {
            IMatchManager matchManager = new MatchManager();
            DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);
            Demographic client = demographicManager.getDemographic(loggedInInfo, demographic_no);
            try {
                matchManager.<Demographic>processEvent(client, IMatchManager.Event.CLIENT_CREATED);
            } catch (MatchManagerException e) {
                MiscUtils.getLogger().error("Error while processing MatchManager.processEvent(Client)", e);
            }
		}

        String fdid = (String) request.getAttribute("fdid");
        String path = request.getContextPath() + "/eform/efmshowform_data.jsp?fdid=" + fdid + "&parentAjaxId=eforms";

		String pdfBase64;
		try {
			Path eFormPdfPath = documentAttachmentManager.renderEFormWithAttachments(request, response);
			pdfBase64 = documentAttachmentManager.convertPDFToBase64(eFormPdfPath);
		} catch (PDFGenerationException e) {
			logger.error(e.getMessage(), e);
			String errorMessage = "This eForm (and attachments, if applicable) could not be downloaded. \\n\\n" + e.getMessage();
			request.setAttribute("errorMessage", errorMessage);
			return "error";
		}

		request.setAttribute("eFormPDF", pdfBase64);
		request.setAttribute("eFormPDFName", generateFileName(loggedInInfo, Integer.parseInt(demographic_no)));
		request.setAttribute("isSuccess_Autoclose", "true");

        try {
            response.sendRedirect(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return NONE;
	}
	
	private String generateFileName(LoggedInInfo loggedInInfo, int demographicNo) {
		DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);
		String demographicLastName = demographicManager.getDemographicFormattedName(loggedInInfo, demographicNo).split(", ")[0];

        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
        String formattedDate = dateFormat.format(currentDate);

        return formattedDate + "_" + demographicLastName + ".pdf";
    }

    private void addEmailAttachments(HttpServletRequest request, String[] attachedEForms, String[] attachedDocuments, String[] attachedLabs, String[] attachedHRMDocuments, String[] attachedForms) {
        Boolean attachEFormItSelf = request.getParameter("attachEFormToEmail") == null || "true".equals(request.getParameter("attachEFormToEmail")) ? true : false;
        Boolean openEFormAfterEmail = request.getParameter("openEFormAfterSendingEmail") == null || "false".equals(request.getParameter("openEFormAfterSendingEmail")) ? false : true;
        Boolean isEmailEncrypted = request.getParameter("enableEmailEncryption") == null || "true".equals(request.getParameter("enableEmailEncryption")) ? true : false;
        Boolean isEmailAttachmentEncrypted = request.getParameter("encryptEmailAttachments") == null || "true".equals(request.getParameter("encryptEmailAttachments")) ? true : false;
        Boolean isEmailAutoSend = request.getParameter("autoSendEmail") == null || "false".equals(request.getParameter("autoSendEmail")) ? false : true;
        Boolean deleteEFormAfterEmail = request.getParameter("deleteEFormAfterSendingEmail") == null || "false".equals(request.getParameter("deleteEFormAfterSendingEmail")) ? false : true;
        request.setAttribute("deleteEFormAfterEmail", deleteEFormAfterEmail);
        request.setAttribute("isEmailEncrypted", isEmailEncrypted);
        request.setAttribute("isEmailAttachmentEncrypted", isEmailAttachmentEncrypted);
        request.setAttribute("isEmailAutoSend", isEmailAutoSend);
        request.setAttribute("openEFormAfterEmail", openEFormAfterEmail);
        request.setAttribute("attachEFormItSelf", attachEFormItSelf);
        request.setAttribute("attachedEForms", attachedEForms);
        request.setAttribute("attachedDocuments", attachedDocuments);
        request.setAttribute("attachedLabs", attachedLabs);
        request.setAttribute("attachedHRMDocuments", attachedHRMDocuments);
        request.setAttribute("attachedForms", attachedForms);
    }

    private void attachToEForm(LoggedInInfo loggedInInfo, String[] attachedEForms, String[] attachedDocuments, String[] attachedLabs, String[] attachedHRMDocuments, String[] attachedForms, String fdid, String demographic_no, String providerNo) {
        documentAttachmentManager.attachToEForm(loggedInInfo, DocumentType.DOC, attachedDocuments, providerNo, Integer.valueOf(fdid), Integer.valueOf(demographic_no));
        documentAttachmentManager.attachToEForm(loggedInInfo, DocumentType.LAB, attachedLabs, providerNo, Integer.valueOf(fdid), Integer.valueOf(demographic_no));
        documentAttachmentManager.attachToEForm(loggedInInfo, DocumentType.FORM, attachedForms, providerNo, Integer.valueOf(fdid), Integer.valueOf(demographic_no));
        documentAttachmentManager.attachToEForm(loggedInInfo, DocumentType.EFORM, attachedEForms, providerNo, Integer.valueOf(fdid), Integer.valueOf(demographic_no));
        documentAttachmentManager.attachToEForm(loggedInInfo, DocumentType.HRM, attachedHRMDocuments, providerNo, Integer.valueOf(fdid), Integer.valueOf(demographic_no));
    }

}
