package ca.openosp.openo.ar2005.impl;

import org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx;
import org.apache.xmlbeans.StringEnumAbstractBase;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.XmlObject;
import ca.openosp.openo.ar2005.YesNoNullType;
import org.apache.xmlbeans.SchemaType;
import javax.xml.namespace.QName;
import ca.openosp.openo.ar2005.CurrentPregnancyType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

public class CurrentPregnancyTypeImpl extends XmlComplexContentImpl implements CurrentPregnancyType
{
    private static final long serialVersionUID = 1L;
    private static final QName BLEEDING$0;
    private static final QName NAUSEA$2;
    private static final QName SMOKING$4;
    private static final QName CIGSPERDAY$6;
    private static final QName ALCOHOLDRUGS$8;
    private static final QName OCCENVRISKS$10;
    private static final QName DIETARYRES$12;
    private static final QName CALCIUMADEQUATE$14;
    private static final QName FOLATE$16;
    
    public CurrentPregnancyTypeImpl(final SchemaType sType) {
        super(sType);
    }
    
    public YesNoNullType getBleeding() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(CurrentPregnancyTypeImpl.BLEEDING$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setBleeding(final YesNoNullType bleeding) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(CurrentPregnancyTypeImpl.BLEEDING$0, 0);
            if (target == null) {
                target = (YesNoNullType)this.get_store().add_element_user(CurrentPregnancyTypeImpl.BLEEDING$0);
            }
            target.set((XmlObject)bleeding);
        }
    }
    
    public YesNoNullType addNewBleeding() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().add_element_user(CurrentPregnancyTypeImpl.BLEEDING$0);
            return target;
        }
    }
    
    public YesNoNullType getNausea() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(CurrentPregnancyTypeImpl.NAUSEA$2, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setNausea(final YesNoNullType nausea) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(CurrentPregnancyTypeImpl.NAUSEA$2, 0);
            if (target == null) {
                target = (YesNoNullType)this.get_store().add_element_user(CurrentPregnancyTypeImpl.NAUSEA$2);
            }
            target.set((XmlObject)nausea);
        }
    }
    
    public YesNoNullType addNewNausea() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().add_element_user(CurrentPregnancyTypeImpl.NAUSEA$2);
            return target;
        }
    }
    
    public YesNoNullType getSmoking() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(CurrentPregnancyTypeImpl.SMOKING$4, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setSmoking(final YesNoNullType smoking) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(CurrentPregnancyTypeImpl.SMOKING$4, 0);
            if (target == null) {
                target = (YesNoNullType)this.get_store().add_element_user(CurrentPregnancyTypeImpl.SMOKING$4);
            }
            target.set((XmlObject)smoking);
        }
    }
    
    public YesNoNullType addNewSmoking() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().add_element_user(CurrentPregnancyTypeImpl.SMOKING$4);
            return target;
        }
    }
    
    public CigsPerDay.Enum getCigsPerDay() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(CurrentPregnancyTypeImpl.CIGSPERDAY$6, 0);
            if (target == null) {
                return null;
            }
            return (CigsPerDay.Enum)target.getEnumValue();
        }
    }
    
    public CigsPerDay xgetCigsPerDay() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            CigsPerDay target = null;
            target = (CigsPerDay)this.get_store().find_element_user(CurrentPregnancyTypeImpl.CIGSPERDAY$6, 0);
            return target;
        }
    }
    
    public void setCigsPerDay(final CigsPerDay.Enum cigsPerDay) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(CurrentPregnancyTypeImpl.CIGSPERDAY$6, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(CurrentPregnancyTypeImpl.CIGSPERDAY$6);
            }
            target.setEnumValue((StringEnumAbstractBase)cigsPerDay);
        }
    }
    
    public void xsetCigsPerDay(final CigsPerDay cigsPerDay) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            CigsPerDay target = null;
            target = (CigsPerDay)this.get_store().find_element_user(CurrentPregnancyTypeImpl.CIGSPERDAY$6, 0);
            if (target == null) {
                target = (CigsPerDay)this.get_store().add_element_user(CurrentPregnancyTypeImpl.CIGSPERDAY$6);
            }
            target.set((XmlObject)cigsPerDay);
        }
    }
    
    public YesNoNullType getAlcoholDrugs() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(CurrentPregnancyTypeImpl.ALCOHOLDRUGS$8, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setAlcoholDrugs(final YesNoNullType alcoholDrugs) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(CurrentPregnancyTypeImpl.ALCOHOLDRUGS$8, 0);
            if (target == null) {
                target = (YesNoNullType)this.get_store().add_element_user(CurrentPregnancyTypeImpl.ALCOHOLDRUGS$8);
            }
            target.set((XmlObject)alcoholDrugs);
        }
    }
    
    public YesNoNullType addNewAlcoholDrugs() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().add_element_user(CurrentPregnancyTypeImpl.ALCOHOLDRUGS$8);
            return target;
        }
    }
    
    public YesNoNullType getOccEnvRisks() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(CurrentPregnancyTypeImpl.OCCENVRISKS$10, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setOccEnvRisks(final YesNoNullType occEnvRisks) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(CurrentPregnancyTypeImpl.OCCENVRISKS$10, 0);
            if (target == null) {
                target = (YesNoNullType)this.get_store().add_element_user(CurrentPregnancyTypeImpl.OCCENVRISKS$10);
            }
            target.set((XmlObject)occEnvRisks);
        }
    }
    
    public YesNoNullType addNewOccEnvRisks() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().add_element_user(CurrentPregnancyTypeImpl.OCCENVRISKS$10);
            return target;
        }
    }
    
    public YesNoNullType getDietaryRes() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(CurrentPregnancyTypeImpl.DIETARYRES$12, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setDietaryRes(final YesNoNullType dietaryRes) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(CurrentPregnancyTypeImpl.DIETARYRES$12, 0);
            if (target == null) {
                target = (YesNoNullType)this.get_store().add_element_user(CurrentPregnancyTypeImpl.DIETARYRES$12);
            }
            target.set((XmlObject)dietaryRes);
        }
    }
    
    public YesNoNullType addNewDietaryRes() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().add_element_user(CurrentPregnancyTypeImpl.DIETARYRES$12);
            return target;
        }
    }
    
    public YesNoNullType getCalciumAdequate() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(CurrentPregnancyTypeImpl.CALCIUMADEQUATE$14, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setCalciumAdequate(final YesNoNullType calciumAdequate) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(CurrentPregnancyTypeImpl.CALCIUMADEQUATE$14, 0);
            if (target == null) {
                target = (YesNoNullType)this.get_store().add_element_user(CurrentPregnancyTypeImpl.CALCIUMADEQUATE$14);
            }
            target.set((XmlObject)calciumAdequate);
        }
    }
    
    public YesNoNullType addNewCalciumAdequate() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().add_element_user(CurrentPregnancyTypeImpl.CALCIUMADEQUATE$14);
            return target;
        }
    }
    
    public YesNoNullType getFolate() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(CurrentPregnancyTypeImpl.FOLATE$16, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setFolate(final YesNoNullType folate) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().find_element_user(CurrentPregnancyTypeImpl.FOLATE$16, 0);
            if (target == null) {
                target = (YesNoNullType)this.get_store().add_element_user(CurrentPregnancyTypeImpl.FOLATE$16);
            }
            target.set((XmlObject)folate);
        }
    }
    
    public YesNoNullType addNewFolate() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            YesNoNullType target = null;
            target = (YesNoNullType)this.get_store().add_element_user(CurrentPregnancyTypeImpl.FOLATE$16);
            return target;
        }
    }
    
    static {
        BLEEDING$0 = new QName("http://www.oscarmcmaster.org/AR2005", "bleeding");
        NAUSEA$2 = new QName("http://www.oscarmcmaster.org/AR2005", "nausea");
        SMOKING$4 = new QName("http://www.oscarmcmaster.org/AR2005", "smoking");
        CIGSPERDAY$6 = new QName("http://www.oscarmcmaster.org/AR2005", "cigsPerDay");
        ALCOHOLDRUGS$8 = new QName("http://www.oscarmcmaster.org/AR2005", "alcoholDrugs");
        OCCENVRISKS$10 = new QName("http://www.oscarmcmaster.org/AR2005", "occEnvRisks");
        DIETARYRES$12 = new QName("http://www.oscarmcmaster.org/AR2005", "dietaryRes");
        CALCIUMADEQUATE$14 = new QName("http://www.oscarmcmaster.org/AR2005", "calciumAdequate");
        FOLATE$16 = new QName("http://www.oscarmcmaster.org/AR2005", "folate");
    }
    
    public static class CigsPerDayImpl extends JavaStringEnumerationHolderEx implements CigsPerDay
    {
        private static final long serialVersionUID = 1L;
        
        public CigsPerDayImpl(final SchemaType sType) {
            super(sType, false);
        }
        
        protected CigsPerDayImpl(final SchemaType sType, final boolean b) {
            super(sType, b);
        }
    }
}
