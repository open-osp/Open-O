package ca.openosp.openo.ar2005.impl;

import org.apache.xmlbeans.XmlObject;
import ca.openosp.openo.ar2005.YesNoNullType;
import org.apache.xmlbeans.SchemaType;
import javax.xml.namespace.QName;
import ca.openosp.openo.ar2005.FamilyHistoryType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

public class FamilyHistoryTypeImpl extends XmlComplexContentImpl implements FamilyHistoryType
{
    private static final long serialVersionUID = 1L;
    private static final QName ATRISK$0;
    
    public FamilyHistoryTypeImpl(final SchemaType sType) {
        super(sType);
    }
    
    public YesNoNullType getAtRisk() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(FamilyHistoryTypeImpl.ATRISK$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setAtRisk(final YesNoNullType atRisk) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(FamilyHistoryTypeImpl.ATRISK$0, 0);
            if (target == null) {
                target = (YesNoNullType)this.get_store().add_element_user(FamilyHistoryTypeImpl.ATRISK$0);
            }
            target.set((XmlObject)atRisk);
        }
    }
    
    public YesNoNullType addNewAtRisk() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().add_element_user(FamilyHistoryTypeImpl.ATRISK$0);
            return target;
        }
    }
    
    static {
        ATRISK$0 = new QName("http://www.oscarmcmaster.org/AR2005", "atRisk");
    }
}
