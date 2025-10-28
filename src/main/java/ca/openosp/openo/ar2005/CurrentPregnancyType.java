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
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlObject;

public interface CurrentPregnancyType extends XmlObject
{
    public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(CurrentPregnancyType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9C023B7D67311A3187802DA7FD51EA38").resolveHandle("currentpregnancytype53a5type");
    
    YesNoNullType getBleeding();
    
    void setBleeding(final YesNoNullType p0);
    
    YesNoNullType addNewBleeding();
    
    YesNoNullType getNausea();
    
    void setNausea(final YesNoNullType p0);
    
    YesNoNullType addNewNausea();
    
    YesNoNullType getSmoking();
    
    void setSmoking(final YesNoNullType p0);
    
    YesNoNullType addNewSmoking();
    
    CigsPerDay.Enum getCigsPerDay();
    
    CigsPerDay xgetCigsPerDay();
    
    void setCigsPerDay(final CigsPerDay.Enum p0);
    
    void xsetCigsPerDay(final CigsPerDay p0);
    
    YesNoNullType getAlcoholDrugs();
    
    void setAlcoholDrugs(final YesNoNullType p0);
    
    YesNoNullType addNewAlcoholDrugs();
    
    YesNoNullType getOccEnvRisks();
    
    void setOccEnvRisks(final YesNoNullType p0);
    
    YesNoNullType addNewOccEnvRisks();
    
    YesNoNullType getDietaryRes();
    
    void setDietaryRes(final YesNoNullType p0);
    
    YesNoNullType addNewDietaryRes();
    
    YesNoNullType getCalciumAdequate();
    
    void setCalciumAdequate(final YesNoNullType p0);
    
    YesNoNullType addNewCalciumAdequate();
    
    YesNoNullType getFolate();
    
    void setFolate(final YesNoNullType p0);
    
    YesNoNullType addNewFolate();
    
    public interface CigsPerDay extends XmlString
    {
        public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(CigsPerDay.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9C023B7D67311A3187802DA7FD51EA38").resolveHandle("cigsperday4d38elemtype");
        public static final Enum X = Enum.forString("");
        public static final Enum LESS_10 = Enum.forString("LESS10");
        public static final Enum UP_20 = Enum.forString("UP20");
        public static final Enum OVER_20 = Enum.forString("OVER20");
        public static final int INT_X = 1;
        public static final int INT_LESS_10 = 2;
        public static final int INT_UP_20 = 3;
        public static final int INT_OVER_20 = 4;
        
        StringEnumAbstractBase enumValue();
        
        void set(final StringEnumAbstractBase p0);
        
        public static final class Enum extends StringEnumAbstractBase
        {
            static final int INT_X = 1;
            static final int INT_LESS_10 = 2;
            static final int INT_UP_20 = 3;
            static final int INT_OVER_20 = 4;
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
                table = new StringEnumAbstractBase.Table((StringEnumAbstractBase[])new Enum[] { new Enum("", 1), new Enum("LESS10", 2), new Enum("UP20", 3), new Enum("OVER20", 4) });
            }
        }
        
        public static final class Factory
        {
            public static CigsPerDay newValue(final Object obj) {
                return (CigsPerDay)CigsPerDay.type.newValue(obj);
            }
            
            public static CigsPerDay newInstance() {
                return (CigsPerDay)XmlBeans.getContextTypeLoader().newInstance(CigsPerDay.type, (XmlOptions)null);
            }
            
            public static CigsPerDay newInstance(final XmlOptions options) {
                return (CigsPerDay)XmlBeans.getContextTypeLoader().newInstance(CigsPerDay.type, options);
            }
            
            private Factory() {
            }
        }
    }
    
    public static final class Factory
    {
        public static CurrentPregnancyType newInstance() {
            return (CurrentPregnancyType)XmlBeans.getContextTypeLoader().newInstance(CurrentPregnancyType.type, (XmlOptions)null);
        }
        
        public static CurrentPregnancyType newInstance(final XmlOptions options) {
            return (CurrentPregnancyType)XmlBeans.getContextTypeLoader().newInstance(CurrentPregnancyType.type, options);
        }
        
        public static CurrentPregnancyType parse(final String xmlAsString) throws XmlException {
            return (CurrentPregnancyType)XmlBeans.getContextTypeLoader().parse(xmlAsString, CurrentPregnancyType.type, (XmlOptions)null);
        }
        
        public static CurrentPregnancyType parse(final String xmlAsString, final XmlOptions options) throws XmlException {
            return (CurrentPregnancyType)XmlBeans.getContextTypeLoader().parse(xmlAsString, CurrentPregnancyType.type, options);
        }
        
        public static CurrentPregnancyType parse(final File file) throws XmlException, IOException {
            return (CurrentPregnancyType)XmlBeans.getContextTypeLoader().parse(file, CurrentPregnancyType.type, (XmlOptions)null);
        }
        
        public static CurrentPregnancyType parse(final File file, final XmlOptions options) throws XmlException, IOException {
            return (CurrentPregnancyType)XmlBeans.getContextTypeLoader().parse(file, CurrentPregnancyType.type, options);
        }
        
        public static CurrentPregnancyType parse(final URL u) throws XmlException, IOException {
            return (CurrentPregnancyType)XmlBeans.getContextTypeLoader().parse(u, CurrentPregnancyType.type, (XmlOptions)null);
        }
        
        public static CurrentPregnancyType parse(final URL u, final XmlOptions options) throws XmlException, IOException {
            return (CurrentPregnancyType)XmlBeans.getContextTypeLoader().parse(u, CurrentPregnancyType.type, options);
        }
        
        public static CurrentPregnancyType parse(final InputStream is) throws XmlException, IOException {
            return (CurrentPregnancyType)XmlBeans.getContextTypeLoader().parse(is, CurrentPregnancyType.type, (XmlOptions)null);
        }
        
        public static CurrentPregnancyType parse(final InputStream is, final XmlOptions options) throws XmlException, IOException {
            return (CurrentPregnancyType)XmlBeans.getContextTypeLoader().parse(is, CurrentPregnancyType.type, options);
        }
        
        public static CurrentPregnancyType parse(final Reader r) throws XmlException, IOException {
            return (CurrentPregnancyType)XmlBeans.getContextTypeLoader().parse(r, CurrentPregnancyType.type, (XmlOptions)null);
        }
        
        public static CurrentPregnancyType parse(final Reader r, final XmlOptions options) throws XmlException, IOException {
            return (CurrentPregnancyType)XmlBeans.getContextTypeLoader().parse(r, CurrentPregnancyType.type, options);
        }
        
        public static CurrentPregnancyType parse(final XMLStreamReader sr) throws XmlException {
            return (CurrentPregnancyType)XmlBeans.getContextTypeLoader().parse(sr, CurrentPregnancyType.type, (XmlOptions)null);
        }
        
        public static CurrentPregnancyType parse(final XMLStreamReader sr, final XmlOptions options) throws XmlException {
            return (CurrentPregnancyType)XmlBeans.getContextTypeLoader().parse(sr, CurrentPregnancyType.type, options);
        }
        
        public static CurrentPregnancyType parse(final Node node) throws XmlException {
            return (CurrentPregnancyType)XmlBeans.getContextTypeLoader().parse(node, CurrentPregnancyType.type, (XmlOptions)null);
        }
        
        public static CurrentPregnancyType parse(final Node node, final XmlOptions options) throws XmlException {
            return (CurrentPregnancyType)XmlBeans.getContextTypeLoader().parse(node, CurrentPregnancyType.type, options);
        }
        
        @Deprecated
        public static CurrentPregnancyType parse(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return (CurrentPregnancyType)XmlBeans.getContextTypeLoader().parse(xis, CurrentPregnancyType.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static CurrentPregnancyType parse(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return (CurrentPregnancyType)XmlBeans.getContextTypeLoader().parse(xis, CurrentPregnancyType.type, options);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, CurrentPregnancyType.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, CurrentPregnancyType.type, options);
        }
        
        private Factory() {
        }
    }
}
