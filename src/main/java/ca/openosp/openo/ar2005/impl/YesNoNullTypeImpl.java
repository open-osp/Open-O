package ca.openosp.openo.ar2005.impl;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.SchemaType;
import javax.xml.namespace.QName;
import ca.openosp.openo.ar2005.YesNoNullType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

public class YesNoNullTypeImpl extends XmlComplexContentImpl implements YesNoNullType
{
    private static final long serialVersionUID = 1L;
    private static final QName YES$0;
    private static final QName NO$2;
    private static final QName NULL$4;
    
    public YesNoNullTypeImpl(final SchemaType sType) {
        super(sType);
    }
    
    public boolean getYes() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(YesNoNullTypeImpl.YES$0, 0);
            return target != null && target.getBooleanValue();
        }
    }
    
    public XmlBoolean xgetYes() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(YesNoNullTypeImpl.YES$0, 0);
            return target;
        }
    }
    
    public boolean isSetYes() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            return this.get_store().count_elements(YesNoNullTypeImpl.YES$0) != 0;
        }
    }
    
    public void setYes(final boolean yes) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(YesNoNullTypeImpl.YES$0, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(YesNoNullTypeImpl.YES$0);
            }
            target.setBooleanValue(yes);
        }
    }
    
    public void xsetYes(final XmlBoolean yes) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(YesNoNullTypeImpl.YES$0, 0);
            if (target == null) {
                target = (XmlBoolean)this.get_store().add_element_user(YesNoNullTypeImpl.YES$0);
            }
            target.set((XmlObject)yes);
        }
    }
    
    public void unsetYes() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            this.get_store().remove_element(YesNoNullTypeImpl.YES$0, 0);
        }
    }
    
    public boolean getNo() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(YesNoNullTypeImpl.NO$2, 0);
            return target != null && target.getBooleanValue();
        }
    }
    
    public XmlBoolean xgetNo() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(YesNoNullTypeImpl.NO$2, 0);
            return target;
        }
    }
    
    public boolean isSetNo() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            return this.get_store().count_elements(YesNoNullTypeImpl.NO$2) != 0;
        }
    }
    
    public void setNo(final boolean no) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(YesNoNullTypeImpl.NO$2, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(YesNoNullTypeImpl.NO$2);
            }
            target.setBooleanValue(no);
        }
    }
    
    public void xsetNo(final XmlBoolean no) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(YesNoNullTypeImpl.NO$2, 0);
            if (target == null) {
                target = (XmlBoolean)this.get_store().add_element_user(YesNoNullTypeImpl.NO$2);
            }
            target.set((XmlObject)no);
        }
    }
    
    public void unsetNo() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            this.get_store().remove_element(YesNoNullTypeImpl.NO$2, 0);
        }
    }
    
    public boolean getNull() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(YesNoNullTypeImpl.NULL$4, 0);
            return target != null && target.getBooleanValue();
        }
    }
    
    public XmlBoolean xgetNull() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(YesNoNullTypeImpl.NULL$4, 0);
            return target;
        }
    }
    
    public boolean isSetNull() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            return this.get_store().count_elements(YesNoNullTypeImpl.NULL$4) != 0;
        }
    }
    
    public void setNull(final boolean xnull) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(YesNoNullTypeImpl.NULL$4, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(YesNoNullTypeImpl.NULL$4);
            }
            target.setBooleanValue(xnull);
        }
    }
    
    public void xsetNull(final XmlBoolean xnull) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(YesNoNullTypeImpl.NULL$4, 0);
            if (target == null) {
                target = (XmlBoolean)this.get_store().add_element_user(YesNoNullTypeImpl.NULL$4);
            }
            target.set((XmlObject)xnull);
        }
    }
    
    public void unsetNull() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            this.get_store().remove_element(YesNoNullTypeImpl.NULL$4, 0);
        }
    }
    
    static {
        YES$0 = new QName("http://www.oscarmcmaster.org/AR2005", "yes");
        NO$2 = new QName("http://www.oscarmcmaster.org/AR2005", "no");
        NULL$4 = new QName("http://www.oscarmcmaster.org/AR2005", "null");
    }
}
