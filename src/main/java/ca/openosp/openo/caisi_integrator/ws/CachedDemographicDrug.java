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
@XmlType(name = "cachedDemographicDrug", propOrder = { "archived", "archivedDate", "archivedReason", "atc", "brandName", "caisiDemographicId", "caisiProviderId", "createDate", "customInstructions", "customName", "dosage", "drugForm", "durUnit", "duration", "endDate", "facilityIdIntegerCompositePk", "freqCode", "genericName", "lastRefillDate", "longTerm", "method", "noSubs", "pastMed", "patientCompliance", "prn", "quantity", "regionalIdentifier", "repeats", "route", "rxDate", "scriptNo", "special", "takeMax", "takeMin", "unit", "unitName" })
public class CachedDemographicDrug extends AbstractModel implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected boolean archived;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1.class)
    @XmlSchemaType(name = "dateTime")
    protected Calendar archivedDate;
    protected String archivedReason;
    protected String atc;
    protected String brandName;
    protected Integer caisiDemographicId;
    protected String caisiProviderId;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1.class)
    @XmlSchemaType(name = "dateTime")
    protected Calendar createDate;
    protected boolean customInstructions;
    protected String customName;
    protected String dosage;
    protected String drugForm;
    protected String durUnit;
    protected String duration;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1.class)
    @XmlSchemaType(name = "dateTime")
    protected Calendar endDate;
    protected FacilityIdIntegerCompositePk facilityIdIntegerCompositePk;
    protected String freqCode;
    protected String genericName;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1.class)
    @XmlSchemaType(name = "dateTime")
    protected Calendar lastRefillDate;
    protected Boolean longTerm;
    protected String method;
    protected boolean noSubs;
    protected Boolean pastMed;
    protected Boolean patientCompliance;
    protected boolean prn;
    protected String quantity;
    protected String regionalIdentifier;
    protected int repeats;
    protected String route;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1.class)
    @XmlSchemaType(name = "dateTime")
    protected Calendar rxDate;
    protected int scriptNo;
    protected String special;
    protected float takeMax;
    protected float takeMin;
    protected String unit;
    protected String unitName;
    
    public boolean isArchived() {
        return this.archived;
    }
    
    public void setArchived(final boolean archived) {
        this.archived = archived;
    }
    
    public Calendar getArchivedDate() {
        return this.archivedDate;
    }
    
    public void setArchivedDate(final Calendar archivedDate) {
        this.archivedDate = archivedDate;
    }
    
    public String getArchivedReason() {
        return this.archivedReason;
    }
    
    public void setArchivedReason(final String archivedReason) {
        this.archivedReason = archivedReason;
    }
    
    public String getAtc() {
        return this.atc;
    }
    
    public void setAtc(final String atc) {
        this.atc = atc;
    }
    
    public String getBrandName() {
        return this.brandName;
    }
    
    public void setBrandName(final String brandName) {
        this.brandName = brandName;
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
    
    public Calendar getCreateDate() {
        return this.createDate;
    }
    
    public void setCreateDate(final Calendar createDate) {
        this.createDate = createDate;
    }
    
    public boolean isCustomInstructions() {
        return this.customInstructions;
    }
    
    public void setCustomInstructions(final boolean customInstructions) {
        this.customInstructions = customInstructions;
    }
    
    public String getCustomName() {
        return this.customName;
    }
    
    public void setCustomName(final String customName) {
        this.customName = customName;
    }
    
    public String getDosage() {
        return this.dosage;
    }
    
    public void setDosage(final String dosage) {
        this.dosage = dosage;
    }
    
    public String getDrugForm() {
        return this.drugForm;
    }
    
    public void setDrugForm(final String drugForm) {
        this.drugForm = drugForm;
    }
    
    public String getDurUnit() {
        return this.durUnit;
    }
    
    public void setDurUnit(final String durUnit) {
        this.durUnit = durUnit;
    }
    
    public String getDuration() {
        return this.duration;
    }
    
    public void setDuration(final String duration) {
        this.duration = duration;
    }
    
    public Calendar getEndDate() {
        return this.endDate;
    }
    
    public void setEndDate(final Calendar endDate) {
        this.endDate = endDate;
    }
    
    public FacilityIdIntegerCompositePk getFacilityIdIntegerCompositePk() {
        return this.facilityIdIntegerCompositePk;
    }
    
    public void setFacilityIdIntegerCompositePk(final FacilityIdIntegerCompositePk facilityIdIntegerCompositePk) {
        this.facilityIdIntegerCompositePk = facilityIdIntegerCompositePk;
    }
    
    public String getFreqCode() {
        return this.freqCode;
    }
    
    public void setFreqCode(final String freqCode) {
        this.freqCode = freqCode;
    }
    
    public String getGenericName() {
        return this.genericName;
    }
    
    public void setGenericName(final String genericName) {
        this.genericName = genericName;
    }
    
    public Calendar getLastRefillDate() {
        return this.lastRefillDate;
    }
    
    public void setLastRefillDate(final Calendar lastRefillDate) {
        this.lastRefillDate = lastRefillDate;
    }
    
    public Boolean isLongTerm() {
        return this.longTerm;
    }
    
    public void setLongTerm(final Boolean longTerm) {
        this.longTerm = longTerm;
    }
    
    public String getMethod() {
        return this.method;
    }
    
    public void setMethod(final String method) {
        this.method = method;
    }
    
    public boolean isNoSubs() {
        return this.noSubs;
    }
    
    public void setNoSubs(final boolean noSubs) {
        this.noSubs = noSubs;
    }
    
    public Boolean isPastMed() {
        return this.pastMed;
    }
    
    public void setPastMed(final Boolean pastMed) {
        this.pastMed = pastMed;
    }
    
    public Boolean isPatientCompliance() {
        return this.patientCompliance;
    }
    
    public void setPatientCompliance(final Boolean patientCompliance) {
        this.patientCompliance = patientCompliance;
    }
    
    public boolean isPrn() {
        return this.prn;
    }
    
    public void setPrn(final boolean prn) {
        this.prn = prn;
    }
    
    public String getQuantity() {
        return this.quantity;
    }
    
    public void setQuantity(final String quantity) {
        this.quantity = quantity;
    }
    
    public String getRegionalIdentifier() {
        return this.regionalIdentifier;
    }
    
    public void setRegionalIdentifier(final String regionalIdentifier) {
        this.regionalIdentifier = regionalIdentifier;
    }
    
    public int getRepeats() {
        return this.repeats;
    }
    
    public void setRepeats(final int repeats) {
        this.repeats = repeats;
    }
    
    public String getRoute() {
        return this.route;
    }
    
    public void setRoute(final String route) {
        this.route = route;
    }
    
    public Calendar getRxDate() {
        return this.rxDate;
    }
    
    public void setRxDate(final Calendar rxDate) {
        this.rxDate = rxDate;
    }
    
    public int getScriptNo() {
        return this.scriptNo;
    }
    
    public void setScriptNo(final int scriptNo) {
        this.scriptNo = scriptNo;
    }
    
    public String getSpecial() {
        return this.special;
    }
    
    public void setSpecial(final String special) {
        this.special = special;
    }
    
    public float getTakeMax() {
        return this.takeMax;
    }
    
    public void setTakeMax(final float takeMax) {
        this.takeMax = takeMax;
    }
    
    public float getTakeMin() {
        return this.takeMin;
    }
    
    public void setTakeMin(final float takeMin) {
        this.takeMin = takeMin;
    }
    
    public String getUnit() {
        return this.unit;
    }
    
    public void setUnit(final String unit) {
        this.unit = unit;
    }
    
    public String getUnitName() {
        return this.unitName;
    }
    
    public void setUnitName(final String unitName) {
        this.unitName = unitName;
    }
}
