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
import org.apache.xmlbeans.XmlDate;
import java.util.Calendar;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlObject;

public interface SignatureType extends XmlObject
{
    public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(SignatureType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9C023B7D67311A3187802DA7FD51EA38").resolveHandle("signaturetypee7b7type");
    
    String getSignature();
    
    XmlString xgetSignature();
    
    void setSignature(final String p0);
    
    void xsetSignature(final XmlString p0);
    
    Calendar getDate();
    
    XmlDate xgetDate();
    
    void setDate(final Calendar p0);
    
    void xsetDate(final XmlDate p0);
    
    String getSignature2();
    
    XmlString xgetSignature2();
    
    boolean isSetSignature2();
    
    void setSignature2(final String p0);
    
    void xsetSignature2(final XmlString p0);
    
    void unsetSignature2();
    
    Calendar getDate2();
    
    XmlDate xgetDate2();
    
    boolean isSetDate2();
    
    void setDate2(final Calendar p0);
    
    void xsetDate2(final XmlDate p0);
    
    void unsetDate2();
    
    public static final class Factory
    {
        public static SignatureType newInstance() {
            return (SignatureType)XmlBeans.getContextTypeLoader().newInstance(SignatureType.type, (XmlOptions)null);
        }
        
        public static SignatureType newInstance(final XmlOptions options) {
            return (SignatureType)XmlBeans.getContextTypeLoader().newInstance(SignatureType.type, options);
        }
        
        public static SignatureType parse(final String xmlAsString) throws XmlException {
            return (SignatureType)XmlBeans.getContextTypeLoader().parse(xmlAsString, SignatureType.type, (XmlOptions)null);
        }
        
        public static SignatureType parse(final String xmlAsString, final XmlOptions options) throws XmlException {
            return (SignatureType)XmlBeans.getContextTypeLoader().parse(xmlAsString, SignatureType.type, options);
        }
        
        public static SignatureType parse(final File file) throws XmlException, IOException {
            return (SignatureType)XmlBeans.getContextTypeLoader().parse(file, SignatureType.type, (XmlOptions)null);
        }
        
        public static SignatureType parse(final File file, final XmlOptions options) throws XmlException, IOException {
            return (SignatureType)XmlBeans.getContextTypeLoader().parse(file, SignatureType.type, options);
        }
        
        public static SignatureType parse(final URL u) throws XmlException, IOException {
            return (SignatureType)XmlBeans.getContextTypeLoader().parse(u, SignatureType.type, (XmlOptions)null);
        }
        
        public static SignatureType parse(final URL u, final XmlOptions options) throws XmlException, IOException {
            return (SignatureType)XmlBeans.getContextTypeLoader().parse(u, SignatureType.type, options);
        }
        
        public static SignatureType parse(final InputStream is) throws XmlException, IOException {
            return (SignatureType)XmlBeans.getContextTypeLoader().parse(is, SignatureType.type, (XmlOptions)null);
        }
        
        public static SignatureType parse(final InputStream is, final XmlOptions options) throws XmlException, IOException {
            return (SignatureType)XmlBeans.getContextTypeLoader().parse(is, SignatureType.type, options);
        }
        
        public static SignatureType parse(final Reader r) throws XmlException, IOException {
            return (SignatureType)XmlBeans.getContextTypeLoader().parse(r, SignatureType.type, (XmlOptions)null);
        }
        
        public static SignatureType parse(final Reader r, final XmlOptions options) throws XmlException, IOException {
            return (SignatureType)XmlBeans.getContextTypeLoader().parse(r, SignatureType.type, options);
        }
        
        public static SignatureType parse(final XMLStreamReader sr) throws XmlException {
            return (SignatureType)XmlBeans.getContextTypeLoader().parse(sr, SignatureType.type, (XmlOptions)null);
        }
        
        public static SignatureType parse(final XMLStreamReader sr, final XmlOptions options) throws XmlException {
            return (SignatureType)XmlBeans.getContextTypeLoader().parse(sr, SignatureType.type, options);
        }
        
        public static SignatureType parse(final Node node) throws XmlException {
            return (SignatureType)XmlBeans.getContextTypeLoader().parse(node, SignatureType.type, (XmlOptions)null);
        }
        
        public static SignatureType parse(final Node node, final XmlOptions options) throws XmlException {
            return (SignatureType)XmlBeans.getContextTypeLoader().parse(node, SignatureType.type, options);
        }
        
        @Deprecated
        public static SignatureType parse(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return (SignatureType)XmlBeans.getContextTypeLoader().parse(xis, SignatureType.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static SignatureType parse(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return (SignatureType)XmlBeans.getContextTypeLoader().parse(xis, SignatureType.type, options);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, SignatureType.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, SignatureType.type, options);
        }
        
        private Factory() {
        }
    }
}
