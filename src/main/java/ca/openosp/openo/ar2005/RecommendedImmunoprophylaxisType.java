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
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlObject;

public interface RecommendedImmunoprophylaxisType extends XmlObject
{
    public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(RecommendedImmunoprophylaxisType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9C023B7D67311A3187802DA7FD51EA38").resolveHandle("recommendedimmunoprophylaxistype0e82type");
    
    boolean getRhNegative();
    
    XmlBoolean xgetRhNegative();
    
    void setRhNegative(final boolean p0);
    
    void xsetRhNegative(final XmlBoolean p0);
    
    Calendar getRhIgGiven();
    
    XmlDate xgetRhIgGiven();
    
    boolean isNilRhIgGiven();
    
    void setRhIgGiven(final Calendar p0);
    
    void xsetRhIgGiven(final XmlDate p0);
    
    void setNilRhIgGiven();
    
    boolean getRubella();
    
    XmlBoolean xgetRubella();
    
    void setRubella(final boolean p0);
    
    void xsetRubella(final XmlBoolean p0);
    
    boolean getNewbornHepIG();
    
    XmlBoolean xgetNewbornHepIG();
    
    void setNewbornHepIG(final boolean p0);
    
    void xsetNewbornHepIG(final XmlBoolean p0);
    
    boolean getHepBVaccine();
    
    XmlBoolean xgetHepBVaccine();
    
    void setHepBVaccine(final boolean p0);
    
    void xsetHepBVaccine(final XmlBoolean p0);
    
    public static final class Factory
    {
        public static RecommendedImmunoprophylaxisType newInstance() {
            return (RecommendedImmunoprophylaxisType)XmlBeans.getContextTypeLoader().newInstance(RecommendedImmunoprophylaxisType.type, (XmlOptions)null);
        }
        
        public static RecommendedImmunoprophylaxisType newInstance(final XmlOptions options) {
            return (RecommendedImmunoprophylaxisType)XmlBeans.getContextTypeLoader().newInstance(RecommendedImmunoprophylaxisType.type, options);
        }
        
        public static RecommendedImmunoprophylaxisType parse(final String xmlAsString) throws XmlException {
            return (RecommendedImmunoprophylaxisType)XmlBeans.getContextTypeLoader().parse(xmlAsString, RecommendedImmunoprophylaxisType.type, (XmlOptions)null);
        }
        
        public static RecommendedImmunoprophylaxisType parse(final String xmlAsString, final XmlOptions options) throws XmlException {
            return (RecommendedImmunoprophylaxisType)XmlBeans.getContextTypeLoader().parse(xmlAsString, RecommendedImmunoprophylaxisType.type, options);
        }
        
        public static RecommendedImmunoprophylaxisType parse(final File file) throws XmlException, IOException {
            return (RecommendedImmunoprophylaxisType)XmlBeans.getContextTypeLoader().parse(file, RecommendedImmunoprophylaxisType.type, (XmlOptions)null);
        }
        
        public static RecommendedImmunoprophylaxisType parse(final File file, final XmlOptions options) throws XmlException, IOException {
            return (RecommendedImmunoprophylaxisType)XmlBeans.getContextTypeLoader().parse(file, RecommendedImmunoprophylaxisType.type, options);
        }
        
        public static RecommendedImmunoprophylaxisType parse(final URL u) throws XmlException, IOException {
            return (RecommendedImmunoprophylaxisType)XmlBeans.getContextTypeLoader().parse(u, RecommendedImmunoprophylaxisType.type, (XmlOptions)null);
        }
        
        public static RecommendedImmunoprophylaxisType parse(final URL u, final XmlOptions options) throws XmlException, IOException {
            return (RecommendedImmunoprophylaxisType)XmlBeans.getContextTypeLoader().parse(u, RecommendedImmunoprophylaxisType.type, options);
        }
        
        public static RecommendedImmunoprophylaxisType parse(final InputStream is) throws XmlException, IOException {
            return (RecommendedImmunoprophylaxisType)XmlBeans.getContextTypeLoader().parse(is, RecommendedImmunoprophylaxisType.type, (XmlOptions)null);
        }
        
        public static RecommendedImmunoprophylaxisType parse(final InputStream is, final XmlOptions options) throws XmlException, IOException {
            return (RecommendedImmunoprophylaxisType)XmlBeans.getContextTypeLoader().parse(is, RecommendedImmunoprophylaxisType.type, options);
        }
        
        public static RecommendedImmunoprophylaxisType parse(final Reader r) throws XmlException, IOException {
            return (RecommendedImmunoprophylaxisType)XmlBeans.getContextTypeLoader().parse(r, RecommendedImmunoprophylaxisType.type, (XmlOptions)null);
        }
        
        public static RecommendedImmunoprophylaxisType parse(final Reader r, final XmlOptions options) throws XmlException, IOException {
            return (RecommendedImmunoprophylaxisType)XmlBeans.getContextTypeLoader().parse(r, RecommendedImmunoprophylaxisType.type, options);
        }
        
        public static RecommendedImmunoprophylaxisType parse(final XMLStreamReader sr) throws XmlException {
            return (RecommendedImmunoprophylaxisType)XmlBeans.getContextTypeLoader().parse(sr, RecommendedImmunoprophylaxisType.type, (XmlOptions)null);
        }
        
        public static RecommendedImmunoprophylaxisType parse(final XMLStreamReader sr, final XmlOptions options) throws XmlException {
            return (RecommendedImmunoprophylaxisType)XmlBeans.getContextTypeLoader().parse(sr, RecommendedImmunoprophylaxisType.type, options);
        }
        
        public static RecommendedImmunoprophylaxisType parse(final Node node) throws XmlException {
            return (RecommendedImmunoprophylaxisType)XmlBeans.getContextTypeLoader().parse(node, RecommendedImmunoprophylaxisType.type, (XmlOptions)null);
        }
        
        public static RecommendedImmunoprophylaxisType parse(final Node node, final XmlOptions options) throws XmlException {
            return (RecommendedImmunoprophylaxisType)XmlBeans.getContextTypeLoader().parse(node, RecommendedImmunoprophylaxisType.type, options);
        }
        
        @Deprecated
        public static RecommendedImmunoprophylaxisType parse(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return (RecommendedImmunoprophylaxisType)XmlBeans.getContextTypeLoader().parse(xis, RecommendedImmunoprophylaxisType.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static RecommendedImmunoprophylaxisType parse(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return (RecommendedImmunoprophylaxisType)XmlBeans.getContextTypeLoader().parse(xis, RecommendedImmunoprophylaxisType.type, options);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, RecommendedImmunoprophylaxisType.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, RecommendedImmunoprophylaxisType.type, options);
        }
        
        private Factory() {
        }
    }
}
