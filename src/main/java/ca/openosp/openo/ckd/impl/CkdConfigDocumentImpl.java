package ca.openosp.openo.ckd.impl;

import org.apache.xmlbeans.XmlObject;
import ca.openosp.openo.ckd.CKDConfig;
import org.apache.xmlbeans.SchemaType;
import javax.xml.namespace.QName;
import ca.openosp.openo.ckd.CkdConfigDocument;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

public class CkdConfigDocumentImpl extends XmlComplexContentImpl implements CkdConfigDocument
{
    private static final long serialVersionUID = 1L;
    private static final QName CKDCONFIG$0;
    
    public CkdConfigDocumentImpl(final SchemaType sType) {
        super(sType);
    }
    
    public CKDConfig getCkdConfig() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            CKDConfig target = null;
            target = (CKDConfig)this.get_store().find_element_user(CkdConfigDocumentImpl.CKDCONFIG$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setCkdConfig(final CKDConfig ckdConfig) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            CKDConfig target = null;
            target = (CKDConfig)this.get_store().find_element_user(CkdConfigDocumentImpl.CKDCONFIG$0, 0);
            if (target == null) {
                target = (CKDConfig)this.get_store().add_element_user(CkdConfigDocumentImpl.CKDCONFIG$0);
            }
            target.set((XmlObject)ckdConfig);
        }
    }
    
    public CKDConfig addNewCkdConfig() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            CKDConfig target = null;
            target = (CKDConfig)this.get_store().add_element_user(CkdConfigDocumentImpl.CKDCONFIG$0);
            return target;
        }
    }
    
    static {
        CKDCONFIG$0 = new QName("http://www.oscarmcmaster.org/ckd", "ckd-config");
    }
}
