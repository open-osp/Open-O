package ca.openosp.openo.ar2005;

import org.apache.xmlbeans.xml.stream.XMLStreamException;
import org.apache.xmlbeans.xml.stream.XMLInputStream;
import org.w3c.dom.Node;
import javax.xml.stream.XMLStreamReader;
import java.io.Reader;
import java.io.InputStream;
import java.net.URL;
import java.io.IOException;
import java.io.File;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlDateTime;
import org.apache.xmlbeans.XmlDate;
import java.util.Calendar;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.XmlInt;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlObject;

public interface AR1 extends XmlObject
{
    public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(AR1.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9C023B7D67311A3187802DA7FD51EA38").resolveHandle("ar15fa5type");
    
    int getId();
    
    XmlInt xgetId();
    
    void setId(final int p0);
    
    void xsetId(final XmlInt p0);
    
    int getVersionID();
    
    XmlInt xgetVersionID();
    
    void setVersionID(final int p0);
    
    void xsetVersionID(final XmlInt p0);
    
    int getEpisodeId();
    
    XmlInt xgetEpisodeId();
    
    void setEpisodeId(final int p0);
    
    void xsetEpisodeId(final XmlInt p0);
    
    int getDemographicNo();
    
    XmlInt xgetDemographicNo();
    
    void setDemographicNo(final int p0);
    
    void xsetDemographicNo(final XmlInt p0);
    
    String getProviderNo();
    
    XmlString xgetProviderNo();
    
    void setProviderNo(final String p0);
    
    void xsetProviderNo(final XmlString p0);
    
    Calendar getFormCreated();
    
    XmlDate xgetFormCreated();
    
    void setFormCreated(final Calendar p0);
    
    void xsetFormCreated(final XmlDate p0);
    
    Calendar getFormEdited();
    
    XmlDateTime xgetFormEdited();
    
    void setFormEdited(final Calendar p0);
    
    void xsetFormEdited(final XmlDateTime p0);
    
    PatientInformation getPatientInformation();
    
    void setPatientInformation(final PatientInformation p0);
    
    PatientInformation addNewPatientInformation();
    
    PartnerInformation getPartnerInformation();
    
    void setPartnerInformation(final PartnerInformation p0);
    
    PartnerInformation addNewPartnerInformation();
    
    PractitionerInformation getPractitionerInformation();
    
    void setPractitionerInformation(final PractitionerInformation p0);
    
    PractitionerInformation addNewPractitionerInformation();
    
    PregnancyHistory getPregnancyHistory();
    
    void setPregnancyHistory(final PregnancyHistory p0);
    
    PregnancyHistory addNewPregnancyHistory();
    
    ObstetricalHistory getObstetricalHistory();
    
    void setObstetricalHistory(final ObstetricalHistory p0);
    
    ObstetricalHistory addNewObstetricalHistory();
    
    MedicalHistoryAndPhysicalExam getMedicalHistoryAndPhysicalExam();
    
    void setMedicalHistoryAndPhysicalExam(final MedicalHistoryAndPhysicalExam p0);
    
    MedicalHistoryAndPhysicalExam addNewMedicalHistoryAndPhysicalExam();
    
    InitialLaboratoryInvestigations getInitialLaboratoryInvestigations();
    
    void setInitialLaboratoryInvestigations(final InitialLaboratoryInvestigations p0);
    
    InitialLaboratoryInvestigations addNewInitialLaboratoryInvestigations();
    
    String getComments();
    
    XmlString xgetComments();
    
    void setComments(final String p0);
    
    void xsetComments(final XmlString p0);
    
    String getExtraComments();
    
    XmlString xgetExtraComments();
    
    void setExtraComments(final String p0);
    
    void xsetExtraComments(final XmlString p0);
    
    SignatureType getSignatures();
    
    void setSignatures(final SignatureType p0);
    
    SignatureType addNewSignatures();
    
    public static final class Factory
    {
        public static AR1 newInstance() {
            return (AR1)XmlBeans.getContextTypeLoader().newInstance(AR1.type, (XmlOptions)null);
        }
        
        public static AR1 newInstance(final XmlOptions options) {
            return (AR1)XmlBeans.getContextTypeLoader().newInstance(AR1.type, options);
        }
        
        public static AR1 parse(final String xmlAsString) throws XmlException {
            return (AR1)XmlBeans.getContextTypeLoader().parse(xmlAsString, AR1.type, (XmlOptions)null);
        }
        
        public static AR1 parse(final String xmlAsString, final XmlOptions options) throws XmlException {
            return (AR1)XmlBeans.getContextTypeLoader().parse(xmlAsString, AR1.type, options);
        }
        
        public static AR1 parse(final File file) throws XmlException, IOException {
            return (AR1)XmlBeans.getContextTypeLoader().parse(file, AR1.type, (XmlOptions)null);
        }
        
        public static AR1 parse(final File file, final XmlOptions options) throws XmlException, IOException {
            return (AR1)XmlBeans.getContextTypeLoader().parse(file, AR1.type, options);
        }
        
        public static AR1 parse(final URL u) throws XmlException, IOException {
            return (AR1)XmlBeans.getContextTypeLoader().parse(u, AR1.type, (XmlOptions)null);
        }
        
        public static AR1 parse(final URL u, final XmlOptions options) throws XmlException, IOException {
            return (AR1)XmlBeans.getContextTypeLoader().parse(u, AR1.type, options);
        }
        
        public static AR1 parse(final InputStream is) throws XmlException, IOException {
            return (AR1)XmlBeans.getContextTypeLoader().parse(is, AR1.type, (XmlOptions)null);
        }
        
        public static AR1 parse(final InputStream is, final XmlOptions options) throws XmlException, IOException {
            return (AR1)XmlBeans.getContextTypeLoader().parse(is, AR1.type, options);
        }
        
        public static AR1 parse(final Reader r) throws XmlException, IOException {
            return (AR1)XmlBeans.getContextTypeLoader().parse(r, AR1.type, (XmlOptions)null);
        }
        
        public static AR1 parse(final Reader r, final XmlOptions options) throws XmlException, IOException {
            return (AR1)XmlBeans.getContextTypeLoader().parse(r, AR1.type, options);
        }
        
        public static AR1 parse(final XMLStreamReader sr) throws XmlException {
            return (AR1)XmlBeans.getContextTypeLoader().parse(sr, AR1.type, (XmlOptions)null);
        }
        
        public static AR1 parse(final XMLStreamReader sr, final XmlOptions options) throws XmlException {
            return (AR1)XmlBeans.getContextTypeLoader().parse(sr, AR1.type, options);
        }
        
        public static AR1 parse(final Node node) throws XmlException {
            return (AR1)XmlBeans.getContextTypeLoader().parse(node, AR1.type, (XmlOptions)null);
        }
        
        public static AR1 parse(final Node node, final XmlOptions options) throws XmlException {
            return (AR1)XmlBeans.getContextTypeLoader().parse(node, AR1.type, options);
        }
        
        @Deprecated
        public static AR1 parse(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return (AR1)XmlBeans.getContextTypeLoader().parse(xis, AR1.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static AR1 parse(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return (AR1)XmlBeans.getContextTypeLoader().parse(xis, AR1.type, options);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, AR1.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, AR1.type, options);
        }
        
        private Factory() {
        }
    }
}
