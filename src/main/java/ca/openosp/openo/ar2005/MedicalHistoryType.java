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
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlObject;

public interface MedicalHistoryType extends XmlObject
{
    public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(MedicalHistoryType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9C023B7D67311A3187802DA7FD51EA38").resolveHandle("medicalhistorytype49f4type");
    
    YesNoNullType getHypertension();
    
    void setHypertension(final YesNoNullType p0);
    
    YesNoNullType addNewHypertension();
    
    YesNoNullType getEndorince();
    
    void setEndorince(final YesNoNullType p0);
    
    YesNoNullType addNewEndorince();
    
    YesNoNullType getUrinaryTract();
    
    void setUrinaryTract(final YesNoNullType p0);
    
    YesNoNullType addNewUrinaryTract();
    
    YesNoNullType getCardiac();
    
    void setCardiac(final YesNoNullType p0);
    
    YesNoNullType addNewCardiac();
    
    YesNoNullType getLiver();
    
    void setLiver(final YesNoNullType p0);
    
    YesNoNullType addNewLiver();
    
    YesNoNullType getGynaecology();
    
    void setGynaecology(final YesNoNullType p0);
    
    YesNoNullType addNewGynaecology();
    
    YesNoNullType getHem();
    
    void setHem(final YesNoNullType p0);
    
    YesNoNullType addNewHem();
    
    YesNoNullType getSurgeries();
    
    void setSurgeries(final YesNoNullType p0);
    
    YesNoNullType addNewSurgeries();
    
    YesNoNullType getBloodTransfusion();
    
    void setBloodTransfusion(final YesNoNullType p0);
    
    YesNoNullType addNewBloodTransfusion();
    
    YesNoNullType getAnesthetics();
    
    void setAnesthetics(final YesNoNullType p0);
    
    YesNoNullType addNewAnesthetics();
    
    YesNoNullType getPsychiatry();
    
    void setPsychiatry(final YesNoNullType p0);
    
    YesNoNullType addNewPsychiatry();
    
    YesNoNullType getEpilepsy();
    
    void setEpilepsy(final YesNoNullType p0);
    
    YesNoNullType addNewEpilepsy();
    
    String getOtherDescr();
    
    XmlString xgetOtherDescr();
    
    void setOtherDescr(final String p0);
    
    void xsetOtherDescr(final XmlString p0);
    
    YesNoNullType getOther();
    
    void setOther(final YesNoNullType p0);
    
    YesNoNullType addNewOther();
    
    public static final class Factory
    {
        public static MedicalHistoryType newInstance() {
            return (MedicalHistoryType)XmlBeans.getContextTypeLoader().newInstance(MedicalHistoryType.type, (XmlOptions)null);
        }
        
        public static MedicalHistoryType newInstance(final XmlOptions options) {
            return (MedicalHistoryType)XmlBeans.getContextTypeLoader().newInstance(MedicalHistoryType.type, options);
        }
        
        public static MedicalHistoryType parse(final String xmlAsString) throws XmlException {
            return (MedicalHistoryType)XmlBeans.getContextTypeLoader().parse(xmlAsString, MedicalHistoryType.type, (XmlOptions)null);
        }
        
        public static MedicalHistoryType parse(final String xmlAsString, final XmlOptions options) throws XmlException {
            return (MedicalHistoryType)XmlBeans.getContextTypeLoader().parse(xmlAsString, MedicalHistoryType.type, options);
        }
        
        public static MedicalHistoryType parse(final File file) throws XmlException, IOException {
            return (MedicalHistoryType)XmlBeans.getContextTypeLoader().parse(file, MedicalHistoryType.type, (XmlOptions)null);
        }
        
        public static MedicalHistoryType parse(final File file, final XmlOptions options) throws XmlException, IOException {
            return (MedicalHistoryType)XmlBeans.getContextTypeLoader().parse(file, MedicalHistoryType.type, options);
        }
        
        public static MedicalHistoryType parse(final URL u) throws XmlException, IOException {
            return (MedicalHistoryType)XmlBeans.getContextTypeLoader().parse(u, MedicalHistoryType.type, (XmlOptions)null);
        }
        
        public static MedicalHistoryType parse(final URL u, final XmlOptions options) throws XmlException, IOException {
            return (MedicalHistoryType)XmlBeans.getContextTypeLoader().parse(u, MedicalHistoryType.type, options);
        }
        
        public static MedicalHistoryType parse(final InputStream is) throws XmlException, IOException {
            return (MedicalHistoryType)XmlBeans.getContextTypeLoader().parse(is, MedicalHistoryType.type, (XmlOptions)null);
        }
        
        public static MedicalHistoryType parse(final InputStream is, final XmlOptions options) throws XmlException, IOException {
            return (MedicalHistoryType)XmlBeans.getContextTypeLoader().parse(is, MedicalHistoryType.type, options);
        }
        
        public static MedicalHistoryType parse(final Reader r) throws XmlException, IOException {
            return (MedicalHistoryType)XmlBeans.getContextTypeLoader().parse(r, MedicalHistoryType.type, (XmlOptions)null);
        }
        
        public static MedicalHistoryType parse(final Reader r, final XmlOptions options) throws XmlException, IOException {
            return (MedicalHistoryType)XmlBeans.getContextTypeLoader().parse(r, MedicalHistoryType.type, options);
        }
        
        public static MedicalHistoryType parse(final XMLStreamReader sr) throws XmlException {
            return (MedicalHistoryType)XmlBeans.getContextTypeLoader().parse(sr, MedicalHistoryType.type, (XmlOptions)null);
        }
        
        public static MedicalHistoryType parse(final XMLStreamReader sr, final XmlOptions options) throws XmlException {
            return (MedicalHistoryType)XmlBeans.getContextTypeLoader().parse(sr, MedicalHistoryType.type, options);
        }
        
        public static MedicalHistoryType parse(final Node node) throws XmlException {
            return (MedicalHistoryType)XmlBeans.getContextTypeLoader().parse(node, MedicalHistoryType.type, (XmlOptions)null);
        }
        
        public static MedicalHistoryType parse(final Node node, final XmlOptions options) throws XmlException {
            return (MedicalHistoryType)XmlBeans.getContextTypeLoader().parse(node, MedicalHistoryType.type, options);
        }
        
        @Deprecated
        public static MedicalHistoryType parse(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return (MedicalHistoryType)XmlBeans.getContextTypeLoader().parse(xis, MedicalHistoryType.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static MedicalHistoryType parse(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return (MedicalHistoryType)XmlBeans.getContextTypeLoader().parse(xis, MedicalHistoryType.type, options);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, MedicalHistoryType.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, MedicalHistoryType.type, options);
        }
        
        private Factory() {
        }
    }
}
