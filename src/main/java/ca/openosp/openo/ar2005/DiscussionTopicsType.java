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
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlObject;

public interface DiscussionTopicsType extends XmlObject
{
    public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(DiscussionTopicsType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9C023B7D67311A3187802DA7FD51EA38").resolveHandle("discussiontopicstype115dtype");
    
    boolean getExercise();
    
    XmlBoolean xgetExercise();
    
    void setExercise(final boolean p0);
    
    void xsetExercise(final XmlBoolean p0);
    
    boolean getWorkPlan();
    
    XmlBoolean xgetWorkPlan();
    
    void setWorkPlan(final boolean p0);
    
    void xsetWorkPlan(final XmlBoolean p0);
    
    boolean getIntercourse();
    
    XmlBoolean xgetIntercourse();
    
    void setIntercourse(final boolean p0);
    
    void xsetIntercourse(final XmlBoolean p0);
    
    boolean getTravel();
    
    XmlBoolean xgetTravel();
    
    void setTravel(final boolean p0);
    
    void xsetTravel(final XmlBoolean p0);
    
    boolean getPrenatalClasses();
    
    XmlBoolean xgetPrenatalClasses();
    
    void setPrenatalClasses(final boolean p0);
    
    void xsetPrenatalClasses(final XmlBoolean p0);
    
    boolean getBirthPlan();
    
    XmlBoolean xgetBirthPlan();
    
    void setBirthPlan(final boolean p0);
    
    void xsetBirthPlan(final XmlBoolean p0);
    
    boolean getOnCallProviders();
    
    XmlBoolean xgetOnCallProviders();
    
    void setOnCallProviders(final boolean p0);
    
    void xsetOnCallProviders(final XmlBoolean p0);
    
    boolean getPretermLabour();
    
    XmlBoolean xgetPretermLabour();
    
    void setPretermLabour(final boolean p0);
    
    void xsetPretermLabour(final XmlBoolean p0);
    
    boolean getPROM();
    
    XmlBoolean xgetPROM();
    
    void setPROM(final boolean p0);
    
    void xsetPROM(final XmlBoolean p0);
    
    boolean getAPH();
    
    XmlBoolean xgetAPH();
    
    void setAPH(final boolean p0);
    
    void xsetAPH(final XmlBoolean p0);
    
    boolean getFetalMovement();
    
    XmlBoolean xgetFetalMovement();
    
    void setFetalMovement(final boolean p0);
    
    void xsetFetalMovement(final XmlBoolean p0);
    
    boolean getAdmissionTiming();
    
    XmlBoolean xgetAdmissionTiming();
    
    void setAdmissionTiming(final boolean p0);
    
    void xsetAdmissionTiming(final XmlBoolean p0);
    
    boolean getPainManagement();
    
    XmlBoolean xgetPainManagement();
    
    void setPainManagement(final boolean p0);
    
    void xsetPainManagement(final XmlBoolean p0);
    
    boolean getLabourSupport();
    
    XmlBoolean xgetLabourSupport();
    
    void setLabourSupport(final boolean p0);
    
    void xsetLabourSupport(final XmlBoolean p0);
    
    boolean getBreastFeeding();
    
    XmlBoolean xgetBreastFeeding();
    
    void setBreastFeeding(final boolean p0);
    
    void xsetBreastFeeding(final XmlBoolean p0);
    
    boolean getCircumcision();
    
    XmlBoolean xgetCircumcision();
    
    void setCircumcision(final boolean p0);
    
    void xsetCircumcision(final XmlBoolean p0);
    
    boolean getDischargePlanning();
    
    XmlBoolean xgetDischargePlanning();
    
    void setDischargePlanning(final boolean p0);
    
    void xsetDischargePlanning(final XmlBoolean p0);
    
    boolean getCarSeatSafety();
    
    XmlBoolean xgetCarSeatSafety();
    
    void setCarSeatSafety(final boolean p0);
    
    void xsetCarSeatSafety(final XmlBoolean p0);
    
    boolean getDepression();
    
    XmlBoolean xgetDepression();
    
    void setDepression(final boolean p0);
    
    void xsetDepression(final XmlBoolean p0);
    
    boolean getContraception();
    
    XmlBoolean xgetContraception();
    
    void setContraception(final boolean p0);
    
    void xsetContraception(final XmlBoolean p0);
    
    boolean getPostpartumCare();
    
    XmlBoolean xgetPostpartumCare();
    
    void setPostpartumCare(final boolean p0);
    
    void xsetPostpartumCare(final XmlBoolean p0);
    
    public static final class Factory
    {
        public static DiscussionTopicsType newInstance() {
            return (DiscussionTopicsType)XmlBeans.getContextTypeLoader().newInstance(DiscussionTopicsType.type, (XmlOptions)null);
        }
        
        public static DiscussionTopicsType newInstance(final XmlOptions options) {
            return (DiscussionTopicsType)XmlBeans.getContextTypeLoader().newInstance(DiscussionTopicsType.type, options);
        }
        
        public static DiscussionTopicsType parse(final String xmlAsString) throws XmlException {
            return (DiscussionTopicsType)XmlBeans.getContextTypeLoader().parse(xmlAsString, DiscussionTopicsType.type, (XmlOptions)null);
        }
        
        public static DiscussionTopicsType parse(final String xmlAsString, final XmlOptions options) throws XmlException {
            return (DiscussionTopicsType)XmlBeans.getContextTypeLoader().parse(xmlAsString, DiscussionTopicsType.type, options);
        }
        
        public static DiscussionTopicsType parse(final File file) throws XmlException, IOException {
            return (DiscussionTopicsType)XmlBeans.getContextTypeLoader().parse(file, DiscussionTopicsType.type, (XmlOptions)null);
        }
        
        public static DiscussionTopicsType parse(final File file, final XmlOptions options) throws XmlException, IOException {
            return (DiscussionTopicsType)XmlBeans.getContextTypeLoader().parse(file, DiscussionTopicsType.type, options);
        }
        
        public static DiscussionTopicsType parse(final URL u) throws XmlException, IOException {
            return (DiscussionTopicsType)XmlBeans.getContextTypeLoader().parse(u, DiscussionTopicsType.type, (XmlOptions)null);
        }
        
        public static DiscussionTopicsType parse(final URL u, final XmlOptions options) throws XmlException, IOException {
            return (DiscussionTopicsType)XmlBeans.getContextTypeLoader().parse(u, DiscussionTopicsType.type, options);
        }
        
        public static DiscussionTopicsType parse(final InputStream is) throws XmlException, IOException {
            return (DiscussionTopicsType)XmlBeans.getContextTypeLoader().parse(is, DiscussionTopicsType.type, (XmlOptions)null);
        }
        
        public static DiscussionTopicsType parse(final InputStream is, final XmlOptions options) throws XmlException, IOException {
            return (DiscussionTopicsType)XmlBeans.getContextTypeLoader().parse(is, DiscussionTopicsType.type, options);
        }
        
        public static DiscussionTopicsType parse(final Reader r) throws XmlException, IOException {
            return (DiscussionTopicsType)XmlBeans.getContextTypeLoader().parse(r, DiscussionTopicsType.type, (XmlOptions)null);
        }
        
        public static DiscussionTopicsType parse(final Reader r, final XmlOptions options) throws XmlException, IOException {
            return (DiscussionTopicsType)XmlBeans.getContextTypeLoader().parse(r, DiscussionTopicsType.type, options);
        }
        
        public static DiscussionTopicsType parse(final XMLStreamReader sr) throws XmlException {
            return (DiscussionTopicsType)XmlBeans.getContextTypeLoader().parse(sr, DiscussionTopicsType.type, (XmlOptions)null);
        }
        
        public static DiscussionTopicsType parse(final XMLStreamReader sr, final XmlOptions options) throws XmlException {
            return (DiscussionTopicsType)XmlBeans.getContextTypeLoader().parse(sr, DiscussionTopicsType.type, options);
        }
        
        public static DiscussionTopicsType parse(final Node node) throws XmlException {
            return (DiscussionTopicsType)XmlBeans.getContextTypeLoader().parse(node, DiscussionTopicsType.type, (XmlOptions)null);
        }
        
        public static DiscussionTopicsType parse(final Node node, final XmlOptions options) throws XmlException {
            return (DiscussionTopicsType)XmlBeans.getContextTypeLoader().parse(node, DiscussionTopicsType.type, options);
        }
        
        @Deprecated
        public static DiscussionTopicsType parse(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return (DiscussionTopicsType)XmlBeans.getContextTypeLoader().parse(xis, DiscussionTopicsType.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static DiscussionTopicsType parse(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return (DiscussionTopicsType)XmlBeans.getContextTypeLoader().parse(xis, DiscussionTopicsType.type, options);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, DiscussionTopicsType.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, DiscussionTopicsType.type, options);
        }
        
        private Factory() {
        }
    }
}
