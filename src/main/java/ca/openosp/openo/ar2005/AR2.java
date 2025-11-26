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

public interface AR2 extends XmlObject
{
    public static final SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(AR2.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s9C023B7D67311A3187802DA7FD51EA38").resolveHandle("ar23326type");
    
    RiskFactorItemType[] getRiskFactorListArray();
    
    RiskFactorItemType getRiskFactorListArray(final int p0);
    
    int sizeOfRiskFactorListArray();
    
    void setRiskFactorListArray(final RiskFactorItemType[] p0);
    
    void setRiskFactorListArray(final int p0, final RiskFactorItemType p1);
    
    RiskFactorItemType insertNewRiskFactorList(final int p0);
    
    RiskFactorItemType addNewRiskFactorList();
    
    void removeRiskFactorList(final int p0);
    
    RecommendedImmunoprophylaxisType getRecommendedImmunoprophylaxis();
    
    void setRecommendedImmunoprophylaxis(final RecommendedImmunoprophylaxisType p0);
    
    RecommendedImmunoprophylaxisType addNewRecommendedImmunoprophylaxis();
    
    SubsequentVisitItemType[] getSubsequentVisitListArray();
    
    SubsequentVisitItemType getSubsequentVisitListArray(final int p0);
    
    int sizeOfSubsequentVisitListArray();
    
    void setSubsequentVisitListArray(final SubsequentVisitItemType[] p0);
    
    void setSubsequentVisitListArray(final int p0, final SubsequentVisitItemType p1);
    
    SubsequentVisitItemType insertNewSubsequentVisitList(final int p0);
    
    SubsequentVisitItemType addNewSubsequentVisitList();
    
    void removeSubsequentVisitList(final int p0);
    
    UltrasoundType[] getUltrasoundArray();
    
    UltrasoundType getUltrasoundArray(final int p0);
    
    int sizeOfUltrasoundArray();
    
    void setUltrasoundArray(final UltrasoundType[] p0);
    
    void setUltrasoundArray(final int p0, final UltrasoundType p1);
    
    UltrasoundType insertNewUltrasound(final int p0);
    
    UltrasoundType addNewUltrasound();
    
    void removeUltrasound(final int p0);
    
    AdditionalLabInvestigationsType getAdditionalLabInvestigations();
    
    void setAdditionalLabInvestigations(final AdditionalLabInvestigationsType p0);
    
    AdditionalLabInvestigationsType addNewAdditionalLabInvestigations();
    
    DiscussionTopicsType getDiscussionTopics();
    
    void setDiscussionTopics(final DiscussionTopicsType p0);
    
    DiscussionTopicsType addNewDiscussionTopics();
    
    SignatureType getSignatures();
    
    void setSignatures(final SignatureType p0);
    
    SignatureType addNewSignatures();
    
    public static final class Factory
    {
        public static AR2 newInstance() {
            return (AR2)XmlBeans.getContextTypeLoader().newInstance(AR2.type, (XmlOptions)null);
        }
        
        public static AR2 newInstance(final XmlOptions options) {
            return (AR2)XmlBeans.getContextTypeLoader().newInstance(AR2.type, options);
        }
        
        public static AR2 parse(final String xmlAsString) throws XmlException {
            return (AR2)XmlBeans.getContextTypeLoader().parse(xmlAsString, AR2.type, (XmlOptions)null);
        }
        
        public static AR2 parse(final String xmlAsString, final XmlOptions options) throws XmlException {
            return (AR2)XmlBeans.getContextTypeLoader().parse(xmlAsString, AR2.type, options);
        }
        
        public static AR2 parse(final File file) throws XmlException, IOException {
            return (AR2)XmlBeans.getContextTypeLoader().parse(file, AR2.type, (XmlOptions)null);
        }
        
        public static AR2 parse(final File file, final XmlOptions options) throws XmlException, IOException {
            return (AR2)XmlBeans.getContextTypeLoader().parse(file, AR2.type, options);
        }
        
        public static AR2 parse(final URL u) throws XmlException, IOException {
            return (AR2)XmlBeans.getContextTypeLoader().parse(u, AR2.type, (XmlOptions)null);
        }
        
        public static AR2 parse(final URL u, final XmlOptions options) throws XmlException, IOException {
            return (AR2)XmlBeans.getContextTypeLoader().parse(u, AR2.type, options);
        }
        
        public static AR2 parse(final InputStream is) throws XmlException, IOException {
            return (AR2)XmlBeans.getContextTypeLoader().parse(is, AR2.type, (XmlOptions)null);
        }
        
        public static AR2 parse(final InputStream is, final XmlOptions options) throws XmlException, IOException {
            return (AR2)XmlBeans.getContextTypeLoader().parse(is, AR2.type, options);
        }
        
        public static AR2 parse(final Reader r) throws XmlException, IOException {
            return (AR2)XmlBeans.getContextTypeLoader().parse(r, AR2.type, (XmlOptions)null);
        }
        
        public static AR2 parse(final Reader r, final XmlOptions options) throws XmlException, IOException {
            return (AR2)XmlBeans.getContextTypeLoader().parse(r, AR2.type, options);
        }
        
        public static AR2 parse(final XMLStreamReader sr) throws XmlException {
            return (AR2)XmlBeans.getContextTypeLoader().parse(sr, AR2.type, (XmlOptions)null);
        }
        
        public static AR2 parse(final XMLStreamReader sr, final XmlOptions options) throws XmlException {
            return (AR2)XmlBeans.getContextTypeLoader().parse(sr, AR2.type, options);
        }
        
        public static AR2 parse(final Node node) throws XmlException {
            return (AR2)XmlBeans.getContextTypeLoader().parse(node, AR2.type, (XmlOptions)null);
        }
        
        public static AR2 parse(final Node node, final XmlOptions options) throws XmlException {
            return (AR2)XmlBeans.getContextTypeLoader().parse(node, AR2.type, options);
        }
        
        @Deprecated
        public static AR2 parse(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return (AR2)XmlBeans.getContextTypeLoader().parse(xis, AR2.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static AR2 parse(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return (AR2)XmlBeans.getContextTypeLoader().parse(xis, AR2.type, options);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, AR2.type, (XmlOptions)null);
        }
        
        @Deprecated
        public static XMLInputStream newValidatingXMLInputStream(final XMLInputStream xis, final XmlOptions options) throws XmlException, XMLStreamException {
            return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(xis, AR2.type, options);
        }
        
        private Factory() {
        }
    }
}
