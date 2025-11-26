package ca.openosp.openo.ar2005.impl;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.SchemaType;
import javax.xml.namespace.QName;
import ca.openosp.openo.ar2005.CustomLab;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

public class CustomLabImpl extends XmlComplexContentImpl implements CustomLab
{
    private static final long serialVersionUID = 1L;
    private static final QName LABEL$0;
    private static final QName RESULT$2;
    
    public CustomLabImpl(final SchemaType sType) {
        super(sType);
    }
    
    public String getLabel() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(CustomLabImpl.LABEL$0, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public XmlString xgetLabel() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(CustomLabImpl.LABEL$0, 0);
            return target;
        }
    }
    
    public void setLabel(final String label) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(CustomLabImpl.LABEL$0, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(CustomLabImpl.LABEL$0);
            }
            target.setStringValue(label);
        }
    }
    
    public void xsetLabel(final XmlString label) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(CustomLabImpl.LABEL$0, 0);
            if (target == null) {
                target = (XmlString)this.get_store().add_element_user(CustomLabImpl.LABEL$0);
            }
            target.set((XmlObject)label);
        }
    }
    
    public String getResult() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(CustomLabImpl.RESULT$2, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public XmlString xgetResult() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(CustomLabImpl.RESULT$2, 0);
            return target;
        }
    }
    
    public void setResult(final String result) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(CustomLabImpl.RESULT$2, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(CustomLabImpl.RESULT$2);
            }
            target.setStringValue(result);
        }
    }
    
    public void xsetResult(final XmlString result) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(CustomLabImpl.RESULT$2, 0);
            if (target == null) {
                target = (XmlString)this.get_store().add_element_user(CustomLabImpl.RESULT$2);
            }
            target.set((XmlObject)result);
        }
    }
    
    static {
        LABEL$0 = new QName("http://www.oscarmcmaster.org/AR2005", "label");
        RESULT$2 = new QName("http://www.oscarmcmaster.org/AR2005", "result");
    }
}
