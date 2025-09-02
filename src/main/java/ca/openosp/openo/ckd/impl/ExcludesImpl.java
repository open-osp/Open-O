package ca.openosp.openo.ckd.impl;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlString;
import java.util.List;
import org.apache.xmlbeans.SimpleValue;
import java.util.ArrayList;
import org.apache.xmlbeans.SchemaType;
import javax.xml.namespace.QName;
import ca.openosp.openo.ckd.Excludes;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

public class ExcludesImpl extends XmlComplexContentImpl implements Excludes
{
    private static final long serialVersionUID = 1L;
    private static final QName EXCLUDE$0;
    
    public ExcludesImpl(final SchemaType sType) {
        super(sType);
    }
    
    public String[] getExcludeArray() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            final List targetList = new ArrayList();
            this.get_store().find_all_element_users(ExcludesImpl.EXCLUDE$0, targetList);
            final String[] result = new String[targetList.size()];
            for (int i = 0, len = targetList.size(); i < len; ++i) {
                result[i] = ((SimpleValue)targetList.get(i)).getStringValue();
            }
            return result;
        }
    }
    
    public String getExcludeArray(final int i) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(ExcludesImpl.EXCLUDE$0, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }
    
    public XmlString[] xgetExcludeArray() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            final List targetList = new ArrayList();
            this.get_store().find_all_element_users(ExcludesImpl.EXCLUDE$0, targetList);
            final XmlString[] result = new XmlString[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    public XmlString xgetExcludeArray(final int i) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(ExcludesImpl.EXCLUDE$0, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    public int sizeOfExcludeArray() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            return this.get_store().count_elements(ExcludesImpl.EXCLUDE$0);
        }
    }
    
    public void setExcludeArray(final String[] excludeArray) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            this.arraySetterHelper(excludeArray, ExcludesImpl.EXCLUDE$0);
        }
    }
    
    public void setExcludeArray(final int i, final String exclude) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(ExcludesImpl.EXCLUDE$0, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(exclude);
        }
    }
    
    public void xsetExcludeArray(final XmlString[] excludeArray) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            this.arraySetterHelper((XmlObject[])excludeArray, ExcludesImpl.EXCLUDE$0);
        }
    }
    
    public void xsetExcludeArray(final int i, final XmlString exclude) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(ExcludesImpl.EXCLUDE$0, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set((XmlObject)exclude);
        }
    }
    
    public void insertExclude(final int i, final String exclude) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            final SimpleValue target = (SimpleValue)this.get_store().insert_element_user(ExcludesImpl.EXCLUDE$0, i);
            target.setStringValue(exclude);
        }
    }
    
    public void addExclude(final String exclude) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().add_element_user(ExcludesImpl.EXCLUDE$0);
            target.setStringValue(exclude);
        }
    }
    
    public XmlString insertNewExclude(final int i) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().insert_element_user(ExcludesImpl.EXCLUDE$0, i);
            return target;
        }
    }
    
    public XmlString addNewExclude() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().add_element_user(ExcludesImpl.EXCLUDE$0);
            return target;
        }
    }
    
    public void removeExclude(final int i) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            this.get_store().remove_element(ExcludesImpl.EXCLUDE$0, i);
        }
    }
    
    static {
        EXCLUDE$0 = new QName("http://www.oscarmcmaster.org/ckd", "exclude");
    }
}
