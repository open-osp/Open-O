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
import org.apache.xmlbeans.XmlFloat;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlObject;

public interface PhysicalExaminationType extends XmlObject
{
    public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(PhysicalExaminationType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9C023B7D67311A3187802DA7FD51EA38").resolveHandle("physicalexaminationtypeb2d9type");
    
    float getHeight();
    
    Height xgetHeight();
    
    boolean isNilHeight();
    
    void setHeight(final float p0);
    
    void xsetHeight(final Height p0);
    
    void setNilHeight();
    
    float getWeight();
    
    Weight xgetWeight();
    
    boolean isNilWeight();
    
    void setWeight(final float p0);
    
    void xsetWeight(final Weight p0);
    
    void setNilWeight();
    
    float getBmi();
    
    Bmi xgetBmi();
    
    boolean isNilBmi();
    
    void setBmi(final float p0);
    
    void xsetBmi(final Bmi p0);
    
    void setNilBmi();
    
    String getBp();
    
    Bp xgetBp();
    
    void setBp(final String p0);
    
    void xsetBp(final Bp p0);
    
    NormalAbnormalNullType getThyroid();
    
    void setThyroid(final NormalAbnormalNullType p0);
    
    NormalAbnormalNullType addNewThyroid();
    
    NormalAbnormalNullType getChest();
    
    void setChest(final NormalAbnormalNullType p0);
    
    NormalAbnormalNullType addNewChest();
    
    NormalAbnormalNullType getBreasts();
    
    void setBreasts(final NormalAbnormalNullType p0);
    
    NormalAbnormalNullType addNewBreasts();
    
    NormalAbnormalNullType getCardiovascular();
    
    void setCardiovascular(final NormalAbnormalNullType p0);
    
    NormalAbnormalNullType addNewCardiovascular();
    
    NormalAbnormalNullType getAbdomen();
    
    void setAbdomen(final NormalAbnormalNullType p0);
    
    NormalAbnormalNullType addNewAbdomen();
    
    NormalAbnormalNullType getVaricosities();
    
    void setVaricosities(final NormalAbnormalNullType p0);
    
    NormalAbnormalNullType addNewVaricosities();
    
    NormalAbnormalNullType getExernalGenitals();
    
    void setExernalGenitals(final NormalAbnormalNullType p0);
    
    NormalAbnormalNullType addNewExernalGenitals();
    
    NormalAbnormalNullType getCervixVagina();
    
    void setCervixVagina(final NormalAbnormalNullType p0);
    
    NormalAbnormalNullType addNewCervixVagina();
    
    NormalAbnormalNullType getUterus();
    
    void setUterus(final NormalAbnormalNullType p0);
    
    NormalAbnormalNullType addNewUterus();
    
    String getUterusSize();
    
    XmlString xgetUterusSize();
    
    void setUterusSize(final String p0);
    
    void xsetUterusSize(final XmlString p0);
    
    NormalAbnormalNullType getAdnexa();
    
    void setAdnexa(final NormalAbnormalNullType p0);
    
    NormalAbnormalNullType addNewAdnexa();
    
    String getOtherDescr();
    
    XmlString xgetOtherDescr();
    
    void setOtherDescr(final String p0);
    
    void xsetOtherDescr(final XmlString p0);
    
    NormalAbnormalNullType getOther();
    
    void setOther(final NormalAbnormalNullType p0);
    
    NormalAbnormalNullType addNewOther();
    
    public interface Height extends XmlFloat
    {
        public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(Height.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9C023B7D67311A3187802DA7FD51EA38").resolveHandle("heightf75celemtype");
        
        public static final class Factory
        {
            public static Height newValue(final Object obj) {
                return (Height)Height.type.newValue(obj);
            }
            
            public static Height newInstance() {
                return (Height)XmlBeans.getContextTypeLoader().newInstance(Height.type, (XmlOptions)null);
            }
            
            public static Height newInstance(final XmlOptions options) {
                return (Height)XmlBeans.getContextTypeLoader().newInstance(Height.type, options);
            }
            
            private Factory() {
            }
        }
    }
    
    public interface Weight extends XmlFloat
    {
        public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(Weight.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9C023B7D67311A3187802DA7FD51EA38").resolveHandle("weightc0edelemtype");
        
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
    
    public interface Bmi extends XmlFloat
    {
        public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(Bmi.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9C023B7D67311A3187802DA7FD51EA38").resolveHandle("bmi6343elemtype");
        
        public static final class Factory
        {
            public static Bmi newValue(final Object obj) {
                return (Bmi)Bmi.type.newValue(obj);
            }
            
            public static Bmi newInstance() {
                return (Bmi)XmlBeans.getContextTypeLoader().newInstance(Bmi.type, (XmlOptions)null);
            }
            
            public static Bmi newInstance(final XmlOptions options) {
                return (Bmi)XmlBeans.getContextTypeLoader().newInstance(Bmi.type, options);
            }
            
            private Factory() {
            }
        }
    }
    
    public interface Bp extends XmlString
    {
        public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(Bp.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9C023B7D67311A3187802DA7FD51EA38").resolveHandle("bp6443elemtype");
        
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
        public static PhysicalExaminationType newInstance() {
            return (PhysicalExaminationType)XmlBeans.getContextTypeLoader().newInstance(PhysicalExaminationType.type, (XmlOptions)null);
        }
        
        public static PhysicalExaminationType newInstance(final XmlOptions options) {
            return (PhysicalExaminationType)XmlBeans.getContextTypeLoader().newInstance(PhysicalExaminationType.type, options);
        }
        
        public static PhysicalExaminationType parse(final String xmlAsString) throws XmlException {
            return (PhysicalExaminationType)XmlBeans.getContextTypeLoader().parse(xmlAsString, PhysicalExaminationType.type, (XmlOptions)null);
        }
        
        public static PhysicalExaminationType parse(final String xmlAsString, final XmlOptions options) throws XmlException {
            return (PhysicalExaminationType)XmlBeans.getContextTypeLoader().parse(xmlAsString, PhysicalExaminationType.type, options);
        }
        
        public static PhysicalExaminationType parse(final File file) throws XmlException, IOException {
            return (PhysicalExaminationType)XmlBeans.getContextTypeLoader().parse(file, PhysicalExaminationType.type, (XmlOptions)null);
        }
        
        public static PhysicalExaminationType parse(final File file, final XmlOptions options) throws XmlException, IOException {
            return (PhysicalExaminationType)XmlBeans.getContextTypeLoader().parse(file, PhysicalExaminationType.type, options);
        }
        
        public static PhysicalExaminationType parse(final URL u) throws XmlException, IOException {
            return (PhysicalExaminationType)XmlBeans.getContextTypeLoader().parse(u, PhysicalExaminationType.type, (XmlOptions)null);
        }
        
        public static PhysicalExaminationType parse(final URL u, final XmlOptions options) throws XmlException, IOException {
            return (PhysicalExaminationType)XmlBeans.getContextTypeLoader().parse(u, PhysicalExaminationType.type, options);
        }
        
        public static PhysicalExaminationType parse(final InputStream is) throws XmlException, IOException {
            return (PhysicalExaminationType)XmlBeans.getContextTypeLoader().parse(is, PhysicalExaminationType.type, (XmlOptions)null);
        }
        
        public static PhysicalExaminationType parse(final InputStream is, final XmlOptions options) throws XmlException, IOException {
            return (PhysicalExaminationType)XmlBeans.getContextTypeLoader().parse(is, PhysicalExaminationType.type, options);
        }
        
        public static PhysicalExaminationType parse(final Reader r) throws XmlException, IOException {
            return (PhysicalExaminationType)XmlBeans.getContextTypeLoader().parse(r, PhysicalExaminationType.type, (XmlOptions)null);
        }
        
        public static PhysicalExaminationType parse(final Reader r, final XmlOptions options) throws XmlException, IOException {
            return (PhysicalExaminationType)XmlBeans.getContextTypeLoader().parse(r, PhysicalExaminationType.type, options);
        }
        
        public static PhysicalExaminationType parse(final XMLStreamReader sr) throws XmlException {
            return (PhysicalExaminationType)XmlBeans.getContextTypeLoader().parse(sr, PhysicalExaminationType.type, (XmlOptions)null);
        }
        
        public static PhysicalExaminationType parse(final XMLStreamReader sr, final XmlOptions options) throws XmlException {
            return (PhysicalExaminationType)XmlBeans.getContextTypeLoader().parse(sr, PhysicalExaminationType.type, options);
        }
        
        public static PhysicalExaminationType parse(final Node node) throws XmlException {
            return (PhysicalExaminationType)XmlBeans.getContextTypeLoader().parse(node, PhysicalExaminationType.type, (XmlOptions)null);
        }
        
        public static PhysicalExaminationType parse(final Node node, final XmlOptions options) throws XmlException {
            return (PhysicalExaminationType)XmlBeans.getContextTypeLoader().parse(node, PhysicalExaminationType.type, options);
        }
        
        @Deprecated
        public static PhysicalExaminationType parse(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return (PhysicalExaminationType)XmlBeans.getContextTypeLoader().parse(xis, PhysicalExaminationType.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static PhysicalExaminationType parse(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return (PhysicalExaminationType)XmlBeans.getContextTypeLoader().parse(xis, PhysicalExaminationType.type, options);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, PhysicalExaminationType.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, PhysicalExaminationType.type, options);
        }
        
        private Factory() {
        }
    }
}
