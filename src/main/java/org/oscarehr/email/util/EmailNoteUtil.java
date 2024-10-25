package org.oscarehr.email.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.oscarehr.common.model.EFormData;
import org.oscarehr.common.model.EmailAttachment;
import org.oscarehr.common.model.EmailLog;
import org.oscarehr.documentManager.EDoc;
import org.oscarehr.documentManager.EDocUtil;
import org.oscarehr.hospitalReportManager.HRMUtil;
import org.oscarehr.hospitalReportManager.model.HRMDocument;
import org.oscarehr.labs.LabIdAndType;
import org.oscarehr.managers.EformDataManager;
import org.oscarehr.managers.FormsManager;
import org.oscarehr.util.DateUtils;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import oscar.oscarEncounter.data.EctFormData.PatientForm;
import oscar.oscarLab.ca.on.CommonLabResultData;
import oscar.oscarLab.ca.on.LabResultData;
import oscar.util.StringUtils;

public class EmailNoteUtil {
    private EmailLog emailLog;
    private LoggedInInfo loggedInInfo;
    private String DATE_FORMAT = "yyyy.MM.dd";
    private String TIME_FORMAT = "hh:mm a";

    private CommonLabResultData commonLabResultData;
    private EformDataManager eFormDataManager = SpringUtils.getBean(EformDataManager.class);
    private FormsManager formsManager = SpringUtils.getBean(FormsManager.class);

    private EmailNoteUtil() { }

    public EmailNoteUtil(LoggedInInfo loggedInInfo, EmailLog emailLog) {
        this.emailLog = emailLog;
        this.loggedInInfo = loggedInInfo;
        this.commonLabResultData = new CommonLabResultData();
    }
    
    public String createNote() {
        StringBuilder noteBuilder = new StringBuilder();
        addHeader(emailLog, noteBuilder);
        addBody(emailLog, noteBuilder);
        addEncryptionInformation(emailLog, noteBuilder);
        addAttachments(emailLog, noteBuilder);
        addEncryptedBody(emailLog, noteBuilder);
        addTechnicalInformation(emailLog, noteBuilder);
        return noteBuilder.toString();
    }

    private void addHeader(EmailLog emailLog, StringBuilder noteBuilder) {
        noteBuilder.append("Email Subject: ").append(emailLog.getSubject()).append("\n\n");
    }

    private void addBody(EmailLog emailLog, StringBuilder noteBuilder) {
        noteBuilder.append(emailLog.getBody().trim()).append("\n\n");
    }

    private void addEncryptionInformation(EmailLog emailLog, StringBuilder noteBuilder) {
        if (!emailLog.getIsEncrypted()) { return; }

        noteBuilder.append("\n*****\n").append(emailLog.getPasswordClue().trim()).append("\n*****\n\n");
    }

    private void addAttachments(EmailLog emailLog, StringBuilder noteBuilder) {
        List<EmailAttachment> emailAttachmentList = emailLog.getEmailAttachments();
        if (emailAttachmentList == null || emailAttachmentList.isEmpty()) { return; }
        noteBuilder.append("***Attached Files***").append("\n\n");

        List<EFormData> eFormDataList = new ArrayList<>();
        List<EDoc> eDocList = new ArrayList<>();
        List<LabResultData> labResultDataList = new ArrayList<>();
        List<HRMDocument> hrmDocumentList = new ArrayList<>();
        List<PatientForm> formList = new ArrayList<>();
        for (EmailAttachment emailAttachment : emailAttachmentList) {
            switch (emailAttachment.getDocumentType()) {
                case EFORM:
                    EFormData eFormData = eFormDataManager.findByFdid(loggedInInfo, emailAttachment.getDocumentId());
                    if (eFormData != null) { eFormDataList.add(eFormData); }
                    break;
                case DOC:
                    EDoc eDoc = EDocUtil.getEDocFromDocId(String.valueOf(emailAttachment.getDocumentId()));
                    if (eDoc != null) { eDocList.add(eDoc); }
                    break;
                case LAB:
                    LabResultData labResultData = commonLabResultData.getLab(new LabIdAndType(emailAttachment.getDocumentId(), "HL7"));
                    if (labResultData != null) { labResultDataList.add(labResultData); }
                    break;
                case HRM:
                    HRMDocument hrmDocument = HRMUtil.getHRMDocumentById(loggedInInfo, emailAttachment.getDocumentId());
                    if (hrmDocument != null) { hrmDocumentList.add(hrmDocument); }
                    break;
                case FORM:
                    PatientForm form = formsManager.getFormById(loggedInInfo, emailAttachment.getDocumentId(), emailLog.getDemographic().getDemographicNo());
                    if (form != null) { formList.add(form); }
                    break;
            }
        }

        addEFormAttachments(eFormDataList, emailLog, noteBuilder);
        addDocumentAttachments(eDocList, emailLog, noteBuilder);
        addLabAttachments(labResultDataList, emailLog, noteBuilder);
        addHRMAttachments(hrmDocumentList, emailLog, noteBuilder);
        addFormAttachments(formList, emailLog, noteBuilder);
        noteBuilder.append("\n");
    }

    private void addEFormAttachments(List<EFormData> eFormDataList, EmailLog emailLog, StringBuilder noteBuilder) {
        Collections.sort(eFormDataList, Collections.reverseOrder(EFormData.FORM_DATE_COMPARATOR));
        for (EFormData eFormData : eFormDataList) {
            noteBuilder.append("eForm: ");
            String eFormDisplayName = StringUtils.isNullOrEmpty(eFormData.getSubject()) ? eFormData.getFormName() : eFormData.getSubject();
            noteBuilder.append(eFormDisplayName).append(" ");
            noteBuilder.append(getFormattedDate(eFormData.getFormDate())).append(" ");
            noteBuilder.append("(").append("ID: ").append(eFormData.getId()).append(") ");
            if (emailLog.getIsAttachmentEncrypted()) { noteBuilder.append("Password: ").append(emailLog.getPassword()); }
            noteBuilder.append("\n");
        }
    }

    private void addDocumentAttachments(List<EDoc> eDocList, EmailLog emailLog, StringBuilder noteBuilder) {
        Collections.sort(eDocList, Collections.reverseOrder(EDoc.OBSERVATION_DATE_COMPARATOR));
        for (EDoc eDoc : eDocList) {
            noteBuilder.append("Doc: ").append(eDoc.getDescription()).append(" ");
            noteBuilder.append(getFormattedDate(eDoc.getObservationDate(), "yyyy/MM/dd")).append(" ");
            noteBuilder.append("(").append("ID: ").append(eDoc.getDocId()).append(") ");
            if (emailLog.getIsAttachmentEncrypted()) { noteBuilder.append("Password: ").append(emailLog.getPassword()); }
            noteBuilder.append("\n");
        }
    }

    private void addLabAttachments(List<LabResultData> labResultDataList, EmailLog emailLog, StringBuilder noteBuilder) {
        Collections.sort(labResultDataList);
        for (LabResultData lab : labResultDataList) {
            noteBuilder.append("Lab: ");
            String labName = StringUtils.isNullOrEmpty(lab.getLabel()) ? lab.getDiscipline() : lab.getLabel();
            labName = StringUtils.isNullOrEmpty(labName) ? "UNLABELLED" : labName;
            noteBuilder.append(labName).append(" ");
            noteBuilder.append(getFormattedDate(lab.getDateObjFormated(), "yyyy-MM-dd")).append(" ");
            noteBuilder.append("(").append("ID: ").append(lab.getSegmentID()).append(") ");
            if (emailLog.getIsAttachmentEncrypted()) { noteBuilder.append("Password: ").append(emailLog.getPassword()); }
            noteBuilder.append("\n");
        }
    }

    private void addHRMAttachments(List<HRMDocument> hrmDocumentList, EmailLog emailLog, StringBuilder noteBuilder) {
        Collections.sort(hrmDocumentList, Collections.reverseOrder(HRMDocument.REPORT_DATE_COMPARATOR));
        for (HRMDocument hrmDocument : hrmDocumentList) {
            noteBuilder.append("HRM: ").append(hrmDocument.getDisplayName()).append(" ");
            noteBuilder.append(getFormattedDate(hrmDocument.getReportDate())).append(" ");
            noteBuilder.append("(").append("ID: ").append(hrmDocument.getId()).append(") ");
            if (emailLog.getIsAttachmentEncrypted()) { noteBuilder.append("Password: ").append(emailLog.getPassword()); }
            noteBuilder.append("\n");
        }
    }

    private void addFormAttachments(List<PatientForm> formList, EmailLog emailLog, StringBuilder noteBuilder) {
        Collections.sort(formList, PatientForm.EDITED_DATE_COMPARATOR);
        for (PatientForm form : formList) {
            noteBuilder.append("Form: ").append(form.getFormName()).append(" ");
            if (form.edited != null) { 
                noteBuilder.append(getFormattedDate(form.getEdited(), "dd-MM-yyyy HH:mm:ss")).append(" "); 
            }
            noteBuilder.append("(").append("ID: ").append(form.getFormId()).append(") ");
            if (emailLog.getIsAttachmentEncrypted()) { noteBuilder.append("Password: ").append(emailLog.getPassword()); }
            noteBuilder.append("\n");
        }
    }

    private void addEncryptedBody(EmailLog emailLog, StringBuilder noteBuilder) {
        if (!emailLog.getIsEncrypted() || StringUtils.isNullOrEmpty(emailLog.getEncryptedMessage())) { 
            return; 
        }

        noteBuilder.append("***Attached Message (message.pdf with password ");
        noteBuilder.append(emailLog.getPassword());
        noteBuilder.append(")***").append("\n\n");
        noteBuilder.append(emailLog.getEncryptedMessage().trim()).append("\n\n");
    }

    private void addTechnicalInformation(EmailLog emailLog, StringBuilder noteBuilder) {
        noteBuilder.append("***Technical Information***").append("\n\n");        
        noteBuilder.append("From: ").append(emailLog.getFromEmail()).append("\n");
        noteBuilder.append("To: ").append(getRecipientEmail()).append("\n");        
        noteBuilder.append("Sent: ").append(getEmailTime()).append(" on ").append(getEmailDate()).append("\n");
        noteBuilder.append("Unique Email Log ID: ").append(emailLog.getId());
    }

    private String getRecipientEmail() {
        String[] recipientEmailList = emailLog.getToEmail();
        StringBuilder formattedEmails = new StringBuilder();
        
        for (int i = 0; i < recipientEmailList.length; i++) {
            if (i == 0) { 
                formattedEmails.append(recipientEmailList[0]);
            } else if (i < recipientEmailList.length - 1) {
                formattedEmails.append(", ").append(recipientEmailList[i]);
            } else if (i == recipientEmailList.length - 1) {
                formattedEmails.append(", and ").append(recipientEmailList[i]);
            }
        }

        return formattedEmails.toString();
    }

    private String getFormattedDate(String dateString, String format) {
        String formattedDate = "";
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            Date date = simpleDateFormat.parse(dateString);
            formattedDate = DateUtils.format(DATE_FORMAT, date, null);
        } catch (ParseException e) {
            formattedDate = "";
        }
        return formattedDate;
    }

    private String getFormattedDate(Date date) {
        return DateUtils.format(DATE_FORMAT, date, null);
    }

    private String getEmailDate() {
        return DateUtils.format(DATE_FORMAT, emailLog.getTimestamp(), null);
    }

    private String getEmailTime() {
        return DateUtils.format(TIME_FORMAT, emailLog.getTimestamp(), null);
    }
}