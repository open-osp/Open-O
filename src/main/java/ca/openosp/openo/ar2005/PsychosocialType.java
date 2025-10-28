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
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlObject;

public interface PsychosocialType extends XmlObject
{
    public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(PsychosocialType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9C023B7D67311A3187802DA7FD51EA38").resolveHandle("psychosocialtype93f2type");
    
    YesNoNullType getPoortSocialSupport();
    
    void setPoortSocialSupport(final YesNoNullType p0);
    
    YesNoNullType addNewPoortSocialSupport();
    
    YesNoNullType getRelationshipProblems();
    
    void setRelationshipProblems(final YesNoNullType p0);
    
    YesNoNullType addNewRelationshipProblems();
    
    YesNoNullType getEmotionalDepression();
    
    void setEmotionalDepression(final YesNoNullType p0);
    
    YesNoNullType addNewEmotionalDepression();
    
    YesNoNullType getSubstanceAbuse();
    
    void setSubstanceAbuse(final YesNoNullType p0);
    
    YesNoNullType addNewSubstanceAbuse();
    
    YesNoNullType getFamilyViolence();
    
    void setFamilyViolence(final YesNoNullType p0);
    
    YesNoNullType addNewFamilyViolence();
    
    YesNoNullType getParentingConcerns();
    
    void setParentingConcerns(final YesNoNullType p0);
    
    YesNoNullType addNewParentingConcerns();
    
    YesNoNullType getReligiousCultural();
    
    void setReligiousCultural(final YesNoNullType p0);
    
    YesNoNullType addNewReligiousCultural();
    
    public static final class Factory
    {
        public static PsychosocialType newInstance() {
            return (PsychosocialType)XmlBeans.getContextTypeLoader().newInstance(PsychosocialType.type, (XmlOptions)null);
        }
        
        public static PsychosocialType newInstance(final XmlOptions options) {
            return (PsychosocialType)XmlBeans.getContextTypeLoader().newInstance(PsychosocialType.type, options);
        }
        
        public static PsychosocialType parse(final String xmlAsString) throws XmlException {
            return (PsychosocialType)XmlBeans.getContextTypeLoader().parse(xmlAsString, PsychosocialType.type, (XmlOptions)null);
        }
        
        public static PsychosocialType parse(final String xmlAsString, final XmlOptions options) throws XmlException {
            return (PsychosocialType)XmlBeans.getContextTypeLoader().parse(xmlAsString, PsychosocialType.type, options);
        }
        
        public static PsychosocialType parse(final File file) throws XmlException, IOException {
            return (PsychosocialType)XmlBeans.getContextTypeLoader().parse(file, PsychosocialType.type, (XmlOptions)null);
        }
        
        public static PsychosocialType parse(final File file, final XmlOptions options) throws XmlException, IOException {
            return (PsychosocialType)XmlBeans.getContextTypeLoader().parse(file, PsychosocialType.type, options);
        }
        
        public static PsychosocialType parse(final URL u) throws XmlException, IOException {
            return (PsychosocialType)XmlBeans.getContextTypeLoader().parse(u, PsychosocialType.type, (XmlOptions)null);
        }
        
        public static PsychosocialType parse(final URL u, final XmlOptions options) throws XmlException, IOException {
            return (PsychosocialType)XmlBeans.getContextTypeLoader().parse(u, PsychosocialType.type, options);
        }
        
        public static PsychosocialType parse(final InputStream is) throws XmlException, IOException {
            return (PsychosocialType)XmlBeans.getContextTypeLoader().parse(is, PsychosocialType.type, (XmlOptions)null);
        }
        
        public static PsychosocialType parse(final InputStream is, final XmlOptions options) throws XmlException, IOException {
            return (PsychosocialType)XmlBeans.getContextTypeLoader().parse(is, PsychosocialType.type, options);
        }
        
        public static PsychosocialType parse(final Reader r) throws XmlException, IOException {
            return (PsychosocialType)XmlBeans.getContextTypeLoader().parse(r, PsychosocialType.type, (XmlOptions)null);
        }
        
        public static PsychosocialType parse(final Reader r, final XmlOptions options) throws XmlException, IOException {
            return (PsychosocialType)XmlBeans.getContextTypeLoader().parse(r, PsychosocialType.type, options);
        }
        
        public static PsychosocialType parse(final XMLStreamReader sr) throws XmlException {
            return (PsychosocialType)XmlBeans.getContextTypeLoader().parse(sr, PsychosocialType.type, (XmlOptions)null);
        }
        
        public static PsychosocialType parse(final XMLStreamReader sr, final XmlOptions options) throws XmlException {
            return (PsychosocialType)XmlBeans.getContextTypeLoader().parse(sr, PsychosocialType.type, options);
        }
        
        public static PsychosocialType parse(final Node node) throws XmlException {
            return (PsychosocialType)XmlBeans.getContextTypeLoader().parse(node, PsychosocialType.type, (XmlOptions)null);
        }
        
        public static PsychosocialType parse(final Node node, final XmlOptions options) throws XmlException {
            return (PsychosocialType)XmlBeans.getContextTypeLoader().parse(node, PsychosocialType.type, options);
        }
        
        @Deprecated
        public static PsychosocialType parse(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return (PsychosocialType)XmlBeans.getContextTypeLoader().parse(xis, PsychosocialType.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static PsychosocialType parse(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return (PsychosocialType)XmlBeans.getContextTypeLoader().parse(xis, PsychosocialType.type, options);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, PsychosocialType.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, PsychosocialType.type, options);
        }
        
        private Factory() {
        }
    }
}
