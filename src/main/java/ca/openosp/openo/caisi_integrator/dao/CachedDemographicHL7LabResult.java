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
import org.apache.openjpa.persistence.jdbc.Index;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class CachedDemographicHL7LabResult extends AbstractModel<FacilityIdLabResultCompositePk> implements PersistenceCapable
{
    @EmbeddedId
    private FacilityIdLabResultCompositePk facilityIdLabResultCompositePk;
    @Column(nullable = false)
    @Index
    private int caisiDemographicId;
    @Column(length = 64)
    private String type;
    @Column(columnDefinition = "mediumblob")
    private String data;
    private static int pcInheritedFieldCount;
    private static String[] pcFieldNames;
    private static Class[] pcFieldTypes;
    private static byte[] pcFieldFlags;
    private static Class pcPCSuperclass;
    protected transient StateManager pcStateManager;
    static /* synthetic */ Class class$Ljava$lang$String;
    static /* synthetic */ Class class$Lca$openosp$openo$caisi_integrator$dao$FacilityIdLabResultCompositePk;
    static /* synthetic */ Class class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicHL7LabResult;
    private transient Object pcDetachedState;
    private static final long serialVersionUID;
    
    public CachedDemographicHL7LabResult() {
        this.caisiDemographicId = 0;
    }
    
    @Override
    public FacilityIdLabResultCompositePk getId() {
        return pcGetfacilityIdLabResultCompositePk(this);
    }
    
    public FacilityIdLabResultCompositePk getFacilityIdLabResultCompositePk() {
        return pcGetfacilityIdLabResultCompositePk(this);
    }
    
    public void setFacilityIdLabResultCompositePk(final FacilityIdLabResultCompositePk facilityIdLabResultCompositePk) {
        pcSetfacilityIdLabResultCompositePk(this, facilityIdLabResultCompositePk);
    }
    
    public int getCaisiDemographicId() {
        return pcGetcaisiDemographicId(this);
    }
    
    public void setCaisiDemographicId(final int caisiDemographicId) {
        pcSetcaisiDemographicId(this, caisiDemographicId);
    }
    
    public String getType() {
        return pcGettype(this);
    }
    
    public void setType(final String type) {
        pcSettype(this, type);
    }
    
    public String getData() {
        return pcGetdata(this);
    }
    
    public void setData(final String data) {
        pcSetdata(this, data);
    }
    
    public int pcGetEnhancementContractVersion() {
        return 2;
    }
    
    static {
        serialVersionUID = 8411054820363937199L;
        CachedDemographicHL7LabResult.pcFieldNames = new String[] { "caisiDemographicId", "data", "facilityIdLabResultCompositePk", "type" };
        CachedDemographicHL7LabResult.pcFieldTypes = new Class[] { Integer.TYPE, (CachedDemographicHL7LabResult.class$Ljava$lang$String != null) ? CachedDemographicHL7LabResult.class$Ljava$lang$String : (CachedDemographicHL7LabResult.class$Ljava$lang$String = class$("java.lang.String")), (CachedDemographicHL7LabResult.class$Lca$openosp$openo$caisi_integrator$dao$FacilityIdLabResultCompositePk != null) ? CachedDemographicHL7LabResult.class$Lca$openosp$openo$caisi_integrator$dao$FacilityIdLabResultCompositePk : (CachedDemographicHL7LabResult.class$Lca$openosp$openo$caisi_integrator$dao$FacilityIdLabResultCompositePk = class$("ca.openosp.openo.caisi_integrator.dao.FacilityIdLabResultCompositePk")), (CachedDemographicHL7LabResult.class$Ljava$lang$String != null) ? CachedDemographicHL7LabResult.class$Ljava$lang$String : (CachedDemographicHL7LabResult.class$Ljava$lang$String = class$("java.lang.String")) };
        CachedDemographicHL7LabResult.pcFieldFlags = new byte[] { 26, 26, 26, 26 };
        PCRegistry.register((CachedDemographicHL7LabResult.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicHL7LabResult != null) ? CachedDemographicHL7LabResult.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicHL7LabResult : (CachedDemographicHL7LabResult.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicHL7LabResult = class$("ca.openosp.openo.caisi_integrator.dao.CachedDemographicHL7LabResult")), CachedDemographicHL7LabResult.pcFieldNames, CachedDemographicHL7LabResult.pcFieldTypes, CachedDemographicHL7LabResult.pcFieldFlags, CachedDemographicHL7LabResult.pcPCSuperclass, "CachedDemographicHL7LabResult", (PersistenceCapable)new CachedDemographicHL7LabResult());
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
        this.caisiDemographicId = 0;
        this.data = null;
        this.facilityIdLabResultCompositePk = null;
        this.type = null;
    }
    
    public PersistenceCapable pcNewInstance(final StateManager pcStateManager, final Object o, final boolean b) {
        final CachedDemographicHL7LabResult cachedDemographicHL7LabResult = new CachedDemographicHL7LabResult();
        if (b) {
            cachedDemographicHL7LabResult.pcClearFields();
        }
        cachedDemographicHL7LabResult.pcStateManager = pcStateManager;
        cachedDemographicHL7LabResult.pcCopyKeyFieldsFromObjectId(o);
        return (PersistenceCapable)cachedDemographicHL7LabResult;
    }
    
    public PersistenceCapable pcNewInstance(final StateManager pcStateManager, final boolean b) {
        final CachedDemographicHL7LabResult cachedDemographicHL7LabResult = new CachedDemographicHL7LabResult();
        if (b) {
            cachedDemographicHL7LabResult.pcClearFields();
        }
        cachedDemographicHL7LabResult.pcStateManager = pcStateManager;
        return (PersistenceCapable)cachedDemographicHL7LabResult;
    }
    
    protected static int pcGetManagedFieldCount() {
        return 4;
    }
    
    public void pcReplaceField(final int n) {
        final int n2 = n - CachedDemographicHL7LabResult.pcInheritedFieldCount;
        if (n2 < 0) {
            throw new IllegalArgumentException();
        }
        switch (n2) {
            case 0: {
                this.caisiDemographicId = this.pcStateManager.replaceIntField((PersistenceCapable)this, n);
                return;
            }
            case 1: {
                this.data = this.pcStateManager.replaceStringField((PersistenceCapable)this, n);
                return;
            }
            case 2: {
                this.facilityIdLabResultCompositePk = (FacilityIdLabResultCompositePk)this.pcStateManager.replaceObjectField((PersistenceCapable)this, n);
                return;
            }
            case 3: {
                this.type = this.pcStateManager.replaceStringField((PersistenceCapable)this, n);
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
        final int n2 = n - CachedDemographicHL7LabResult.pcInheritedFieldCount;
        if (n2 < 0) {
            throw new IllegalArgumentException();
        }
        switch (n2) {
            case 0: {
                this.pcStateManager.providedIntField((PersistenceCapable)this, n, this.caisiDemographicId);
                return;
            }
            case 1: {
                this.pcStateManager.providedStringField((PersistenceCapable)this, n, this.data);
                return;
            }
            case 2: {
                this.pcStateManager.providedObjectField((PersistenceCapable)this, n, (Object)this.facilityIdLabResultCompositePk);
                return;
            }
            case 3: {
                this.pcStateManager.providedStringField((PersistenceCapable)this, n, this.type);
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
    
    protected void pcCopyField(final CachedDemographicHL7LabResult cachedDemographicHL7LabResult, final int n) {
        final int n2 = n - CachedDemographicHL7LabResult.pcInheritedFieldCount;
        if (n2 < 0) {
            throw new IllegalArgumentException();
        }
        switch (n2) {
            case 0: {
                this.caisiDemographicId = cachedDemographicHL7LabResult.caisiDemographicId;
                return;
            }
            case 1: {
                this.data = cachedDemographicHL7LabResult.data;
                return;
            }
            case 2: {
                this.facilityIdLabResultCompositePk = cachedDemographicHL7LabResult.facilityIdLabResultCompositePk;
                return;
            }
            case 3: {
                this.type = cachedDemographicHL7LabResult.type;
                return;
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
    }
    
    public void pcCopyFields(final Object o, final int[] array) {
        final CachedDemographicHL7LabResult cachedDemographicHL7LabResult = (CachedDemographicHL7LabResult)o;
        if (cachedDemographicHL7LabResult.pcStateManager != this.pcStateManager) {
            throw new IllegalArgumentException();
        }
        if (this.pcStateManager == null) {
            throw new IllegalStateException();
        }
        for (int i = 0; i < array.length; ++i) {
            this.pcCopyField(cachedDemographicHL7LabResult, array[i]);
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
        fieldConsumer.storeObjectField(2 + CachedDemographicHL7LabResult.pcInheritedFieldCount, ((ObjectId)o).getId());
    }
    
    public void pcCopyKeyFieldsFromObjectId(final Object o) {
        this.facilityIdLabResultCompositePk = (FacilityIdLabResultCompositePk)((ObjectId)o).getId();
    }
    
    public Object pcNewObjectIdInstance(final Object o) {
        throw new IllegalArgumentException("The id type \"class org.apache.openjpa.util.ObjectId\" specified by persistent type \"class ca.openosp.openo.caisi_integrator.dao.CachedDemographicHL7LabResult\" does not have a public class org.apache.openjpa.util.ObjectId(String) or class org.apache.openjpa.util.ObjectId(Class, String) constructor.");
    }
    
    public Object pcNewObjectIdInstance() {
        return new ObjectId((CachedDemographicHL7LabResult.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicHL7LabResult != null) ? CachedDemographicHL7LabResult.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicHL7LabResult : (CachedDemographicHL7LabResult.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicHL7LabResult = class$("ca.openosp.openo.caisi_integrator.dao.CachedDemographicHL7LabResult")), (Object)this.facilityIdLabResultCompositePk);
    }
    
    private static final int pcGetcaisiDemographicId(final CachedDemographicHL7LabResult cachedDemographicHL7LabResult) {
        if (cachedDemographicHL7LabResult.pcStateManager == null) {
            return cachedDemographicHL7LabResult.caisiDemographicId;
        }
        cachedDemographicHL7LabResult.pcStateManager.accessingField(CachedDemographicHL7LabResult.pcInheritedFieldCount + 0);
        return cachedDemographicHL7LabResult.caisiDemographicId;
    }
    
    private static final void pcSetcaisiDemographicId(final CachedDemographicHL7LabResult cachedDemographicHL7LabResult, final int caisiDemographicId) {
        if (cachedDemographicHL7LabResult.pcStateManager == null) {
            cachedDemographicHL7LabResult.caisiDemographicId = caisiDemographicId;
            return;
        }
        cachedDemographicHL7LabResult.pcStateManager.settingIntField((PersistenceCapable)cachedDemographicHL7LabResult, CachedDemographicHL7LabResult.pcInheritedFieldCount + 0, cachedDemographicHL7LabResult.caisiDemographicId, caisiDemographicId, 0);
    }
    
    private static final String pcGetdata(final CachedDemographicHL7LabResult cachedDemographicHL7LabResult) {
        if (cachedDemographicHL7LabResult.pcStateManager == null) {
            return cachedDemographicHL7LabResult.data;
        }
        cachedDemographicHL7LabResult.pcStateManager.accessingField(CachedDemographicHL7LabResult.pcInheritedFieldCount + 1);
        return cachedDemographicHL7LabResult.data;
    }
    
    private static final void pcSetdata(final CachedDemographicHL7LabResult cachedDemographicHL7LabResult, final String data) {
        if (cachedDemographicHL7LabResult.pcStateManager == null) {
            cachedDemographicHL7LabResult.data = data;
            return;
        }
        cachedDemographicHL7LabResult.pcStateManager.settingStringField((PersistenceCapable)cachedDemographicHL7LabResult, CachedDemographicHL7LabResult.pcInheritedFieldCount + 1, cachedDemographicHL7LabResult.data, data, 0);
    }
    
    private static final FacilityIdLabResultCompositePk pcGetfacilityIdLabResultCompositePk(final CachedDemographicHL7LabResult cachedDemographicHL7LabResult) {
        if (cachedDemographicHL7LabResult.pcStateManager == null) {
            return cachedDemographicHL7LabResult.facilityIdLabResultCompositePk;
        }
        cachedDemographicHL7LabResult.pcStateManager.accessingField(CachedDemographicHL7LabResult.pcInheritedFieldCount + 2);
        return cachedDemographicHL7LabResult.facilityIdLabResultCompositePk;
    }
    
    private static final void pcSetfacilityIdLabResultCompositePk(final CachedDemographicHL7LabResult cachedDemographicHL7LabResult, final FacilityIdLabResultCompositePk facilityIdLabResultCompositePk) {
        if (cachedDemographicHL7LabResult.pcStateManager == null) {
            cachedDemographicHL7LabResult.facilityIdLabResultCompositePk = facilityIdLabResultCompositePk;
            return;
        }
        cachedDemographicHL7LabResult.pcStateManager.settingObjectField((PersistenceCapable)cachedDemographicHL7LabResult, CachedDemographicHL7LabResult.pcInheritedFieldCount + 2, (Object)cachedDemographicHL7LabResult.facilityIdLabResultCompositePk, (Object)facilityIdLabResultCompositePk, 0);
    }
    
    private static final String pcGettype(final CachedDemographicHL7LabResult cachedDemographicHL7LabResult) {
        if (cachedDemographicHL7LabResult.pcStateManager == null) {
            return cachedDemographicHL7LabResult.type;
        }
        cachedDemographicHL7LabResult.pcStateManager.accessingField(CachedDemographicHL7LabResult.pcInheritedFieldCount + 3);
        return cachedDemographicHL7LabResult.type;
    }
    
    private static final void pcSettype(final CachedDemographicHL7LabResult cachedDemographicHL7LabResult, final String type) {
        if (cachedDemographicHL7LabResult.pcStateManager == null) {
            cachedDemographicHL7LabResult.type = type;
            return;
        }
        cachedDemographicHL7LabResult.pcStateManager.settingStringField((PersistenceCapable)cachedDemographicHL7LabResult, CachedDemographicHL7LabResult.pcInheritedFieldCount + 3, cachedDemographicHL7LabResult.type, type, 0);
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
