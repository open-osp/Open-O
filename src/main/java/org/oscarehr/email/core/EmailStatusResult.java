package org.oscarehr.email.core;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.oscarehr.common.model.EmailLog.EmailStatus;

public class EmailStatusResult implements Comparable<EmailStatusResult> {
    private Integer logId;
    private String subject;
    private String senderFirstName;
    private String senderLastName;
    private String senderEmail;
    private String recipientFirstName;
    private String recipientLastName;
    private String providerFirstName;
    private String providerLastName;
    private String recipientEmail;
    private boolean isEncrypted;
    private String password;
    private EmailStatus status;
    private String errorMessage;
    private Date created;

    public EmailStatusResult() {
    }

    public EmailStatusResult(Integer logId, String subject, String senderFirstName, String senderLastName, String senderEmail, 
                             String recipientFirstName, String recipientLastName, String recipientEmail, String providerFirstName, 
                             String providerLastName, boolean isEncrypted, String password, EmailStatus status, 
                             String errorMessage, Date created) {
        this.logId = logId;
        this.subject = subject;
        this.senderFirstName = senderFirstName;
        this.senderLastName = senderLastName;
        this.senderEmail = senderEmail;
        this.recipientFirstName = recipientFirstName;
        this.recipientLastName = recipientLastName;
        this.providerFirstName = providerFirstName;
        this.providerLastName = providerLastName;
        this.recipientEmail = recipientEmail;
        this.isEncrypted = isEncrypted;
        this.password = password;
        this.status = status;
        this.errorMessage = errorMessage;
        this.created = created;
    }

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSenderFirstName() {
        return senderFirstName;
    }

    public void setSenderFirstName(String senderFirstName) {
        this.senderFirstName = senderFirstName;
    }

    public String getSenderLastName() {
        return senderLastName;
    }

    public void setSenderLastName(String senderLastName) {
        this.senderLastName = senderLastName;
    }

    public String getSenderFullName() {
        return toCamelCase(senderFirstName) + " " + toCamelCase(senderLastName);
    }

    public void setSenderFullName(String senderFirstName, String senderLastName) {
        this.senderFirstName = senderFirstName;
        this.senderLastName = senderLastName;
    } 

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getRecipientFirstName() {
        return recipientFirstName;
    }

    public void setRecipientFirstName(String recipientFirstName) {
        this.recipientFirstName = recipientFirstName;
    }

    public String getRecipientLastName() {
        return recipientLastName;
    }

    public void setRecipientLastName(String recipientLastName) {
        this.recipientLastName = recipientLastName;
    }

    public String getRecipientFullName() {
        return toCamelCase(recipientFirstName) + " " + toCamelCase(recipientLastName);
    }

    public void setRecipientFullName(String recipientFirstName, String recipientLastName) {
        this.recipientFirstName = recipientFirstName;
        this.recipientLastName = recipientLastName;
    }

    public String getProviderFirstName() {
        return providerFirstName;
    }

    public void setProviderFirstName(String providerFirstName) {
        this.providerFirstName = providerFirstName;
    }

    public String getProviderLastName() {
        return providerLastName;
    }

    public void setProviderLastName(String providerLastName) {
        this.providerLastName = providerLastName;
    }

    public String getProviderFullName() {
        return toCamelCase(providerLastName) + ", " + toCamelCase(providerFirstName);
    }

    public void setProviderFullName(String providerFirstName, String providerLastName) {
        this.providerFirstName = providerFirstName;
        this.providerLastName = providerLastName;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail.replace(";", ", ");
    }

    public boolean getIsEncrypted() {
        return isEncrypted;
    }

    public void setIsEncrypted(boolean isEncrypted) {
        this.isEncrypted = isEncrypted;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public EmailStatus getStatus() {
        return status;
    }

    public void setStatus(EmailStatus status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getCreatedDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(created);
    }

    public String getCreatedTime() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        return timeFormat.format(created);
    }

    public String getCreatedStringDate() {
        LocalDateTime createdLocalDateTime = created.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        return createdLocalDateTime.format(formatter);
    }

    private String toCamelCase(String inputString) {
        return Character.toUpperCase(inputString.charAt(0)) + inputString.substring(1).toLowerCase();
    }

    @Override
    public int compareTo(EmailStatusResult other) {
        // Compare based on the timestamp
        return this.created.compareTo(other.created);
    }
}
