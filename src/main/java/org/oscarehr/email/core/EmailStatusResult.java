package org.oscarehr.email.core;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;
import org.oscarehr.common.model.EmailLog.EmailStatus;

public class EmailStatusResult implements Comparable<EmailStatusResult> {
    private Integer logId;
    private String subject;
    private String senderFirstName;
    private String senderLastName;
    private String senderEmail;
    private String recipientFirstName;
    private String recipientLastName;
    private String recipientEmail;
    private Boolean isEncrypted;
    private String password;
    private EmailStatus status;
    private String errorMessage;
    private Date created;

    public EmailStatusResult() {
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

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail.replace(";", ", ");
    }

    public Boolean getIsEncrypted() {
        return isEncrypted;
    }

    public void setIsEncrypted(Boolean isEncrypted) {
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
        return DateFormatUtils.format(created, "yyyy-MM-dd HH:mm:ss");
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
