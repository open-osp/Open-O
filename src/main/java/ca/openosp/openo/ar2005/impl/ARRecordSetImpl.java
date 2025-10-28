package ca.openosp.openo.ar2005.impl;

import org.apache.xmlbeans.XmlObject;
import java.util.List;
import java.util.ArrayList;
import ca.openosp.openo.ar2005.ARRecord;
import org.apache.xmlbeans.SchemaType;
import javax.xml.namespace.QName;
import ca.openosp.openo.ar2005.ARRecordSet;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

public class ARRecordSetImpl extends XmlComplexContentImpl implements ARRecordSet
{
    private static final long serialVersionUID = 1L;
    private static final QName ARRECORD$0;
    
    public ARRecordSetImpl(final SchemaType sType) {
        super(sType);
    }
    
    public ARRecord[] getARRecordArray() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            final List targetList = new ArrayList();
            this.get_store().find_all_element_users(ARRecordSetImpl.ARRECORD$0, targetList);
            final ARRecord[] result = new ARRecord[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    public ARRecord getARRecordArray(final int i) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            ARRecord target = null;
            target = (ARRecord)this.get_store().find_element_user(ARRecordSetImpl.ARRECORD$0, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    public int sizeOfARRecordArray() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            return this.get_store().count_elements(ARRecordSetImpl.ARRECORD$0);
        }
    }
    
    public void setARRecordArray(final ARRecord[] arRecordArray) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            this.arraySetterHelper((XmlObject[])arRecordArray, ARRecordSetImpl.ARRECORD$0);
        }
    }
    
    public void setARRecordArray(final int i, final ARRecord arRecord) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            ARRecord target = null;
            target = (ARRecord)this.get_store().find_element_user(ARRecordSetImpl.ARRECORD$0, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set((XmlObject)arRecord);
        }
    }
    
    public ARRecord insertNewARRecord(final int i) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            ARRecord target = null;
            target = (ARRecord)this.get_store().insert_element_user(ARRecordSetImpl.ARRECORD$0, i);
            return target;
        }
    }
    
    public ARRecord addNewARRecord() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            ARRecord target = null;
            target = (ARRecord)this.get_store().add_element_user(ARRecordSetImpl.ARRECORD$0);
            return target;
        }
    }
    
    public void removeARRecord(final int i) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            this.get_store().remove_element(ARRecordSetImpl.ARRECORD$0, i);
        }
    }
    
    static {
        ARRECORD$0 = new QName("http://www.oscarmcmaster.org/AR2005", "ARRecord");
    }
}
