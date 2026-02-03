package ca.openosp.openo.ar2005.impl;

import ca.openosp.openo.ar2005.SignatureType;
import ca.openosp.openo.ar2005.InitialLaboratoryInvestigations;
import ca.openosp.openo.ar2005.MedicalHistoryAndPhysicalExam;
import ca.openosp.openo.ar2005.ObstetricalHistory;
import ca.openosp.openo.ar2005.PregnancyHistory;
import ca.openosp.openo.ar2005.PractitionerInformation;
import ca.openosp.openo.ar2005.PartnerInformation;
import ca.openosp.openo.ar2005.PatientInformation;
import org.apache.xmlbeans.XmlDateTime;
import org.apache.xmlbeans.XmlDate;
import java.util.Calendar;
import org.apache.xmlbeans.XmlString;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlInt;
import org.apache.xmlbeans.SimpleValue;
import org.apache.xmlbeans.SchemaType;
import javax.xml.namespace.QName;
import ca.openosp.openo.ar2005.AR1;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;

public class AR1Impl extends XmlComplexContentImpl implements AR1
{
    private static final long serialVersionUID = 1L;
    private static final QName ID$0;
    private static final QName VERSIONID$2;
    private static final QName EPISODEID$4;
    private static final QName DEMOGRAPHICNO$6;
    private static final QName PROVIDERNO$8;
    private static final QName FORMCREATED$10;
    private static final QName FORMEDITED$12;
    private static final QName PATIENTINFORMATION$14;
    private static final QName PARTNERINFORMATION$16;
    private static final QName PRACTITIONERINFORMATION$18;
    private static final QName PREGNANCYHISTORY$20;
    private static final QName OBSTETRICALHISTORY$22;
    private static final QName MEDICALHISTORYANDPHYSICALEXAM$24;
    private static final QName INITIALLABORATORYINVESTIGATIONS$26;
    private static final QName COMMENTS$28;
    private static final QName EXTRACOMMENTS$30;
    private static final QName SIGNATURES$32;
    
    public AR1Impl(final SchemaType sType) {
        super(sType);
    }
    
    public int getId() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(AR1Impl.ID$0, 0);
            if (target == null) {
                return 0;
            }
            return target.getIntValue();
        }
    }
    
    public XmlInt xgetId() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlInt target = null;
            target = (XmlInt)this.get_store().find_element_user(AR1Impl.ID$0, 0);
            return target;
        }
    }
    
    public void setId(final int id) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(AR1Impl.ID$0, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(AR1Impl.ID$0);
            }
            target.setIntValue(id);
        }
    }
    
    public void xsetId(final XmlInt id) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlInt target = null;
            target = (XmlInt)this.get_store().find_element_user(AR1Impl.ID$0, 0);
            if (target == null) {
                target = (XmlInt)this.get_store().add_element_user(AR1Impl.ID$0);
            }
            target.set((XmlObject)id);
        }
    }
    
    public int getVersionID() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(AR1Impl.VERSIONID$2, 0);
            if (target == null) {
                return 0;
            }
            return target.getIntValue();
        }
    }
    
    public XmlInt xgetVersionID() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlInt target = null;
            target = (XmlInt)this.get_store().find_element_user(AR1Impl.VERSIONID$2, 0);
            return target;
        }
    }
    
    public void setVersionID(final int versionID) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(AR1Impl.VERSIONID$2, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(AR1Impl.VERSIONID$2);
            }
            target.setIntValue(versionID);
        }
    }
    
    public void xsetVersionID(final XmlInt versionID) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlInt target = null;
            target = (XmlInt)this.get_store().find_element_user(AR1Impl.VERSIONID$2, 0);
            if (target == null) {
                target = (XmlInt)this.get_store().add_element_user(AR1Impl.VERSIONID$2);
            }
            target.set((XmlObject)versionID);
        }
    }
    
    public int getEpisodeId() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(AR1Impl.EPISODEID$4, 0);
            if (target == null) {
                return 0;
            }
            return target.getIntValue();
        }
    }
    
    public XmlInt xgetEpisodeId() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlInt target = null;
            target = (XmlInt)this.get_store().find_element_user(AR1Impl.EPISODEID$4, 0);
            return target;
        }
    }
    
    public void setEpisodeId(final int episodeId) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(AR1Impl.EPISODEID$4, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(AR1Impl.EPISODEID$4);
            }
            target.setIntValue(episodeId);
        }
    }
    
    public void xsetEpisodeId(final XmlInt episodeId) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlInt target = null;
            target = (XmlInt)this.get_store().find_element_user(AR1Impl.EPISODEID$4, 0);
            if (target == null) {
                target = (XmlInt)this.get_store().add_element_user(AR1Impl.EPISODEID$4);
            }
            target.set((XmlObject)episodeId);
        }
    }
    
    public int getDemographicNo() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(AR1Impl.DEMOGRAPHICNO$6, 0);
            if (target == null) {
                return 0;
            }
            return target.getIntValue();
        }
    }
    
    public XmlInt xgetDemographicNo() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlInt target = null;
            target = (XmlInt)this.get_store().find_element_user(AR1Impl.DEMOGRAPHICNO$6, 0);
            return target;
        }
    }
    
    public void setDemographicNo(final int demographicNo) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(AR1Impl.DEMOGRAPHICNO$6, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(AR1Impl.DEMOGRAPHICNO$6);
            }
            target.setIntValue(demographicNo);
        }
    }
    
    public void xsetDemographicNo(final XmlInt demographicNo) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlInt target = null;
            target = (XmlInt)this.get_store().find_element_user(AR1Impl.DEMOGRAPHICNO$6, 0);
            if (target == null) {
                target = (XmlInt)this.get_store().add_element_user(AR1Impl.DEMOGRAPHICNO$6);
            }
            target.set((XmlObject)demographicNo);
        }
    }
    
    public String getProviderNo() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(AR1Impl.PROVIDERNO$8, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public XmlString xgetProviderNo() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(AR1Impl.PROVIDERNO$8, 0);
            return target;
        }
    }
    
    public void setProviderNo(final String providerNo) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(AR1Impl.PROVIDERNO$8, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(AR1Impl.PROVIDERNO$8);
            }
            target.setStringValue(providerNo);
        }
    }
    
    public void xsetProviderNo(final XmlString providerNo) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(AR1Impl.PROVIDERNO$8, 0);
            if (target == null) {
                target = (XmlString)this.get_store().add_element_user(AR1Impl.PROVIDERNO$8);
            }
            target.set((XmlObject)providerNo);
        }
    }
    
    public Calendar getFormCreated() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(AR1Impl.FORMCREATED$10, 0);
            if (target == null) {
                return null;
            }
            return target.getCalendarValue();
        }
    }
    
    public XmlDate xgetFormCreated() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlDate target = null;
            target = (XmlDate)this.get_store().find_element_user(AR1Impl.FORMCREATED$10, 0);
            return target;
        }
    }
    
    public void setFormCreated(final Calendar formCreated) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(AR1Impl.FORMCREATED$10, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(AR1Impl.FORMCREATED$10);
            }
            target.setCalendarValue(formCreated);
        }
    }
    
    public void xsetFormCreated(final XmlDate formCreated) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlDate target = null;
            target = (XmlDate)this.get_store().find_element_user(AR1Impl.FORMCREATED$10, 0);
            if (target == null) {
                target = (XmlDate)this.get_store().add_element_user(AR1Impl.FORMCREATED$10);
            }
            target.set((XmlObject)formCreated);
        }
    }
    
    public Calendar getFormEdited() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(AR1Impl.FORMEDITED$12, 0);
            if (target == null) {
                return null;
            }
            return target.getCalendarValue();
        }
    }
    
    public XmlDateTime xgetFormEdited() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlDateTime target = null;
            target = (XmlDateTime)this.get_store().find_element_user(AR1Impl.FORMEDITED$12, 0);
            return target;
        }
    }
    
    public void setFormEdited(final Calendar formEdited) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(AR1Impl.FORMEDITED$12, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(AR1Impl.FORMEDITED$12);
            }
            target.setCalendarValue(formEdited);
        }
    }
    
    public void xsetFormEdited(final XmlDateTime formEdited) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlDateTime target = null;
            target = (XmlDateTime)this.get_store().find_element_user(AR1Impl.FORMEDITED$12, 0);
            if (target == null) {
                target = (XmlDateTime)this.get_store().add_element_user(AR1Impl.FORMEDITED$12);
            }
            target.set((XmlObject)formEdited);
        }
    }
    
    public PatientInformation getPatientInformation() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            PatientInformation target = null;
            target = (PatientInformation)this.get_store().find_element_user(AR1Impl.PATIENTINFORMATION$14, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setPatientInformation(final PatientInformation patientInformation) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            PatientInformation target = null;
            target = (PatientInformation)this.get_store().find_element_user(AR1Impl.PATIENTINFORMATION$14, 0);
            if (target == null) {
                target = (PatientInformation)this.get_store().add_element_user(AR1Impl.PATIENTINFORMATION$14);
            }
            target.set((XmlObject)patientInformation);
        }
    }
    
    public PatientInformation addNewPatientInformation() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            PatientInformation target = null;
            target = (PatientInformation)this.get_store().add_element_user(AR1Impl.PATIENTINFORMATION$14);
            return target;
        }
    }
    
    public PartnerInformation getPartnerInformation() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            PartnerInformation target = null;
            target = (PartnerInformation)this.get_store().find_element_user(AR1Impl.PARTNERINFORMATION$16, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setPartnerInformation(final PartnerInformation partnerInformation) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            PartnerInformation target = null;
            target = (PartnerInformation)this.get_store().find_element_user(AR1Impl.PARTNERINFORMATION$16, 0);
            if (target == null) {
                target = (PartnerInformation)this.get_store().add_element_user(AR1Impl.PARTNERINFORMATION$16);
            }
            target.set((XmlObject)partnerInformation);
        }
    }
    
    public PartnerInformation addNewPartnerInformation() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            PartnerInformation target = null;
            target = (PartnerInformation)this.get_store().add_element_user(AR1Impl.PARTNERINFORMATION$16);
            return target;
        }
    }
    
    public PractitionerInformation getPractitionerInformation() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            PractitionerInformation target = null;
            target = (PractitionerInformation)this.get_store().find_element_user(AR1Impl.PRACTITIONERINFORMATION$18, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setPractitionerInformation(final PractitionerInformation practitionerInformation) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            PractitionerInformation target = null;
            target = (PractitionerInformation)this.get_store().find_element_user(AR1Impl.PRACTITIONERINFORMATION$18, 0);
            if (target == null) {
                target = (PractitionerInformation)this.get_store().add_element_user(AR1Impl.PRACTITIONERINFORMATION$18);
            }
            target.set((XmlObject)practitionerInformation);
        }
    }
    
    public PractitionerInformation addNewPractitionerInformation() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            PractitionerInformation target = null;
            target = (PractitionerInformation)this.get_store().add_element_user(AR1Impl.PRACTITIONERINFORMATION$18);
            return target;
        }
    }
    
    public PregnancyHistory getPregnancyHistory() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            PregnancyHistory target = null;
            target = (PregnancyHistory)this.get_store().find_element_user(AR1Impl.PREGNANCYHISTORY$20, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setPregnancyHistory(final PregnancyHistory pregnancyHistory) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            PregnancyHistory target = null;
            target = (PregnancyHistory)this.get_store().find_element_user(AR1Impl.PREGNANCYHISTORY$20, 0);
            if (target == null) {
                target = (PregnancyHistory)this.get_store().add_element_user(AR1Impl.PREGNANCYHISTORY$20);
            }
            target.set((XmlObject)pregnancyHistory);
        }
    }
    
    public PregnancyHistory addNewPregnancyHistory() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            PregnancyHistory target = null;
            target = (PregnancyHistory)this.get_store().add_element_user(AR1Impl.PREGNANCYHISTORY$20);
            return target;
        }
    }
    
    public ObstetricalHistory getObstetricalHistory() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            ObstetricalHistory target = null;
            target = (ObstetricalHistory)this.get_store().find_element_user(AR1Impl.OBSTETRICALHISTORY$22, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setObstetricalHistory(final ObstetricalHistory obstetricalHistory) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            ObstetricalHistory target = null;
            target = (ObstetricalHistory)this.get_store().find_element_user(AR1Impl.OBSTETRICALHISTORY$22, 0);
            if (target == null) {
                target = (ObstetricalHistory)this.get_store().add_element_user(AR1Impl.OBSTETRICALHISTORY$22);
            }
            target.set((XmlObject)obstetricalHistory);
        }
    }
    
    public ObstetricalHistory addNewObstetricalHistory() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            ObstetricalHistory target = null;
            target = (ObstetricalHistory)this.get_store().add_element_user(AR1Impl.OBSTETRICALHISTORY$22);
            return target;
        }
    }
    
    public MedicalHistoryAndPhysicalExam getMedicalHistoryAndPhysicalExam() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            MedicalHistoryAndPhysicalExam target = null;
            target = (MedicalHistoryAndPhysicalExam)this.get_store().find_element_user(AR1Impl.MEDICALHISTORYANDPHYSICALEXAM$24, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setMedicalHistoryAndPhysicalExam(final MedicalHistoryAndPhysicalExam medicalHistoryAndPhysicalExam) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            MedicalHistoryAndPhysicalExam target = null;
            target = (MedicalHistoryAndPhysicalExam)this.get_store().find_element_user(AR1Impl.MEDICALHISTORYANDPHYSICALEXAM$24, 0);
            if (target == null) {
                target = (MedicalHistoryAndPhysicalExam)this.get_store().add_element_user(AR1Impl.MEDICALHISTORYANDPHYSICALEXAM$24);
            }
            target.set((XmlObject)medicalHistoryAndPhysicalExam);
        }
    }
    
    public MedicalHistoryAndPhysicalExam addNewMedicalHistoryAndPhysicalExam() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            MedicalHistoryAndPhysicalExam target = null;
            target = (MedicalHistoryAndPhysicalExam)this.get_store().add_element_user(AR1Impl.MEDICALHISTORYANDPHYSICALEXAM$24);
            return target;
        }
    }
    
    public InitialLaboratoryInvestigations getInitialLaboratoryInvestigations() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            InitialLaboratoryInvestigations target = null;
            target = (InitialLaboratoryInvestigations)this.get_store().find_element_user(AR1Impl.INITIALLABORATORYINVESTIGATIONS$26, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setInitialLaboratoryInvestigations(final InitialLaboratoryInvestigations initialLaboratoryInvestigations) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            InitialLaboratoryInvestigations target = null;
            target = (InitialLaboratoryInvestigations)this.get_store().find_element_user(AR1Impl.INITIALLABORATORYINVESTIGATIONS$26, 0);
            if (target == null) {
                target = (InitialLaboratoryInvestigations)this.get_store().add_element_user(AR1Impl.INITIALLABORATORYINVESTIGATIONS$26);
            }
            target.set((XmlObject)initialLaboratoryInvestigations);
        }
    }
    
    public InitialLaboratoryInvestigations addNewInitialLaboratoryInvestigations() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            InitialLaboratoryInvestigations target = null;
            target = (InitialLaboratoryInvestigations)this.get_store().add_element_user(AR1Impl.INITIALLABORATORYINVESTIGATIONS$26);
            return target;
        }
    }
    
    public String getComments() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(AR1Impl.COMMENTS$28, 0);
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
            target = (XmlString)this.get_store().find_element_user(AR1Impl.COMMENTS$28, 0);
            return target;
        }
    }
    
    public void setComments(final String comments) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(AR1Impl.COMMENTS$28, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(AR1Impl.COMMENTS$28);
            }
            target.setStringValue(comments);
        }
    }
    
    public void xsetComments(final XmlString comments) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(AR1Impl.COMMENTS$28, 0);
            if (target == null) {
                target = (XmlString)this.get_store().add_element_user(AR1Impl.COMMENTS$28);
            }
            target.set((XmlObject)comments);
        }
    }
    
    public String getExtraComments() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(AR1Impl.EXTRACOMMENTS$30, 0);
            if (target == null) {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    public XmlString xgetExtraComments() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(AR1Impl.EXTRACOMMENTS$30, 0);
            return target;
        }
    }
    
    public void setExtraComments(final String extraComments) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SimpleValue target = null;
            target = (SimpleValue)this.get_store().find_element_user(AR1Impl.EXTRACOMMENTS$30, 0);
            if (target == null) {
                target = (SimpleValue)this.get_store().add_element_user(AR1Impl.EXTRACOMMENTS$30);
            }
            target.setStringValue(extraComments);
        }
    }
    
    public void xsetExtraComments(final XmlString extraComments) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            XmlString target = null;
            target = (XmlString)this.get_store().find_element_user(AR1Impl.EXTRACOMMENTS$30, 0);
            if (target == null) {
                target = (XmlString)this.get_store().add_element_user(AR1Impl.EXTRACOMMENTS$30);
            }
            target.set((XmlObject)extraComments);
        }
    }
    
    public SignatureType getSignatures() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SignatureType target = null;
            target = (SignatureType)this.get_store().find_element_user(AR1Impl.SIGNATURES$32, 0);
            if (target == null) {
                return null;
            }
            return target;
        }
    }
    
    public void setSignatures(final SignatureType signatures) {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SignatureType target = null;
            target = (SignatureType)this.get_store().find_element_user(AR1Impl.SIGNATURES$32, 0);
            if (target == null) {
                target = (SignatureType)this.get_store().add_element_user(AR1Impl.SIGNATURES$32);
            }
            target.set((XmlObject)signatures);
        }
    }
    
    public SignatureType addNewSignatures() {
        synchronized (this.monitor()) {
            this.check_orphaned();
            SignatureType target = null;
            target = (SignatureType)this.get_store().add_element_user(AR1Impl.SIGNATURES$32);
            return target;
        }
    }
    
    static {
        ID$0 = new QName("http://www.oscarmcmaster.org/AR2005", "id");
        VERSIONID$2 = new QName("http://www.oscarmcmaster.org/AR2005", "VersionID");
        EPISODEID$4 = new QName("http://www.oscarmcmaster.org/AR2005", "episodeId");
        DEMOGRAPHICNO$6 = new QName("http://www.oscarmcmaster.org/AR2005", "demographicNo");
        PROVIDERNO$8 = new QName("http://www.oscarmcmaster.org/AR2005", "providerNo");
        FORMCREATED$10 = new QName("http://www.oscarmcmaster.org/AR2005", "formCreated");
        FORMEDITED$12 = new QName("http://www.oscarmcmaster.org/AR2005", "formEdited");
        PATIENTINFORMATION$14 = new QName("http://www.oscarmcmaster.org/AR2005", "patientInformation");
        PARTNERINFORMATION$16 = new QName("http://www.oscarmcmaster.org/AR2005", "partnerInformation");
        PRACTITIONERINFORMATION$18 = new QName("http://www.oscarmcmaster.org/AR2005", "practitionerInformation");
        PREGNANCYHISTORY$20 = new QName("http://www.oscarmcmaster.org/AR2005", "pregnancyHistory");
        OBSTETRICALHISTORY$22 = new QName("http://www.oscarmcmaster.org/AR2005", "obstetricalHistory");
        MEDICALHISTORYANDPHYSICALEXAM$24 = new QName("http://www.oscarmcmaster.org/AR2005", "medicalHistoryAndPhysicalExam");
        INITIALLABORATORYINVESTIGATIONS$26 = new QName("http://www.oscarmcmaster.org/AR2005", "initialLaboratoryInvestigations");
        COMMENTS$28 = new QName("http://www.oscarmcmaster.org/AR2005", "comments");
        EXTRACOMMENTS$30 = new QName("http://www.oscarmcmaster.org/AR2005", "extraComments");
        SIGNATURES$32 = new QName("http://www.oscarmcmaster.org/AR2005", "signatures");
    }
}
