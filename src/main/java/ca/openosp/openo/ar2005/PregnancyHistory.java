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
import org.apache.xmlbeans.XmlInt;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.XmlDate;
import java.util.Calendar;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlObject;

public interface PregnancyHistory extends XmlObject
{
    public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(PregnancyHistory.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9C023B7D67311A3187802DA7FD51EA38").resolveHandle("pregnancyhistory853etype");
    
    Calendar getLMP();
    
    XmlDate xgetLMP();
    
    boolean isNilLMP();
    
    void setLMP(final Calendar p0);
    
    void xsetLMP(final XmlDate p0);
    
    void setNilLMP();
    
    YesNoNullType getLMPCertain();
    
    void setLMPCertain(final YesNoNullType p0);
    
    YesNoNullType addNewLMPCertain();
    
    String getMenCycle();
    
    XmlString xgetMenCycle();
    
    void setMenCycle(final String p0);
    
    void xsetMenCycle(final XmlString p0);
    
    YesNoNullType getMenCycleRegular();
    
    void setMenCycleRegular(final YesNoNullType p0);
    
    YesNoNullType addNewMenCycleRegular();
    
    String getContraceptiveType();
    
    XmlString xgetContraceptiveType();
    
    boolean isSetContraceptiveType();
    
    void setContraceptiveType(final String p0);
    
    void xsetContraceptiveType(final XmlString p0);
    
    void unsetContraceptiveType();
    
    Calendar getContraceptiveLastUsed();
    
    XmlDate xgetContraceptiveLastUsed();
    
    boolean isNilContraceptiveLastUsed();
    
    void setContraceptiveLastUsed(final Calendar p0);
    
    void xsetContraceptiveLastUsed(final XmlDate p0);
    
    void setNilContraceptiveLastUsed();
    
    Calendar getMenstrualEDB();
    
    XmlDate xgetMenstrualEDB();
    
    boolean isNilMenstrualEDB();
    
    void setMenstrualEDB(final Calendar p0);
    
    void xsetMenstrualEDB(final XmlDate p0);
    
    void setNilMenstrualEDB();
    
    Calendar getFinalEDB();
    
    XmlDate xgetFinalEDB();
    
    void setFinalEDB(final Calendar p0);
    
    void xsetFinalEDB(final XmlDate p0);
    
    DatingMethods getDatingMethods();
    
    void setDatingMethods(final DatingMethods p0);
    
    DatingMethods addNewDatingMethods();
    
    int getGravida();
    
    XmlInt xgetGravida();
    
    void setGravida(final int p0);
    
    void xsetGravida(final XmlInt p0);
    
    int getTerm();
    
    XmlInt xgetTerm();
    
    void setTerm(final int p0);
    
    void xsetTerm(final XmlInt p0);
    
    int getPremature();
    
    XmlInt xgetPremature();
    
    void setPremature(final int p0);
    
    void xsetPremature(final XmlInt p0);
    
    int getAbortuses();
    
    XmlInt xgetAbortuses();
    
    void setAbortuses(final int p0);
    
    void xsetAbortuses(final XmlInt p0);
    
    int getLiving();
    
    XmlInt xgetLiving();
    
    void setLiving(final int p0);
    
    void xsetLiving(final XmlInt p0);
    
    public static final class Factory
    {
        public static PregnancyHistory newInstance() {
            return (PregnancyHistory)XmlBeans.getContextTypeLoader().newInstance(PregnancyHistory.type, (XmlOptions)null);
        }
        
        public static PregnancyHistory newInstance(final XmlOptions options) {
            return (PregnancyHistory)XmlBeans.getContextTypeLoader().newInstance(PregnancyHistory.type, options);
        }
        
        public static PregnancyHistory parse(final String xmlAsString) throws XmlException {
            return (PregnancyHistory)XmlBeans.getContextTypeLoader().parse(xmlAsString, PregnancyHistory.type, (XmlOptions)null);
        }
        
        public static PregnancyHistory parse(final String xmlAsString, final XmlOptions options) throws XmlException {
            return (PregnancyHistory)XmlBeans.getContextTypeLoader().parse(xmlAsString, PregnancyHistory.type, options);
        }
        
        public static PregnancyHistory parse(final File file) throws XmlException, IOException {
            return (PregnancyHistory)XmlBeans.getContextTypeLoader().parse(file, PregnancyHistory.type, (XmlOptions)null);
        }
        
        public static PregnancyHistory parse(final File file, final XmlOptions options) throws XmlException, IOException {
            return (PregnancyHistory)XmlBeans.getContextTypeLoader().parse(file, PregnancyHistory.type, options);
        }
        
        public static PregnancyHistory parse(final URL u) throws XmlException, IOException {
            return (PregnancyHistory)XmlBeans.getContextTypeLoader().parse(u, PregnancyHistory.type, (XmlOptions)null);
        }
        
        public static PregnancyHistory parse(final URL u, final XmlOptions options) throws XmlException, IOException {
            return (PregnancyHistory)XmlBeans.getContextTypeLoader().parse(u, PregnancyHistory.type, options);
        }
        
        public static PregnancyHistory parse(final InputStream is) throws XmlException, IOException {
            return (PregnancyHistory)XmlBeans.getContextTypeLoader().parse(is, PregnancyHistory.type, (XmlOptions)null);
        }
        
        public static PregnancyHistory parse(final InputStream is, final XmlOptions options) throws XmlException, IOException {
            return (PregnancyHistory)XmlBeans.getContextTypeLoader().parse(is, PregnancyHistory.type, options);
        }
        
        public static PregnancyHistory parse(final Reader r) throws XmlException, IOException {
            return (PregnancyHistory)XmlBeans.getContextTypeLoader().parse(r, PregnancyHistory.type, (XmlOptions)null);
        }
        
        public static PregnancyHistory parse(final Reader r, final XmlOptions options) throws XmlException, IOException {
            return (PregnancyHistory)XmlBeans.getContextTypeLoader().parse(r, PregnancyHistory.type, options);
        }
        
        public static PregnancyHistory parse(final XMLStreamReader sr) throws XmlException {
            return (PregnancyHistory)XmlBeans.getContextTypeLoader().parse(sr, PregnancyHistory.type, (XmlOptions)null);
        }
        
        public static PregnancyHistory parse(final XMLStreamReader sr, final XmlOptions options) throws XmlException {
            return (PregnancyHistory)XmlBeans.getContextTypeLoader().parse(sr, PregnancyHistory.type, options);
        }
        
        public static PregnancyHistory parse(final Node node) throws XmlException {
            return (PregnancyHistory)XmlBeans.getContextTypeLoader().parse(node, PregnancyHistory.type, (XmlOptions)null);
        }
        
        public static PregnancyHistory parse(final Node node, final XmlOptions options) throws XmlException {
            return (PregnancyHistory)XmlBeans.getContextTypeLoader().parse(node, PregnancyHistory.type, options);
        }
        
        @Deprecated
        public static PregnancyHistory parse(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return (PregnancyHistory)XmlBeans.getContextTypeLoader().parse(xis, PregnancyHistory.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static PregnancyHistory parse(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return (PregnancyHistory)XmlBeans.getContextTypeLoader().parse(xis, PregnancyHistory.type, options);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, PregnancyHistory.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, PregnancyHistory.type, options);
        }
        
        private Factory() {
        }
    }
}
