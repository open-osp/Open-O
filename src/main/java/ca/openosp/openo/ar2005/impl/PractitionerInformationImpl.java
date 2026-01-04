package ca.openosp.openo.ar2005.impl;

import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.SimpleValue;
import ca.openosp.openo.ar2005.NewbornCare;
import org.apache.xmlbeans.XmlObject;
import ca.openosp.openo.ar2005.BirthAttendants;
import org.apache.xmlbeans.SchemaType;
import javax.xml.namespace.QName;
import ca.openosp.openo.ar2005.PractitionerInformation;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

public class PractitionerInformationImpl extends XmlComplexContentImpl implements PractitionerInformation
{
    private static final long serialVersionUID = 1L;
    private static final QName BIRTHATTENDANTS$0;
    private static final QName NEWBORNCARE$2;
    private static final QName FAMILYPHYSICIAN$4;
    
    public PractitionerInformationImpl(final SchemaType sType) {
        super(sType);
    }
    
    public BirthAttendants getBirthAttendants() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            BirthAttendants target = null;
            target = (BirthAttendants)this.get_store().find_element_user(PractitionerInformationImpl.BIRTHATTENDANTS$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setBirthAttendants(final BirthAttendants birthAttendants) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            BirthAttendants target = null;
            target = (BirthAttendants)this.get_store().find_element_user(PractitionerInformationImpl.BIRTHATTENDANTS$0, 0);
            if (target == null) {
                target = (BirthAttendants)this.get_store().add_element_user(PractitionerInformationImpl.BIRTHATTENDANTS$0);
            }
            target.set((XmlObject)birthAttendants);
        }
    }
    
    public BirthAttendants addNewBirthAttendants() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            BirthAttendants target = null;
            target = (BirthAttendants)this.get_store().add_element_user(PractitionerInformationImpl.BIRTHATTENDANTS$0);
            return target;
        }
    }
    
    public NewbornCare getNewbornCare() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            NewbornCare target = null;
            target = (NewbornCare)this.get_store().find_element_user(PractitionerInformationImpl.NEWBORNCARE$2, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setNewbornCare(final NewbornCare newbornCare) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            NewbornCare target = null;
            target = (NewbornCare)this.get_store().find_element_user(PractitionerInformationImpl.NEWBORNCARE$2, 0);
            if (target == null) {
                target = (NewbornCare)this.get_store().add_element_user(PractitionerInformationImpl.NEWBORNCARE$2);
            }
            target.set((XmlObject)newbornCare);
        }
    }
    
    public NewbornCare addNewNewbornCare() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            NewbornCare target = null;
            target = (NewbornCare)this.get_store().add_element_user(PractitionerInformationImpl.NEWBORNCARE$2);
            return target;
        }
    }
    
    public String getFamilyPhysician() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PractitionerInformationImpl.FAMILYPHYSICIAN$4, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public XmlString xgetFamilyPhysician() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(PractitionerInformationImpl.FAMILYPHYSICIAN$4, 0);
            return target;
        }
    }
    
    public void setFamilyPhysician(final String familyPhysician) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(PractitionerInformationImpl.FAMILYPHYSICIAN$4, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(PractitionerInformationImpl.FAMILYPHYSICIAN$4);
            }
            target.setStringValue(familyPhysician);
        }
    }
    
    public void xsetFamilyPhysician(final XmlString familyPhysician) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(PractitionerInformationImpl.FAMILYPHYSICIAN$4, 0);
            if (target == null) {
                target = (XmlString)this.get_store().add_element_user(PractitionerInformationImpl.FAMILYPHYSICIAN$4);
            }
            target.set((XmlObject)familyPhysician);
        }
    }
    
    static {
        BIRTHATTENDANTS$0 = new QName("http://www.oscarmcmaster.org/AR2005", "birthAttendants");
        NEWBORNCARE$2 = new QName("http://www.oscarmcmaster.org/AR2005", "newbornCare");
        FAMILYPHYSICIAN$4 = new QName("http://www.oscarmcmaster.org/AR2005", "familyPhysician");
    }
}
