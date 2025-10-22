package ca.openosp.openo.ar2005.impl;

import org.apache.xmlbeans.XmlObject;
import ca.openosp.openo.ar2005.ARRecordSet;
import org.apache.xmlbeans.SchemaType;
import javax.xml.namespace.QName;
import ca.openosp.openo.ar2005.ARRecordSetDocument;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

public class ARRecordSetDocumentImpl extends XmlComplexContentImpl implements ARRecordSetDocument
{
    private static final long serialVersionUID = 1L;
    private static final QName ARRECORDSET$0;
    
    public ARRecordSetDocumentImpl(final SchemaType sType) {
        super(sType);
    }
    
    public ARRecordSet getARRecordSet() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            ARRecordSet target = null;
            target = (ARRecordSet)this.get_store().find_element_user(ARRecordSetDocumentImpl.ARRECORDSET$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setARRecordSet(final ARRecordSet arRecordSet) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            ARRecordSet target = null;
            target = (ARRecordSet)this.get_store().find_element_user(ARRecordSetDocumentImpl.ARRECORDSET$0, 0);
            if (target == null) {
                target = (ARRecordSet)this.get_store().add_element_user(ARRecordSetDocumentImpl.ARRECORDSET$0);
            }
            target.set((XmlObject)arRecordSet);
        }
    }
    
    public ARRecordSet addNewARRecordSet() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            ARRecordSet target = null;
            target = (ARRecordSet)this.get_store().add_element_user(ARRecordSetDocumentImpl.ARRECORDSET$0);
            return target;
        }
    }
    
    static {
        ARRECORDSET$0 = new QName("http://www.oscarmcmaster.org/AR2005", "ARRecordSet");
    }
}
