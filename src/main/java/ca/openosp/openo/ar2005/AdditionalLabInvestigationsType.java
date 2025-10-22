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
import org.apache.xmlbeans.StringEnumAbstractBase;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlObject;

public interface AdditionalLabInvestigationsType extends XmlObject
{
    public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(AdditionalLabInvestigationsType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9C023B7D67311A3187802DA7FD51EA38").resolveHandle("additionallabinvestigationstypef776type");
    
    String getHb();
    
    XmlString xgetHb();
    
    void setHb(final String p0);
    
    void xsetHb(final XmlString p0);
    
    BloodGroup.Enum getBloodGroup();
    
    BloodGroup xgetBloodGroup();
    
    void setBloodGroup(final BloodGroup.Enum p0);
    
    void xsetBloodGroup(final BloodGroup p0);
    
    Rh.Enum getRh();
    
    Rh xgetRh();
    
    void setRh(final Rh.Enum p0);
    
    void xsetRh(final Rh p0);
    
    String getRepeatABS();
    
    XmlString xgetRepeatABS();
    
    void setRepeatABS(final String p0);
    
    void xsetRepeatABS(final XmlString p0);
    
    String getGCT();
    
    XmlString xgetGCT();
    
    void setGCT(final String p0);
    
    void xsetGCT(final XmlString p0);
    
    String getGTT();
    
    XmlString xgetGTT();
    
    void setGTT(final String p0);
    
    void xsetGTT(final XmlString p0);
    
    GBS.Enum getGBS();
    
    GBS xgetGBS();
    
    void setGBS(final GBS.Enum p0);
    
    void xsetGBS(final GBS p0);
    
    CustomLab getCustomLab1();
    
    void setCustomLab1(final CustomLab p0);
    
    CustomLab addNewCustomLab1();
    
    CustomLab getCustomLab2();
    
    void setCustomLab2(final CustomLab p0);
    
    CustomLab addNewCustomLab2();
    
    CustomLab getCustomLab3();
    
    void setCustomLab3(final CustomLab p0);
    
    CustomLab addNewCustomLab3();
    
    CustomLab getCustomLab4();
    
    void setCustomLab4(final CustomLab p0);
    
    CustomLab addNewCustomLab4();
    
    public interface BloodGroup extends XmlString
    {
        public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(BloodGroup.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9C023B7D67311A3187802DA7FD51EA38").resolveHandle("bloodgroup51f7elemtype");
        public static final Enum A = Enum.forString("A");
        public static final Enum B = Enum.forString("B");
        public static final Enum AB = Enum.forString("AB");
        public static final Enum O = Enum.forString("O");
        public static final Enum UN = Enum.forString("UN");
        public static final Enum ND = Enum.forString("ND");
        public static final int INT_A = 1;
        public static final int INT_B = 2;
        public static final int INT_AB = 3;
        public static final int INT_O = 4;
        public static final int INT_UN = 5;
        public static final int INT_ND = 6;
        
        StringEnumAbstractBase enumValue();
        
        void set(final StringEnumAbstractBase p0);
        
        public static final class Enum extends StringEnumAbstractBase
        {
            static final int INT_A = 1;
            static final int INT_B = 2;
            static final int INT_AB = 3;
            static final int INT_O = 4;
            static final int INT_UN = 5;
            static final int INT_ND = 6;
            public static final StringEnumAbstractBase.Table table;
            private static final long serialVersionUID = 1L;
            
            public static Enum forString(final String s) {
                return (Enum)Enum.table.forString(s);
            }
            
            public static Enum forInt(final int i) {
                return (Enum)Enum.table.forInt(i);
            }
            
            private Enum(final String s, final int i) {
                super(s, i);
            }
            
            private Object readResolve() {
                return forInt(this.intValue());
            }
            
            static {
                table = new StringEnumAbstractBase.Table((StringEnumAbstractBase[])new Enum[] { new Enum("A", 1), new Enum("B", 2), new Enum("AB", 3), new Enum("O", 4), new Enum("UN", 5), new Enum("ND", 6) });
            }
        }
        
        public static final class Factory
        {
            public static BloodGroup newValue(final Object obj) {
                return (BloodGroup)BloodGroup.type.newValue(obj);
            }
            
            public static BloodGroup newInstance() {
                return (BloodGroup)XmlBeans.getContextTypeLoader().newInstance(BloodGroup.type, (XmlOptions)null);
            }
            
            public static BloodGroup newInstance(final XmlOptions options) {
                return (BloodGroup)XmlBeans.getContextTypeLoader().newInstance(BloodGroup.type, options);
            }
            
            private Factory() {
            }
        }
    }
    
    public interface Rh extends XmlString
    {
        public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(Rh.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9C023B7D67311A3187802DA7FD51EA38").resolveHandle("rh52c8elemtype");
        public static final Enum POS = Enum.forString("POS");
        public static final Enum WPOS = Enum.forString("WPOS");
        public static final Enum NEG = Enum.forString("NEG");
        public static final Enum NDONE = Enum.forString("NDONE");
        public static final Enum UNK = Enum.forString("UNK");
        public static final int INT_POS = 1;
        public static final int INT_WPOS = 2;
        public static final int INT_NEG = 3;
        public static final int INT_NDONE = 4;
        public static final int INT_UNK = 5;
        
        StringEnumAbstractBase enumValue();
        
        void set(final StringEnumAbstractBase p0);
        
        public static final class Enum extends StringEnumAbstractBase
        {
            static final int INT_POS = 1;
            static final int INT_WPOS = 2;
            static final int INT_NEG = 3;
            static final int INT_NDONE = 4;
            static final int INT_UNK = 5;
            public static final StringEnumAbstractBase.Table table;
            private static final long serialVersionUID = 1L;
            
            public static Enum forString(final String s) {
                return (Enum)Enum.table.forString(s);
            }
            
            public static Enum forInt(final int i) {
                return (Enum)Enum.table.forInt(i);
            }
            
            private Enum(final String s, final int i) {
                super(s, i);
            }
            
            private Object readResolve() {
                return forInt(this.intValue());
            }
            
            static {
                table = new StringEnumAbstractBase.Table((StringEnumAbstractBase[])new Enum[] { new Enum("POS", 1), new Enum("WPOS", 2), new Enum("NEG", 3), new Enum("NDONE", 4), new Enum("UNK", 5) });
            }
        }
        
        public static final class Factory
        {
            public static Rh newValue(final Object obj) {
                return (Rh)Rh.type.newValue(obj);
            }
            
            public static Rh newInstance() {
                return (Rh)XmlBeans.getContextTypeLoader().newInstance(Rh.type, (XmlOptions)null);
            }
            
            public static Rh newInstance(final XmlOptions options) {
                return (Rh)XmlBeans.getContextTypeLoader().newInstance(Rh.type, options);
            }
            
            private Factory() {
            }
        }
    }
    
    public interface GBS extends XmlString
    {
        public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(GBS.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9C023B7D67311A3187802DA7FD51EA38").resolveHandle("gbs77baelemtype");
        public static final Enum NDONE = Enum.forString("NDONE");
        public static final Enum POSSWAB = Enum.forString("POSSWAB");
        public static final Enum POSURINE = Enum.forString("POSURINE");
        public static final Enum NEGSWAB = Enum.forString("NEGSWAB");
        public static final Enum DONEUNK = Enum.forString("DONEUNK");
        public static final Enum UNK = Enum.forString("UNK");
        public static final int INT_NDONE = 1;
        public static final int INT_POSSWAB = 2;
        public static final int INT_POSURINE = 3;
        public static final int INT_NEGSWAB = 4;
        public static final int INT_DONEUNK = 5;
        public static final int INT_UNK = 6;
        
        StringEnumAbstractBase enumValue();
        
        void set(final StringEnumAbstractBase p0);
        
        public static final class Enum extends StringEnumAbstractBase
        {
            static final int INT_NDONE = 1;
            static final int INT_POSSWAB = 2;
            static final int INT_POSURINE = 3;
            static final int INT_NEGSWAB = 4;
            static final int INT_DONEUNK = 5;
            static final int INT_UNK = 6;
            public static final StringEnumAbstractBase.Table table;
            private static final long serialVersionUID = 1L;
            
            public static Enum forString(final String s) {
                return (Enum)Enum.table.forString(s);
            }
            
            public static Enum forInt(final int i) {
                return (Enum)Enum.table.forInt(i);
            }
            
            private Enum(final String s, final int i) {
                super(s, i);
            }
            
            private Object readResolve() {
                return forInt(this.intValue());
            }
            
            static {
                table = new StringEnumAbstractBase.Table((StringEnumAbstractBase[])new Enum[] { new Enum("NDONE", 1), new Enum("POSSWAB", 2), new Enum("POSURINE", 3), new Enum("NEGSWAB", 4), new Enum("DONEUNK", 5), new Enum("UNK", 6) });
            }
        }
        
        public static final class Factory
        {
            public static GBS newValue(final Object obj) {
                return (GBS)GBS.type.newValue(obj);
            }
            
            public static GBS newInstance() {
                return (GBS)XmlBeans.getContextTypeLoader().newInstance(GBS.type, (XmlOptions)null);
            }
            
            public static GBS newInstance(final XmlOptions options) {
                return (GBS)XmlBeans.getContextTypeLoader().newInstance(GBS.type, options);
            }
            
            private Factory() {
            }
        }
    }
    
    public static final class Factory
    {
        public static AdditionalLabInvestigationsType newInstance() {
            return (AdditionalLabInvestigationsType)XmlBeans.getContextTypeLoader().newInstance(AdditionalLabInvestigationsType.type, (XmlOptions)null);
        }
        
        public static AdditionalLabInvestigationsType newInstance(final XmlOptions options) {
            return (AdditionalLabInvestigationsType)XmlBeans.getContextTypeLoader().newInstance(AdditionalLabInvestigationsType.type, options);
        }
        
        public static AdditionalLabInvestigationsType parse(final String xmlAsString) throws XmlException {
            return (AdditionalLabInvestigationsType)XmlBeans.getContextTypeLoader().parse(xmlAsString, AdditionalLabInvestigationsType.type, (XmlOptions)null);
        }
        
        public static AdditionalLabInvestigationsType parse(final String xmlAsString, final XmlOptions options) throws XmlException {
            return (AdditionalLabInvestigationsType)XmlBeans.getContextTypeLoader().parse(xmlAsString, AdditionalLabInvestigationsType.type, options);
        }
        
        public static AdditionalLabInvestigationsType parse(final File file) throws XmlException, IOException {
            return (AdditionalLabInvestigationsType)XmlBeans.getContextTypeLoader().parse(file, AdditionalLabInvestigationsType.type, (XmlOptions)null);
        }
        
        public static AdditionalLabInvestigationsType parse(final File file, final XmlOptions options) throws XmlException, IOException {
            return (AdditionalLabInvestigationsType)XmlBeans.getContextTypeLoader().parse(file, AdditionalLabInvestigationsType.type, options);
        }
        
        public static AdditionalLabInvestigationsType parse(final URL u) throws XmlException, IOException {
            return (AdditionalLabInvestigationsType)XmlBeans.getContextTypeLoader().parse(u, AdditionalLabInvestigationsType.type, (XmlOptions)null);
        }
        
        public static AdditionalLabInvestigationsType parse(final URL u, final XmlOptions options) throws XmlException, IOException {
            return (AdditionalLabInvestigationsType)XmlBeans.getContextTypeLoader().parse(u, AdditionalLabInvestigationsType.type, options);
        }
        
        public static AdditionalLabInvestigationsType parse(final InputStream is) throws XmlException, IOException {
            return (AdditionalLabInvestigationsType)XmlBeans.getContextTypeLoader().parse(is, AdditionalLabInvestigationsType.type, (XmlOptions)null);
        }
        
        public static AdditionalLabInvestigationsType parse(final InputStream is, final XmlOptions options) throws XmlException, IOException {
            return (AdditionalLabInvestigationsType)XmlBeans.getContextTypeLoader().parse(is, AdditionalLabInvestigationsType.type, options);
        }
        
        public static AdditionalLabInvestigationsType parse(final Reader r) throws XmlException, IOException {
            return (AdditionalLabInvestigationsType)XmlBeans.getContextTypeLoader().parse(r, AdditionalLabInvestigationsType.type, (XmlOptions)null);
        }
        
        public static AdditionalLabInvestigationsType parse(final Reader r, final XmlOptions options) throws XmlException, IOException {
            return (AdditionalLabInvestigationsType)XmlBeans.getContextTypeLoader().parse(r, AdditionalLabInvestigationsType.type, options);
        }
        
        public static AdditionalLabInvestigationsType parse(final XMLStreamReader sr) throws XmlException {
            return (AdditionalLabInvestigationsType)XmlBeans.getContextTypeLoader().parse(sr, AdditionalLabInvestigationsType.type, (XmlOptions)null);
        }
        
        public static AdditionalLabInvestigationsType parse(final XMLStreamReader sr, final XmlOptions options) throws XmlException {
            return (AdditionalLabInvestigationsType)XmlBeans.getContextTypeLoader().parse(sr, AdditionalLabInvestigationsType.type, options);
        }
        
        public static AdditionalLabInvestigationsType parse(final Node node) throws XmlException {
            return (AdditionalLabInvestigationsType)XmlBeans.getContextTypeLoader().parse(node, AdditionalLabInvestigationsType.type, (XmlOptions)null);
        }
        
        public static AdditionalLabInvestigationsType parse(final Node node, final XmlOptions options) throws XmlException {
            return (AdditionalLabInvestigationsType)XmlBeans.getContextTypeLoader().parse(node, AdditionalLabInvestigationsType.type, options);
        }
        
        @Deprecated
        public static AdditionalLabInvestigationsType parse(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return (AdditionalLabInvestigationsType)XmlBeans.getContextTypeLoader().parse(xis, AdditionalLabInvestigationsType.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static AdditionalLabInvestigationsType parse(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return (AdditionalLabInvestigationsType)XmlBeans.getContextTypeLoader().parse(xis, AdditionalLabInvestigationsType.type, options);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, AdditionalLabInvestigationsType.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, AdditionalLabInvestigationsType.type, options);
        }
        
        private Factory() {
        }
    }
}
