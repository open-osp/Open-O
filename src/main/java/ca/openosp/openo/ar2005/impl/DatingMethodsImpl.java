package ca.openosp.openo.ar2005.impl;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlBoolean;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.SchemaType;
import javax.xml.namespace.QName;
import ca.openosp.openo.ar2005.DatingMethods;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

public class DatingMethodsImpl extends XmlComplexContentImpl implements DatingMethods
{
    private static final long serialVersionUID = 1L;
    private static final QName DATES$0;
    private static final QName T1US$2;
    private static final QName T2US$4;
    private static final QName ART$6;
    
    public DatingMethodsImpl(final SchemaType sType) {
        super(sType);
    }
    
    public boolean getDates() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DatingMethodsImpl.DATES$0, 0);
            return target != null && target.getBooleanValue();
        }
    }
    
    public XmlBoolean xgetDates() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(DatingMethodsImpl.DATES$0, 0);
            return target;
        }
    }
    
    public void setDates(final boolean dates) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DatingMethodsImpl.DATES$0, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(DatingMethodsImpl.DATES$0);
            }
            target.setBooleanValue(dates);
        }
    }
    
    public void xsetDates(final XmlBoolean dates) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(DatingMethodsImpl.DATES$0, 0);
            if (target == null) {
                target = (XmlBoolean)this.get_store().add_element_user(DatingMethodsImpl.DATES$0);
            }
            target.set((XmlObject)dates);
        }
    }
    
    public boolean getT1US() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DatingMethodsImpl.T1US$2, 0);
            return target != null && target.getBooleanValue();
        }
    }
    
    public XmlBoolean xgetT1US() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(DatingMethodsImpl.T1US$2, 0);
            return target;
        }
    }
    
    public void setT1US(final boolean t1US) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DatingMethodsImpl.T1US$2, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(DatingMethodsImpl.T1US$2);
            }
            target.setBooleanValue(t1US);
        }
    }
    
    public void xsetT1US(final XmlBoolean t1US) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(DatingMethodsImpl.T1US$2, 0);
            if (target == null) {
                target = (XmlBoolean)this.get_store().add_element_user(DatingMethodsImpl.T1US$2);
            }
            target.set((XmlObject)t1US);
        }
    }
    
    public boolean getT2US() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DatingMethodsImpl.T2US$4, 0);
            return target != null && target.getBooleanValue();
        }
    }
    
    public XmlBoolean xgetT2US() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(DatingMethodsImpl.T2US$4, 0);
            return target;
        }
    }
    
    public void setT2US(final boolean t2US) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DatingMethodsImpl.T2US$4, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(DatingMethodsImpl.T2US$4);
            }
            target.setBooleanValue(t2US);
        }
    }
    
    public void xsetT2US(final XmlBoolean t2US) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(DatingMethodsImpl.T2US$4, 0);
            if (target == null) {
                target = (XmlBoolean)this.get_store().add_element_user(DatingMethodsImpl.T2US$4);
            }
            target.set((XmlObject)t2US);
        }
    }
    
    public boolean getArt() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DatingMethodsImpl.ART$6, 0);
            return target != null && target.getBooleanValue();
        }
    }
    
    public XmlBoolean xgetArt() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(DatingMethodsImpl.ART$6, 0);
            return target;
        }
    }
    
    public void setArt(final boolean art) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DatingMethodsImpl.ART$6, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(DatingMethodsImpl.ART$6);
            }
            target.setBooleanValue(art);
        }
    }
    
    public void xsetArt(final XmlBoolean art) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlBoolean target = null;
            target = (XmlBoolean)this.get_store().find_element_user(DatingMethodsImpl.ART$6, 0);
            if (target == null) {
                target = (XmlBoolean)this.get_store().add_element_user(DatingMethodsImpl.ART$6);
            }
            target.set((XmlObject)art);
        }
    }
    
    static {
        DATES$0 = new QName("http://www.oscarmcmaster.org/AR2005", "dates");
        T1US$2 = new QName("http://www.oscarmcmaster.org/AR2005", "t1US");
        T2US$4 = new QName("http://www.oscarmcmaster.org/AR2005", "t2US");
        ART$6 = new QName("http://www.oscarmcmaster.org/AR2005", "art");
    }
}
