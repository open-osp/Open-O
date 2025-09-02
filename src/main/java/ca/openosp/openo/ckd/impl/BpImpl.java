package ca.openosp.openo.ckd.impl;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.SchemaType;
import javax.xml.namespace.QName;
import ca.openosp.openo.ckd.Bp;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

public class BpImpl extends XmlComplexContentImpl implements Bp
{
    private static final long serialVersionUID = 1L;
    private static final QName SYSTOLIC$0;
    private static final QName DIASTOLIC$2;
    
    public BpImpl(final SchemaType sType) {
        super(sType);
    }
    
    public String getSystolic() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(BpImpl.SYSTOLIC$0, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public XmlString xgetSystolic() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(BpImpl.SYSTOLIC$0, 0);
            return target;
        }
    }
    
    public void setSystolic(final String systolic) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(BpImpl.SYSTOLIC$0, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(BpImpl.SYSTOLIC$0);
            }
            target.setStringValue(systolic);
        }
    }
    
    public void xsetSystolic(final XmlString systolic) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(BpImpl.SYSTOLIC$0, 0);
            if (target == null) {
                target = (XmlString)this.get_store().add_element_user(BpImpl.SYSTOLIC$0);
            }
            target.set((XmlObject)systolic);
        }
    }
    
    public String getDiastolic() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(BpImpl.DIASTOLIC$2, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public XmlString xgetDiastolic() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(BpImpl.DIASTOLIC$2, 0);
            return target;
        }
    }
    
    public void setDiastolic(final String diastolic) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(BpImpl.DIASTOLIC$2, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(BpImpl.DIASTOLIC$2);
            }
            target.setStringValue(diastolic);
        }
    }
    
    public void xsetDiastolic(final XmlString diastolic) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(BpImpl.DIASTOLIC$2, 0);
            if (target == null) {
                target = (XmlString)this.get_store().add_element_user(BpImpl.DIASTOLIC$2);
            }
            target.set((XmlObject)diastolic);
        }
    }
    
    static {
        SYSTOLIC$0 = new QName("http://www.oscarmcmaster.org/ckd", "systolic");
        DIASTOLIC$2 = new QName("http://www.oscarmcmaster.org/ckd", "diastolic");
    }
}
