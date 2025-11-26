package ca.openosp.openo.hospitalReportManager.xsd;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;
import ca.openosp.openo.hospitalReportManager.xsd.Address;
import ca.openosp.openo.hospitalReportManager.xsd.AddressStructured;
import ca.openosp.openo.hospitalReportManager.xsd.BloodPressure;
import ca.openosp.openo.hospitalReportManager.xsd.Code;
import ca.openosp.openo.hospitalReportManager.xsd.DateFullOrPartial;
import ca.openosp.openo.hospitalReportManager.xsd.Demographics;
import ca.openosp.openo.hospitalReportManager.xsd.DiabetesComplicationScreening;
import ca.openosp.openo.hospitalReportManager.xsd.DiabetesEducationalSelfManagement;
import ca.openosp.openo.hospitalReportManager.xsd.DiabetesMotivationalCounselling;
import ca.openosp.openo.hospitalReportManager.xsd.DiabetesSelfManagementChallenges;
import ca.openosp.openo.hospitalReportManager.xsd.DiabetesSelfManagementCollaborative;
import ca.openosp.openo.hospitalReportManager.xsd.DrugMeasure;
import ca.openosp.openo.hospitalReportManager.xsd.EnrollmentInfo;
import ca.openosp.openo.hospitalReportManager.xsd.HealthCard;
import ca.openosp.openo.hospitalReportManager.xsd.Height;
import ca.openosp.openo.hospitalReportManager.xsd.HypoglycemicEpisodes;
import ca.openosp.openo.hospitalReportManager.xsd.OmdCds;
import ca.openosp.openo.hospitalReportManager.xsd.PatientRecord;
import ca.openosp.openo.hospitalReportManager.xsd.PersonNameSimple;
import ca.openosp.openo.hospitalReportManager.xsd.PersonNameSimpleWithMiddleName;
import ca.openosp.openo.hospitalReportManager.xsd.PersonNameStandard;
import ca.openosp.openo.hospitalReportManager.xsd.PhoneNumber;
import ca.openosp.openo.hospitalReportManager.xsd.PostalZipCode;
import ca.openosp.openo.hospitalReportManager.xsd.ReportContent;
import ca.openosp.openo.hospitalReportManager.xsd.ReportsReceived;
import ca.openosp.openo.hospitalReportManager.xsd.ResidualInformation;
import ca.openosp.openo.hospitalReportManager.xsd.SelfMonitoringBloodGlucose;
import ca.openosp.openo.hospitalReportManager.xsd.SmokingPacks;
import ca.openosp.openo.hospitalReportManager.xsd.SmokingStatus;
import ca.openosp.openo.hospitalReportManager.xsd.TransactionInformation;
import ca.openosp.openo.hospitalReportManager.xsd.WaistCircumference;
import ca.openosp.openo.hospitalReportManager.xsd.Weight;
import ca.openosp.openo.hospitalReportManager.xsd.YnIndicator;
import ca.openosp.openo.hospitalReportManager.xsd.YnIndicatorAndBlank;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
@XmlRegistry
public class ObjectFactory {
    private static final QName _PhoneNumberNumber_QNAME = new QName("cds_dt", "number");
    private static final QName _PhoneNumberExchange_QNAME = new QName("cds_dt", "exchange");
    private static final QName _PhoneNumberExtension_QNAME = new QName("cds_dt", "extension");
    private static final QName _PhoneNumberPhoneNumber_QNAME = new QName("cds_dt", "phoneNumber");
    private static final QName _PhoneNumberAreaCode_QNAME = new QName("cds_dt", "areaCode");

    public PatientRecord createPatientRecord() {
        return new PatientRecord();
    }

    public SelfMonitoringBloodGlucose createSelfMonitoringBloodGlucose() {
        return new SelfMonitoringBloodGlucose();
    }

    public PersonNameSimpleWithMiddleName createPersonNameSimpleWithMiddleName() {
        return new PersonNameSimpleWithMiddleName();
    }

    public ReportsReceived.OBRContent createReportsReceivedOBRContent() {
        return new ReportsReceived.OBRContent();
    }

    public DiabetesComplicationScreening createDiabetesComplicationScreening() {
        return new DiabetesComplicationScreening();
    }

    public Weight createWeight() {
        return new Weight();
    }

    public PersonNameStandard.LegalName.FirstName createPersonNameStandardLegalNameFirstName() {
        return new PersonNameStandard.LegalName.FirstName();
    }

    public Demographics.PrimaryPhysician createDemographicsPrimaryPhysician() {
        return new Demographics.PrimaryPhysician();
    }

    public PersonNameStandard.OtherNames.OtherName createPersonNameStandardOtherNamesOtherName() {
        return new PersonNameStandard.OtherNames.OtherName();
    }

    public DrugMeasure createDrugMeasure() {
        return new DrugMeasure();
    }

    public PersonNameStandard createPersonNameStandard() {
        return new PersonNameStandard();
    }

    public Demographics createDemographics() {
        return new Demographics();
    }

    public PostalZipCode createPostalZipCode() {
        return new PostalZipCode();
    }

    public ResidualInformation createResidualInformation() {
        return new ResidualInformation();
    }

    public ReportsReceived createReportsReceived() {
        return new ReportsReceived();
    }

    public Address createAddress() {
        return new Address();
    }

    public DiabetesMotivationalCounselling createDiabetesMotivationalCounselling() {
        return new DiabetesMotivationalCounselling();
    }

    public Height createHeight() {
        return new Height();
    }

    public DateFullOrPartial createDateFullOrPartial() {
        return new DateFullOrPartial();
    }

    public EnrollmentInfo createEnrollmentInfo() {
        return new EnrollmentInfo();
    }

    public HypoglycemicEpisodes createHypoglycemicEpisodes() {
        return new HypoglycemicEpisodes();
    }

    public SmokingStatus createSmokingStatus() {
        return new SmokingStatus();
    }

    public AddressStructured createAddressStructured() {
        return new AddressStructured();
    }

    public TransactionInformation createTransactionInformation() {
        return new TransactionInformation();
    }

    public PersonNameStandard.OtherNames createPersonNameStandardOtherNames() {
        return new PersonNameStandard.OtherNames();
    }

    public YnIndicatorAndBlank createYnIndicatorAndBlank() {
        return new YnIndicatorAndBlank();
    }

    public PersonNameStandard.LegalName createPersonNameStandardLegalName() {
        return new PersonNameStandard.LegalName();
    }

    public PersonNameSimple createPersonNameSimple() {
        return new PersonNameSimple();
    }

    public Code createCode() {
        return new Code();
    }

    public PersonNameStandard.LegalName.OtherName createPersonNameStandardLegalNameOtherName() {
        return new PersonNameStandard.LegalName.OtherName();
    }

    public DiabetesEducationalSelfManagement createDiabetesEducationalSelfManagement() {
        return new DiabetesEducationalSelfManagement();
    }

    public PhoneNumber createPhoneNumber() {
        return new PhoneNumber();
    }

    public OmdCds createOmdCds() {
        return new OmdCds();
    }

    public YnIndicator createYnIndicator() {
        return new YnIndicator();
    }

    public PersonNameStandard.LegalName.LastName createPersonNameStandardLegalNameLastName() {
        return new PersonNameStandard.LegalName.LastName();
    }

    public ReportContent createReportContent() {
        return new ReportContent();
    }

    public DiabetesSelfManagementCollaborative createDiabetesSelfManagementCollaborative() {
        return new DiabetesSelfManagementCollaborative();
    }

    public Demographics.Contact createDemographicsContact() {
        return new Demographics.Contact();
    }

    public DiabetesSelfManagementChallenges createDiabetesSelfManagementChallenges() {
        return new DiabetesSelfManagementChallenges();
    }

    public BloodPressure createBloodPressure() {
        return new BloodPressure();
    }

    public WaistCircumference createWaistCircumference() {
        return new WaistCircumference();
    }

    public ResidualInformation.DataElement createResidualInformationDataElement() {
        return new ResidualInformation.DataElement();
    }

    public HealthCard createHealthCard() {
        return new HealthCard();
    }

    public SmokingPacks createSmokingPacks() {
        return new SmokingPacks();
    }

    @XmlElementDecl(namespace="cds_dt", name="number", scope=PhoneNumber.class)
    @XmlJavaTypeAdapter(value=CollapsedStringAdapter.class)
    public JAXBElement<String> createPhoneNumberNumber(String value) {
        return new JAXBElement(_PhoneNumberNumber_QNAME, String.class, PhoneNumber.class, (Object)value);
    }

    @XmlElementDecl(namespace="cds_dt", name="exchange", scope=PhoneNumber.class)
    @XmlJavaTypeAdapter(value=CollapsedStringAdapter.class)
    public JAXBElement<String> createPhoneNumberExchange(String value) {
        return new JAXBElement(_PhoneNumberExchange_QNAME, String.class, PhoneNumber.class, (Object)value);
    }

    @XmlElementDecl(namespace="cds_dt", name="extension", scope=PhoneNumber.class)
    @XmlJavaTypeAdapter(value=CollapsedStringAdapter.class)
    public JAXBElement<String> createPhoneNumberExtension(String value) {
        return new JAXBElement(_PhoneNumberExtension_QNAME, String.class, PhoneNumber.class, (Object)value);
    }

    @XmlElementDecl(namespace="cds_dt", name="phoneNumber", scope=PhoneNumber.class)
    public JAXBElement<String> createPhoneNumberPhoneNumber(String value) {
        return new JAXBElement(_PhoneNumberPhoneNumber_QNAME, String.class, PhoneNumber.class, (Object)value);
    }

    @XmlElementDecl(namespace="cds_dt", name="areaCode", scope=PhoneNumber.class)
    @XmlJavaTypeAdapter(value=CollapsedStringAdapter.class)
    public JAXBElement<String> createPhoneNumberAreaCode(String value) {
        return new JAXBElement(_PhoneNumberAreaCode_QNAME, String.class, PhoneNumber.class, (Object)value);
    }
}
