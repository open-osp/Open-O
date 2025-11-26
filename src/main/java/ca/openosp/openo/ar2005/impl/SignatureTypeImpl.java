package ca.openosp.openo.ar2005.impl;

import org.apache.xmlbeans.XmlDate;
import java.util.Calendar;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.SchemaType;
import javax.xml.namespace.QName;
import ca.openosp.openo.ar2005.SignatureType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

public class SignatureTypeImpl extends XmlComplexContentImpl implements SignatureType
{
    private static final long serialVersionUID = 1L;
    private static final QName SIGNATURE$0;
    private static final QName DATE$2;
    private static final QName SIGNATURE2$4;
    private static final QName DATE2$6;
    
    public SignatureTypeImpl(final SchemaType sType) {
        super(sType);
    }
    
    public String getSignature() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(SignatureTypeImpl.SIGNATURE$0, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public XmlString xgetSignature() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(SignatureTypeImpl.SIGNATURE$0, 0);
            return target;
        }
    }
    
    public void setSignature(final String signature) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(SignatureTypeImpl.SIGNATURE$0, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(SignatureTypeImpl.SIGNATURE$0);
            }
            target.setStringValue(signature);
        }
    }
    
    public void xsetSignature(final XmlString signature) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(SignatureTypeImpl.SIGNATURE$0, 0);
            if (target == null) {
                target = (XmlString)this.get_store().add_element_user(SignatureTypeImpl.SIGNATURE$0);
            }
            target.set((XmlObject)signature);
        }
    }
    
    public Calendar getDate() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(SignatureTypeImpl.DATE$2, 0);
            if (target == null) {
                return null;
            }
            return target.getCalendarValue();
        }
    }
    
    public XmlDate xgetDate() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlDate target = null;
            target = (XmlDate)this.get_store().find_element_user(SignatureTypeImpl.DATE$2, 0);
            return target;
        }
    }
    
    public void setDate(final Calendar date) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(SignatureTypeImpl.DATE$2, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(SignatureTypeImpl.DATE$2);
            }
            target.setCalendarValue(date);
        }
    }
    
    public void xsetDate(final XmlDate date) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlDate target = null;
            target = (XmlDate)this.get_store().find_element_user(SignatureTypeImpl.DATE$2, 0);
            if (target == null) {
                target = (XmlDate)this.get_store().add_element_user(SignatureTypeImpl.DATE$2);
            }
            target.set((XmlObject)date);
        }
    }
    
    public String getSignature2() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(SignatureTypeImpl.SIGNATURE2$4, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public XmlString xgetSignature2() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(SignatureTypeImpl.SIGNATURE2$4, 0);
            return target;
        }
    }
    
    public boolean isSetSignature2() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            return this.get_store().count_elements(SignatureTypeImpl.SIGNATURE2$4) != 0;
        }
    }
    
    public void setSignature2(final String signature2) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(SignatureTypeImpl.SIGNATURE2$4, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(SignatureTypeImpl.SIGNATURE2$4);
            }
            target.setStringValue(signature2);
        }
    }
    
    public void xsetSignature2(final XmlString signature2) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(SignatureTypeImpl.SIGNATURE2$4, 0);
            if (target == null) {
                target = (XmlString)this.get_store().add_element_user(SignatureTypeImpl.SIGNATURE2$4);
            }
            target.set((XmlObject)signature2);
        }
    }
    
    public void unsetSignature2() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            this.get_store().remove_element(SignatureTypeImpl.SIGNATURE2$4, 0);
        }
    }
    
    public Calendar getDate2() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(SignatureTypeImpl.DATE2$6, 0);
            if (target == null) {
                return null;
            }
            return target.getCalendarValue();
        }
    }
    
    public XmlDate xgetDate2() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlDate target = null;
            target = (XmlDate)this.get_store().find_element_user(SignatureTypeImpl.DATE2$6, 0);
            return target;
        }
    }
    
    public boolean isSetDate2() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            return this.get_store().count_elements(SignatureTypeImpl.DATE2$6) != 0;
        }
    }
    
    public void setDate2(final Calendar date2) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(SignatureTypeImpl.DATE2$6, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(SignatureTypeImpl.DATE2$6);
            }
            target.setCalendarValue(date2);
        }
    }
    
    public void xsetDate2(final XmlDate date2) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlDate target = null;
            target = (XmlDate)this.get_store().find_element_user(SignatureTypeImpl.DATE2$6, 0);
            if (target == null) {
                target = (XmlDate)this.get_store().add_element_user(SignatureTypeImpl.DATE2$6);
            }
            target.set((XmlObject)date2);
        }
    }
    
    public void unsetDate2() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            this.get_store().remove_element(SignatureTypeImpl.DATE2$6, 0);
        }
    }
    
    static {
        SIGNATURE$0 = new QName("http://www.oscarmcmaster.org/AR2005", "signature");
        DATE$2 = new QName("http://www.oscarmcmaster.org/AR2005", "date");
        SIGNATURE2$4 = new QName("http://www.oscarmcmaster.org/AR2005", "signature2");
        DATE2$6 = new QName("http://www.oscarmcmaster.org/AR2005", "date2");
    }
}
