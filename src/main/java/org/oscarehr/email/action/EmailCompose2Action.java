//CHECKSTYLE:OFF
package org.oscarehr.email.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;
import org.oscarehr.common.model.EmailAttachment;
import org.oscarehr.common.model.EmailConfig;
import org.oscarehr.common.model.EmailLog.TransactionType;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.managers.EmailComposeManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.PDFGenerationException;
import org.oscarehr.util.SpringUtils;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

public class EmailCompose2Action extends ActionSupport {
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();

    private static final Logger logger = MiscUtils.getLogger();
    private DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);
    private EmailComposeManager emailComposeManager = SpringUtils.getBean(EmailComposeManager.class);


    public String execute() {
        return prepareComposeEFormMailer();
    }

    public String prepareComposeEFormMailer() {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
        boolean attachEFormItSelf = (boolean) request.getAttribute("attachEFormItSelf");
        String fdid = attachEFormItSelf ? (String) request.getAttribute("fdid") : "";
        String demographicId = (String) request.getAttribute("demographicId");
        String emailPDFPassword = (String) request.getAttribute("emailPDFPassword");
        String emailPDFPasswordClue = (String) request.getAttribute("emailPDFPasswordClue");
        String[] attachedDocuments = (String[]) request.getAttribute("attachedDocuments");
        String[] attachedLabs = (String[]) request.getAttribute("attachedLabs");
        String[] attachedForms = (String[]) request.getAttribute("attachedForms");
        String[] attachedEForms = (String[]) request.getAttribute("attachedEForms");
        String[] attachedHRMDocuments = (String[]) request.getAttribute("attachedHRMDocuments");

        String[] emailConsent = emailComposeManager.getEmailConsentStatus(loggedInInfo, Integer.parseInt(demographicId));

        String receiverName = demographicManager.getDemographicFormattedName(loggedInInfo, Integer.parseInt(demographicId));
        List<?>[] receiverEmailList = emailComposeManager.getRecipients(loggedInInfo, Integer.parseInt(demographicId));

        List<EmailConfig> senderAccounts = emailComposeManager.getAllSenderAccounts();

        if (emailPDFPassword == null) {
            emailPDFPassword = emailComposeManager.createEmailPDFPassword(loggedInInfo, Integer.parseInt(demographicId));
            emailPDFPasswordClue = "To protect your privacy, the PDF attachments in this email have been encrypted with a 18 digit password - your date of birth in the format YYYYMMDD followed by the 10 digits of your health insurance number.";
        }

        List<EmailAttachment> emailAttachmentList = new ArrayList<>();
        try {
            emailAttachmentList.addAll(emailComposeManager.prepareEFormAttachments(loggedInInfo, fdid, attachedEForms));
            emailAttachmentList.addAll(emailComposeManager.prepareEDocAttachments(loggedInInfo, attachedDocuments));
            emailAttachmentList.addAll(emailComposeManager.prepareLabAttachments(loggedInInfo, attachedLabs));
            emailAttachmentList.addAll(emailComposeManager.prepareHRMAttachments(loggedInInfo, attachedHRMDocuments));
            emailAttachmentList.addAll(emailComposeManager.prepareFormAttachments(request, response, attachedForms, Integer.parseInt(demographicId)));
        } catch (PDFGenerationException e) {
            logger.error(e.getMessage(), e);
            return emailComposeError(request,"This eForm (and attachments, if applicable) could not be emailed. \\n\\n" + e.getMessage());
        }
        emailComposeManager.sanitizeAttachments(emailAttachmentList);

        request.setAttribute("transactionType", TransactionType.EFORM);
        request.setAttribute("emailConsentName", emailConsent[0]);
        request.setAttribute("emailConsentStatus", emailConsent[1]);
        request.setAttribute("receiverName", receiverName);
        request.setAttribute("receiverEmailList", receiverEmailList[0]);
        request.setAttribute("invalidReceiverEmailList", receiverEmailList[1]);
        request.setAttribute("senderAccounts", senderAccounts);
        request.setAttribute("emailPDFPassword", emailPDFPassword);
        request.setAttribute("emailPDFPasswordClue", emailPDFPasswordClue);
        request.getSession().setAttribute("emailAttachmentList", emailAttachmentList);

        return "compose";
    }

    private String emailComposeError(HttpServletRequest request, String errorMessage) {
        request.setAttribute("errorMessage", errorMessage);
        return "eFormError";
    }
}
