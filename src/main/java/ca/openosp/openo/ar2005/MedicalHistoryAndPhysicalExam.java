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
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlObject;

public interface MedicalHistoryAndPhysicalExam extends XmlObject
{
    public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(MedicalHistoryAndPhysicalExam.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9C023B7D67311A3187802DA7FD51EA38").resolveHandle("medicalhistoryandphysicalexam176ftype");
    
    CurrentPregnancyType getCurrentPregnancy();
    
    void setCurrentPregnancy(final CurrentPregnancyType p0);
    
    CurrentPregnancyType addNewCurrentPregnancy();
    
    MedicalHistoryType getMedicalHistory();
    
    void setMedicalHistory(final MedicalHistoryType p0);
    
    MedicalHistoryType addNewMedicalHistory();
    
    GenericHistoryType getGenericHistory();
    
    void setGenericHistory(final GenericHistoryType p0);
    
    GenericHistoryType addNewGenericHistory();
    
    InfectiousDiseaseType getInfectiousDisease();
    
    void setInfectiousDisease(final InfectiousDiseaseType p0);
    
    InfectiousDiseaseType addNewInfectiousDisease();
    
    PsychosocialType getPsychosocial();
    
    void setPsychosocial(final PsychosocialType p0);
    
    PsychosocialType addNewPsychosocial();
    
    FamilyHistoryType getFamilyHistory();
    
    void setFamilyHistory(final FamilyHistoryType p0);
    
    FamilyHistoryType addNewFamilyHistory();
    
    PhysicalExaminationType getPhysicalExamination();
    
    void setPhysicalExamination(final PhysicalExaminationType p0);
    
    PhysicalExaminationType addNewPhysicalExamination();
    
    public static final class Factory
    {
        public static MedicalHistoryAndPhysicalExam newInstance() {
            return (MedicalHistoryAndPhysicalExam)XmlBeans.getContextTypeLoader().newInstance(MedicalHistoryAndPhysicalExam.type, (XmlOptions)null);
        }
        
        public static MedicalHistoryAndPhysicalExam newInstance(final XmlOptions options) {
            return (MedicalHistoryAndPhysicalExam)XmlBeans.getContextTypeLoader().newInstance(MedicalHistoryAndPhysicalExam.type, options);
        }
        
        public static MedicalHistoryAndPhysicalExam parse(final String xmlAsString) throws XmlException {
            return (MedicalHistoryAndPhysicalExam)XmlBeans.getContextTypeLoader().parse(xmlAsString, MedicalHistoryAndPhysicalExam.type, (XmlOptions)null);
        }
        
        public static MedicalHistoryAndPhysicalExam parse(final String xmlAsString, final XmlOptions options) throws XmlException {
            return (MedicalHistoryAndPhysicalExam)XmlBeans.getContextTypeLoader().parse(xmlAsString, MedicalHistoryAndPhysicalExam.type, options);
        }
        
        public static MedicalHistoryAndPhysicalExam parse(final File file) throws XmlException, IOException {
            return (MedicalHistoryAndPhysicalExam)XmlBeans.getContextTypeLoader().parse(file, MedicalHistoryAndPhysicalExam.type, (XmlOptions)null);
        }
        
        public static MedicalHistoryAndPhysicalExam parse(final File file, final XmlOptions options) throws XmlException, IOException {
            return (MedicalHistoryAndPhysicalExam)XmlBeans.getContextTypeLoader().parse(file, MedicalHistoryAndPhysicalExam.type, options);
        }
        
        public static MedicalHistoryAndPhysicalExam parse(final URL u) throws XmlException, IOException {
            return (MedicalHistoryAndPhysicalExam)XmlBeans.getContextTypeLoader().parse(u, MedicalHistoryAndPhysicalExam.type, (XmlOptions)null);
        }
        
        public static MedicalHistoryAndPhysicalExam parse(final URL u, final XmlOptions options) throws XmlException, IOException {
            return (MedicalHistoryAndPhysicalExam)XmlBeans.getContextTypeLoader().parse(u, MedicalHistoryAndPhysicalExam.type, options);
        }
        
        public static MedicalHistoryAndPhysicalExam parse(final InputStream is) throws XmlException, IOException {
            return (MedicalHistoryAndPhysicalExam)XmlBeans.getContextTypeLoader().parse(is, MedicalHistoryAndPhysicalExam.type, (XmlOptions)null);
        }
        
        public static MedicalHistoryAndPhysicalExam parse(final InputStream is, final XmlOptions options) throws XmlException, IOException {
            return (MedicalHistoryAndPhysicalExam)XmlBeans.getContextTypeLoader().parse(is, MedicalHistoryAndPhysicalExam.type, options);
        }
        
        public static MedicalHistoryAndPhysicalExam parse(final Reader r) throws XmlException, IOException {
            return (MedicalHistoryAndPhysicalExam)XmlBeans.getContextTypeLoader().parse(r, MedicalHistoryAndPhysicalExam.type, (XmlOptions)null);
        }
        
        public static MedicalHistoryAndPhysicalExam parse(final Reader r, final XmlOptions options) throws XmlException, IOException {
            return (MedicalHistoryAndPhysicalExam)XmlBeans.getContextTypeLoader().parse(r, MedicalHistoryAndPhysicalExam.type, options);
        }
        
        public static MedicalHistoryAndPhysicalExam parse(final XMLStreamReader sr) throws XmlException {
            return (MedicalHistoryAndPhysicalExam)XmlBeans.getContextTypeLoader().parse(sr, MedicalHistoryAndPhysicalExam.type, (XmlOptions)null);
        }
        
        public static MedicalHistoryAndPhysicalExam parse(final XMLStreamReader sr, final XmlOptions options) throws XmlException {
            return (MedicalHistoryAndPhysicalExam)XmlBeans.getContextTypeLoader().parse(sr, MedicalHistoryAndPhysicalExam.type, options);
        }
        
        public static MedicalHistoryAndPhysicalExam parse(final Node node) throws XmlException {
            return (MedicalHistoryAndPhysicalExam)XmlBeans.getContextTypeLoader().parse(node, MedicalHistoryAndPhysicalExam.type, (XmlOptions)null);
        }
        
        public static MedicalHistoryAndPhysicalExam parse(final Node node, final XmlOptions options) throws XmlException {
            return (MedicalHistoryAndPhysicalExam)XmlBeans.getContextTypeLoader().parse(node, MedicalHistoryAndPhysicalExam.type, options);
        }
        
        @Deprecated
        public static MedicalHistoryAndPhysicalExam parse(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return (MedicalHistoryAndPhysicalExam)XmlBeans.getContextTypeLoader().parse(xis, MedicalHistoryAndPhysicalExam.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static MedicalHistoryAndPhysicalExam parse(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return (MedicalHistoryAndPhysicalExam)XmlBeans.getContextTypeLoader().parse(xis, MedicalHistoryAndPhysicalExam.type, options);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, MedicalHistoryAndPhysicalExam.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, MedicalHistoryAndPhysicalExam.type, options);
        }
        
        private Factory() {
        }
    }
}
