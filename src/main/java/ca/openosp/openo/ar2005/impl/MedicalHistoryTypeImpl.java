package ca.openosp.openo.ar2005.impl;

import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlObject;
import ca.openosp.openo.ar2005.YesNoNullType;
import org.apache.xmlbeans.SchemaType;
import javax.xml.namespace.QName;
import ca.openosp.openo.ar2005.MedicalHistoryType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

public class MedicalHistoryTypeImpl extends XmlComplexContentImpl implements MedicalHistoryType
{
    private static final long serialVersionUID = 1L;
    private static final QName HYPERTENSION$0;
    private static final QName ENDORINCE$2;
    private static final QName URINARYTRACT$4;
    private static final QName CARDIAC$6;
    private static final QName LIVER$8;
    private static final QName GYNAECOLOGY$10;
    private static final QName HEM$12;
    private static final QName SURGERIES$14;
    private static final QName BLOODTRANSFUSION$16;
    private static final QName ANESTHETICS$18;
    private static final QName PSYCHIATRY$20;
    private static final QName EPILEPSY$22;
    private static final QName OTHERDESCR$24;
    private static final QName OTHER$26;
    
    public MedicalHistoryTypeImpl(final SchemaType sType) {
        super(sType);
    }
    
    public YesNoNullType getHypertension() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(MedicalHistoryTypeImpl.HYPERTENSION$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setHypertension(final YesNoNullType hypertension) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(MedicalHistoryTypeImpl.HYPERTENSION$0, 0);
            if (target == null) {
                target = (YesNoNullType)this.get_store().add_element_user(MedicalHistoryTypeImpl.HYPERTENSION$0);
            }
            target.set((XmlObject)hypertension);
        }
    }
    
    public YesNoNullType addNewHypertension() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().add_element_user(MedicalHistoryTypeImpl.HYPERTENSION$0);
            return target;
        }
    }
    
    public YesNoNullType getEndorince() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(MedicalHistoryTypeImpl.ENDORINCE$2, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setEndorince(final YesNoNullType endorince) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(MedicalHistoryTypeImpl.ENDORINCE$2, 0);
            if (target == null) {
                target = (YesNoNullType)this.get_store().add_element_user(MedicalHistoryTypeImpl.ENDORINCE$2);
            }
            target.set((XmlObject)endorince);
        }
    }
    
    public YesNoNullType addNewEndorince() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().add_element_user(MedicalHistoryTypeImpl.ENDORINCE$2);
            return target;
        }
    }
    
    public YesNoNullType getUrinaryTract() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(MedicalHistoryTypeImpl.URINARYTRACT$4, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setUrinaryTract(final YesNoNullType urinaryTract) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(MedicalHistoryTypeImpl.URINARYTRACT$4, 0);
            if (target == null) {
                target = (YesNoNullType)this.get_store().add_element_user(MedicalHistoryTypeImpl.URINARYTRACT$4);
            }
            target.set((XmlObject)urinaryTract);
        }
    }
    
    public YesNoNullType addNewUrinaryTract() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().add_element_user(MedicalHistoryTypeImpl.URINARYTRACT$4);
            return target;
        }
    }
    
    public YesNoNullType getCardiac() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(MedicalHistoryTypeImpl.CARDIAC$6, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setCardiac(final YesNoNullType cardiac) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(MedicalHistoryTypeImpl.CARDIAC$6, 0);
            if (target == null) {
                target = (YesNoNullType)this.get_store().add_element_user(MedicalHistoryTypeImpl.CARDIAC$6);
            }
            target.set((XmlObject)cardiac);
        }
    }
    
    public YesNoNullType addNewCardiac() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().add_element_user(MedicalHistoryTypeImpl.CARDIAC$6);
            return target;
        }
    }
    
    public YesNoNullType getLiver() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(MedicalHistoryTypeImpl.LIVER$8, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setLiver(final YesNoNullType liver) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(MedicalHistoryTypeImpl.LIVER$8, 0);
            if (target == null) {
                target = (YesNoNullType)this.get_store().add_element_user(MedicalHistoryTypeImpl.LIVER$8);
            }
            target.set((XmlObject)liver);
        }
    }
    
    public YesNoNullType addNewLiver() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().add_element_user(MedicalHistoryTypeImpl.LIVER$8);
            return target;
        }
    }
    
    public YesNoNullType getGynaecology() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(MedicalHistoryTypeImpl.GYNAECOLOGY$10, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setGynaecology(final YesNoNullType gynaecology) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(MedicalHistoryTypeImpl.GYNAECOLOGY$10, 0);
            if (target == null) {
                target = (YesNoNullType)this.get_store().add_element_user(MedicalHistoryTypeImpl.GYNAECOLOGY$10);
            }
            target.set((XmlObject)gynaecology);
        }
    }
    
    public YesNoNullType addNewGynaecology() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().add_element_user(MedicalHistoryTypeImpl.GYNAECOLOGY$10);
            return target;
        }
    }
    
    public YesNoNullType getHem() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(MedicalHistoryTypeImpl.HEM$12, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setHem(final YesNoNullType hem) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(MedicalHistoryTypeImpl.HEM$12, 0);
            if (target == null) {
                target = (YesNoNullType)this.get_store().add_element_user(MedicalHistoryTypeImpl.HEM$12);
            }
            target.set((XmlObject)hem);
        }
    }
    
    public YesNoNullType addNewHem() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().add_element_user(MedicalHistoryTypeImpl.HEM$12);
            return target;
        }
    }
    
    public YesNoNullType getSurgeries() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(MedicalHistoryTypeImpl.SURGERIES$14, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setSurgeries(final YesNoNullType surgeries) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(MedicalHistoryTypeImpl.SURGERIES$14, 0);
            if (target == null) {
                target = (YesNoNullType)this.get_store().add_element_user(MedicalHistoryTypeImpl.SURGERIES$14);
            }
            target.set((XmlObject)surgeries);
        }
    }
    
    public YesNoNullType addNewSurgeries() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().add_element_user(MedicalHistoryTypeImpl.SURGERIES$14);
            return target;
        }
    }
    
    public YesNoNullType getBloodTransfusion() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(MedicalHistoryTypeImpl.BLOODTRANSFUSION$16, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setBloodTransfusion(final YesNoNullType bloodTransfusion) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(MedicalHistoryTypeImpl.BLOODTRANSFUSION$16, 0);
            if (target == null) {
                target = (YesNoNullType)this.get_store().add_element_user(MedicalHistoryTypeImpl.BLOODTRANSFUSION$16);
            }
            target.set((XmlObject)bloodTransfusion);
        }
    }
    
    public YesNoNullType addNewBloodTransfusion() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().add_element_user(MedicalHistoryTypeImpl.BLOODTRANSFUSION$16);
            return target;
        }
    }
    
    public YesNoNullType getAnesthetics() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(MedicalHistoryTypeImpl.ANESTHETICS$18, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setAnesthetics(final YesNoNullType anesthetics) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(MedicalHistoryTypeImpl.ANESTHETICS$18, 0);
            if (target == null) {
                target = (YesNoNullType)this.get_store().add_element_user(MedicalHistoryTypeImpl.ANESTHETICS$18);
            }
            target.set((XmlObject)anesthetics);
        }
    }
    
    public YesNoNullType addNewAnesthetics() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().add_element_user(MedicalHistoryTypeImpl.ANESTHETICS$18);
            return target;
        }
    }
    
    public YesNoNullType getPsychiatry() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(MedicalHistoryTypeImpl.PSYCHIATRY$20, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setPsychiatry(final YesNoNullType psychiatry) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(MedicalHistoryTypeImpl.PSYCHIATRY$20, 0);
            if (target == null) {
                target = (YesNoNullType)this.get_store().add_element_user(MedicalHistoryTypeImpl.PSYCHIATRY$20);
            }
            target.set((XmlObject)psychiatry);
        }
    }
    
    public YesNoNullType addNewPsychiatry() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().add_element_user(MedicalHistoryTypeImpl.PSYCHIATRY$20);
            return target;
        }
    }
    
    public YesNoNullType getEpilepsy() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(MedicalHistoryTypeImpl.EPILEPSY$22, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setEpilepsy(final YesNoNullType epilepsy) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(MedicalHistoryTypeImpl.EPILEPSY$22, 0);
            if (target == null) {
                target = (YesNoNullType)this.get_store().add_element_user(MedicalHistoryTypeImpl.EPILEPSY$22);
            }
            target.set((XmlObject)epilepsy);
        }
    }
    
    public YesNoNullType addNewEpilepsy() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().add_element_user(MedicalHistoryTypeImpl.EPILEPSY$22);
            return target;
        }
    }
    
    public String getOtherDescr() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(MedicalHistoryTypeImpl.OTHERDESCR$24, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public XmlString xgetOtherDescr() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(MedicalHistoryTypeImpl.OTHERDESCR$24, 0);
            return target;
        }
    }
    
    public void setOtherDescr(final String otherDescr) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(MedicalHistoryTypeImpl.OTHERDESCR$24, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(MedicalHistoryTypeImpl.OTHERDESCR$24);
            }
            target.setStringValue(otherDescr);
        }
    }
    
    public void xsetOtherDescr(final XmlString otherDescr) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(MedicalHistoryTypeImpl.OTHERDESCR$24, 0);
            if (target == null) {
                target = (XmlString)this.get_store().add_element_user(MedicalHistoryTypeImpl.OTHERDESCR$24);
            }
            target.set((XmlObject)otherDescr);
        }
    }
    
    public YesNoNullType getOther() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(MedicalHistoryTypeImpl.OTHER$26, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setOther(final YesNoNullType other) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(MedicalHistoryTypeImpl.OTHER$26, 0);
            if (target == null) {
                target = (YesNoNullType)this.get_store().add_element_user(MedicalHistoryTypeImpl.OTHER$26);
            }
            target.set((XmlObject)other);
        }
    }
    
    public YesNoNullType addNewOther() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().add_element_user(MedicalHistoryTypeImpl.OTHER$26);
            return target;
        }
    }
    
    static {
        HYPERTENSION$0 = new QName("http://www.oscarmcmaster.org/AR2005", "hypertension");
        ENDORINCE$2 = new QName("http://www.oscarmcmaster.org/AR2005", "endorince");
        URINARYTRACT$4 = new QName("http://www.oscarmcmaster.org/AR2005", "urinaryTract");
        CARDIAC$6 = new QName("http://www.oscarmcmaster.org/AR2005", "cardiac");
        LIVER$8 = new QName("http://www.oscarmcmaster.org/AR2005", "liver");
        GYNAECOLOGY$10 = new QName("http://www.oscarmcmaster.org/AR2005", "gynaecology");
        HEM$12 = new QName("http://www.oscarmcmaster.org/AR2005", "hem");
        SURGERIES$14 = new QName("http://www.oscarmcmaster.org/AR2005", "surgeries");
        BLOODTRANSFUSION$16 = new QName("http://www.oscarmcmaster.org/AR2005", "bloodTransfusion");
        ANESTHETICS$18 = new QName("http://www.oscarmcmaster.org/AR2005", "anesthetics");
        PSYCHIATRY$20 = new QName("http://www.oscarmcmaster.org/AR2005", "psychiatry");
        EPILEPSY$22 = new QName("http://www.oscarmcmaster.org/AR2005", "epilepsy");
        OTHERDESCR$24 = new QName("http://www.oscarmcmaster.org/AR2005", "otherDescr");
        OTHER$26 = new QName("http://www.oscarmcmaster.org/AR2005", "other");
    }
}
