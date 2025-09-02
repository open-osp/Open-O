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

public interface Bp extends XmlObject
{
    public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(Bp.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9F21579FEC5BB34270776B6AF571836F").resolveHandle("bp4e0btype");
    
    String getSystolic();
    
    XmlString xgetSystolic();
    
    void setSystolic(final String p0);
    
    void xsetSystolic(final XmlString p0);
    
    String getDiastolic();
    
    XmlString xgetDiastolic();
    
    void setDiastolic(final String p0);
    
    void xsetDiastolic(final XmlString p0);
    
    public static final class Factory
    {
        public static Bp newInstance() {
            return (Bp)XmlBeans.getContextTypeLoader().newInstance(Bp.type, (XmlOptions)null);
        }
        
        public static Bp newInstance(final XmlOptions options) {
            return (Bp)XmlBeans.getContextTypeLoader().newInstance(Bp.type, options);
        }
        
        public static Bp parse(final String xmlAsString) throws XmlException {
            return (Bp)XmlBeans.getContextTypeLoader().parse(xmlAsString, Bp.type, (XmlOptions)null);
        }
        
        public static Bp parse(final String xmlAsString, final XmlOptions options) throws XmlException {
            return (Bp)XmlBeans.getContextTypeLoader().parse(xmlAsString, Bp.type, options);
        }
        
        public static Bp parse(final File file) throws XmlException, IOException {
            return (Bp)XmlBeans.getContextTypeLoader().parse(file, Bp.type, (XmlOptions)null);
        }
        
        public static Bp parse(final File file, final XmlOptions options) throws XmlException, IOException {
            return (Bp)XmlBeans.getContextTypeLoader().parse(file, Bp.type, options);
        }
        
        public static Bp parse(final URL u) throws XmlException, IOException {
            return (Bp)XmlBeans.getContextTypeLoader().parse(u, Bp.type, (XmlOptions)null);
        }
        
        public static Bp parse(final URL u, final XmlOptions options) throws XmlException, IOException {
            return (Bp)XmlBeans.getContextTypeLoader().parse(u, Bp.type, options);
        }
        
        public static Bp parse(final InputStream is) throws XmlException, IOException {
            return (Bp)XmlBeans.getContextTypeLoader().parse(is, Bp.type, (XmlOptions)null);
        }
        
        public static Bp parse(final InputStream is, final XmlOptions options) throws XmlException, IOException {
            return (Bp)XmlBeans.getContextTypeLoader().parse(is, Bp.type, options);
        }
        
        public static Bp parse(final Reader r) throws XmlException, IOException {
            return (Bp)XmlBeans.getContextTypeLoader().parse(r, Bp.type, (XmlOptions)null);
        }
        
        public static Bp parse(final Reader r, final XmlOptions options) throws XmlException, IOException {
            return (Bp)XmlBeans.getContextTypeLoader().parse(r, Bp.type, options);
        }
        
        public static Bp parse(final XMLStreamReader sr) throws XmlException {
            return (Bp)XmlBeans.getContextTypeLoader().parse(sr, Bp.type, (XmlOptions)null);
        }
        
        public static Bp parse(final XMLStreamReader sr, final XmlOptions options) throws XmlException {
            return (Bp)XmlBeans.getContextTypeLoader().parse(sr, Bp.type, options);
        }
        
        public static Bp parse(final Node node) throws XmlException {
            return (Bp)XmlBeans.getContextTypeLoader().parse(node, Bp.type, (XmlOptions)null);
        }
        
        public static Bp parse(final Node node, final XmlOptions options) throws XmlException {
            return (Bp)XmlBeans.getContextTypeLoader().parse(node, Bp.type, options);
        }
        
        @Deprecated
        public static Bp parse(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return (Bp)XmlBeans.getContextTypeLoader().parse(xis, Bp.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static Bp parse(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return (Bp)XmlBeans.getContextTypeLoader().parse(xis, Bp.type, options);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, Bp.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, Bp.type, options);
        }
        
        private Factory() {
        }
    }
}
