package org.oscarehr.email.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.model.EmailAttachment;
import org.oscarehr.common.model.EmailLog;
import org.oscarehr.common.model.EmailLog.EmailStatus;
import org.oscarehr.email.core.EmailData;
import org.oscarehr.managers.EformDataManager;
import org.oscarehr.managers.EmailManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class EmailSendAction extends DispatchAction {
    private static final Logger logger = MiscUtils.getLogger();
    private EmailManager emailManager = SpringUtils.getBean(EmailManager.class);
    private EformDataManager eformDataManager = SpringUtils.getBean(EformDataManager.class);

    public ActionForward sendEFormEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        boolean deleteEFormAfterEmail = request.getParameter("deleteEFormAfterEmail") != null && "true".equalsIgnoreCase(request.getParameter("deleteEFormAfterEmail"));

        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
        EmailLog emailLog = sendEmail(request);

        boolean isEmailSuccessful = emailLog.getStatus() == EmailStatus.SUCCESS;
        request.setAttribute("isEmailSuccessful", isEmailSuccessful);
        if (isEmailSuccessful && deleteEFormAfterEmail) { eformDataManager.removeEFormData(loggedInInfo, request.getParameter("fdid")); }
        request.setAttribute("isOpenEForm", request.getParameter("openEFormAfterEmail"));
        request.setAttribute("fdid", request.getParameter("fdid"));
        request.setAttribute("emailLog", emailLog);
        return mapping.findForward("success");
    }

    public ActionForward sendDirectEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        EmailLog emailLog = sendEmail(request);
        boolean isEmailSuccessful = emailLog.getStatus() == EmailStatus.SUCCESS;
        request.setAttribute("isEmailSuccessful", isEmailSuccessful);
        request.setAttribute("emailLog", emailLog);
        return mapping.findForward("success");
    }

    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        EmailData emailData = prepareEmailFields(request);
        ActionRedirect emailRedirect = new ActionRedirect(mapping.findForward(emailData.getTransactionType().name()));
        switch (emailData.getTransactionType()) {
            case EFORM:
                emailRedirect.addParameter("fdid", request.getParameter("fdid"));
                emailRedirect.addParameter("parentAjaxId", "eforms");
                break;
            default:
                break;
        }
        return emailRedirect;
    }

    private EmailLog sendEmail(HttpServletRequest request) {
        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
        EmailData emailData = prepareEmailFields(request);
        return emailManager.sendEmail(loggedInInfo, emailData);
    }

    private EmailData prepareEmailFields(HttpServletRequest request) {
        String fromEmail = request.getParameter("senderEmailAddress");
        String[] receiverEmails = request.getParameterValues("receiverEmailAddress");
        String subject = request.getParameter("subjectEmail");
        String body = request.getParameter("bodyEmail");
        String encryptedMessage = request.getParameter("encryptedMessage");
        String password = request.getParameter("emailPDFPassword");
        String passwordClue = request.getParameter("emailPDFPasswordClue");
        String isEncrypted = request.getParameter("isEmailEncrypted");
        String isAttachmentEncrypted = request.getParameter("isEmailAttachmentEncrypted");
        String chartDisplayOption = request.getParameter("patientChartOption");
        String transactionType = request.getParameter("transactionType");
        String demographicNo = request.getParameter("demographicId");
        String additionalParams = request.getParameter("additionalURLParams");
        List<EmailAttachment> emailAttachmentList = (List<EmailAttachment>) request.getSession().getAttribute("emailAttachmentList");

        LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
        String providerNo = loggedInInfo.getLoggedInProviderNo();

        EmailData emailData = new EmailData();
        emailData.setSender(fromEmail);
        emailData.setRecipients(receiverEmails);
        emailData.setSubject(subject);
        emailData.setBody(body);
        emailData.setEncryptedMessage(encryptedMessage);
        emailData.setPassword(password);
        emailData.setPasswordClue(passwordClue);
        emailData.setIsEncrypted(isEncrypted);
        emailData.setIsAttachmentEncrypted(isAttachmentEncrypted);
        emailData.setChartDisplayOption(chartDisplayOption);
        emailData.setTransactionType(transactionType);
        emailData.setDemographicNo(demographicNo);
        emailData.setProviderNo(providerNo);
        emailData.setAdditionalParams(additionalParams);
        emailData.setAttachments(emailAttachmentList);

        request.getSession().removeAttribute("emailAttachmentList");

        return emailData;
    }
}
