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
import org.apache.xmlbeans.XmlDate;
import java.util.Calendar;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlObject;

public interface UltrasoundType extends XmlObject
{
    public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(UltrasoundType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9C023B7D67311A3187802DA7FD51EA38").resolveHandle("ultrasoundtype4a74type");
    
    Calendar getDate();
    
    XmlDate xgetDate();
    
    void setDate(final Calendar p0);
    
    void xsetDate(final XmlDate p0);
    
    String getGa();
    
    Ga xgetGa();
    
    void setGa(final String p0);
    
    void xsetGa(final Ga p0);
    
    String getResults();
    
    XmlString xgetResults();
    
    void setResults(final String p0);
    
    void xsetResults(final XmlString p0);
    
    public interface Ga extends XmlString
    {
        public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(Ga.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9C023B7D67311A3187802DA7FD51EA38").resolveHandle("gab8beelemtype");
        
        public static final class Factory
        {
            public static Ga newValue(final Object obj) {
                return (Ga)Ga.type.newValue(obj);
            }
            
            public static Ga newInstance() {
                return (Ga)XmlBeans.getContextTypeLoader().newInstance(Ga.type, (XmlOptions)null);
            }
            
            public static Ga newInstance(final XmlOptions options) {
                return (Ga)XmlBeans.getContextTypeLoader().newInstance(Ga.type, options);
            }
            
            private Factory() {
            }
        }
    }
    
    public static final class Factory
    {
        public static UltrasoundType newInstance() {
            return (UltrasoundType)XmlBeans.getContextTypeLoader().newInstance(UltrasoundType.type, (XmlOptions)null);
        }
        
        public static UltrasoundType newInstance(final XmlOptions options) {
            return (UltrasoundType)XmlBeans.getContextTypeLoader().newInstance(UltrasoundType.type, options);
        }
        
        public static UltrasoundType parse(final String xmlAsString) throws XmlException {
            return (UltrasoundType)XmlBeans.getContextTypeLoader().parse(xmlAsString, UltrasoundType.type, (XmlOptions)null);
        }
        
        public static UltrasoundType parse(final String xmlAsString, final XmlOptions options) throws XmlException {
            return (UltrasoundType)XmlBeans.getContextTypeLoader().parse(xmlAsString, UltrasoundType.type, options);
        }
        
        public static UltrasoundType parse(final File file) throws XmlException, IOException {
            return (UltrasoundType)XmlBeans.getContextTypeLoader().parse(file, UltrasoundType.type, (XmlOptions)null);
        }
        
        public static UltrasoundType parse(final File file, final XmlOptions options) throws XmlException, IOException {
            return (UltrasoundType)XmlBeans.getContextTypeLoader().parse(file, UltrasoundType.type, options);
        }
        
        public static UltrasoundType parse(final URL u) throws XmlException, IOException {
            return (UltrasoundType)XmlBeans.getContextTypeLoader().parse(u, UltrasoundType.type, (XmlOptions)null);
        }
        
        public static UltrasoundType parse(final URL u, final XmlOptions options) throws XmlException, IOException {
            return (UltrasoundType)XmlBeans.getContextTypeLoader().parse(u, UltrasoundType.type, options);
        }
        
        public static UltrasoundType parse(final InputStream is) throws XmlException, IOException {
            return (UltrasoundType)XmlBeans.getContextTypeLoader().parse(is, UltrasoundType.type, (XmlOptions)null);
        }
        
        public static UltrasoundType parse(final InputStream is, final XmlOptions options) throws XmlException, IOException {
            return (UltrasoundType)XmlBeans.getContextTypeLoader().parse(is, UltrasoundType.type, options);
        }
        
        public static UltrasoundType parse(final Reader r) throws XmlException, IOException {
            return (UltrasoundType)XmlBeans.getContextTypeLoader().parse(r, UltrasoundType.type, (XmlOptions)null);
        }
        
        public static UltrasoundType parse(final Reader r, final XmlOptions options) throws XmlException, IOException {
            return (UltrasoundType)XmlBeans.getContextTypeLoader().parse(r, UltrasoundType.type, options);
        }
        
        public static UltrasoundType parse(final XMLStreamReader sr) throws XmlException {
            return (UltrasoundType)XmlBeans.getContextTypeLoader().parse(sr, UltrasoundType.type, (XmlOptions)null);
        }
        
        public static UltrasoundType parse(final XMLStreamReader sr, final XmlOptions options) throws XmlException {
            return (UltrasoundType)XmlBeans.getContextTypeLoader().parse(sr, UltrasoundType.type, options);
        }
        
        public static UltrasoundType parse(final Node node) throws XmlException {
            return (UltrasoundType)XmlBeans.getContextTypeLoader().parse(node, UltrasoundType.type, (XmlOptions)null);
        }
        
        public static UltrasoundType parse(final Node node, final XmlOptions options) throws XmlException {
            return (UltrasoundType)XmlBeans.getContextTypeLoader().parse(node, UltrasoundType.type, options);
        }
        
        @Deprecated
        public static UltrasoundType parse(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return (UltrasoundType)XmlBeans.getContextTypeLoader().parse(xis, UltrasoundType.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static UltrasoundType parse(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return (UltrasoundType)XmlBeans.getContextTypeLoader().parse(xis, UltrasoundType.type, options);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, UltrasoundType.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, UltrasoundType.type, options);
        }
        
        private Factory() {
        }
    }
}
