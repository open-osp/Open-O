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

public interface CkdConfigDocument extends XmlObject
{
    public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(CkdConfigDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9F21579FEC5BB34270776B6AF571836F").resolveHandle("ckdconfiga2b6doctype");
    
    CKDConfig getCkdConfig();
    
    void setCkdConfig(final CKDConfig p0);
    
    CKDConfig addNewCkdConfig();
    
    public static final class Factory
    {
        public static CkdConfigDocument newInstance() {
            return (CkdConfigDocument)XmlBeans.getContextTypeLoader().newInstance(CkdConfigDocument.type, (XmlOptions)null);
        }
        
        public static CkdConfigDocument newInstance(final XmlOptions options) {
            return (CkdConfigDocument)XmlBeans.getContextTypeLoader().newInstance(CkdConfigDocument.type, options);
        }
        
        public static CkdConfigDocument parse(final String xmlAsString) throws XmlException {
            return (CkdConfigDocument)XmlBeans.getContextTypeLoader().parse(xmlAsString, CkdConfigDocument.type, (XmlOptions)null);
        }
        
        public static CkdConfigDocument parse(final String xmlAsString, final XmlOptions options) throws XmlException {
            return (CkdConfigDocument)XmlBeans.getContextTypeLoader().parse(xmlAsString, CkdConfigDocument.type, options);
        }
        
        public static CkdConfigDocument parse(final File file) throws XmlException, IOException {
            return (CkdConfigDocument)XmlBeans.getContextTypeLoader().parse(file, CkdConfigDocument.type, (XmlOptions)null);
        }
        
        public static CkdConfigDocument parse(final File file, final XmlOptions options) throws XmlException, IOException {
            return (CkdConfigDocument)XmlBeans.getContextTypeLoader().parse(file, CkdConfigDocument.type, options);
        }
        
        public static CkdConfigDocument parse(final URL u) throws XmlException, IOException {
            return (CkdConfigDocument)XmlBeans.getContextTypeLoader().parse(u, CkdConfigDocument.type, (XmlOptions)null);
        }
        
        public static CkdConfigDocument parse(final URL u, final XmlOptions options) throws XmlException, IOException {
            return (CkdConfigDocument)XmlBeans.getContextTypeLoader().parse(u, CkdConfigDocument.type, options);
        }
        
        public static CkdConfigDocument parse(final InputStream is) throws XmlException, IOException {
            return (CkdConfigDocument)XmlBeans.getContextTypeLoader().parse(is, CkdConfigDocument.type, (XmlOptions)null);
        }
        
        public static CkdConfigDocument parse(final InputStream is, final XmlOptions options) throws XmlException, IOException {
            return (CkdConfigDocument)XmlBeans.getContextTypeLoader().parse(is, CkdConfigDocument.type, options);
        }
        
        public static CkdConfigDocument parse(final Reader r) throws XmlException, IOException {
            return (CkdConfigDocument)XmlBeans.getContextTypeLoader().parse(r, CkdConfigDocument.type, (XmlOptions)null);
        }
        
        public static CkdConfigDocument parse(final Reader r, final XmlOptions options) throws XmlException, IOException {
            return (CkdConfigDocument)XmlBeans.getContextTypeLoader().parse(r, CkdConfigDocument.type, options);
        }
        
        public static CkdConfigDocument parse(final XMLStreamReader sr) throws XmlException {
            return (CkdConfigDocument)XmlBeans.getContextTypeLoader().parse(sr, CkdConfigDocument.type, (XmlOptions)null);
        }
        
        public static CkdConfigDocument parse(final XMLStreamReader sr, final XmlOptions options) throws XmlException {
            return (CkdConfigDocument)XmlBeans.getContextTypeLoader().parse(sr, CkdConfigDocument.type, options);
        }
        
        public static CkdConfigDocument parse(final Node node) throws XmlException {
            return (CkdConfigDocument)XmlBeans.getContextTypeLoader().parse(node, CkdConfigDocument.type, (XmlOptions)null);
        }
        
        public static CkdConfigDocument parse(final Node node, final XmlOptions options) throws XmlException {
            return (CkdConfigDocument)XmlBeans.getContextTypeLoader().parse(node, CkdConfigDocument.type, options);
        }
        
        @Deprecated
        public static CkdConfigDocument parse(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return (CkdConfigDocument)XmlBeans.getContextTypeLoader().parse(xis, CkdConfigDocument.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static CkdConfigDocument parse(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return (CkdConfigDocument)XmlBeans.getContextTypeLoader().parse(xis, CkdConfigDocument.type, options);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, CkdConfigDocument.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, CkdConfigDocument.type, options);
        }
        
        private Factory() {
        }
    }
}
