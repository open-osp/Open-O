package ca.openosp.openo.ar2005.impl;

import org.apache.xmlbeans.XmlObject;
import ca.openosp.openo.ar2005.YesNoNullType;
import org.apache.xmlbeans.SchemaType;
import javax.xml.namespace.QName;
import ca.openosp.openo.ar2005.PsychosocialType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

public class PsychosocialTypeImpl extends XmlComplexContentImpl implements PsychosocialType
{
    private static final long serialVersionUID = 1L;
    private static final QName POORTSOCIALSUPPORT$0;
    private static final QName RELATIONSHIPPROBLEMS$2;
    private static final QName EMOTIONALDEPRESSION$4;
    private static final QName SUBSTANCEABUSE$6;
    private static final QName FAMILYVIOLENCE$8;
    private static final QName PARENTINGCONCERNS$10;
    private static final QName RELIGIOUSCULTURAL$12;
    
    public PsychosocialTypeImpl(final SchemaType sType) {
        super(sType);
    }
    
    public YesNoNullType getPoortSocialSupport() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(PsychosocialTypeImpl.POORTSOCIALSUPPORT$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setPoortSocialSupport(final YesNoNullType poortSocialSupport) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(PsychosocialTypeImpl.POORTSOCIALSUPPORT$0, 0);
            if (target == null) {
                target = (YesNoNullType)this.get_store().add_element_user(PsychosocialTypeImpl.POORTSOCIALSUPPORT$0);
            }
            target.set((XmlObject)poortSocialSupport);
        }
    }
    
    public YesNoNullType addNewPoortSocialSupport() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().add_element_user(PsychosocialTypeImpl.POORTSOCIALSUPPORT$0);
            return target;
        }
    }
    
    public YesNoNullType getRelationshipProblems() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(PsychosocialTypeImpl.RELATIONSHIPPROBLEMS$2, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setRelationshipProblems(final YesNoNullType relationshipProblems) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(PsychosocialTypeImpl.RELATIONSHIPPROBLEMS$2, 0);
            if (target == null) {
                target = (YesNoNullType)this.get_store().add_element_user(PsychosocialTypeImpl.RELATIONSHIPPROBLEMS$2);
            }
            target.set((XmlObject)relationshipProblems);
        }
    }
    
    public YesNoNullType addNewRelationshipProblems() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().add_element_user(PsychosocialTypeImpl.RELATIONSHIPPROBLEMS$2);
            return target;
        }
    }
    
    public YesNoNullType getEmotionalDepression() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(PsychosocialTypeImpl.EMOTIONALDEPRESSION$4, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setEmotionalDepression(final YesNoNullType emotionalDepression) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(PsychosocialTypeImpl.EMOTIONALDEPRESSION$4, 0);
            if (target == null) {
                target = (YesNoNullType)this.get_store().add_element_user(PsychosocialTypeImpl.EMOTIONALDEPRESSION$4);
            }
            target.set((XmlObject)emotionalDepression);
        }
    }
    
    public YesNoNullType addNewEmotionalDepression() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().add_element_user(PsychosocialTypeImpl.EMOTIONALDEPRESSION$4);
            return target;
        }
    }
    
    public YesNoNullType getSubstanceAbuse() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(PsychosocialTypeImpl.SUBSTANCEABUSE$6, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setSubstanceAbuse(final YesNoNullType substanceAbuse) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(PsychosocialTypeImpl.SUBSTANCEABUSE$6, 0);
            if (target == null) {
                target = (YesNoNullType)this.get_store().add_element_user(PsychosocialTypeImpl.SUBSTANCEABUSE$6);
            }
            target.set((XmlObject)substanceAbuse);
        }
    }
    
    public YesNoNullType addNewSubstanceAbuse() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().add_element_user(PsychosocialTypeImpl.SUBSTANCEABUSE$6);
            return target;
        }
    }
    
    public YesNoNullType getFamilyViolence() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(PsychosocialTypeImpl.FAMILYVIOLENCE$8, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setFamilyViolence(final YesNoNullType familyViolence) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(PsychosocialTypeImpl.FAMILYVIOLENCE$8, 0);
            if (target == null) {
                target = (YesNoNullType)this.get_store().add_element_user(PsychosocialTypeImpl.FAMILYVIOLENCE$8);
            }
            target.set((XmlObject)familyViolence);
        }
    }
    
    public YesNoNullType addNewFamilyViolence() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().add_element_user(PsychosocialTypeImpl.FAMILYVIOLENCE$8);
            return target;
        }
    }
    
    public YesNoNullType getParentingConcerns() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(PsychosocialTypeImpl.PARENTINGCONCERNS$10, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setParentingConcerns(final YesNoNullType parentingConcerns) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(PsychosocialTypeImpl.PARENTINGCONCERNS$10, 0);
            if (target == null) {
                target = (YesNoNullType)this.get_store().add_element_user(PsychosocialTypeImpl.PARENTINGCONCERNS$10);
            }
            target.set((XmlObject)parentingConcerns);
        }
    }
    
    public YesNoNullType addNewParentingConcerns() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().add_element_user(PsychosocialTypeImpl.PARENTINGCONCERNS$10);
            return target;
        }
    }
    
    public YesNoNullType getReligiousCultural() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(PsychosocialTypeImpl.RELIGIOUSCULTURAL$12, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setReligiousCultural(final YesNoNullType religiousCultural) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(PsychosocialTypeImpl.RELIGIOUSCULTURAL$12, 0);
            if (target == null) {
                target = (YesNoNullType)this.get_store().add_element_user(PsychosocialTypeImpl.RELIGIOUSCULTURAL$12);
            }
            target.set((XmlObject)religiousCultural);
        }
    }
    
    public YesNoNullType addNewReligiousCultural() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().add_element_user(PsychosocialTypeImpl.RELIGIOUSCULTURAL$12);
            return target;
        }
    }
    
    static {
        POORTSOCIALSUPPORT$0 = new QName("http://www.oscarmcmaster.org/AR2005", "poortSocialSupport");
        RELATIONSHIPPROBLEMS$2 = new QName("http://www.oscarmcmaster.org/AR2005", "relationshipProblems");
        EMOTIONALDEPRESSION$4 = new QName("http://www.oscarmcmaster.org/AR2005", "emotionalDepression");
        SUBSTANCEABUSE$6 = new QName("http://www.oscarmcmaster.org/AR2005", "substanceAbuse");
        FAMILYVIOLENCE$8 = new QName("http://www.oscarmcmaster.org/AR2005", "familyViolence");
        PARENTINGCONCERNS$10 = new QName("http://www.oscarmcmaster.org/AR2005", "parentingConcerns");
        RELIGIOUSCULTURAL$12 = new QName("http://www.oscarmcmaster.org/AR2005", "religiousCultural");
    }
}
