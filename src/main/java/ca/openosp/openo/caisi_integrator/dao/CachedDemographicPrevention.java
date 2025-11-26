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
public class CachedDemographicPrevention extends AbstractModel<FacilityIdIntegerCompositePk> implements PersistenceCapable
{
    @EmbeddedId
    private FacilityIdIntegerCompositePk facilityPreventionPk;
    @Column(nullable = false)
    @Index
    private Integer caisiDemographicId;
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date preventionDate;
    @Column(nullable = false, length = 16)
    private String caisiProviderId;
    @Column(nullable = false, length = 32)
    private String preventionType;
    @Temporal(TemporalType.DATE)
    private Date nextDate;
    @Column(columnDefinition = "tinyint(1)")
    private boolean refused;
    @Column(columnDefinition = "tinyint(1)")
    private boolean never;
    @Column(columnDefinition = "mediumblob")
    private String attributes;
    private static int pcInheritedFieldCount;
    private static String[] pcFieldNames;
    private static Class[] pcFieldTypes;
    private static byte[] pcFieldFlags;
    private static Class pcPCSuperclass;
    protected transient StateManager pcStateManager;
    static /* synthetic */ Class class$Ljava$lang$String;
    static /* synthetic */ Class class$Ljava$lang$Integer;
    static /* synthetic */ Class class$Lca$openosp$openo$caisi_integrator$dao$FacilityIdIntegerCompositePk;
    static /* synthetic */ Class class$Ljava$util$Date;
    static /* synthetic */ Class class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicPrevention;
    private transient Object pcDetachedState;
    private static final long serialVersionUID;
    
    public CachedDemographicPrevention() {
        this.facilityPreventionPk = null;
        this.caisiDemographicId = null;
        this.preventionDate = null;
        this.caisiProviderId = null;
        this.preventionType = null;
        this.nextDate = null;
        this.refused = false;
        this.never = false;
        this.attributes = null;
    }
    
    @Override
    public FacilityIdIntegerCompositePk getId() {
        return pcGetfacilityPreventionPk(this);
    }
    
    public FacilityIdIntegerCompositePk getFacilityPreventionPk() {
        return pcGetfacilityPreventionPk(this);
    }
    
    public void setFacilityPreventionPk(final FacilityIdIntegerCompositePk facilityPreventionPk) {
        pcSetfacilityPreventionPk(this, facilityPreventionPk);
    }
    
    public Integer getCaisiDemographicId() {
        return pcGetcaisiDemographicId(this);
    }
    
    public void setCaisiDemographicId(final Integer caisiDemographicId) {
        pcSetcaisiDemographicId(this, caisiDemographicId);
    }
    
    public Date getPreventionDate() {
        return pcGetpreventionDate(this);
    }
    
    public void setPreventionDate(final Date preventionDate) {
        pcSetpreventionDate(this, preventionDate);
    }
    
    public String getCaisiProviderId() {
        return pcGetcaisiProviderId(this);
    }
    
    public void setCaisiProviderId(final String caisiProviderId) {
        pcSetcaisiProviderId(this, caisiProviderId);
    }
    
    public String getPreventionType() {
        return pcGetpreventionType(this);
    }
    
    public void setPreventionType(final String preventionType) {
        pcSetpreventionType(this, preventionType);
    }
    
    public Date getNextDate() {
        return pcGetnextDate(this);
    }
    
    public void setNextDate(final Date nextDate) {
        pcSetnextDate(this, nextDate);
    }
    
    public String getAttributes() {
        return pcGetattributes(this);
    }
    
    public void setAttributes(final String attributes) {
        pcSetattributes(this, attributes);
    }
    
    public boolean isRefused() {
        return pcGetrefused(this);
    }
    
    public void setRefused(final boolean refused) {
        pcSetrefused(this, refused);
    }
    
    public boolean isNever() {
        return pcGetnever(this);
    }
    
    public void setNever(final boolean never) {
        pcSetnever(this, never);
    }
    
    public int pcGetEnhancementContractVersion() {
        return 2;
    }
    
    static {
        serialVersionUID = -5461784508345120397L;
        CachedDemographicPrevention.pcFieldNames = new String[] { "attributes", "caisiDemographicId", "caisiProviderId", "facilityPreventionPk", "never", "nextDate", "preventionDate", "preventionType", "refused" };
        CachedDemographicPrevention.pcFieldTypes = new Class[] { (CachedDemographicPrevention.class$Ljava$lang$String != null) ? CachedDemographicPrevention.class$Ljava$lang$String : (CachedDemographicPrevention.class$Ljava$lang$String = class$("java.lang.String")), (CachedDemographicPrevention.class$Ljava$lang$Integer != null) ? CachedDemographicPrevention.class$Ljava$lang$Integer : (CachedDemographicPrevention.class$Ljava$lang$Integer = class$("java.lang.Integer")), (CachedDemographicPrevention.class$Ljava$lang$String != null) ? CachedDemographicPrevention.class$Ljava$lang$String : (CachedDemographicPrevention.class$Ljava$lang$String = class$("java.lang.String")), (CachedDemographicPrevention.class$Lca$openosp$openo$caisi_integrator$dao$FacilityIdIntegerCompositePk != null) ? CachedDemographicPrevention.class$Lca$openosp$openo$caisi_integrator$dao$FacilityIdIntegerCompositePk : (CachedDemographicPrevention.class$Lca$openosp$openo$caisi_integrator$dao$FacilityIdIntegerCompositePk = class$("ca.openosp.openo.caisi_integrator.dao.FacilityIdIntegerCompositePk")), Boolean.TYPE, (CachedDemographicPrevention.class$Ljava$util$Date != null) ? CachedDemographicPrevention.class$Ljava$util$Date : (CachedDemographicPrevention.class$Ljava$util$Date = class$("java.util.Date")), (CachedDemographicPrevention.class$Ljava$util$Date != null) ? CachedDemographicPrevention.class$Ljava$util$Date : (CachedDemographicPrevention.class$Ljava$util$Date = class$("java.util.Date")), (CachedDemographicPrevention.class$Ljava$lang$String != null) ? CachedDemographicPrevention.class$Ljava$lang$String : (CachedDemographicPrevention.class$Ljava$lang$String = class$("java.lang.String")), Boolean.TYPE };
        CachedDemographicPrevention.pcFieldFlags = new byte[] { 26, 26, 26, 26, 26, 26, 26, 26, 26 };
        PCRegistry.register((CachedDemographicPrevention.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicPrevention != null) ? CachedDemographicPrevention.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicPrevention : (CachedDemographicPrevention.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicPrevention = class$("ca.openosp.openo.caisi_integrator.dao.CachedDemographicPrevention")), CachedDemographicPrevention.pcFieldNames, CachedDemographicPrevention.pcFieldTypes, CachedDemographicPrevention.pcFieldFlags, CachedDemographicPrevention.pcPCSuperclass, "CachedDemographicPrevention", (PersistenceCapable)new CachedDemographicPrevention());
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
        this.attributes = null;
        this.caisiDemographicId = null;
        this.caisiProviderId = null;
        this.facilityPreventionPk = null;
        this.never = false;
        this.nextDate = null;
        this.preventionDate = null;
        this.preventionType = null;
        this.refused = false;
    }
    
    public PersistenceCapable pcNewInstance(final StateManager pcStateManager, final Object o, final boolean b) {
        final CachedDemographicPrevention cachedDemographicPrevention = new CachedDemographicPrevention();
        if (b) {
            cachedDemographicPrevention.pcClearFields();
        }
        cachedDemographicPrevention.pcStateManager = pcStateManager;
        cachedDemographicPrevention.pcCopyKeyFieldsFromObjectId(o);
        return (PersistenceCapable)cachedDemographicPrevention;
    }
    
    public PersistenceCapable pcNewInstance(final StateManager pcStateManager, final boolean b) {
        final CachedDemographicPrevention cachedDemographicPrevention = new CachedDemographicPrevention();
        if (b) {
            cachedDemographicPrevention.pcClearFields();
        }
        cachedDemographicPrevention.pcStateManager = pcStateManager;
        return (PersistenceCapable)cachedDemographicPrevention;
    }
    
    protected static int pcGetManagedFieldCount() {
        return 9;
    }
    
    public void pcReplaceField(final int n) {
        final int n2 = n - CachedDemographicPrevention.pcInheritedFieldCount;
        if (n2 < 0) {
            throw new IllegalArgumentException();
        }
        switch (n2) {
            case 0: {
                this.attributes = this.pcStateManager.replaceStringField((PersistenceCapable)this, n);
                return;
            }
            case 1: {
                this.caisiDemographicId = (Integer)this.pcStateManager.replaceObjectField((PersistenceCapable)this, n);
                return;
            }
            case 2: {
                this.caisiProviderId = this.pcStateManager.replaceStringField((PersistenceCapable)this, n);
                return;
            }
            case 3: {
                this.facilityPreventionPk = (FacilityIdIntegerCompositePk)this.pcStateManager.replaceObjectField((PersistenceCapable)this, n);
                return;
            }
            case 4: {
                this.never = this.pcStateManager.replaceBooleanField((PersistenceCapable)this, n);
                return;
            }
            case 5: {
                this.nextDate = (Date)this.pcStateManager.replaceObjectField((PersistenceCapable)this, n);
                return;
            }
            case 6: {
                this.preventionDate = (Date)this.pcStateManager.replaceObjectField((PersistenceCapable)this, n);
                return;
            }
            case 7: {
                this.preventionType = this.pcStateManager.replaceStringField((PersistenceCapable)this, n);
                return;
            }
            case 8: {
                this.refused = this.pcStateManager.replaceBooleanField((PersistenceCapable)this, n);
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
        final int n2 = n - CachedDemographicPrevention.pcInheritedFieldCount;
        if (n2 < 0) {
            throw new IllegalArgumentException();
        }
        switch (n2) {
            case 0: {
                this.pcStateManager.providedStringField((PersistenceCapable)this, n, this.attributes);
                return;
            }
            case 1: {
                this.pcStateManager.providedObjectField((PersistenceCapable)this, n, (Object)this.caisiDemographicId);
                return;
            }
            case 2: {
                this.pcStateManager.providedStringField((PersistenceCapable)this, n, this.caisiProviderId);
                return;
            }
            case 3: {
                this.pcStateManager.providedObjectField((PersistenceCapable)this, n, (Object)this.facilityPreventionPk);
                return;
            }
            case 4: {
                this.pcStateManager.providedBooleanField((PersistenceCapable)this, n, this.never);
                return;
            }
            case 5: {
                this.pcStateManager.providedObjectField((PersistenceCapable)this, n, (Object)this.nextDate);
                return;
            }
            case 6: {
                this.pcStateManager.providedObjectField((PersistenceCapable)this, n, (Object)this.preventionDate);
                return;
            }
            case 7: {
                this.pcStateManager.providedStringField((PersistenceCapable)this, n, this.preventionType);
                return;
            }
            case 8: {
                this.pcStateManager.providedBooleanField((PersistenceCapable)this, n, this.refused);
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
    
    protected void pcCopyField(final CachedDemographicPrevention cachedDemographicPrevention, final int n) {
        final int n2 = n - CachedDemographicPrevention.pcInheritedFieldCount;
        if (n2 < 0) {
            throw new IllegalArgumentException();
        }
        switch (n2) {
            case 0: {
                this.attributes = cachedDemographicPrevention.attributes;
                return;
            }
            case 1: {
                this.caisiDemographicId = cachedDemographicPrevention.caisiDemographicId;
                return;
            }
            case 2: {
                this.caisiProviderId = cachedDemographicPrevention.caisiProviderId;
                return;
            }
            case 3: {
                this.facilityPreventionPk = cachedDemographicPrevention.facilityPreventionPk;
                return;
            }
            case 4: {
                this.never = cachedDemographicPrevention.never;
                return;
            }
            case 5: {
                this.nextDate = cachedDemographicPrevention.nextDate;
                return;
            }
            case 6: {
                this.preventionDate = cachedDemographicPrevention.preventionDate;
                return;
            }
            case 7: {
                this.preventionType = cachedDemographicPrevention.preventionType;
                return;
            }
            case 8: {
                this.refused = cachedDemographicPrevention.refused;
                return;
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
    }
    
    public void pcCopyFields(final Object o, final int[] array) {
        final CachedDemographicPrevention cachedDemographicPrevention = (CachedDemographicPrevention)o;
        if (cachedDemographicPrevention.pcStateManager != this.pcStateManager) {
            throw new IllegalArgumentException();
        }
        if (this.pcStateManager == null) {
            throw new IllegalStateException();
        }
        for (int i = 0; i < array.length; ++i) {
            this.pcCopyField(cachedDemographicPrevention, array[i]);
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
        fieldConsumer.storeObjectField(3 + CachedDemographicPrevention.pcInheritedFieldCount, ((ObjectId)o).getId());
    }
    
    public void pcCopyKeyFieldsFromObjectId(final Object o) {
        this.facilityPreventionPk = (FacilityIdIntegerCompositePk)((ObjectId)o).getId();
    }
    
    public Object pcNewObjectIdInstance(final Object o) {
        throw new IllegalArgumentException("The id type \"class org.apache.openjpa.util.ObjectId\" specified by persistent type \"class ca.openosp.openo.caisi_integrator.dao.CachedDemographicPrevention\" does not have a public class org.apache.openjpa.util.ObjectId(String) or class org.apache.openjpa.util.ObjectId(Class, String) constructor.");
    }
    
    public Object pcNewObjectIdInstance() {
        return new ObjectId((CachedDemographicPrevention.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicPrevention != null) ? CachedDemographicPrevention.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicPrevention : (CachedDemographicPrevention.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicPrevention = class$("ca.openosp.openo.caisi_integrator.dao.CachedDemographicPrevention")), (Object)this.facilityPreventionPk);
    }
    
    private static final String pcGetattributes(final CachedDemographicPrevention cachedDemographicPrevention) {
        if (cachedDemographicPrevention.pcStateManager == null) {
            return cachedDemographicPrevention.attributes;
        }
        cachedDemographicPrevention.pcStateManager.accessingField(CachedDemographicPrevention.pcInheritedFieldCount + 0);
        return cachedDemographicPrevention.attributes;
    }
    
    private static final void pcSetattributes(final CachedDemographicPrevention cachedDemographicPrevention, final String attributes) {
        if (cachedDemographicPrevention.pcStateManager == null) {
            cachedDemographicPrevention.attributes = attributes;
            return;
        }
        cachedDemographicPrevention.pcStateManager.settingStringField((PersistenceCapable)cachedDemographicPrevention, CachedDemographicPrevention.pcInheritedFieldCount + 0, cachedDemographicPrevention.attributes, attributes, 0);
    }
    
    private static final Integer pcGetcaisiDemographicId(final CachedDemographicPrevention cachedDemographicPrevention) {
        if (cachedDemographicPrevention.pcStateManager == null) {
            return cachedDemographicPrevention.caisiDemographicId;
        }
        cachedDemographicPrevention.pcStateManager.accessingField(CachedDemographicPrevention.pcInheritedFieldCount + 1);
        return cachedDemographicPrevention.caisiDemographicId;
    }
    
    private static final void pcSetcaisiDemographicId(final CachedDemographicPrevention cachedDemographicPrevention, final Integer caisiDemographicId) {
        if (cachedDemographicPrevention.pcStateManager == null) {
            cachedDemographicPrevention.caisiDemographicId = caisiDemographicId;
            return;
        }
        cachedDemographicPrevention.pcStateManager.settingObjectField((PersistenceCapable)cachedDemographicPrevention, CachedDemographicPrevention.pcInheritedFieldCount + 1, (Object)cachedDemographicPrevention.caisiDemographicId, (Object)caisiDemographicId, 0);
    }
    
    private static final String pcGetcaisiProviderId(final CachedDemographicPrevention cachedDemographicPrevention) {
        if (cachedDemographicPrevention.pcStateManager == null) {
            return cachedDemographicPrevention.caisiProviderId;
        }
        cachedDemographicPrevention.pcStateManager.accessingField(CachedDemographicPrevention.pcInheritedFieldCount + 2);
        return cachedDemographicPrevention.caisiProviderId;
    }
    
    private static final void pcSetcaisiProviderId(final CachedDemographicPrevention cachedDemographicPrevention, final String caisiProviderId) {
        if (cachedDemographicPrevention.pcStateManager == null) {
            cachedDemographicPrevention.caisiProviderId = caisiProviderId;
            return;
        }
        cachedDemographicPrevention.pcStateManager.settingStringField((PersistenceCapable)cachedDemographicPrevention, CachedDemographicPrevention.pcInheritedFieldCount + 2, cachedDemographicPrevention.caisiProviderId, caisiProviderId, 0);
    }
    
    private static final FacilityIdIntegerCompositePk pcGetfacilityPreventionPk(final CachedDemographicPrevention cachedDemographicPrevention) {
        if (cachedDemographicPrevention.pcStateManager == null) {
            return cachedDemographicPrevention.facilityPreventionPk;
        }
        cachedDemographicPrevention.pcStateManager.accessingField(CachedDemographicPrevention.pcInheritedFieldCount + 3);
        return cachedDemographicPrevention.facilityPreventionPk;
    }
    
    private static final void pcSetfacilityPreventionPk(final CachedDemographicPrevention cachedDemographicPrevention, final FacilityIdIntegerCompositePk facilityPreventionPk) {
        if (cachedDemographicPrevention.pcStateManager == null) {
            cachedDemographicPrevention.facilityPreventionPk = facilityPreventionPk;
            return;
        }
        cachedDemographicPrevention.pcStateManager.settingObjectField((PersistenceCapable)cachedDemographicPrevention, CachedDemographicPrevention.pcInheritedFieldCount + 3, (Object)cachedDemographicPrevention.facilityPreventionPk, (Object)facilityPreventionPk, 0);
    }
    
    private static final boolean pcGetnever(final CachedDemographicPrevention cachedDemographicPrevention) {
        if (cachedDemographicPrevention.pcStateManager == null) {
            return cachedDemographicPrevention.never;
        }
        cachedDemographicPrevention.pcStateManager.accessingField(CachedDemographicPrevention.pcInheritedFieldCount + 4);
        return cachedDemographicPrevention.never;
    }
    
    private static final void pcSetnever(final CachedDemographicPrevention cachedDemographicPrevention, final boolean never) {
        if (cachedDemographicPrevention.pcStateManager == null) {
            cachedDemographicPrevention.never = never;
            return;
        }
        cachedDemographicPrevention.pcStateManager.settingBooleanField((PersistenceCapable)cachedDemographicPrevention, CachedDemographicPrevention.pcInheritedFieldCount + 4, cachedDemographicPrevention.never, never, 0);
    }
    
    private static final Date pcGetnextDate(final CachedDemographicPrevention cachedDemographicPrevention) {
        if (cachedDemographicPrevention.pcStateManager == null) {
            return cachedDemographicPrevention.nextDate;
        }
        cachedDemographicPrevention.pcStateManager.accessingField(CachedDemographicPrevention.pcInheritedFieldCount + 5);
        return cachedDemographicPrevention.nextDate;
    }
    
    private static final void pcSetnextDate(final CachedDemographicPrevention cachedDemographicPrevention, final Date nextDate) {
        if (cachedDemographicPrevention.pcStateManager == null) {
            cachedDemographicPrevention.nextDate = nextDate;
            return;
        }
        cachedDemographicPrevention.pcStateManager.settingObjectField((PersistenceCapable)cachedDemographicPrevention, CachedDemographicPrevention.pcInheritedFieldCount + 5, (Object)cachedDemographicPrevention.nextDate, (Object)nextDate, 0);
    }
    
    private static final Date pcGetpreventionDate(final CachedDemographicPrevention cachedDemographicPrevention) {
        if (cachedDemographicPrevention.pcStateManager == null) {
            return cachedDemographicPrevention.preventionDate;
        }
        cachedDemographicPrevention.pcStateManager.accessingField(CachedDemographicPrevention.pcInheritedFieldCount + 6);
        return cachedDemographicPrevention.preventionDate;
    }
    
    private static final void pcSetpreventionDate(final CachedDemographicPrevention cachedDemographicPrevention, final Date preventionDate) {
        if (cachedDemographicPrevention.pcStateManager == null) {
            cachedDemographicPrevention.preventionDate = preventionDate;
            return;
        }
        cachedDemographicPrevention.pcStateManager.settingObjectField((PersistenceCapable)cachedDemographicPrevention, CachedDemographicPrevention.pcInheritedFieldCount + 6, (Object)cachedDemographicPrevention.preventionDate, (Object)preventionDate, 0);
    }
    
    private static final String pcGetpreventionType(final CachedDemographicPrevention cachedDemographicPrevention) {
        if (cachedDemographicPrevention.pcStateManager == null) {
            return cachedDemographicPrevention.preventionType;
        }
        cachedDemographicPrevention.pcStateManager.accessingField(CachedDemographicPrevention.pcInheritedFieldCount + 7);
        return cachedDemographicPrevention.preventionType;
    }
    
    private static final void pcSetpreventionType(final CachedDemographicPrevention cachedDemographicPrevention, final String preventionType) {
        if (cachedDemographicPrevention.pcStateManager == null) {
            cachedDemographicPrevention.preventionType = preventionType;
            return;
        }
        cachedDemographicPrevention.pcStateManager.settingStringField((PersistenceCapable)cachedDemographicPrevention, CachedDemographicPrevention.pcInheritedFieldCount + 7, cachedDemographicPrevention.preventionType, preventionType, 0);
    }
    
    private static final boolean pcGetrefused(final CachedDemographicPrevention cachedDemographicPrevention) {
        if (cachedDemographicPrevention.pcStateManager == null) {
            return cachedDemographicPrevention.refused;
        }
        cachedDemographicPrevention.pcStateManager.accessingField(CachedDemographicPrevention.pcInheritedFieldCount + 8);
        return cachedDemographicPrevention.refused;
    }
    
    private static final void pcSetrefused(final CachedDemographicPrevention cachedDemographicPrevention, final boolean refused) {
        if (cachedDemographicPrevention.pcStateManager == null) {
            cachedDemographicPrevention.refused = refused;
            return;
        }
        cachedDemographicPrevention.pcStateManager.settingBooleanField((PersistenceCapable)cachedDemographicPrevention, CachedDemographicPrevention.pcInheritedFieldCount + 8, cachedDemographicPrevention.refused, refused, 0);
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
