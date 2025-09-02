package ca.openosp.openo.ckd;

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

public interface CKDConfig extends XmlObject
{
    public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(CKDConfig.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9F21579FEC5BB34270776B6AF571836F").resolveHandle("ckdconfig3b4dtype");
    
    DxCodes getDxCodes();
    
    void setDxCodes(final DxCodes p0);
    
    DxCodes addNewDxCodes();
    
    Bp getBp();
    
    void setBp(final Bp p0);
    
    Bp addNewBp();
    
    Hx getHx();
    
    void setHx(final Hx p0);
    
    Hx addNewHx();
    
    Drugs getDrugs();
    
    boolean isSetDrugs();
    
    void setDrugs(final Drugs p0);
    
    Drugs addNewDrugs();
    
    void unsetDrugs();
    
    Excludes getExcludes();
    
    boolean isSetExcludes();
    
    void setExcludes(final Excludes p0);
    
    Excludes addNewExcludes();
    
    void unsetExcludes();
    
    public static final class Factory
    {
        public static CKDConfig newInstance() {
            return (CKDConfig)XmlBeans.getContextTypeLoader().newInstance(CKDConfig.type, (XmlOptions)null);
        }
        
        public static CKDConfig newInstance(final XmlOptions options) {
            return (CKDConfig)XmlBeans.getContextTypeLoader().newInstance(CKDConfig.type, options);
        }
        
        public static CKDConfig parse(final String xmlAsString) throws XmlException {
            return (CKDConfig)XmlBeans.getContextTypeLoader().parse(xmlAsString, CKDConfig.type, (XmlOptions)null);
        }
        
        public static CKDConfig parse(final String xmlAsString, final XmlOptions options) throws XmlException {
            return (CKDConfig)XmlBeans.getContextTypeLoader().parse(xmlAsString, CKDConfig.type, options);
        }
        
        public static CKDConfig parse(final File file) throws XmlException, IOException {
            return (CKDConfig)XmlBeans.getContextTypeLoader().parse(file, CKDConfig.type, (XmlOptions)null);
        }
        
        public static CKDConfig parse(final File file, final XmlOptions options) throws XmlException, IOException {
            return (CKDConfig)XmlBeans.getContextTypeLoader().parse(file, CKDConfig.type, options);
        }
        
        public static CKDConfig parse(final URL u) throws XmlException, IOException {
            return (CKDConfig)XmlBeans.getContextTypeLoader().parse(u, CKDConfig.type, (XmlOptions)null);
        }
        
        public static CKDConfig parse(final URL u, final XmlOptions options) throws XmlException, IOException {
            return (CKDConfig)XmlBeans.getContextTypeLoader().parse(u, CKDConfig.type, options);
        }
        
        public static CKDConfig parse(final InputStream is) throws XmlException, IOException {
            return (CKDConfig)XmlBeans.getContextTypeLoader().parse(is, CKDConfig.type, (XmlOptions)null);
        }
        
        public static CKDConfig parse(final InputStream is, final XmlOptions options) throws XmlException, IOException {
            return (CKDConfig)XmlBeans.getContextTypeLoader().parse(is, CKDConfig.type, options);
        }
        
        public static CKDConfig parse(final Reader r) throws XmlException, IOException {
            return (CKDConfig)XmlBeans.getContextTypeLoader().parse(r, CKDConfig.type, (XmlOptions)null);
        }
        
        public static CKDConfig parse(final Reader r, final XmlOptions options) throws XmlException, IOException {
            return (CKDConfig)XmlBeans.getContextTypeLoader().parse(r, CKDConfig.type, options);
        }
        
        public static CKDConfig parse(final XMLStreamReader sr) throws XmlException {
            return (CKDConfig)XmlBeans.getContextTypeLoader().parse(sr, CKDConfig.type, (XmlOptions)null);
        }
        
        public static CKDConfig parse(final XMLStreamReader sr, final XmlOptions options) throws XmlException {
            return (CKDConfig)XmlBeans.getContextTypeLoader().parse(sr, CKDConfig.type, options);
        }
        
        public static CKDConfig parse(final Node node) throws XmlException {
            return (CKDConfig)XmlBeans.getContextTypeLoader().parse(node, CKDConfig.type, (XmlOptions)null);
        }
        
        public static CKDConfig parse(final Node node, final XmlOptions options) throws XmlException {
            return (CKDConfig)XmlBeans.getContextTypeLoader().parse(node, CKDConfig.type, options);
        }
        
        @Deprecated
        public static CKDConfig parse(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return (CKDConfig)XmlBeans.getContextTypeLoader().parse(xis, CKDConfig.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static CKDConfig parse(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return (CKDConfig)XmlBeans.getContextTypeLoader().parse(xis, CKDConfig.type, options);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, CKDConfig.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, CKDConfig.type, options);
        }
        
        private Factory() {
        }
    }
}
