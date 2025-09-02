package ca.openosp.openo.caisi_integrator.dao;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import org.apache.openjpa.enhance.FieldConsumer;
import org.apache.openjpa.enhance.FieldSupplier;
import org.apache.openjpa.enhance.RedefinitionHelper;
import org.apache.openjpa.enhance.PersistenceCapable;
import org.apache.openjpa.enhance.PCRegistry;
import org.apache.openjpa.enhance.StateManager;
import org.apache.openjpa.persistence.jdbc.Index;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class FacilityIdIntegerCompositePk implements Serializable, Comparable<FacilityIdIntegerCompositePk>, PersistenceCapable
{
    @Index
    private Integer integratorFacilityId;
    @Index
    private Integer caisiItemId;
    private static int pcInheritedFieldCount;
    private static String[] pcFieldNames;
    private static Class[] pcFieldTypes;
    private static byte[] pcFieldFlags;
    private static Class pcPCSuperclass;
    protected transient StateManager pcStateManager;
    static /* synthetic */ Class class$Ljava$lang$Integer;
    static /* synthetic */ Class class$Lca$openosp$openo$caisi_integrator$dao$FacilityIdIntegerCompositePk;
    private transient Object pcDetachedState;
    private static final long serialVersionUID;
    
    public FacilityIdIntegerCompositePk() {
        this.integratorFacilityId = null;
        this.caisiItemId = null;
    }
    
    public FacilityIdIntegerCompositePk(final Integer integratorFacilityId, final Integer caisiItemId) {
        this.integratorFacilityId = null;
        this.caisiItemId = null;
        this.integratorFacilityId = integratorFacilityId;
        this.caisiItemId = caisiItemId;
    }
    
    public Integer getIntegratorFacilityId() {
        return pcGetintegratorFacilityId(this);
    }
    
    public void setIntegratorFacilityId(final Integer integratorFacilityId) {
        pcSetintegratorFacilityId(this, integratorFacilityId);
    }
    
    public Integer getCaisiItemId() {
        return pcGetcaisiItemId(this);
    }
    
    public void setCaisiItemId(final Integer caisiItemId) {
        pcSetcaisiItemId(this, caisiItemId);
    }
    
    @Override
    public String toString() {
        return String.valueOf(pcGetintegratorFacilityId(this)) + ':' + pcGetcaisiItemId(this);
    }
    
    @Override
    public int hashCode() {
        return pcGetcaisiItemId(this);
    }
    
    @Override
    public boolean equals(final Object o) {
        try {
            final FacilityIdIntegerCompositePk o2 = (FacilityIdIntegerCompositePk)o;
            return pcGetintegratorFacilityId(this).equals(pcGetintegratorFacilityId(o2)) && pcGetcaisiItemId(this).equals(pcGetcaisiItemId(o2));
        }
        catch (final RuntimeException e) {
            return false;
        }
    }
    
    @Override
    public int compareTo(final FacilityIdIntegerCompositePk o) {
        return this.toString().compareTo(o.toString());
    }
    
    public int pcGetEnhancementContractVersion() {
        return 2;
    }
    
    static {
        serialVersionUID = 6684273625189092823L;
        FacilityIdIntegerCompositePk.pcFieldNames = new String[] { "caisiItemId", "integratorFacilityId" };
        FacilityIdIntegerCompositePk.pcFieldTypes = new Class[] { (FacilityIdIntegerCompositePk.class$Ljava$lang$Integer != null) ? FacilityIdIntegerCompositePk.class$Ljava$lang$Integer : (FacilityIdIntegerCompositePk.class$Ljava$lang$Integer = class$("java.lang.Integer")), (FacilityIdIntegerCompositePk.class$Ljava$lang$Integer != null) ? FacilityIdIntegerCompositePk.class$Ljava$lang$Integer : (FacilityIdIntegerCompositePk.class$Ljava$lang$Integer = class$("java.lang.Integer")) };
        FacilityIdIntegerCompositePk.pcFieldFlags = new byte[] { 26, 26 };
        PCRegistry.register((FacilityIdIntegerCompositePk.class$Lca$openosp$openo$caisi_integrator$dao$FacilityIdIntegerCompositePk != null) ? FacilityIdIntegerCompositePk.class$Lca$openosp$openo$caisi_integrator$dao$FacilityIdIntegerCompositePk : (FacilityIdIntegerCompositePk.class$Lca$openosp$openo$caisi_integrator$dao$FacilityIdIntegerCompositePk = class$("ca.openosp.openo.caisi_integrator.dao.FacilityIdIntegerCompositePk")), FacilityIdIntegerCompositePk.pcFieldNames, FacilityIdIntegerCompositePk.pcFieldTypes, FacilityIdIntegerCompositePk.pcFieldFlags, FacilityIdIntegerCompositePk.pcPCSuperclass, (String)null, (PersistenceCapable)new FacilityIdIntegerCompositePk());
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
        this.caisiItemId = null;
        this.integratorFacilityId = null;
    }
    
    public PersistenceCapable pcNewInstance(final StateManager pcStateManager, final Object o, final boolean b) {
        final FacilityIdIntegerCompositePk facilityIdIntegerCompositePk = new FacilityIdIntegerCompositePk();
        if (b) {
            facilityIdIntegerCompositePk.pcClearFields();
        }
        facilityIdIntegerCompositePk.pcStateManager = pcStateManager;
        facilityIdIntegerCompositePk.pcCopyKeyFieldsFromObjectId(o);
        return (PersistenceCapable)facilityIdIntegerCompositePk;
    }
    
    public PersistenceCapable pcNewInstance(final StateManager pcStateManager, final boolean b) {
        final FacilityIdIntegerCompositePk facilityIdIntegerCompositePk = new FacilityIdIntegerCompositePk();
        if (b) {
            facilityIdIntegerCompositePk.pcClearFields();
        }
        facilityIdIntegerCompositePk.pcStateManager = pcStateManager;
        return (PersistenceCapable)facilityIdIntegerCompositePk;
    }
    
    protected static int pcGetManagedFieldCount() {
        return 2;
    }
    
    public void pcReplaceField(final int n) {
        final int n2 = n - FacilityIdIntegerCompositePk.pcInheritedFieldCount;
        if (n2 < 0) {
            throw new IllegalArgumentException();
        }
        switch (n2) {
            case 0: {
                this.caisiItemId = (Integer)this.pcStateManager.replaceObjectField((PersistenceCapable)this, n);
                return;
            }
            case 1: {
                this.integratorFacilityId = (Integer)this.pcStateManager.replaceObjectField((PersistenceCapable)this, n);
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
        final int n2 = n - FacilityIdIntegerCompositePk.pcInheritedFieldCount;
        if (n2 < 0) {
            throw new IllegalArgumentException();
        }
        switch (n2) {
            case 0: {
                this.pcStateManager.providedObjectField((PersistenceCapable)this, n, (Object)this.caisiItemId);
                return;
            }
            case 1: {
                this.pcStateManager.providedObjectField((PersistenceCapable)this, n, (Object)this.integratorFacilityId);
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
    
    protected void pcCopyField(final FacilityIdIntegerCompositePk facilityIdIntegerCompositePk, final int n) {
        final int n2 = n - FacilityIdIntegerCompositePk.pcInheritedFieldCount;
        if (n2 < 0) {
            throw new IllegalArgumentException();
        }
        switch (n2) {
            case 0: {
                this.caisiItemId = facilityIdIntegerCompositePk.caisiItemId;
                return;
            }
            case 1: {
                this.integratorFacilityId = facilityIdIntegerCompositePk.integratorFacilityId;
                return;
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
    }
    
    public void pcCopyFields(final Object o, final int[] array) {
        final FacilityIdIntegerCompositePk facilityIdIntegerCompositePk = (FacilityIdIntegerCompositePk)o;
        if (facilityIdIntegerCompositePk.pcStateManager != this.pcStateManager) {
            throw new IllegalArgumentException();
        }
        if (this.pcStateManager == null) {
            throw new IllegalStateException();
        }
        for (int i = 0; i < array.length; ++i) {
            this.pcCopyField(facilityIdIntegerCompositePk, array[i]);
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
    }
    
    public void pcCopyKeyFieldsToObjectId(final Object o) {
    }
    
    public void pcCopyKeyFieldsFromObjectId(final FieldConsumer fieldConsumer, final Object o) {
    }
    
    public void pcCopyKeyFieldsFromObjectId(final Object o) {
    }
    
    public Object pcNewObjectIdInstance() {
        return null;
    }
    
    public Object pcNewObjectIdInstance(final Object o) {
        return null;
    }
    
    private static final Integer pcGetcaisiItemId(final FacilityIdIntegerCompositePk facilityIdIntegerCompositePk) {
        if (facilityIdIntegerCompositePk.pcStateManager == null) {
            return facilityIdIntegerCompositePk.caisiItemId;
        }
        facilityIdIntegerCompositePk.pcStateManager.accessingField(FacilityIdIntegerCompositePk.pcInheritedFieldCount + 0);
        return facilityIdIntegerCompositePk.caisiItemId;
    }
    
    private static final void pcSetcaisiItemId(final FacilityIdIntegerCompositePk facilityIdIntegerCompositePk, final Integer caisiItemId) {
        if (facilityIdIntegerCompositePk.pcStateManager == null) {
            facilityIdIntegerCompositePk.caisiItemId = caisiItemId;
            return;
        }
        facilityIdIntegerCompositePk.pcStateManager.settingObjectField((PersistenceCapable)facilityIdIntegerCompositePk, FacilityIdIntegerCompositePk.pcInheritedFieldCount + 0, (Object)facilityIdIntegerCompositePk.caisiItemId, (Object)caisiItemId, 0);
    }
    
    private static final Integer pcGetintegratorFacilityId(final FacilityIdIntegerCompositePk facilityIdIntegerCompositePk) {
        if (facilityIdIntegerCompositePk.pcStateManager == null) {
            return facilityIdIntegerCompositePk.integratorFacilityId;
        }
        facilityIdIntegerCompositePk.pcStateManager.accessingField(FacilityIdIntegerCompositePk.pcInheritedFieldCount + 1);
        return facilityIdIntegerCompositePk.integratorFacilityId;
    }
    
    private static final void pcSetintegratorFacilityId(final FacilityIdIntegerCompositePk facilityIdIntegerCompositePk, final Integer integratorFacilityId) {
        if (facilityIdIntegerCompositePk.pcStateManager == null) {
            facilityIdIntegerCompositePk.integratorFacilityId = integratorFacilityId;
            return;
        }
        facilityIdIntegerCompositePk.pcStateManager.settingObjectField((PersistenceCapable)facilityIdIntegerCompositePk, FacilityIdIntegerCompositePk.pcInheritedFieldCount + 1, (Object)facilityIdIntegerCompositePk.integratorFacilityId, (Object)integratorFacilityId, 0);
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
