package ca.openosp.openo.ar2005.impl;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.SchemaType;
import javax.xml.namespace.QName;
import ca.openosp.openo.ar2005.DiscussionTopicsType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

public class DiscussionTopicsTypeImpl extends XmlComplexContentImpl implements DiscussionTopicsType
{
    private static final long serialVersionUID = 1L;
    private static final QName EXERCISE$0;
    private static final QName WORKPLAN$2;
    private static final QName INTERCOURSE$4;
    private static final QName TRAVEL$6;
    private static final QName PRENATALCLASSES$8;
    private static final QName BIRTHPLAN$10;
    private static final QName ONCALLPROVIDERS$12;
    private static final QName PRETERMLABOUR$14;
    private static final QName PROM$16;
    private static final QName APH$18;
    private static final QName FETALMOVEMENT$20;
    private static final QName ADMISSIONTIMING$22;
    private static final QName PAINMANAGEMENT$24;
    private static final QName LABOURSUPPORT$26;
    private static final QName BREASTFEEDING$28;
    private static final QName CIRCUMCISION$30;
    private static final QName DISCHARGEPLANNING$32;
    private static final QName CARSEATSAFETY$34;
    private static final QName DEPRESSION$36;
    private static final QName CONTRACEPTION$38;
    private static final QName POSTPARTUMCARE$40;
    
    public DiscussionTopicsTypeImpl(final SchemaType sType) {
        super(sType);
    }
    
    public boolean getExercise() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DiscussionTopicsTypeImpl.EXERCISE$0, 0);
            return target != null && target.getBooleanValue();
        }
    }
    
    public XmlBoolean xgetExercise() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(DiscussionTopicsTypeImpl.EXERCISE$0, 0);
            return target;
        }
    }
    
    public void setExercise(final boolean exercise) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DiscussionTopicsTypeImpl.EXERCISE$0, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(DiscussionTopicsTypeImpl.EXERCISE$0);
            }
            target.setBooleanValue(exercise);
        }
    }
    
    public void xsetExercise(final XmlBoolean exercise) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(DiscussionTopicsTypeImpl.EXERCISE$0, 0);
            if (target == null) {
                target = (XmlBoolean)this.get_store().add_element_user(DiscussionTopicsTypeImpl.EXERCISE$0);
            }
            target.set((XmlObject)exercise);
        }
    }
    
    public boolean getWorkPlan() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DiscussionTopicsTypeImpl.WORKPLAN$2, 0);
            return target != null && target.getBooleanValue();
        }
    }
    
    public XmlBoolean xgetWorkPlan() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(DiscussionTopicsTypeImpl.WORKPLAN$2, 0);
            return target;
        }
    }
    
    public void setWorkPlan(final boolean workPlan) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DiscussionTopicsTypeImpl.WORKPLAN$2, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(DiscussionTopicsTypeImpl.WORKPLAN$2);
            }
            target.setBooleanValue(workPlan);
        }
    }
    
    public void xsetWorkPlan(final XmlBoolean workPlan) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(DiscussionTopicsTypeImpl.WORKPLAN$2, 0);
            if (target == null) {
                target = (XmlBoolean)this.get_store().add_element_user(DiscussionTopicsTypeImpl.WORKPLAN$2);
            }
            target.set((XmlObject)workPlan);
        }
    }
    
    public boolean getIntercourse() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DiscussionTopicsTypeImpl.INTERCOURSE$4, 0);
            return target != null && target.getBooleanValue();
        }
    }
    
    public XmlBoolean xgetIntercourse() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(DiscussionTopicsTypeImpl.INTERCOURSE$4, 0);
            return target;
        }
    }
    
    public void setIntercourse(final boolean intercourse) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DiscussionTopicsTypeImpl.INTERCOURSE$4, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(DiscussionTopicsTypeImpl.INTERCOURSE$4);
            }
            target.setBooleanValue(intercourse);
        }
    }
    
    public void xsetIntercourse(final XmlBoolean intercourse) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(DiscussionTopicsTypeImpl.INTERCOURSE$4, 0);
            if (target == null) {
                target = (XmlBoolean)this.get_store().add_element_user(DiscussionTopicsTypeImpl.INTERCOURSE$4);
            }
            target.set((XmlObject)intercourse);
        }
    }
    
    public boolean getTravel() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DiscussionTopicsTypeImpl.TRAVEL$6, 0);
            return target != null && target.getBooleanValue();
        }
    }
    
    public XmlBoolean xgetTravel() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(DiscussionTopicsTypeImpl.TRAVEL$6, 0);
            return target;
        }
    }
    
    public void setTravel(final boolean travel) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DiscussionTopicsTypeImpl.TRAVEL$6, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(DiscussionTopicsTypeImpl.TRAVEL$6);
            }
            target.setBooleanValue(travel);
        }
    }
    
    public void xsetTravel(final XmlBoolean travel) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(DiscussionTopicsTypeImpl.TRAVEL$6, 0);
            if (target == null) {
                target = (XmlBoolean)this.get_store().add_element_user(DiscussionTopicsTypeImpl.TRAVEL$6);
            }
            target.set((XmlObject)travel);
        }
    }
    
    public boolean getPrenatalClasses() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DiscussionTopicsTypeImpl.PRENATALCLASSES$8, 0);
            return target != null && target.getBooleanValue();
        }
    }
    
    public XmlBoolean xgetPrenatalClasses() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(DiscussionTopicsTypeImpl.PRENATALCLASSES$8, 0);
            return target;
        }
    }
    
    public void setPrenatalClasses(final boolean prenatalClasses) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DiscussionTopicsTypeImpl.PRENATALCLASSES$8, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(DiscussionTopicsTypeImpl.PRENATALCLASSES$8);
            }
            target.setBooleanValue(prenatalClasses);
        }
    }
    
    public void xsetPrenatalClasses(final XmlBoolean prenatalClasses) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(DiscussionTopicsTypeImpl.PRENATALCLASSES$8, 0);
            if (target == null) {
                target = (XmlBoolean)this.get_store().add_element_user(DiscussionTopicsTypeImpl.PRENATALCLASSES$8);
            }
            target.set((XmlObject)prenatalClasses);
        }
    }
    
    public boolean getBirthPlan() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DiscussionTopicsTypeImpl.BIRTHPLAN$10, 0);
            return target != null && target.getBooleanValue();
        }
    }
    
    public XmlBoolean xgetBirthPlan() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(DiscussionTopicsTypeImpl.BIRTHPLAN$10, 0);
            return target;
        }
    }
    
    public void setBirthPlan(final boolean birthPlan) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DiscussionTopicsTypeImpl.BIRTHPLAN$10, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(DiscussionTopicsTypeImpl.BIRTHPLAN$10);
            }
            target.setBooleanValue(birthPlan);
        }
    }
    
    public void xsetBirthPlan(final XmlBoolean birthPlan) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(DiscussionTopicsTypeImpl.BIRTHPLAN$10, 0);
            if (target == null) {
                target = (XmlBoolean)this.get_store().add_element_user(DiscussionTopicsTypeImpl.BIRTHPLAN$10);
            }
            target.set((XmlObject)birthPlan);
        }
    }
    
    public boolean getOnCallProviders() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DiscussionTopicsTypeImpl.ONCALLPROVIDERS$12, 0);
            return target != null && target.getBooleanValue();
        }
    }
    
    public XmlBoolean xgetOnCallProviders() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(DiscussionTopicsTypeImpl.ONCALLPROVIDERS$12, 0);
            return target;
        }
    }
    
    public void setOnCallProviders(final boolean onCallProviders) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DiscussionTopicsTypeImpl.ONCALLPROVIDERS$12, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(DiscussionTopicsTypeImpl.ONCALLPROVIDERS$12);
            }
            target.setBooleanValue(onCallProviders);
        }
    }
    
    public void xsetOnCallProviders(final XmlBoolean onCallProviders) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(DiscussionTopicsTypeImpl.ONCALLPROVIDERS$12, 0);
            if (target == null) {
                target = (XmlBoolean)this.get_store().add_element_user(DiscussionTopicsTypeImpl.ONCALLPROVIDERS$12);
            }
            target.set((XmlObject)onCallProviders);
        }
    }
    
    public boolean getPretermLabour() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DiscussionTopicsTypeImpl.PRETERMLABOUR$14, 0);
            return target != null && target.getBooleanValue();
        }
    }
    
    public XmlBoolean xgetPretermLabour() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(DiscussionTopicsTypeImpl.PRETERMLABOUR$14, 0);
            return target;
        }
    }
    
    public void setPretermLabour(final boolean pretermLabour) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DiscussionTopicsTypeImpl.PRETERMLABOUR$14, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(DiscussionTopicsTypeImpl.PRETERMLABOUR$14);
            }
            target.setBooleanValue(pretermLabour);
        }
    }
    
    public void xsetPretermLabour(final XmlBoolean pretermLabour) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(DiscussionTopicsTypeImpl.PRETERMLABOUR$14, 0);
            if (target == null) {
                target = (XmlBoolean)this.get_store().add_element_user(DiscussionTopicsTypeImpl.PRETERMLABOUR$14);
            }
            target.set((XmlObject)pretermLabour);
        }
    }
    
    public boolean getPROM() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DiscussionTopicsTypeImpl.PROM$16, 0);
            return target != null && target.getBooleanValue();
        }
    }
    
    public XmlBoolean xgetPROM() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(DiscussionTopicsTypeImpl.PROM$16, 0);
            return target;
        }
    }
    
    public void setPROM(final boolean prom) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DiscussionTopicsTypeImpl.PROM$16, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(DiscussionTopicsTypeImpl.PROM$16);
            }
            target.setBooleanValue(prom);
        }
    }
    
    public void xsetPROM(final XmlBoolean prom) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(DiscussionTopicsTypeImpl.PROM$16, 0);
            if (target == null) {
                target = (XmlBoolean)this.get_store().add_element_user(DiscussionTopicsTypeImpl.PROM$16);
            }
            target.set((XmlObject)prom);
        }
    }
    
    public boolean getAPH() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DiscussionTopicsTypeImpl.APH$18, 0);
            return target != null && target.getBooleanValue();
        }
    }
    
    public XmlBoolean xgetAPH() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(DiscussionTopicsTypeImpl.APH$18, 0);
            return target;
        }
    }
    
    public void setAPH(final boolean aph) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DiscussionTopicsTypeImpl.APH$18, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(DiscussionTopicsTypeImpl.APH$18);
            }
            target.setBooleanValue(aph);
        }
    }
    
    public void xsetAPH(final XmlBoolean aph) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(DiscussionTopicsTypeImpl.APH$18, 0);
            if (target == null) {
                target = (XmlBoolean)this.get_store().add_element_user(DiscussionTopicsTypeImpl.APH$18);
            }
            target.set((XmlObject)aph);
        }
    }
    
    public boolean getFetalMovement() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DiscussionTopicsTypeImpl.FETALMOVEMENT$20, 0);
            return target != null && target.getBooleanValue();
        }
    }
    
    public XmlBoolean xgetFetalMovement() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(DiscussionTopicsTypeImpl.FETALMOVEMENT$20, 0);
            return target;
        }
    }
    
    public void setFetalMovement(final boolean fetalMovement) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DiscussionTopicsTypeImpl.FETALMOVEMENT$20, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(DiscussionTopicsTypeImpl.FETALMOVEMENT$20);
            }
            target.setBooleanValue(fetalMovement);
        }
    }
    
    public void xsetFetalMovement(final XmlBoolean fetalMovement) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(DiscussionTopicsTypeImpl.FETALMOVEMENT$20, 0);
            if (target == null) {
                target = (XmlBoolean)this.get_store().add_element_user(DiscussionTopicsTypeImpl.FETALMOVEMENT$20);
            }
            target.set((XmlObject)fetalMovement);
        }
    }
    
    public boolean getAdmissionTiming() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DiscussionTopicsTypeImpl.ADMISSIONTIMING$22, 0);
            return target != null && target.getBooleanValue();
        }
    }
    
    public XmlBoolean xgetAdmissionTiming() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(DiscussionTopicsTypeImpl.ADMISSIONTIMING$22, 0);
            return target;
        }
    }
    
    public void setAdmissionTiming(final boolean admissionTiming) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DiscussionTopicsTypeImpl.ADMISSIONTIMING$22, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(DiscussionTopicsTypeImpl.ADMISSIONTIMING$22);
            }
            target.setBooleanValue(admissionTiming);
        }
    }
    
    public void xsetAdmissionTiming(final XmlBoolean admissionTiming) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(DiscussionTopicsTypeImpl.ADMISSIONTIMING$22, 0);
            if (target == null) {
                target = (XmlBoolean)this.get_store().add_element_user(DiscussionTopicsTypeImpl.ADMISSIONTIMING$22);
            }
            target.set((XmlObject)admissionTiming);
        }
    }
    
    public boolean getPainManagement() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DiscussionTopicsTypeImpl.PAINMANAGEMENT$24, 0);
            return target != null && target.getBooleanValue();
        }
    }
    
    public XmlBoolean xgetPainManagement() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(DiscussionTopicsTypeImpl.PAINMANAGEMENT$24, 0);
            return target;
        }
    }
    
    public void setPainManagement(final boolean painManagement) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DiscussionTopicsTypeImpl.PAINMANAGEMENT$24, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(DiscussionTopicsTypeImpl.PAINMANAGEMENT$24);
            }
            target.setBooleanValue(painManagement);
        }
    }
    
    public void xsetPainManagement(final XmlBoolean painManagement) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(DiscussionTopicsTypeImpl.PAINMANAGEMENT$24, 0);
            if (target == null) {
                target = (XmlBoolean)this.get_store().add_element_user(DiscussionTopicsTypeImpl.PAINMANAGEMENT$24);
            }
            target.set((XmlObject)painManagement);
        }
    }
    
    public boolean getLabourSupport() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DiscussionTopicsTypeImpl.LABOURSUPPORT$26, 0);
            return target != null && target.getBooleanValue();
        }
    }
    
    public XmlBoolean xgetLabourSupport() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(DiscussionTopicsTypeImpl.LABOURSUPPORT$26, 0);
            return target;
        }
    }
    
    public void setLabourSupport(final boolean labourSupport) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DiscussionTopicsTypeImpl.LABOURSUPPORT$26, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(DiscussionTopicsTypeImpl.LABOURSUPPORT$26);
            }
            target.setBooleanValue(labourSupport);
        }
    }
    
    public void xsetLabourSupport(final XmlBoolean labourSupport) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(DiscussionTopicsTypeImpl.LABOURSUPPORT$26, 0);
            if (target == null) {
                target = (XmlBoolean)this.get_store().add_element_user(DiscussionTopicsTypeImpl.LABOURSUPPORT$26);
            }
            target.set((XmlObject)labourSupport);
        }
    }
    
    public boolean getBreastFeeding() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DiscussionTopicsTypeImpl.BREASTFEEDING$28, 0);
            return target != null && target.getBooleanValue();
        }
    }
    
    public XmlBoolean xgetBreastFeeding() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(DiscussionTopicsTypeImpl.BREASTFEEDING$28, 0);
            return target;
        }
    }
    
    public void setBreastFeeding(final boolean breastFeeding) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DiscussionTopicsTypeImpl.BREASTFEEDING$28, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(DiscussionTopicsTypeImpl.BREASTFEEDING$28);
            }
            target.setBooleanValue(breastFeeding);
        }
    }
    
    public void xsetBreastFeeding(final XmlBoolean breastFeeding) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(DiscussionTopicsTypeImpl.BREASTFEEDING$28, 0);
            if (target == null) {
                target = (XmlBoolean)this.get_store().add_element_user(DiscussionTopicsTypeImpl.BREASTFEEDING$28);
            }
            target.set((XmlObject)breastFeeding);
        }
    }
    
    public boolean getCircumcision() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DiscussionTopicsTypeImpl.CIRCUMCISION$30, 0);
            return target != null && target.getBooleanValue();
        }
    }
    
    public XmlBoolean xgetCircumcision() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(DiscussionTopicsTypeImpl.CIRCUMCISION$30, 0);
            return target;
        }
    }
    
    public void setCircumcision(final boolean circumcision) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DiscussionTopicsTypeImpl.CIRCUMCISION$30, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(DiscussionTopicsTypeImpl.CIRCUMCISION$30);
            }
            target.setBooleanValue(circumcision);
        }
    }
    
    public void xsetCircumcision(final XmlBoolean circumcision) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(DiscussionTopicsTypeImpl.CIRCUMCISION$30, 0);
            if (target == null) {
                target = (XmlBoolean)this.get_store().add_element_user(DiscussionTopicsTypeImpl.CIRCUMCISION$30);
            }
            target.set((XmlObject)circumcision);
        }
    }
    
    public boolean getDischargePlanning() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DiscussionTopicsTypeImpl.DISCHARGEPLANNING$32, 0);
            return target != null && target.getBooleanValue();
        }
    }
    
    public XmlBoolean xgetDischargePlanning() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(DiscussionTopicsTypeImpl.DISCHARGEPLANNING$32, 0);
            return target;
        }
    }
    
    public void setDischargePlanning(final boolean dischargePlanning) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DiscussionTopicsTypeImpl.DISCHARGEPLANNING$32, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(DiscussionTopicsTypeImpl.DISCHARGEPLANNING$32);
            }
            target.setBooleanValue(dischargePlanning);
        }
    }
    
    public void xsetDischargePlanning(final XmlBoolean dischargePlanning) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(DiscussionTopicsTypeImpl.DISCHARGEPLANNING$32, 0);
            if (target == null) {
                target = (XmlBoolean)this.get_store().add_element_user(DiscussionTopicsTypeImpl.DISCHARGEPLANNING$32);
            }
            target.set((XmlObject)dischargePlanning);
        }
    }
    
    public boolean getCarSeatSafety() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DiscussionTopicsTypeImpl.CARSEATSAFETY$34, 0);
            return target != null && target.getBooleanValue();
        }
    }
    
    public XmlBoolean xgetCarSeatSafety() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(DiscussionTopicsTypeImpl.CARSEATSAFETY$34, 0);
            return target;
        }
    }
    
    public void setCarSeatSafety(final boolean carSeatSafety) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DiscussionTopicsTypeImpl.CARSEATSAFETY$34, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(DiscussionTopicsTypeImpl.CARSEATSAFETY$34);
            }
            target.setBooleanValue(carSeatSafety);
        }
    }
    
    public void xsetCarSeatSafety(final XmlBoolean carSeatSafety) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(DiscussionTopicsTypeImpl.CARSEATSAFETY$34, 0);
            if (target == null) {
                target = (XmlBoolean)this.get_store().add_element_user(DiscussionTopicsTypeImpl.CARSEATSAFETY$34);
            }
            target.set((XmlObject)carSeatSafety);
        }
    }
    
    public boolean getDepression() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DiscussionTopicsTypeImpl.DEPRESSION$36, 0);
            return target != null && target.getBooleanValue();
        }
    }
    
    public XmlBoolean xgetDepression() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(DiscussionTopicsTypeImpl.DEPRESSION$36, 0);
            return target;
        }
    }
    
    public void setDepression(final boolean depression) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DiscussionTopicsTypeImpl.DEPRESSION$36, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(DiscussionTopicsTypeImpl.DEPRESSION$36);
            }
            target.setBooleanValue(depression);
        }
    }
    
    public void xsetDepression(final XmlBoolean depression) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(DiscussionTopicsTypeImpl.DEPRESSION$36, 0);
            if (target == null) {
                target = (XmlBoolean)this.get_store().add_element_user(DiscussionTopicsTypeImpl.DEPRESSION$36);
            }
            target.set((XmlObject)depression);
        }
    }
    
    public boolean getContraception() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DiscussionTopicsTypeImpl.CONTRACEPTION$38, 0);
            return target != null && target.getBooleanValue();
        }
    }
    
    public XmlBoolean xgetContraception() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(DiscussionTopicsTypeImpl.CONTRACEPTION$38, 0);
            return target;
        }
    }
    
    public void setContraception(final boolean contraception) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DiscussionTopicsTypeImpl.CONTRACEPTION$38, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(DiscussionTopicsTypeImpl.CONTRACEPTION$38);
            }
            target.setBooleanValue(contraception);
        }
    }
    
    public void xsetContraception(final XmlBoolean contraception) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(DiscussionTopicsTypeImpl.CONTRACEPTION$38, 0);
            if (target == null) {
                target = (XmlBoolean)this.get_store().add_element_user(DiscussionTopicsTypeImpl.CONTRACEPTION$38);
            }
            target.set((XmlObject)contraception);
        }
    }
    
    public boolean getPostpartumCare() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DiscussionTopicsTypeImpl.POSTPARTUMCARE$40, 0);
            return target != null && target.getBooleanValue();
        }
    }
    
    public XmlBoolean xgetPostpartumCare() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(DiscussionTopicsTypeImpl.POSTPARTUMCARE$40, 0);
            return target;
        }
    }
    
    public void setPostpartumCare(final boolean postpartumCare) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DiscussionTopicsTypeImpl.POSTPARTUMCARE$40, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(DiscussionTopicsTypeImpl.POSTPARTUMCARE$40);
            }
            target.setBooleanValue(postpartumCare);
        }
    }
    
    public void xsetPostpartumCare(final XmlBoolean postpartumCare) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(DiscussionTopicsTypeImpl.POSTPARTUMCARE$40, 0);
            if (target == null) {
                target = (XmlBoolean)this.get_store().add_element_user(DiscussionTopicsTypeImpl.POSTPARTUMCARE$40);
            }
            target.set((XmlObject)postpartumCare);
        }
    }
    
    static {
        EXERCISE$0 = new QName("http://www.oscarmcmaster.org/AR2005", "exercise");
        WORKPLAN$2 = new QName("http://www.oscarmcmaster.org/AR2005", "workPlan");
        INTERCOURSE$4 = new QName("http://www.oscarmcmaster.org/AR2005", "intercourse");
        TRAVEL$6 = new QName("http://www.oscarmcmaster.org/AR2005", "travel");
        PRENATALCLASSES$8 = new QName("http://www.oscarmcmaster.org/AR2005", "prenatalClasses");
        BIRTHPLAN$10 = new QName("http://www.oscarmcmaster.org/AR2005", "birthPlan");
        ONCALLPROVIDERS$12 = new QName("http://www.oscarmcmaster.org/AR2005", "onCallProviders");
        PRETERMLABOUR$14 = new QName("http://www.oscarmcmaster.org/AR2005", "pretermLabour");
        PROM$16 = new QName("http://www.oscarmcmaster.org/AR2005", "PROM");
        APH$18 = new QName("http://www.oscarmcmaster.org/AR2005", "APH");
        FETALMOVEMENT$20 = new QName("http://www.oscarmcmaster.org/AR2005", "fetalMovement");
        ADMISSIONTIMING$22 = new QName("http://www.oscarmcmaster.org/AR2005", "admissionTiming");
        PAINMANAGEMENT$24 = new QName("http://www.oscarmcmaster.org/AR2005", "painManagement");
        LABOURSUPPORT$26 = new QName("http://www.oscarmcmaster.org/AR2005", "labourSupport");
        BREASTFEEDING$28 = new QName("http://www.oscarmcmaster.org/AR2005", "breastFeeding");
        CIRCUMCISION$30 = new QName("http://www.oscarmcmaster.org/AR2005", "circumcision");
        DISCHARGEPLANNING$32 = new QName("http://www.oscarmcmaster.org/AR2005", "dischargePlanning");
        CARSEATSAFETY$34 = new QName("http://www.oscarmcmaster.org/AR2005", "carSeatSafety");
        DEPRESSION$36 = new QName("http://www.oscarmcmaster.org/AR2005", "depression");
        CONTRACEPTION$38 = new QName("http://www.oscarmcmaster.org/AR2005", "contraception");
        POSTPARTUMCARE$40 = new QName("http://www.oscarmcmaster.org/AR2005", "postpartumCare");
    }
}
