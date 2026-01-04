package ca.openosp.openo.caisi_integrator.dao;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import org.apache.openjpa.util.ObjectId;
import org.apache.openjpa.enhance.FieldConsumer;
import org.apache.openjpa.util.InternalException;
import org.apache.openjpa.enhance.FieldSupplier;
import org.apache.openjpa.enhance.RedefinitionHelper;
import org.apache.openjpa.enhance.PersistenceCapable;
import org.apache.openjpa.enhance.PCRegistry;
import org.apache.openjpa.enhance.StateManager;
import javax.persistence.TemporalType;
import javax.persistence.Temporal;
import java.util.Date;
import org.apache.openjpa.persistence.jdbc.Index;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class CachedDemographicDrug extends AbstractModel<FacilityIdIntegerCompositePk> implements PersistenceCapable
{
    @EmbeddedId
    private FacilityIdIntegerCompositePk facilityIdIntegerCompositePk;
    @Column(nullable = false, length = 16)
    private String caisiProviderId;
    @Column(nullable = false)
    @Index
    private Integer caisiDemographicId;
    @Temporal(TemporalType.DATE)
    private Date rxDate;
    @Temporal(TemporalType.DATE)
    private Date endDate;
    @Column(length = 255)
    private String brandName;
    @Column(length = 255)
    private String customName;
    private float takeMin;
    private float takeMax;
    @Column(length = 64)
    private String freqCode;
    @Column(length = 64)
    private String duration;
    @Column(length = 64)
    private String durUnit;
    @Column(length = 64)
    private String quantity;
    private int repeats;
    @Temporal(TemporalType.DATE)
    private Date lastRefillDate;
    @Column(columnDefinition = "tinyint(1)")
    private boolean noSubs;
    @Column(columnDefinition = "tinyint(1)")
    private boolean prn;
    @Column(columnDefinition = "text")
    private String special;
    @Column(columnDefinition = "tinyint(1)")
    private boolean archived;
    @Column(length = 100)
    private String archivedReason;
    @Temporal(TemporalType.TIMESTAMP)
    private Date archivedDate;
    @Column(length = 255)
    private String genericName;
    @Column(length = 64)
    private String atc;
    private int scriptNo;
    @Column(length = 64)
    private String regionalIdentifier;
    @Column(length = 64)
    private String unit;
    @Column(length = 64)
    private String method;
    @Column(length = 64)
    private String route;
    @Column(length = 64)
    private String drugForm;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Column(columnDefinition = "text")
    private String dosage;
    @Column(columnDefinition = "tinyint(1)")
    private boolean customInstructions;
    @Column(length = 50)
    private String unitName;
    @Column(columnDefinition = "tinyint(1)")
    private Boolean longTerm;
    @Column(columnDefinition = "tinyint(1)")
    private Boolean pastMed;
    @Column(columnDefinition = "tinyint(1)")
    private Boolean patientCompliance;
    private static int pcInheritedFieldCount;
    private static String[] pcFieldNames;
    private static Class[] pcFieldTypes;
    private static byte[] pcFieldFlags;
    private static Class pcPCSuperclass;
    protected transient StateManager pcStateManager;
    static /* synthetic */ Class class$Ljava$util$Date;
    static /* synthetic */ Class class$Ljava$lang$String;
    static /* synthetic */ Class class$Ljava$lang$Integer;
    static /* synthetic */ Class class$Lca$openosp$openo$caisi_integrator$dao$FacilityIdIntegerCompositePk;
    static /* synthetic */ Class class$Ljava$lang$Boolean;
    static /* synthetic */ Class class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicDrug;
    private transient Object pcDetachedState;
    private static final long serialVersionUID;
    
    public CachedDemographicDrug() {
        this.caisiProviderId = null;
        this.caisiDemographicId = null;
        this.rxDate = null;
        this.endDate = null;
        this.brandName = null;
        this.customName = null;
        this.takeMin = 0.0f;
        this.takeMax = 0.0f;
        this.freqCode = null;
        this.duration = null;
        this.durUnit = null;
        this.quantity = null;
        this.repeats = 0;
        this.lastRefillDate = null;
        this.special = null;
        this.genericName = null;
        this.atc = null;
        this.scriptNo = 0;
        this.regionalIdentifier = null;
        this.unit = null;
        this.method = null;
        this.route = null;
        this.drugForm = null;
        this.createDate = null;
        this.dosage = null;
        this.unitName = null;
    }
    
    @Override
    public FacilityIdIntegerCompositePk getId() {
        return pcGetfacilityIdIntegerCompositePk(this);
    }
    
    public FacilityIdIntegerCompositePk getFacilityIdIntegerCompositePk() {
        return pcGetfacilityIdIntegerCompositePk(this);
    }
    
    public void setFacilityIdIntegerCompositePk(final FacilityIdIntegerCompositePk facilityIdIntegerCompositePk) {
        pcSetfacilityIdIntegerCompositePk(this, facilityIdIntegerCompositePk);
    }
    
    public String getCaisiProviderId() {
        return pcGetcaisiProviderId(this);
    }
    
    public void setCaisiProviderId(final String caisiProviderId) {
        pcSetcaisiProviderId(this, caisiProviderId);
    }
    
    public Integer getCaisiDemographicId() {
        return pcGetcaisiDemographicId(this);
    }
    
    public void setCaisiDemographicId(final Integer caisiDemographicId) {
        pcSetcaisiDemographicId(this, caisiDemographicId);
    }
    
    public Date getRxDate() {
        return pcGetrxDate(this);
    }
    
    public void setRxDate(final Date rxDate) {
        pcSetrxDate(this, rxDate);
    }
    
    public Date getEndDate() {
        return pcGetendDate(this);
    }
    
    public void setEndDate(final Date endDate) {
        pcSetendDate(this, endDate);
    }
    
    public String getBrandName() {
        return pcGetbrandName(this);
    }
    
    public void setBrandName(final String brandName) {
        pcSetbrandName(this, brandName);
    }
    
    public String getCustomName() {
        return pcGetcustomName(this);
    }
    
    public void setCustomName(final String customName) {
        pcSetcustomName(this, customName);
    }
    
    public float getTakeMin() {
        return pcGettakeMin(this);
    }
    
    public void setTakeMin(final float takeMin) {
        pcSettakeMin(this, takeMin);
    }
    
    public float getTakeMax() {
        return pcGettakeMax(this);
    }
    
    public void setTakeMax(final float takeMax) {
        pcSettakeMax(this, takeMax);
    }
    
    public String getFreqCode() {
        return pcGetfreqCode(this);
    }
    
    public void setFreqCode(final String freqCode) {
        pcSetfreqCode(this, freqCode);
    }
    
    public String getDuration() {
        return pcGetduration(this);
    }
    
    public void setDuration(final String duration) {
        pcSetduration(this, duration);
    }
    
    public String getDurUnit() {
        return pcGetdurUnit(this);
    }
    
    public void setDurUnit(final String durUnit) {
        pcSetdurUnit(this, durUnit);
    }
    
    public String getQuantity() {
        return pcGetquantity(this);
    }
    
    public void setQuantity(final String quantity) {
        pcSetquantity(this, quantity);
    }
    
    public int getRepeats() {
        return pcGetrepeats(this);
    }
    
    public void setRepeats(final int repeats) {
        pcSetrepeats(this, repeats);
    }
    
    public Date getLastRefillDate() {
        return pcGetlastRefillDate(this);
    }
    
    public void setLastRefillDate(final Date lastRefillDate) {
        pcSetlastRefillDate(this, lastRefillDate);
    }
    
    public boolean isNoSubs() {
        return pcGetnoSubs(this);
    }
    
    public void setNoSubs(final boolean noSubs) {
        pcSetnoSubs(this, noSubs);
    }
    
    public boolean isPrn() {
        return pcGetprn(this);
    }
    
    public void setPrn(final boolean prn) {
        pcSetprn(this, prn);
    }
    
    public String getSpecial() {
        return pcGetspecial(this);
    }
    
    public void setSpecial(final String special) {
        pcSetspecial(this, special);
    }
    
    public boolean isArchived() {
        return pcGetarchived(this);
    }
    
    public void setArchived(final boolean archived) {
        pcSetarchived(this, archived);
    }
    
    public String getArchivedReason() {
        return pcGetarchivedReason(this);
    }
    
    public void setArchivedReason(final String archivedReason) {
        pcSetarchivedReason(this, archivedReason);
    }
    
    public Date getArchivedDate() {
        return pcGetarchivedDate(this);
    }
    
    public void setArchivedDate(final Date archivedDate) {
        pcSetarchivedDate(this, archivedDate);
    }
    
    public String getGenericName() {
        return pcGetgenericName(this);
    }
    
    public void setGenericName(final String genericName) {
        pcSetgenericName(this, genericName);
    }
    
    public String getAtc() {
        return pcGetatc(this);
    }
    
    public void setAtc(final String atc) {
        pcSetatc(this, atc);
    }
    
    public int getScriptNo() {
        return pcGetscriptNo(this);
    }
    
    public void setScriptNo(final int scriptNo) {
        pcSetscriptNo(this, scriptNo);
    }
    
    public String getRegionalIdentifier() {
        return pcGetregionalIdentifier(this);
    }
    
    public void setRegionalIdentifier(final String regionalIdentifier) {
        pcSetregionalIdentifier(this, regionalIdentifier);
    }
    
    public String getUnit() {
        return pcGetunit(this);
    }
    
    public void setUnit(final String unit) {
        pcSetunit(this, unit);
    }
    
    public String getMethod() {
        return pcGetmethod(this);
    }
    
    public void setMethod(final String method) {
        pcSetmethod(this, method);
    }
    
    public String getRoute() {
        return pcGetroute(this);
    }
    
    public void setRoute(final String route) {
        pcSetroute(this, route);
    }
    
    public String getDrugForm() {
        return pcGetdrugForm(this);
    }
    
    public void setDrugForm(final String drugForm) {
        pcSetdrugForm(this, drugForm);
    }
    
    public Date getCreateDate() {
        return pcGetcreateDate(this);
    }
    
    public void setCreateDate(final Date createDate) {
        pcSetcreateDate(this, createDate);
    }
    
    public String getDosage() {
        return pcGetdosage(this);
    }
    
    public void setDosage(final String dosage) {
        pcSetdosage(this, dosage);
    }
    
    public boolean isCustomInstructions() {
        return pcGetcustomInstructions(this);
    }
    
    public void setCustomInstructions(final boolean customInstructions) {
        pcSetcustomInstructions(this, customInstructions);
    }
    
    public String getUnitName() {
        return pcGetunitName(this);
    }
    
    public void setUnitName(final String unitName) {
        pcSetunitName(this, unitName);
    }
    
    public Boolean getLongTerm() {
        return pcGetlongTerm(this);
    }
    
    public void setLongTerm(final Boolean longTerm) {
        pcSetlongTerm(this, longTerm);
    }
    
    public Boolean getPastMed() {
        return pcGetpastMed(this);
    }
    
    public void setPastMed(final Boolean pastMed) {
        pcSetpastMed(this, pastMed);
    }
    
    public Boolean getPatientCompliance() {
        return pcGetpatientCompliance(this);
    }
    
    public void setPatientCompliance(final Boolean patientCompliance) {
        pcSetpatientCompliance(this, patientCompliance);
    }
    
    public int pcGetEnhancementContractVersion() {
        return 2;
    }
    
    static {
        serialVersionUID = -1659277755766988859L;
        CachedDemographicDrug.pcFieldNames = new String[] { "archived", "archivedDate", "archivedReason", "atc", "brandName", "caisiDemographicId", "caisiProviderId", "createDate", "customInstructions", "customName", "dosage", "drugForm", "durUnit", "duration", "endDate", "facilityIdIntegerCompositePk", "freqCode", "genericName", "lastRefillDate", "longTerm", "method", "noSubs", "pastMed", "patientCompliance", "prn", "quantity", "regionalIdentifier", "repeats", "route", "rxDate", "scriptNo", "special", "takeMax", "takeMin", "unit", "unitName" };
        CachedDemographicDrug.pcFieldTypes = new Class[] { Boolean.TYPE, (CachedDemographicDrug.class$Ljava$util$Date != null) ? CachedDemographicDrug.class$Ljava$util$Date : (CachedDemographicDrug.class$Ljava$util$Date = class$("java.util.Date")), (CachedDemographicDrug.class$Ljava$lang$String != null) ? CachedDemographicDrug.class$Ljava$lang$String : (CachedDemographicDrug.class$Ljava$lang$String = class$("java.lang.String")), (CachedDemographicDrug.class$Ljava$lang$String != null) ? CachedDemographicDrug.class$Ljava$lang$String : (CachedDemographicDrug.class$Ljava$lang$String = class$("java.lang.String")), (CachedDemographicDrug.class$Ljava$lang$String != null) ? CachedDemographicDrug.class$Ljava$lang$String : (CachedDemographicDrug.class$Ljava$lang$String = class$("java.lang.String")), (CachedDemographicDrug.class$Ljava$lang$Integer != null) ? CachedDemographicDrug.class$Ljava$lang$Integer : (CachedDemographicDrug.class$Ljava$lang$Integer = class$("java.lang.Integer")), (CachedDemographicDrug.class$Ljava$lang$String != null) ? CachedDemographicDrug.class$Ljava$lang$String : (CachedDemographicDrug.class$Ljava$lang$String = class$("java.lang.String")), (CachedDemographicDrug.class$Ljava$util$Date != null) ? CachedDemographicDrug.class$Ljava$util$Date : (CachedDemographicDrug.class$Ljava$util$Date = class$("java.util.Date")), Boolean.TYPE, (CachedDemographicDrug.class$Ljava$lang$String != null) ? CachedDemographicDrug.class$Ljava$lang$String : (CachedDemographicDrug.class$Ljava$lang$String = class$("java.lang.String")), (CachedDemographicDrug.class$Ljava$lang$String != null) ? CachedDemographicDrug.class$Ljava$lang$String : (CachedDemographicDrug.class$Ljava$lang$String = class$("java.lang.String")), (CachedDemographicDrug.class$Ljava$lang$String != null) ? CachedDemographicDrug.class$Ljava$lang$String : (CachedDemographicDrug.class$Ljava$lang$String = class$("java.lang.String")), (CachedDemographicDrug.class$Ljava$lang$String != null) ? CachedDemographicDrug.class$Ljava$lang$String : (CachedDemographicDrug.class$Ljava$lang$String = class$("java.lang.String")), (CachedDemographicDrug.class$Ljava$lang$String != null) ? CachedDemographicDrug.class$Ljava$lang$String : (CachedDemographicDrug.class$Ljava$lang$String = class$("java.lang.String")), (CachedDemographicDrug.class$Ljava$util$Date != null) ? CachedDemographicDrug.class$Ljava$util$Date : (CachedDemographicDrug.class$Ljava$util$Date = class$("java.util.Date")), (CachedDemographicDrug.class$Lca$openosp$openo$caisi_integrator$dao$FacilityIdIntegerCompositePk != null) ? CachedDemographicDrug.class$Lca$openosp$openo$caisi_integrator$dao$FacilityIdIntegerCompositePk : (CachedDemographicDrug.class$Lca$openosp$openo$caisi_integrator$dao$FacilityIdIntegerCompositePk = class$("ca.openosp.openo.caisi_integrator.dao.FacilityIdIntegerCompositePk")), (CachedDemographicDrug.class$Ljava$lang$String != null) ? CachedDemographicDrug.class$Ljava$lang$String : (CachedDemographicDrug.class$Ljava$lang$String = class$("java.lang.String")), (CachedDemographicDrug.class$Ljava$lang$String != null) ? CachedDemographicDrug.class$Ljava$lang$String : (CachedDemographicDrug.class$Ljava$lang$String = class$("java.lang.String")), (CachedDemographicDrug.class$Ljava$util$Date != null) ? CachedDemographicDrug.class$Ljava$util$Date : (CachedDemographicDrug.class$Ljava$util$Date = class$("java.util.Date")), (CachedDemographicDrug.class$Ljava$lang$Boolean != null) ? CachedDemographicDrug.class$Ljava$lang$Boolean : (CachedDemographicDrug.class$Ljava$lang$Boolean = class$("java.lang.Boolean")), (CachedDemographicDrug.class$Ljava$lang$String != null) ? CachedDemographicDrug.class$Ljava$lang$String : (CachedDemographicDrug.class$Ljava$lang$String = class$("java.lang.String")), Boolean.TYPE, (CachedDemographicDrug.class$Ljava$lang$Boolean != null) ? CachedDemographicDrug.class$Ljava$lang$Boolean : (CachedDemographicDrug.class$Ljava$lang$Boolean = class$("java.lang.Boolean")), (CachedDemographicDrug.class$Ljava$lang$Boolean != null) ? CachedDemographicDrug.class$Ljava$lang$Boolean : (CachedDemographicDrug.class$Ljava$lang$Boolean = class$("java.lang.Boolean")), Boolean.TYPE, (CachedDemographicDrug.class$Ljava$lang$String != null) ? CachedDemographicDrug.class$Ljava$lang$String : (CachedDemographicDrug.class$Ljava$lang$String = class$("java.lang.String")), (CachedDemographicDrug.class$Ljava$lang$String != null) ? CachedDemographicDrug.class$Ljava$lang$String : (CachedDemographicDrug.class$Ljava$lang$String = class$("java.lang.String")), Integer.TYPE, (CachedDemographicDrug.class$Ljava$lang$String != null) ? CachedDemographicDrug.class$Ljava$lang$String : (CachedDemographicDrug.class$Ljava$lang$String = class$("java.lang.String")), (CachedDemographicDrug.class$Ljava$util$Date != null) ? CachedDemographicDrug.class$Ljava$util$Date : (CachedDemographicDrug.class$Ljava$util$Date = class$("java.util.Date")), Integer.TYPE, (CachedDemographicDrug.class$Ljava$lang$String != null) ? CachedDemographicDrug.class$Ljava$lang$String : (CachedDemographicDrug.class$Ljava$lang$String = class$("java.lang.String")), Float.TYPE, Float.TYPE, (CachedDemographicDrug.class$Ljava$lang$String != null) ? CachedDemographicDrug.class$Ljava$lang$String : (CachedDemographicDrug.class$Ljava$lang$String = class$("java.lang.String")), (CachedDemographicDrug.class$Ljava$lang$String != null) ? CachedDemographicDrug.class$Ljava$lang$String : (CachedDemographicDrug.class$Ljava$lang$String = class$("java.lang.String")) };
        CachedDemographicDrug.pcFieldFlags = new byte[] { 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26 };
        PCRegistry.register((CachedDemographicDrug.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicDrug != null) ? CachedDemographicDrug.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicDrug : (CachedDemographicDrug.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicDrug = class$("ca.openosp.openo.caisi_integrator.dao.CachedDemographicDrug")), CachedDemographicDrug.pcFieldNames, CachedDemographicDrug.pcFieldTypes, CachedDemographicDrug.pcFieldFlags, CachedDemographicDrug.pcPCSuperclass, "CachedDemographicDrug", (PersistenceCapable)new CachedDemographicDrug());
    }
    
    static /* synthetic */ Class class$(final String className) {
        try {
            return Class.forName(className);
        }
        catch (final ClassNotFoundException ex) {
            throw new NoClassDefFoundError(ex.getMessage());
        }
    }
    
    protected void pcClearFields() {
        this.archived = false;
        this.archivedDate = null;
        this.archivedReason = null;
        this.atc = null;
        this.brandName = null;
        this.caisiDemographicId = null;
        this.caisiProviderId = null;
        this.createDate = null;
        this.customInstructions = false;
        this.customName = null;
        this.dosage = null;
        this.drugForm = null;
        this.durUnit = null;
        this.duration = null;
        this.endDate = null;
        this.facilityIdIntegerCompositePk = null;
        this.freqCode = null;
        this.genericName = null;
        this.lastRefillDate = null;
        this.longTerm = null;
        this.method = null;
        this.noSubs = false;
        this.pastMed = null;
        this.patientCompliance = null;
        this.prn = false;
        this.quantity = null;
        this.regionalIdentifier = null;
        this.repeats = 0;
        this.route = null;
        this.rxDate = null;
        this.scriptNo = 0;
        this.special = null;
        this.takeMax = 0.0f;
        this.takeMin = 0.0f;
        this.unit = null;
        this.unitName = null;
    }
    
    public PersistenceCapable pcNewInstance(final StateManager pcStateManager, final Object o, final boolean b) {
        final CachedDemographicDrug cachedDemographicDrug = new CachedDemographicDrug();
        if (b) {
            cachedDemographicDrug.pcClearFields();
        }
        cachedDemographicDrug.pcStateManager = pcStateManager;
        cachedDemographicDrug.pcCopyKeyFieldsFromObjectId(o);
        return (PersistenceCapable)cachedDemographicDrug;
    }
    
    public PersistenceCapable pcNewInstance(final StateManager pcStateManager, final boolean b) {
        final CachedDemographicDrug cachedDemographicDrug = new CachedDemographicDrug();
        if (b) {
            cachedDemographicDrug.pcClearFields();
        }
        cachedDemographicDrug.pcStateManager = pcStateManager;
        return (PersistenceCapable)cachedDemographicDrug;
    }
    
    protected static int pcGetManagedFieldCount() {
        return 36;
    }
    
    public void pcReplaceField(final int n) {
        final int n2 = n - CachedDemographicDrug.pcInheritedFieldCount;
        if (n2 < 0) {
            throw new IllegalArgumentException();
        }
        switch (n2) {
            case 0: {
                this.archived = this.pcStateManager.replaceBooleanField((PersistenceCapable)this, n);
                return;
            }
            case 1: {
                this.archivedDate = (Date)this.pcStateManager.replaceObjectField((PersistenceCapable)this, n);
                return;
            }
            case 2: {
                this.archivedReason = this.pcStateManager.replaceStringField((PersistenceCapable)this, n);
                return;
            }
            case 3: {
                this.atc = this.pcStateManager.replaceStringField((PersistenceCapable)this, n);
                return;
            }
            case 4: {
                this.brandName = this.pcStateManager.replaceStringField((PersistenceCapable)this, n);
                return;
            }
            case 5: {
                this.caisiDemographicId = (Integer)this.pcStateManager.replaceObjectField((PersistenceCapable)this, n);
                return;
            }
            case 6: {
                this.caisiProviderId = this.pcStateManager.replaceStringField((PersistenceCapable)this, n);
                return;
            }
            case 7: {
                this.createDate = (Date)this.pcStateManager.replaceObjectField((PersistenceCapable)this, n);
                return;
            }
            case 8: {
                this.customInstructions = this.pcStateManager.replaceBooleanField((PersistenceCapable)this, n);
                return;
            }
            case 9: {
                this.customName = this.pcStateManager.replaceStringField((PersistenceCapable)this, n);
                return;
            }
            case 10: {
                this.dosage = this.pcStateManager.replaceStringField((PersistenceCapable)this, n);
                return;
            }
            case 11: {
                this.drugForm = this.pcStateManager.replaceStringField((PersistenceCapable)this, n);
                return;
            }
            case 12: {
                this.durUnit = this.pcStateManager.replaceStringField((PersistenceCapable)this, n);
                return;
            }
            case 13: {
                this.duration = this.pcStateManager.replaceStringField((PersistenceCapable)this, n);
                return;
            }
            case 14: {
                this.endDate = (Date)this.pcStateManager.replaceObjectField((PersistenceCapable)this, n);
                return;
            }
            case 15: {
                this.facilityIdIntegerCompositePk = (FacilityIdIntegerCompositePk)this.pcStateManager.replaceObjectField((PersistenceCapable)this, n);
                return;
            }
            case 16: {
                this.freqCode = this.pcStateManager.replaceStringField((PersistenceCapable)this, n);
                return;
            }
            case 17: {
                this.genericName = this.pcStateManager.replaceStringField((PersistenceCapable)this, n);
                return;
            }
            case 18: {
                this.lastRefillDate = (Date)this.pcStateManager.replaceObjectField((PersistenceCapable)this, n);
                return;
            }
            case 19: {
                this.longTerm = (Boolean)this.pcStateManager.replaceObjectField((PersistenceCapable)this, n);
                return;
            }
            case 20: {
                this.method = this.pcStateManager.replaceStringField((PersistenceCapable)this, n);
                return;
            }
            case 21: {
                this.noSubs = this.pcStateManager.replaceBooleanField((PersistenceCapable)this, n);
                return;
            }
            case 22: {
                this.pastMed = (Boolean)this.pcStateManager.replaceObjectField((PersistenceCapable)this, n);
                return;
            }
            case 23: {
                this.patientCompliance = (Boolean)this.pcStateManager.replaceObjectField((PersistenceCapable)this, n);
                return;
            }
            case 24: {
                this.prn = this.pcStateManager.replaceBooleanField((PersistenceCapable)this, n);
                return;
            }
            case 25: {
                this.quantity = this.pcStateManager.replaceStringField((PersistenceCapable)this, n);
                return;
            }
            case 26: {
                this.regionalIdentifier = this.pcStateManager.replaceStringField((PersistenceCapable)this, n);
                return;
            }
            case 27: {
                this.repeats = this.pcStateManager.replaceIntField((PersistenceCapable)this, n);
                return;
            }
            case 28: {
                this.route = this.pcStateManager.replaceStringField((PersistenceCapable)this, n);
                return;
            }
            case 29: {
                this.rxDate = (Date)this.pcStateManager.replaceObjectField((PersistenceCapable)this, n);
                return;
            }
            case 30: {
                this.scriptNo = this.pcStateManager.replaceIntField((PersistenceCapable)this, n);
                return;
            }
            case 31: {
                this.special = this.pcStateManager.replaceStringField((PersistenceCapable)this, n);
                return;
            }
            case 32: {
                this.takeMax = this.pcStateManager.replaceFloatField((PersistenceCapable)this, n);
                return;
            }
            case 33: {
                this.takeMin = this.pcStateManager.replaceFloatField((PersistenceCapable)this, n);
                return;
            }
            case 34: {
                this.unit = this.pcStateManager.replaceStringField((PersistenceCapable)this, n);
                return;
            }
            case 35: {
                this.unitName = this.pcStateManager.replaceStringField((PersistenceCapable)this, n);
                return;
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
    }
    
    public void pcReplaceFields(final int[] array) {
        for (int i = 0; i < array.length; ++i) {
            this.pcReplaceField(array[i]);
        }
    }
    
    public void pcProvideField(final int n) {
        final int n2 = n - CachedDemographicDrug.pcInheritedFieldCount;
        if (n2 < 0) {
            throw new IllegalArgumentException();
        }
        switch (n2) {
            case 0: {
                this.pcStateManager.providedBooleanField((PersistenceCapable)this, n, this.archived);
                return;
            }
            case 1: {
                this.pcStateManager.providedObjectField((PersistenceCapable)this, n, (Object)this.archivedDate);
                return;
            }
            case 2: {
                this.pcStateManager.providedStringField((PersistenceCapable)this, n, this.archivedReason);
                return;
            }
            case 3: {
                this.pcStateManager.providedStringField((PersistenceCapable)this, n, this.atc);
                return;
            }
            case 4: {
                this.pcStateManager.providedStringField((PersistenceCapable)this, n, this.brandName);
                return;
            }
            case 5: {
                this.pcStateManager.providedObjectField((PersistenceCapable)this, n, (Object)this.caisiDemographicId);
                return;
            }
            case 6: {
                this.pcStateManager.providedStringField((PersistenceCapable)this, n, this.caisiProviderId);
                return;
            }
            case 7: {
                this.pcStateManager.providedObjectField((PersistenceCapable)this, n, (Object)this.createDate);
                return;
            }
            case 8: {
                this.pcStateManager.providedBooleanField((PersistenceCapable)this, n, this.customInstructions);
                return;
            }
            case 9: {
                this.pcStateManager.providedStringField((PersistenceCapable)this, n, this.customName);
                return;
            }
            case 10: {
                this.pcStateManager.providedStringField((PersistenceCapable)this, n, this.dosage);
                return;
            }
            case 11: {
                this.pcStateManager.providedStringField((PersistenceCapable)this, n, this.drugForm);
                return;
            }
            case 12: {
                this.pcStateManager.providedStringField((PersistenceCapable)this, n, this.durUnit);
                return;
            }
            case 13: {
                this.pcStateManager.providedStringField((PersistenceCapable)this, n, this.duration);
                return;
            }
            case 14: {
                this.pcStateManager.providedObjectField((PersistenceCapable)this, n, (Object)this.endDate);
                return;
            }
            case 15: {
                this.pcStateManager.providedObjectField((PersistenceCapable)this, n, (Object)this.facilityIdIntegerCompositePk);
                return;
            }
            case 16: {
                this.pcStateManager.providedStringField((PersistenceCapable)this, n, this.freqCode);
                return;
            }
            case 17: {
                this.pcStateManager.providedStringField((PersistenceCapable)this, n, this.genericName);
                return;
            }
            case 18: {
                this.pcStateManager.providedObjectField((PersistenceCapable)this, n, (Object)this.lastRefillDate);
                return;
            }
            case 19: {
                this.pcStateManager.providedObjectField((PersistenceCapable)this, n, (Object)this.longTerm);
                return;
            }
            case 20: {
                this.pcStateManager.providedStringField((PersistenceCapable)this, n, this.method);
                return;
            }
            case 21: {
                this.pcStateManager.providedBooleanField((PersistenceCapable)this, n, this.noSubs);
                return;
            }
            case 22: {
                this.pcStateManager.providedObjectField((PersistenceCapable)this, n, (Object)this.pastMed);
                return;
            }
            case 23: {
                this.pcStateManager.providedObjectField((PersistenceCapable)this, n, (Object)this.patientCompliance);
                return;
            }
            case 24: {
                this.pcStateManager.providedBooleanField((PersistenceCapable)this, n, this.prn);
                return;
            }
            case 25: {
                this.pcStateManager.providedStringField((PersistenceCapable)this, n, this.quantity);
                return;
            }
            case 26: {
                this.pcStateManager.providedStringField((PersistenceCapable)this, n, this.regionalIdentifier);
                return;
            }
            case 27: {
                this.pcStateManager.providedIntField((PersistenceCapable)this, n, this.repeats);
                return;
            }
            case 28: {
                this.pcStateManager.providedStringField((PersistenceCapable)this, n, this.route);
                return;
            }
            case 29: {
                this.pcStateManager.providedObjectField((PersistenceCapable)this, n, (Object)this.rxDate);
                return;
            }
            case 30: {
                this.pcStateManager.providedIntField((PersistenceCapable)this, n, this.scriptNo);
                return;
            }
            case 31: {
                this.pcStateManager.providedStringField((PersistenceCapable)this, n, this.special);
                return;
            }
            case 32: {
                this.pcStateManager.providedFloatField((PersistenceCapable)this, n, this.takeMax);
                return;
            }
            case 33: {
                this.pcStateManager.providedFloatField((PersistenceCapable)this, n, this.takeMin);
                return;
            }
            case 34: {
                this.pcStateManager.providedStringField((PersistenceCapable)this, n, this.unit);
                return;
            }
            case 35: {
                this.pcStateManager.providedStringField((PersistenceCapable)this, n, this.unitName);
                return;
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
    }
    
    public void pcProvideFields(final int[] array) {
        for (int i = 0; i < array.length; ++i) {
            this.pcProvideField(array[i]);
        }
    }
    
    protected void pcCopyField(final CachedDemographicDrug cachedDemographicDrug, final int n) {
        final int n2 = n - CachedDemographicDrug.pcInheritedFieldCount;
        if (n2 < 0) {
            throw new IllegalArgumentException();
        }
        switch (n2) {
            case 0: {
                this.archived = cachedDemographicDrug.archived;
                return;
            }
            case 1: {
                this.archivedDate = cachedDemographicDrug.archivedDate;
                return;
            }
            case 2: {
                this.archivedReason = cachedDemographicDrug.archivedReason;
                return;
            }
            case 3: {
                this.atc = cachedDemographicDrug.atc;
                return;
            }
            case 4: {
                this.brandName = cachedDemographicDrug.brandName;
                return;
            }
            case 5: {
                this.caisiDemographicId = cachedDemographicDrug.caisiDemographicId;
                return;
            }
            case 6: {
                this.caisiProviderId = cachedDemographicDrug.caisiProviderId;
                return;
            }
            case 7: {
                this.createDate = cachedDemographicDrug.createDate;
                return;
            }
            case 8: {
                this.customInstructions = cachedDemographicDrug.customInstructions;
                return;
            }
            case 9: {
                this.customName = cachedDemographicDrug.customName;
                return;
            }
            case 10: {
                this.dosage = cachedDemographicDrug.dosage;
                return;
            }
            case 11: {
                this.drugForm = cachedDemographicDrug.drugForm;
                return;
            }
            case 12: {
                this.durUnit = cachedDemographicDrug.durUnit;
                return;
            }
            case 13: {
                this.duration = cachedDemographicDrug.duration;
                return;
            }
            case 14: {
                this.endDate = cachedDemographicDrug.endDate;
                return;
            }
            case 15: {
                this.facilityIdIntegerCompositePk = cachedDemographicDrug.facilityIdIntegerCompositePk;
                return;
            }
            case 16: {
                this.freqCode = cachedDemographicDrug.freqCode;
                return;
            }
            case 17: {
                this.genericName = cachedDemographicDrug.genericName;
                return;
            }
            case 18: {
                this.lastRefillDate = cachedDemographicDrug.lastRefillDate;
                return;
            }
            case 19: {
                this.longTerm = cachedDemographicDrug.longTerm;
                return;
            }
            case 20: {
                this.method = cachedDemographicDrug.method;
                return;
            }
            case 21: {
                this.noSubs = cachedDemographicDrug.noSubs;
                return;
            }
            case 22: {
                this.pastMed = cachedDemographicDrug.pastMed;
                return;
            }
            case 23: {
                this.patientCompliance = cachedDemographicDrug.patientCompliance;
                return;
            }
            case 24: {
                this.prn = cachedDemographicDrug.prn;
                return;
            }
            case 25: {
                this.quantity = cachedDemographicDrug.quantity;
                return;
            }
            case 26: {
                this.regionalIdentifier = cachedDemographicDrug.regionalIdentifier;
                return;
            }
            case 27: {
                this.repeats = cachedDemographicDrug.repeats;
                return;
            }
            case 28: {
                this.route = cachedDemographicDrug.route;
                return;
            }
            case 29: {
                this.rxDate = cachedDemographicDrug.rxDate;
                return;
            }
            case 30: {
                this.scriptNo = cachedDemographicDrug.scriptNo;
                return;
            }
            case 31: {
                this.special = cachedDemographicDrug.special;
                return;
            }
            case 32: {
                this.takeMax = cachedDemographicDrug.takeMax;
                return;
            }
            case 33: {
                this.takeMin = cachedDemographicDrug.takeMin;
                return;
            }
            case 34: {
                this.unit = cachedDemographicDrug.unit;
                return;
            }
            case 35: {
                this.unitName = cachedDemographicDrug.unitName;
                return;
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
    }
    
    public void pcCopyFields(final Object o, final int[] array) {
        final CachedDemographicDrug cachedDemographicDrug = (CachedDemographicDrug)o;
        if (cachedDemographicDrug.pcStateManager != this.pcStateManager) {
            throw new IllegalArgumentException();
        }
        if (this.pcStateManager == null) {
            throw new IllegalStateException();
        }
        for (int i = 0; i < array.length; ++i) {
            this.pcCopyField(cachedDemographicDrug, array[i]);
        }
    }
    
    public Object pcGetGenericContext() {
        if (this.pcStateManager == null) {
            return null;
        }
        return this.pcStateManager.getGenericContext();
    }
    
    public Object pcFetchObjectId() {
        if (this.pcStateManager == null) {
            return null;
        }
        return this.pcStateManager.fetchObjectId();
    }
    
    public boolean pcIsDeleted() {
        return this.pcStateManager != null && this.pcStateManager.isDeleted();
    }
    
    public boolean pcIsDirty() {
        if (this.pcStateManager == null) {
            return false;
        }
        final StateManager pcStateManager = this.pcStateManager;
        RedefinitionHelper.dirtyCheck(pcStateManager);
        return pcStateManager.isDirty();
    }
    
    public boolean pcIsNew() {
        return this.pcStateManager != null && this.pcStateManager.isNew();
    }
    
    public boolean pcIsPersistent() {
        return this.pcStateManager != null && this.pcStateManager.isPersistent();
    }
    
    public boolean pcIsTransactional() {
        return this.pcStateManager != null && this.pcStateManager.isTransactional();
    }
    
    public boolean pcSerializing() {
        return this.pcStateManager != null && this.pcStateManager.serializing();
    }
    
    public void pcDirty(final String s) {
        if (this.pcStateManager == null) {
            return;
        }
        this.pcStateManager.dirty(s);
    }
    
    public StateManager pcGetStateManager() {
        return this.pcStateManager;
    }
    
    public Object pcGetVersion() {
        if (this.pcStateManager == null) {
            return null;
        }
        return this.pcStateManager.getVersion();
    }
    
    public void pcReplaceStateManager(final StateManager pcStateManager) throws SecurityException {
        if (this.pcStateManager != null) {
            this.pcStateManager = this.pcStateManager.replaceStateManager(pcStateManager);
            return;
        }
        this.pcStateManager = pcStateManager;
    }
    
    public void pcCopyKeyFieldsToObjectId(final FieldSupplier fieldSupplier, final Object o) {
        throw new InternalException();
    }
    
    public void pcCopyKeyFieldsToObjectId(final Object o) {
        throw new InternalException();
    }
    
    public void pcCopyKeyFieldsFromObjectId(final FieldConsumer fieldConsumer, final Object o) {
        fieldConsumer.storeObjectField(15 + CachedDemographicDrug.pcInheritedFieldCount, ((ObjectId)o).getId());
    }
    
    public void pcCopyKeyFieldsFromObjectId(final Object o) {
        this.facilityIdIntegerCompositePk = (FacilityIdIntegerCompositePk)((ObjectId)o).getId();
    }
    
    public Object pcNewObjectIdInstance(final Object o) {
        throw new IllegalArgumentException("The id type \"class org.apache.openjpa.util.ObjectId\" specified by persistent type \"class ca.openosp.openo.caisi_integrator.dao.CachedDemographicDrug\" does not have a public class org.apache.openjpa.util.ObjectId(String) or class org.apache.openjpa.util.ObjectId(Class, String) constructor.");
    }
    
    public Object pcNewObjectIdInstance() {
        return new ObjectId((CachedDemographicDrug.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicDrug != null) ? CachedDemographicDrug.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicDrug : (CachedDemographicDrug.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicDrug = class$("ca.openosp.openo.caisi_integrator.dao.CachedDemographicDrug")), (Object)this.facilityIdIntegerCompositePk);
    }
    
    private static final boolean pcGetarchived(final CachedDemographicDrug cachedDemographicDrug) {
        if (cachedDemographicDrug.pcStateManager == null) {
            return cachedDemographicDrug.archived;
        }
        cachedDemographicDrug.pcStateManager.accessingField(CachedDemographicDrug.pcInheritedFieldCount + 0);
        return cachedDemographicDrug.archived;
    }
    
    private static final void pcSetarchived(final CachedDemographicDrug cachedDemographicDrug, final boolean archived) {
        if (cachedDemographicDrug.pcStateManager == null) {
            cachedDemographicDrug.archived = archived;
            return;
        }
        cachedDemographicDrug.pcStateManager.settingBooleanField((PersistenceCapable)cachedDemographicDrug, CachedDemographicDrug.pcInheritedFieldCount + 0, cachedDemographicDrug.archived, archived, 0);
    }
    
    private static final Date pcGetarchivedDate(final CachedDemographicDrug cachedDemographicDrug) {
        if (cachedDemographicDrug.pcStateManager == null) {
            return cachedDemographicDrug.archivedDate;
        }
        cachedDemographicDrug.pcStateManager.accessingField(CachedDemographicDrug.pcInheritedFieldCount + 1);
        return cachedDemographicDrug.archivedDate;
    }
    
    private static final void pcSetarchivedDate(final CachedDemographicDrug cachedDemographicDrug, final Date archivedDate) {
        if (cachedDemographicDrug.pcStateManager == null) {
            cachedDemographicDrug.archivedDate = archivedDate;
            return;
        }
        cachedDemographicDrug.pcStateManager.settingObjectField((PersistenceCapable)cachedDemographicDrug, CachedDemographicDrug.pcInheritedFieldCount + 1, (Object)cachedDemographicDrug.archivedDate, (Object)archivedDate, 0);
    }
    
    private static final String pcGetarchivedReason(final CachedDemographicDrug cachedDemographicDrug) {
        if (cachedDemographicDrug.pcStateManager == null) {
            return cachedDemographicDrug.archivedReason;
        }
        cachedDemographicDrug.pcStateManager.accessingField(CachedDemographicDrug.pcInheritedFieldCount + 2);
        return cachedDemographicDrug.archivedReason;
    }
    
    private static final void pcSetarchivedReason(final CachedDemographicDrug cachedDemographicDrug, final String archivedReason) {
        if (cachedDemographicDrug.pcStateManager == null) {
            cachedDemographicDrug.archivedReason = archivedReason;
            return;
        }
        cachedDemographicDrug.pcStateManager.settingStringField((PersistenceCapable)cachedDemographicDrug, CachedDemographicDrug.pcInheritedFieldCount + 2, cachedDemographicDrug.archivedReason, archivedReason, 0);
    }
    
    private static final String pcGetatc(final CachedDemographicDrug cachedDemographicDrug) {
        if (cachedDemographicDrug.pcStateManager == null) {
            return cachedDemographicDrug.atc;
        }
        cachedDemographicDrug.pcStateManager.accessingField(CachedDemographicDrug.pcInheritedFieldCount + 3);
        return cachedDemographicDrug.atc;
    }
    
    private static final void pcSetatc(final CachedDemographicDrug cachedDemographicDrug, final String atc) {
        if (cachedDemographicDrug.pcStateManager == null) {
            cachedDemographicDrug.atc = atc;
            return;
        }
        cachedDemographicDrug.pcStateManager.settingStringField((PersistenceCapable)cachedDemographicDrug, CachedDemographicDrug.pcInheritedFieldCount + 3, cachedDemographicDrug.atc, atc, 0);
    }
    
    private static final String pcGetbrandName(final CachedDemographicDrug cachedDemographicDrug) {
        if (cachedDemographicDrug.pcStateManager == null) {
            return cachedDemographicDrug.brandName;
        }
        cachedDemographicDrug.pcStateManager.accessingField(CachedDemographicDrug.pcInheritedFieldCount + 4);
        return cachedDemographicDrug.brandName;
    }
    
    private static final void pcSetbrandName(final CachedDemographicDrug cachedDemographicDrug, final String brandName) {
        if (cachedDemographicDrug.pcStateManager == null) {
            cachedDemographicDrug.brandName = brandName;
            return;
        }
        cachedDemographicDrug.pcStateManager.settingStringField((PersistenceCapable)cachedDemographicDrug, CachedDemographicDrug.pcInheritedFieldCount + 4, cachedDemographicDrug.brandName, brandName, 0);
    }
    
    private static final Integer pcGetcaisiDemographicId(final CachedDemographicDrug cachedDemographicDrug) {
        if (cachedDemographicDrug.pcStateManager == null) {
            return cachedDemographicDrug.caisiDemographicId;
        }
        cachedDemographicDrug.pcStateManager.accessingField(CachedDemographicDrug.pcInheritedFieldCount + 5);
        return cachedDemographicDrug.caisiDemographicId;
    }
    
    private static final void pcSetcaisiDemographicId(final CachedDemographicDrug cachedDemographicDrug, final Integer caisiDemographicId) {
        if (cachedDemographicDrug.pcStateManager == null) {
            cachedDemographicDrug.caisiDemographicId = caisiDemographicId;
            return;
        }
        cachedDemographicDrug.pcStateManager.settingObjectField((PersistenceCapable)cachedDemographicDrug, CachedDemographicDrug.pcInheritedFieldCount + 5, (Object)cachedDemographicDrug.caisiDemographicId, (Object)caisiDemographicId, 0);
    }
    
    private static final String pcGetcaisiProviderId(final CachedDemographicDrug cachedDemographicDrug) {
        if (cachedDemographicDrug.pcStateManager == null) {
            return cachedDemographicDrug.caisiProviderId;
        }
        cachedDemographicDrug.pcStateManager.accessingField(CachedDemographicDrug.pcInheritedFieldCount + 6);
        return cachedDemographicDrug.caisiProviderId;
    }
    
    private static final void pcSetcaisiProviderId(final CachedDemographicDrug cachedDemographicDrug, final String caisiProviderId) {
        if (cachedDemographicDrug.pcStateManager == null) {
            cachedDemographicDrug.caisiProviderId = caisiProviderId;
            return;
        }
        cachedDemographicDrug.pcStateManager.settingStringField((PersistenceCapable)cachedDemographicDrug, CachedDemographicDrug.pcInheritedFieldCount + 6, cachedDemographicDrug.caisiProviderId, caisiProviderId, 0);
    }
    
    private static final Date pcGetcreateDate(final CachedDemographicDrug cachedDemographicDrug) {
        if (cachedDemographicDrug.pcStateManager == null) {
            return cachedDemographicDrug.createDate;
        }
        cachedDemographicDrug.pcStateManager.accessingField(CachedDemographicDrug.pcInheritedFieldCount + 7);
        return cachedDemographicDrug.createDate;
    }
    
    private static final void pcSetcreateDate(final CachedDemographicDrug cachedDemographicDrug, final Date createDate) {
        if (cachedDemographicDrug.pcStateManager == null) {
            cachedDemographicDrug.createDate = createDate;
            return;
        }
        cachedDemographicDrug.pcStateManager.settingObjectField((PersistenceCapable)cachedDemographicDrug, CachedDemographicDrug.pcInheritedFieldCount + 7, (Object)cachedDemographicDrug.createDate, (Object)createDate, 0);
    }
    
    private static final boolean pcGetcustomInstructions(final CachedDemographicDrug cachedDemographicDrug) {
        if (cachedDemographicDrug.pcStateManager == null) {
            return cachedDemographicDrug.customInstructions;
        }
        cachedDemographicDrug.pcStateManager.accessingField(CachedDemographicDrug.pcInheritedFieldCount + 8);
        return cachedDemographicDrug.customInstructions;
    }
    
    private static final void pcSetcustomInstructions(final CachedDemographicDrug cachedDemographicDrug, final boolean customInstructions) {
        if (cachedDemographicDrug.pcStateManager == null) {
            cachedDemographicDrug.customInstructions = customInstructions;
            return;
        }
        cachedDemographicDrug.pcStateManager.settingBooleanField((PersistenceCapable)cachedDemographicDrug, CachedDemographicDrug.pcInheritedFieldCount + 8, cachedDemographicDrug.customInstructions, customInstructions, 0);
    }
    
    private static final String pcGetcustomName(final CachedDemographicDrug cachedDemographicDrug) {
        if (cachedDemographicDrug.pcStateManager == null) {
            return cachedDemographicDrug.customName;
        }
        cachedDemographicDrug.pcStateManager.accessingField(CachedDemographicDrug.pcInheritedFieldCount + 9);
        return cachedDemographicDrug.customName;
    }
    
    private static final void pcSetcustomName(final CachedDemographicDrug cachedDemographicDrug, final String customName) {
        if (cachedDemographicDrug.pcStateManager == null) {
            cachedDemographicDrug.customName = customName;
            return;
        }
        cachedDemographicDrug.pcStateManager.settingStringField((PersistenceCapable)cachedDemographicDrug, CachedDemographicDrug.pcInheritedFieldCount + 9, cachedDemographicDrug.customName, customName, 0);
    }
    
    private static final String pcGetdosage(final CachedDemographicDrug cachedDemographicDrug) {
        if (cachedDemographicDrug.pcStateManager == null) {
            return cachedDemographicDrug.dosage;
        }
        cachedDemographicDrug.pcStateManager.accessingField(CachedDemographicDrug.pcInheritedFieldCount + 10);
        return cachedDemographicDrug.dosage;
    }
    
    private static final void pcSetdosage(final CachedDemographicDrug cachedDemographicDrug, final String dosage) {
        if (cachedDemographicDrug.pcStateManager == null) {
            cachedDemographicDrug.dosage = dosage;
            return;
        }
        cachedDemographicDrug.pcStateManager.settingStringField((PersistenceCapable)cachedDemographicDrug, CachedDemographicDrug.pcInheritedFieldCount + 10, cachedDemographicDrug.dosage, dosage, 0);
    }
    
    private static final String pcGetdrugForm(final CachedDemographicDrug cachedDemographicDrug) {
        if (cachedDemographicDrug.pcStateManager == null) {
            return cachedDemographicDrug.drugForm;
        }
        cachedDemographicDrug.pcStateManager.accessingField(CachedDemographicDrug.pcInheritedFieldCount + 11);
        return cachedDemographicDrug.drugForm;
    }
    
    private static final void pcSetdrugForm(final CachedDemographicDrug cachedDemographicDrug, final String drugForm) {
        if (cachedDemographicDrug.pcStateManager == null) {
            cachedDemographicDrug.drugForm = drugForm;
            return;
        }
        cachedDemographicDrug.pcStateManager.settingStringField((PersistenceCapable)cachedDemographicDrug, CachedDemographicDrug.pcInheritedFieldCount + 11, cachedDemographicDrug.drugForm, drugForm, 0);
    }
    
    private static final String pcGetdurUnit(final CachedDemographicDrug cachedDemographicDrug) {
        if (cachedDemographicDrug.pcStateManager == null) {
            return cachedDemographicDrug.durUnit;
        }
        cachedDemographicDrug.pcStateManager.accessingField(CachedDemographicDrug.pcInheritedFieldCount + 12);
        return cachedDemographicDrug.durUnit;
    }
    
    private static final void pcSetdurUnit(final CachedDemographicDrug cachedDemographicDrug, final String durUnit) {
        if (cachedDemographicDrug.pcStateManager == null) {
            cachedDemographicDrug.durUnit = durUnit;
            return;
        }
        cachedDemographicDrug.pcStateManager.settingStringField((PersistenceCapable)cachedDemographicDrug, CachedDemographicDrug.pcInheritedFieldCount + 12, cachedDemographicDrug.durUnit, durUnit, 0);
    }
    
    private static final String pcGetduration(final CachedDemographicDrug cachedDemographicDrug) {
        if (cachedDemographicDrug.pcStateManager == null) {
            return cachedDemographicDrug.duration;
        }
        cachedDemographicDrug.pcStateManager.accessingField(CachedDemographicDrug.pcInheritedFieldCount + 13);
        return cachedDemographicDrug.duration;
    }
    
    private static final void pcSetduration(final CachedDemographicDrug cachedDemographicDrug, final String duration) {
        if (cachedDemographicDrug.pcStateManager == null) {
            cachedDemographicDrug.duration = duration;
            return;
        }
        cachedDemographicDrug.pcStateManager.settingStringField((PersistenceCapable)cachedDemographicDrug, CachedDemographicDrug.pcInheritedFieldCount + 13, cachedDemographicDrug.duration, duration, 0);
    }
    
    private static final Date pcGetendDate(final CachedDemographicDrug cachedDemographicDrug) {
        if (cachedDemographicDrug.pcStateManager == null) {
            return cachedDemographicDrug.endDate;
        }
        cachedDemographicDrug.pcStateManager.accessingField(CachedDemographicDrug.pcInheritedFieldCount + 14);
        return cachedDemographicDrug.endDate;
    }
    
    private static final void pcSetendDate(final CachedDemographicDrug cachedDemographicDrug, final Date endDate) {
        if (cachedDemographicDrug.pcStateManager == null) {
            cachedDemographicDrug.endDate = endDate;
            return;
        }
        cachedDemographicDrug.pcStateManager.settingObjectField((PersistenceCapable)cachedDemographicDrug, CachedDemographicDrug.pcInheritedFieldCount + 14, (Object)cachedDemographicDrug.endDate, (Object)endDate, 0);
    }
    
    private static final FacilityIdIntegerCompositePk pcGetfacilityIdIntegerCompositePk(final CachedDemographicDrug cachedDemographicDrug) {
        if (cachedDemographicDrug.pcStateManager == null) {
            return cachedDemographicDrug.facilityIdIntegerCompositePk;
        }
        cachedDemographicDrug.pcStateManager.accessingField(CachedDemographicDrug.pcInheritedFieldCount + 15);
        return cachedDemographicDrug.facilityIdIntegerCompositePk;
    }
    
    private static final void pcSetfacilityIdIntegerCompositePk(final CachedDemographicDrug cachedDemographicDrug, final FacilityIdIntegerCompositePk facilityIdIntegerCompositePk) {
        if (cachedDemographicDrug.pcStateManager == null) {
            cachedDemographicDrug.facilityIdIntegerCompositePk = facilityIdIntegerCompositePk;
            return;
        }
        cachedDemographicDrug.pcStateManager.settingObjectField((PersistenceCapable)cachedDemographicDrug, CachedDemographicDrug.pcInheritedFieldCount + 15, (Object)cachedDemographicDrug.facilityIdIntegerCompositePk, (Object)facilityIdIntegerCompositePk, 0);
    }
    
    private static final String pcGetfreqCode(final CachedDemographicDrug cachedDemographicDrug) {
        if (cachedDemographicDrug.pcStateManager == null) {
            return cachedDemographicDrug.freqCode;
        }
        cachedDemographicDrug.pcStateManager.accessingField(CachedDemographicDrug.pcInheritedFieldCount + 16);
        return cachedDemographicDrug.freqCode;
    }
    
    private static final void pcSetfreqCode(final CachedDemographicDrug cachedDemographicDrug, final String freqCode) {
        if (cachedDemographicDrug.pcStateManager == null) {
            cachedDemographicDrug.freqCode = freqCode;
            return;
        }
        cachedDemographicDrug.pcStateManager.settingStringField((PersistenceCapable)cachedDemographicDrug, CachedDemographicDrug.pcInheritedFieldCount + 16, cachedDemographicDrug.freqCode, freqCode, 0);
    }
    
    private static final String pcGetgenericName(final CachedDemographicDrug cachedDemographicDrug) {
        if (cachedDemographicDrug.pcStateManager == null) {
            return cachedDemographicDrug.genericName;
        }
        cachedDemographicDrug.pcStateManager.accessingField(CachedDemographicDrug.pcInheritedFieldCount + 17);
        return cachedDemographicDrug.genericName;
    }
    
    private static final void pcSetgenericName(final CachedDemographicDrug cachedDemographicDrug, final String genericName) {
        if (cachedDemographicDrug.pcStateManager == null) {
            cachedDemographicDrug.genericName = genericName;
            return;
        }
        cachedDemographicDrug.pcStateManager.settingStringField((PersistenceCapable)cachedDemographicDrug, CachedDemographicDrug.pcInheritedFieldCount + 17, cachedDemographicDrug.genericName, genericName, 0);
    }
    
    private static final Date pcGetlastRefillDate(final CachedDemographicDrug cachedDemographicDrug) {
        if (cachedDemographicDrug.pcStateManager == null) {
            return cachedDemographicDrug.lastRefillDate;
        }
        cachedDemographicDrug.pcStateManager.accessingField(CachedDemographicDrug.pcInheritedFieldCount + 18);
        return cachedDemographicDrug.lastRefillDate;
    }
    
    private static final void pcSetlastRefillDate(final CachedDemographicDrug cachedDemographicDrug, final Date lastRefillDate) {
        if (cachedDemographicDrug.pcStateManager == null) {
            cachedDemographicDrug.lastRefillDate = lastRefillDate;
            return;
        }
        cachedDemographicDrug.pcStateManager.settingObjectField((PersistenceCapable)cachedDemographicDrug, CachedDemographicDrug.pcInheritedFieldCount + 18, (Object)cachedDemographicDrug.lastRefillDate, (Object)lastRefillDate, 0);
    }
    
    private static final Boolean pcGetlongTerm(final CachedDemographicDrug cachedDemographicDrug) {
        if (cachedDemographicDrug.pcStateManager == null) {
            return cachedDemographicDrug.longTerm;
        }
        cachedDemographicDrug.pcStateManager.accessingField(CachedDemographicDrug.pcInheritedFieldCount + 19);
        return cachedDemographicDrug.longTerm;
    }
    
    private static final void pcSetlongTerm(final CachedDemographicDrug cachedDemographicDrug, final Boolean longTerm) {
        if (cachedDemographicDrug.pcStateManager == null) {
            cachedDemographicDrug.longTerm = longTerm;
            return;
        }
        cachedDemographicDrug.pcStateManager.settingObjectField((PersistenceCapable)cachedDemographicDrug, CachedDemographicDrug.pcInheritedFieldCount + 19, (Object)cachedDemographicDrug.longTerm, (Object)longTerm, 0);
    }
    
    private static final String pcGetmethod(final CachedDemographicDrug cachedDemographicDrug) {
        if (cachedDemographicDrug.pcStateManager == null) {
            return cachedDemographicDrug.method;
        }
        cachedDemographicDrug.pcStateManager.accessingField(CachedDemographicDrug.pcInheritedFieldCount + 20);
        return cachedDemographicDrug.method;
    }
    
    private static final void pcSetmethod(final CachedDemographicDrug cachedDemographicDrug, final String method) {
        if (cachedDemographicDrug.pcStateManager == null) {
            cachedDemographicDrug.method = method;
            return;
        }
        cachedDemographicDrug.pcStateManager.settingStringField((PersistenceCapable)cachedDemographicDrug, CachedDemographicDrug.pcInheritedFieldCount + 20, cachedDemographicDrug.method, method, 0);
    }
    
    private static final boolean pcGetnoSubs(final CachedDemographicDrug cachedDemographicDrug) {
        if (cachedDemographicDrug.pcStateManager == null) {
            return cachedDemographicDrug.noSubs;
        }
        cachedDemographicDrug.pcStateManager.accessingField(CachedDemographicDrug.pcInheritedFieldCount + 21);
        return cachedDemographicDrug.noSubs;
    }
    
    private static final void pcSetnoSubs(final CachedDemographicDrug cachedDemographicDrug, final boolean noSubs) {
        if (cachedDemographicDrug.pcStateManager == null) {
            cachedDemographicDrug.noSubs = noSubs;
            return;
        }
        cachedDemographicDrug.pcStateManager.settingBooleanField((PersistenceCapable)cachedDemographicDrug, CachedDemographicDrug.pcInheritedFieldCount + 21, cachedDemographicDrug.noSubs, noSubs, 0);
    }
    
    private static final Boolean pcGetpastMed(final CachedDemographicDrug cachedDemographicDrug) {
        if (cachedDemographicDrug.pcStateManager == null) {
            return cachedDemographicDrug.pastMed;
        }
        cachedDemographicDrug.pcStateManager.accessingField(CachedDemographicDrug.pcInheritedFieldCount + 22);
        return cachedDemographicDrug.pastMed;
    }
    
    private static final void pcSetpastMed(final CachedDemographicDrug cachedDemographicDrug, final Boolean pastMed) {
        if (cachedDemographicDrug.pcStateManager == null) {
            cachedDemographicDrug.pastMed = pastMed;
            return;
        }
        cachedDemographicDrug.pcStateManager.settingObjectField((PersistenceCapable)cachedDemographicDrug, CachedDemographicDrug.pcInheritedFieldCount + 22, (Object)cachedDemographicDrug.pastMed, (Object)pastMed, 0);
    }
    
    private static final Boolean pcGetpatientCompliance(final CachedDemographicDrug cachedDemographicDrug) {
        if (cachedDemographicDrug.pcStateManager == null) {
            return cachedDemographicDrug.patientCompliance;
        }
        cachedDemographicDrug.pcStateManager.accessingField(CachedDemographicDrug.pcInheritedFieldCount + 23);
        return cachedDemographicDrug.patientCompliance;
    }
    
    private static final void pcSetpatientCompliance(final CachedDemographicDrug cachedDemographicDrug, final Boolean patientCompliance) {
        if (cachedDemographicDrug.pcStateManager == null) {
            cachedDemographicDrug.patientCompliance = patientCompliance;
            return;
        }
        cachedDemographicDrug.pcStateManager.settingObjectField((PersistenceCapable)cachedDemographicDrug, CachedDemographicDrug.pcInheritedFieldCount + 23, (Object)cachedDemographicDrug.patientCompliance, (Object)patientCompliance, 0);
    }
    
    private static final boolean pcGetprn(final CachedDemographicDrug cachedDemographicDrug) {
        if (cachedDemographicDrug.pcStateManager == null) {
            return cachedDemographicDrug.prn;
        }
        cachedDemographicDrug.pcStateManager.accessingField(CachedDemographicDrug.pcInheritedFieldCount + 24);
        return cachedDemographicDrug.prn;
    }
    
    private static final void pcSetprn(final CachedDemographicDrug cachedDemographicDrug, final boolean prn) {
        if (cachedDemographicDrug.pcStateManager == null) {
            cachedDemographicDrug.prn = prn;
            return;
        }
        cachedDemographicDrug.pcStateManager.settingBooleanField((PersistenceCapable)cachedDemographicDrug, CachedDemographicDrug.pcInheritedFieldCount + 24, cachedDemographicDrug.prn, prn, 0);
    }
    
    private static final String pcGetquantity(final CachedDemographicDrug cachedDemographicDrug) {
        if (cachedDemographicDrug.pcStateManager == null) {
            return cachedDemographicDrug.quantity;
        }
        cachedDemographicDrug.pcStateManager.accessingField(CachedDemographicDrug.pcInheritedFieldCount + 25);
        return cachedDemographicDrug.quantity;
    }
    
    private static final void pcSetquantity(final CachedDemographicDrug cachedDemographicDrug, final String quantity) {
        if (cachedDemographicDrug.pcStateManager == null) {
            cachedDemographicDrug.quantity = quantity;
            return;
        }
        cachedDemographicDrug.pcStateManager.settingStringField((PersistenceCapable)cachedDemographicDrug, CachedDemographicDrug.pcInheritedFieldCount + 25, cachedDemographicDrug.quantity, quantity, 0);
    }
    
    private static final String pcGetregionalIdentifier(final CachedDemographicDrug cachedDemographicDrug) {
        if (cachedDemographicDrug.pcStateManager == null) {
            return cachedDemographicDrug.regionalIdentifier;
        }
        cachedDemographicDrug.pcStateManager.accessingField(CachedDemographicDrug.pcInheritedFieldCount + 26);
        return cachedDemographicDrug.regionalIdentifier;
    }
    
    private static final void pcSetregionalIdentifier(final CachedDemographicDrug cachedDemographicDrug, final String regionalIdentifier) {
        if (cachedDemographicDrug.pcStateManager == null) {
            cachedDemographicDrug.regionalIdentifier = regionalIdentifier;
            return;
        }
        cachedDemographicDrug.pcStateManager.settingStringField((PersistenceCapable)cachedDemographicDrug, CachedDemographicDrug.pcInheritedFieldCount + 26, cachedDemographicDrug.regionalIdentifier, regionalIdentifier, 0);
    }
    
    private static final int pcGetrepeats(final CachedDemographicDrug cachedDemographicDrug) {
        if (cachedDemographicDrug.pcStateManager == null) {
            return cachedDemographicDrug.repeats;
        }
        cachedDemographicDrug.pcStateManager.accessingField(CachedDemographicDrug.pcInheritedFieldCount + 27);
        return cachedDemographicDrug.repeats;
    }
    
    private static final void pcSetrepeats(final CachedDemographicDrug cachedDemographicDrug, final int repeats) {
        if (cachedDemographicDrug.pcStateManager == null) {
            cachedDemographicDrug.repeats = repeats;
            return;
        }
        cachedDemographicDrug.pcStateManager.settingIntField((PersistenceCapable)cachedDemographicDrug, CachedDemographicDrug.pcInheritedFieldCount + 27, cachedDemographicDrug.repeats, repeats, 0);
    }
    
    private static final String pcGetroute(final CachedDemographicDrug cachedDemographicDrug) {
        if (cachedDemographicDrug.pcStateManager == null) {
            return cachedDemographicDrug.route;
        }
        cachedDemographicDrug.pcStateManager.accessingField(CachedDemographicDrug.pcInheritedFieldCount + 28);
        return cachedDemographicDrug.route;
    }
    
    private static final void pcSetroute(final CachedDemographicDrug cachedDemographicDrug, final String route) {
        if (cachedDemographicDrug.pcStateManager == null) {
            cachedDemographicDrug.route = route;
            return;
        }
        cachedDemographicDrug.pcStateManager.settingStringField((PersistenceCapable)cachedDemographicDrug, CachedDemographicDrug.pcInheritedFieldCount + 28, cachedDemographicDrug.route, route, 0);
    }
    
    private static final Date pcGetrxDate(final CachedDemographicDrug cachedDemographicDrug) {
        if (cachedDemographicDrug.pcStateManager == null) {
            return cachedDemographicDrug.rxDate;
        }
        cachedDemographicDrug.pcStateManager.accessingField(CachedDemographicDrug.pcInheritedFieldCount + 29);
        return cachedDemographicDrug.rxDate;
    }
    
    private static final void pcSetrxDate(final CachedDemographicDrug cachedDemographicDrug, final Date rxDate) {
        if (cachedDemographicDrug.pcStateManager == null) {
            cachedDemographicDrug.rxDate = rxDate;
            return;
        }
        cachedDemographicDrug.pcStateManager.settingObjectField((PersistenceCapable)cachedDemographicDrug, CachedDemographicDrug.pcInheritedFieldCount + 29, (Object)cachedDemographicDrug.rxDate, (Object)rxDate, 0);
    }
    
    private static final int pcGetscriptNo(final CachedDemographicDrug cachedDemographicDrug) {
        if (cachedDemographicDrug.pcStateManager == null) {
            return cachedDemographicDrug.scriptNo;
        }
        cachedDemographicDrug.pcStateManager.accessingField(CachedDemographicDrug.pcInheritedFieldCount + 30);
        return cachedDemographicDrug.scriptNo;
    }
    
    private static final void pcSetscriptNo(final CachedDemographicDrug cachedDemographicDrug, final int scriptNo) {
        if (cachedDemographicDrug.pcStateManager == null) {
            cachedDemographicDrug.scriptNo = scriptNo;
            return;
        }
        cachedDemographicDrug.pcStateManager.settingIntField((PersistenceCapable)cachedDemographicDrug, CachedDemographicDrug.pcInheritedFieldCount + 30, cachedDemographicDrug.scriptNo, scriptNo, 0);
    }
    
    private static final String pcGetspecial(final CachedDemographicDrug cachedDemographicDrug) {
        if (cachedDemographicDrug.pcStateManager == null) {
            return cachedDemographicDrug.special;
        }
        cachedDemographicDrug.pcStateManager.accessingField(CachedDemographicDrug.pcInheritedFieldCount + 31);
        return cachedDemographicDrug.special;
    }
    
    private static final void pcSetspecial(final CachedDemographicDrug cachedDemographicDrug, final String special) {
        if (cachedDemographicDrug.pcStateManager == null) {
            cachedDemographicDrug.special = special;
            return;
        }
        cachedDemographicDrug.pcStateManager.settingStringField((PersistenceCapable)cachedDemographicDrug, CachedDemographicDrug.pcInheritedFieldCount + 31, cachedDemographicDrug.special, special, 0);
    }
    
    private static final float pcGettakeMax(final CachedDemographicDrug cachedDemographicDrug) {
        if (cachedDemographicDrug.pcStateManager == null) {
            return cachedDemographicDrug.takeMax;
        }
        cachedDemographicDrug.pcStateManager.accessingField(CachedDemographicDrug.pcInheritedFieldCount + 32);
        return cachedDemographicDrug.takeMax;
    }
    
    private static final void pcSettakeMax(final CachedDemographicDrug cachedDemographicDrug, final float takeMax) {
        if (cachedDemographicDrug.pcStateManager == null) {
            cachedDemographicDrug.takeMax = takeMax;
            return;
        }
        cachedDemographicDrug.pcStateManager.settingFloatField((PersistenceCapable)cachedDemographicDrug, CachedDemographicDrug.pcInheritedFieldCount + 32, cachedDemographicDrug.takeMax, takeMax, 0);
    }
    
    private static final float pcGettakeMin(final CachedDemographicDrug cachedDemographicDrug) {
        if (cachedDemographicDrug.pcStateManager == null) {
            return cachedDemographicDrug.takeMin;
        }
        cachedDemographicDrug.pcStateManager.accessingField(CachedDemographicDrug.pcInheritedFieldCount + 33);
        return cachedDemographicDrug.takeMin;
    }
    
    private static final void pcSettakeMin(final CachedDemographicDrug cachedDemographicDrug, final float takeMin) {
        if (cachedDemographicDrug.pcStateManager == null) {
            cachedDemographicDrug.takeMin = takeMin;
            return;
        }
        cachedDemographicDrug.pcStateManager.settingFloatField((PersistenceCapable)cachedDemographicDrug, CachedDemographicDrug.pcInheritedFieldCount + 33, cachedDemographicDrug.takeMin, takeMin, 0);
    }
    
    private static final String pcGetunit(final CachedDemographicDrug cachedDemographicDrug) {
        if (cachedDemographicDrug.pcStateManager == null) {
            return cachedDemographicDrug.unit;
        }
        cachedDemographicDrug.pcStateManager.accessingField(CachedDemographicDrug.pcInheritedFieldCount + 34);
        return cachedDemographicDrug.unit;
    }
    
    private static final void pcSetunit(final CachedDemographicDrug cachedDemographicDrug, final String unit) {
        if (cachedDemographicDrug.pcStateManager == null) {
            cachedDemographicDrug.unit = unit;
            return;
        }
        cachedDemographicDrug.pcStateManager.settingStringField((PersistenceCapable)cachedDemographicDrug, CachedDemographicDrug.pcInheritedFieldCount + 34, cachedDemographicDrug.unit, unit, 0);
    }
    
    private static final String pcGetunitName(final CachedDemographicDrug cachedDemographicDrug) {
        if (cachedDemographicDrug.pcStateManager == null) {
            return cachedDemographicDrug.unitName;
        }
        cachedDemographicDrug.pcStateManager.accessingField(CachedDemographicDrug.pcInheritedFieldCount + 35);
        return cachedDemographicDrug.unitName;
    }
    
    private static final void pcSetunitName(final CachedDemographicDrug cachedDemographicDrug, final String unitName) {
        if (cachedDemographicDrug.pcStateManager == null) {
            cachedDemographicDrug.unitName = unitName;
            return;
        }
        cachedDemographicDrug.pcStateManager.settingStringField((PersistenceCapable)cachedDemographicDrug, CachedDemographicDrug.pcInheritedFieldCount + 35, cachedDemographicDrug.unitName, unitName, 0);
    }
    
    public Boolean pcIsDetached() {
        if (this.pcStateManager != null) {
            if (this.pcStateManager.isDetached()) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }
        else {
            if (this.pcGetDetachedState() != null && this.pcGetDetachedState() != PersistenceCapable.DESERIALIZED) {
                return Boolean.TRUE;
            }
            if (!this.pcisDetachedStateDefinitive()) {
                return null;
            }
            if (this.pcGetDetachedState() == null) {
                return Boolean.FALSE;
            }
            return null;
        }
    }
    
    private boolean pcisDetachedStateDefinitive() {
        return false;
    }
    
    public Object pcGetDetachedState() {
        return this.pcDetachedState;
    }
    
    public void pcSetDetachedState(final Object pcDetachedState) {
        this.pcDetachedState = pcDetachedState;
    }
    
    private void writeObject(final ObjectOutputStream objectOutputStream) throws IOException {
        final boolean pcSerializing = this.pcSerializing();
        objectOutputStream.defaultWriteObject();
        if (pcSerializing) {
            this.pcSetDetachedState(null);
        }
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.pcSetDetachedState(PersistenceCapable.DESERIALIZED);
        objectInputStream.defaultReadObject();
    }
}
