package ca.openosp.openo.ar2005.impl;

import org.apache.xmlbeans.XmlDate;
import java.util.Calendar;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.SchemaType;
import javax.xml.namespace.QName;
import ca.openosp.openo.ar2005.RecommendedImmunoprophylaxisType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

public class RecommendedImmunoprophylaxisTypeImpl extends XmlComplexContentImpl implements RecommendedImmunoprophylaxisType
{
    private static final long serialVersionUID = 1L;
    private static final QName RHNEGATIVE$0;
    private static final QName RHIGGIVEN$2;
    private static final QName RUBELLA$4;
    private static final QName NEWBORNHEPIG$6;
    private static final QName HEPBVACCINE$8;
    
    public RecommendedImmunoprophylaxisTypeImpl(final SchemaType sType) {
        super(sType);
    }
    
    public boolean getRhNegative() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(RecommendedImmunoprophylaxisTypeImpl.RHNEGATIVE$0, 0);
            return target != null && target.getBooleanValue();
        }
    }
    
    public XmlBoolean xgetRhNegative() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(RecommendedImmunoprophylaxisTypeImpl.RHNEGATIVE$0, 0);
            return target;
        }
    }
    
    public void setRhNegative(final boolean rhNegative) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(RecommendedImmunoprophylaxisTypeImpl.RHNEGATIVE$0, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(RecommendedImmunoprophylaxisTypeImpl.RHNEGATIVE$0);
            }
            target.setBooleanValue(rhNegative);
        }
    }
    
    public void xsetRhNegative(final XmlBoolean rhNegative) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(RecommendedImmunoprophylaxisTypeImpl.RHNEGATIVE$0, 0);
            if (target == null) {
                target = (XmlBoolean)this.get_store().add_element_user(RecommendedImmunoprophylaxisTypeImpl.RHNEGATIVE$0);
            }
            target.set((XmlObject)rhNegative);
        }
    }
    
    public Calendar getRhIgGiven() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(RecommendedImmunoprophylaxisTypeImpl.RHIGGIVEN$2, 0);
            if (target == null) {
                return null;
            }
            return target.getCalendarValue();
        }
    }
    
    public XmlDate xgetRhIgGiven() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlDate target = null;
            target = (XmlDate)this.get_store().find_element_user(RecommendedImmunoprophylaxisTypeImpl.RHIGGIVEN$2, 0);
            return target;
        }
    }
    
    public boolean isNilRhIgGiven() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlDate target = null;
            target = (XmlDate)this.get_store().find_element_user(RecommendedImmunoprophylaxisTypeImpl.RHIGGIVEN$2, 0);
            return target != null && target.isNil();
        }
    }
    
    public void setRhIgGiven(final Calendar rhIgGiven) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(RecommendedImmunoprophylaxisTypeImpl.RHIGGIVEN$2, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(RecommendedImmunoprophylaxisTypeImpl.RHIGGIVEN$2);
            }
            target.setCalendarValue(rhIgGiven);
        }
    }
    
    public void xsetRhIgGiven(final XmlDate rhIgGiven) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlDate target = null;
            target = (XmlDate)this.get_store().find_element_user(RecommendedImmunoprophylaxisTypeImpl.RHIGGIVEN$2, 0);
            if (target == null) {
                target = (XmlDate)this.get_store().add_element_user(RecommendedImmunoprophylaxisTypeImpl.RHIGGIVEN$2);
            }
            target.set((XmlObject)rhIgGiven);
        }
    }
    
    public void setNilRhIgGiven() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlDate target = null;
            target = (XmlDate)this.get_store().find_element_user(RecommendedImmunoprophylaxisTypeImpl.RHIGGIVEN$2, 0);
            if (target == null) {
                target = (XmlDate)this.get_store().add_element_user(RecommendedImmunoprophylaxisTypeImpl.RHIGGIVEN$2);
            }
            target.setNil();
        }
    }
    
    public boolean getRubella() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(RecommendedImmunoprophylaxisTypeImpl.RUBELLA$4, 0);
            return target != null && target.getBooleanValue();
        }
    }
    
    public XmlBoolean xgetRubella() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(RecommendedImmunoprophylaxisTypeImpl.RUBELLA$4, 0);
            return target;
        }
    }
    
    public void setRubella(final boolean rubella) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(RecommendedImmunoprophylaxisTypeImpl.RUBELLA$4, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(RecommendedImmunoprophylaxisTypeImpl.RUBELLA$4);
            }
            target.setBooleanValue(rubella);
        }
    }
    
    public void xsetRubella(final XmlBoolean rubella) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(RecommendedImmunoprophylaxisTypeImpl.RUBELLA$4, 0);
            if (target == null) {
                target = (XmlBoolean)this.get_store().add_element_user(RecommendedImmunoprophylaxisTypeImpl.RUBELLA$4);
            }
            target.set((XmlObject)rubella);
        }
    }
    
    public boolean getNewbornHepIG() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(RecommendedImmunoprophylaxisTypeImpl.NEWBORNHEPIG$6, 0);
            return target != null && target.getBooleanValue();
        }
    }
    
    public XmlBoolean xgetNewbornHepIG() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(RecommendedImmunoprophylaxisTypeImpl.NEWBORNHEPIG$6, 0);
            return target;
        }
    }
    
    public void setNewbornHepIG(final boolean newbornHepIG) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(RecommendedImmunoprophylaxisTypeImpl.NEWBORNHEPIG$6, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(RecommendedImmunoprophylaxisTypeImpl.NEWBORNHEPIG$6);
            }
            target.setBooleanValue(newbornHepIG);
        }
    }
    
    public void xsetNewbornHepIG(final XmlBoolean newbornHepIG) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(RecommendedImmunoprophylaxisTypeImpl.NEWBORNHEPIG$6, 0);
            if (target == null) {
                target = (XmlBoolean)this.get_store().add_element_user(RecommendedImmunoprophylaxisTypeImpl.NEWBORNHEPIG$6);
            }
            target.set((XmlObject)newbornHepIG);
        }
    }
    
    public boolean getHepBVaccine() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(RecommendedImmunoprophylaxisTypeImpl.HEPBVACCINE$8, 0);
            return target != null && target.getBooleanValue();
        }
    }
    
    public XmlBoolean xgetHepBVaccine() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(RecommendedImmunoprophylaxisTypeImpl.HEPBVACCINE$8, 0);
            return target;
        }
    }
    
    public void setHepBVaccine(final boolean hepBVaccine) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(RecommendedImmunoprophylaxisTypeImpl.HEPBVACCINE$8, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(RecommendedImmunoprophylaxisTypeImpl.HEPBVACCINE$8);
            }
            target.setBooleanValue(hepBVaccine);
        }
    }
    
    public void xsetHepBVaccine(final XmlBoolean hepBVaccine) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(RecommendedImmunoprophylaxisTypeImpl.HEPBVACCINE$8, 0);
            if (target == null) {
                target = (XmlBoolean)this.get_store().add_element_user(RecommendedImmunoprophylaxisTypeImpl.HEPBVACCINE$8);
            }
            target.set((XmlObject)hepBVaccine);
        }
    }
    
    static {
        RHNEGATIVE$0 = new QName("http://www.oscarmcmaster.org/AR2005", "rhNegative");
        RHIGGIVEN$2 = new QName("http://www.oscarmcmaster.org/AR2005", "rhIgGiven");
        RUBELLA$4 = new QName("http://www.oscarmcmaster.org/AR2005", "rubella");
        NEWBORNHEPIG$6 = new QName("http://www.oscarmcmaster.org/AR2005", "newbornHepIG");
        HEPBVACCINE$8 = new QName("http://www.oscarmcmaster.org/AR2005", "hepBVaccine");
    }
}
