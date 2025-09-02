package ca.openosp.openo.ckd.impl;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlString;
import java.util.List;
import org.apache.xmlbeans.SimpleValue;
import java.util.ArrayList;
import org.apache.xmlbeans.SchemaType;
import javax.xml.namespace.QName;
import ca.openosp.openo.ckd.Drugs;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

public class DrugsImpl extends XmlComplexContentImpl implements Drugs
{
    private static final long serialVersionUID = 1L;
    private static final QName DRUG$0;
    
    public DrugsImpl(final SchemaType sType) {
        super(sType);
    }
    
    public String[] getDrugArray() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            final List targetList = new ArrayList();
            this.get_store().find_all_element_users(DrugsImpl.DRUG$0, targetList);
            final String[] result = new String[targetList.size()];
            for (int i = 0, len = targetList.size(); i < len; ++i) {
                result[i] = ((SimpleValue)targetList.get(i)).getStringValue();
            }
            return result;
        }
    }
    
    public String getDrugArray(final int i) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DrugsImpl.DRUG$0, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }
    
    public XmlString[] xgetDrugArray() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            final List targetList = new ArrayList();
            this.get_store().find_all_element_users(DrugsImpl.DRUG$0, targetList);
            final XmlString[] result = new XmlString[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    public XmlString xgetDrugArray(final int i) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(DrugsImpl.DRUG$0, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    public int sizeOfDrugArray() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            return this.get_store().count_elements(DrugsImpl.DRUG$0);
        }
    }
    
    public void setDrugArray(final String[] drugArray) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            this.arraySetterHelper(drugArray, DrugsImpl.DRUG$0);
        }
    }
    
    public void setDrugArray(final int i, final String drug) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(DrugsImpl.DRUG$0, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(drug);
        }
    }
    
    public void xsetDrugArray(final XmlString[] drugArray) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            this.arraySetterHelper((XmlObject[])drugArray, DrugsImpl.DRUG$0);
        }
    }
    
    public void xsetDrugArray(final int i, final XmlString drug) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(DrugsImpl.DRUG$0, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set((XmlObject)drug);
        }
    }
    
    public void insertDrug(final int i, final String drug) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            final SimpleValue target = (SimpleValue)this.get_store().insert_element_user(DrugsImpl.DRUG$0, i);
            target.setStringValue(drug);
        }
    }
    
    public void addDrug(final String drug) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().add_element_user(DrugsImpl.DRUG$0);
            target.setStringValue(drug);
        }
    }
    
    public XmlString insertNewDrug(final int i) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().insert_element_user(DrugsImpl.DRUG$0, i);
            return target;
        }
    }
    
    public XmlString addNewDrug() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().add_element_user(DrugsImpl.DRUG$0);
            return target;
        }
    }
    
    public void removeDrug(final int i) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            this.get_store().remove_element(DrugsImpl.DRUG$0, i);
        }
    }
    
    static {
        DRUG$0 = new QName("http://www.oscarmcmaster.org/ckd", "drug");
    }
}
