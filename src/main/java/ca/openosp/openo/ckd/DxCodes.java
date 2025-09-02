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
import org.apache.xmlbeans.StringEnumAbstractBase;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlObject;

public interface DxCodes extends XmlObject
{
    public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(DxCodes.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9F21579FEC5BB34270776B6AF571836F").resolveHandle("dxcodes4b79type");
    
    Code[] getCodeArray();
    
    Code getCodeArray(final int p0);
    
    int sizeOfCodeArray();
    
    void setCodeArray(final Code[] p0);
    
    void setCodeArray(final int p0, final Code p1);
    
    Code insertNewCode(final int p0);
    
    Code addNewCode();
    
    void removeCode(final int p0);
    
    public interface Code extends XmlString
    {
        public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(Code.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9F21579FEC5BB34270776B6AF571836F").resolveHandle("codec550elemtype");
        
        Type.Enum getType();
        
        Type xgetType();
        
        boolean isSetType();
        
        void setType(final Type.Enum p0);
        
        void xsetType(final Type p0);
        
        void unsetType();
        
        String getName();
        
        XmlString xgetName();
        
        boolean isSetName();
        
        void setName(final String p0);
        
        void xsetName(final XmlString p0);
        
        void unsetName();
        
        public interface Type extends XmlString
        {
            public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(Type.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9F21579FEC5BB34270776B6AF571836F").resolveHandle("type242aattrtype");
            public static final Enum ICD_9 = Enum.forString("icd9");
            public static final int INT_ICD_9 = 1;
            
            StringEnumAbstractBase enumValue();
            
            void set(final StringEnumAbstractBase p0);
            
            public static final class Enum extends StringEnumAbstractBase
            {
                static final int INT_ICD_9 = 1;
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
                    table = new StringEnumAbstractBase.Table((StringEnumAbstractBase[])new Enum[] { new Enum("icd9", 1) });
                }
            }
            
            public static final class Factory
            {
                public static Type newValue(final Object obj) {
                    return (Type)Type.type.newValue(obj);
                }
                
                public static Type newInstance() {
                    return (Type)XmlBeans.getContextTypeLoader().newInstance(Type.type, (XmlOptions)null);
                }
                
                public static Type newInstance(final XmlOptions options) {
                    return (Type)XmlBeans.getContextTypeLoader().newInstance(Type.type, options);
                }
                
                private Factory() {
                }
            }
        }
        
        public static final class Factory
        {
            public static Code newInstance() {
                return (Code)XmlBeans.getContextTypeLoader().newInstance(Code.type, (XmlOptions)null);
            }
            
            public static Code newInstance(final XmlOptions options) {
                return (Code)XmlBeans.getContextTypeLoader().newInstance(Code.type, options);
            }
            
            private Factory() {
            }
        }
    }
    
    public static final class Factory
    {
        public static DxCodes newInstance() {
            return (DxCodes)XmlBeans.getContextTypeLoader().newInstance(DxCodes.type, (XmlOptions)null);
        }
        
        public static DxCodes newInstance(final XmlOptions options) {
            return (DxCodes)XmlBeans.getContextTypeLoader().newInstance(DxCodes.type, options);
        }
        
        public static DxCodes parse(final String xmlAsString) throws XmlException {
            return (DxCodes)XmlBeans.getContextTypeLoader().parse(xmlAsString, DxCodes.type, (XmlOptions)null);
        }
        
        public static DxCodes parse(final String xmlAsString, final XmlOptions options) throws XmlException {
            return (DxCodes)XmlBeans.getContextTypeLoader().parse(xmlAsString, DxCodes.type, options);
        }
        
        public static DxCodes parse(final File file) throws XmlException, IOException {
            return (DxCodes)XmlBeans.getContextTypeLoader().parse(file, DxCodes.type, (XmlOptions)null);
        }
        
        public static DxCodes parse(final File file, final XmlOptions options) throws XmlException, IOException {
            return (DxCodes)XmlBeans.getContextTypeLoader().parse(file, DxCodes.type, options);
        }
        
        public static DxCodes parse(final URL u) throws XmlException, IOException {
            return (DxCodes)XmlBeans.getContextTypeLoader().parse(u, DxCodes.type, (XmlOptions)null);
        }
        
        public static DxCodes parse(final URL u, final XmlOptions options) throws XmlException, IOException {
            return (DxCodes)XmlBeans.getContextTypeLoader().parse(u, DxCodes.type, options);
        }
        
        public static DxCodes parse(final InputStream is) throws XmlException, IOException {
            return (DxCodes)XmlBeans.getContextTypeLoader().parse(is, DxCodes.type, (XmlOptions)null);
        }
        
        public static DxCodes parse(final InputStream is, final XmlOptions options) throws XmlException, IOException {
            return (DxCodes)XmlBeans.getContextTypeLoader().parse(is, DxCodes.type, options);
        }
        
        public static DxCodes parse(final Reader r) throws XmlException, IOException {
            return (DxCodes)XmlBeans.getContextTypeLoader().parse(r, DxCodes.type, (XmlOptions)null);
        }
        
        public static DxCodes parse(final Reader r, final XmlOptions options) throws XmlException, IOException {
            return (DxCodes)XmlBeans.getContextTypeLoader().parse(r, DxCodes.type, options);
        }
        
        public static DxCodes parse(final XMLStreamReader sr) throws XmlException {
            return (DxCodes)XmlBeans.getContextTypeLoader().parse(sr, DxCodes.type, (XmlOptions)null);
        }
        
        public static DxCodes parse(final XMLStreamReader sr, final XmlOptions options) throws XmlException {
            return (DxCodes)XmlBeans.getContextTypeLoader().parse(sr, DxCodes.type, options);
        }
        
        public static DxCodes parse(final Node node) throws XmlException {
            return (DxCodes)XmlBeans.getContextTypeLoader().parse(node, DxCodes.type, (XmlOptions)null);
        }
        
        public static DxCodes parse(final Node node, final XmlOptions options) throws XmlException {
            return (DxCodes)XmlBeans.getContextTypeLoader().parse(node, DxCodes.type, options);
        }
        
        @Deprecated
        public static DxCodes parse(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return (DxCodes)XmlBeans.getContextTypeLoader().parse(xis, DxCodes.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static DxCodes parse(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return (DxCodes)XmlBeans.getContextTypeLoader().parse(xis, DxCodes.type, options);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, DxCodes.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, DxCodes.type, options);
        }
        
        private Factory() {
        }
    }
}
