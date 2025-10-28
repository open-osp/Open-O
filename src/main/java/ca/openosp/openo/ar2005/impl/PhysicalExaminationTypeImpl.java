package ca.openosp.openo.ar2005.impl;

import org.apache.xmlbeans.impl.values.JavaStringHolderEx;
import org.apache.xmlbeans.impl.values.JavaFloatHolderEx;
import org.apache.xmlbeans.XmlString;
import ca.openosp.openo.ar2005.NormalAbnormalNullType;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.SchemaType;
import javax.xml.namespace.QName;
import ca.openosp.openo.ar2005.PhysicalExaminationType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

public class PhysicalExaminationTypeImpl extends XmlComplexContentImpl implements PhysicalExaminationType
{
    private static final long serialVersionUID = 1L;
    private static final QName HEIGHT$0;
    private static final QName WEIGHT$2;
    private static final QName BMI$4;
    private static final QName BP$6;
    private static final QName THYROID$8;
    private static final QName CHEST$10;
    private static final QName BREASTS$12;
    private static final QName CARDIOVASCULAR$14;
    private static final QName ABDOMEN$16;
    private static final QName VARICOSITIES$18;
    private static final QName EXERNALGENITALS$20;
    private static final QName CERVIXVAGINA$22;
    private static final QName UTERUS$24;
    private static final QName UTERUSSIZE$26;
    private static final QName ADNEXA$28;
    private static final QName OTHERDESCR$30;
    private static final QName OTHER$32;
    
    public PhysicalExaminationTypeImpl(final SchemaType sType) {
        super(sType);
    }
    
    public float getHeight() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PhysicalExaminationTypeImpl.HEIGHT$0, 0);
            if (target == null) {
                return 0.0f;
            }
            return target.getFloatValue();
        }
    }
    
    public Height xgetHeight() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Height target = null;
            target = (Height)this.get_store().find_element_user(PhysicalExaminationTypeImpl.HEIGHT$0, 0);
            return target;
        }
    }
    
    public boolean isNilHeight() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Height target = null;
            target = (Height)this.get_store().find_element_user(PhysicalExaminationTypeImpl.HEIGHT$0, 0);
            return target != null && target.isNil();
        }
    }
    
    public void setHeight(final float height) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PhysicalExaminationTypeImpl.HEIGHT$0, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(PhysicalExaminationTypeImpl.HEIGHT$0);
            }
            target.setFloatValue(height);
        }
    }
    
    public void xsetHeight(final Height height) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Height target = null;
            target = (Height)this.get_store().find_element_user(PhysicalExaminationTypeImpl.HEIGHT$0, 0);
            if (target == null) {
                target = (Height)this.get_store().add_element_user(PhysicalExaminationTypeImpl.HEIGHT$0);
            }
            target.set((XmlObject)height);
        }
    }
    
    public void setNilHeight() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Height target = null;
            target = (Height)this.get_store().find_element_user(PhysicalExaminationTypeImpl.HEIGHT$0, 0);
            if (target == null) {
                target = (Height)this.get_store().add_element_user(PhysicalExaminationTypeImpl.HEIGHT$0);
            }
            target.setNil();
        }
    }
    
    public float getWeight() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PhysicalExaminationTypeImpl.WEIGHT$2, 0);
            if (target == null) {
                return 0.0f;
            }
            return target.getFloatValue();
        }
    }
    
    public Weight xgetWeight() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Weight target = null;
            target = (Weight)this.get_store().find_element_user(PhysicalExaminationTypeImpl.WEIGHT$2, 0);
            return target;
        }
    }
    
    public boolean isNilWeight() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Weight target = null;
            target = (Weight)this.get_store().find_element_user(PhysicalExaminationTypeImpl.WEIGHT$2, 0);
            return target != null && target.isNil();
        }
    }
    
    public void setWeight(final float weight) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PhysicalExaminationTypeImpl.WEIGHT$2, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(PhysicalExaminationTypeImpl.WEIGHT$2);
            }
            target.setFloatValue(weight);
        }
    }
    
    public void xsetWeight(final Weight weight) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Weight target = null;
            target = (Weight)this.get_store().find_element_user(PhysicalExaminationTypeImpl.WEIGHT$2, 0);
            if (target == null) {
                target = (Weight)this.get_store().add_element_user(PhysicalExaminationTypeImpl.WEIGHT$2);
            }
            target.set((XmlObject)weight);
        }
    }
    
    public void setNilWeight() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Weight target = null;
            target = (Weight)this.get_store().find_element_user(PhysicalExaminationTypeImpl.WEIGHT$2, 0);
            if (target == null) {
                target = (Weight)this.get_store().add_element_user(PhysicalExaminationTypeImpl.WEIGHT$2);
            }
            target.setNil();
        }
    }
    
    public float getBmi() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PhysicalExaminationTypeImpl.BMI$4, 0);
            if (target == null) {
                return 0.0f;
            }
            return target.getFloatValue();
        }
    }
    
    public Bmi xgetBmi() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Bmi target = null;
            target = (Bmi)this.get_store().find_element_user(PhysicalExaminationTypeImpl.BMI$4, 0);
            return target;
        }
    }
    
    public boolean isNilBmi() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Bmi target = null;
            target = (Bmi)this.get_store().find_element_user(PhysicalExaminationTypeImpl.BMI$4, 0);
            return target != null && target.isNil();
        }
    }
    
    public void setBmi(final float bmi) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PhysicalExaminationTypeImpl.BMI$4, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(PhysicalExaminationTypeImpl.BMI$4);
            }
            target.setFloatValue(bmi);
        }
    }
    
    public void xsetBmi(final Bmi bmi) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Bmi target = null;
            target = (Bmi)this.get_store().find_element_user(PhysicalExaminationTypeImpl.BMI$4, 0);
            if (target == null) {
                target = (Bmi)this.get_store().add_element_user(PhysicalExaminationTypeImpl.BMI$4);
            }
            target.set((XmlObject)bmi);
        }
    }
    
    public void setNilBmi() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Bmi target = null;
            target = (Bmi)this.get_store().find_element_user(PhysicalExaminationTypeImpl.BMI$4, 0);
            if (target == null) {
                target = (Bmi)this.get_store().add_element_user(PhysicalExaminationTypeImpl.BMI$4);
            }
            target.setNil();
        }
    }
    
    public String getBp() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PhysicalExaminationTypeImpl.BP$6, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public Bp xgetBp() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Bp target = null;
            target = (Bp)this.get_store().find_element_user(PhysicalExaminationTypeImpl.BP$6, 0);
            return target;
        }
    }
    
    public void setBp(final String bp) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PhysicalExaminationTypeImpl.BP$6, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(PhysicalExaminationTypeImpl.BP$6);
            }
            target.setStringValue(bp);
        }
    }
    
    public void xsetBp(final Bp bp) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Bp target = null;
            target = (Bp)this.get_store().find_element_user(PhysicalExaminationTypeImpl.BP$6, 0);
            if (target == null) {
                target = (Bp)this.get_store().add_element_user(PhysicalExaminationTypeImpl.BP$6);
            }
            target.set((XmlObject)bp);
        }
    }
    
    public NormalAbnormalNullType getThyroid() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            NormalAbnormalNullType target = null;
            target = (NormalAbnormalNullType)this.get_store().find_element_user(PhysicalExaminationTypeImpl.THYROID$8, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setThyroid(final NormalAbnormalNullType thyroid) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            NormalAbnormalNullType target = null;
            target = (NormalAbnormalNullType)this.get_store().find_element_user(PhysicalExaminationTypeImpl.THYROID$8, 0);
            if (target == null) {
                target = (NormalAbnormalNullType)this.get_store().add_element_user(PhysicalExaminationTypeImpl.THYROID$8);
            }
            target.set((XmlObject)thyroid);
        }
    }
    
    public NormalAbnormalNullType addNewThyroid() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            NormalAbnormalNullType target = null;
            target = (NormalAbnormalNullType)this.get_store().add_element_user(PhysicalExaminationTypeImpl.THYROID$8);
            return target;
        }
    }
    
    public NormalAbnormalNullType getChest() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            NormalAbnormalNullType target = null;
            target = (NormalAbnormalNullType)this.get_store().find_element_user(PhysicalExaminationTypeImpl.CHEST$10, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setChest(final NormalAbnormalNullType chest) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            NormalAbnormalNullType target = null;
            target = (NormalAbnormalNullType)this.get_store().find_element_user(PhysicalExaminationTypeImpl.CHEST$10, 0);
            if (target == null) {
                target = (NormalAbnormalNullType)this.get_store().add_element_user(PhysicalExaminationTypeImpl.CHEST$10);
            }
            target.set((XmlObject)chest);
        }
    }
    
    public NormalAbnormalNullType addNewChest() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            NormalAbnormalNullType target = null;
            target = (NormalAbnormalNullType)this.get_store().add_element_user(PhysicalExaminationTypeImpl.CHEST$10);
            return target;
        }
    }
    
    public NormalAbnormalNullType getBreasts() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            NormalAbnormalNullType target = null;
            target = (NormalAbnormalNullType)this.get_store().find_element_user(PhysicalExaminationTypeImpl.BREASTS$12, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setBreasts(final NormalAbnormalNullType breasts) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            NormalAbnormalNullType target = null;
            target = (NormalAbnormalNullType)this.get_store().find_element_user(PhysicalExaminationTypeImpl.BREASTS$12, 0);
            if (target == null) {
                target = (NormalAbnormalNullType)this.get_store().add_element_user(PhysicalExaminationTypeImpl.BREASTS$12);
            }
            target.set((XmlObject)breasts);
        }
    }
    
    public NormalAbnormalNullType addNewBreasts() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            NormalAbnormalNullType target = null;
            target = (NormalAbnormalNullType)this.get_store().add_element_user(PhysicalExaminationTypeImpl.BREASTS$12);
            return target;
        }
    }
    
    public NormalAbnormalNullType getCardiovascular() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            NormalAbnormalNullType target = null;
            target = (NormalAbnormalNullType)this.get_store().find_element_user(PhysicalExaminationTypeImpl.CARDIOVASCULAR$14, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setCardiovascular(final NormalAbnormalNullType cardiovascular) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            NormalAbnormalNullType target = null;
            target = (NormalAbnormalNullType)this.get_store().find_element_user(PhysicalExaminationTypeImpl.CARDIOVASCULAR$14, 0);
            if (target == null) {
                target = (NormalAbnormalNullType)this.get_store().add_element_user(PhysicalExaminationTypeImpl.CARDIOVASCULAR$14);
            }
            target.set((XmlObject)cardiovascular);
        }
    }
    
    public NormalAbnormalNullType addNewCardiovascular() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            NormalAbnormalNullType target = null;
            target = (NormalAbnormalNullType)this.get_store().add_element_user(PhysicalExaminationTypeImpl.CARDIOVASCULAR$14);
            return target;
        }
    }
    
    public NormalAbnormalNullType getAbdomen() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            NormalAbnormalNullType target = null;
            target = (NormalAbnormalNullType)this.get_store().find_element_user(PhysicalExaminationTypeImpl.ABDOMEN$16, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setAbdomen(final NormalAbnormalNullType abdomen) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            NormalAbnormalNullType target = null;
            target = (NormalAbnormalNullType)this.get_store().find_element_user(PhysicalExaminationTypeImpl.ABDOMEN$16, 0);
            if (target == null) {
                target = (NormalAbnormalNullType)this.get_store().add_element_user(PhysicalExaminationTypeImpl.ABDOMEN$16);
            }
            target.set((XmlObject)abdomen);
        }
    }
    
    public NormalAbnormalNullType addNewAbdomen() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            NormalAbnormalNullType target = null;
            target = (NormalAbnormalNullType)this.get_store().add_element_user(PhysicalExaminationTypeImpl.ABDOMEN$16);
            return target;
        }
    }
    
    public NormalAbnormalNullType getVaricosities() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            NormalAbnormalNullType target = null;
            target = (NormalAbnormalNullType)this.get_store().find_element_user(PhysicalExaminationTypeImpl.VARICOSITIES$18, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setVaricosities(final NormalAbnormalNullType varicosities) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            NormalAbnormalNullType target = null;
            target = (NormalAbnormalNullType)this.get_store().find_element_user(PhysicalExaminationTypeImpl.VARICOSITIES$18, 0);
            if (target == null) {
                target = (NormalAbnormalNullType)this.get_store().add_element_user(PhysicalExaminationTypeImpl.VARICOSITIES$18);
            }
            target.set((XmlObject)varicosities);
        }
    }
    
    public NormalAbnormalNullType addNewVaricosities() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            NormalAbnormalNullType target = null;
            target = (NormalAbnormalNullType)this.get_store().add_element_user(PhysicalExaminationTypeImpl.VARICOSITIES$18);
            return target;
        }
    }
    
    public NormalAbnormalNullType getExernalGenitals() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            NormalAbnormalNullType target = null;
            target = (NormalAbnormalNullType)this.get_store().find_element_user(PhysicalExaminationTypeImpl.EXERNALGENITALS$20, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setExernalGenitals(final NormalAbnormalNullType exernalGenitals) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            NormalAbnormalNullType target = null;
            target = (NormalAbnormalNullType)this.get_store().find_element_user(PhysicalExaminationTypeImpl.EXERNALGENITALS$20, 0);
            if (target == null) {
                target = (NormalAbnormalNullType)this.get_store().add_element_user(PhysicalExaminationTypeImpl.EXERNALGENITALS$20);
            }
            target.set((XmlObject)exernalGenitals);
        }
    }
    
    public NormalAbnormalNullType addNewExernalGenitals() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            NormalAbnormalNullType target = null;
            target = (NormalAbnormalNullType)this.get_store().add_element_user(PhysicalExaminationTypeImpl.EXERNALGENITALS$20);
            return target;
        }
    }
    
    public NormalAbnormalNullType getCervixVagina() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            NormalAbnormalNullType target = null;
            target = (NormalAbnormalNullType)this.get_store().find_element_user(PhysicalExaminationTypeImpl.CERVIXVAGINA$22, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setCervixVagina(final NormalAbnormalNullType cervixVagina) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            NormalAbnormalNullType target = null;
            target = (NormalAbnormalNullType)this.get_store().find_element_user(PhysicalExaminationTypeImpl.CERVIXVAGINA$22, 0);
            if (target == null) {
                target = (NormalAbnormalNullType)this.get_store().add_element_user(PhysicalExaminationTypeImpl.CERVIXVAGINA$22);
            }
            target.set((XmlObject)cervixVagina);
        }
    }
    
    public NormalAbnormalNullType addNewCervixVagina() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            NormalAbnormalNullType target = null;
            target = (NormalAbnormalNullType)this.get_store().add_element_user(PhysicalExaminationTypeImpl.CERVIXVAGINA$22);
            return target;
        }
    }
    
    public NormalAbnormalNullType getUterus() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            NormalAbnormalNullType target = null;
            target = (NormalAbnormalNullType)this.get_store().find_element_user(PhysicalExaminationTypeImpl.UTERUS$24, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setUterus(final NormalAbnormalNullType uterus) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            NormalAbnormalNullType target = null;
            target = (NormalAbnormalNullType)this.get_store().find_element_user(PhysicalExaminationTypeImpl.UTERUS$24, 0);
            if (target == null) {
                target = (NormalAbnormalNullType)this.get_store().add_element_user(PhysicalExaminationTypeImpl.UTERUS$24);
            }
            target.set((XmlObject)uterus);
        }
    }
    
    public NormalAbnormalNullType addNewUterus() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            NormalAbnormalNullType target = null;
            target = (NormalAbnormalNullType)this.get_store().add_element_user(PhysicalExaminationTypeImpl.UTERUS$24);
            return target;
        }
    }
    
    public String getUterusSize() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PhysicalExaminationTypeImpl.UTERUSSIZE$26, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public XmlString xgetUterusSize() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(PhysicalExaminationTypeImpl.UTERUSSIZE$26, 0);
            return target;
        }
    }
    
    public void setUterusSize(final String uterusSize) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PhysicalExaminationTypeImpl.UTERUSSIZE$26, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(PhysicalExaminationTypeImpl.UTERUSSIZE$26);
            }
            target.setStringValue(uterusSize);
        }
    }
    
    public void xsetUterusSize(final XmlString uterusSize) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(PhysicalExaminationTypeImpl.UTERUSSIZE$26, 0);
            if (target == null) {
                target = (XmlString)this.get_store().add_element_user(PhysicalExaminationTypeImpl.UTERUSSIZE$26);
            }
            target.set((XmlObject)uterusSize);
        }
    }
    
    public NormalAbnormalNullType getAdnexa() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            NormalAbnormalNullType target = null;
            target = (NormalAbnormalNullType)this.get_store().find_element_user(PhysicalExaminationTypeImpl.ADNEXA$28, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setAdnexa(final NormalAbnormalNullType adnexa) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            NormalAbnormalNullType target = null;
            target = (NormalAbnormalNullType)this.get_store().find_element_user(PhysicalExaminationTypeImpl.ADNEXA$28, 0);
            if (target == null) {
                target = (NormalAbnormalNullType)this.get_store().add_element_user(PhysicalExaminationTypeImpl.ADNEXA$28);
            }
            target.set((XmlObject)adnexa);
        }
    }
    
    public NormalAbnormalNullType addNewAdnexa() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            NormalAbnormalNullType target = null;
            target = (NormalAbnormalNullType)this.get_store().add_element_user(PhysicalExaminationTypeImpl.ADNEXA$28);
            return target;
        }
    }
    
    public String getOtherDescr() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PhysicalExaminationTypeImpl.OTHERDESCR$30, 0);
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
            target = (XmlString)this.get_store().find_element_user(PhysicalExaminationTypeImpl.OTHERDESCR$30, 0);
            return target;
        }
    }
    
    public void setOtherDescr(final String otherDescr) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PhysicalExaminationTypeImpl.OTHERDESCR$30, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(PhysicalExaminationTypeImpl.OTHERDESCR$30);
            }
            target.setStringValue(otherDescr);
        }
    }
    
    public void xsetOtherDescr(final XmlString otherDescr) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(PhysicalExaminationTypeImpl.OTHERDESCR$30, 0);
            if (target == null) {
                target = (XmlString)this.get_store().add_element_user(PhysicalExaminationTypeImpl.OTHERDESCR$30);
            }
            target.set((XmlObject)otherDescr);
        }
    }
    
    public NormalAbnormalNullType getOther() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            NormalAbnormalNullType target = null;
            target = (NormalAbnormalNullType)this.get_store().find_element_user(PhysicalExaminationTypeImpl.OTHER$32, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setOther(final NormalAbnormalNullType other) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            NormalAbnormalNullType target = null;
            target = (NormalAbnormalNullType)this.get_store().find_element_user(PhysicalExaminationTypeImpl.OTHER$32, 0);
            if (target == null) {
                target = (NormalAbnormalNullType)this.get_store().add_element_user(PhysicalExaminationTypeImpl.OTHER$32);
            }
            target.set((XmlObject)other);
        }
    }
    
    public NormalAbnormalNullType addNewOther() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            NormalAbnormalNullType target = null;
            target = (NormalAbnormalNullType)this.get_store().add_element_user(PhysicalExaminationTypeImpl.OTHER$32);
            return target;
        }
    }
    
    static {
        HEIGHT$0 = new QName("http://www.oscarmcmaster.org/AR2005", "height");
        WEIGHT$2 = new QName("http://www.oscarmcmaster.org/AR2005", "weight");
        BMI$4 = new QName("http://www.oscarmcmaster.org/AR2005", "bmi");
        BP$6 = new QName("http://www.oscarmcmaster.org/AR2005", "bp");
        THYROID$8 = new QName("http://www.oscarmcmaster.org/AR2005", "thyroid");
        CHEST$10 = new QName("http://www.oscarmcmaster.org/AR2005", "chest");
        BREASTS$12 = new QName("http://www.oscarmcmaster.org/AR2005", "breasts");
        CARDIOVASCULAR$14 = new QName("http://www.oscarmcmaster.org/AR2005", "cardiovascular");
        ABDOMEN$16 = new QName("http://www.oscarmcmaster.org/AR2005", "abdomen");
        VARICOSITIES$18 = new QName("http://www.oscarmcmaster.org/AR2005", "varicosities");
        EXERNALGENITALS$20 = new QName("http://www.oscarmcmaster.org/AR2005", "exernalGenitals");
        CERVIXVAGINA$22 = new QName("http://www.oscarmcmaster.org/AR2005", "cervixVagina");
        UTERUS$24 = new QName("http://www.oscarmcmaster.org/AR2005", "uterus");
        UTERUSSIZE$26 = new QName("http://www.oscarmcmaster.org/AR2005", "uterusSize");
        ADNEXA$28 = new QName("http://www.oscarmcmaster.org/AR2005", "adnexa");
        OTHERDESCR$30 = new QName("http://www.oscarmcmaster.org/AR2005", "otherDescr");
        OTHER$32 = new QName("http://www.oscarmcmaster.org/AR2005", "other");
    }
    
    public static class HeightImpl extends JavaFloatHolderEx implements Height
    {
        private static final long serialVersionUID = 1L;
        
        public HeightImpl(final SchemaType sType) {
            super(sType, false);
        }
        
        protected HeightImpl(final SchemaType sType, final boolean b) {
            super(sType, b);
        }
    }
    
    public static class WeightImpl extends JavaFloatHolderEx implements Weight
    {
        private static final long serialVersionUID = 1L;
        
        public WeightImpl(final SchemaType sType) {
            super(sType, false);
        }
        
        protected WeightImpl(final SchemaType sType, final boolean b) {
            super(sType, b);
        }
    }
    
    public static class BmiImpl extends JavaFloatHolderEx implements Bmi
    {
        private static final long serialVersionUID = 1L;
        
        public BmiImpl(final SchemaType sType) {
            super(sType, false);
        }
        
        protected BmiImpl(final SchemaType sType, final boolean b) {
            super(sType, b);
        }
    }
    
    public static class BpImpl extends JavaStringHolderEx implements Bp
    {
        private static final long serialVersionUID = 1L;
        
        public BpImpl(final SchemaType sType) {
            super(sType, false);
        }
        
        protected BpImpl(final SchemaType sType, final boolean b) {
            super(sType, b);
        }
    }
}
