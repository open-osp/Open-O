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

public interface Hx extends XmlObject
{
    public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(Hx.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9F21579FEC5BB34270776B6AF571836F").resolveHandle("hxed89type");
    
    Issues getIssues();
    
    void setIssues(final Issues p0);
    
    Issues addNewIssues();
    
    SearchText getSearchtext();
    
    void setSearchtext(final SearchText p0);
    
    SearchText addNewSearchtext();
    
    public static final class Factory
    {
        public static Hx newInstance() {
            return (Hx)XmlBeans.getContextTypeLoader().newInstance(Hx.type, (XmlOptions)null);
        }
        
        public static Hx newInstance(final XmlOptions options) {
            return (Hx)XmlBeans.getContextTypeLoader().newInstance(Hx.type, options);
        }
        
        public static Hx parse(final String xmlAsString) throws XmlException {
            return (Hx)XmlBeans.getContextTypeLoader().parse(xmlAsString, Hx.type, (XmlOptions)null);
        }
        
        public static Hx parse(final String xmlAsString, final XmlOptions options) throws XmlException {
            return (Hx)XmlBeans.getContextTypeLoader().parse(xmlAsString, Hx.type, options);
        }
        
        public static Hx parse(final File file) throws XmlException, IOException {
            return (Hx)XmlBeans.getContextTypeLoader().parse(file, Hx.type, (XmlOptions)null);
        }
        
        public static Hx parse(final File file, final XmlOptions options) throws XmlException, IOException {
            return (Hx)XmlBeans.getContextTypeLoader().parse(file, Hx.type, options);
        }
        
        public static Hx parse(final URL u) throws XmlException, IOException {
            return (Hx)XmlBeans.getContextTypeLoader().parse(u, Hx.type, (XmlOptions)null);
        }
        
        public static Hx parse(final URL u, final XmlOptions options) throws XmlException, IOException {
            return (Hx)XmlBeans.getContextTypeLoader().parse(u, Hx.type, options);
        }
        
        public static Hx parse(final InputStream is) throws XmlException, IOException {
            return (Hx)XmlBeans.getContextTypeLoader().parse(is, Hx.type, (XmlOptions)null);
        }
        
        public static Hx parse(final InputStream is, final XmlOptions options) throws XmlException, IOException {
            return (Hx)XmlBeans.getContextTypeLoader().parse(is, Hx.type, options);
        }
        
        public static Hx parse(final Reader r) throws XmlException, IOException {
            return (Hx)XmlBeans.getContextTypeLoader().parse(r, Hx.type, (XmlOptions)null);
        }
        
        public static Hx parse(final Reader r, final XmlOptions options) throws XmlException, IOException {
            return (Hx)XmlBeans.getContextTypeLoader().parse(r, Hx.type, options);
        }
        
        public static Hx parse(final XMLStreamReader sr) throws XmlException {
            return (Hx)XmlBeans.getContextTypeLoader().parse(sr, Hx.type, (XmlOptions)null);
        }
        
        public static Hx parse(final XMLStreamReader sr, final XmlOptions options) throws XmlException {
            return (Hx)XmlBeans.getContextTypeLoader().parse(sr, Hx.type, options);
        }
        
        public static Hx parse(final Node node) throws XmlException {
            return (Hx)XmlBeans.getContextTypeLoader().parse(node, Hx.type, (XmlOptions)null);
        }
        
        public static Hx parse(final Node node, final XmlOptions options) throws XmlException {
            return (Hx)XmlBeans.getContextTypeLoader().parse(node, Hx.type, options);
        }
        
        @Deprecated
        public static Hx parse(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return (Hx)XmlBeans.getContextTypeLoader().parse(xis, Hx.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static Hx parse(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return (Hx)XmlBeans.getContextTypeLoader().parse(xis, Hx.type, options);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, Hx.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, Hx.type, options);
        }
        
        private Factory() {
        }
    }
}
