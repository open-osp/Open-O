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
import org.apache.xmlbeans.StringEnumAbstractBase;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlString;

public interface EthnicValueType extends XmlString
{
    public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(EthnicValueType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9C023B7D67311A3187802DA7FD51EA38").resolveHandle("ethnicvaluetype7021type");
    public static final Enum ANC_001 = Enum.forString("ANC001");
    public static final Enum ANC_002 = Enum.forString("ANC002");
    public static final Enum ANC_005 = Enum.forString("ANC005");
    public static final Enum ANC_007 = Enum.forString("ANC007");
    public static final Enum OTHER = Enum.forString("OTHER");
    public static final Enum UN = Enum.forString("UN");
    public static final int INT_ANC_001 = 1;
    public static final int INT_ANC_002 = 2;
    public static final int INT_ANC_005 = 3;
    public static final int INT_ANC_007 = 4;
    public static final int INT_OTHER = 5;
    public static final int INT_UN = 6;
    
    StringEnumAbstractBase enumValue();
    
    void set(final StringEnumAbstractBase p0);
    
    public static final class Enum extends StringEnumAbstractBase
    {
        static final int INT_ANC_001 = 1;
        static final int INT_ANC_002 = 2;
        static final int INT_ANC_005 = 3;
        static final int INT_ANC_007 = 4;
        static final int INT_OTHER = 5;
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
            table = new StringEnumAbstractBase.Table((StringEnumAbstractBase[])new Enum[] { new Enum("ANC001", 1), new Enum("ANC002", 2), new Enum("ANC005", 3), new Enum("ANC007", 4), new Enum("OTHER", 5), new Enum("UN", 6) });
        }
    }
    
    public static final class Factory
    {
        public static EthnicValueType newValue(final Object obj) {
            return (EthnicValueType)EthnicValueType.type.newValue(obj);
        }
        
        public static EthnicValueType newInstance() {
            return (EthnicValueType)XmlBeans.getContextTypeLoader().newInstance(EthnicValueType.type, (XmlOptions)null);
        }
        
        public static EthnicValueType newInstance(final XmlOptions options) {
            return (EthnicValueType)XmlBeans.getContextTypeLoader().newInstance(EthnicValueType.type, options);
        }
        
        public static EthnicValueType parse(final String xmlAsString) throws XmlException {
            return (EthnicValueType)XmlBeans.getContextTypeLoader().parse(xmlAsString, EthnicValueType.type, (XmlOptions)null);
        }
        
        public static EthnicValueType parse(final String xmlAsString, final XmlOptions options) throws XmlException {
            return (EthnicValueType)XmlBeans.getContextTypeLoader().parse(xmlAsString, EthnicValueType.type, options);
        }
        
        public static EthnicValueType parse(final File file) throws XmlException, IOException {
            return (EthnicValueType)XmlBeans.getContextTypeLoader().parse(file, EthnicValueType.type, (XmlOptions)null);
        }
        
        public static EthnicValueType parse(final File file, final XmlOptions options) throws XmlException, IOException {
            return (EthnicValueType)XmlBeans.getContextTypeLoader().parse(file, EthnicValueType.type, options);
        }
        
        public static EthnicValueType parse(final URL u) throws XmlException, IOException {
            return (EthnicValueType)XmlBeans.getContextTypeLoader().parse(u, EthnicValueType.type, (XmlOptions)null);
        }
        
        public static EthnicValueType parse(final URL u, final XmlOptions options) throws XmlException, IOException {
            return (EthnicValueType)XmlBeans.getContextTypeLoader().parse(u, EthnicValueType.type, options);
        }
        
        public static EthnicValueType parse(final InputStream is) throws XmlException, IOException {
            return (EthnicValueType)XmlBeans.getContextTypeLoader().parse(is, EthnicValueType.type, (XmlOptions)null);
        }
        
        public static EthnicValueType parse(final InputStream is, final XmlOptions options) throws XmlException, IOException {
            return (EthnicValueType)XmlBeans.getContextTypeLoader().parse(is, EthnicValueType.type, options);
        }
        
        public static EthnicValueType parse(final Reader r) throws XmlException, IOException {
            return (EthnicValueType)XmlBeans.getContextTypeLoader().parse(r, EthnicValueType.type, (XmlOptions)null);
        }
        
        public static EthnicValueType parse(final Reader r, final XmlOptions options) throws XmlException, IOException {
            return (EthnicValueType)XmlBeans.getContextTypeLoader().parse(r, EthnicValueType.type, options);
        }
        
        public static EthnicValueType parse(final XMLStreamReader sr) throws XmlException {
            return (EthnicValueType)XmlBeans.getContextTypeLoader().parse(sr, EthnicValueType.type, (XmlOptions)null);
        }
        
        public static EthnicValueType parse(final XMLStreamReader sr, final XmlOptions options) throws XmlException {
            return (EthnicValueType)XmlBeans.getContextTypeLoader().parse(sr, EthnicValueType.type, options);
        }
        
        public static EthnicValueType parse(final Node node) throws XmlException {
            return (EthnicValueType)XmlBeans.getContextTypeLoader().parse(node, EthnicValueType.type, (XmlOptions)null);
        }
        
        public static EthnicValueType parse(final Node node, final XmlOptions options) throws XmlException {
            return (EthnicValueType)XmlBeans.getContextTypeLoader().parse(node, EthnicValueType.type, options);
        }
        
        @Deprecated
        public static EthnicValueType parse(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return (EthnicValueType)XmlBeans.getContextTypeLoader().parse(xis, EthnicValueType.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static EthnicValueType parse(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return (EthnicValueType)XmlBeans.getContextTypeLoader().parse(xis, EthnicValueType.type, options);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, EthnicValueType.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, EthnicValueType.type, options);
        }
        
        private Factory() {
        }
    }
}
