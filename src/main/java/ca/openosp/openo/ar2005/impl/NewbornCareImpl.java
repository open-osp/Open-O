package ca.openosp.openo.ar2005.impl;

import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.SchemaType;
import javax.xml.namespace.QName;
import ca.openosp.openo.ar2005.NewbornCare;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

public class NewbornCareImpl extends XmlComplexContentImpl implements NewbornCare
{
    private static final long serialVersionUID = 1L;
    private static final QName PED$0;
    private static final QName FP$2;
    private static final QName MIDWIFE$4;
    private static final QName OTHER$6;
    
    public NewbornCareImpl(final SchemaType sType) {
        super(sType);
    }
    
    public boolean getPed() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(NewbornCareImpl.PED$0, 0);
            return target != null && target.getBooleanValue();
        }
    }
    
    public XmlBoolean xgetPed() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(NewbornCareImpl.PED$0, 0);
            return target;
        }
    }
    
    public void setPed(final boolean ped) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(NewbornCareImpl.PED$0, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(NewbornCareImpl.PED$0);
            }
            target.setBooleanValue(ped);
        }
    }
    
    public void xsetPed(final XmlBoolean ped) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(NewbornCareImpl.PED$0, 0);
            if (target == null) {
                target = (XmlBoolean)this.get_store().add_element_user(NewbornCareImpl.PED$0);
            }
            target.set((XmlObject)ped);
        }
    }
    
    public boolean getFP() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(NewbornCareImpl.FP$2, 0);
            return target != null && target.getBooleanValue();
        }
    }
    
    public XmlBoolean xgetFP() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(NewbornCareImpl.FP$2, 0);
            return target;
        }
    }
    
    public void setFP(final boolean fp) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(NewbornCareImpl.FP$2, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(NewbornCareImpl.FP$2);
            }
            target.setBooleanValue(fp);
        }
    }
    
    public void xsetFP(final XmlBoolean fp) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(NewbornCareImpl.FP$2, 0);
            if (target == null) {
                target = (XmlBoolean)this.get_store().add_element_user(NewbornCareImpl.FP$2);
            }
            target.set((XmlObject)fp);
        }
    }
    
    public boolean getMidwife() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(NewbornCareImpl.MIDWIFE$4, 0);
            return target != null && target.getBooleanValue();
        }
    }
    
    public XmlBoolean xgetMidwife() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(NewbornCareImpl.MIDWIFE$4, 0);
            return target;
        }
    }
    
    public void setMidwife(final boolean midwife) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(NewbornCareImpl.MIDWIFE$4, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(NewbornCareImpl.MIDWIFE$4);
            }
            target.setBooleanValue(midwife);
        }
    }
    
    public void xsetMidwife(final XmlBoolean midwife) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(NewbornCareImpl.MIDWIFE$4, 0);
            if (target == null) {
                target = (XmlBoolean)this.get_store().add_element_user(NewbornCareImpl.MIDWIFE$4);
            }
            target.set((XmlObject)midwife);
        }
    }
    
    public String getOther() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(NewbornCareImpl.OTHER$6, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public XmlString xgetOther() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(NewbornCareImpl.OTHER$6, 0);
            return target;
        }
    }
    
    public void setOther(final String other) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(NewbornCareImpl.OTHER$6, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(NewbornCareImpl.OTHER$6);
            }
            target.setStringValue(other);
        }
    }
    
    public void xsetOther(final XmlString other) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(NewbornCareImpl.OTHER$6, 0);
            if (target == null) {
                target = (XmlString)this.get_store().add_element_user(NewbornCareImpl.OTHER$6);
            }
            target.set((XmlObject)other);
        }
    }
    
    static {
        PED$0 = new QName("http://www.oscarmcmaster.org/AR2005", "Ped");
        FP$2 = new QName("http://www.oscarmcmaster.org/AR2005", "FP");
        MIDWIFE$4 = new QName("http://www.oscarmcmaster.org/AR2005", "Midwife");
        OTHER$6 = new QName("http://www.oscarmcmaster.org/AR2005", "Other");
    }
}
