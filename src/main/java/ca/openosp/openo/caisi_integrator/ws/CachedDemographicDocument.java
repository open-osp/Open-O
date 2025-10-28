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
@XmlType(name = "cachedDemographicDocument", propOrder = { "appointmentNo", "caisiDemographicId", "contentType", "description", "docCreator", "docFilename", "docType", "docXml", "facilityIntegerPk", "numberOfPages", "observationDate", "programId", "public1", "responsible", "reviewDateTime", "reviewer", "source", "status", "updateDateTime" })
public class CachedDemographicDocument extends AbstractModel implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected int appointmentNo;
    protected int caisiDemographicId;
    protected String contentType;
    protected String description;
    protected String docCreator;
    protected String docFilename;
    protected String docType;
    protected String docXml;
    protected FacilityIdIntegerCompositePk facilityIntegerPk;
    protected int numberOfPages;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1.class)
    @XmlSchemaType(name = "dateTime")
    protected Calendar observationDate;
    protected Integer programId;
    protected int public1;
    protected String responsible;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1.class)
    @XmlSchemaType(name = "dateTime")
    protected Calendar reviewDateTime;
    protected String reviewer;
    protected String source;
    protected String status;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1.class)
    @XmlSchemaType(name = "dateTime")
    protected Calendar updateDateTime;
    
    public int getAppointmentNo() {
        return this.appointmentNo;
    }
    
    public void setAppointmentNo(final int appointmentNo) {
        this.appointmentNo = appointmentNo;
    }
    
    public int getCaisiDemographicId() {
        return this.caisiDemographicId;
    }
    
    public void setCaisiDemographicId(final int caisiDemographicId) {
        this.caisiDemographicId = caisiDemographicId;
    }
    
    public String getContentType() {
        return this.contentType;
    }
    
    public void setContentType(final String contentType) {
        this.contentType = contentType;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
    
    public String getDocCreator() {
        return this.docCreator;
    }
    
    public void setDocCreator(final String docCreator) {
        this.docCreator = docCreator;
    }
    
    public String getDocFilename() {
        return this.docFilename;
    }
    
    public void setDocFilename(final String docFilename) {
        this.docFilename = docFilename;
    }
    
    public String getDocType() {
        return this.docType;
    }
    
    public void setDocType(final String docType) {
        this.docType = docType;
    }
    
    public String getDocXml() {
        return this.docXml;
    }
    
    public void setDocXml(final String docXml) {
        this.docXml = docXml;
    }
    
    public FacilityIdIntegerCompositePk getFacilityIntegerPk() {
        return this.facilityIntegerPk;
    }
    
    public void setFacilityIntegerPk(final FacilityIdIntegerCompositePk facilityIntegerPk) {
        this.facilityIntegerPk = facilityIntegerPk;
    }
    
    public int getNumberOfPages() {
        return this.numberOfPages;
    }
    
    public void setNumberOfPages(final int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }
    
    public Calendar getObservationDate() {
        return this.observationDate;
    }
    
    public void setObservationDate(final Calendar observationDate) {
        this.observationDate = observationDate;
    }
    
    public Integer getProgramId() {
        return this.programId;
    }
    
    public void setProgramId(final Integer programId) {
        this.programId = programId;
    }
    
    public int getPublic1() {
        return this.public1;
    }
    
    public void setPublic1(final int public1) {
        this.public1 = public1;
    }
    
    public String getResponsible() {
        return this.responsible;
    }
    
    public void setResponsible(final String responsible) {
        this.responsible = responsible;
    }
    
    public Calendar getReviewDateTime() {
        return this.reviewDateTime;
    }
    
    public void setReviewDateTime(final Calendar reviewDateTime) {
        this.reviewDateTime = reviewDateTime;
    }
    
    public String getReviewer() {
        return this.reviewer;
    }
    
    public void setReviewer(final String reviewer) {
        this.reviewer = reviewer;
    }
    
    public String getSource() {
        return this.source;
    }
    
    public void setSource(final String source) {
        this.source = source;
    }
    
    public String getStatus() {
        return this.status;
    }
    
    public void setStatus(final String status) {
        this.status = status;
    }
    
    public Calendar getUpdateDateTime() {
        return this.updateDateTime;
    }
    
    public void setUpdateDateTime(final Calendar updateDateTime) {
        this.updateDateTime = updateDateTime;
    }
}
