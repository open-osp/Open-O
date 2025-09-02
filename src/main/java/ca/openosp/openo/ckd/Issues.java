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

public interface Issues extends XmlObject
{
    public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(Issues.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9F21579FEC5BB34270776B6AF571836F").resolveHandle("issues297ftype");
    
    String[] getIssueArray();
    
    String getIssueArray(final int p0);
    
    XmlString[] xgetIssueArray();
    
    XmlString xgetIssueArray(final int p0);
    
    int sizeOfIssueArray();
    
    void setIssueArray(final String[] p0);
    
    void setIssueArray(final int p0, final String p1);
    
    void xsetIssueArray(final XmlString[] p0);
    
    void xsetIssueArray(final int p0, final XmlString p1);
    
    void insertIssue(final int p0, final String p1);
    
    void addIssue(final String p0);
    
    XmlString insertNewIssue(final int p0);
    
    XmlString addNewIssue();
    
    void removeIssue(final int p0);
    
    public static final class Factory
    {
        public static Issues newInstance() {
            return (Issues)XmlBeans.getContextTypeLoader().newInstance(Issues.type, (XmlOptions)null);
        }
        
        public static Issues newInstance(final XmlOptions options) {
            return (Issues)XmlBeans.getContextTypeLoader().newInstance(Issues.type, options);
        }
        
        public static Issues parse(final String xmlAsString) throws XmlException {
            return (Issues)XmlBeans.getContextTypeLoader().parse(xmlAsString, Issues.type, (XmlOptions)null);
        }
        
        public static Issues parse(final String xmlAsString, final XmlOptions options) throws XmlException {
            return (Issues)XmlBeans.getContextTypeLoader().parse(xmlAsString, Issues.type, options);
        }
        
        public static Issues parse(final File file) throws XmlException, IOException {
            return (Issues)XmlBeans.getContextTypeLoader().parse(file, Issues.type, (XmlOptions)null);
        }
        
        public static Issues parse(final File file, final XmlOptions options) throws XmlException, IOException {
            return (Issues)XmlBeans.getContextTypeLoader().parse(file, Issues.type, options);
        }
        
        public static Issues parse(final URL u) throws XmlException, IOException {
            return (Issues)XmlBeans.getContextTypeLoader().parse(u, Issues.type, (XmlOptions)null);
        }
        
        public static Issues parse(final URL u, final XmlOptions options) throws XmlException, IOException {
            return (Issues)XmlBeans.getContextTypeLoader().parse(u, Issues.type, options);
        }
        
        public static Issues parse(final InputStream is) throws XmlException, IOException {
            return (Issues)XmlBeans.getContextTypeLoader().parse(is, Issues.type, (XmlOptions)null);
        }
        
        public static Issues parse(final InputStream is, final XmlOptions options) throws XmlException, IOException {
            return (Issues)XmlBeans.getContextTypeLoader().parse(is, Issues.type, options);
        }
        
        public static Issues parse(final Reader r) throws XmlException, IOException {
            return (Issues)XmlBeans.getContextTypeLoader().parse(r, Issues.type, (XmlOptions)null);
        }
        
        public static Issues parse(final Reader r, final XmlOptions options) throws XmlException, IOException {
            return (Issues)XmlBeans.getContextTypeLoader().parse(r, Issues.type, options);
        }
        
        public static Issues parse(final XMLStreamReader sr) throws XmlException {
            return (Issues)XmlBeans.getContextTypeLoader().parse(sr, Issues.type, (XmlOptions)null);
        }
        
        public static Issues parse(final XMLStreamReader sr, final XmlOptions options) throws XmlException {
            return (Issues)XmlBeans.getContextTypeLoader().parse(sr, Issues.type, options);
        }
        
        public static Issues parse(final Node node) throws XmlException {
            return (Issues)XmlBeans.getContextTypeLoader().parse(node, Issues.type, (XmlOptions)null);
        }
        
        public static Issues parse(final Node node, final XmlOptions options) throws XmlException {
            return (Issues)XmlBeans.getContextTypeLoader().parse(node, Issues.type, options);
        }
        
        @Deprecated
        public static Issues parse(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return (Issues)XmlBeans.getContextTypeLoader().parse(xis, Issues.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static Issues parse(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return (Issues)XmlBeans.getContextTypeLoader().parse(xis, Issues.type, options);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, Issues.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, Issues.type, options);
        }
        
        private Factory() {
        }
    }
}
