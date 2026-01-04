//CHECKSTYLE:OFF
package ca.openosp.openo.billings.ca.bc.privateBilling;

/*
 * Author: Charles Liu <charles.liu@nondfa.com>
 * Company: WELL Health Technologies Corp.
 * Date: December 6, 2018
 */
public class PrivateBillingModel {

    private int billingCount;
    private String billingNumber;
    private String billingDate;
    private String billingType;
    private String billingStatus;
    private String demographicName;
    private String demographicNumber;
    private String providerNumber;
    private String recipientId;
    private String recipientName;
    private String balance;
    private String status;

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String value) {
        this.status = value;
    }

    public String getBillingNumber() {
        return this.billingNumber;
    }

    public void setBillingNumber(String value) {
        this.billingNumber = value;
    }

    public int getBillingCount() {
        return this.billingCount;
    }

    public void setBillingCount(int value) {
        this.billingCount = value;
    }

    public String getBillingDate() {
        return this.billingDate;
    }

    public void setBillingDate(String value) {
        this.billingDate = value;
    }

    public String getBillingType() {
        return this.billingType;
    }

    public void setBillingType(String value) {
        this.billingType = value;
    }

    public String getBillingStatus() {
        return this.billingStatus;
    }

    public void setBillingStatus(String value) {
        this.billingStatus = value;
    }

    public String getDemographicNumber() {
        return demographicNumber;
    }

    public void setDemographicNumber(String value) {
        this.demographicNumber = value;
    }

    public String getDemographicName() {
        return demographicName;
    }

    public void setDemographicName(String value) {
        this.demographicName = value;
    }

    public String getProviderNumber() {
        return this.providerNumber;
    }

    public void setProviderNumber(String value) {
        this.providerNumber = value;
    }

    public String getRecipientId() {
        return this.recipientId;
    }

    public void setRecipientId(String value) {
        this.recipientId = value;
    }

    public String getRecipientName() {
        return this.recipientName;
    }

    public void setRecipientName(String value) {
        this.recipientName = value;
    }

    public String getBalance() {
        return this.balance;
    }

    public void setBalance(String value) {
        this.balance = value;
    }
}