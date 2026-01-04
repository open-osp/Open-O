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
import org.apache.xmlbeans.XmlFloat;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.XmlInt;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlObject;

public interface ObstetricalHistoryItemList extends XmlObject
{
    public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(ObstetricalHistoryItemList.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9C023B7D67311A3187802DA7FD51EA38").resolveHandle("obstetricalhistoryitemliste7c8type");
    
    int getYear();
    
    XmlInt xgetYear();
    
    void setYear(final int p0);
    
    void xsetYear(final XmlInt p0);
    
    Sex.Enum getSex();
    
    Sex xgetSex();
    
    void setSex(final Sex.Enum p0);
    
    void xsetSex(final Sex p0);
    
    int getGestAge();
    
    XmlInt xgetGestAge();
    
    void setGestAge(final int p0);
    
    void xsetGestAge(final XmlInt p0);
    
    String getBirthWeight();
    
    XmlString xgetBirthWeight();
    
    void setBirthWeight(final String p0);
    
    void xsetBirthWeight(final XmlString p0);
    
    float getLengthOfLabour();
    
    XmlFloat xgetLengthOfLabour();
    
    boolean isNilLengthOfLabour();
    
    void setLengthOfLabour(final float p0);
    
    void xsetLengthOfLabour(final XmlFloat p0);
    
    void setNilLengthOfLabour();
    
    String getPlaceOfBirth();
    
    XmlString xgetPlaceOfBirth();
    
    void setPlaceOfBirth(final String p0);
    
    void xsetPlaceOfBirth(final XmlString p0);
    
    TypeOfDelivery.Enum getTypeOfDelivery();
    
    TypeOfDelivery xgetTypeOfDelivery();
    
    void setTypeOfDelivery(final TypeOfDelivery.Enum p0);
    
    void xsetTypeOfDelivery(final TypeOfDelivery p0);
    
    String getComments();
    
    XmlString xgetComments();
    
    void setComments(final String p0);
    
    void xsetComments(final XmlString p0);
    
    public interface Sex extends XmlString
    {
        public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(Sex.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9C023B7D67311A3187802DA7FD51EA38").resolveHandle("sex4536elemtype");
        public static final Enum M = Enum.forString("M");
        public static final Enum F = Enum.forString("F");
        public static final Enum A = Enum.forString("A");
        public static final Enum U = Enum.forString("U");
        public static final int INT_M = 1;
        public static final int INT_F = 2;
        public static final int INT_A = 3;
        public static final int INT_U = 4;
        
        StringEnumAbstractBase enumValue();
        
        void set(final StringEnumAbstractBase p0);
        
        public static final class Enum extends StringEnumAbstractBase
        {
            static final int INT_M = 1;
            static final int INT_F = 2;
            static final int INT_A = 3;
            static final int INT_U = 4;
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
                table = new StringEnumAbstractBase.Table((StringEnumAbstractBase[])new Enum[] { new Enum("M", 1), new Enum("F", 2), new Enum("A", 3), new Enum("U", 4) });
            }
        }
        
        public static final class Factory
        {
            public static Sex newValue(final Object obj) {
                return (Sex)Sex.type.newValue(obj);
            }
            
            public static Sex newInstance() {
                return (Sex)XmlBeans.getContextTypeLoader().newInstance(Sex.type, (XmlOptions)null);
            }
            
            public static Sex newInstance(final XmlOptions options) {
                return (Sex)XmlBeans.getContextTypeLoader().newInstance(Sex.type, options);
            }
            
            private Factory() {
            }
        }
    }
    
    public interface TypeOfDelivery extends XmlString
    {
        public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(TypeOfDelivery.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9C023B7D67311A3187802DA7FD51EA38").resolveHandle("typeofdelivery6b87elemtype");
        public static final Enum AVAG = Enum.forString("AVAG");
        public static final Enum IND = Enum.forString("IND");
        public static final Enum CS = Enum.forString("CS");
        public static final Enum SVAG = Enum.forString("SVAG");
        public static final Enum VAG = Enum.forString("VAG");
        public static final Enum UN = Enum.forString("UN");
        public static final int INT_AVAG = 1;
        public static final int INT_IND = 2;
        public static final int INT_CS = 3;
        public static final int INT_SVAG = 4;
        public static final int INT_VAG = 5;
        public static final int INT_UN = 6;
        
        StringEnumAbstractBase enumValue();
        
        void set(final StringEnumAbstractBase p0);
        
        public static final class Enum extends StringEnumAbstractBase
        {
            static final int INT_AVAG = 1;
            static final int INT_IND = 2;
            static final int INT_CS = 3;
            static final int INT_SVAG = 4;
            static final int INT_VAG = 5;
            static final int INT_UN = 6;
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
                table = new StringEnumAbstractBase.Table((StringEnumAbstractBase[])new Enum[] { new Enum("AVAG", 1), new Enum("IND", 2), new Enum("CS", 3), new Enum("SVAG", 4), new Enum("VAG", 5), new Enum("UN", 6) });
            }
        }
        
        public static final class Factory
        {
            public static TypeOfDelivery newValue(final Object obj) {
                return (TypeOfDelivery)TypeOfDelivery.type.newValue(obj);
            }
            
            public static TypeOfDelivery newInstance() {
                return (TypeOfDelivery)XmlBeans.getContextTypeLoader().newInstance(TypeOfDelivery.type, (XmlOptions)null);
            }
            
            public static TypeOfDelivery newInstance(final XmlOptions options) {
                return (TypeOfDelivery)XmlBeans.getContextTypeLoader().newInstance(TypeOfDelivery.type, options);
            }
            
            private Factory() {
            }
        }
    }
    
    public static final class Factory
    {
        public static ObstetricalHistoryItemList newInstance() {
            return (ObstetricalHistoryItemList)XmlBeans.getContextTypeLoader().newInstance(ObstetricalHistoryItemList.type, (XmlOptions)null);
        }
        
        public static ObstetricalHistoryItemList newInstance(final XmlOptions options) {
            return (ObstetricalHistoryItemList)XmlBeans.getContextTypeLoader().newInstance(ObstetricalHistoryItemList.type, options);
        }
        
        public static ObstetricalHistoryItemList parse(final String xmlAsString) throws XmlException {
            return (ObstetricalHistoryItemList)XmlBeans.getContextTypeLoader().parse(xmlAsString, ObstetricalHistoryItemList.type, (XmlOptions)null);
        }
        
        public static ObstetricalHistoryItemList parse(final String xmlAsString, final XmlOptions options) throws XmlException {
            return (ObstetricalHistoryItemList)XmlBeans.getContextTypeLoader().parse(xmlAsString, ObstetricalHistoryItemList.type, options);
        }
        
        public static ObstetricalHistoryItemList parse(final File file) throws XmlException, IOException {
            return (ObstetricalHistoryItemList)XmlBeans.getContextTypeLoader().parse(file, ObstetricalHistoryItemList.type, (XmlOptions)null);
        }
        
        public static ObstetricalHistoryItemList parse(final File file, final XmlOptions options) throws XmlException, IOException {
            return (ObstetricalHistoryItemList)XmlBeans.getContextTypeLoader().parse(file, ObstetricalHistoryItemList.type, options);
        }
        
        public static ObstetricalHistoryItemList parse(final URL u) throws XmlException, IOException {
            return (ObstetricalHistoryItemList)XmlBeans.getContextTypeLoader().parse(u, ObstetricalHistoryItemList.type, (XmlOptions)null);
        }
        
        public static ObstetricalHistoryItemList parse(final URL u, final XmlOptions options) throws XmlException, IOException {
            return (ObstetricalHistoryItemList)XmlBeans.getContextTypeLoader().parse(u, ObstetricalHistoryItemList.type, options);
        }
        
        public static ObstetricalHistoryItemList parse(final InputStream is) throws XmlException, IOException {
            return (ObstetricalHistoryItemList)XmlBeans.getContextTypeLoader().parse(is, ObstetricalHistoryItemList.type, (XmlOptions)null);
        }
        
        public static ObstetricalHistoryItemList parse(final InputStream is, final XmlOptions options) throws XmlException, IOException {
            return (ObstetricalHistoryItemList)XmlBeans.getContextTypeLoader().parse(is, ObstetricalHistoryItemList.type, options);
        }
        
        public static ObstetricalHistoryItemList parse(final Reader r) throws XmlException, IOException {
            return (ObstetricalHistoryItemList)XmlBeans.getContextTypeLoader().parse(r, ObstetricalHistoryItemList.type, (XmlOptions)null);
        }
        
        public static ObstetricalHistoryItemList parse(final Reader r, final XmlOptions options) throws XmlException, IOException {
            return (ObstetricalHistoryItemList)XmlBeans.getContextTypeLoader().parse(r, ObstetricalHistoryItemList.type, options);
        }
        
        public static ObstetricalHistoryItemList parse(final XMLStreamReader sr) throws XmlException {
            return (ObstetricalHistoryItemList)XmlBeans.getContextTypeLoader().parse(sr, ObstetricalHistoryItemList.type, (XmlOptions)null);
        }
        
        public static ObstetricalHistoryItemList parse(final XMLStreamReader sr, final XmlOptions options) throws XmlException {
            return (ObstetricalHistoryItemList)XmlBeans.getContextTypeLoader().parse(sr, ObstetricalHistoryItemList.type, options);
        }
        
        public static ObstetricalHistoryItemList parse(final Node node) throws XmlException {
            return (ObstetricalHistoryItemList)XmlBeans.getContextTypeLoader().parse(node, ObstetricalHistoryItemList.type, (XmlOptions)null);
        }
        
        public static ObstetricalHistoryItemList parse(final Node node, final XmlOptions options) throws XmlException {
            return (ObstetricalHistoryItemList)XmlBeans.getContextTypeLoader().parse(node, ObstetricalHistoryItemList.type, options);
        }
        
        @Deprecated
        public static ObstetricalHistoryItemList parse(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return (ObstetricalHistoryItemList)XmlBeans.getContextTypeLoader().parse(xis, ObstetricalHistoryItemList.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static ObstetricalHistoryItemList parse(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return (ObstetricalHistoryItemList)XmlBeans.getContextTypeLoader().parse(xis, ObstetricalHistoryItemList.type, options);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, ObstetricalHistoryItemList.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, ObstetricalHistoryItemList.type, options);
        }
        
        private Factory() {
        }
    }
}
