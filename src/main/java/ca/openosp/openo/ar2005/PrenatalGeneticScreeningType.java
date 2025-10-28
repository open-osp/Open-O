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
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlObject;

public interface PrenatalGeneticScreeningType extends XmlObject
{
    public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(PrenatalGeneticScreeningType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9C023B7D67311A3187802DA7FD51EA38").resolveHandle("prenatalgeneticscreeningtype87f7type");
    
    String getMSSIPSFTS();
    
    XmlString xgetMSSIPSFTS();
    
    void setMSSIPSFTS(final String p0);
    
    void xsetMSSIPSFTS(final XmlString p0);
    
    String getEDBCVS();
    
    XmlString xgetEDBCVS();
    
    void setEDBCVS(final String p0);
    
    void xsetEDBCVS(final XmlString p0);
    
    String getMSAFP();
    
    XmlString xgetMSAFP();
    
    void setMSAFP(final String p0);
    
    void xsetMSAFP(final XmlString p0);
    
    CustomLab getCustomLab1();
    
    void setCustomLab1(final CustomLab p0);
    
    CustomLab addNewCustomLab1();
    
    boolean getDeclined();
    
    XmlBoolean xgetDeclined();
    
    void setDeclined(final boolean p0);
    
    void xsetDeclined(final XmlBoolean p0);
    
    public static final class Factory
    {
        public static PrenatalGeneticScreeningType newInstance() {
            return (PrenatalGeneticScreeningType)XmlBeans.getContextTypeLoader().newInstance(PrenatalGeneticScreeningType.type, (XmlOptions)null);
        }
        
        public static PrenatalGeneticScreeningType newInstance(final XmlOptions options) {
            return (PrenatalGeneticScreeningType)XmlBeans.getContextTypeLoader().newInstance(PrenatalGeneticScreeningType.type, options);
        }
        
        public static PrenatalGeneticScreeningType parse(final String xmlAsString) throws XmlException {
            return (PrenatalGeneticScreeningType)XmlBeans.getContextTypeLoader().parse(xmlAsString, PrenatalGeneticScreeningType.type, (XmlOptions)null);
        }
        
        public static PrenatalGeneticScreeningType parse(final String xmlAsString, final XmlOptions options) throws XmlException {
            return (PrenatalGeneticScreeningType)XmlBeans.getContextTypeLoader().parse(xmlAsString, PrenatalGeneticScreeningType.type, options);
        }
        
        public static PrenatalGeneticScreeningType parse(final File file) throws XmlException, IOException {
            return (PrenatalGeneticScreeningType)XmlBeans.getContextTypeLoader().parse(file, PrenatalGeneticScreeningType.type, (XmlOptions)null);
        }
        
        public static PrenatalGeneticScreeningType parse(final File file, final XmlOptions options) throws XmlException, IOException {
            return (PrenatalGeneticScreeningType)XmlBeans.getContextTypeLoader().parse(file, PrenatalGeneticScreeningType.type, options);
        }
        
        public static PrenatalGeneticScreeningType parse(final URL u) throws XmlException, IOException {
            return (PrenatalGeneticScreeningType)XmlBeans.getContextTypeLoader().parse(u, PrenatalGeneticScreeningType.type, (XmlOptions)null);
        }
        
        public static PrenatalGeneticScreeningType parse(final URL u, final XmlOptions options) throws XmlException, IOException {
            return (PrenatalGeneticScreeningType)XmlBeans.getContextTypeLoader().parse(u, PrenatalGeneticScreeningType.type, options);
        }
        
        public static PrenatalGeneticScreeningType parse(final InputStream is) throws XmlException, IOException {
            return (PrenatalGeneticScreeningType)XmlBeans.getContextTypeLoader().parse(is, PrenatalGeneticScreeningType.type, (XmlOptions)null);
        }
        
        public static PrenatalGeneticScreeningType parse(final InputStream is, final XmlOptions options) throws XmlException, IOException {
            return (PrenatalGeneticScreeningType)XmlBeans.getContextTypeLoader().parse(is, PrenatalGeneticScreeningType.type, options);
        }
        
        public static PrenatalGeneticScreeningType parse(final Reader r) throws XmlException, IOException {
            return (PrenatalGeneticScreeningType)XmlBeans.getContextTypeLoader().parse(r, PrenatalGeneticScreeningType.type, (XmlOptions)null);
        }
        
        public static PrenatalGeneticScreeningType parse(final Reader r, final XmlOptions options) throws XmlException, IOException {
            return (PrenatalGeneticScreeningType)XmlBeans.getContextTypeLoader().parse(r, PrenatalGeneticScreeningType.type, options);
        }
        
        public static PrenatalGeneticScreeningType parse(final XMLStreamReader sr) throws XmlException {
            return (PrenatalGeneticScreeningType)XmlBeans.getContextTypeLoader().parse(sr, PrenatalGeneticScreeningType.type, (XmlOptions)null);
        }
        
        public static PrenatalGeneticScreeningType parse(final XMLStreamReader sr, final XmlOptions options) throws XmlException {
            return (PrenatalGeneticScreeningType)XmlBeans.getContextTypeLoader().parse(sr, PrenatalGeneticScreeningType.type, options);
        }
        
        public static PrenatalGeneticScreeningType parse(final Node node) throws XmlException {
            return (PrenatalGeneticScreeningType)XmlBeans.getContextTypeLoader().parse(node, PrenatalGeneticScreeningType.type, (XmlOptions)null);
        }
        
        public static PrenatalGeneticScreeningType parse(final Node node, final XmlOptions options) throws XmlException {
            return (PrenatalGeneticScreeningType)XmlBeans.getContextTypeLoader().parse(node, PrenatalGeneticScreeningType.type, options);
        }
        
        @Deprecated
        public static PrenatalGeneticScreeningType parse(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return (PrenatalGeneticScreeningType)XmlBeans.getContextTypeLoader().parse(xis, PrenatalGeneticScreeningType.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static PrenatalGeneticScreeningType parse(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return (PrenatalGeneticScreeningType)XmlBeans.getContextTypeLoader().parse(xis, PrenatalGeneticScreeningType.type, options);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, PrenatalGeneticScreeningType.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, PrenatalGeneticScreeningType.type, options);
        }
        
        private Factory() {
        }
    }
}
