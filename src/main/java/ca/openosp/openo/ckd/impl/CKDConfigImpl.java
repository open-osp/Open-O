package ca.openosp.openo.ckd.impl;

import ca.openosp.openo.ckd.Excludes;
import ca.openosp.openo.ckd.Drugs;
import ca.openosp.openo.ckd.Hx;
import ca.openosp.openo.ckd.Bp;
import org.apache.xmlbeans.XmlObject;
import ca.openosp.openo.ckd.DxCodes;
import org.apache.xmlbeans.SchemaType;
import javax.xml.namespace.QName;
import ca.openosp.openo.ckd.CKDConfig;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

public class CKDConfigImpl extends XmlComplexContentImpl implements CKDConfig
{
    private static final long serialVersionUID = 1L;
    private static final QName DXCODES$0;
    private static final QName BP$2;
    private static final QName HX$4;
    private static final QName DRUGS$6;
    private static final QName EXCLUDES$8;
    
    public CKDConfigImpl(final SchemaType sType) {
        super(sType);
    }
    
    public DxCodes getDxCodes() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            DxCodes target = null;
            target = (DxCodes)this.get_store().find_element_user(CKDConfigImpl.DXCODES$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setDxCodes(final DxCodes dxCodes) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            DxCodes target = null;
            target = (DxCodes)this.get_store().find_element_user(CKDConfigImpl.DXCODES$0, 0);
            if (target == null) {
                target = (DxCodes)this.get_store().add_element_user(CKDConfigImpl.DXCODES$0);
            }
            target.set((XmlObject)dxCodes);
        }
    }
    
    public DxCodes addNewDxCodes() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            DxCodes target = null;
            target = (DxCodes)this.get_store().add_element_user(CKDConfigImpl.DXCODES$0);
            return target;
        }
    }
    
    public Bp getBp() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Bp target = null;
            target = (Bp)this.get_store().find_element_user(CKDConfigImpl.BP$2, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setBp(final Bp bp) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Bp target = null;
            target = (Bp)this.get_store().find_element_user(CKDConfigImpl.BP$2, 0);
            if (target == null) {
                target = (Bp)this.get_store().add_element_user(CKDConfigImpl.BP$2);
            }
            target.set((XmlObject)bp);
        }
    }
    
    public Bp addNewBp() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Bp target = null;
            target = (Bp)this.get_store().add_element_user(CKDConfigImpl.BP$2);
            return target;
        }
    }
    
    public Hx getHx() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Hx target = null;
            target = (Hx)this.get_store().find_element_user(CKDConfigImpl.HX$4, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setHx(final Hx hx) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Hx target = null;
            target = (Hx)this.get_store().find_element_user(CKDConfigImpl.HX$4, 0);
            if (target == null) {
                target = (Hx)this.get_store().add_element_user(CKDConfigImpl.HX$4);
            }
            target.set((XmlObject)hx);
        }
    }
    
    public Hx addNewHx() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Hx target = null;
            target = (Hx)this.get_store().add_element_user(CKDConfigImpl.HX$4);
            return target;
        }
    }
    
    public Drugs getDrugs() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Drugs target = null;
            target = (Drugs)this.get_store().find_element_user(CKDConfigImpl.DRUGS$6, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public boolean isSetDrugs() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            return this.get_store().count_elements(CKDConfigImpl.DRUGS$6) != 0;
        }
    }
    
    public void setDrugs(final Drugs drugs) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Drugs target = null;
            target = (Drugs)this.get_store().find_element_user(CKDConfigImpl.DRUGS$6, 0);
            if (target == null) {
                target = (Drugs)this.get_store().add_element_user(CKDConfigImpl.DRUGS$6);
            }
            target.set((XmlObject)drugs);
        }
    }
    
    public Drugs addNewDrugs() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Drugs target = null;
            target = (Drugs)this.get_store().add_element_user(CKDConfigImpl.DRUGS$6);
            return target;
        }
    }
    
    public void unsetDrugs() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            this.get_store().remove_element(CKDConfigImpl.DRUGS$6, 0);
        }
    }
    
    public Excludes getExcludes() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Excludes target = null;
            target = (Excludes)this.get_store().find_element_user(CKDConfigImpl.EXCLUDES$8, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public boolean isSetExcludes() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            return this.get_store().count_elements(CKDConfigImpl.EXCLUDES$8) != 0;
        }
    }
    
    public void setExcludes(final Excludes excludes) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Excludes target = null;
            target = (Excludes)this.get_store().find_element_user(CKDConfigImpl.EXCLUDES$8, 0);
            if (target == null) {
                target = (Excludes)this.get_store().add_element_user(CKDConfigImpl.EXCLUDES$8);
            }
            target.set((XmlObject)excludes);
        }
    }
    
    public Excludes addNewExcludes() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Excludes target = null;
            target = (Excludes)this.get_store().add_element_user(CKDConfigImpl.EXCLUDES$8);
            return target;
        }
    }
    
    public void unsetExcludes() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            this.get_store().remove_element(CKDConfigImpl.EXCLUDES$8, 0);
        }
    }
    
    static {
        DXCODES$0 = new QName("http://www.oscarmcmaster.org/ckd", "dx-codes");
        BP$2 = new QName("http://www.oscarmcmaster.org/ckd", "bp");
        HX$4 = new QName("http://www.oscarmcmaster.org/ckd", "hx");
        DRUGS$6 = new QName("http://www.oscarmcmaster.org/ckd", "drugs");
        EXCLUDES$8 = new QName("http://www.oscarmcmaster.org/ckd", "excludes");
    }
}
