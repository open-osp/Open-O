package ca.openosp.openo.ar2005.impl;

import ca.openosp.openo.ar2005.AR2;
import org.apache.xmlbeans.XmlObject;
import ca.openosp.openo.ar2005.AR1;
import org.apache.xmlbeans.SchemaType;
import javax.xml.namespace.QName;
import ca.openosp.openo.ar2005.ARRecord;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

public class ARRecordImpl extends XmlComplexContentImpl implements ARRecord
{
    private static final long serialVersionUID = 1L;
    private static final QName AR1$0;
    private static final QName AR2$2;
    
    public ARRecordImpl(final SchemaType sType) {
        super(sType);
    }
    
    public AR1 getAR1() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            AR1 target = null;
            target = (AR1)this.get_store().find_element_user(ARRecordImpl.AR1$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setAR1(final AR1 ar1) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            AR1 target = null;
            target = (AR1)this.get_store().find_element_user(ARRecordImpl.AR1$0, 0);
            if (target == null) {
                target = (AR1)this.get_store().add_element_user(ARRecordImpl.AR1$0);
            }
            target.set((XmlObject)ar1);
        }
    }
    
    public AR1 addNewAR1() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            AR1 target = null;
            target = (AR1)this.get_store().add_element_user(ARRecordImpl.AR1$0);
            return target;
        }
    }
    
    public AR2 getAR2() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            AR2 target = null;
            target = (AR2)this.get_store().find_element_user(ARRecordImpl.AR2$2, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setAR2(final AR2 ar2) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            AR2 target = null;
            target = (AR2)this.get_store().find_element_user(ARRecordImpl.AR2$2, 0);
            if (target == null) {
                target = (AR2)this.get_store().add_element_user(ARRecordImpl.AR2$2);
            }
            target.set((XmlObject)ar2);
        }
    }
    
    public AR2 addNewAR2() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            AR2 target = null;
            target = (AR2)this.get_store().add_element_user(ARRecordImpl.AR2$2);
            return target;
        }
    }
    
    static {
        AR1$0 = new QName("http://www.oscarmcmaster.org/AR2005", "AR1");
        AR2$2 = new QName("http://www.oscarmcmaster.org/AR2005", "AR2");
    }
}
