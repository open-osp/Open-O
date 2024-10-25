package org.oscarehr.email.core;

import java.util.Collections;
import java.util.List;

import org.oscarehr.common.model.EmailAttachment;
import org.oscarehr.common.model.EmailLog.ChartDisplayOption;
import org.oscarehr.common.model.EmailLog.TransactionType;

import oscar.util.StringUtils;

public class EmailData {
    private String sender;
    private String[] recipients;
    private String subject;
    private String body;
    private String encryptedMessage;
    private String password;
    private String passwordClue;
    private boolean isEncrypted;
    private boolean isAttachmentEncrypted;
    private ChartDisplayOption chartDisplayOption;
    private TransactionType transactionType;
    private Integer demographicNo;
    private String providerNo;
    private String additionalParams;
    private List<EmailAttachment> attachments;

    public EmailData() { }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender != null ? sender : "";
    }

    public String[] getRecipients() {
        return recipients;
    }

    public void setRecipients(String[] recipients) {
        this.recipients = recipients != null ? recipients : new String[0];
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject != null ? subject : "";
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body != null ? body : "";
    }

    public String getEncryptedMessage() {
        return encryptedMessage;
    }

    public void setEncryptedMessage(String encryptedMessage) {
        this.encryptedMessage = encryptedMessage != null ? encryptedMessage : "";
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password != null ? password : "";
    }

    public String getPasswordClue() {
        return passwordClue;
    }

    public void setPasswordClue(String passwordClue) {
        this.passwordClue = passwordClue != null ? passwordClue : "";
    }

    public boolean getIsEncrypted() {
        return isEncrypted;
    }

    public void setIsEncrypted(boolean isEncrypted) {
        this.isEncrypted = isEncrypted;
    }

    public void setIsEncrypted(String isEncrypted) {
        if (isEncrypted == null) { isEncrypted = "false"; }
        this.isEncrypted = "true".equals(isEncrypted);
    }

    public boolean getIsAttachmentEncrypted() {
        return isAttachmentEncrypted;
    }

    public void setIsAttachmentEncrypted(boolean isAttachmentEncrypted) {
        this.isAttachmentEncrypted = isAttachmentEncrypted;
    }

    public void setIsAttachmentEncrypted(String isAttachmentEncrypted) {
        if (isAttachmentEncrypted == null) { isAttachmentEncrypted = "false"; }
        this.isAttachmentEncrypted = "true".equals(isAttachmentEncrypted);
    }

    public ChartDisplayOption getChartDisplayOption() {
        return chartDisplayOption;
    }

    public void setChartDisplayOption(ChartDisplayOption chartDisplayOption) {
        this.chartDisplayOption = chartDisplayOption;
    }

    public void setChartDisplayOption(String chartDisplayOption) {
        if (chartDisplayOption == null) { chartDisplayOption = "addFullNote"; }
        this.chartDisplayOption = "doNotAddAsNote".equalsIgnoreCase(chartDisplayOption) ? ChartDisplayOption.WITHOUT_NOTE : ChartDisplayOption.WITH_FULL_NOTE;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public void setTransactionType(String transactionType) {
        if (transactionType == null) { transactionType = "DIRECT"; }
        switch (transactionType.toUpperCase()) {
            case "EFORM":
                this.transactionType = TransactionType.EFORM;
                break;
            case "CONSULTATION":
                this.transactionType = TransactionType.CONSULTATION;
                break;
            case "TICKLER":
                this.transactionType = TransactionType.TICKLER;
                break;
            default:
                this.transactionType = TransactionType.DIRECT;
                break;
        }
    }

    public Integer getDemographicNo() {
        return demographicNo;
    }

    public void setDemographicNo(Integer demographicNo) {
        this.demographicNo = demographicNo;
    }

    public void setDemographicNo(String demographicNo) {
        this.demographicNo = (StringUtils.isNullOrEmpty(demographicNo)) ? -1 : Integer.parseInt(demographicNo);
    }

    public String getProviderNo() {
        return providerNo;
    }

    public void setProviderNo(String providerNo) {
        this.providerNo = providerNo == null ? "-1" : providerNo;
    }

    public String getAdditionalParams() {
        return additionalParams;
    }

    public void setAdditionalParams(String additionalParams) {
        this.additionalParams = StringUtils.isNullOrEmpty(additionalParams) ? "" : additionalParams;
    }

    public List<EmailAttachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<EmailAttachment> attachments) {
        this.attachments = attachments != null ? attachments : Collections.emptyList();
    }
}


