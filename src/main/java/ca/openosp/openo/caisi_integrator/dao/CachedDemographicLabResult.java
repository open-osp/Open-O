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
public class CachedDemographicLabResult extends AbstractModel<FacilityIdLabResultCompositePk> implements PersistenceCapable
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
    static /* synthetic */ Class class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicLabResult;
    private transient Object pcDetachedState;
    private static final long serialVersionUID;
    
    public CachedDemographicLabResult() {
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
        serialVersionUID = 3515657542883150113L;
        CachedDemographicLabResult.pcFieldNames = new String[] { "caisiDemographicId", "data", "facilityIdLabResultCompositePk", "type" };
        CachedDemographicLabResult.pcFieldTypes = new Class[] { Integer.TYPE, (CachedDemographicLabResult.class$Ljava$lang$String != null) ? CachedDemographicLabResult.class$Ljava$lang$String : (CachedDemographicLabResult.class$Ljava$lang$String = class$("java.lang.String")), (CachedDemographicLabResult.class$Lca$openosp$openo$caisi_integrator$dao$FacilityIdLabResultCompositePk != null) ? CachedDemographicLabResult.class$Lca$openosp$openo$caisi_integrator$dao$FacilityIdLabResultCompositePk : (CachedDemographicLabResult.class$Lca$openosp$openo$caisi_integrator$dao$FacilityIdLabResultCompositePk = class$("ca.openosp.openo.caisi_integrator.dao.FacilityIdLabResultCompositePk")), (CachedDemographicLabResult.class$Ljava$lang$String != null) ? CachedDemographicLabResult.class$Ljava$lang$String : (CachedDemographicLabResult.class$Ljava$lang$String = class$("java.lang.String")) };
        CachedDemographicLabResult.pcFieldFlags = new byte[] { 26, 26, 26, 26 };
        PCRegistry.register((CachedDemographicLabResult.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicLabResult != null) ? CachedDemographicLabResult.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicLabResult : (CachedDemographicLabResult.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicLabResult = class$("ca.openosp.openo.caisi_integrator.dao.CachedDemographicLabResult")), CachedDemographicLabResult.pcFieldNames, CachedDemographicLabResult.pcFieldTypes, CachedDemographicLabResult.pcFieldFlags, CachedDemographicLabResult.pcPCSuperclass, "CachedDemographicLabResult", (PersistenceCapable)new CachedDemographicLabResult());
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
        final CachedDemographicLabResult cachedDemographicLabResult = new CachedDemographicLabResult();
        if (b) {
            cachedDemographicLabResult.pcClearFields();
        }
        cachedDemographicLabResult.pcStateManager = pcStateManager;
        cachedDemographicLabResult.pcCopyKeyFieldsFromObjectId(o);
        return (PersistenceCapable)cachedDemographicLabResult;
    }
    
    public PersistenceCapable pcNewInstance(final StateManager pcStateManager, final boolean b) {
        final CachedDemographicLabResult cachedDemographicLabResult = new CachedDemographicLabResult();
        if (b) {
            cachedDemographicLabResult.pcClearFields();
        }
        cachedDemographicLabResult.pcStateManager = pcStateManager;
        return (PersistenceCapable)cachedDemographicLabResult;
    }
    
    protected static int pcGetManagedFieldCount() {
        return 4;
    }
    
    public void pcReplaceField(final int n) {
        final int n2 = n - CachedDemographicLabResult.pcInheritedFieldCount;
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
        final int n2 = n - CachedDemographicLabResult.pcInheritedFieldCount;
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
    
    protected void pcCopyField(final CachedDemographicLabResult cachedDemographicLabResult, final int n) {
        final int n2 = n - CachedDemographicLabResult.pcInheritedFieldCount;
        if (n2 < 0) {
            throw new IllegalArgumentException();
        }
        switch (n2) {
            case 0: {
                this.caisiDemographicId = cachedDemographicLabResult.caisiDemographicId;
                return;
            }
            case 1: {
                this.data = cachedDemographicLabResult.data;
                return;
            }
            case 2: {
                this.facilityIdLabResultCompositePk = cachedDemographicLabResult.facilityIdLabResultCompositePk;
                return;
            }
            case 3: {
                this.type = cachedDemographicLabResult.type;
                return;
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
    }
    
    public void pcCopyFields(final Object o, final int[] array) {
        final CachedDemographicLabResult cachedDemographicLabResult = (CachedDemographicLabResult)o;
        if (cachedDemographicLabResult.pcStateManager != this.pcStateManager) {
            throw new IllegalArgumentException();
        }
        if (this.pcStateManager == null) {
            throw new IllegalStateException();
        }
        for (int i = 0; i < array.length; ++i) {
            this.pcCopyField(cachedDemographicLabResult, array[i]);
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
        fieldConsumer.storeObjectField(2 + CachedDemographicLabResult.pcInheritedFieldCount, ((ObjectId)o).getId());
    }
    
    public void pcCopyKeyFieldsFromObjectId(final Object o) {
        this.facilityIdLabResultCompositePk = (FacilityIdLabResultCompositePk)((ObjectId)o).getId();
    }
    
    public Object pcNewObjectIdInstance(final Object o) {
        throw new IllegalArgumentException("The id type \"class org.apache.openjpa.util.ObjectId\" specified by persistent type \"class ca.openosp.openo.caisi_integrator.dao.CachedDemographicLabResult\" does not have a public class org.apache.openjpa.util.ObjectId(String) or class org.apache.openjpa.util.ObjectId(Class, String) constructor.");
    }
    
    public Object pcNewObjectIdInstance() {
        return new ObjectId((CachedDemographicLabResult.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicLabResult != null) ? CachedDemographicLabResult.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicLabResult : (CachedDemographicLabResult.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicLabResult = class$("ca.openosp.openo.caisi_integrator.dao.CachedDemographicLabResult")), (Object)this.facilityIdLabResultCompositePk);
    }
    
    private static final int pcGetcaisiDemographicId(final CachedDemographicLabResult cachedDemographicLabResult) {
        if (cachedDemographicLabResult.pcStateManager == null) {
            return cachedDemographicLabResult.caisiDemographicId;
        }
        cachedDemographicLabResult.pcStateManager.accessingField(CachedDemographicLabResult.pcInheritedFieldCount + 0);
        return cachedDemographicLabResult.caisiDemographicId;
    }
    
    private static final void pcSetcaisiDemographicId(final CachedDemographicLabResult cachedDemographicLabResult, final int caisiDemographicId) {
        if (cachedDemographicLabResult.pcStateManager == null) {
            cachedDemographicLabResult.caisiDemographicId = caisiDemographicId;
            return;
        }
        cachedDemographicLabResult.pcStateManager.settingIntField((PersistenceCapable)cachedDemographicLabResult, CachedDemographicLabResult.pcInheritedFieldCount + 0, cachedDemographicLabResult.caisiDemographicId, caisiDemographicId, 0);
    }
    
    private static final String pcGetdata(final CachedDemographicLabResult cachedDemographicLabResult) {
        if (cachedDemographicLabResult.pcStateManager == null) {
            return cachedDemographicLabResult.data;
        }
        cachedDemographicLabResult.pcStateManager.accessingField(CachedDemographicLabResult.pcInheritedFieldCount + 1);
        return cachedDemographicLabResult.data;
    }
    
    private static final void pcSetdata(final CachedDemographicLabResult cachedDemographicLabResult, final String data) {
        if (cachedDemographicLabResult.pcStateManager == null) {
            cachedDemographicLabResult.data = data;
            return;
        }
        cachedDemographicLabResult.pcStateManager.settingStringField((PersistenceCapable)cachedDemographicLabResult, CachedDemographicLabResult.pcInheritedFieldCount + 1, cachedDemographicLabResult.data, data, 0);
    }
    
    private static final FacilityIdLabResultCompositePk pcGetfacilityIdLabResultCompositePk(final CachedDemographicLabResult cachedDemographicLabResult) {
        if (cachedDemographicLabResult.pcStateManager == null) {
            return cachedDemographicLabResult.facilityIdLabResultCompositePk;
        }
        cachedDemographicLabResult.pcStateManager.accessingField(CachedDemographicLabResult.pcInheritedFieldCount + 2);
        return cachedDemographicLabResult.facilityIdLabResultCompositePk;
    }
    
    private static final void pcSetfacilityIdLabResultCompositePk(final CachedDemographicLabResult cachedDemographicLabResult, final FacilityIdLabResultCompositePk facilityIdLabResultCompositePk) {
        if (cachedDemographicLabResult.pcStateManager == null) {
            cachedDemographicLabResult.facilityIdLabResultCompositePk = facilityIdLabResultCompositePk;
            return;
        }
        cachedDemographicLabResult.pcStateManager.settingObjectField((PersistenceCapable)cachedDemographicLabResult, CachedDemographicLabResult.pcInheritedFieldCount + 2, (Object)cachedDemographicLabResult.facilityIdLabResultCompositePk, (Object)facilityIdLabResultCompositePk, 0);
    }
    
    private static final String pcGettype(final CachedDemographicLabResult cachedDemographicLabResult) {
        if (cachedDemographicLabResult.pcStateManager == null) {
            return cachedDemographicLabResult.type;
        }
        cachedDemographicLabResult.pcStateManager.accessingField(CachedDemographicLabResult.pcInheritedFieldCount + 3);
        return cachedDemographicLabResult.type;
    }
    
    private static final void pcSettype(final CachedDemographicLabResult cachedDemographicLabResult, final String type) {
        if (cachedDemographicLabResult.pcStateManager == null) {
            cachedDemographicLabResult.type = type;
            return;
        }
        cachedDemographicLabResult.pcStateManager.settingStringField((PersistenceCapable)cachedDemographicLabResult, CachedDemographicLabResult.pcInheritedFieldCount + 3, cachedDemographicLabResult.type, type, 0);
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
