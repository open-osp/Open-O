package ca.openosp.openo.ar2005.impl;

import ca.openosp.openo.ar2005.SignatureType;
import ca.openosp.openo.ar2005.DiscussionTopicsType;
import ca.openosp.openo.ar2005.AdditionalLabInvestigationsType;
import ca.openosp.openo.ar2005.UltrasoundType;
import ca.openosp.openo.ar2005.SubsequentVisitItemType;
import ca.openosp.openo.ar2005.RecommendedImmunoprophylaxisType;
import org.apache.xmlbeans.XmlObject;
import java.util.List;
import java.util.ArrayList;
import ca.openosp.openo.ar2005.RiskFactorItemType;
import org.apache.xmlbeans.SchemaType;
import javax.xml.namespace.QName;
import ca.openosp.openo.ar2005.AR2;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

public class AR2Impl extends XmlComplexContentImpl implements AR2
{
    private static final long serialVersionUID = 1L;
    private static final QName RISKFACTORLIST$0;
    private static final QName RECOMMENDEDIMMUNOPROPHYLAXIS$2;
    private static final QName SUBSEQUENTVISITLIST$4;
    private static final QName ULTRASOUND$6;
    private static final QName ADDITIONALLABINVESTIGATIONS$8;
    private static final QName DISCUSSIONTOPICS$10;
    private static final QName SIGNATURES$12;
    
    public AR2Impl(final SchemaType sType) {
        super(sType);
    }
    
    public RiskFactorItemType[] getRiskFactorListArray() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            final List targetList = new ArrayList();
            this.get_store().find_all_element_users(AR2Impl.RISKFACTORLIST$0, targetList);
            final RiskFactorItemType[] result = new RiskFactorItemType[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    public RiskFactorItemType getRiskFactorListArray(final int i) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            RiskFactorItemType target = null;
            target = (RiskFactorItemType)this.get_store().find_element_user(AR2Impl.RISKFACTORLIST$0, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    public int sizeOfRiskFactorListArray() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            return this.get_store().count_elements(AR2Impl.RISKFACTORLIST$0);
        }
    }
    
    public void setRiskFactorListArray(final RiskFactorItemType[] riskFactorListArray) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            this.arraySetterHelper((XmlObject[])riskFactorListArray, AR2Impl.RISKFACTORLIST$0);
        }
    }
    
    public void setRiskFactorListArray(final int i, final RiskFactorItemType riskFactorList) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            RiskFactorItemType target = null;
            target = (RiskFactorItemType)this.get_store().find_element_user(AR2Impl.RISKFACTORLIST$0, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set((XmlObject)riskFactorList);
        }
    }
    
    public RiskFactorItemType insertNewRiskFactorList(final int i) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            RiskFactorItemType target = null;
            target = (RiskFactorItemType)this.get_store().insert_element_user(AR2Impl.RISKFACTORLIST$0, i);
            return target;
        }
    }
    
    public RiskFactorItemType addNewRiskFactorList() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            RiskFactorItemType target = null;
            target = (RiskFactorItemType)this.get_store().add_element_user(AR2Impl.RISKFACTORLIST$0);
            return target;
        }
    }
    
    public void removeRiskFactorList(final int i) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            this.get_store().remove_element(AR2Impl.RISKFACTORLIST$0, i);
        }
    }
    
    public RecommendedImmunoprophylaxisType getRecommendedImmunoprophylaxis() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            RecommendedImmunoprophylaxisType target = null;
            target = (RecommendedImmunoprophylaxisType)this.get_store().find_element_user(AR2Impl.RECOMMENDEDIMMUNOPROPHYLAXIS$2, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setRecommendedImmunoprophylaxis(final RecommendedImmunoprophylaxisType recommendedImmunoprophylaxis) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            RecommendedImmunoprophylaxisType target = null;
            target = (RecommendedImmunoprophylaxisType)this.get_store().find_element_user(AR2Impl.RECOMMENDEDIMMUNOPROPHYLAXIS$2, 0);
            if (target == null) {
                target = (RecommendedImmunoprophylaxisType)this.get_store().add_element_user(AR2Impl.RECOMMENDEDIMMUNOPROPHYLAXIS$2);
            }
            target.set((XmlObject)recommendedImmunoprophylaxis);
        }
    }
    
    public RecommendedImmunoprophylaxisType addNewRecommendedImmunoprophylaxis() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            RecommendedImmunoprophylaxisType target = null;
            target = (RecommendedImmunoprophylaxisType)this.get_store().add_element_user(AR2Impl.RECOMMENDEDIMMUNOPROPHYLAXIS$2);
            return target;
        }
    }
    
    public SubsequentVisitItemType[] getSubsequentVisitListArray() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            final List targetList = new ArrayList();
            this.get_store().find_all_element_users(AR2Impl.SUBSEQUENTVISITLIST$4, targetList);
            final SubsequentVisitItemType[] result = new SubsequentVisitItemType[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    public SubsequentVisitItemType getSubsequentVisitListArray(final int i) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SubsequentVisitItemType target = null;
            target = (SubsequentVisitItemType)this.get_store().find_element_user(AR2Impl.SUBSEQUENTVISITLIST$4, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    public int sizeOfSubsequentVisitListArray() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            return this.get_store().count_elements(AR2Impl.SUBSEQUENTVISITLIST$4);
        }
    }
    
    public void setSubsequentVisitListArray(final SubsequentVisitItemType[] subsequentVisitListArray) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            this.arraySetterHelper((XmlObject[])subsequentVisitListArray, AR2Impl.SUBSEQUENTVISITLIST$4);
        }
    }
    
    public void setSubsequentVisitListArray(final int i, final SubsequentVisitItemType subsequentVisitList) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SubsequentVisitItemType target = null;
            target = (SubsequentVisitItemType)this.get_store().find_element_user(AR2Impl.SUBSEQUENTVISITLIST$4, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set((XmlObject)subsequentVisitList);
        }
    }
    
    public SubsequentVisitItemType insertNewSubsequentVisitList(final int i) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SubsequentVisitItemType target = null;
            target = (SubsequentVisitItemType)this.get_store().insert_element_user(AR2Impl.SUBSEQUENTVISITLIST$4, i);
            return target;
        }
    }
    
    public SubsequentVisitItemType addNewSubsequentVisitList() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SubsequentVisitItemType target = null;
            target = (SubsequentVisitItemType)this.get_store().add_element_user(AR2Impl.SUBSEQUENTVISITLIST$4);
            return target;
        }
    }
    
    public void removeSubsequentVisitList(final int i) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            this.get_store().remove_element(AR2Impl.SUBSEQUENTVISITLIST$4, i);
        }
    }
    
    public UltrasoundType[] getUltrasoundArray() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            final List targetList = new ArrayList();
            this.get_store().find_all_element_users(AR2Impl.ULTRASOUND$6, targetList);
            final UltrasoundType[] result = new UltrasoundType[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    public UltrasoundType getUltrasoundArray(final int i) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            UltrasoundType target = null;
            target = (UltrasoundType)this.get_store().find_element_user(AR2Impl.ULTRASOUND$6, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    public int sizeOfUltrasoundArray() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            return this.get_store().count_elements(AR2Impl.ULTRASOUND$6);
        }
    }
    
    public void setUltrasoundArray(final UltrasoundType[] ultrasoundArray) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            this.arraySetterHelper((XmlObject[])ultrasoundArray, AR2Impl.ULTRASOUND$6);
        }
    }
    
    public void setUltrasoundArray(final int i, final UltrasoundType ultrasound) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            UltrasoundType target = null;
            target = (UltrasoundType)this.get_store().find_element_user(AR2Impl.ULTRASOUND$6, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set((XmlObject)ultrasound);
        }
    }
    
    public UltrasoundType insertNewUltrasound(final int i) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            UltrasoundType target = null;
            target = (UltrasoundType)this.get_store().insert_element_user(AR2Impl.ULTRASOUND$6, i);
            return target;
        }
    }
    
    public UltrasoundType addNewUltrasound() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            UltrasoundType target = null;
            target = (UltrasoundType)this.get_store().add_element_user(AR2Impl.ULTRASOUND$6);
            return target;
        }
    }
    
    public void removeUltrasound(final int i) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            this.get_store().remove_element(AR2Impl.ULTRASOUND$6, i);
        }
    }
    
    public AdditionalLabInvestigationsType getAdditionalLabInvestigations() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            AdditionalLabInvestigationsType target = null;
            target = (AdditionalLabInvestigationsType)this.get_store().find_element_user(AR2Impl.ADDITIONALLABINVESTIGATIONS$8, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setAdditionalLabInvestigations(final AdditionalLabInvestigationsType additionalLabInvestigations) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            AdditionalLabInvestigationsType target = null;
            target = (AdditionalLabInvestigationsType)this.get_store().find_element_user(AR2Impl.ADDITIONALLABINVESTIGATIONS$8, 0);
            if (target == null) {
                target = (AdditionalLabInvestigationsType)this.get_store().add_element_user(AR2Impl.ADDITIONALLABINVESTIGATIONS$8);
            }
            target.set((XmlObject)additionalLabInvestigations);
        }
    }
    
    public AdditionalLabInvestigationsType addNewAdditionalLabInvestigations() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            AdditionalLabInvestigationsType target = null;
            target = (AdditionalLabInvestigationsType)this.get_store().add_element_user(AR2Impl.ADDITIONALLABINVESTIGATIONS$8);
            return target;
        }
    }
    
    public DiscussionTopicsType getDiscussionTopics() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            DiscussionTopicsType target = null;
            target = (DiscussionTopicsType)this.get_store().find_element_user(AR2Impl.DISCUSSIONTOPICS$10, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setDiscussionTopics(final DiscussionTopicsType discussionTopics) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            DiscussionTopicsType target = null;
            target = (DiscussionTopicsType)this.get_store().find_element_user(AR2Impl.DISCUSSIONTOPICS$10, 0);
            if (target == null) {
                target = (DiscussionTopicsType)this.get_store().add_element_user(AR2Impl.DISCUSSIONTOPICS$10);
            }
            target.set((XmlObject)discussionTopics);
        }
    }
    
    public DiscussionTopicsType addNewDiscussionTopics() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            DiscussionTopicsType target = null;
            target = (DiscussionTopicsType)this.get_store().add_element_user(AR2Impl.DISCUSSIONTOPICS$10);
            return target;
        }
    }
    
    public SignatureType getSignatures() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SignatureType target = null;
            target = (SignatureType)this.get_store().find_element_user(AR2Impl.SIGNATURES$12, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setSignatures(final SignatureType signatures) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SignatureType target = null;
            target = (SignatureType)this.get_store().find_element_user(AR2Impl.SIGNATURES$12, 0);
            if (target == null) {
                target = (SignatureType)this.get_store().add_element_user(AR2Impl.SIGNATURES$12);
            }
            target.set((XmlObject)signatures);
        }
    }
    
    public SignatureType addNewSignatures() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SignatureType target = null;
            target = (SignatureType)this.get_store().add_element_user(AR2Impl.SIGNATURES$12);
            return target;
        }
    }
    
    static {
        RISKFACTORLIST$0 = new QName("http://www.oscarmcmaster.org/AR2005", "riskFactorList");
        RECOMMENDEDIMMUNOPROPHYLAXIS$2 = new QName("http://www.oscarmcmaster.org/AR2005", "recommendedImmunoprophylaxis");
        SUBSEQUENTVISITLIST$4 = new QName("http://www.oscarmcmaster.org/AR2005", "subsequentVisitList");
        ULTRASOUND$6 = new QName("http://www.oscarmcmaster.org/AR2005", "ultrasound");
        ADDITIONALLABINVESTIGATIONS$8 = new QName("http://www.oscarmcmaster.org/AR2005", "additionalLabInvestigations");
        DISCUSSIONTOPICS$10 = new QName("http://www.oscarmcmaster.org/AR2005", "discussionTopics");
        SIGNATURES$12 = new QName("http://www.oscarmcmaster.org/AR2005", "signatures");
    }
}
