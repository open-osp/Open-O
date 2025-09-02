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

public interface SearchText extends XmlObject
{
    public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(SearchText.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9F21579FEC5BB34270776B6AF571836F").resolveHandle("searchtext4124type");
    
    String[] getTextArray();
    
    String getTextArray(final int p0);
    
    XmlString[] xgetTextArray();
    
    XmlString xgetTextArray(final int p0);
    
    int sizeOfTextArray();
    
    void setTextArray(final String[] p0);
    
    void setTextArray(final int p0, final String p1);
    
    void xsetTextArray(final XmlString[] p0);
    
    void xsetTextArray(final int p0, final XmlString p1);
    
    void insertText(final int p0, final String p1);
    
    void addText(final String p0);
    
    XmlString insertNewText(final int p0);
    
    XmlString addNewText();
    
    void removeText(final int p0);
    
    public static final class Factory
    {
        public static SearchText newInstance() {
            return (SearchText)XmlBeans.getContextTypeLoader().newInstance(SearchText.type, (XmlOptions)null);
        }
        
        public static SearchText newInstance(final XmlOptions options) {
            return (SearchText)XmlBeans.getContextTypeLoader().newInstance(SearchText.type, options);
        }
        
        public static SearchText parse(final String xmlAsString) throws XmlException {
            return (SearchText)XmlBeans.getContextTypeLoader().parse(xmlAsString, SearchText.type, (XmlOptions)null);
        }
        
        public static SearchText parse(final String xmlAsString, final XmlOptions options) throws XmlException {
            return (SearchText)XmlBeans.getContextTypeLoader().parse(xmlAsString, SearchText.type, options);
        }
        
        public static SearchText parse(final File file) throws XmlException, IOException {
            return (SearchText)XmlBeans.getContextTypeLoader().parse(file, SearchText.type, (XmlOptions)null);
        }
        
        public static SearchText parse(final File file, final XmlOptions options) throws XmlException, IOException {
            return (SearchText)XmlBeans.getContextTypeLoader().parse(file, SearchText.type, options);
        }
        
        public static SearchText parse(final URL u) throws XmlException, IOException {
            return (SearchText)XmlBeans.getContextTypeLoader().parse(u, SearchText.type, (XmlOptions)null);
        }
        
        public static SearchText parse(final URL u, final XmlOptions options) throws XmlException, IOException {
            return (SearchText)XmlBeans.getContextTypeLoader().parse(u, SearchText.type, options);
        }
        
        public static SearchText parse(final InputStream is) throws XmlException, IOException {
            return (SearchText)XmlBeans.getContextTypeLoader().parse(is, SearchText.type, (XmlOptions)null);
        }
        
        public static SearchText parse(final InputStream is, final XmlOptions options) throws XmlException, IOException {
            return (SearchText)XmlBeans.getContextTypeLoader().parse(is, SearchText.type, options);
        }
        
        public static SearchText parse(final Reader r) throws XmlException, IOException {
            return (SearchText)XmlBeans.getContextTypeLoader().parse(r, SearchText.type, (XmlOptions)null);
        }
        
        public static SearchText parse(final Reader r, final XmlOptions options) throws XmlException, IOException {
            return (SearchText)XmlBeans.getContextTypeLoader().parse(r, SearchText.type, options);
        }
        
        public static SearchText parse(final XMLStreamReader sr) throws XmlException {
            return (SearchText)XmlBeans.getContextTypeLoader().parse(sr, SearchText.type, (XmlOptions)null);
        }
        
        public static SearchText parse(final XMLStreamReader sr, final XmlOptions options) throws XmlException {
            return (SearchText)XmlBeans.getContextTypeLoader().parse(sr, SearchText.type, options);
        }
        
        public static SearchText parse(final Node node) throws XmlException {
            return (SearchText)XmlBeans.getContextTypeLoader().parse(node, SearchText.type, (XmlOptions)null);
        }
        
        public static SearchText parse(final Node node, final XmlOptions options) throws XmlException {
            return (SearchText)XmlBeans.getContextTypeLoader().parse(node, SearchText.type, options);
        }
        
        @Deprecated
        public static SearchText parse(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return (SearchText)XmlBeans.getContextTypeLoader().parse(xis, SearchText.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static SearchText parse(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return (SearchText)XmlBeans.getContextTypeLoader().parse(xis, SearchText.type, options);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, SearchText.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, SearchText.type, options);
        }
        
        private Factory() {
        }
    }
}
