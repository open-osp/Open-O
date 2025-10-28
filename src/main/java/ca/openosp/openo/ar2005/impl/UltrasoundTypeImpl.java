package ca.openosp.openo.ar2005.impl;

import org.apache.xmlbeans.impl.values.JavaStringHolderEx;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlDate;
import org.apache.xmlbeans.SimpleValue;
import java.util.Calendar;
import org.apache.xmlbeans.SchemaType;
import javax.xml.namespace.QName;
import ca.openosp.openo.ar2005.UltrasoundType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

public class UltrasoundTypeImpl extends XmlComplexContentImpl implements UltrasoundType
{
    private static final long serialVersionUID = 1L;
    private static final QName DATE$0;
    private static final QName GA$2;
    private static final QName RESULTS$4;
    
    public UltrasoundTypeImpl(final SchemaType sType) {
        super(sType);
    }
    
    public Calendar getDate() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(UltrasoundTypeImpl.DATE$0, 0);
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
            target = (XmlDate)this.get_store().find_element_user(UltrasoundTypeImpl.DATE$0, 0);
            return target;
        }
    }
    
    public void setDate(final Calendar date) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(UltrasoundTypeImpl.DATE$0, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(UltrasoundTypeImpl.DATE$0);
            }
            target.setCalendarValue(date);
        }
    }
    
    public void xsetDate(final XmlDate date) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlDate target = null;
            target = (XmlDate)this.get_store().find_element_user(UltrasoundTypeImpl.DATE$0, 0);
            if (target == null) {
                target = (XmlDate)this.get_store().add_element_user(UltrasoundTypeImpl.DATE$0);
            }
            target.set((XmlObject)date);
        }
    }
    
    public String getGa() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(UltrasoundTypeImpl.GA$2, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public Ga xgetGa() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Ga target = null;
            target = (Ga)this.get_store().find_element_user(UltrasoundTypeImpl.GA$2, 0);
            return target;
        }
    }
    
    public void setGa(final String ga) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(UltrasoundTypeImpl.GA$2, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(UltrasoundTypeImpl.GA$2);
            }
            target.setStringValue(ga);
        }
    }
    
    public void xsetGa(final Ga ga) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Ga target = null;
            target = (Ga)this.get_store().find_element_user(UltrasoundTypeImpl.GA$2, 0);
            if (target == null) {
                target = (Ga)this.get_store().add_element_user(UltrasoundTypeImpl.GA$2);
            }
            target.set((XmlObject)ga);
        }
    }
    
    public String getResults() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(UltrasoundTypeImpl.RESULTS$4, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public XmlString xgetResults() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(UltrasoundTypeImpl.RESULTS$4, 0);
            return target;
        }
    }
    
    public void setResults(final String results) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(UltrasoundTypeImpl.RESULTS$4, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(UltrasoundTypeImpl.RESULTS$4);
            }
            target.setStringValue(results);
        }
    }
    
    public void xsetResults(final XmlString results) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(UltrasoundTypeImpl.RESULTS$4, 0);
            if (target == null) {
                target = (XmlString)this.get_store().add_element_user(UltrasoundTypeImpl.RESULTS$4);
            }
            target.set((XmlObject)results);
        }
    }
    
    static {
        DATE$0 = new QName("http://www.oscarmcmaster.org/AR2005", "date");
        GA$2 = new QName("http://www.oscarmcmaster.org/AR2005", "ga");
        RESULTS$4 = new QName("http://www.oscarmcmaster.org/AR2005", "results");
    }
    
    public static class GaImpl extends JavaStringHolderEx implements Ga
    {
        private static final long serialVersionUID = 1L;
        
        public GaImpl(final SchemaType sType) {
            super(sType, false);
        }
        
        protected GaImpl(final SchemaType sType, final boolean b) {
            super(sType, b);
        }
    }
}
