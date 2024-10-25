package org.oscarehr.common.model;

import javax.persistence.*;

import java.nio.charset.StandardCharsets;
import org.apache.commons.codec.binary.Base64;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "emailLog")
public class EmailLog extends AbstractModel<Integer> implements Comparable<EmailLog> {

    public enum EmailStatus {
        SUCCESS,
        FAILED,
        RESOLVED
    }

    public enum ChartDisplayOption {
        WITHOUT_NOTE("doNotAddAsNote"),
        WITH_FULL_NOTE("addFullNote");

        private final String value;

        ChartDisplayOption(String value) {
            this.value = value;
        }

        public String getValue() { return value; }
    }

    public enum TransactionType {
        EFORM,
        CONSULTATION,
        TICKLER,
        DIRECT
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String fromEmail;

    private String toEmail;

    private String subject;

    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] body;

    @Enumerated(EnumType.STRING)
    private EmailStatus status;

    private String errorMessage;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp = new Date();

    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] encryptedMessage;

    private String password;

    private String passwordClue;

    private boolean isEncrypted;

    private boolean isAttachmentEncrypted;

    @Enumerated(EnumType.STRING)
    private ChartDisplayOption chartDisplayOption;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    private String additionalParams;

    @ManyToOne
    @JoinColumn(name = "DemographicNo")
    private Demographic demographic;

    @ManyToOne
    @JoinColumn(name = "ProviderNo")
    private Provider provider;

    @ManyToOne
    @JoinColumn(name = "configId")
    private EmailConfig emailConfig;

    @OneToMany(mappedBy = "emailLog", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<EmailAttachment> emailAttachments;

    public EmailLog() {}

    public EmailLog(EmailConfig emailConfig, String fromEmail, String[] toEmail, String subject, String body, EmailStatus status) {
        this.emailConfig = emailConfig;
        this.fromEmail = fromEmail;
        this.toEmail = toEmail != null ? String.join(";", toEmail) : "";
        this.subject = subject;
        this.body = Base64.encodeBase64(body.getBytes(StandardCharsets.UTF_8));
        this.status = status;
        this.timestamp = new Date();
    }

    public Integer getId() {
        return id;
    }

    public EmailConfig getEmailConfig() {
        return emailConfig;
    }

    public void setEmailConfig(EmailConfig emailConfig) {
        this.emailConfig = emailConfig;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String[] getToEmail() {
        return toEmail.split(";");
    }

    public void setToEmail(String[] toEmail) {
        this.toEmail = toEmail != null ? String.join(";", toEmail) : "";
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return new String(Base64.decodeBase64(body), StandardCharsets.UTF_8);
    }

    public void setBody(String body) {
        this.body = Base64.encodeBase64(body.getBytes(StandardCharsets.UTF_8));
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getEncryptedMessage() {
        return new String(Base64.decodeBase64(encryptedMessage), StandardCharsets.UTF_8);
    }

    public void setEncryptedMessage(String encryptedMessage) {
        this.encryptedMessage = Base64.encodeBase64(encryptedMessage.getBytes(StandardCharsets.UTF_8));
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordClue() {
        return passwordClue;
    }

    public void setPasswordClue(String passwordClue) {
        this.passwordClue = passwordClue;
    }

    public boolean getIsEncrypted() {
        return isEncrypted;
    }

    public void setIsEncrypted(boolean isEncrypted) {
        this.isEncrypted = isEncrypted;
    }

    public boolean getIsAttachmentEncrypted() {
        return isAttachmentEncrypted;
    }

    public void setIsAttachmentEncrypted(boolean isAttachmentEncrypted) {
        this.isAttachmentEncrypted = isAttachmentEncrypted;
    }

    public ChartDisplayOption getChartDisplayOption() {
        return chartDisplayOption;
    }

    public void setChartDisplayOption(ChartDisplayOption chartDisplayOption) {
        this.chartDisplayOption = chartDisplayOption;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Demographic getDemographic() {
        return demographic;
    }

    public void setDemographic(Demographic demographic) {
        this.demographic = demographic;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public String getAdditionalParams() {
        return additionalParams;
    }

    public void setAdditionalParams(String additionalParams) {
        this.additionalParams = additionalParams;
    }

    public List<EmailAttachment> getEmailAttachments() {
        return emailAttachments;
    }

    public void setEmailAttachments(List<EmailAttachment> emailAttachments) {
        this.emailAttachments = emailAttachments;
    }

    @Override
    public int compareTo(EmailLog other) {
        // Compare based on the timestamp
        return this.timestamp.compareTo(other.timestamp);
    }
}
