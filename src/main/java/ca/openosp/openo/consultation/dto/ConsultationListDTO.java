package ca.openosp.openo.consultation.dto;

import org.apache.commons.lang3.time.DateFormatUtils;

import ca.openosp.openo.commn.model.enumerator.ConsultationRequestExtKey;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Data Transfer Object for consultation request list views.
 * <p>
 * Provides a lightweight, flat representation of consultation request data optimized for list display,
 * eliminating N+1 query issues by fetching all display fields via a single JOIN query with batch-loaded
 * extensions. Replaces the previous pattern of loading full entities and then individually querying
 * related Demographics, Providers, Services, and Specialists per row.
 * </p>
 *
 * @since 2026-02-03
 */
public class ConsultationListDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String NOT_APPLICABLE = "N/A";

    private Integer id;
    private String status;
    private String urgency;
    private Integer demographicNo;
    private String demographicLastName;
    private String demographicFirstName;
    private String demographicProviderNo;
    private String mrpLastName;
    private String mrpFirstName;
    private String consultProviderNo;
    private String consultProviderLastName;
    private String consultProviderFirstName;
    private Integer serviceId;
    private String serviceDescription;
    private String specialistLastName;
    private String specialistFirstName;
    private Date referralDate;
    private Date appointmentDate;
    private Date appointmentTime;
    private boolean patientWillBook;
    private Date followUpDate;
    private String sendTo;
    private String siteName;

    private boolean eReferral;
    private String ereferralService;
    private String ereferralDoctor;

    /**
     * Default constructor.
     */
    public ConsultationListDTO() {
    }

    /**
     * Constructor for JPQL projection queries. Parameter order must match the SELECT NEW clause exactly.
     *
     * @param id Integer the consultation request ID
     * @param status String the consultation status code
     * @param urgency String the urgency level
     * @param demographicNo Integer the patient demographic number
     * @param demographicLastName String the patient's last name
     * @param demographicFirstName String the patient's first name
     * @param demographicProviderNo String the patient's MRP provider number
     * @param mrpLastName String the MRP provider's last name
     * @param mrpFirstName String the MRP provider's first name
     * @param consultProviderNo String the consulting provider number
     * @param consultProviderLastName String the consulting provider's last name
     * @param consultProviderFirstName String the consulting provider's first name
     * @param serviceId Integer the consultation service ID
     * @param serviceDescription String the service description
     * @param specialistLastName String the specialist's last name
     * @param specialistFirstName String the specialist's first name
     * @param referralDate Date the referral date
     * @param appointmentDate Date the appointment date
     * @param appointmentTime Date the appointment time
     * @param patientWillBook boolean whether the patient will book
     * @param followUpDate Date the follow-up date
     * @param sendTo String the team/send-to value
     * @param siteName String the site name
     */
    public ConsultationListDTO(Integer id, String status, String urgency,
                               Integer demographicNo, String demographicLastName, String demographicFirstName,
                               String demographicProviderNo, String mrpLastName, String mrpFirstName,
                               String consultProviderNo, String consultProviderLastName, String consultProviderFirstName,
                               Integer serviceId, String serviceDescription,
                               String specialistLastName, String specialistFirstName,
                               Date referralDate, Date appointmentDate, Date appointmentTime,
                               boolean patientWillBook, Date followUpDate,
                               String sendTo, String siteName) {
        this.id = id;
        this.status = status;
        this.urgency = urgency;
        this.demographicNo = demographicNo;
        this.demographicLastName = demographicLastName;
        this.demographicFirstName = demographicFirstName;
        this.demographicProviderNo = demographicProviderNo;
        this.mrpLastName = mrpLastName;
        this.mrpFirstName = mrpFirstName;
        this.consultProviderNo = consultProviderNo;
        this.consultProviderLastName = consultProviderLastName;
        this.consultProviderFirstName = consultProviderFirstName;
        this.serviceId = serviceId;
        this.serviceDescription = serviceDescription;
        this.specialistLastName = specialistLastName;
        this.specialistFirstName = specialistFirstName;
        this.referralDate = referralDate;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.patientWillBook = patientWillBook;
        this.followUpDate = followUpDate;
        this.sendTo = sendTo;
        this.siteName = siteName;
    }

    /**
     * Returns patient name formatted as "LastName, FirstName".
     *
     * @return String the formatted patient name
     */
    public String getPatientFormattedName() {
        if (demographicLastName == null && demographicFirstName == null) {
            return "";
        }
        if (demographicLastName == null) {
            return demographicFirstName;
        }
        if (demographicFirstName == null) {
            return demographicLastName;
        }
        return demographicLastName + ", " + demographicFirstName;
    }

    /**
     * Returns MRP provider name formatted as "LastName, FirstName".
     *
     * @return String the formatted MRP name, or "N/A" if not available
     */
    public String getMrpFormattedName() {
        if (mrpLastName == null && mrpFirstName == null) {
            return NOT_APPLICABLE;
        }
        if (mrpLastName == null) {
            return mrpFirstName;
        }
        if (mrpFirstName == null) {
            return mrpLastName;
        }
        return mrpLastName + ", " + mrpFirstName;
    }

    /**
     * Returns consulting provider name formatted as "LastName, FirstName".
     *
     * @return String the formatted consulting provider name, or "N/A" if not available
     */
    public String getConsultProviderFormattedName() {
        if (consultProviderLastName == null && consultProviderFirstName == null) {
            return NOT_APPLICABLE;
        }
        if (consultProviderLastName == null) {
            return consultProviderFirstName;
        }
        if (consultProviderFirstName == null) {
            return consultProviderLastName;
        }
        return consultProviderLastName + ", " + consultProviderFirstName;
    }

    /**
     * Returns specialist name formatted as "LastName, FirstName".
     * Falls back to eReferral doctor name if no specialist is set and serviceId is 0.
     *
     * @return String the formatted specialist name, or "N/A" if not available
     */
    public String getSpecialistFormattedName() {
        if (specialistLastName != null || specialistFirstName != null) {
            if (specialistLastName == null) return specialistFirstName;
            if (specialistFirstName == null) return specialistLastName;
            return specialistLastName + ", " + specialistFirstName;
        }
        if (serviceId != null && serviceId == 0 && ereferralDoctor != null) {
            return ereferralDoctor;
        }
        return NOT_APPLICABLE;
    }

    /**
     * Returns the effective service description.
     * Falls back to eReferral service name if serviceId is 0.
     *
     * @return String the service description
     */
    public String getEffectiveServiceDescription() {
        if (serviceId != null && serviceId == 0 && ereferralService != null) {
            return ereferralService;
        }
        return serviceDescription != null ? serviceDescription : "";
    }

    /**
     * Returns the referral date formatted as ISO date string.
     *
     * @return String the formatted referral date
     */
    public String getReferralDateFormatted() {
        if (referralDate == null) return "";
        return DateFormatUtils.ISO_DATE_FORMAT.format(referralDate);
    }

    /**
     * Returns the appointment date and time formatted for display.
     *
     * @return String the formatted appointment date/time, or "N/A" if not set
     */
    public String getAppointmentDateFormatted() {
        if (appointmentDate == null) {
            return NOT_APPLICABLE;
        }
        if (appointmentTime == null) {
            return DateFormatUtils.ISO_DATE_FORMAT.format(appointmentDate) + " T00:00:00";
        }
        return DateFormatUtils.ISO_DATE_FORMAT.format(appointmentDate) + " " + DateFormatUtils.ISO_TIME_FORMAT.format(appointmentTime);
    }

    /**
     * Returns the follow-up date formatted as ISO date string.
     *
     * @return String the formatted follow-up date, or "N/A" if not set
     */
    public String getFollowUpDateFormatted() {
        if (followUpDate == null) return NOT_APPLICABLE;
        return DateFormatUtils.ISO_DATE_FORMAT.format(followUpDate);
    }

    /**
     * Applies extension data (eReferral fields) from a pre-loaded map.
     *
     * @param extMap Map of extension key to value for this consultation request
     */
    public void applyExtensions(Map<String, String> extMap) {
        if (extMap == null) return;
        this.eReferral = extMap.containsKey(ConsultationRequestExtKey.EREFERRAL_REF.getKey());
        this.ereferralService = extMap.getOrDefault(ConsultationRequestExtKey.EREFERRAL_SERVICE.getKey(), null);
        this.ereferralDoctor = extMap.getOrDefault(ConsultationRequestExtKey.EREFERRAL_DOCTOR.getKey(), null);
    }

    public Integer getId() { return id; }
    public String getStatus() { return status; }
    public String getUrgency() { return urgency; }
    public Integer getDemographicNo() { return demographicNo; }
    public String getDemographicProviderNo() { return demographicProviderNo; }
    public String getConsultProviderNo() { return consultProviderNo; }
    public String getConsultProviderLastName() { return consultProviderLastName; }
    public String getConsultProviderFirstName() { return consultProviderFirstName; }
    public Integer getServiceId() { return serviceId; }
    public String getServiceDescription() { return serviceDescription; }
    public Date getReferralDate() { return referralDate; }
    public Date getAppointmentDate() { return appointmentDate; }
    public Date getAppointmentTime() { return appointmentTime; }
    public boolean isPatientWillBook() { return patientWillBook; }
    public Date getFollowUpDate() { return followUpDate; }
    public String getSendTo() { return sendTo; }
    public String getSiteName() { return siteName; }
    public boolean isEReferral() { return eReferral; }
}
