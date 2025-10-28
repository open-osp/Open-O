package ca.openosp.openo.caisi_integrator.ws;

import javax.xml.bind.annotation.XmlSchemaType;
import org.w3._2001.xmlschema.Adapter1;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.XmlElement;
import java.util.Calendar;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cachedAppointment", propOrder = { "appointmentDate", "caisiDemographicId", "caisiProviderId", "createDatetime", "endTime", "facilityIdIntegerCompositePk", "location", "notes", "reason", "remarks", "resources", "startTime", "status", "style", "type", "updateDatetime" })
public class CachedAppointment extends AbstractModel implements Serializable
{
    private static final long serialVersionUID = 1L;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1.class)
    @XmlSchemaType(name = "dateTime")
    protected Calendar appointmentDate;
    protected Integer caisiDemographicId;
    protected String caisiProviderId;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1.class)
    @XmlSchemaType(name = "dateTime")
    protected Calendar createDatetime;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1.class)
    @XmlSchemaType(name = "dateTime")
    protected Calendar endTime;
    protected FacilityIdIntegerCompositePk facilityIdIntegerCompositePk;
    protected String location;
    protected String notes;
    protected String reason;
    protected String remarks;
    protected String resources;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1.class)
    @XmlSchemaType(name = "dateTime")
    protected Calendar startTime;
    protected String status;
    protected String style;
    protected String type;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1.class)
    @XmlSchemaType(name = "dateTime")
    protected Calendar updateDatetime;
    
    public Calendar getAppointmentDate() {
        return this.appointmentDate;
    }
    
    public void setAppointmentDate(final Calendar appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
    
    public Integer getCaisiDemographicId() {
        return this.caisiDemographicId;
    }
    
    public void setCaisiDemographicId(final Integer caisiDemographicId) {
        this.caisiDemographicId = caisiDemographicId;
    }
    
    public String getCaisiProviderId() {
        return this.caisiProviderId;
    }
    
    public void setCaisiProviderId(final String caisiProviderId) {
        this.caisiProviderId = caisiProviderId;
    }
    
    public Calendar getCreateDatetime() {
        return this.createDatetime;
    }
    
    public void setCreateDatetime(final Calendar createDatetime) {
        this.createDatetime = createDatetime;
    }
    
    public Calendar getEndTime() {
        return this.endTime;
    }
    
    public void setEndTime(final Calendar endTime) {
        this.endTime = endTime;
    }
    
    public FacilityIdIntegerCompositePk getFacilityIdIntegerCompositePk() {
        return this.facilityIdIntegerCompositePk;
    }
    
    public void setFacilityIdIntegerCompositePk(final FacilityIdIntegerCompositePk facilityIdIntegerCompositePk) {
        this.facilityIdIntegerCompositePk = facilityIdIntegerCompositePk;
    }
    
    public String getLocation() {
        return this.location;
    }
    
    public void setLocation(final String location) {
        this.location = location;
    }
    
    public String getNotes() {
        return this.notes;
    }
    
    public void setNotes(final String notes) {
        this.notes = notes;
    }
    
    public String getReason() {
        return this.reason;
    }
    
    public void setReason(final String reason) {
        this.reason = reason;
    }
    
    public String getRemarks() {
        return this.remarks;
    }
    
    public void setRemarks(final String remarks) {
        this.remarks = remarks;
    }
    
    public String getResources() {
        return this.resources;
    }
    
    public void setResources(final String resources) {
        this.resources = resources;
    }
    
    public Calendar getStartTime() {
        return this.startTime;
    }
    
    public void setStartTime(final Calendar startTime) {
        this.startTime = startTime;
    }
    
    public String getStatus() {
        return this.status;
    }
    
    public void setStatus(final String status) {
        this.status = status;
    }
    
    public String getStyle() {
        return this.style;
    }
    
    public void setStyle(final String style) {
        this.style = style;
    }
    
    public String getType() {
        return this.type;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    public Calendar getUpdateDatetime() {
        return this.updateDatetime;
    }
    
    public void setUpdateDatetime(final Calendar updateDatetime) {
        this.updateDatetime = updateDatetime;
    }
}
