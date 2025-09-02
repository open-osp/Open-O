package ca.openosp.openo.ckd.impl;

import ca.openosp.openo.ckd.SearchText;
import org.apache.xmlbeans.XmlObject;
import ca.openosp.openo.ckd.Issues;
import org.apache.xmlbeans.SchemaType;
import javax.xml.namespace.QName;
import ca.openosp.openo.ckd.Hx;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

public class HxImpl extends XmlComplexContentImpl implements Hx
{
    private static final long serialVersionUID = 1L;
    private static final QName ISSUES$0;
    private static final QName SEARCHTEXT$2;
    
    public HxImpl(final SchemaType sType) {
        super(sType);
    }
    
    public Issues getIssues() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Issues target = null;
            target = (Issues)this.get_store().find_element_user(HxImpl.ISSUES$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setIssues(final Issues issues) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Issues target = null;
            target = (Issues)this.get_store().find_element_user(HxImpl.ISSUES$0, 0);
            if (target == null) {
                target = (Issues)this.get_store().add_element_user(HxImpl.ISSUES$0);
            }
            target.set((XmlObject)issues);
        }
    }
    
    public Issues addNewIssues() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Issues target = null;
            target = (Issues)this.get_store().add_element_user(HxImpl.ISSUES$0);
            return target;
        }
    }
    
    public SearchText getSearchtext() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SearchText target = null;
            target = (SearchText)this.get_store().find_element_user(HxImpl.SEARCHTEXT$2, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setSearchtext(final SearchText searchtext) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SearchText target = null;
            target = (SearchText)this.get_store().find_element_user(HxImpl.SEARCHTEXT$2, 0);
            if (target == null) {
                target = (SearchText)this.get_store().add_element_user(HxImpl.SEARCHTEXT$2);
            }
            target.set((XmlObject)searchtext);
        }
    }
    
    public SearchText addNewSearchtext() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SearchText target = null;
            target = (SearchText)this.get_store().add_element_user(HxImpl.SEARCHTEXT$2);
            return target;
        }
    }
    
    static {
        ISSUES$0 = new QName("http://www.oscarmcmaster.org/ckd", "issues");
        SEARCHTEXT$2 = new QName("http://www.oscarmcmaster.org/ckd", "searchtext");
    }
}
