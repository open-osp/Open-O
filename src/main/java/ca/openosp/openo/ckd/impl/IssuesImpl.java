package ca.openosp.openo.ckd.impl;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlString;
import java.util.List;
import org.apache.xmlbeans.SimpleValue;
import java.util.ArrayList;
import org.apache.xmlbeans.SchemaType;
import javax.xml.namespace.QName;
import ca.openosp.openo.ckd.Issues;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

public class IssuesImpl extends XmlComplexContentImpl implements Issues
{
    private static final long serialVersionUID = 1L;
    private static final QName ISSUE$0;
    
    public IssuesImpl(final SchemaType sType) {
        super(sType);
    }
    
    public String[] getIssueArray() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            final List targetList = new ArrayList();
            this.get_store().find_all_element_users(IssuesImpl.ISSUE$0, targetList);
            final String[] result = new String[targetList.size()];
            for (int i = 0, len = targetList.size(); i < len; ++i) {
                result[i] = ((SimpleValue)targetList.get(i)).getStringValue();
            }
            return result;
        }
    }
    
    public String getIssueArray(final int i) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(IssuesImpl.ISSUE$0, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target.getStringValue();
        }
    }
    
    public XmlString[] xgetIssueArray() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            final List targetList = new ArrayList();
            this.get_store().find_all_element_users(IssuesImpl.ISSUE$0, targetList);
            final XmlString[] result = new XmlString[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    public XmlString xgetIssueArray(final int i) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(IssuesImpl.ISSUE$0, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    public int sizeOfIssueArray() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            return this.get_store().count_elements(IssuesImpl.ISSUE$0);
        }
    }
    
    public void setIssueArray(final String[] issueArray) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            this.arraySetterHelper(issueArray, IssuesImpl.ISSUE$0);
        }
    }
    
    public void setIssueArray(final int i, final String issue) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(IssuesImpl.ISSUE$0, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.setStringValue(issue);
        }
    }
    
    public void xsetIssueArray(final XmlString[] issueArray) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            this.arraySetterHelper((XmlObject[])issueArray, IssuesImpl.ISSUE$0);
        }
    }
    
    public void xsetIssueArray(final int i, final XmlString issue) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(IssuesImpl.ISSUE$0, i);
            if (target == null) {
                throw new IndexOutOfBoundsException();
            }
            target.set((XmlObject)issue);
        }
    }
    
    public void insertIssue(final int i, final String issue) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            final SimpleValue target = (SimpleValue)this.get_store().insert_element_user(IssuesImpl.ISSUE$0, i);
            target.setStringValue(issue);
        }
    }
    
    public void addIssue(final String issue) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().add_element_user(IssuesImpl.ISSUE$0);
            target.setStringValue(issue);
        }
    }
    
    public XmlString insertNewIssue(final int i) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().insert_element_user(IssuesImpl.ISSUE$0, i);
            return target;
        }
    }
    
    public XmlString addNewIssue() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().add_element_user(IssuesImpl.ISSUE$0);
            return target;
        }
    }
    
    public void removeIssue(final int i) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            this.get_store().remove_element(IssuesImpl.ISSUE$0, i);
        }
    }
    
    static {
        ISSUE$0 = new QName("http://www.oscarmcmaster.org/ckd", "issue");
    }
}
