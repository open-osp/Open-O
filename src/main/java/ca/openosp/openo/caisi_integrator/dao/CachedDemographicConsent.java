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
import java.util.HashMap;
import org.apache.openjpa.enhance.StateManager;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ElementCollection;
import javax.persistence.CollectionTable;
import javax.persistence.MapKeyColumn;
import javax.persistence.Column;
import java.util.Map;
import javax.persistence.TemporalType;
import javax.persistence.Temporal;
import java.util.Date;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class CachedDemographicConsent extends AbstractModel<FacilityIdIntegerCompositePk> implements PersistenceCapable
{
    @EmbeddedId
    private FacilityIdIntegerCompositePk facilityDemographicPk;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "CachedDemographicConsentConsents")
    @MapKeyColumn(name = "integratorFacilityId")
    @Column(name = "shareData", columnDefinition = "tinyint(1)")
    private Map<Integer, Boolean> consentToShareData;
    private boolean excludeMentalHealthData;
    @Enumerated(EnumType.STRING)
    private ConsentStatus clientConsentStatus;
    @Temporal(TemporalType.DATE)
    private Date expiry;
    private static int pcInheritedFieldCount;
    private static String[] pcFieldNames;
    private static Class[] pcFieldTypes;
    private static byte[] pcFieldFlags;
    private static Class pcPCSuperclass;
    protected transient StateManager pcStateManager;
    static /* synthetic */ Class class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicConsent$ConsentStatus;
    static /* synthetic */ Class class$Ljava$util$Map;
    static /* synthetic */ Class class$Ljava$util$Date;
    static /* synthetic */ Class class$Lca$openosp$openo$caisi_integrator$dao$FacilityIdIntegerCompositePk;
    static /* synthetic */ Class class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicConsent;
    private transient Object pcDetachedState;
    private static final long serialVersionUID;
    
    public CachedDemographicConsent() {
        this.createdDate = null;
        this.consentToShareData = new HashMap<Integer, Boolean>();
        this.excludeMentalHealthData = false;
        this.clientConsentStatus = null;
        this.expiry = null;
    }
    
    public FacilityIdIntegerCompositePk getFacilityDemographicPk() {
        return pcGetfacilityDemographicPk(this);
    }
    
    public void setFacilityDemographicPk(final FacilityIdIntegerCompositePk facilityDemographicPk) {
        pcSetfacilityDemographicPk(this, facilityDemographicPk);
    }
    
    public Date getCreatedDate() {
        return pcGetcreatedDate(this);
    }
    
    public void setCreatedDate(final Date createdDate) {
        pcSetcreatedDate(this, createdDate);
    }
    
    @Override
    public FacilityIdIntegerCompositePk getId() {
        return pcGetfacilityDemographicPk(this);
    }
    
    public Map<Integer, Boolean> getConsentToShareData() {
        return pcGetconsentToShareData(this);
    }
    
    public void setConsentToShareData(final Map<Integer, Boolean> consentToShareData) {
        pcSetconsentToShareData(this, consentToShareData);
    }
    
    public boolean allowedToShareData(final Integer integratorFacilityId) {
        if (pcGetclientConsentStatus(this) != ConsentStatus.GIVEN) {
            return false;
        }
        if (pcGetexpiry(this) != null && pcGetexpiry(this).before(new Date())) {
            return false;
        }
        final Boolean result = (Boolean)((Map)pcGetconsentToShareData(this)).get(integratorFacilityId);
        return result == null || result;
    }
    
    public boolean isExcludeMentalHealthData() {
        return pcGetexcludeMentalHealthData(this);
    }
    
    public void setExcludeMentalHealthData(final boolean excludeMentalHealthData) {
        pcSetexcludeMentalHealthData(this, excludeMentalHealthData);
    }
    
    public ConsentStatus getClientConsentStatus() {
        return pcGetclientConsentStatus(this);
    }
    
    public void setClientConsentStatus(final ConsentStatus clientConsentStatus) {
        pcSetclientConsentStatus(this, clientConsentStatus);
    }
    
    public Date getExpiry() {
        return pcGetexpiry(this);
    }
    
    public void setExpiry(final Date expiry) {
        pcSetexpiry(this, expiry);
    }
    
    public int pcGetEnhancementContractVersion() {
        return 2;
    }
    
    static {
        serialVersionUID = -7209077813006175494L;
        CachedDemographicConsent.pcFieldNames = new String[] { "clientConsentStatus", "consentToShareData", "createdDate", "excludeMentalHealthData", "expiry", "facilityDemographicPk" };
        CachedDemographicConsent.pcFieldTypes = new Class[] { (CachedDemographicConsent.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicConsent$ConsentStatus != null) ? CachedDemographicConsent.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicConsent$ConsentStatus : (CachedDemographicConsent.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicConsent$ConsentStatus = class$("ca.openosp.openo.caisi_integrator.dao.CachedDemographicConsent$ConsentStatus")), (CachedDemographicConsent.class$Ljava$util$Map != null) ? CachedDemographicConsent.class$Ljava$util$Map : (CachedDemographicConsent.class$Ljava$util$Map = class$("java.util.Map")), (CachedDemographicConsent.class$Ljava$util$Date != null) ? CachedDemographicConsent.class$Ljava$util$Date : (CachedDemographicConsent.class$Ljava$util$Date = class$("java.util.Date")), Boolean.TYPE, (CachedDemographicConsent.class$Ljava$util$Date != null) ? CachedDemographicConsent.class$Ljava$util$Date : (CachedDemographicConsent.class$Ljava$util$Date = class$("java.util.Date")), (CachedDemographicConsent.class$Lca$openosp$openo$caisi_integrator$dao$FacilityIdIntegerCompositePk != null) ? CachedDemographicConsent.class$Lca$openosp$openo$caisi_integrator$dao$FacilityIdIntegerCompositePk : (CachedDemographicConsent.class$Lca$openosp$openo$caisi_integrator$dao$FacilityIdIntegerCompositePk = class$("ca.openosp.openo.caisi_integrator.dao.FacilityIdIntegerCompositePk")) };
        CachedDemographicConsent.pcFieldFlags = new byte[] { 26, 10, 26, 26, 26, 26 };
        PCRegistry.register((CachedDemographicConsent.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicConsent != null) ? CachedDemographicConsent.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicConsent : (CachedDemographicConsent.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicConsent = class$("ca.openosp.openo.caisi_integrator.dao.CachedDemographicConsent")), CachedDemographicConsent.pcFieldNames, CachedDemographicConsent.pcFieldTypes, CachedDemographicConsent.pcFieldFlags, CachedDemographicConsent.pcPCSuperclass, "CachedDemographicConsent", (PersistenceCapable)new CachedDemographicConsent());
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
        this.clientConsentStatus = null;
        this.consentToShareData = null;
        this.createdDate = null;
        this.excludeMentalHealthData = false;
        this.expiry = null;
        this.facilityDemographicPk = null;
    }
    
    public PersistenceCapable pcNewInstance(final StateManager pcStateManager, final Object o, final boolean b) {
        final CachedDemographicConsent cachedDemographicConsent = new CachedDemographicConsent();
        if (b) {
            cachedDemographicConsent.pcClearFields();
        }
        cachedDemographicConsent.pcStateManager = pcStateManager;
        cachedDemographicConsent.pcCopyKeyFieldsFromObjectId(o);
        return (PersistenceCapable)cachedDemographicConsent;
    }
    
    public PersistenceCapable pcNewInstance(final StateManager pcStateManager, final boolean b) {
        final CachedDemographicConsent cachedDemographicConsent = new CachedDemographicConsent();
        if (b) {
            cachedDemographicConsent.pcClearFields();
        }
        cachedDemographicConsent.pcStateManager = pcStateManager;
        return (PersistenceCapable)cachedDemographicConsent;
    }
    
    protected static int pcGetManagedFieldCount() {
        return 6;
    }
    
    public void pcReplaceField(final int n) {
        final int n2 = n - CachedDemographicConsent.pcInheritedFieldCount;
        if (n2 < 0) {
            throw new IllegalArgumentException();
        }
        switch (n2) {
            case 0: {
                this.clientConsentStatus = (ConsentStatus)this.pcStateManager.replaceObjectField((PersistenceCapable)this, n);
                return;
            }
            case 1: {
                this.consentToShareData = (Map)this.pcStateManager.replaceObjectField((PersistenceCapable)this, n);
                return;
            }
            case 2: {
                this.createdDate = (Date)this.pcStateManager.replaceObjectField((PersistenceCapable)this, n);
                return;
            }
            case 3: {
                this.excludeMentalHealthData = this.pcStateManager.replaceBooleanField((PersistenceCapable)this, n);
                return;
            }
            case 4: {
                this.expiry = (Date)this.pcStateManager.replaceObjectField((PersistenceCapable)this, n);
                return;
            }
            case 5: {
                this.facilityDemographicPk = (FacilityIdIntegerCompositePk)this.pcStateManager.replaceObjectField((PersistenceCapable)this, n);
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
        final int n2 = n - CachedDemographicConsent.pcInheritedFieldCount;
        if (n2 < 0) {
            throw new IllegalArgumentException();
        }
        switch (n2) {
            case 0: {
                this.pcStateManager.providedObjectField((PersistenceCapable)this, n, (Object)this.clientConsentStatus);
                return;
            }
            case 1: {
                this.pcStateManager.providedObjectField((PersistenceCapable)this, n, (Object)this.consentToShareData);
                return;
            }
            case 2: {
                this.pcStateManager.providedObjectField((PersistenceCapable)this, n, (Object)this.createdDate);
                return;
            }
            case 3: {
                this.pcStateManager.providedBooleanField((PersistenceCapable)this, n, this.excludeMentalHealthData);
                return;
            }
            case 4: {
                this.pcStateManager.providedObjectField((PersistenceCapable)this, n, (Object)this.expiry);
                return;
            }
            case 5: {
                this.pcStateManager.providedObjectField((PersistenceCapable)this, n, (Object)this.facilityDemographicPk);
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
    
    protected void pcCopyField(final CachedDemographicConsent cachedDemographicConsent, final int n) {
        final int n2 = n - CachedDemographicConsent.pcInheritedFieldCount;
        if (n2 < 0) {
            throw new IllegalArgumentException();
        }
        switch (n2) {
            case 0: {
                this.clientConsentStatus = cachedDemographicConsent.clientConsentStatus;
                return;
            }
            case 1: {
                this.consentToShareData = cachedDemographicConsent.consentToShareData;
                return;
            }
            case 2: {
                this.createdDate = cachedDemographicConsent.createdDate;
                return;
            }
            case 3: {
                this.excludeMentalHealthData = cachedDemographicConsent.excludeMentalHealthData;
                return;
            }
            case 4: {
                this.expiry = cachedDemographicConsent.expiry;
                return;
            }
            case 5: {
                this.facilityDemographicPk = cachedDemographicConsent.facilityDemographicPk;
                return;
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
    }
    
    public void pcCopyFields(final Object o, final int[] array) {
        final CachedDemographicConsent cachedDemographicConsent = (CachedDemographicConsent)o;
        if (cachedDemographicConsent.pcStateManager != this.pcStateManager) {
            throw new IllegalArgumentException();
        }
        if (this.pcStateManager == null) {
            throw new IllegalStateException();
        }
        for (int i = 0; i < array.length; ++i) {
            this.pcCopyField(cachedDemographicConsent, array[i]);
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
        fieldConsumer.storeObjectField(5 + CachedDemographicConsent.pcInheritedFieldCount, ((ObjectId)o).getId());
    }
    
    public void pcCopyKeyFieldsFromObjectId(final Object o) {
        this.facilityDemographicPk = (FacilityIdIntegerCompositePk)((ObjectId)o).getId();
    }
    
    public Object pcNewObjectIdInstance(final Object o) {
        throw new IllegalArgumentException("The id type \"class org.apache.openjpa.util.ObjectId\" specified by persistent type \"class ca.openosp.openo.caisi_integrator.dao.CachedDemographicConsent\" does not have a public class org.apache.openjpa.util.ObjectId(String) or class org.apache.openjpa.util.ObjectId(Class, String) constructor.");
    }
    
    public Object pcNewObjectIdInstance() {
        return new ObjectId((CachedDemographicConsent.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicConsent != null) ? CachedDemographicConsent.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicConsent : (CachedDemographicConsent.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicConsent = class$("ca.openosp.openo.caisi_integrator.dao.CachedDemographicConsent")), (Object)this.facilityDemographicPk);
    }
    
    private static final ConsentStatus pcGetclientConsentStatus(final CachedDemographicConsent cachedDemographicConsent) {
        if (cachedDemographicConsent.pcStateManager == null) {
            return cachedDemographicConsent.clientConsentStatus;
        }
        cachedDemographicConsent.pcStateManager.accessingField(CachedDemographicConsent.pcInheritedFieldCount + 0);
        return cachedDemographicConsent.clientConsentStatus;
    }
    
    private static final void pcSetclientConsentStatus(final CachedDemographicConsent cachedDemographicConsent, final ConsentStatus clientConsentStatus) {
        if (cachedDemographicConsent.pcStateManager == null) {
            cachedDemographicConsent.clientConsentStatus = clientConsentStatus;
            return;
        }
        cachedDemographicConsent.pcStateManager.settingObjectField((PersistenceCapable)cachedDemographicConsent, CachedDemographicConsent.pcInheritedFieldCount + 0, (Object)cachedDemographicConsent.clientConsentStatus, (Object)clientConsentStatus, 0);
    }
    
    private static final Map pcGetconsentToShareData(final CachedDemographicConsent cachedDemographicConsent) {
        if (cachedDemographicConsent.pcStateManager == null) {
            return cachedDemographicConsent.consentToShareData;
        }
        cachedDemographicConsent.pcStateManager.accessingField(CachedDemographicConsent.pcInheritedFieldCount + 1);
        return cachedDemographicConsent.consentToShareData;
    }
    
    private static final void pcSetconsentToShareData(final CachedDemographicConsent cachedDemographicConsent, final Map consentToShareData) {
        if (cachedDemographicConsent.pcStateManager == null) {
            cachedDemographicConsent.consentToShareData = consentToShareData;
            return;
        }
        cachedDemographicConsent.pcStateManager.settingObjectField((PersistenceCapable)cachedDemographicConsent, CachedDemographicConsent.pcInheritedFieldCount + 1, (Object)cachedDemographicConsent.consentToShareData, (Object)consentToShareData, 0);
    }
    
    private static final Date pcGetcreatedDate(final CachedDemographicConsent cachedDemographicConsent) {
        if (cachedDemographicConsent.pcStateManager == null) {
            return cachedDemographicConsent.createdDate;
        }
        cachedDemographicConsent.pcStateManager.accessingField(CachedDemographicConsent.pcInheritedFieldCount + 2);
        return cachedDemographicConsent.createdDate;
    }
    
    private static final void pcSetcreatedDate(final CachedDemographicConsent cachedDemographicConsent, final Date createdDate) {
        if (cachedDemographicConsent.pcStateManager == null) {
            cachedDemographicConsent.createdDate = createdDate;
            return;
        }
        cachedDemographicConsent.pcStateManager.settingObjectField((PersistenceCapable)cachedDemographicConsent, CachedDemographicConsent.pcInheritedFieldCount + 2, (Object)cachedDemographicConsent.createdDate, (Object)createdDate, 0);
    }
    
    private static final boolean pcGetexcludeMentalHealthData(final CachedDemographicConsent cachedDemographicConsent) {
        if (cachedDemographicConsent.pcStateManager == null) {
            return cachedDemographicConsent.excludeMentalHealthData;
        }
        cachedDemographicConsent.pcStateManager.accessingField(CachedDemographicConsent.pcInheritedFieldCount + 3);
        return cachedDemographicConsent.excludeMentalHealthData;
    }
    
    private static final void pcSetexcludeMentalHealthData(final CachedDemographicConsent cachedDemographicConsent, final boolean excludeMentalHealthData) {
        if (cachedDemographicConsent.pcStateManager == null) {
            cachedDemographicConsent.excludeMentalHealthData = excludeMentalHealthData;
            return;
        }
        cachedDemographicConsent.pcStateManager.settingBooleanField((PersistenceCapable)cachedDemographicConsent, CachedDemographicConsent.pcInheritedFieldCount + 3, cachedDemographicConsent.excludeMentalHealthData, excludeMentalHealthData, 0);
    }
    
    private static final Date pcGetexpiry(final CachedDemographicConsent cachedDemographicConsent) {
        if (cachedDemographicConsent.pcStateManager == null) {
            return cachedDemographicConsent.expiry;
        }
        cachedDemographicConsent.pcStateManager.accessingField(CachedDemographicConsent.pcInheritedFieldCount + 4);
        return cachedDemographicConsent.expiry;
    }
    
    private static final void pcSetexpiry(final CachedDemographicConsent cachedDemographicConsent, final Date expiry) {
        if (cachedDemographicConsent.pcStateManager == null) {
            cachedDemographicConsent.expiry = expiry;
            return;
        }
        cachedDemographicConsent.pcStateManager.settingObjectField((PersistenceCapable)cachedDemographicConsent, CachedDemographicConsent.pcInheritedFieldCount + 4, (Object)cachedDemographicConsent.expiry, (Object)expiry, 0);
    }
    
    private static final FacilityIdIntegerCompositePk pcGetfacilityDemographicPk(final CachedDemographicConsent cachedDemographicConsent) {
        if (cachedDemographicConsent.pcStateManager == null) {
            return cachedDemographicConsent.facilityDemographicPk;
        }
        cachedDemographicConsent.pcStateManager.accessingField(CachedDemographicConsent.pcInheritedFieldCount + 5);
        return cachedDemographicConsent.facilityDemographicPk;
    }
    
    private static final void pcSetfacilityDemographicPk(final CachedDemographicConsent cachedDemographicConsent, final FacilityIdIntegerCompositePk facilityDemographicPk) {
        if (cachedDemographicConsent.pcStateManager == null) {
            cachedDemographicConsent.facilityDemographicPk = facilityDemographicPk;
            return;
        }
        cachedDemographicConsent.pcStateManager.settingObjectField((PersistenceCapable)cachedDemographicConsent, CachedDemographicConsent.pcInheritedFieldCount + 5, (Object)cachedDemographicConsent.facilityDemographicPk, (Object)facilityDemographicPk, 0);
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
    
    public enum ConsentStatus
    {
        GIVEN, 
        REVOKED;
    }
}
