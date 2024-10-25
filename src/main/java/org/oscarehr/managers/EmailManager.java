package org.oscarehr.managers;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.CaseManagementNoteLink;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.common.dao.EmailConfigDaoImpl;
import org.oscarehr.common.dao.EmailLogDaoImpl;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.EmailAttachment;
import org.oscarehr.common.model.EmailConfig;
import org.oscarehr.common.model.EmailLog;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.EmailLog.ChartDisplayOption;
import org.oscarehr.common.model.EmailLog.EmailStatus;
import org.oscarehr.common.model.SecRole;
import org.oscarehr.common.model.enumerator.DocumentType;
import org.oscarehr.documentManager.ConvertToEdoc;
import org.oscarehr.documentManager.DocumentAttachmentManager;
import org.oscarehr.email.core.EmailData;
import org.oscarehr.email.core.EmailSender;
import org.oscarehr.email.core.EmailStatusResult;
import org.oscarehr.email.util.EmailNoteUtil;
import org.oscarehr.util.EmailSendingException;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.PDFEncryptionUtil;
import org.oscarehr.util.PDFGenerationException;
import org.owasp.encoder.Encode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import oscar.log.LogAction;
import oscar.oscarEncounter.data.EctProgram;
import oscar.util.StringUtils;

@Service
public class EmailManager {
    private final Logger logger = MiscUtils.getLogger();

    @Autowired
    private EmailConfigDaoImpl emailConfigDao;
    @Autowired
    private EmailLogDaoImpl emailLogDao;
    @Autowired
    private CaseManagementManager caseManagementManager;
    @Autowired
    private DemographicManager demographicManager;
    @Autowired
    private DocumentAttachmentManager documentAttachmentManager;
    @Autowired
	private ProgramManager programManager;
    @Autowired
    private ProviderManager2 providerManager;
    @Autowired
	private SecurityInfoManager securityInfoManager;

    public EmailLog sendEmail(LoggedInInfo loggedInInfo, EmailData emailData) {
        if (!securityInfoManager.hasPrivilege(loggedInInfo, "_email", SecurityInfoManager.WRITE, null)) {
			throw new RuntimeException("missing required security object (_email)");
		}

        sanitizeEmailFields(emailData);
        EmailLog emailLog = prepareEmailForOutbox(loggedInInfo, emailData);
        try {
            if (emailData.getIsEncrypted()) { encryptEmail(emailData); }
            EmailSender emailSender = new EmailSender(loggedInInfo, emailLog.getEmailConfig(), emailData);
            emailSender.send();
            updateEmailStatus(loggedInInfo, emailLog, EmailStatus.SUCCESS, "");
            if (emailLog.getChartDisplayOption().equals(ChartDisplayOption.WITH_FULL_NOTE)) { addEmailNote(loggedInInfo, emailLog); }
        } catch (EmailSendingException e) {
            updateEmailStatus(loggedInInfo, emailLog, EmailStatus.FAILED, e.getMessage());
            logger.error("Failed to send email", e);
        }
        return emailLog;
    }

    public EmailLog prepareEmailForOutbox(LoggedInInfo loggedInInfo, EmailData emailData) {
        if (!securityInfoManager.hasPrivilege(loggedInInfo, "_email", SecurityInfoManager.WRITE, null)) {
			throw new RuntimeException("missing required security object (_email)");
		}

        EmailConfig emailConfig = emailConfigDao.findActiveEmailConfig(emailData.getSender());
        Demographic demographic = demographicManager.getDemographic(loggedInInfo, emailData.getDemographicNo());
        Provider provider = providerManager.getProvider(loggedInInfo, emailData.getProviderNo());

        EmailLog emailLog = new EmailLog(emailConfig, emailData.getSender(), emailData.getRecipients(), emailData.getSubject(), emailData.getBody(), EmailStatus.FAILED);
        setEmailAttachments(emailLog, emailData.getAttachments());
        emailLog.setEncryptedMessage(emailData.getEncryptedMessage());
        emailLog.setPassword(emailData.getPassword());
        emailLog.setPasswordClue(emailData.getPasswordClue());
        emailLog.setIsEncrypted(emailData.getIsEncrypted());
        emailLog.setIsAttachmentEncrypted(emailData.getIsAttachmentEncrypted());
        emailLog.setChartDisplayOption(emailData.getChartDisplayOption());
        emailLog.setTransactionType(emailData.getTransactionType());
        emailLog.setErrorMessage("Email was not sent successfully for unknown reasons.");
        emailLog.setAdditionalParams(emailData.getAdditionalParams());
        emailLog.setDemographic(demographic);
        emailLog.setProvider(provider);
        emailLogDao.persist(emailLog);

        LogAction.addLog(loggedInInfo, "EmailManager.prepareEmailForOutbox", "Email", "emailLogId=" + emailLog.getId(), String.valueOf(emailLog.getDemographic().getDemographicNo()), "");

        return emailLog;
    }

    public EmailLog updateEmailStatus(LoggedInInfo loggedInInfo, Integer emailLogId, EmailStatus emailStatus, String errorMessage) {
        if (!securityInfoManager.hasPrivilege(loggedInInfo, "_email", SecurityInfoManager.WRITE, null)) {
			throw new RuntimeException("missing required security object (_email)");
		}
        
        EmailLog emailLog = emailLogDao.find(emailLogId);
        return updateEmailStatus(loggedInInfo, emailLog, emailStatus, errorMessage);
    }

    public EmailLog updateEmailStatus(LoggedInInfo loggedInInfo, EmailLog emailLog, EmailStatus emailStatus, String errorMessage) {
        if (!securityInfoManager.hasPrivilege(loggedInInfo, "_email", SecurityInfoManager.WRITE, null)) {
			throw new RuntimeException("missing required security object (_email)");
		}
        
        emailLog.setStatus(emailStatus);
        if(errorMessage != null) { emailLog.setErrorMessage(errorMessage); }
        if (!emailStatus.equals(EmailStatus.RESOLVED)) { emailLog.setTimestamp(new Date()); }
        emailLogDao.merge(emailLog);
        return emailLog;
    }

    public List<EmailStatusResult> getEmailStatusByDateDemographicSenderStatus(LoggedInInfo loggedInInfo, String dateBeginStr, String dateEndStr, String demographic_no, String senderEmailAddress, String emailStatus) {
        if (!securityInfoManager.hasPrivilege(loggedInInfo, "_email", SecurityInfoManager.READ, null)) {
			throw new RuntimeException("missing required security object (_email)");
		}

        Date dateBegin = parseDate(dateBeginStr, "yyyy-MM-dd", "00:00:00");
        Date dateEnd = parseDate(dateEndStr, "yyyy-MM-dd", "23:59:59");
        if (dateBegin == null || dateEnd == null) { return Collections.emptyList(); }
        
        List<EmailLog> resultList = emailLogDao.getEmailStatusByDateDemographicSenderStatus(dateBegin, dateEnd, demographic_no, senderEmailAddress, emailStatus);
        return retriveEmailStatusResultList(resultList);
    }

    public EmailLog getEmailLogByCaseManagementNoteId(LoggedInInfo loggedInInfo, Long noteId) {
        if (!securityInfoManager.hasPrivilege(loggedInInfo, "_email", SecurityInfoManager.READ, null)) {
			throw new RuntimeException("missing required security object (_email)");
		}

        CaseManagementNoteLink caseManagementNoteLink = caseManagementManager.getLatestLinkByNote(noteId);
        if (caseManagementNoteLink == null || !caseManagementNoteLink.getTableName().equals(CaseManagementNoteLink.EMAIL)) { return null; }
        Long emailLogId = caseManagementNoteLink.getTableId();
        return emailLogDao.find(emailLogId.intValue());
    }

    public void addEmailNote(LoggedInInfo loggedInInfo, EmailLog emailLog) {
        if (!securityInfoManager.hasPrivilege(loggedInInfo, "_email", SecurityInfoManager.READ, null)) {
			throw new RuntimeException("missing required security object (_email)");
		}

        EmailNoteUtil emailNoteUtil = new EmailNoteUtil(loggedInInfo, emailLog);
        String emailNote = emailNoteUtil.createNote();

        String providerNo = loggedInInfo.getLoggedInProviderNo();
        String programId = new EctProgram(loggedInInfo.getSession()).getProgram(providerNo);
        Date creationDate = new Date();

        ProgramProvider programProvider = programManager.getProgramProvider(providerNo, programId);
        SecRole doctorRole = caseManagementManager.getSecRoleByRoleName("doctor");
        String role = programProvider != null ? String.valueOf(programProvider.getRoleId()) : String.valueOf(doctorRole.getId());

        CaseManagementNote caseManagementNote = new CaseManagementNote();
		caseManagementNote.setUpdate_date(creationDate);
		caseManagementNote.setObservation_date(creationDate);
		caseManagementNote.setDemographic_no(String.valueOf(emailLog.getDemographic().getDemographicNo()));
		caseManagementNote.setProviderNo(providerNo);
		caseManagementNote.setNote(emailNote);
		caseManagementNote.setSigned(true);
		caseManagementNote.setSigning_provider_no(providerNo);
		caseManagementNote.setProgram_no(programId);
        caseManagementNote.setReporter_caisi_role(role);
		caseManagementNote.setReporter_program_team("0");
		caseManagementNote.setHistory(emailNote);
		Long noteId = caseManagementManager.saveNoteSimpleReturnID(caseManagementNote);
				 
		CaseManagementNoteLink caseManagementNoteLink = new CaseManagementNoteLink(CaseManagementNoteLink.EMAIL, Long.valueOf(emailLog.getId()), noteId);
        caseManagementManager.saveNoteLink(caseManagementNoteLink);
    }

    private void setEmailAttachments(EmailLog emailLog, List<EmailAttachment> emailAttachments) {
        List<EmailAttachment> emailAttachmentList = new ArrayList<>();
        for (EmailAttachment emailAttachment : emailAttachments) {
            emailAttachmentList.add(new EmailAttachment(emailLog, emailAttachment.getFileName(), emailAttachment.getFilePath(), emailAttachment.getDocumentType(), emailAttachment.getDocumentId()));
        }
        emailLog.setEmailAttachments(emailAttachmentList);  
    }

    private void sanitizeEmailFields(EmailData emailData) {
        if (StringUtils.isNullOrEmpty(emailData.getEncryptedMessage()) && emailData.getAttachments().isEmpty()) {
            emailData.setIsEncrypted(false);
            emailData.setIsAttachmentEncrypted(false);
            emailData.setPassword("");
            emailData.setPasswordClue("");
        } else if (StringUtils.isNullOrEmpty(emailData.getEncryptedMessage()) && emailData.getAttachments().size() > 0 && !emailData.getIsAttachmentEncrypted()) {
            emailData.setIsEncrypted(false);
            emailData.setIsAttachmentEncrypted(false);
            emailData.setPassword("");
            emailData.setPasswordClue("");
        } else if (emailData.getAttachments().isEmpty()) {
            emailData.setIsAttachmentEncrypted(false);
        } else if (!emailData.getIsEncrypted()) {
            emailData.setEncryptedMessage("");
            emailData.setIsAttachmentEncrypted(false);
            emailData.setPassword("");
            emailData.setPasswordClue("");
        }
    }

    private void encryptEmail(EmailData emailData) throws EmailSendingException {
        // Encrypt message and attachment
        List<EmailAttachment> encryptableAttachments = new ArrayList<>();
        if (!StringUtils.isNullOrEmpty(emailData.getEncryptedMessage())) { encryptableAttachments.add(createMessageAttachment(emailData)); }
        if (emailData.getIsAttachmentEncrypted() && !emailData.getAttachments().isEmpty()) { encryptableAttachments.addAll(emailData.getAttachments()); }
        encryptAttachments(encryptableAttachments, emailData.getPassword());

        List<EmailAttachment> emailAttachments = new ArrayList<>();
        emailAttachments.addAll(encryptableAttachments);
        if (!emailData.getIsAttachmentEncrypted() && !emailData.getAttachments().isEmpty()) { emailAttachments.addAll(emailData.getAttachments()); }
        emailData.setAttachments(emailAttachments);

        //append password clue
        emailData.setBody(emailData.getBody() + "\n\n*****\n" + emailData.getPasswordClue().trim() + "\n*****\n");
    }

    private EmailAttachment createMessageAttachment(EmailData emailData) {
        if (StringUtils.isNullOrEmpty(emailData.getEncryptedMessage())) { return null; }
        String htmlSafeMessage = Encode.forHtmlContent(emailData.getEncryptedMessage()).replace("\n", "<br>");
        emailData.setEncryptedMessage(htmlSafeMessage);
        Path encryptedMessagePDF = ConvertToEdoc.saveAsTempPDF(emailData);
        EmailAttachment emailAttachment = new EmailAttachment("message.pdf", encryptedMessagePDF.toString(), DocumentType.DOC, -1);
        return emailAttachment;
    }

    private void encryptAttachments(List<EmailAttachment> encryptableAttachments, String password) throws EmailSendingException {
        for (EmailAttachment attachment: encryptableAttachments) {
            try {
                Path attachmentPDFPath = Paths.get(attachment.getFilePath());
                attachmentPDFPath = PDFEncryptionUtil.encryptPDF(attachmentPDFPath, password);
                attachment.setFilePath(attachmentPDFPath.toString());
            } catch (IOException e) {
                logger.error("Failed to create encrypted email attachments", e);
                throw new EmailSendingException("Failed to create encrypted email attachments", e);
            }
        }
    }

    private Path concatPDFs(List<EmailAttachment> attachments) throws PDFGenerationException {
        if (attachments == null || attachments.isEmpty()) { return null; }

        List<Path> attachmentPathList = new ArrayList<>();
        for (EmailAttachment emailAttachment : attachments) {
            attachmentPathList.add(Paths.get(emailAttachment.getFilePath()));
        }

        return documentAttachmentManager.concatPDF(attachmentPathList);
    }

    private Date parseDate(String date, String format, String time) {
        if (date == null) { return null; }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            LocalDate localDate = LocalDate.parse(date, formatter);
            if (time == null || time.isEmpty()) { return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()); }
            LocalTime localTime = LocalTime.parse(time);
            LocalDateTime localDateTime = localDate.atTime(localTime);
            return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        } catch (DateTimeParseException e) {
            logger.error("UNPARSEABLE DATE " + date);
            return null;
        }
    }

    /**
     * Converts a list of EmailLog arrays into a list of EmailStatusResult DTOs.
     * This method facilitates easy transfer of data to the UI layer.
     *
     * @param resultList The list of EmailLog arrays containing email log data, demographic name, and provider name.
     * @return List of EmailStatusResult DTOs representing email status information.
     */
    private List<EmailStatusResult> retriveEmailStatusResultList(List<EmailLog> resultList) {
        List<EmailStatusResult> emailStatusResults = new ArrayList<>();
        for (EmailLog result : resultList) {
            EmailConfig emailConfig = result.getEmailConfig();
            Demographic demographic = result.getDemographic();
            Provider provider = result.getProvider();
            EmailStatusResult emailStatusResult = new EmailStatusResult(result.getId(), result.getSubject(), emailConfig.getSenderFirstName(), 
                                                    emailConfig.getSenderLastName(), result.getFromEmail(), demographic.getFirstName(),
                                                    demographic.getLastName(), String.join(", ", result.getToEmail()), provider.getFirstName(), provider.getLastName(),
                                                    result.getIsEncrypted(), result.getPassword(), result.getStatus(), result.getErrorMessage(), result.getTimestamp());
            emailStatusResults.add(emailStatusResult);
        }
        Collections.sort(emailStatusResults);
        return emailStatusResults;
    }
}
