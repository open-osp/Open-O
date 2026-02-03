package ca.openosp.openo.ar2005.impl;

import org.apache.xmlbeans.impl.values.JavaIntHolderEx;
import org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx;
import org.apache.xmlbeans.StringEnumAbstractBase;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.SchemaType;
import javax.xml.namespace.QName;
import ca.openosp.openo.ar2005.PartnerInformation;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

public class PartnerInformationImpl extends XmlComplexContentImpl implements PartnerInformation
{
    private static final long serialVersionUID = 1L;
    private static final QName LASTNAME$0;
    private static final QName FIRSTNAME$2;
    private static final QName OCCUPATION$4;
    private static final QName EDUCATIONLEVEL$6;
    private static final QName AGE$8;
    
    public PartnerInformationImpl(final SchemaType sType) {
        super(sType);
    }
    
    public String getLastName() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PartnerInformationImpl.LASTNAME$0, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public XmlString xgetLastName() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(PartnerInformationImpl.LASTNAME$0, 0);
            return target;
        }
    }
    
    public void setLastName(final String lastName) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PartnerInformationImpl.LASTNAME$0, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(PartnerInformationImpl.LASTNAME$0);
            }
            target.setStringValue(lastName);
        }
    }
    
    public void xsetLastName(final XmlString lastName) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(PartnerInformationImpl.LASTNAME$0, 0);
            if (target == null) {
                target = (XmlString)this.get_store().add_element_user(PartnerInformationImpl.LASTNAME$0);
            }
            target.set((XmlObject)lastName);
        }
    }
    
    public String getFirstName() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PartnerInformationImpl.FIRSTNAME$2, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public XmlString xgetFirstName() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(PartnerInformationImpl.FIRSTNAME$2, 0);
            return target;
        }
    }
    
    public void setFirstName(final String firstName) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PartnerInformationImpl.FIRSTNAME$2, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(PartnerInformationImpl.FIRSTNAME$2);
            }
            target.setStringValue(firstName);
        }
    }
    
    public void xsetFirstName(final XmlString firstName) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(PartnerInformationImpl.FIRSTNAME$2, 0);
            if (target == null) {
                target = (XmlString)this.get_store().add_element_user(PartnerInformationImpl.FIRSTNAME$2);
            }
            target.set((XmlObject)firstName);
        }
    }
    
    public Occupation getOccupation() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Occupation target = null;
            target = (Occupation)this.get_store().find_element_user(PartnerInformationImpl.OCCUPATION$4, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setOccupation(final Occupation occupation) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Occupation target = null;
            target = (Occupation)this.get_store().find_element_user(PartnerInformationImpl.OCCUPATION$4, 0);
            if (target == null) {
                target = (Occupation)this.get_store().add_element_user(PartnerInformationImpl.OCCUPATION$4);
            }
            target.set((XmlObject)occupation);
        }
    }
    
    public Occupation addNewOccupation() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Occupation target = null;
            target = (Occupation)this.get_store().add_element_user(PartnerInformationImpl.OCCUPATION$4);
            return target;
        }
    }
    
    public EducationLevel.Enum getEducationLevel() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PartnerInformationImpl.EDUCATIONLEVEL$6, 0);
            if (target == null) {
                return null;
            }
            return (EducationLevel.Enum)target.getEnumValue();
        }
    }
    
    public EducationLevel xgetEducationLevel() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            EducationLevel target = null;
            target = (EducationLevel)this.get_store().find_element_user(PartnerInformationImpl.EDUCATIONLEVEL$6, 0);
            return target;
        }
    }
    
    public void setEducationLevel(final EducationLevel.Enum educationLevel) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PartnerInformationImpl.EDUCATIONLEVEL$6, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(PartnerInformationImpl.EDUCATIONLEVEL$6);
            }
            target.setEnumValue((StringEnumAbstractBase)educationLevel);
        }
    }
    
    public void xsetEducationLevel(final EducationLevel educationLevel) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            EducationLevel target = null;
            target = (EducationLevel)this.get_store().find_element_user(PartnerInformationImpl.EDUCATIONLEVEL$6, 0);
            if (target == null) {
                target = (EducationLevel)this.get_store().add_element_user(PartnerInformationImpl.EDUCATIONLEVEL$6);
            }
            target.set((XmlObject)educationLevel);
        }
    }
    
    public int getAge() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PartnerInformationImpl.AGE$8, 0);
            if (target == null) {
                return 0;
            }
            return target.getIntValue();
        }
    }
    
    public Age xgetAge() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Age target = null;
            target = (Age)this.get_store().find_element_user(PartnerInformationImpl.AGE$8, 0);
            return target;
        }
    }
    
    public void setAge(final int age) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PartnerInformationImpl.AGE$8, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(PartnerInformationImpl.AGE$8);
            }
            target.setIntValue(age);
        }
    }
    
    public void xsetAge(final Age age) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Age target = null;
            target = (Age)this.get_store().find_element_user(PartnerInformationImpl.AGE$8, 0);
            if (target == null) {
                target = (Age)this.get_store().add_element_user(PartnerInformationImpl.AGE$8);
            }
            target.set((XmlObject)age);
        }
    }
    
    static {
        LASTNAME$0 = new QName("http://www.oscarmcmaster.org/AR2005", "lastName");
        FIRSTNAME$2 = new QName("http://www.oscarmcmaster.org/AR2005", "firstName");
        OCCUPATION$4 = new QName("http://www.oscarmcmaster.org/AR2005", "occupation");
        EDUCATIONLEVEL$6 = new QName("http://www.oscarmcmaster.org/AR2005", "educationLevel");
        AGE$8 = new QName("http://www.oscarmcmaster.org/AR2005", "age");
    }
    
    public static class OccupationImpl extends XmlComplexContentImpl implements Occupation
    {
        private static final long serialVersionUID = 1L;
        private static final QName VALUE$0;
        private static final QName OTHER$2;
        
        public OccupationImpl(final SchemaType sType) {
            super(sType);
        }
        
        public Value.Enum getValue() {
            synchronized (this.monitor()) {
                this.check_orphaned();
                SimpleValue target = null;
                target = (SimpleValue)this.get_store().find_element_user(OccupationImpl.VALUE$0, 0);
                if (target == null) {
                    return null;
                }
                return (Value.Enum)target.getEnumValue();
            }
        }
        
        public Value xgetValue() {
            synchronized (this.monitor()) {
                this.check_orphaned();
                Value target = null;
                target = (Value)this.get_store().find_element_user(OccupationImpl.VALUE$0, 0);
                return target;
            }
        }
        
        public void setValue(final Value.Enum value) {
            synchronized (this.monitor()) {
                this.check_orphaned();
                SimpleValue target = null;
                target = (SimpleValue)this.get_store().find_element_user(OccupationImpl.VALUE$0, 0);
                if (target == null) {
                    target = (SimpleValue)this.get_store().add_element_user(OccupationImpl.VALUE$0);
                }
                target.setEnumValue((StringEnumAbstractBase)value);
            }
        }
        
        public void xsetValue(final Value value) {
            synchronized (this.monitor()) {
                this.check_orphaned();
                Value target = null;
                target = (Value)this.get_store().find_element_user(OccupationImpl.VALUE$0, 0);
                if (target == null) {
                    target = (Value)this.get_store().add_element_user(OccupationImpl.VALUE$0);
                }
                target.set((XmlObject)value);
            }
        }
        
        public String getOther() {
            synchronized (this.monitor()) {
                this.check_orphaned();
                SimpleValue target = null;
                target = (SimpleValue)this.get_store().find_element_user(OccupationImpl.OTHER$2, 0);
                if (target == null) {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        public XmlString xgetOther() {
            synchronized (this.monitor()) {
                this.check_orphaned();
                XmlString target = null;
                target = (XmlString)this.get_store().find_element_user(OccupationImpl.OTHER$2, 0);
                return target;
            }
        }
        
        public void setOther(final String other) {
            synchronized (this.monitor()) {
                this.check_orphaned();
                SimpleValue target = null;
                target = (SimpleValue)this.get_store().find_element_user(OccupationImpl.OTHER$2, 0);
                if (target == null) {
                    target = (SimpleValue)this.get_store().add_element_user(OccupationImpl.OTHER$2);
                }
                target.setStringValue(other);
            }
        }
        
        public void xsetOther(final XmlString other) {
            synchronized (this.monitor()) {
                this.check_orphaned();
                XmlString target = null;
                target = (XmlString)this.get_store().find_element_user(OccupationImpl.OTHER$2, 0);
                if (target == null) {
                    target = (XmlString)this.get_store().add_element_user(OccupationImpl.OTHER$2);
                }
                target.set((XmlObject)other);
            }
        }
        
        static {
            VALUE$0 = new QName("http://www.oscarmcmaster.org/AR2005", "value");
            OTHER$2 = new QName("http://www.oscarmcmaster.org/AR2005", "other");
        }
        
        public static class ValueImpl extends JavaStringEnumerationHolderEx implements Value
        {
            private static final long serialVersionUID = 1L;
            
            public ValueImpl(final SchemaType sType) {
                super(sType, false);
            }
            
            protected ValueImpl(final SchemaType sType, final boolean b) {
                super(sType, b);
            }
        }
    }
    
    public static class EducationLevelImpl extends JavaStringEnumerationHolderEx implements EducationLevel
    {
        private static final long serialVersionUID = 1L;
        
        public EducationLevelImpl(final SchemaType sType) {
            super(sType, false);
        }
        
        protected EducationLevelImpl(final SchemaType sType, final boolean b) {
            super(sType, b);
        }
    }
    
    public static class AgeImpl extends JavaIntHolderEx implements Age
    {
        private static final long serialVersionUID = 1L;
        
        public AgeImpl(final SchemaType sType) {
            super(sType, false);
        }
        
        protected AgeImpl(final SchemaType sType, final boolean b) {
            super(sType, b);
        }
    }
}
