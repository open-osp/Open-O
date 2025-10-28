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
public class CachedAdmission extends AbstractModel<FacilityIdIntegerCompositePk> implements PersistenceCapable
{
    @EmbeddedId
    private FacilityIdIntegerCompositePk facilityIdIntegerCompositePk;
    @Column(nullable = false)
    @Index
    private int caisiDemographicId;
    @Column(nullable = false)
    @Index
    private int caisiProgramId;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @Index
    private Date admissionDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dischargeDate;
    @Column(columnDefinition = "mediumtext")
    private String admissionNotes;
    @Column(columnDefinition = "mediumtext")
    private String dischargeNotes;
    private static int pcInheritedFieldCount;
    private static String[] pcFieldNames;
    private static Class[] pcFieldTypes;
    private static byte[] pcFieldFlags;
    private static Class pcPCSuperclass;
    protected transient StateManager pcStateManager;
    static /* synthetic */ Class class$Ljava$util$Date;
    static /* synthetic */ Class class$Ljava$lang$String;
    static /* synthetic */ Class class$Lca$openosp$openo$caisi_integrator$dao$FacilityIdIntegerCompositePk;
    static /* synthetic */ Class class$Lca$openosp$openo$caisi_integrator$dao$CachedAdmission;
    private transient Object pcDetachedState;
    private static final long serialVersionUID;
    
    public CachedAdmission() {
        this.facilityIdIntegerCompositePk = null;
        this.caisiDemographicId = 0;
        this.caisiProgramId = 0;
        this.admissionDate = null;
        this.dischargeDate = null;
        this.admissionNotes = null;
        this.dischargeNotes = null;
    }
    
    public FacilityIdIntegerCompositePk getFacilityIdIntegerCompositePk() {
        return pcGetfacilityIdIntegerCompositePk(this);
    }
    
    public void setFacilityIdIntegerCompositePk(final FacilityIdIntegerCompositePk facilityIdIntegerCompositePk) {
        pcSetfacilityIdIntegerCompositePk(this, facilityIdIntegerCompositePk);
    }
    
    @Override
    public FacilityIdIntegerCompositePk getId() {
        return pcGetfacilityIdIntegerCompositePk(this);
    }
    
    public int getCaisiDemographicId() {
        return pcGetcaisiDemographicId(this);
    }
    
    public void setCaisiDemographicId(final int caisiDemographicId) {
        pcSetcaisiDemographicId(this, caisiDemographicId);
    }
    
    public int getCaisiProgramId() {
        return pcGetcaisiProgramId(this);
    }
    
    public void setCaisiProgramId(final int caisiProgramId) {
        pcSetcaisiProgramId(this, caisiProgramId);
    }
    
    public Date getAdmissionDate() {
        return pcGetadmissionDate(this);
    }
    
    public void setAdmissionDate(final Date admissionDate) {
        pcSetadmissionDate(this, admissionDate);
    }
    
    public Date getDischargeDate() {
        return pcGetdischargeDate(this);
    }
    
    public void setDischargeDate(final Date dischargeDate) {
        pcSetdischargeDate(this, dischargeDate);
    }
    
    public String getAdmissionNotes() {
        return pcGetadmissionNotes(this);
    }
    
    public void setAdmissionNotes(final String admissionNotes) {
        pcSetadmissionNotes(this, admissionNotes);
    }
    
    public String getDischargeNotes() {
        return pcGetdischargeNotes(this);
    }
    
    public void setDischargeNotes(final String dischargeNotes) {
        pcSetdischargeNotes(this, dischargeNotes);
    }
    
    public int pcGetEnhancementContractVersion() {
        return 2;
    }
    
    static {
        serialVersionUID = 475490153351795557L;
        CachedAdmission.pcFieldNames = new String[] { "admissionDate", "admissionNotes", "caisiDemographicId", "caisiProgramId", "dischargeDate", "dischargeNotes", "facilityIdIntegerCompositePk" };
        CachedAdmission.pcFieldTypes = new Class[] { (CachedAdmission.class$Ljava$util$Date != null) ? CachedAdmission.class$Ljava$util$Date : (CachedAdmission.class$Ljava$util$Date = class$("java.util.Date")), (CachedAdmission.class$Ljava$lang$String != null) ? CachedAdmission.class$Ljava$lang$String : (CachedAdmission.class$Ljava$lang$String = class$("java.lang.String")), Integer.TYPE, Integer.TYPE, (CachedAdmission.class$Ljava$util$Date != null) ? CachedAdmission.class$Ljava$util$Date : (CachedAdmission.class$Ljava$util$Date = class$("java.util.Date")), (CachedAdmission.class$Ljava$lang$String != null) ? CachedAdmission.class$Ljava$lang$String : (CachedAdmission.class$Ljava$lang$String = class$("java.lang.String")), (CachedAdmission.class$Lca$openosp$openo$caisi_integrator$dao$FacilityIdIntegerCompositePk != null) ? CachedAdmission.class$Lca$openosp$openo$caisi_integrator$dao$FacilityIdIntegerCompositePk : (CachedAdmission.class$Lca$openosp$openo$caisi_integrator$dao$FacilityIdIntegerCompositePk = class$("ca.openosp.openo.caisi_integrator.dao.FacilityIdIntegerCompositePk")) };
        CachedAdmission.pcFieldFlags = new byte[] { 26, 26, 26, 26, 26, 26, 26 };
        PCRegistry.register((CachedAdmission.class$Lca$openosp$openo$caisi_integrator$dao$CachedAdmission != null) ? CachedAdmission.class$Lca$openosp$openo$caisi_integrator$dao$CachedAdmission : (CachedAdmission.class$Lca$openosp$openo$caisi_integrator$dao$CachedAdmission = class$("ca.openosp.openo.caisi_integrator.dao.CachedAdmission")), CachedAdmission.pcFieldNames, CachedAdmission.pcFieldTypes, CachedAdmission.pcFieldFlags, CachedAdmission.pcPCSuperclass, "CachedAdmission", (PersistenceCapable)new CachedAdmission());
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
        this.admissionDate = null;
        this.admissionNotes = null;
        this.caisiDemographicId = 0;
        this.caisiProgramId = 0;
        this.dischargeDate = null;
        this.dischargeNotes = null;
        this.facilityIdIntegerCompositePk = null;
    }
    
    public PersistenceCapable pcNewInstance(final StateManager pcStateManager, final Object o, final boolean b) {
        final CachedAdmission cachedAdmission = new CachedAdmission();
        if (b) {
            cachedAdmission.pcClearFields();
        }
        cachedAdmission.pcStateManager = pcStateManager;
        cachedAdmission.pcCopyKeyFieldsFromObjectId(o);
        return (PersistenceCapable)cachedAdmission;
    }
    
    public PersistenceCapable pcNewInstance(final StateManager pcStateManager, final boolean b) {
        final CachedAdmission cachedAdmission = new CachedAdmission();
        if (b) {
            cachedAdmission.pcClearFields();
        }
        cachedAdmission.pcStateManager = pcStateManager;
        return (PersistenceCapable)cachedAdmission;
    }
    
    protected static int pcGetManagedFieldCount() {
        return 7;
    }
    
    public void pcReplaceField(final int n) {
        final int n2 = n - CachedAdmission.pcInheritedFieldCount;
        if (n2 < 0) {
            throw new IllegalArgumentException();
        }
        switch (n2) {
            case 0: {
                this.admissionDate = (Date)this.pcStateManager.replaceObjectField((PersistenceCapable)this, n);
                return;
            }
            case 1: {
                this.admissionNotes = this.pcStateManager.replaceStringField((PersistenceCapable)this, n);
                return;
            }
            case 2: {
                this.caisiDemographicId = this.pcStateManager.replaceIntField((PersistenceCapable)this, n);
                return;
            }
            case 3: {
                this.caisiProgramId = this.pcStateManager.replaceIntField((PersistenceCapable)this, n);
                return;
            }
            case 4: {
                this.dischargeDate = (Date)this.pcStateManager.replaceObjectField((PersistenceCapable)this, n);
                return;
            }
            case 5: {
                this.dischargeNotes = this.pcStateManager.replaceStringField((PersistenceCapable)this, n);
                return;
            }
            case 6: {
                this.facilityIdIntegerCompositePk = (FacilityIdIntegerCompositePk)this.pcStateManager.replaceObjectField((PersistenceCapable)this, n);
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
        final int n2 = n - CachedAdmission.pcInheritedFieldCount;
        if (n2 < 0) {
            throw new IllegalArgumentException();
        }
        switch (n2) {
            case 0: {
                this.pcStateManager.providedObjectField((PersistenceCapable)this, n, (Object)this.admissionDate);
                return;
            }
            case 1: {
                this.pcStateManager.providedStringField((PersistenceCapable)this, n, this.admissionNotes);
                return;
            }
            case 2: {
                this.pcStateManager.providedIntField((PersistenceCapable)this, n, this.caisiDemographicId);
                return;
            }
            case 3: {
                this.pcStateManager.providedIntField((PersistenceCapable)this, n, this.caisiProgramId);
                return;
            }
            case 4: {
                this.pcStateManager.providedObjectField((PersistenceCapable)this, n, (Object)this.dischargeDate);
                return;
            }
            case 5: {
                this.pcStateManager.providedStringField((PersistenceCapable)this, n, this.dischargeNotes);
                return;
            }
            case 6: {
                this.pcStateManager.providedObjectField((PersistenceCapable)this, n, (Object)this.facilityIdIntegerCompositePk);
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
    
    protected void pcCopyField(final CachedAdmission cachedAdmission, final int n) {
        final int n2 = n - CachedAdmission.pcInheritedFieldCount;
        if (n2 < 0) {
            throw new IllegalArgumentException();
        }
        switch (n2) {
            case 0: {
                this.admissionDate = cachedAdmission.admissionDate;
                return;
            }
            case 1: {
                this.admissionNotes = cachedAdmission.admissionNotes;
                return;
            }
            case 2: {
                this.caisiDemographicId = cachedAdmission.caisiDemographicId;
                return;
            }
            case 3: {
                this.caisiProgramId = cachedAdmission.caisiProgramId;
                return;
            }
            case 4: {
                this.dischargeDate = cachedAdmission.dischargeDate;
                return;
            }
            case 5: {
                this.dischargeNotes = cachedAdmission.dischargeNotes;
                return;
            }
            case 6: {
                this.facilityIdIntegerCompositePk = cachedAdmission.facilityIdIntegerCompositePk;
                return;
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
    }
    
    public void pcCopyFields(final Object o, final int[] array) {
        final CachedAdmission cachedAdmission = (CachedAdmission)o;
        if (cachedAdmission.pcStateManager != this.pcStateManager) {
            throw new IllegalArgumentException();
        }
        if (this.pcStateManager == null) {
            throw new IllegalStateException();
        }
        for (int i = 0; i < array.length; ++i) {
            this.pcCopyField(cachedAdmission, array[i]);
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
        fieldConsumer.storeObjectField(6 + CachedAdmission.pcInheritedFieldCount, ((ObjectId)o).getId());
    }
    
    public void pcCopyKeyFieldsFromObjectId(final Object o) {
        this.facilityIdIntegerCompositePk = (FacilityIdIntegerCompositePk)((ObjectId)o).getId();
    }
    
    public Object pcNewObjectIdInstance(final Object o) {
        throw new IllegalArgumentException("The id type \"class org.apache.openjpa.util.ObjectId\" specified by persistent type \"class ca.openosp.openo.caisi_integrator.dao.CachedAdmission\" does not have a public class org.apache.openjpa.util.ObjectId(String) or class org.apache.openjpa.util.ObjectId(Class, String) constructor.");
    }
    
    public Object pcNewObjectIdInstance() {
        return new ObjectId((CachedAdmission.class$Lca$openosp$openo$caisi_integrator$dao$CachedAdmission != null) ? CachedAdmission.class$Lca$openosp$openo$caisi_integrator$dao$CachedAdmission : (CachedAdmission.class$Lca$openosp$openo$caisi_integrator$dao$CachedAdmission = class$("ca.openosp.openo.caisi_integrator.dao.CachedAdmission")), (Object)this.facilityIdIntegerCompositePk);
    }
    
    private static final Date pcGetadmissionDate(final CachedAdmission cachedAdmission) {
        if (cachedAdmission.pcStateManager == null) {
            return cachedAdmission.admissionDate;
        }
        cachedAdmission.pcStateManager.accessingField(CachedAdmission.pcInheritedFieldCount + 0);
        return cachedAdmission.admissionDate;
    }
    
    private static final void pcSetadmissionDate(final CachedAdmission cachedAdmission, final Date admissionDate) {
        if (cachedAdmission.pcStateManager == null) {
            cachedAdmission.admissionDate = admissionDate;
            return;
        }
        cachedAdmission.pcStateManager.settingObjectField((PersistenceCapable)cachedAdmission, CachedAdmission.pcInheritedFieldCount + 0, (Object)cachedAdmission.admissionDate, (Object)admissionDate, 0);
    }
    
    private static final String pcGetadmissionNotes(final CachedAdmission cachedAdmission) {
        if (cachedAdmission.pcStateManager == null) {
            return cachedAdmission.admissionNotes;
        }
        cachedAdmission.pcStateManager.accessingField(CachedAdmission.pcInheritedFieldCount + 1);
        return cachedAdmission.admissionNotes;
    }
    
    private static final void pcSetadmissionNotes(final CachedAdmission cachedAdmission, final String admissionNotes) {
        if (cachedAdmission.pcStateManager == null) {
            cachedAdmission.admissionNotes = admissionNotes;
            return;
        }
        cachedAdmission.pcStateManager.settingStringField((PersistenceCapable)cachedAdmission, CachedAdmission.pcInheritedFieldCount + 1, cachedAdmission.admissionNotes, admissionNotes, 0);
    }
    
    private static final int pcGetcaisiDemographicId(final CachedAdmission cachedAdmission) {
        if (cachedAdmission.pcStateManager == null) {
            return cachedAdmission.caisiDemographicId;
        }
        cachedAdmission.pcStateManager.accessingField(CachedAdmission.pcInheritedFieldCount + 2);
        return cachedAdmission.caisiDemographicId;
    }
    
    private static final void pcSetcaisiDemographicId(final CachedAdmission cachedAdmission, final int caisiDemographicId) {
        if (cachedAdmission.pcStateManager == null) {
            cachedAdmission.caisiDemographicId = caisiDemographicId;
            return;
        }
        cachedAdmission.pcStateManager.settingIntField((PersistenceCapable)cachedAdmission, CachedAdmission.pcInheritedFieldCount + 2, cachedAdmission.caisiDemographicId, caisiDemographicId, 0);
    }
    
    private static final int pcGetcaisiProgramId(final CachedAdmission cachedAdmission) {
        if (cachedAdmission.pcStateManager == null) {
            return cachedAdmission.caisiProgramId;
        }
        cachedAdmission.pcStateManager.accessingField(CachedAdmission.pcInheritedFieldCount + 3);
        return cachedAdmission.caisiProgramId;
    }
    
    private static final void pcSetcaisiProgramId(final CachedAdmission cachedAdmission, final int caisiProgramId) {
        if (cachedAdmission.pcStateManager == null) {
            cachedAdmission.caisiProgramId = caisiProgramId;
            return;
        }
        cachedAdmission.pcStateManager.settingIntField((PersistenceCapable)cachedAdmission, CachedAdmission.pcInheritedFieldCount + 3, cachedAdmission.caisiProgramId, caisiProgramId, 0);
    }
    
    private static final Date pcGetdischargeDate(final CachedAdmission cachedAdmission) {
        if (cachedAdmission.pcStateManager == null) {
            return cachedAdmission.dischargeDate;
        }
        cachedAdmission.pcStateManager.accessingField(CachedAdmission.pcInheritedFieldCount + 4);
        return cachedAdmission.dischargeDate;
    }
    
    private static final void pcSetdischargeDate(final CachedAdmission cachedAdmission, final Date dischargeDate) {
        if (cachedAdmission.pcStateManager == null) {
            cachedAdmission.dischargeDate = dischargeDate;
            return;
        }
        cachedAdmission.pcStateManager.settingObjectField((PersistenceCapable)cachedAdmission, CachedAdmission.pcInheritedFieldCount + 4, (Object)cachedAdmission.dischargeDate, (Object)dischargeDate, 0);
    }
    
    private static final String pcGetdischargeNotes(final CachedAdmission cachedAdmission) {
        if (cachedAdmission.pcStateManager == null) {
            return cachedAdmission.dischargeNotes;
        }
        cachedAdmission.pcStateManager.accessingField(CachedAdmission.pcInheritedFieldCount + 5);
        return cachedAdmission.dischargeNotes;
    }
    
    private static final void pcSetdischargeNotes(final CachedAdmission cachedAdmission, final String dischargeNotes) {
        if (cachedAdmission.pcStateManager == null) {
            cachedAdmission.dischargeNotes = dischargeNotes;
            return;
        }
        cachedAdmission.pcStateManager.settingStringField((PersistenceCapable)cachedAdmission, CachedAdmission.pcInheritedFieldCount + 5, cachedAdmission.dischargeNotes, dischargeNotes, 0);
    }
    
    private static final FacilityIdIntegerCompositePk pcGetfacilityIdIntegerCompositePk(final CachedAdmission cachedAdmission) {
        if (cachedAdmission.pcStateManager == null) {
            return cachedAdmission.facilityIdIntegerCompositePk;
        }
        cachedAdmission.pcStateManager.accessingField(CachedAdmission.pcInheritedFieldCount + 6);
        return cachedAdmission.facilityIdIntegerCompositePk;
    }
    
    private static final void pcSetfacilityIdIntegerCompositePk(final CachedAdmission cachedAdmission, final FacilityIdIntegerCompositePk facilityIdIntegerCompositePk) {
        if (cachedAdmission.pcStateManager == null) {
            cachedAdmission.facilityIdIntegerCompositePk = facilityIdIntegerCompositePk;
            return;
        }
        cachedAdmission.pcStateManager.settingObjectField((PersistenceCapable)cachedAdmission, CachedAdmission.pcInheritedFieldCount + 6, (Object)cachedAdmission.facilityIdIntegerCompositePk, (Object)facilityIdIntegerCompositePk, 0);
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
