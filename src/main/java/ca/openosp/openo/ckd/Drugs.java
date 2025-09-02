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

public interface Drugs extends XmlObject
{
    public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(Drugs.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9F21579FEC5BB34270776B6AF571836F").resolveHandle("drugs98d8type");
    
    String[] getDrugArray();
    
    String getDrugArray(final int p0);
    
    XmlString[] xgetDrugArray();
    
    XmlString xgetDrugArray(final int p0);
    
    int sizeOfDrugArray();
    
    void setDrugArray(final String[] p0);
    
    void setDrugArray(final int p0, final String p1);
    
    void xsetDrugArray(final XmlString[] p0);
    
    void xsetDrugArray(final int p0, final XmlString p1);
    
    void insertDrug(final int p0, final String p1);
    
    void addDrug(final String p0);
    
    XmlString insertNewDrug(final int p0);
    
    XmlString addNewDrug();
    
    void removeDrug(final int p0);
    
    public static final class Factory
    {
        public static Drugs newInstance() {
            return (Drugs)XmlBeans.getContextTypeLoader().newInstance(Drugs.type, (XmlOptions)null);
        }
        
        public static Drugs newInstance(final XmlOptions options) {
            return (Drugs)XmlBeans.getContextTypeLoader().newInstance(Drugs.type, options);
        }
        
        public static Drugs parse(final String xmlAsString) throws XmlException {
            return (Drugs)XmlBeans.getContextTypeLoader().parse(xmlAsString, Drugs.type, (XmlOptions)null);
        }
        
        public static Drugs parse(final String xmlAsString, final XmlOptions options) throws XmlException {
            return (Drugs)XmlBeans.getContextTypeLoader().parse(xmlAsString, Drugs.type, options);
        }
        
        public static Drugs parse(final File file) throws XmlException, IOException {
            return (Drugs)XmlBeans.getContextTypeLoader().parse(file, Drugs.type, (XmlOptions)null);
        }
        
        public static Drugs parse(final File file, final XmlOptions options) throws XmlException, IOException {
            return (Drugs)XmlBeans.getContextTypeLoader().parse(file, Drugs.type, options);
        }
        
        public static Drugs parse(final URL u) throws XmlException, IOException {
            return (Drugs)XmlBeans.getContextTypeLoader().parse(u, Drugs.type, (XmlOptions)null);
        }
        
        public static Drugs parse(final URL u, final XmlOptions options) throws XmlException, IOException {
            return (Drugs)XmlBeans.getContextTypeLoader().parse(u, Drugs.type, options);
        }
        
        public static Drugs parse(final InputStream is) throws XmlException, IOException {
            return (Drugs)XmlBeans.getContextTypeLoader().parse(is, Drugs.type, (XmlOptions)null);
        }
        
        public static Drugs parse(final InputStream is, final XmlOptions options) throws XmlException, IOException {
            return (Drugs)XmlBeans.getContextTypeLoader().parse(is, Drugs.type, options);
        }
        
        public static Drugs parse(final Reader r) throws XmlException, IOException {
            return (Drugs)XmlBeans.getContextTypeLoader().parse(r, Drugs.type, (XmlOptions)null);
        }
        
        public static Drugs parse(final Reader r, final XmlOptions options) throws XmlException, IOException {
            return (Drugs)XmlBeans.getContextTypeLoader().parse(r, Drugs.type, options);
        }
        
        public static Drugs parse(final XMLStreamReader sr) throws XmlException {
            return (Drugs)XmlBeans.getContextTypeLoader().parse(sr, Drugs.type, (XmlOptions)null);
        }
        
        public static Drugs parse(final XMLStreamReader sr, final XmlOptions options) throws XmlException {
            return (Drugs)XmlBeans.getContextTypeLoader().parse(sr, Drugs.type, options);
        }
        
        public static Drugs parse(final Node node) throws XmlException {
            return (Drugs)XmlBeans.getContextTypeLoader().parse(node, Drugs.type, (XmlOptions)null);
        }
        
        public static Drugs parse(final Node node, final XmlOptions options) throws XmlException {
            return (Drugs)XmlBeans.getContextTypeLoader().parse(node, Drugs.type, options);
        }
        
        @Deprecated
        public static Drugs parse(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return (Drugs)XmlBeans.getContextTypeLoader().parse(xis, Drugs.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static Drugs parse(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return (Drugs)XmlBeans.getContextTypeLoader().parse(xis, Drugs.type, options);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, Drugs.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, Drugs.type, options);
        }
        
        private Factory() {
        }
    }
}
