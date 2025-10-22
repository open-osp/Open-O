package ca.openosp.openo.ar2005.impl;

import org.apache.xmlbeans.XmlObject;
import ca.openosp.openo.ar2005.ARRecord;
import org.apache.xmlbeans.SchemaType;
import javax.xml.namespace.QName;
import ca.openosp.openo.ar2005.ARRecordDocument;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

public class ARRecordDocumentImpl extends XmlComplexContentImpl implements ARRecordDocument
{
    private static final long serialVersionUID = 1L;
    private static final QName ARRECORD$0;
    
    public ARRecordDocumentImpl(final SchemaType sType) {
        super(sType);
    }
    
    public ARRecord getARRecord() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            ARRecord target = null;
            target = (ARRecord)this.get_store().find_element_user(ARRecordDocumentImpl.ARRECORD$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setARRecord(final ARRecord arRecord) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            ARRecord target = null;
            target = (ARRecord)this.get_store().find_element_user(ARRecordDocumentImpl.ARRECORD$0, 0);
            if (target == null) {
                target = (ARRecord)this.get_store().add_element_user(ARRecordDocumentImpl.ARRECORD$0);
            }
            target.set((XmlObject)arRecord);
        }
    }
    
    public ARRecord addNewARRecord() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            ARRecord target = null;
            target = (ARRecord)this.get_store().add_element_user(ARRecordDocumentImpl.ARRECORD$0);
            return target;
        }
    }
    
    static {
        ARRECORD$0 = new QName("http://www.oscarmcmaster.org/AR2005", "ARRecord");
    }
}
