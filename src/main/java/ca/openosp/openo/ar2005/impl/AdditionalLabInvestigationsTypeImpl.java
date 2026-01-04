package ca.openosp.openo.ar2005.impl;

import org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx;
import ca.openosp.openo.ar2005.CustomLab;
import org.apache.xmlbeans.StringEnumAbstractBase;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.SchemaType;
import javax.xml.namespace.QName;
import ca.openosp.openo.ar2005.AdditionalLabInvestigationsType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

public class AdditionalLabInvestigationsTypeImpl extends XmlComplexContentImpl implements AdditionalLabInvestigationsType
{
    private static final long serialVersionUID = 1L;
    private static final QName HB$0;
    private static final QName BLOODGROUP$2;
    private static final QName RH$4;
    private static final QName REPEATABS$6;
    private static final QName GCT$8;
    private static final QName GTT$10;
    private static final QName GBS$12;
    private static final QName CUSTOMLAB1$14;
    private static final QName CUSTOMLAB2$16;
    private static final QName CUSTOMLAB3$18;
    private static final QName CUSTOMLAB4$20;
    
    public AdditionalLabInvestigationsTypeImpl(final SchemaType sType) {
        super(sType);
    }
    
    public String getHb() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(AdditionalLabInvestigationsTypeImpl.HB$0, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public XmlString xgetHb() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(AdditionalLabInvestigationsTypeImpl.HB$0, 0);
            return target;
        }
    }
    
    public void setHb(final String hb) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(AdditionalLabInvestigationsTypeImpl.HB$0, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(AdditionalLabInvestigationsTypeImpl.HB$0);
            }
            target.setStringValue(hb);
        }
    }
    
    public void xsetHb(final XmlString hb) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(AdditionalLabInvestigationsTypeImpl.HB$0, 0);
            if (target == null) {
                target = (XmlString)this.get_store().add_element_user(AdditionalLabInvestigationsTypeImpl.HB$0);
            }
            target.set((XmlObject)hb);
        }
    }
    
    public BloodGroup.Enum getBloodGroup() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(AdditionalLabInvestigationsTypeImpl.BLOODGROUP$2, 0);
            if (target == null) {
                return null;
            }
            return (BloodGroup.Enum)target.getEnumValue();
        }
    }
    
    public BloodGroup xgetBloodGroup() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            BloodGroup target = null;
            target = (BloodGroup)this.get_store().find_element_user(AdditionalLabInvestigationsTypeImpl.BLOODGROUP$2, 0);
            return target;
        }
    }
    
    public void setBloodGroup(final BloodGroup.Enum bloodGroup) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(AdditionalLabInvestigationsTypeImpl.BLOODGROUP$2, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(AdditionalLabInvestigationsTypeImpl.BLOODGROUP$2);
            }
            target.setEnumValue((StringEnumAbstractBase)bloodGroup);
        }
    }
    
    public void xsetBloodGroup(final BloodGroup bloodGroup) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            BloodGroup target = null;
            target = (BloodGroup)this.get_store().find_element_user(AdditionalLabInvestigationsTypeImpl.BLOODGROUP$2, 0);
            if (target == null) {
                target = (BloodGroup)this.get_store().add_element_user(AdditionalLabInvestigationsTypeImpl.BLOODGROUP$2);
            }
            target.set((XmlObject)bloodGroup);
        }
    }
    
    public Rh.Enum getRh() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(AdditionalLabInvestigationsTypeImpl.RH$4, 0);
            if (target == null) {
                return null;
            }
            return (Rh.Enum)target.getEnumValue();
        }
    }
    
    public Rh xgetRh() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Rh target = null;
            target = (Rh)this.get_store().find_element_user(AdditionalLabInvestigationsTypeImpl.RH$4, 0);
            return target;
        }
    }
    
    public void setRh(final Rh.Enum rh) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(AdditionalLabInvestigationsTypeImpl.RH$4, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(AdditionalLabInvestigationsTypeImpl.RH$4);
            }
            target.setEnumValue((StringEnumAbstractBase)rh);
        }
    }
    
    public void xsetRh(final Rh rh) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Rh target = null;
            target = (Rh)this.get_store().find_element_user(AdditionalLabInvestigationsTypeImpl.RH$4, 0);
            if (target == null) {
                target = (Rh)this.get_store().add_element_user(AdditionalLabInvestigationsTypeImpl.RH$4);
            }
            target.set((XmlObject)rh);
        }
    }
    
    public String getRepeatABS() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(AdditionalLabInvestigationsTypeImpl.REPEATABS$6, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public XmlString xgetRepeatABS() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(AdditionalLabInvestigationsTypeImpl.REPEATABS$6, 0);
            return target;
        }
    }
    
    public void setRepeatABS(final String repeatABS) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(AdditionalLabInvestigationsTypeImpl.REPEATABS$6, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(AdditionalLabInvestigationsTypeImpl.REPEATABS$6);
            }
            target.setStringValue(repeatABS);
        }
    }
    
    public void xsetRepeatABS(final XmlString repeatABS) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(AdditionalLabInvestigationsTypeImpl.REPEATABS$6, 0);
            if (target == null) {
                target = (XmlString)this.get_store().add_element_user(AdditionalLabInvestigationsTypeImpl.REPEATABS$6);
            }
            target.set((XmlObject)repeatABS);
        }
    }
    
    public String getGCT() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(AdditionalLabInvestigationsTypeImpl.GCT$8, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public XmlString xgetGCT() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(AdditionalLabInvestigationsTypeImpl.GCT$8, 0);
            return target;
        }
    }
    
    public void setGCT(final String gct) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(AdditionalLabInvestigationsTypeImpl.GCT$8, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(AdditionalLabInvestigationsTypeImpl.GCT$8);
            }
            target.setStringValue(gct);
        }
    }
    
    public void xsetGCT(final XmlString gct) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(AdditionalLabInvestigationsTypeImpl.GCT$8, 0);
            if (target == null) {
                target = (XmlString)this.get_store().add_element_user(AdditionalLabInvestigationsTypeImpl.GCT$8);
            }
            target.set((XmlObject)gct);
        }
    }
    
    public String getGTT() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(AdditionalLabInvestigationsTypeImpl.GTT$10, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public XmlString xgetGTT() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(AdditionalLabInvestigationsTypeImpl.GTT$10, 0);
            return target;
        }
    }
    
    public void setGTT(final String gtt) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(AdditionalLabInvestigationsTypeImpl.GTT$10, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(AdditionalLabInvestigationsTypeImpl.GTT$10);
            }
            target.setStringValue(gtt);
        }
    }
    
    public void xsetGTT(final XmlString gtt) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(AdditionalLabInvestigationsTypeImpl.GTT$10, 0);
            if (target == null) {
                target = (XmlString)this.get_store().add_element_user(AdditionalLabInvestigationsTypeImpl.GTT$10);
            }
            target.set((XmlObject)gtt);
        }
    }
    
    public GBS.Enum getGBS() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(AdditionalLabInvestigationsTypeImpl.GBS$12, 0);
            if (target == null) {
                return null;
            }
            return (GBS.Enum)target.getEnumValue();
        }
    }
    
    public GBS xgetGBS() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            GBS target = null;
            target = (GBS)this.get_store().find_element_user(AdditionalLabInvestigationsTypeImpl.GBS$12, 0);
            return target;
        }
    }
    
    public void setGBS(final GBS.Enum gbs) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(AdditionalLabInvestigationsTypeImpl.GBS$12, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(AdditionalLabInvestigationsTypeImpl.GBS$12);
            }
            target.setEnumValue((StringEnumAbstractBase)gbs);
        }
    }
    
    public void xsetGBS(final GBS gbs) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            GBS target = null;
            target = (GBS)this.get_store().find_element_user(AdditionalLabInvestigationsTypeImpl.GBS$12, 0);
            if (target == null) {
                target = (GBS)this.get_store().add_element_user(AdditionalLabInvestigationsTypeImpl.GBS$12);
            }
            target.set((XmlObject)gbs);
        }
    }
    
    public CustomLab getCustomLab1() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            CustomLab target = null;
            target = (CustomLab)this.get_store().find_element_user(AdditionalLabInvestigationsTypeImpl.CUSTOMLAB1$14, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setCustomLab1(final CustomLab customLab1) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            CustomLab target = null;
            target = (CustomLab)this.get_store().find_element_user(AdditionalLabInvestigationsTypeImpl.CUSTOMLAB1$14, 0);
            if (target == null) {
                target = (CustomLab)this.get_store().add_element_user(AdditionalLabInvestigationsTypeImpl.CUSTOMLAB1$14);
            }
            target.set((XmlObject)customLab1);
        }
    }
    
    public CustomLab addNewCustomLab1() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            CustomLab target = null;
            target = (CustomLab)this.get_store().add_element_user(AdditionalLabInvestigationsTypeImpl.CUSTOMLAB1$14);
            return target;
        }
    }
    
    public CustomLab getCustomLab2() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            CustomLab target = null;
            target = (CustomLab)this.get_store().find_element_user(AdditionalLabInvestigationsTypeImpl.CUSTOMLAB2$16, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setCustomLab2(final CustomLab customLab2) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            CustomLab target = null;
            target = (CustomLab)this.get_store().find_element_user(AdditionalLabInvestigationsTypeImpl.CUSTOMLAB2$16, 0);
            if (target == null) {
                target = (CustomLab)this.get_store().add_element_user(AdditionalLabInvestigationsTypeImpl.CUSTOMLAB2$16);
            }
            target.set((XmlObject)customLab2);
        }
    }
    
    public CustomLab addNewCustomLab2() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            CustomLab target = null;
            target = (CustomLab)this.get_store().add_element_user(AdditionalLabInvestigationsTypeImpl.CUSTOMLAB2$16);
            return target;
        }
    }
    
    public CustomLab getCustomLab3() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            CustomLab target = null;
            target = (CustomLab)this.get_store().find_element_user(AdditionalLabInvestigationsTypeImpl.CUSTOMLAB3$18, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setCustomLab3(final CustomLab customLab3) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            CustomLab target = null;
            target = (CustomLab)this.get_store().find_element_user(AdditionalLabInvestigationsTypeImpl.CUSTOMLAB3$18, 0);
            if (target == null) {
                target = (CustomLab)this.get_store().add_element_user(AdditionalLabInvestigationsTypeImpl.CUSTOMLAB3$18);
            }
            target.set((XmlObject)customLab3);
        }
    }
    
    public CustomLab addNewCustomLab3() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            CustomLab target = null;
            target = (CustomLab)this.get_store().add_element_user(AdditionalLabInvestigationsTypeImpl.CUSTOMLAB3$18);
            return target;
        }
    }
    
    public CustomLab getCustomLab4() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            CustomLab target = null;
            target = (CustomLab)this.get_store().find_element_user(AdditionalLabInvestigationsTypeImpl.CUSTOMLAB4$20, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setCustomLab4(final CustomLab customLab4) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            CustomLab target = null;
            target = (CustomLab)this.get_store().find_element_user(AdditionalLabInvestigationsTypeImpl.CUSTOMLAB4$20, 0);
            if (target == null) {
                target = (CustomLab)this.get_store().add_element_user(AdditionalLabInvestigationsTypeImpl.CUSTOMLAB4$20);
            }
            target.set((XmlObject)customLab4);
        }
    }
    
    public CustomLab addNewCustomLab4() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            CustomLab target = null;
            target = (CustomLab)this.get_store().add_element_user(AdditionalLabInvestigationsTypeImpl.CUSTOMLAB4$20);
            return target;
        }
    }
    
    static {
        HB$0 = new QName("http://www.oscarmcmaster.org/AR2005", "hb");
        BLOODGROUP$2 = new QName("http://www.oscarmcmaster.org/AR2005", "bloodGroup");
        RH$4 = new QName("http://www.oscarmcmaster.org/AR2005", "rh");
        REPEATABS$6 = new QName("http://www.oscarmcmaster.org/AR2005", "repeatABS");
        GCT$8 = new QName("http://www.oscarmcmaster.org/AR2005", "GCT");
        GTT$10 = new QName("http://www.oscarmcmaster.org/AR2005", "GTT");
        GBS$12 = new QName("http://www.oscarmcmaster.org/AR2005", "GBS");
        CUSTOMLAB1$14 = new QName("http://www.oscarmcmaster.org/AR2005", "customLab1");
        CUSTOMLAB2$16 = new QName("http://www.oscarmcmaster.org/AR2005", "customLab2");
        CUSTOMLAB3$18 = new QName("http://www.oscarmcmaster.org/AR2005", "customLab3");
        CUSTOMLAB4$20 = new QName("http://www.oscarmcmaster.org/AR2005", "customLab4");
    }
    
    public static class BloodGroupImpl extends JavaStringEnumerationHolderEx implements BloodGroup
    {
        private static final long serialVersionUID = 1L;
        
        public BloodGroupImpl(final SchemaType sType) {
            super(sType, false);
        }
        
        protected BloodGroupImpl(final SchemaType sType, final boolean b) {
            super(sType, b);
        }
    }
    
    public static class RhImpl extends JavaStringEnumerationHolderEx implements Rh
    {
        private static final long serialVersionUID = 1L;
        
        public RhImpl(final SchemaType sType) {
            super(sType, false);
        }
        
        protected RhImpl(final SchemaType sType, final boolean b) {
            super(sType, b);
        }
    }
    
    public static class GBSImpl extends JavaStringEnumerationHolderEx implements GBS
    {
        private static final long serialVersionUID = 1L;
        
        public GBSImpl(final SchemaType sType) {
            super(sType, false);
        }
        
        protected GBSImpl(final SchemaType sType, final boolean b) {
            super(sType, b);
        }
    }
}
