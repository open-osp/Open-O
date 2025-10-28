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
import org.apache.xmlbeans.XmlFloat;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.XmlDate;
import java.util.Calendar;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlObject;

public interface SubsequentVisitItemType extends XmlObject
{
    public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(SubsequentVisitItemType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9C023B7D67311A3187802DA7FD51EA38").resolveHandle("subsequentvisititemtypeb4c8type");
    
    Calendar getDate();
    
    XmlDate xgetDate();
    
    boolean isNilDate();
    
    void setDate(final Calendar p0);
    
    void xsetDate(final XmlDate p0);
    
    void setNilDate();
    
    String getGa();
    
    Ga xgetGa();
    
    void setGa(final String p0);
    
    void xsetGa(final Ga p0);
    
    float getWeight();
    
    Weight xgetWeight();
    
    boolean isNilWeight();
    
    void setWeight(final float p0);
    
    void xsetWeight(final Weight p0);
    
    void setNilWeight();
    
    String getBp();
    
    Bp xgetBp();
    
    void setBp(final String p0);
    
    void xsetBp(final Bp p0);
    
    String getUrinePR();
    
    XmlString xgetUrinePR();
    
    void setUrinePR(final String p0);
    
    void xsetUrinePR(final XmlString p0);
    
    String getUrineGI();
    
    XmlString xgetUrineGI();
    
    void setUrineGI(final String p0);
    
    void xsetUrineGI(final XmlString p0);
    
    String getSFH();
    
    XmlString xgetSFH();
    
    void setSFH(final String p0);
    
    void xsetSFH(final XmlString p0);
    
    String getPresentationPosition();
    
    XmlString xgetPresentationPosition();
    
    void setPresentationPosition(final String p0);
    
    void xsetPresentationPosition(final XmlString p0);
    
    String getFHRFm();
    
    XmlString xgetFHRFm();
    
    void setFHRFm(final String p0);
    
    void xsetFHRFm(final XmlString p0);
    
    String getComments();
    
    XmlString xgetComments();
    
    void setComments(final String p0);
    
    void xsetComments(final XmlString p0);
    
    public interface Ga extends XmlString
    {
        public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(Ga.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9C023B7D67311A3187802DA7FD51EA38").resolveHandle("gab9beelemtype");
        
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
    
    public interface Weight extends XmlFloat
    {
        public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(Weight.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9C023B7D67311A3187802DA7FD51EA38").resolveHandle("weightc2dcelemtype");
        
        public static final class Factory
        {
            public static Weight newValue(final Object obj) {
                return (Weight)Weight.type.newValue(obj);
            }
            
            public static Weight newInstance() {
                return (Weight)XmlBeans.getContextTypeLoader().newInstance(Weight.type, (XmlOptions)null);
            }
            
            public static Weight newInstance(final XmlOptions options) {
                return (Weight)XmlBeans.getContextTypeLoader().newInstance(Weight.type, options);
            }
            
            private Factory() {
            }
        }
    }
    
    public interface Bp extends XmlString
    {
        public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(Bp.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9C023B7D67311A3187802DA7FD51EA38").resolveHandle("bp6632elemtype");
        
        public static final class Factory
        {
            public static Bp newValue(final Object obj) {
                return (Bp)Bp.type.newValue(obj);
            }
            
            public static Bp newInstance() {
                return (Bp)XmlBeans.getContextTypeLoader().newInstance(Bp.type, (XmlOptions)null);
            }
            
            public static Bp newInstance(final XmlOptions options) {
                return (Bp)XmlBeans.getContextTypeLoader().newInstance(Bp.type, options);
            }
            
            private Factory() {
            }
        }
    }
    
    public static final class Factory
    {
        public static SubsequentVisitItemType newInstance() {
            return (SubsequentVisitItemType)XmlBeans.getContextTypeLoader().newInstance(SubsequentVisitItemType.type, (XmlOptions)null);
        }
        
        public static SubsequentVisitItemType newInstance(final XmlOptions options) {
            return (SubsequentVisitItemType)XmlBeans.getContextTypeLoader().newInstance(SubsequentVisitItemType.type, options);
        }
        
        public static SubsequentVisitItemType parse(final String xmlAsString) throws XmlException {
            return (SubsequentVisitItemType)XmlBeans.getContextTypeLoader().parse(xmlAsString, SubsequentVisitItemType.type, (XmlOptions)null);
        }
        
        public static SubsequentVisitItemType parse(final String xmlAsString, final XmlOptions options) throws XmlException {
            return (SubsequentVisitItemType)XmlBeans.getContextTypeLoader().parse(xmlAsString, SubsequentVisitItemType.type, options);
        }
        
        public static SubsequentVisitItemType parse(final File file) throws XmlException, IOException {
            return (SubsequentVisitItemType)XmlBeans.getContextTypeLoader().parse(file, SubsequentVisitItemType.type, (XmlOptions)null);
        }
        
        public static SubsequentVisitItemType parse(final File file, final XmlOptions options) throws XmlException, IOException {
            return (SubsequentVisitItemType)XmlBeans.getContextTypeLoader().parse(file, SubsequentVisitItemType.type, options);
        }
        
        public static SubsequentVisitItemType parse(final URL u) throws XmlException, IOException {
            return (SubsequentVisitItemType)XmlBeans.getContextTypeLoader().parse(u, SubsequentVisitItemType.type, (XmlOptions)null);
        }
        
        public static SubsequentVisitItemType parse(final URL u, final XmlOptions options) throws XmlException, IOException {
            return (SubsequentVisitItemType)XmlBeans.getContextTypeLoader().parse(u, SubsequentVisitItemType.type, options);
        }
        
        public static SubsequentVisitItemType parse(final InputStream is) throws XmlException, IOException {
            return (SubsequentVisitItemType)XmlBeans.getContextTypeLoader().parse(is, SubsequentVisitItemType.type, (XmlOptions)null);
        }
        
        public static SubsequentVisitItemType parse(final InputStream is, final XmlOptions options) throws XmlException, IOException {
            return (SubsequentVisitItemType)XmlBeans.getContextTypeLoader().parse(is, SubsequentVisitItemType.type, options);
        }
        
        public static SubsequentVisitItemType parse(final Reader r) throws XmlException, IOException {
            return (SubsequentVisitItemType)XmlBeans.getContextTypeLoader().parse(r, SubsequentVisitItemType.type, (XmlOptions)null);
        }
        
        public static SubsequentVisitItemType parse(final Reader r, final XmlOptions options) throws XmlException, IOException {
            return (SubsequentVisitItemType)XmlBeans.getContextTypeLoader().parse(r, SubsequentVisitItemType.type, options);
        }
        
        public static SubsequentVisitItemType parse(final XMLStreamReader sr) throws XmlException {
            return (SubsequentVisitItemType)XmlBeans.getContextTypeLoader().parse(sr, SubsequentVisitItemType.type, (XmlOptions)null);
        }
        
        public static SubsequentVisitItemType parse(final XMLStreamReader sr, final XmlOptions options) throws XmlException {
            return (SubsequentVisitItemType)XmlBeans.getContextTypeLoader().parse(sr, SubsequentVisitItemType.type, options);
        }
        
        public static SubsequentVisitItemType parse(final Node node) throws XmlException {
            return (SubsequentVisitItemType)XmlBeans.getContextTypeLoader().parse(node, SubsequentVisitItemType.type, (XmlOptions)null);
        }
        
        public static SubsequentVisitItemType parse(final Node node, final XmlOptions options) throws XmlException {
            return (SubsequentVisitItemType)XmlBeans.getContextTypeLoader().parse(node, SubsequentVisitItemType.type, options);
        }
        
        @Deprecated
        public static SubsequentVisitItemType parse(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return (SubsequentVisitItemType)XmlBeans.getContextTypeLoader().parse(xis, SubsequentVisitItemType.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static SubsequentVisitItemType parse(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return (SubsequentVisitItemType)XmlBeans.getContextTypeLoader().parse(xis, SubsequentVisitItemType.type, options);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, SubsequentVisitItemType.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, SubsequentVisitItemType.type, options);
        }
        
        private Factory() {
        }
    }
}
