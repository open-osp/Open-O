package ca.openosp.openo.ckd.impl;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlString;
import java.util.List;
import org.apache.xmlbeans.SimpleValue;
import java.util.ArrayList;
import org.apache.xmlbeans.SchemaType;
import javax.xml.namespace.QName;
import ca.openosp.openo.ckd.SearchText;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

public class SearchTextImpl extends XmlComplexContentImpl implements SearchText
{
    private static final long serialVersionUID = 1L;
    private static final QName TEXT$0;
    
    public SearchTextImpl(final SchemaType sType) {
        super(sType);
    }
    
    public String[] getTextArray() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            final List targetList = new ArrayList();
            this.get_store().find_all_element_users(SearchTextImpl.TEXT$0, targetList);
            final String[] result = new String[targetList.size()];
            for (int i = 0, len = targetList.size(); i < len; ++i) {
                result[i] = ((SimpleValue)targetList.get(i)).getStringValue();
            }
            return result;
        }
    }
    
    public String getTextArray(final int i) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(SearchTextImpl.TEXT$0, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }
    
    public XmlString[] xgetTextArray() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            final List targetList = new ArrayList();
            this.get_store().find_all_element_users(SearchTextImpl.TEXT$0, targetList);
            final XmlString[] result = new XmlString[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    public XmlString xgetTextArray(final int i) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(SearchTextImpl.TEXT$0, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    public int sizeOfTextArray() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            return this.get_store().count_elements(SearchTextImpl.TEXT$0);
        }
    }
    
    public void setTextArray(final String[] textArray) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            this.arraySetterHelper(textArray, SearchTextImpl.TEXT$0);
        }
    }
    
    public void setTextArray(final int i, final String text) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(SearchTextImpl.TEXT$0, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(text);
        }
    }
    
    public void xsetTextArray(final XmlString[] textArray) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            this.arraySetterHelper((XmlObject[])textArray, SearchTextImpl.TEXT$0);
        }
    }
    
    public void xsetTextArray(final int i, final XmlString text) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(SearchTextImpl.TEXT$0, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set((XmlObject)text);
        }
    }
    
    public void insertText(final int i, final String text) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            final SimpleValue target = (SimpleValue)this.get_store().insert_element_user(SearchTextImpl.TEXT$0, i);
            target.setStringValue(text);
        }
    }
    
    public void addText(final String text) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().add_element_user(SearchTextImpl.TEXT$0);
            target.setStringValue(text);
        }
    }
    
    public XmlString insertNewText(final int i) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().insert_element_user(SearchTextImpl.TEXT$0, i);
            return target;
        }
    }
    
    public XmlString addNewText() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().add_element_user(SearchTextImpl.TEXT$0);
            return target;
        }
    }
    
    public void removeText(final int i) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            this.get_store().remove_element(SearchTextImpl.TEXT$0, i);
        }
    }
    
    static {
        TEXT$0 = new QName("http://www.oscarmcmaster.org/ckd", "text");
    }
}
