package ca.openosp.openo.ar2005.impl;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.SchemaType;
import javax.xml.namespace.QName;
import ca.openosp.openo.ar2005.RiskFactorItemType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

public class RiskFactorItemTypeImpl extends XmlComplexContentImpl implements RiskFactorItemType
{
    private static final long serialVersionUID = 1L;
    private static final QName RISKFACTOR$0;
    private static final QName PLANOFMANAGEMENT$2;
    
    public RiskFactorItemTypeImpl(final SchemaType sType) {
        super(sType);
    }
    
    public String getRiskFactor() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(RiskFactorItemTypeImpl.RISKFACTOR$0, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public XmlString xgetRiskFactor() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(RiskFactorItemTypeImpl.RISKFACTOR$0, 0);
            return target;
        }
    }
    
    public void setRiskFactor(final String riskFactor) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(RiskFactorItemTypeImpl.RISKFACTOR$0, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(RiskFactorItemTypeImpl.RISKFACTOR$0);
            }
            target.setStringValue(riskFactor);
        }
    }
    
    public void xsetRiskFactor(final XmlString riskFactor) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(RiskFactorItemTypeImpl.RISKFACTOR$0, 0);
            if (target == null) {
                target = (XmlString)this.get_store().add_element_user(RiskFactorItemTypeImpl.RISKFACTOR$0);
            }
            target.set((XmlObject)riskFactor);
        }
    }
    
    public String getPlanOfManagement() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(RiskFactorItemTypeImpl.PLANOFMANAGEMENT$2, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public XmlString xgetPlanOfManagement() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(RiskFactorItemTypeImpl.PLANOFMANAGEMENT$2, 0);
            return target;
        }
    }
    
    public void setPlanOfManagement(final String planOfManagement) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(RiskFactorItemTypeImpl.PLANOFMANAGEMENT$2, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(RiskFactorItemTypeImpl.PLANOFMANAGEMENT$2);
            }
            target.setStringValue(planOfManagement);
        }
    }
    
    public void xsetPlanOfManagement(final XmlString planOfManagement) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(RiskFactorItemTypeImpl.PLANOFMANAGEMENT$2, 0);
            if (target == null) {
                target = (XmlString)this.get_store().add_element_user(RiskFactorItemTypeImpl.PLANOFMANAGEMENT$2);
            }
            target.set((XmlObject)planOfManagement);
        }
    }
    
    static {
        RISKFACTOR$0 = new QName("http://www.oscarmcmaster.org/AR2005", "riskFactor");
        PLANOFMANAGEMENT$2 = new QName("http://www.oscarmcmaster.org/AR2005", "planOfManagement");
    }
}
