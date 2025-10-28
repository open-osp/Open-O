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
@XmlType(name = "cachedDemographicAllergy", propOrder = { "agccs", "agcsp", "ageOfOnset", "caisiDemographicId", "description", "entryDate", "facilityIdIntegerCompositePk", "hicSeqNo", "hiclSeqNo", "lifeStage", "onSetCode", "pickId", "reaction", "regionalIdentifier", "severityCode", "startDate", "typeCode" })
public class CachedDemographicAllergy extends AbstractModel implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected int agccs;
    protected int agcsp;
    protected String ageOfOnset;
    protected int caisiDemographicId;
    protected String description;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1.class)
    @XmlSchemaType(name = "dateTime")
    protected Calendar entryDate;
    protected FacilityIdIntegerCompositePk facilityIdIntegerCompositePk;
    protected int hicSeqNo;
    protected int hiclSeqNo;
    protected String lifeStage;
    protected String onSetCode;
    protected int pickId;
    protected String reaction;
    protected String regionalIdentifier;
    protected String severityCode;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1.class)
    @XmlSchemaType(name = "dateTime")
    protected Calendar startDate;
    protected int typeCode;
    
    public int getAgccs() {
        return this.agccs;
    }
    
    public void setAgccs(final int agccs) {
        this.agccs = agccs;
    }
    
    public int getAgcsp() {
        return this.agcsp;
    }
    
    public void setAgcsp(final int agcsp) {
        this.agcsp = agcsp;
    }
    
    public String getAgeOfOnset() {
        return this.ageOfOnset;
    }
    
    public void setAgeOfOnset(final String ageOfOnset) {
        this.ageOfOnset = ageOfOnset;
    }
    
    public int getCaisiDemographicId() {
        return this.caisiDemographicId;
    }
    
    public void setCaisiDemographicId(final int caisiDemographicId) {
        this.caisiDemographicId = caisiDemographicId;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
    
    public Calendar getEntryDate() {
        return this.entryDate;
    }
    
    public void setEntryDate(final Calendar entryDate) {
        this.entryDate = entryDate;
    }
    
    public FacilityIdIntegerCompositePk getFacilityIdIntegerCompositePk() {
        return this.facilityIdIntegerCompositePk;
    }
    
    public void setFacilityIdIntegerCompositePk(final FacilityIdIntegerCompositePk facilityIdIntegerCompositePk) {
        this.facilityIdIntegerCompositePk = facilityIdIntegerCompositePk;
    }
    
    public int getHicSeqNo() {
        return this.hicSeqNo;
    }
    
    public void setHicSeqNo(final int hicSeqNo) {
        this.hicSeqNo = hicSeqNo;
    }
    
    public int getHiclSeqNo() {
        return this.hiclSeqNo;
    }
    
    public void setHiclSeqNo(final int hiclSeqNo) {
        this.hiclSeqNo = hiclSeqNo;
    }
    
    public String getLifeStage() {
        return this.lifeStage;
    }
    
    public void setLifeStage(final String lifeStage) {
        this.lifeStage = lifeStage;
    }
    
    public String getOnSetCode() {
        return this.onSetCode;
    }
    
    public void setOnSetCode(final String onSetCode) {
        this.onSetCode = onSetCode;
    }
    
    public int getPickId() {
        return this.pickId;
    }
    
    public void setPickId(final int pickId) {
        this.pickId = pickId;
    }
    
    public String getReaction() {
        return this.reaction;
    }
    
    public void setReaction(final String reaction) {
        this.reaction = reaction;
    }
    
    public String getRegionalIdentifier() {
        return this.regionalIdentifier;
    }
    
    public void setRegionalIdentifier(final String regionalIdentifier) {
        this.regionalIdentifier = regionalIdentifier;
    }
    
    public String getSeverityCode() {
        return this.severityCode;
    }
    
    public void setSeverityCode(final String severityCode) {
        this.severityCode = severityCode;
    }
    
    public Calendar getStartDate() {
        return this.startDate;
    }
    
    public void setStartDate(final Calendar startDate) {
        this.startDate = startDate;
    }
    
    public int getTypeCode() {
        return this.typeCode;
    }
    
    public void setTypeCode(final int typeCode) {
        this.typeCode = typeCode;
    }
}
