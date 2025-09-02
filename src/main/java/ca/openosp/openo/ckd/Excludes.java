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
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlObject;

public interface Excludes extends XmlObject
{
    public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(Excludes.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9F21579FEC5BB34270776B6AF571836F").resolveHandle("excludes44a0type");
    
    String[] getExcludeArray();
    
    String getExcludeArray(final int p0);
    
    XmlString[] xgetExcludeArray();
    
    XmlString xgetExcludeArray(final int p0);
    
    int sizeOfExcludeArray();
    
    void setExcludeArray(final String[] p0);
    
    void setExcludeArray(final int p0, final String p1);
    
    void xsetExcludeArray(final XmlString[] p0);
    
    void xsetExcludeArray(final int p0, final XmlString p1);
    
    void insertExclude(final int p0, final String p1);
    
    void addExclude(final String p0);
    
    XmlString insertNewExclude(final int p0);
    
    XmlString addNewExclude();
    
    void removeExclude(final int p0);
    
    public static final class Factory
    {
        public static Excludes newInstance() {
            return (Excludes)XmlBeans.getContextTypeLoader().newInstance(Excludes.type, (XmlOptions)null);
        }
        
        public static Excludes newInstance(final XmlOptions options) {
            return (Excludes)XmlBeans.getContextTypeLoader().newInstance(Excludes.type, options);
        }
        
        public static Excludes parse(final String xmlAsString) throws XmlException {
            return (Excludes)XmlBeans.getContextTypeLoader().parse(xmlAsString, Excludes.type, (XmlOptions)null);
        }
        
        public static Excludes parse(final String xmlAsString, final XmlOptions options) throws XmlException {
            return (Excludes)XmlBeans.getContextTypeLoader().parse(xmlAsString, Excludes.type, options);
        }
        
        public static Excludes parse(final File file) throws XmlException, IOException {
            return (Excludes)XmlBeans.getContextTypeLoader().parse(file, Excludes.type, (XmlOptions)null);
        }
        
        public static Excludes parse(final File file, final XmlOptions options) throws XmlException, IOException {
            return (Excludes)XmlBeans.getContextTypeLoader().parse(file, Excludes.type, options);
        }
        
        public static Excludes parse(final URL u) throws XmlException, IOException {
            return (Excludes)XmlBeans.getContextTypeLoader().parse(u, Excludes.type, (XmlOptions)null);
        }
        
        public static Excludes parse(final URL u, final XmlOptions options) throws XmlException, IOException {
            return (Excludes)XmlBeans.getContextTypeLoader().parse(u, Excludes.type, options);
        }
        
        public static Excludes parse(final InputStream is) throws XmlException, IOException {
            return (Excludes)XmlBeans.getContextTypeLoader().parse(is, Excludes.type, (XmlOptions)null);
        }
        
        public static Excludes parse(final InputStream is, final XmlOptions options) throws XmlException, IOException {
            return (Excludes)XmlBeans.getContextTypeLoader().parse(is, Excludes.type, options);
        }
        
        public static Excludes parse(final Reader r) throws XmlException, IOException {
            return (Excludes)XmlBeans.getContextTypeLoader().parse(r, Excludes.type, (XmlOptions)null);
        }
        
        public static Excludes parse(final Reader r, final XmlOptions options) throws XmlException, IOException {
            return (Excludes)XmlBeans.getContextTypeLoader().parse(r, Excludes.type, options);
        }
        
        public static Excludes parse(final XMLStreamReader sr) throws XmlException {
            return (Excludes)XmlBeans.getContextTypeLoader().parse(sr, Excludes.type, (XmlOptions)null);
        }
        
        public static Excludes parse(final XMLStreamReader sr, final XmlOptions options) throws XmlException {
            return (Excludes)XmlBeans.getContextTypeLoader().parse(sr, Excludes.type, options);
        }
        
        public static Excludes parse(final Node node) throws XmlException {
            return (Excludes)XmlBeans.getContextTypeLoader().parse(node, Excludes.type, (XmlOptions)null);
        }
        
        public static Excludes parse(final Node node, final XmlOptions options) throws XmlException {
            return (Excludes)XmlBeans.getContextTypeLoader().parse(node, Excludes.type, options);
        }
        
        @Deprecated
        public static Excludes parse(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return (Excludes)XmlBeans.getContextTypeLoader().parse(xis, Excludes.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static Excludes parse(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return (Excludes)XmlBeans.getContextTypeLoader().parse(xis, Excludes.type, options);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, Excludes.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, Excludes.type, options);
        }
        
        private Factory() {
        }
    }
}
