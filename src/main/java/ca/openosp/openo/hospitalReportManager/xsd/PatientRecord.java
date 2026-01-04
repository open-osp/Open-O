package ca.openosp.openo.hospitalReportManager.xsd;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import ca.openosp.openo.hospitalReportManager.xsd.Demographics;
import ca.openosp.openo.hospitalReportManager.xsd.ReportsReceived;
import ca.openosp.openo.hospitalReportManager.xsd.TransactionInformation;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
@XmlAccessorType(value=XmlAccessType.FIELD)
@XmlType(name="", propOrder={"demographics", "reportsReceived", "transactionInformation"})
@XmlRootElement(name="PatientRecord", namespace="cds")
public class PatientRecord {
    @XmlElement(name="Demographics", namespace="cds", required=true)
    protected Demographics demographics;
    @XmlElement(name="ReportsReceived", namespace="cds")
    protected List<ReportsReceived> reportsReceived;
    @XmlElement(name="TransactionInformation", namespace="cds")
    protected List<TransactionInformation> transactionInformation;

    public Demographics getDemographics() {
        return this.demographics;
    }

    public void setDemographics(Demographics value) {
        this.demographics = value;
    }

    public List<ReportsReceived> getReportsReceived() {
        if (this.reportsReceived == null) {
            this.reportsReceived = new ArrayList<ReportsReceived>();
        }
        return this.reportsReceived;
    }

    public List<TransactionInformation> getTransactionInformation() {
        if (this.transactionInformation == null) {
            this.transactionInformation = new ArrayList<TransactionInformation>();
        }
        return this.transactionInformation;
    }
}
