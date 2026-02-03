package ca.openosp.openo.ar2005.impl;

import org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx;
import org.apache.xmlbeans.XmlFloat;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.StringEnumAbstractBase;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlInt;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.SchemaType;
import javax.xml.namespace.QName;
import ca.openosp.openo.ar2005.ObstetricalHistoryItemList;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

public class ObstetricalHistoryItemListImpl extends XmlComplexContentImpl implements ObstetricalHistoryItemList
{
    private static final long serialVersionUID = 1L;
    private static final QName YEAR$0;
    private static final QName SEX$2;
    private static final QName GESTAGE$4;
    private static final QName BIRTHWEIGHT$6;
    private static final QName LENGTHOFLABOUR$8;
    private static final QName PLACEOFBIRTH$10;
    private static final QName TYPEOFDELIVERY$12;
    private static final QName COMMENTS$14;
    
    public ObstetricalHistoryItemListImpl(final SchemaType sType) {
        super(sType);
    }
    
    public int getYear() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(ObstetricalHistoryItemListImpl.YEAR$0, 0);
            if (target == null) {
                return 0;
            }
            return target.getIntValue();
        }
    }
    
    public XmlInt xgetYear() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlInt target = null;
            target = (XmlInt)this.get_store().find_element_user(ObstetricalHistoryItemListImpl.YEAR$0, 0);
            return target;
        }
    }
    
    public void setYear(final int year) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(ObstetricalHistoryItemListImpl.YEAR$0, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(ObstetricalHistoryItemListImpl.YEAR$0);
            }
            target.setIntValue(year);
        }
    }
    
    public void xsetYear(final XmlInt year) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlInt target = null;
            target = (XmlInt)this.get_store().find_element_user(ObstetricalHistoryItemListImpl.YEAR$0, 0);
            if (target == null) {
                target = (XmlInt)this.get_store().add_element_user(ObstetricalHistoryItemListImpl.YEAR$0);
            }
            target.set((XmlObject)year);
        }
    }
    
    public Sex.Enum getSex() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(ObstetricalHistoryItemListImpl.SEX$2, 0);
            if (target == null) {
                return null;
            }
            return (Sex.Enum)target.getEnumValue();
        }
    }
    
    public Sex xgetSex() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Sex target = null;
            target = (Sex)this.get_store().find_element_user(ObstetricalHistoryItemListImpl.SEX$2, 0);
            return target;
        }
    }
    
    public void setSex(final Sex.Enum sex) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(ObstetricalHistoryItemListImpl.SEX$2, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(ObstetricalHistoryItemListImpl.SEX$2);
            }
            target.setEnumValue((StringEnumAbstractBase)sex);
        }
    }
    
    public void xsetSex(final Sex sex) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Sex target = null;
            target = (Sex)this.get_store().find_element_user(ObstetricalHistoryItemListImpl.SEX$2, 0);
            if (target == null) {
                target = (Sex)this.get_store().add_element_user(ObstetricalHistoryItemListImpl.SEX$2);
            }
            target.set((XmlObject)sex);
        }
    }
    
    public int getGestAge() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(ObstetricalHistoryItemListImpl.GESTAGE$4, 0);
            if (target == null) {
                return 0;
            }
            return target.getIntValue();
        }
    }
    
    public XmlInt xgetGestAge() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlInt target = null;
            target = (XmlInt)this.get_store().find_element_user(ObstetricalHistoryItemListImpl.GESTAGE$4, 0);
            return target;
        }
    }
    
    public void setGestAge(final int gestAge) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(ObstetricalHistoryItemListImpl.GESTAGE$4, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(ObstetricalHistoryItemListImpl.GESTAGE$4);
            }
            target.setIntValue(gestAge);
        }
    }
    
    public void xsetGestAge(final XmlInt gestAge) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlInt target = null;
            target = (XmlInt)this.get_store().find_element_user(ObstetricalHistoryItemListImpl.GESTAGE$4, 0);
            if (target == null) {
                target = (XmlInt)this.get_store().add_element_user(ObstetricalHistoryItemListImpl.GESTAGE$4);
            }
            target.set((XmlObject)gestAge);
        }
    }
    
    public String getBirthWeight() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(ObstetricalHistoryItemListImpl.BIRTHWEIGHT$6, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public XmlString xgetBirthWeight() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(ObstetricalHistoryItemListImpl.BIRTHWEIGHT$6, 0);
            return target;
        }
    }
    
    public void setBirthWeight(final String birthWeight) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(ObstetricalHistoryItemListImpl.BIRTHWEIGHT$6, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(ObstetricalHistoryItemListImpl.BIRTHWEIGHT$6);
            }
            target.setStringValue(birthWeight);
        }
    }
    
    public void xsetBirthWeight(final XmlString birthWeight) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(ObstetricalHistoryItemListImpl.BIRTHWEIGHT$6, 0);
            if (target == null) {
                target = (XmlString)this.get_store().add_element_user(ObstetricalHistoryItemListImpl.BIRTHWEIGHT$6);
            }
            target.set((XmlObject)birthWeight);
        }
    }
    
    public float getLengthOfLabour() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(ObstetricalHistoryItemListImpl.LENGTHOFLABOUR$8, 0);
            if (target == null) {
                return 0.0f;
            }
            return target.getFloatValue();
        }
    }
    
    public XmlFloat xgetLengthOfLabour() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlFloat target = null;
            target = (XmlFloat)this.get_store().find_element_user(ObstetricalHistoryItemListImpl.LENGTHOFLABOUR$8, 0);
            return target;
        }
    }
    
    public boolean isNilLengthOfLabour() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlFloat target = null;
            target = (XmlFloat)this.get_store().find_element_user(ObstetricalHistoryItemListImpl.LENGTHOFLABOUR$8, 0);
            return target != null && target.isNil();
        }
    }
    
    public void setLengthOfLabour(final float lengthOfLabour) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(ObstetricalHistoryItemListImpl.LENGTHOFLABOUR$8, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(ObstetricalHistoryItemListImpl.LENGTHOFLABOUR$8);
            }
            target.setFloatValue(lengthOfLabour);
        }
    }
    
    public void xsetLengthOfLabour(final XmlFloat lengthOfLabour) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlFloat target = null;
            target = (XmlFloat)this.get_store().find_element_user(ObstetricalHistoryItemListImpl.LENGTHOFLABOUR$8, 0);
            if (target == null) {
                target = (XmlFloat)this.get_store().add_element_user(ObstetricalHistoryItemListImpl.LENGTHOFLABOUR$8);
            }
            target.set((XmlObject)lengthOfLabour);
        }
    }
    
    public void setNilLengthOfLabour() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlFloat target = null;
            target = (XmlFloat)this.get_store().find_element_user(ObstetricalHistoryItemListImpl.LENGTHOFLABOUR$8, 0);
            if (target == null) {
                target = (XmlFloat)this.get_store().add_element_user(ObstetricalHistoryItemListImpl.LENGTHOFLABOUR$8);
            }
            target.setNil();
        }
    }
    
    public String getPlaceOfBirth() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(ObstetricalHistoryItemListImpl.PLACEOFBIRTH$10, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public XmlString xgetPlaceOfBirth() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(ObstetricalHistoryItemListImpl.PLACEOFBIRTH$10, 0);
            return target;
        }
    }
    
    public void setPlaceOfBirth(final String placeOfBirth) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(ObstetricalHistoryItemListImpl.PLACEOFBIRTH$10, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(ObstetricalHistoryItemListImpl.PLACEOFBIRTH$10);
            }
            target.setStringValue(placeOfBirth);
        }
    }
    
    public void xsetPlaceOfBirth(final XmlString placeOfBirth) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(ObstetricalHistoryItemListImpl.PLACEOFBIRTH$10, 0);
            if (target == null) {
                target = (XmlString)this.get_store().add_element_user(ObstetricalHistoryItemListImpl.PLACEOFBIRTH$10);
            }
            target.set((XmlObject)placeOfBirth);
        }
    }
    
    public TypeOfDelivery.Enum getTypeOfDelivery() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(ObstetricalHistoryItemListImpl.TYPEOFDELIVERY$12, 0);
            if (target == null) {
                return null;
            }
            return (TypeOfDelivery.Enum)target.getEnumValue();
        }
    }
    
    public TypeOfDelivery xgetTypeOfDelivery() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            TypeOfDelivery target = null;
            target = (TypeOfDelivery)this.get_store().find_element_user(ObstetricalHistoryItemListImpl.TYPEOFDELIVERY$12, 0);
            return target;
        }
    }
    
    public void setTypeOfDelivery(final TypeOfDelivery.Enum typeOfDelivery) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(ObstetricalHistoryItemListImpl.TYPEOFDELIVERY$12, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(ObstetricalHistoryItemListImpl.TYPEOFDELIVERY$12);
            }
            target.setEnumValue((StringEnumAbstractBase)typeOfDelivery);
        }
    }
    
    public void xsetTypeOfDelivery(final TypeOfDelivery typeOfDelivery) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            TypeOfDelivery target = null;
            target = (TypeOfDelivery)this.get_store().find_element_user(ObstetricalHistoryItemListImpl.TYPEOFDELIVERY$12, 0);
            if (target == null) {
                target = (TypeOfDelivery)this.get_store().add_element_user(ObstetricalHistoryItemListImpl.TYPEOFDELIVERY$12);
            }
            target.set((XmlObject)typeOfDelivery);
        }
    }
    
    public String getComments() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(ObstetricalHistoryItemListImpl.COMMENTS$14, 0);
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
            target = (XmlString)this.get_store().find_element_user(ObstetricalHistoryItemListImpl.COMMENTS$14, 0);
            return target;
        }
    }
    
    public void setComments(final String comments) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(ObstetricalHistoryItemListImpl.COMMENTS$14, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(ObstetricalHistoryItemListImpl.COMMENTS$14);
            }
            target.setStringValue(comments);
        }
    }
    
    public void xsetComments(final XmlString comments) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(ObstetricalHistoryItemListImpl.COMMENTS$14, 0);
            if (target == null) {
                target = (XmlString)this.get_store().add_element_user(ObstetricalHistoryItemListImpl.COMMENTS$14);
            }
            target.set((XmlObject)comments);
        }
    }
    
    static {
        YEAR$0 = new QName("http://www.oscarmcmaster.org/AR2005", "year");
        SEX$2 = new QName("http://www.oscarmcmaster.org/AR2005", "sex");
        GESTAGE$4 = new QName("http://www.oscarmcmaster.org/AR2005", "gestAge");
        BIRTHWEIGHT$6 = new QName("http://www.oscarmcmaster.org/AR2005", "birthWeight");
        LENGTHOFLABOUR$8 = new QName("http://www.oscarmcmaster.org/AR2005", "lengthOfLabour");
        PLACEOFBIRTH$10 = new QName("http://www.oscarmcmaster.org/AR2005", "placeOfBirth");
        TYPEOFDELIVERY$12 = new QName("http://www.oscarmcmaster.org/AR2005", "typeOfDelivery");
        COMMENTS$14 = new QName("http://www.oscarmcmaster.org/AR2005", "comments");
    }
    
    public static class SexImpl extends JavaStringEnumerationHolderEx implements Sex
    {
        private static final long serialVersionUID = 1L;
        
        public SexImpl(final SchemaType sType) {
            super(sType, false);
        }
        
        protected SexImpl(final SchemaType sType, final boolean b) {
            super(sType, b);
        }
    }
    
    public static class TypeOfDeliveryImpl extends JavaStringEnumerationHolderEx implements TypeOfDelivery
    {
        private static final long serialVersionUID = 1L;
        
        public TypeOfDeliveryImpl(final SchemaType sType) {
            super(sType, false);
        }
        
        protected TypeOfDeliveryImpl(final SchemaType sType, final boolean b) {
            super(sType, b);
        }
    }
}
