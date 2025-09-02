package ca.openosp.openo.ar2005.impl;

import java.util.List;
import java.util.ArrayList;

import org.apache.xmlbeans.impl.values.JavaIntHolderEx;
import org.apache.xmlbeans.impl.values.JavaStringHolderEx;
import org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx;
import org.apache.xmlbeans.XmlDate;
import java.util.Calendar;
import org.apache.xmlbeans.StringEnumAbstractBase;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.SchemaType;
import javax.xml.namespace.QName;
import ca.openosp.openo.ar2005.PatientInformation;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

public class PatientInformationImpl extends XmlComplexContentImpl implements PatientInformation
{
    private static final long serialVersionUID = 1L;
    private static final QName LASTNAME$0;
    private static final QName FIRSTNAME$2;
    private static final QName ADDRESS$4;
    private static final QName APT$6;
    private static final QName CITY$8;
    private static final QName PROVINCE$10;
    private static final QName POSTALCODE$12;
    private static final QName HOMEPHONE$14;
    private static final QName WORKPHONE$16;
    private static final QName LANGUAGE$18;
    private static final QName DOB$20;
    private static final QName AGE$22;
    private static final QName OCCUPATION$24;
    private static final QName LEVELOFEDUCATION$26;
    private static final QName HIN$28;
    private static final QName FILENO$30;
    private static final QName ETHNICBACKGROUND$32;
    private static final QName MARITALSTATUS$34;
    private static final QName ALLERGIES$36;
    private static final QName MEDICATIONS$38;
    
    public PatientInformationImpl(final SchemaType sType) {
        super(sType);
    }
    
    public String getLastName() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PatientInformationImpl.LASTNAME$0, 0);
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
            target = (XmlString)this.get_store().find_element_user(PatientInformationImpl.LASTNAME$0, 0);
            return target;
        }
    }
    
    public void setLastName(final String lastName) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PatientInformationImpl.LASTNAME$0, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(PatientInformationImpl.LASTNAME$0);
            }
            target.setStringValue(lastName);
        }
    }
    
    public void xsetLastName(final XmlString lastName) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(PatientInformationImpl.LASTNAME$0, 0);
            if (target == null) {
                target = (XmlString)this.get_store().add_element_user(PatientInformationImpl.LASTNAME$0);
            }
            target.set((XmlObject)lastName);
        }
    }
    
    public String getFirstName() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PatientInformationImpl.FIRSTNAME$2, 0);
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
            target = (XmlString)this.get_store().find_element_user(PatientInformationImpl.FIRSTNAME$2, 0);
            return target;
        }
    }
    
    public void setFirstName(final String firstName) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PatientInformationImpl.FIRSTNAME$2, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(PatientInformationImpl.FIRSTNAME$2);
            }
            target.setStringValue(firstName);
        }
    }
    
    public void xsetFirstName(final XmlString firstName) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(PatientInformationImpl.FIRSTNAME$2, 0);
            if (target == null) {
                target = (XmlString)this.get_store().add_element_user(PatientInformationImpl.FIRSTNAME$2);
            }
            target.set((XmlObject)firstName);
        }
    }
    
    public String getAddress() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PatientInformationImpl.ADDRESS$4, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public XmlString xgetAddress() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(PatientInformationImpl.ADDRESS$4, 0);
            return target;
        }
    }
    
    public void setAddress(final String address) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PatientInformationImpl.ADDRESS$4, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(PatientInformationImpl.ADDRESS$4);
            }
            target.setStringValue(address);
        }
    }
    
    public void xsetAddress(final XmlString address) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(PatientInformationImpl.ADDRESS$4, 0);
            if (target == null) {
                target = (XmlString)this.get_store().add_element_user(PatientInformationImpl.ADDRESS$4);
            }
            target.set((XmlObject)address);
        }
    }
    
    public String getApt() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PatientInformationImpl.APT$6, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public XmlString xgetApt() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(PatientInformationImpl.APT$6, 0);
            return target;
        }
    }
    
    public void setApt(final String apt) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PatientInformationImpl.APT$6, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(PatientInformationImpl.APT$6);
            }
            target.setStringValue(apt);
        }
    }
    
    public void xsetApt(final XmlString apt) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(PatientInformationImpl.APT$6, 0);
            if (target == null) {
                target = (XmlString)this.get_store().add_element_user(PatientInformationImpl.APT$6);
            }
            target.set((XmlObject)apt);
        }
    }
    
    public String getCity() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PatientInformationImpl.CITY$8, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public XmlString xgetCity() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(PatientInformationImpl.CITY$8, 0);
            return target;
        }
    }
    
    public void setCity(final String city) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PatientInformationImpl.CITY$8, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(PatientInformationImpl.CITY$8);
            }
            target.setStringValue(city);
        }
    }
    
    public void xsetCity(final XmlString city) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(PatientInformationImpl.CITY$8, 0);
            if (target == null) {
                target = (XmlString)this.get_store().add_element_user(PatientInformationImpl.CITY$8);
            }
            target.set((XmlObject)city);
        }
    }
    
    public Province.Enum getProvince() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PatientInformationImpl.PROVINCE$10, 0);
            if (target == null) {
                return null;
            }
            return (Province.Enum)target.getEnumValue();
        }
    }
    
    public Province xgetProvince() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Province target = null;
            target = (Province)this.get_store().find_element_user(PatientInformationImpl.PROVINCE$10, 0);
            return target;
        }
    }
    
    public void setProvince(final Province.Enum province) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PatientInformationImpl.PROVINCE$10, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(PatientInformationImpl.PROVINCE$10);
            }
            target.setEnumValue((StringEnumAbstractBase)province);
        }
    }
    
    public void xsetProvince(final Province province) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Province target = null;
            target = (Province)this.get_store().find_element_user(PatientInformationImpl.PROVINCE$10, 0);
            if (target == null) {
                target = (Province)this.get_store().add_element_user(PatientInformationImpl.PROVINCE$10);
            }
            target.set((XmlObject)province);
        }
    }
    
    public String getPostalCode() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PatientInformationImpl.POSTALCODE$12, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public PostalCode xgetPostalCode() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            PostalCode target = null;
            target = (PostalCode)this.get_store().find_element_user(PatientInformationImpl.POSTALCODE$12, 0);
            return target;
        }
    }
    
    public void setPostalCode(final String postalCode) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PatientInformationImpl.POSTALCODE$12, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(PatientInformationImpl.POSTALCODE$12);
            }
            target.setStringValue(postalCode);
        }
    }
    
    public void xsetPostalCode(final PostalCode postalCode) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            PostalCode target = null;
            target = (PostalCode)this.get_store().find_element_user(PatientInformationImpl.POSTALCODE$12, 0);
            if (target == null) {
                target = (PostalCode)this.get_store().add_element_user(PatientInformationImpl.POSTALCODE$12);
            }
            target.set((XmlObject)postalCode);
        }
    }
    
    public String getHomePhone() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PatientInformationImpl.HOMEPHONE$14, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public HomePhone xgetHomePhone() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            HomePhone target = null;
            target = (HomePhone)this.get_store().find_element_user(PatientInformationImpl.HOMEPHONE$14, 0);
            return target;
        }
    }
    
    public void setHomePhone(final String homePhone) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PatientInformationImpl.HOMEPHONE$14, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(PatientInformationImpl.HOMEPHONE$14);
            }
            target.setStringValue(homePhone);
        }
    }
    
    public void xsetHomePhone(final HomePhone homePhone) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            HomePhone target = null;
            target = (HomePhone)this.get_store().find_element_user(PatientInformationImpl.HOMEPHONE$14, 0);
            if (target == null) {
                target = (HomePhone)this.get_store().add_element_user(PatientInformationImpl.HOMEPHONE$14);
            }
            target.set((XmlObject)homePhone);
        }
    }
    
    public String getWorkPhone() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PatientInformationImpl.WORKPHONE$16, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public WorkPhone xgetWorkPhone() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            WorkPhone target = null;
            target = (WorkPhone)this.get_store().find_element_user(PatientInformationImpl.WORKPHONE$16, 0);
            return target;
        }
    }
    
    public void setWorkPhone(final String workPhone) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PatientInformationImpl.WORKPHONE$16, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(PatientInformationImpl.WORKPHONE$16);
            }
            target.setStringValue(workPhone);
        }
    }
    
    public void xsetWorkPhone(final WorkPhone workPhone) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            WorkPhone target = null;
            target = (WorkPhone)this.get_store().find_element_user(PatientInformationImpl.WORKPHONE$16, 0);
            if (target == null) {
                target = (WorkPhone)this.get_store().add_element_user(PatientInformationImpl.WORKPHONE$16);
            }
            target.set((XmlObject)workPhone);
        }
    }
    
    public Language.Enum getLanguage() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PatientInformationImpl.LANGUAGE$18, 0);
            if (target == null) {
                return null;
            }
            return (Language.Enum)target.getEnumValue();
        }
    }
    
    public Language xgetLanguage() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Language target = null;
            target = (Language)this.get_store().find_element_user(PatientInformationImpl.LANGUAGE$18, 0);
            return target;
        }
    }
    
    public void setLanguage(final Language.Enum language) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PatientInformationImpl.LANGUAGE$18, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(PatientInformationImpl.LANGUAGE$18);
            }
            target.setEnumValue((StringEnumAbstractBase)language);
        }
    }
    
    public void xsetLanguage(final Language language) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Language target = null;
            target = (Language)this.get_store().find_element_user(PatientInformationImpl.LANGUAGE$18, 0);
            if (target == null) {
                target = (Language)this.get_store().add_element_user(PatientInformationImpl.LANGUAGE$18);
            }
            target.set((XmlObject)language);
        }
    }
    
    public Calendar getDob() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PatientInformationImpl.DOB$20, 0);
            if (target == null) {
                return null;
            }
            return target.getCalendarValue();
        }
    }
    
    public XmlDate xgetDob() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlDate target = null;
            target = (XmlDate)this.get_store().find_element_user(PatientInformationImpl.DOB$20, 0);
            return target;
        }
    }
    
    public void setDob(final Calendar dob) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PatientInformationImpl.DOB$20, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(PatientInformationImpl.DOB$20);
            }
            target.setCalendarValue(dob);
        }
    }
    
    public void xsetDob(final XmlDate dob) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlDate target = null;
            target = (XmlDate)this.get_store().find_element_user(PatientInformationImpl.DOB$20, 0);
            if (target == null) {
                target = (XmlDate)this.get_store().add_element_user(PatientInformationImpl.DOB$20);
            }
            target.set((XmlObject)dob);
        }
    }
    
    public int getAge() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PatientInformationImpl.AGE$22, 0);
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
            target = (Age)this.get_store().find_element_user(PatientInformationImpl.AGE$22, 0);
            return target;
        }
    }
    
    public void setAge(final int age) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PatientInformationImpl.AGE$22, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(PatientInformationImpl.AGE$22);
            }
            target.setIntValue(age);
        }
    }
    
    public void xsetAge(final Age age) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Age target = null;
            target = (Age)this.get_store().find_element_user(PatientInformationImpl.AGE$22, 0);
            if (target == null) {
                target = (Age)this.get_store().add_element_user(PatientInformationImpl.AGE$22);
            }
            target.set((XmlObject)age);
        }
    }
    
    public Occupation getOccupation() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Occupation target = null;
            target = (Occupation)this.get_store().find_element_user(PatientInformationImpl.OCCUPATION$24, 0);
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
            target = (Occupation)this.get_store().find_element_user(PatientInformationImpl.OCCUPATION$24, 0);
            if (target == null) {
                target = (Occupation)this.get_store().add_element_user(PatientInformationImpl.OCCUPATION$24);
            }
            target.set((XmlObject)occupation);
        }
    }
    
    public Occupation addNewOccupation() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Occupation target = null;
            target = (Occupation)this.get_store().add_element_user(PatientInformationImpl.OCCUPATION$24);
            return target;
        }
    }
    
    public LevelOfEducation.Enum getLevelOfEducation() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PatientInformationImpl.LEVELOFEDUCATION$26, 0);
            if (target == null) {
                return null;
            }
            return (LevelOfEducation.Enum)target.getEnumValue();
        }
    }
    
    public LevelOfEducation xgetLevelOfEducation() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            LevelOfEducation target = null;
            target = (LevelOfEducation)this.get_store().find_element_user(PatientInformationImpl.LEVELOFEDUCATION$26, 0);
            return target;
        }
    }
    
    public void setLevelOfEducation(final LevelOfEducation.Enum levelOfEducation) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PatientInformationImpl.LEVELOFEDUCATION$26, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(PatientInformationImpl.LEVELOFEDUCATION$26);
            }
            target.setEnumValue((StringEnumAbstractBase)levelOfEducation);
        }
    }
    
    public void xsetLevelOfEducation(final LevelOfEducation levelOfEducation) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            LevelOfEducation target = null;
            target = (LevelOfEducation)this.get_store().find_element_user(PatientInformationImpl.LEVELOFEDUCATION$26, 0);
            if (target == null) {
                target = (LevelOfEducation)this.get_store().add_element_user(PatientInformationImpl.LEVELOFEDUCATION$26);
            }
            target.set((XmlObject)levelOfEducation);
        }
    }
    
    public Hin getHin() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Hin target = null;
            target = (Hin)this.get_store().find_element_user(PatientInformationImpl.HIN$28, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setHin(final Hin hin) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Hin target = null;
            target = (Hin)this.get_store().find_element_user(PatientInformationImpl.HIN$28, 0);
            if (target == null) {
                target = (Hin)this.get_store().add_element_user(PatientInformationImpl.HIN$28);
            }
            target.set((XmlObject)hin);
        }
    }
    
    public Hin addNewHin() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Hin target = null;
            target = (Hin)this.get_store().add_element_user(PatientInformationImpl.HIN$28);
            return target;
        }
    }
    
    public String getFileNo() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PatientInformationImpl.FILENO$30, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public XmlString xgetFileNo() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(PatientInformationImpl.FILENO$30, 0);
            return target;
        }
    }
    
    public void setFileNo(final String fileNo) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PatientInformationImpl.FILENO$30, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(PatientInformationImpl.FILENO$30);
            }
            target.setStringValue(fileNo);
        }
    }
    
    public void xsetFileNo(final XmlString fileNo) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(PatientInformationImpl.FILENO$30, 0);
            if (target == null) {
                target = (XmlString)this.get_store().add_element_user(PatientInformationImpl.FILENO$30);
            }
            target.set((XmlObject)fileNo);
        }
    }
    
    public EthnicBackground getEthnicBackground() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            EthnicBackground target = null;
            target = (EthnicBackground)this.get_store().find_element_user(PatientInformationImpl.ETHNICBACKGROUND$32, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setEthnicBackground(final EthnicBackground ethnicBackground) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            EthnicBackground target = null;
            target = (EthnicBackground)this.get_store().find_element_user(PatientInformationImpl.ETHNICBACKGROUND$32, 0);
            if (target == null) {
                target = (EthnicBackground)this.get_store().add_element_user(PatientInformationImpl.ETHNICBACKGROUND$32);
            }
            target.set((XmlObject)ethnicBackground);
        }
    }
    
    public EthnicBackground addNewEthnicBackground() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            EthnicBackground target = null;
            target = (EthnicBackground)this.get_store().add_element_user(PatientInformationImpl.ETHNICBACKGROUND$32);
            return target;
        }
    }
    
    public MaritalStatus.Enum getMaritalStatus() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PatientInformationImpl.MARITALSTATUS$34, 0);
            if (target == null) {
                return null;
            }
            return (MaritalStatus.Enum)target.getEnumValue();
        }
    }
    
    public MaritalStatus xgetMaritalStatus() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            MaritalStatus target = null;
            target = (MaritalStatus)this.get_store().find_element_user(PatientInformationImpl.MARITALSTATUS$34, 0);
            return target;
        }
    }
    
    public void setMaritalStatus(final MaritalStatus.Enum maritalStatus) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PatientInformationImpl.MARITALSTATUS$34, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(PatientInformationImpl.MARITALSTATUS$34);
            }
            target.setEnumValue((StringEnumAbstractBase)maritalStatus);
        }
    }
    
    public void xsetMaritalStatus(final MaritalStatus maritalStatus) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            MaritalStatus target = null;
            target = (MaritalStatus)this.get_store().find_element_user(PatientInformationImpl.MARITALSTATUS$34, 0);
            if (target == null) {
                target = (MaritalStatus)this.get_store().add_element_user(PatientInformationImpl.MARITALSTATUS$34);
            }
            target.set((XmlObject)maritalStatus);
        }
    }
    
    public String getAllergies() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PatientInformationImpl.ALLERGIES$36, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public XmlString xgetAllergies() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(PatientInformationImpl.ALLERGIES$36, 0);
            return target;
        }
    }
    
    public void setAllergies(final String allergies) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PatientInformationImpl.ALLERGIES$36, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(PatientInformationImpl.ALLERGIES$36);
            }
            target.setStringValue(allergies);
        }
    }
    
    public void xsetAllergies(final XmlString allergies) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(PatientInformationImpl.ALLERGIES$36, 0);
            if (target == null) {
                target = (XmlString)this.get_store().add_element_user(PatientInformationImpl.ALLERGIES$36);
            }
            target.set((XmlObject)allergies);
        }
    }
    
    public Medications getMedications() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Medications target = null;
            target = (Medications)this.get_store().find_element_user(PatientInformationImpl.MEDICATIONS$38, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setMedications(final Medications medications) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Medications target = null;
            target = (Medications)this.get_store().find_element_user(PatientInformationImpl.MEDICATIONS$38, 0);
            if (target == null) {
                target = (Medications)this.get_store().add_element_user(PatientInformationImpl.MEDICATIONS$38);
            }
            target.set((XmlObject)medications);
        }
    }
    
    public Medications addNewMedications() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            Medications target = null;
            target = (Medications)this.get_store().add_element_user(PatientInformationImpl.MEDICATIONS$38);
            return target;
        }
    }
    
    static {
        LASTNAME$0 = new QName("http://www.oscarmcmaster.org/AR2005", "lastName");
        FIRSTNAME$2 = new QName("http://www.oscarmcmaster.org/AR2005", "firstName");
        ADDRESS$4 = new QName("http://www.oscarmcmaster.org/AR2005", "address");
        APT$6 = new QName("http://www.oscarmcmaster.org/AR2005", "apt");
        CITY$8 = new QName("http://www.oscarmcmaster.org/AR2005", "city");
        PROVINCE$10 = new QName("http://www.oscarmcmaster.org/AR2005", "province");
        POSTALCODE$12 = new QName("http://www.oscarmcmaster.org/AR2005", "postalCode");
        HOMEPHONE$14 = new QName("http://www.oscarmcmaster.org/AR2005", "homePhone");
        WORKPHONE$16 = new QName("http://www.oscarmcmaster.org/AR2005", "workPhone");
        LANGUAGE$18 = new QName("http://www.oscarmcmaster.org/AR2005", "language");
        DOB$20 = new QName("http://www.oscarmcmaster.org/AR2005", "dob");
        AGE$22 = new QName("http://www.oscarmcmaster.org/AR2005", "age");
        OCCUPATION$24 = new QName("http://www.oscarmcmaster.org/AR2005", "occupation");
        LEVELOFEDUCATION$26 = new QName("http://www.oscarmcmaster.org/AR2005", "levelOfEducation");
        HIN$28 = new QName("http://www.oscarmcmaster.org/AR2005", "hin");
        FILENO$30 = new QName("http://www.oscarmcmaster.org/AR2005", "fileNo");
        ETHNICBACKGROUND$32 = new QName("http://www.oscarmcmaster.org/AR2005", "ethnicBackground");
        MARITALSTATUS$34 = new QName("http://www.oscarmcmaster.org/AR2005", "maritalStatus");
        ALLERGIES$36 = new QName("http://www.oscarmcmaster.org/AR2005", "allergies");
        MEDICATIONS$38 = new QName("http://www.oscarmcmaster.org/AR2005", "medications");
    }
    
    public static class ProvinceImpl extends JavaStringEnumerationHolderEx implements Province
    {
        private static final long serialVersionUID = 1L;
        
        public ProvinceImpl(final SchemaType sType) {
            super(sType, false);
        }
        
        protected ProvinceImpl(final SchemaType sType, final boolean b) {
            super(sType, b);
        }
    }
    
    public static class PostalCodeImpl extends JavaStringHolderEx implements PostalCode
    {
        private static final long serialVersionUID = 1L;
        
        public PostalCodeImpl(final SchemaType sType) {
            super(sType, false);
        }
        
        protected PostalCodeImpl(final SchemaType sType, final boolean b) {
            super(sType, b);
        }
    }
    
    public static class HomePhoneImpl extends JavaStringHolderEx implements HomePhone
    {
        private static final long serialVersionUID = 1L;
        
        public HomePhoneImpl(final SchemaType sType) {
            super(sType, false);
        }
        
        protected HomePhoneImpl(final SchemaType sType, final boolean b) {
            super(sType, b);
        }
    }
    
    public static class WorkPhoneImpl extends JavaStringHolderEx implements WorkPhone
    {
        private static final long serialVersionUID = 1L;
        
        public WorkPhoneImpl(final SchemaType sType) {
            super(sType, false);
        }
        
        protected WorkPhoneImpl(final SchemaType sType, final boolean b) {
            super(sType, b);
        }
    }
    
    public static class LanguageImpl extends JavaStringEnumerationHolderEx implements Language
    {
        private static final long serialVersionUID = 1L;
        
        public LanguageImpl(final SchemaType sType) {
            super(sType, false);
        }
        
        protected LanguageImpl(final SchemaType sType, final boolean b) {
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
    
    public static class LevelOfEducationImpl extends JavaStringEnumerationHolderEx implements LevelOfEducation
    {
        private static final long serialVersionUID = 1L;
        
        public LevelOfEducationImpl(final SchemaType sType) {
            super(sType, false);
        }
        
        protected LevelOfEducationImpl(final SchemaType sType, final boolean b) {
            super(sType, b);
        }
    }
    
    public static class HinImpl extends JavaStringHolderEx implements Hin
    {
        private static final long serialVersionUID = 1L;
        private static final QName TYPE$0;
        
        public HinImpl(final SchemaType sType) {
            super(sType, true);
        }
        
        protected HinImpl(final SchemaType sType, final boolean b) {
            super(sType, b);
        }
        
        public Type.Enum getType() {
            synchronized (this.monitor()) {
                this.check_orphaned();
                SimpleValue target = null;
                target = (SimpleValue)this.get_store().find_attribute_user(HinImpl.TYPE$0);
                if (target == null) {
                    return null;
                }
                return (Type.Enum)target.getEnumValue();
            }
        }
        
        public Type xgetType() {
            synchronized (this.monitor()) {
                this.check_orphaned();
                Type target = null;
                target = (Type)this.get_store().find_attribute_user(HinImpl.TYPE$0);
                return target;
            }
        }
        
        public boolean isSetType() {
            synchronized (this.monitor()) {
                this.check_orphaned();
                return this.get_store().find_attribute_user(HinImpl.TYPE$0) != null;
            }
        }
        
        public void setType(final Type.Enum type) {
            synchronized (this.monitor()) {
                this.check_orphaned();
                SimpleValue target = null;
                target = (SimpleValue)this.get_store().find_attribute_user(HinImpl.TYPE$0);
                if (target == null) {
                    target = (SimpleValue)this.get_store().add_attribute_user(HinImpl.TYPE$0);
                }
                target.setEnumValue((StringEnumAbstractBase)type);
            }
        }
        
        public void xsetType(final Type type) {
            synchronized (this.monitor()) {
                this.check_orphaned();
                Type target = null;
                target = (Type)this.get_store().find_attribute_user(HinImpl.TYPE$0);
                if (target == null) {
                    target = (Type)this.get_store().add_attribute_user(HinImpl.TYPE$0);
                }
                target.set((XmlObject)type);
            }
        }
        
        public void unsetType() {
            synchronized (this.monitor()) {
                this.check_orphaned();
                this.get_store().remove_attribute(HinImpl.TYPE$0);
            }
        }
        
        static {
            TYPE$0 = new QName("", "type");
        }
        
        public static class TypeImpl extends JavaStringEnumerationHolderEx implements Type
        {
            private static final long serialVersionUID = 1L;
            
            public TypeImpl(final SchemaType sType) {
                super(sType, false);
            }
            
            protected TypeImpl(final SchemaType sType, final boolean b) {
                super(sType, b);
            }
        }
    }
    
    public static class EthnicBackgroundImpl extends XmlComplexContentImpl implements EthnicBackground
    {
        private static final long serialVersionUID = 1L;
        private static final QName VALUE$0;
        
        public EthnicBackgroundImpl(final SchemaType sType) {
            super(sType);
        }
        
        public Value[] getValueArray() {
            synchronized (this.monitor()) {
                this.check_orphaned();
                final List targetList = new ArrayList();
                this.get_store().find_all_element_users(EthnicBackgroundImpl.VALUE$0, targetList);
                final Value[] result = new Value[targetList.size()];
                targetList.toArray(result);
                return result;
            }
        }
        
        public Value getValueArray(final int i) {
            synchronized (this.monitor()) {
                this.check_orphaned();
                Value target = null;
                target = (Value)this.get_store().find_element_user(EthnicBackgroundImpl.VALUE$0, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
                return target;
            }
        }
        
        public int sizeOfValueArray() {
            synchronized (this.monitor()) {
                this.check_orphaned();
                return this.get_store().count_elements(EthnicBackgroundImpl.VALUE$0);
            }
        }
        
        public void setValueArray(final Value[] valueArray) {
            synchronized (this.monitor()) {
                this.check_orphaned();
                this.arraySetterHelper((XmlObject[])valueArray, EthnicBackgroundImpl.VALUE$0);
            }
        }
        
        public void setValueArray(final int i, final Value value) {
            synchronized (this.monitor()) {
                this.check_orphaned();
                Value target = null;
                target = (Value)this.get_store().find_element_user(EthnicBackgroundImpl.VALUE$0, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
                target.set((XmlObject)value);
            }
        }
        
        public Value insertNewValue(final int i) {
            synchronized (this.monitor()) {
                this.check_orphaned();
                Value target = null;
                target = (Value)this.get_store().insert_element_user(EthnicBackgroundImpl.VALUE$0, i);
                return target;
            }
        }
        
        public Value addNewValue() {
            synchronized (this.monitor()) {
                this.check_orphaned();
                Value target = null;
                target = (Value)this.get_store().add_element_user(EthnicBackgroundImpl.VALUE$0);
                return target;
            }
        }
        
        public void removeValue(final int i) {
            synchronized (this.monitor()) {
                this.check_orphaned();
                this.get_store().remove_element(EthnicBackgroundImpl.VALUE$0, i);
            }
        }
        
        static {
            VALUE$0 = new QName("http://www.oscarmcmaster.org/AR2005", "value");
        }
        
        public static class ValueImpl extends JavaStringEnumerationHolderEx implements Value
        {
            private static final long serialVersionUID = 1L;
            private static final QName PARENT$0;
            
            public ValueImpl(final SchemaType sType) {
                super(sType, true);
            }
            
            protected ValueImpl(final SchemaType sType, final boolean b) {
                super(sType, b);
            }
            
            public Parent.Enum getParent() {
                synchronized (this.monitor()) {
                    this.check_orphaned();
                    SimpleValue target = null;
                    target = (SimpleValue)this.get_store().find_attribute_user(ValueImpl.PARENT$0);
                    if (target == null) {
                        return null;
                    }
                    return (Parent.Enum)target.getEnumValue();
                }
            }
            
            public Parent xgetParent() {
                synchronized (this.monitor()) {
                    this.check_orphaned();
                    Parent target = null;
                    target = (Parent)this.get_store().find_attribute_user(ValueImpl.PARENT$0);
                    return target;
                }
            }
            
            public boolean isSetParent() {
                synchronized (this.monitor()) {
                    this.check_orphaned();
                    return this.get_store().find_attribute_user(ValueImpl.PARENT$0) != null;
                }
            }
            
            public void setParent(final Parent.Enum parent) {
                synchronized (this.monitor()) {
                    this.check_orphaned();
                    SimpleValue target = null;
                    target = (SimpleValue)this.get_store().find_attribute_user(ValueImpl.PARENT$0);
                    if (target == null) {
                        target = (SimpleValue)this.get_store().add_attribute_user(ValueImpl.PARENT$0);
                    }
                    target.setEnumValue((StringEnumAbstractBase)parent);
                }
            }
            
            public void xsetParent(final Parent parent) {
                synchronized (this.monitor()) {
                    this.check_orphaned();
                    Parent target = null;
                    target = (Parent)this.get_store().find_attribute_user(ValueImpl.PARENT$0);
                    if (target == null) {
                        target = (Parent)this.get_store().add_attribute_user(ValueImpl.PARENT$0);
                    }
                    target.set((XmlObject)parent);
                }
            }
            
            public void unsetParent() {
                synchronized (this.monitor()) {
                    this.check_orphaned();
                    this.get_store().remove_attribute(ValueImpl.PARENT$0);
                }
            }
            
            static {
                PARENT$0 = new QName("", "parent");
            }
            
            public static class ParentImpl extends JavaStringEnumerationHolderEx implements Parent
            {
                private static final long serialVersionUID = 1L;
                
                public ParentImpl(final SchemaType sType) {
                    super(sType, false);
                }
                
                protected ParentImpl(final SchemaType sType, final boolean b) {
                    super(sType, b);
                }
            }
        }
    }
    
    public static class MaritalStatusImpl extends JavaStringEnumerationHolderEx implements MaritalStatus
    {
        private static final long serialVersionUID = 1L;
        
        public MaritalStatusImpl(final SchemaType sType) {
            super(sType, false);
        }
        
        protected MaritalStatusImpl(final SchemaType sType, final boolean b) {
            super(sType, b);
        }
    }
    
    public static class MedicationsImpl extends XmlComplexContentImpl implements Medications
    {
        private static final long serialVersionUID = 1L;
        private static final QName VALUE$0;
        private static final QName OTHER$2;
        
        public MedicationsImpl(final SchemaType sType) {
            super(sType);
        }
        
        public Value.Enum[] getValueArray() {
            synchronized (this.monitor()) {
                this.check_orphaned();
                final List targetList = new ArrayList();
                this.get_store().find_all_element_users(MedicationsImpl.VALUE$0, targetList);
                final Value.Enum[] result = new Value.Enum[targetList.size()];
                for (int i = 0, len = targetList.size(); i < len; ++i) {
                    result[i] = (Value.Enum)((SimpleValue)targetList.get(i)).getEnumValue();
                }
                return result;
            }
        }
        
        public Value.Enum getValueArray(final int i) {
            synchronized (this.monitor()) {
                this.check_orphaned();
                SimpleValue target = null;
                target = (SimpleValue)this.get_store().find_element_user(MedicationsImpl.VALUE$0, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
                return (Value.Enum)target.getEnumValue();
            }
        }
        
        public Value[] xgetValueArray() {
            synchronized (this.monitor()) {
                this.check_orphaned();
                final List targetList = new ArrayList();
                this.get_store().find_all_element_users(MedicationsImpl.VALUE$0, targetList);
                final Value[] result = new Value[targetList.size()];
                targetList.toArray(result);
                return result;
            }
        }
        
        public Value xgetValueArray(final int i) {
            synchronized (this.monitor()) {
                this.check_orphaned();
                Value target = null;
                target = (Value)this.get_store().find_element_user(MedicationsImpl.VALUE$0, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
                return target;
            }
        }
        
        public int sizeOfValueArray() {
            synchronized (this.monitor()) {
                this.check_orphaned();
                return this.get_store().count_elements(MedicationsImpl.VALUE$0);
            }
        }
        
        public void setValueArray(final Value.Enum[] valueArray) {
            synchronized (this.monitor()) {
                this.check_orphaned();
                this.arraySetterHelper((StringEnumAbstractBase[])valueArray, MedicationsImpl.VALUE$0);
            }
        }
        
        public void setValueArray(final int i, final Value.Enum value) {
            synchronized (this.monitor()) {
                this.check_orphaned();
                SimpleValue target = null;
                target = (SimpleValue)this.get_store().find_element_user(MedicationsImpl.VALUE$0, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
                target.setEnumValue((StringEnumAbstractBase)value);
            }
        }
        
        public void xsetValueArray(final Value[] valueArray) {
            synchronized (this.monitor()) {
                this.check_orphaned();
                this.arraySetterHelper((XmlObject[])valueArray, MedicationsImpl.VALUE$0);
            }
        }
        
        public void xsetValueArray(final int i, final Value value) {
            synchronized (this.monitor()) {
                this.check_orphaned();
                Value target = null;
                target = (Value)this.get_store().find_element_user(MedicationsImpl.VALUE$0, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
                target.set((XmlObject)value);
            }
        }
        
        public void insertValue(final int i, final Value.Enum value) {
            synchronized (this.monitor()) {
                this.check_orphaned();
                final SimpleValue target = (SimpleValue)this.get_store().insert_element_user(MedicationsImpl.VALUE$0, i);
                target.setEnumValue((StringEnumAbstractBase)value);
            }
        }
        
        public void addValue(final Value.Enum value) {
            synchronized (this.monitor()) {
                this.check_orphaned();
                SimpleValue target = null;
                target = (SimpleValue)this.get_store().add_element_user(MedicationsImpl.VALUE$0);
                target.setEnumValue((StringEnumAbstractBase)value);
            }
        }
        
        public Value insertNewValue(final int i) {
            synchronized (this.monitor()) {
                this.check_orphaned();
                Value target = null;
                target = (Value)this.get_store().insert_element_user(MedicationsImpl.VALUE$0, i);
                return target;
            }
        }
        
        public Value addNewValue() {
            synchronized (this.monitor()) {
                this.check_orphaned();
                Value target = null;
                target = (Value)this.get_store().add_element_user(MedicationsImpl.VALUE$0);
                return target;
            }
        }
        
        public void removeValue(final int i) {
            synchronized (this.monitor()) {
                this.check_orphaned();
                this.get_store().remove_element(MedicationsImpl.VALUE$0, i);
            }
        }
        
        public String[] getOtherArray() {
            synchronized (this.monitor()) {
                this.check_orphaned();
                final List targetList = new ArrayList();
                this.get_store().find_all_element_users(MedicationsImpl.OTHER$2, targetList);
                final String[] result = new String[targetList.size()];
                for (int i = 0, len = targetList.size(); i < len; ++i) {
                    result[i] = ((SimpleValue)targetList.get(i)).getStringValue();
                }
                return result;
            }
        }
        
        public String getOtherArray(final int i) {
            synchronized (this.monitor()) {
                this.check_orphaned();
                SimpleValue target = null;
                target = (SimpleValue)this.get_store().find_element_user(MedicationsImpl.OTHER$2, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
                return target.getStringValue();
            }
        }
        
        public XmlString[] xgetOtherArray() {
            synchronized (this.monitor()) {
                this.check_orphaned();
                final List targetList = new ArrayList();
                this.get_store().find_all_element_users(MedicationsImpl.OTHER$2, targetList);
                final XmlString[] result = new XmlString[targetList.size()];
                targetList.toArray(result);
                return result;
            }
        }
        
        public XmlString xgetOtherArray(final int i) {
            synchronized (this.monitor()) {
                this.check_orphaned();
                XmlString target = null;
                target = (XmlString)this.get_store().find_element_user(MedicationsImpl.OTHER$2, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
                return target;
            }
        }
        
        public int sizeOfOtherArray() {
            synchronized (this.monitor()) {
                this.check_orphaned();
                return this.get_store().count_elements(MedicationsImpl.OTHER$2);
            }
        }
        
        public void setOtherArray(final String[] otherArray) {
            synchronized (this.monitor()) {
                this.check_orphaned();
                this.arraySetterHelper(otherArray, MedicationsImpl.OTHER$2);
            }
        }
        
        public void setOtherArray(final int i, final String other) {
            synchronized (this.monitor()) {
                this.check_orphaned();
                SimpleValue target = null;
                target = (SimpleValue)this.get_store().find_element_user(MedicationsImpl.OTHER$2, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
                target.setStringValue(other);
            }
        }
        
        public void xsetOtherArray(final XmlString[] otherArray) {
            synchronized (this.monitor()) {
                this.check_orphaned();
                this.arraySetterHelper((XmlObject[])otherArray, MedicationsImpl.OTHER$2);
            }
        }
        
        public void xsetOtherArray(final int i, final XmlString other) {
            synchronized (this.monitor()) {
                this.check_orphaned();
                XmlString target = null;
                target = (XmlString)this.get_store().find_element_user(MedicationsImpl.OTHER$2, i);
                if (target == null) {
                    throw new IndexOutOfBoundsException();
                }
                target.set((XmlObject)other);
            }
        }
        
        public void insertOther(final int i, final String other) {
            synchronized (this.monitor()) {
                this.check_orphaned();
                final SimpleValue target = (SimpleValue)this.get_store().insert_element_user(MedicationsImpl.OTHER$2, i);
                target.setStringValue(other);
            }
        }
        
        public void addOther(final String other) {
            synchronized (this.monitor()) {
                this.check_orphaned();
                SimpleValue target = null;
                target = (SimpleValue)this.get_store().add_element_user(MedicationsImpl.OTHER$2);
                target.setStringValue(other);
            }
        }
        
        public XmlString insertNewOther(final int i) {
            synchronized (this.monitor()) {
                this.check_orphaned();
                XmlString target = null;
                target = (XmlString)this.get_store().insert_element_user(MedicationsImpl.OTHER$2, i);
                return target;
            }
        }
        
        public XmlString addNewOther() {
            synchronized (this.monitor()) {
                this.check_orphaned();
                XmlString target = null;
                target = (XmlString)this.get_store().add_element_user(MedicationsImpl.OTHER$2);
                return target;
            }
        }
        
        public void removeOther(final int i) {
            synchronized (this.monitor()) {
                this.check_orphaned();
                this.get_store().remove_element(MedicationsImpl.OTHER$2, i);
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
}
