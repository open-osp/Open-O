package ca.openosp.openo.ar2005.impl;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.SchemaType;
import javax.xml.namespace.QName;
import ca.openosp.openo.ar2005.NormalAbnormalNullType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

public class NormalAbnormalNullTypeImpl extends XmlComplexContentImpl implements NormalAbnormalNullType
{
    private static final long serialVersionUID = 1L;
    private static final QName NORMAL$0;
    private static final QName ABNORMAL$2;
    private static final QName NULL$4;
    
    public NormalAbnormalNullTypeImpl(final SchemaType sType) {
        super(sType);
    }
    
    public boolean getNormal() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(NormalAbnormalNullTypeImpl.NORMAL$0, 0);
            return target != null && target.getBooleanValue();
        }
    }
    
    public XmlBoolean xgetNormal() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(NormalAbnormalNullTypeImpl.NORMAL$0, 0);
            return target;
        }
    }
    
    public boolean isSetNormal() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            return this.get_store().count_elements(NormalAbnormalNullTypeImpl.NORMAL$0) != 0;
        }
    }
    
    public void setNormal(final boolean normal) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(NormalAbnormalNullTypeImpl.NORMAL$0, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(NormalAbnormalNullTypeImpl.NORMAL$0);
            }
            target.setBooleanValue(normal);
        }
    }
    
    public void xsetNormal(final XmlBoolean normal) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(NormalAbnormalNullTypeImpl.NORMAL$0, 0);
            if (target == null) {
                target = (XmlBoolean)this.get_store().add_element_user(NormalAbnormalNullTypeImpl.NORMAL$0);
            }
            target.set((XmlObject)normal);
        }
    }
    
    public void unsetNormal() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            this.get_store().remove_element(NormalAbnormalNullTypeImpl.NORMAL$0, 0);
        }
    }
    
    public boolean getAbnormal() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(NormalAbnormalNullTypeImpl.ABNORMAL$2, 0);
            return target != null && target.getBooleanValue();
        }
    }
    
    public XmlBoolean xgetAbnormal() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(NormalAbnormalNullTypeImpl.ABNORMAL$2, 0);
            return target;
        }
    }
    
    public boolean isSetAbnormal() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            return this.get_store().count_elements(NormalAbnormalNullTypeImpl.ABNORMAL$2) != 0;
        }
    }
    
    public void setAbnormal(final boolean abnormal) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(NormalAbnormalNullTypeImpl.ABNORMAL$2, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(NormalAbnormalNullTypeImpl.ABNORMAL$2);
            }
            target.setBooleanValue(abnormal);
        }
    }
    
    public void xsetAbnormal(final XmlBoolean abnormal) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(NormalAbnormalNullTypeImpl.ABNORMAL$2, 0);
            if (target == null) {
                target = (XmlBoolean)this.get_store().add_element_user(NormalAbnormalNullTypeImpl.ABNORMAL$2);
            }
            target.set((XmlObject)abnormal);
        }
    }
    
    public void unsetAbnormal() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            this.get_store().remove_element(NormalAbnormalNullTypeImpl.ABNORMAL$2, 0);
        }
    }
    
    public boolean getNull() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(NormalAbnormalNullTypeImpl.NULL$4, 0);
            return target != null && target.getBooleanValue();
        }
    }
    
    public XmlBoolean xgetNull() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(NormalAbnormalNullTypeImpl.NULL$4, 0);
            return target;
        }
    }
    
    public boolean isSetNull() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            return this.get_store().count_elements(NormalAbnormalNullTypeImpl.NULL$4) != 0;
        }
    }
    
    public void setNull(final boolean xnull) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(NormalAbnormalNullTypeImpl.NULL$4, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(NormalAbnormalNullTypeImpl.NULL$4);
            }
            target.setBooleanValue(xnull);
        }
    }
    
    public void xsetNull(final XmlBoolean xnull) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(NormalAbnormalNullTypeImpl.NULL$4, 0);
            if (target == null) {
                target = (XmlBoolean)this.get_store().add_element_user(NormalAbnormalNullTypeImpl.NULL$4);
            }
            target.set((XmlObject)xnull);
        }
    }
    
    public void unsetNull() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            this.get_store().remove_element(NormalAbnormalNullTypeImpl.NULL$4, 0);
        }
    }
    
    static {
        NORMAL$0 = new QName("http://www.oscarmcmaster.org/AR2005", "normal");
        ABNORMAL$2 = new QName("http://www.oscarmcmaster.org/AR2005", "abnormal");
        NULL$4 = new QName("http://www.oscarmcmaster.org/AR2005", "null");
    }
}
