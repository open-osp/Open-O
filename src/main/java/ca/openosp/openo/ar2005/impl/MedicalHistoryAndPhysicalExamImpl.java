package ca.openosp.openo.ar2005.impl;

import ca.openosp.openo.ar2005.PhysicalExaminationType;
import ca.openosp.openo.ar2005.FamilyHistoryType;
import ca.openosp.openo.ar2005.PsychosocialType;
import ca.openosp.openo.ar2005.InfectiousDiseaseType;
import ca.openosp.openo.ar2005.GenericHistoryType;
import ca.openosp.openo.ar2005.MedicalHistoryType;
import org.apache.xmlbeans.XmlObject;
import ca.openosp.openo.ar2005.CurrentPregnancyType;
import org.apache.xmlbeans.SchemaType;
import javax.xml.namespace.QName;
import ca.openosp.openo.ar2005.MedicalHistoryAndPhysicalExam;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

public class MedicalHistoryAndPhysicalExamImpl extends XmlComplexContentImpl implements MedicalHistoryAndPhysicalExam
{
    private static final long serialVersionUID = 1L;
    private static final QName CURRENTPREGNANCY$0;
    private static final QName MEDICALHISTORY$2;
    private static final QName GENERICHISTORY$4;
    private static final QName INFECTIOUSDISEASE$6;
    private static final QName PSYCHOSOCIAL$8;
    private static final QName FAMILYHISTORY$10;
    private static final QName PHYSICALEXAMINATION$12;
    
    public MedicalHistoryAndPhysicalExamImpl(final SchemaType sType) {
        super(sType);
    }
    
    public CurrentPregnancyType getCurrentPregnancy() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            CurrentPregnancyType target = null;
            target = (CurrentPregnancyType)this.get_store().find_element_user(MedicalHistoryAndPhysicalExamImpl.CURRENTPREGNANCY$0, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setCurrentPregnancy(final CurrentPregnancyType currentPregnancy) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            CurrentPregnancyType target = null;
            target = (CurrentPregnancyType)this.get_store().find_element_user(MedicalHistoryAndPhysicalExamImpl.CURRENTPREGNANCY$0, 0);
            if (target == null) {
                target = (CurrentPregnancyType)this.get_store().add_element_user(MedicalHistoryAndPhysicalExamImpl.CURRENTPREGNANCY$0);
            }
            target.set((XmlObject)currentPregnancy);
        }
    }
    
    public CurrentPregnancyType addNewCurrentPregnancy() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            CurrentPregnancyType target = null;
            target = (CurrentPregnancyType)this.get_store().add_element_user(MedicalHistoryAndPhysicalExamImpl.CURRENTPREGNANCY$0);
            return target;
        }
    }
    
    public MedicalHistoryType getMedicalHistory() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            MedicalHistoryType target = null;
            target = (MedicalHistoryType)this.get_store().find_element_user(MedicalHistoryAndPhysicalExamImpl.MEDICALHISTORY$2, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setMedicalHistory(final MedicalHistoryType medicalHistory) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            MedicalHistoryType target = null;
            target = (MedicalHistoryType)this.get_store().find_element_user(MedicalHistoryAndPhysicalExamImpl.MEDICALHISTORY$2, 0);
            if (target == null) {
                target = (MedicalHistoryType)this.get_store().add_element_user(MedicalHistoryAndPhysicalExamImpl.MEDICALHISTORY$2);
            }
            target.set((XmlObject)medicalHistory);
        }
    }
    
    public MedicalHistoryType addNewMedicalHistory() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            MedicalHistoryType target = null;
            target = (MedicalHistoryType)this.get_store().add_element_user(MedicalHistoryAndPhysicalExamImpl.MEDICALHISTORY$2);
            return target;
        }
    }
    
    public GenericHistoryType getGenericHistory() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            GenericHistoryType target = null;
            target = (GenericHistoryType)this.get_store().find_element_user(MedicalHistoryAndPhysicalExamImpl.GENERICHISTORY$4, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setGenericHistory(final GenericHistoryType genericHistory) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            GenericHistoryType target = null;
            target = (GenericHistoryType)this.get_store().find_element_user(MedicalHistoryAndPhysicalExamImpl.GENERICHISTORY$4, 0);
            if (target == null) {
                target = (GenericHistoryType)this.get_store().add_element_user(MedicalHistoryAndPhysicalExamImpl.GENERICHISTORY$4);
            }
            target.set((XmlObject)genericHistory);
        }
    }
    
    public GenericHistoryType addNewGenericHistory() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            GenericHistoryType target = null;
            target = (GenericHistoryType)this.get_store().add_element_user(MedicalHistoryAndPhysicalExamImpl.GENERICHISTORY$4);
            return target;
        }
    }
    
    public InfectiousDiseaseType getInfectiousDisease() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            InfectiousDiseaseType target = null;
            target = (InfectiousDiseaseType)this.get_store().find_element_user(MedicalHistoryAndPhysicalExamImpl.INFECTIOUSDISEASE$6, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setInfectiousDisease(final InfectiousDiseaseType infectiousDisease) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            InfectiousDiseaseType target = null;
            target = (InfectiousDiseaseType)this.get_store().find_element_user(MedicalHistoryAndPhysicalExamImpl.INFECTIOUSDISEASE$6, 0);
            if (target == null) {
                target = (InfectiousDiseaseType)this.get_store().add_element_user(MedicalHistoryAndPhysicalExamImpl.INFECTIOUSDISEASE$6);
            }
            target.set((XmlObject)infectiousDisease);
        }
    }
    
    public InfectiousDiseaseType addNewInfectiousDisease() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            InfectiousDiseaseType target = null;
            target = (InfectiousDiseaseType)this.get_store().add_element_user(MedicalHistoryAndPhysicalExamImpl.INFECTIOUSDISEASE$6);
            return target;
        }
    }
    
    public PsychosocialType getPsychosocial() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            PsychosocialType target = null;
            target = (PsychosocialType)this.get_store().find_element_user(MedicalHistoryAndPhysicalExamImpl.PSYCHOSOCIAL$8, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setPsychosocial(final PsychosocialType psychosocial) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            PsychosocialType target = null;
            target = (PsychosocialType)this.get_store().find_element_user(MedicalHistoryAndPhysicalExamImpl.PSYCHOSOCIAL$8, 0);
            if (target == null) {
                target = (PsychosocialType)this.get_store().add_element_user(MedicalHistoryAndPhysicalExamImpl.PSYCHOSOCIAL$8);
            }
            target.set((XmlObject)psychosocial);
        }
    }
    
    public PsychosocialType addNewPsychosocial() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            PsychosocialType target = null;
            target = (PsychosocialType)this.get_store().add_element_user(MedicalHistoryAndPhysicalExamImpl.PSYCHOSOCIAL$8);
            return target;
        }
    }
    
    public FamilyHistoryType getFamilyHistory() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            FamilyHistoryType target = null;
            target = (FamilyHistoryType)this.get_store().find_element_user(MedicalHistoryAndPhysicalExamImpl.FAMILYHISTORY$10, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setFamilyHistory(final FamilyHistoryType familyHistory) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            FamilyHistoryType target = null;
            target = (FamilyHistoryType)this.get_store().find_element_user(MedicalHistoryAndPhysicalExamImpl.FAMILYHISTORY$10, 0);
            if (target == null) {
                target = (FamilyHistoryType)this.get_store().add_element_user(MedicalHistoryAndPhysicalExamImpl.FAMILYHISTORY$10);
            }
            target.set((XmlObject)familyHistory);
        }
    }
    
    public FamilyHistoryType addNewFamilyHistory() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            FamilyHistoryType target = null;
            target = (FamilyHistoryType)this.get_store().add_element_user(MedicalHistoryAndPhysicalExamImpl.FAMILYHISTORY$10);
            return target;
        }
    }
    
    public PhysicalExaminationType getPhysicalExamination() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            PhysicalExaminationType target = null;
            target = (PhysicalExaminationType)this.get_store().find_element_user(MedicalHistoryAndPhysicalExamImpl.PHYSICALEXAMINATION$12, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setPhysicalExamination(final PhysicalExaminationType physicalExamination) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            PhysicalExaminationType target = null;
            target = (PhysicalExaminationType)this.get_store().find_element_user(MedicalHistoryAndPhysicalExamImpl.PHYSICALEXAMINATION$12, 0);
            if (target == null) {
                target = (PhysicalExaminationType)this.get_store().add_element_user(MedicalHistoryAndPhysicalExamImpl.PHYSICALEXAMINATION$12);
            }
            target.set((XmlObject)physicalExamination);
        }
    }
    
    public PhysicalExaminationType addNewPhysicalExamination() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            PhysicalExaminationType target = null;
            target = (PhysicalExaminationType)this.get_store().add_element_user(MedicalHistoryAndPhysicalExamImpl.PHYSICALEXAMINATION$12);
            return target;
        }
    }
    
    static {
        CURRENTPREGNANCY$0 = new QName("http://www.oscarmcmaster.org/AR2005", "currentPregnancy");
        MEDICALHISTORY$2 = new QName("http://www.oscarmcmaster.org/AR2005", "medicalHistory");
        GENERICHISTORY$4 = new QName("http://www.oscarmcmaster.org/AR2005", "genericHistory");
        INFECTIOUSDISEASE$6 = new QName("http://www.oscarmcmaster.org/AR2005", "infectiousDisease");
        PSYCHOSOCIAL$8 = new QName("http://www.oscarmcmaster.org/AR2005", "psychosocial");
        FAMILYHISTORY$10 = new QName("http://www.oscarmcmaster.org/AR2005", "familyHistory");
        PHYSICALEXAMINATION$12 = new QName("http://www.oscarmcmaster.org/AR2005", "physicalExamination");
    }
}
