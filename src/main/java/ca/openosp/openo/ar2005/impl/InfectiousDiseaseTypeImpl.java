package ca.openosp.openo.ar2005.impl;

import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlObject;
import ca.openosp.openo.ar2005.YesNoNullType;
import org.apache.xmlbeans.SchemaType;
import javax.xml.namespace.QName;
import ca.openosp.openo.ar2005.InfectiousDiseaseType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

public class InfectiousDiseaseTypeImpl extends XmlComplexContentImpl implements InfectiousDiseaseType
{
    private static final long serialVersionUID = 1L;
    private static final QName VARICELLA$0;
    private static final QName STD$2;
    private static final QName TUBERCULOSIS$4;
    private static final QName OTHERDESCR$6;
    private static final QName OTHER$8;
    
    public InfectiousDiseaseTypeImpl(final SchemaType sType) {
        super(sType);
    }
    
    public YesNoNullType getVaricella() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(InfectiousDiseaseTypeImpl.VARICELLA$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setVaricella(final YesNoNullType varicella) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(InfectiousDiseaseTypeImpl.VARICELLA$0, 0);
            if (target == null) {
                target = (YesNoNullType)this.get_store().add_element_user(InfectiousDiseaseTypeImpl.VARICELLA$0);
            }
            target.set((XmlObject)varicella);
        }
    }
    
    public YesNoNullType addNewVaricella() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().add_element_user(InfectiousDiseaseTypeImpl.VARICELLA$0);
            return target;
        }
    }
    
    public YesNoNullType getStd() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(InfectiousDiseaseTypeImpl.STD$2, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setStd(final YesNoNullType std) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(InfectiousDiseaseTypeImpl.STD$2, 0);
            if (target == null) {
                target = (YesNoNullType)this.get_store().add_element_user(InfectiousDiseaseTypeImpl.STD$2);
            }
            target.set((XmlObject)std);
        }
    }
    
    public YesNoNullType addNewStd() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().add_element_user(InfectiousDiseaseTypeImpl.STD$2);
            return target;
        }
    }
    
    public YesNoNullType getTuberculosis() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(InfectiousDiseaseTypeImpl.TUBERCULOSIS$4, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setTuberculosis(final YesNoNullType tuberculosis) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(InfectiousDiseaseTypeImpl.TUBERCULOSIS$4, 0);
            if (target == null) {
                target = (YesNoNullType)this.get_store().add_element_user(InfectiousDiseaseTypeImpl.TUBERCULOSIS$4);
            }
            target.set((XmlObject)tuberculosis);
        }
    }
    
    public YesNoNullType addNewTuberculosis() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().add_element_user(InfectiousDiseaseTypeImpl.TUBERCULOSIS$4);
            return target;
        }
    }
    
    public String getOtherDescr() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(InfectiousDiseaseTypeImpl.OTHERDESCR$6, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public XmlString xgetOtherDescr() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(InfectiousDiseaseTypeImpl.OTHERDESCR$6, 0);
            return target;
        }
    }
    
    public void setOtherDescr(final String otherDescr) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(InfectiousDiseaseTypeImpl.OTHERDESCR$6, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(InfectiousDiseaseTypeImpl.OTHERDESCR$6);
            }
            target.setStringValue(otherDescr);
        }
    }
    
    public void xsetOtherDescr(final XmlString otherDescr) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(InfectiousDiseaseTypeImpl.OTHERDESCR$6, 0);
            if (target == null) {
                target = (XmlString)this.get_store().add_element_user(InfectiousDiseaseTypeImpl.OTHERDESCR$6);
            }
            target.set((XmlObject)otherDescr);
        }
    }
    
    public YesNoNullType getOther() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(InfectiousDiseaseTypeImpl.OTHER$8, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setOther(final YesNoNullType other) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(InfectiousDiseaseTypeImpl.OTHER$8, 0);
            if (target == null) {
                target = (YesNoNullType)this.get_store().add_element_user(InfectiousDiseaseTypeImpl.OTHER$8);
            }
            target.set((XmlObject)other);
        }
    }
    
    public YesNoNullType addNewOther() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().add_element_user(InfectiousDiseaseTypeImpl.OTHER$8);
            return target;
        }
    }
    
    static {
        VARICELLA$0 = new QName("http://www.oscarmcmaster.org/AR2005", "varicella");
        STD$2 = new QName("http://www.oscarmcmaster.org/AR2005", "std");
        TUBERCULOSIS$4 = new QName("http://www.oscarmcmaster.org/AR2005", "tuberculosis");
        OTHERDESCR$6 = new QName("http://www.oscarmcmaster.org/AR2005", "otherDescr");
        OTHER$8 = new QName("http://www.oscarmcmaster.org/AR2005", "other");
    }
}
