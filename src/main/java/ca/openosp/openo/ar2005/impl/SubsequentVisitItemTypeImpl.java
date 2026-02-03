package ca.openosp.openo.ar2005.impl;

import org.apache.xmlbeans.impl.values.JavaFloatHolderEx;
import org.apache.xmlbeans.impl.values.JavaStringHolderEx;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlDate;
import org.apache.xmlbeans.SimpleValue;
import java.util.Calendar;
import org.apache.xmlbeans.SchemaType;
import javax.xml.namespace.QName;
import ca.openosp.openo.ar2005.SubsequentVisitItemType;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

public class SubsequentVisitItemTypeImpl extends XmlComplexContentImpl implements SubsequentVisitItemType
{
    private static final long serialVersionUID = 1L;
    private static final QName DATE$0;
    private static final QName GA$2;
    private static final QName WEIGHT$4;
    private static final QName BP$6;
    private static final QName URINEPR$8;
    private static final QName URINEGI$10;
    private static final QName SFH$12;
    private static final QName PRESENTATIONPOSITION$14;
    private static final QName FHRFM$16;
    private static final QName COMMENTS$18;
    
    public SubsequentVisitItemTypeImpl(final SchemaType sType) {
        super(sType);
    }
    
    public Calendar getDate() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(SubsequentVisitItemTypeImpl.DATE$0, 0);
            if (target == null) {
                return null;
            }
            return target.getCalendarValue();
        }
    }
    
    public XmlDate xgetDate() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlDate target = null;
            target = (XmlDate)this.get_store().find_element_user(SubsequentVisitItemTypeImpl.DATE$0, 0);
            return target;
        }
    }
    
    public boolean isNilDate() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlDate target = null;
            target = (XmlDate)this.get_store().find_element_user(SubsequentVisitItemTypeImpl.DATE$0, 0);
            return target != null && target.isNil();
        }
    }
    
    public void setDate(final Calendar date) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(SubsequentVisitItemTypeImpl.DATE$0, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(SubsequentVisitItemTypeImpl.DATE$0);
            }
            target.setCalendarValue(date);
        }
    }
    
    public void xsetDate(final XmlDate date) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlDate target = null;
            target = (XmlDate)this.get_store().find_element_user(SubsequentVisitItemTypeImpl.DATE$0, 0);
            if (target == null) {
                target = (XmlDate)this.get_store().add_element_user(SubsequentVisitItemTypeImpl.DATE$0);
            }
            target.set((XmlObject)date);
        }
    }
    
    public void setNilDate() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlDate target = null;
            target = (XmlDate)this.get_store().find_element_user(SubsequentVisitItemTypeImpl.DATE$0, 0);
            if (target == null) {
                target = (XmlDate)this.get_store().add_element_user(SubsequentVisitItemTypeImpl.DATE$0);
            }
            target.setNil();
        }
    }
    
    public String getGa() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(SubsequentVisitItemTypeImpl.GA$2, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public Ga xgetGa() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Ga target = null;
            target = (Ga)this.get_store().find_element_user(SubsequentVisitItemTypeImpl.GA$2, 0);
            return target;
        }
    }
    
    public void setGa(final String ga) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(SubsequentVisitItemTypeImpl.GA$2, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(SubsequentVisitItemTypeImpl.GA$2);
            }
            target.setStringValue(ga);
        }
    }
    
    public void xsetGa(final Ga ga) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Ga target = null;
            target = (Ga)this.get_store().find_element_user(SubsequentVisitItemTypeImpl.GA$2, 0);
            if (target == null) {
                target = (Ga)this.get_store().add_element_user(SubsequentVisitItemTypeImpl.GA$2);
            }
            target.set((XmlObject)ga);
        }
    }
    
    public float getWeight() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(SubsequentVisitItemTypeImpl.WEIGHT$4, 0);
            if (target == null) {
                return 0.0f;
            }
            return target.getFloatValue();
        }
    }
    
    public Weight xgetWeight() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Weight target = null;
            target = (Weight)this.get_store().find_element_user(SubsequentVisitItemTypeImpl.WEIGHT$4, 0);
            return target;
        }
    }
    
    public boolean isNilWeight() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Weight target = null;
            target = (Weight)this.get_store().find_element_user(SubsequentVisitItemTypeImpl.WEIGHT$4, 0);
            return target != null && target.isNil();
        }
    }
    
    public void setWeight(final float weight) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(SubsequentVisitItemTypeImpl.WEIGHT$4, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(SubsequentVisitItemTypeImpl.WEIGHT$4);
            }
            target.setFloatValue(weight);
        }
    }
    
    public void xsetWeight(final Weight weight) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Weight target = null;
            target = (Weight)this.get_store().find_element_user(SubsequentVisitItemTypeImpl.WEIGHT$4, 0);
            if (target == null) {
                target = (Weight)this.get_store().add_element_user(SubsequentVisitItemTypeImpl.WEIGHT$4);
            }
            target.set((XmlObject)weight);
        }
    }
    
    public void setNilWeight() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Weight target = null;
            target = (Weight)this.get_store().find_element_user(SubsequentVisitItemTypeImpl.WEIGHT$4, 0);
            if (target == null) {
                target = (Weight)this.get_store().add_element_user(SubsequentVisitItemTypeImpl.WEIGHT$4);
            }
            target.setNil();
        }
    }
    
    public String getBp() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(SubsequentVisitItemTypeImpl.BP$6, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public Bp xgetBp() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Bp target = null;
            target = (Bp)this.get_store().find_element_user(SubsequentVisitItemTypeImpl.BP$6, 0);
            return target;
        }
    }
    
    public void setBp(final String bp) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(SubsequentVisitItemTypeImpl.BP$6, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(SubsequentVisitItemTypeImpl.BP$6);
            }
            target.setStringValue(bp);
        }
    }
    
    public void xsetBp(final Bp bp) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Bp target = null;
            target = (Bp)this.get_store().find_element_user(SubsequentVisitItemTypeImpl.BP$6, 0);
            if (target == null) {
                target = (Bp)this.get_store().add_element_user(SubsequentVisitItemTypeImpl.BP$6);
            }
            target.set((XmlObject)bp);
        }
    }
    
    public String getUrinePR() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(SubsequentVisitItemTypeImpl.URINEPR$8, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public XmlString xgetUrinePR() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(SubsequentVisitItemTypeImpl.URINEPR$8, 0);
            return target;
        }
    }
    
    public void setUrinePR(final String urinePR) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(SubsequentVisitItemTypeImpl.URINEPR$8, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(SubsequentVisitItemTypeImpl.URINEPR$8);
            }
            target.setStringValue(urinePR);
        }
    }
    
    public void xsetUrinePR(final XmlString urinePR) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(SubsequentVisitItemTypeImpl.URINEPR$8, 0);
            if (target == null) {
                target = (XmlString)this.get_store().add_element_user(SubsequentVisitItemTypeImpl.URINEPR$8);
            }
            target.set((XmlObject)urinePR);
        }
    }
    
    public String getUrineGI() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(SubsequentVisitItemTypeImpl.URINEGI$10, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public XmlString xgetUrineGI() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(SubsequentVisitItemTypeImpl.URINEGI$10, 0);
            return target;
        }
    }
    
    public void setUrineGI(final String urineGI) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(SubsequentVisitItemTypeImpl.URINEGI$10, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(SubsequentVisitItemTypeImpl.URINEGI$10);
            }
            target.setStringValue(urineGI);
        }
    }
    
    public void xsetUrineGI(final XmlString urineGI) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(SubsequentVisitItemTypeImpl.URINEGI$10, 0);
            if (target == null) {
                target = (XmlString)this.get_store().add_element_user(SubsequentVisitItemTypeImpl.URINEGI$10);
            }
            target.set((XmlObject)urineGI);
        }
    }
    
    public String getSFH() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(SubsequentVisitItemTypeImpl.SFH$12, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public XmlString xgetSFH() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(SubsequentVisitItemTypeImpl.SFH$12, 0);
            return target;
        }
    }
    
    public void setSFH(final String sfh) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(SubsequentVisitItemTypeImpl.SFH$12, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(SubsequentVisitItemTypeImpl.SFH$12);
            }
            target.setStringValue(sfh);
        }
    }
    
    public void xsetSFH(final XmlString sfh) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(SubsequentVisitItemTypeImpl.SFH$12, 0);
            if (target == null) {
                target = (XmlString)this.get_store().add_element_user(SubsequentVisitItemTypeImpl.SFH$12);
            }
            target.set((XmlObject)sfh);
        }
    }
    
    public String getPresentationPosition() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(SubsequentVisitItemTypeImpl.PRESENTATIONPOSITION$14, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public XmlString xgetPresentationPosition() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(SubsequentVisitItemTypeImpl.PRESENTATIONPOSITION$14, 0);
            return target;
        }
    }
    
    public void setPresentationPosition(final String presentationPosition) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(SubsequentVisitItemTypeImpl.PRESENTATIONPOSITION$14, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(SubsequentVisitItemTypeImpl.PRESENTATIONPOSITION$14);
            }
            target.setStringValue(presentationPosition);
        }
    }
    
    public void xsetPresentationPosition(final XmlString presentationPosition) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(SubsequentVisitItemTypeImpl.PRESENTATIONPOSITION$14, 0);
            if (target == null) {
                target = (XmlString)this.get_store().add_element_user(SubsequentVisitItemTypeImpl.PRESENTATIONPOSITION$14);
            }
            target.set((XmlObject)presentationPosition);
        }
    }
    
    public String getFHRFm() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(SubsequentVisitItemTypeImpl.FHRFM$16, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public XmlString xgetFHRFm() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(SubsequentVisitItemTypeImpl.FHRFM$16, 0);
            return target;
        }
    }
    
    public void setFHRFm(final String fhrFm) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(SubsequentVisitItemTypeImpl.FHRFM$16, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(SubsequentVisitItemTypeImpl.FHRFM$16);
            }
            target.setStringValue(fhrFm);
        }
    }
    
    public void xsetFHRFm(final XmlString fhrFm) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(SubsequentVisitItemTypeImpl.FHRFM$16, 0);
            if (target == null) {
                target = (XmlString)this.get_store().add_element_user(SubsequentVisitItemTypeImpl.FHRFM$16);
            }
            target.set((XmlObject)fhrFm);
        }
    }
    
    public String getComments() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(SubsequentVisitItemTypeImpl.COMMENTS$18, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public XmlString xgetComments() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(SubsequentVisitItemTypeImpl.COMMENTS$18, 0);
            return target;
        }
    }
    
    public void setComments(final String comments) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(SubsequentVisitItemTypeImpl.COMMENTS$18, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(SubsequentVisitItemTypeImpl.COMMENTS$18);
            }
            target.setStringValue(comments);
        }
    }
    
    public void xsetComments(final XmlString comments) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(SubsequentVisitItemTypeImpl.COMMENTS$18, 0);
            if (target == null) {
                target = (XmlString)this.get_store().add_element_user(SubsequentVisitItemTypeImpl.COMMENTS$18);
            }
            target.set((XmlObject)comments);
        }
    }
    
    static {
        DATE$0 = new QName("http://www.oscarmcmaster.org/AR2005", "date");
        GA$2 = new QName("http://www.oscarmcmaster.org/AR2005", "ga");
        WEIGHT$4 = new QName("http://www.oscarmcmaster.org/AR2005", "weight");
        BP$6 = new QName("http://www.oscarmcmaster.org/AR2005", "bp");
        URINEPR$8 = new QName("http://www.oscarmcmaster.org/AR2005", "urinePR");
        URINEGI$10 = new QName("http://www.oscarmcmaster.org/AR2005", "urineGI");
        SFH$12 = new QName("http://www.oscarmcmaster.org/AR2005", "SFH");
        PRESENTATIONPOSITION$14 = new QName("http://www.oscarmcmaster.org/AR2005", "presentation_position");
        FHRFM$16 = new QName("http://www.oscarmcmaster.org/AR2005", "FHR_fm");
        COMMENTS$18 = new QName("http://www.oscarmcmaster.org/AR2005", "comments");
    }
    
    public static class GaImpl extends JavaStringHolderEx implements Ga
    {
        private static final long serialVersionUID = 1L;
        
        public GaImpl(final SchemaType sType) {
            super(sType, false);
        }
        
        protected GaImpl(final SchemaType sType, final boolean b) {
            super(sType, b);
        }
    }
    
    public static class WeightImpl extends JavaFloatHolderEx implements Weight
    {
        private static final long serialVersionUID = 1L;
        
        public WeightImpl(final SchemaType sType) {
            super(sType, false);
        }
        
        protected WeightImpl(final SchemaType sType, final boolean b) {
            super(sType, b);
        }
    }
    
    public static class BpImpl extends JavaStringHolderEx implements Bp
    {
        private static final long serialVersionUID = 1L;
        
        public BpImpl(final SchemaType sType) {
            super(sType, false);
        }
        
        protected BpImpl(final SchemaType sType, final boolean b) {
            super(sType, b);
        }
    }
}
