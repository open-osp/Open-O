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
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class CachedDemographicDocumentContents extends AbstractModel<FacilityIdIntegerCompositePk> implements PersistenceCapable
{
    @EmbeddedId
    private FacilityIdIntegerCompositePk facilityIntegerCompositePk;
    @Column(columnDefinition = "longblob")
    private byte[] fileContents;
    private static int pcInheritedFieldCount;
    private static String[] pcFieldNames;
    private static Class[] pcFieldTypes;
    private static byte[] pcFieldFlags;
    private static Class pcPCSuperclass;
    protected transient StateManager pcStateManager;
    static /* synthetic */ Class class$Lca$openosp$openo$caisi_integrator$dao$FacilityIdIntegerCompositePk;
    static /* synthetic */ Class class$L$B;
    static /* synthetic */ Class class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicDocumentContents;
    private transient Object pcDetachedState;
    private static final long serialVersionUID;
    
    @Override
    public FacilityIdIntegerCompositePk getId() {
        return pcGetfacilityIntegerCompositePk(this);
    }
    
    public FacilityIdIntegerCompositePk getFacilityIntegerCompositePk() {
        return pcGetfacilityIntegerCompositePk(this);
    }
    
    public void setFacilityIntegerCompositePk(final FacilityIdIntegerCompositePk facilityIntegerCompositePk) {
        pcSetfacilityIntegerCompositePk(this, facilityIntegerCompositePk);
    }
    
    public byte[] getFileContents() {
        return pcGetfileContents(this);
    }
    
    public void setFileContents(final byte[] fileContents) {
        pcSetfileContents(this, fileContents);
    }
    
    public int pcGetEnhancementContractVersion() {
        return 2;
    }
    
    static {
        serialVersionUID = 2801972328978058955L;
        CachedDemographicDocumentContents.pcFieldNames = new String[] { "facilityIntegerCompositePk", "fileContents" };
        CachedDemographicDocumentContents.pcFieldTypes = new Class[] { (CachedDemographicDocumentContents.class$Lca$openosp$openo$caisi_integrator$dao$FacilityIdIntegerCompositePk != null) ? CachedDemographicDocumentContents.class$Lca$openosp$openo$caisi_integrator$dao$FacilityIdIntegerCompositePk : (CachedDemographicDocumentContents.class$Lca$openosp$openo$caisi_integrator$dao$FacilityIdIntegerCompositePk = class$("ca.openosp.openo.caisi_integrator.dao.FacilityIdIntegerCompositePk")), (CachedDemographicDocumentContents.class$L$B != null) ? CachedDemographicDocumentContents.class$L$B : (CachedDemographicDocumentContents.class$L$B = class$("[B")) };
        CachedDemographicDocumentContents.pcFieldFlags = new byte[] { 26, 26 };
        PCRegistry.register((CachedDemographicDocumentContents.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicDocumentContents != null) ? CachedDemographicDocumentContents.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicDocumentContents : (CachedDemographicDocumentContents.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicDocumentContents = class$("ca.openosp.openo.caisi_integrator.dao.CachedDemographicDocumentContents")), CachedDemographicDocumentContents.pcFieldNames, CachedDemographicDocumentContents.pcFieldTypes, CachedDemographicDocumentContents.pcFieldFlags, CachedDemographicDocumentContents.pcPCSuperclass, "CachedDemographicDocumentContents", (PersistenceCapable)new CachedDemographicDocumentContents());
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
        this.facilityIntegerCompositePk = null;
        this.fileContents = null;
    }
    
    public PersistenceCapable pcNewInstance(final StateManager pcStateManager, final Object o, final boolean b) {
        final CachedDemographicDocumentContents cachedDemographicDocumentContents = new CachedDemographicDocumentContents();
        if (b) {
            cachedDemographicDocumentContents.pcClearFields();
        }
        cachedDemographicDocumentContents.pcStateManager = pcStateManager;
        cachedDemographicDocumentContents.pcCopyKeyFieldsFromObjectId(o);
        return (PersistenceCapable)cachedDemographicDocumentContents;
    }
    
    public PersistenceCapable pcNewInstance(final StateManager pcStateManager, final boolean b) {
        final CachedDemographicDocumentContents cachedDemographicDocumentContents = new CachedDemographicDocumentContents();
        if (b) {
            cachedDemographicDocumentContents.pcClearFields();
        }
        cachedDemographicDocumentContents.pcStateManager = pcStateManager;
        return (PersistenceCapable)cachedDemographicDocumentContents;
    }
    
    protected static int pcGetManagedFieldCount() {
        return 2;
    }
    
    public void pcReplaceField(final int n) {
        final int n2 = n - CachedDemographicDocumentContents.pcInheritedFieldCount;
        if (n2 < 0) {
            throw new IllegalArgumentException();
        }
        switch (n2) {
            case 0: {
                this.facilityIntegerCompositePk = (FacilityIdIntegerCompositePk)this.pcStateManager.replaceObjectField((PersistenceCapable)this, n);
                return;
            }
            case 1: {
                this.fileContents = (byte[])this.pcStateManager.replaceObjectField((PersistenceCapable)this, n);
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
        final int n2 = n - CachedDemographicDocumentContents.pcInheritedFieldCount;
        if (n2 < 0) {
            throw new IllegalArgumentException();
        }
        switch (n2) {
            case 0: {
                this.pcStateManager.providedObjectField((PersistenceCapable)this, n, (Object)this.facilityIntegerCompositePk);
                return;
            }
            case 1: {
                this.pcStateManager.providedObjectField((PersistenceCapable)this, n, (Object)this.fileContents);
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
    
    protected void pcCopyField(final CachedDemographicDocumentContents cachedDemographicDocumentContents, final int n) {
        final int n2 = n - CachedDemographicDocumentContents.pcInheritedFieldCount;
        if (n2 < 0) {
            throw new IllegalArgumentException();
        }
        switch (n2) {
            case 0: {
                this.facilityIntegerCompositePk = cachedDemographicDocumentContents.facilityIntegerCompositePk;
                return;
            }
            case 1: {
                this.fileContents = cachedDemographicDocumentContents.fileContents;
                return;
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
    }
    
    public void pcCopyFields(final Object o, final int[] array) {
        final CachedDemographicDocumentContents cachedDemographicDocumentContents = (CachedDemographicDocumentContents)o;
        if (cachedDemographicDocumentContents.pcStateManager != this.pcStateManager) {
            throw new IllegalArgumentException();
        }
        if (this.pcStateManager == null) {
            throw new IllegalStateException();
        }
        for (int i = 0; i < array.length; ++i) {
            this.pcCopyField(cachedDemographicDocumentContents, array[i]);
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
        fieldConsumer.storeObjectField(0 + CachedDemographicDocumentContents.pcInheritedFieldCount, ((ObjectId)o).getId());
    }
    
    public void pcCopyKeyFieldsFromObjectId(final Object o) {
        this.facilityIntegerCompositePk = (FacilityIdIntegerCompositePk)((ObjectId)o).getId();
    }
    
    public Object pcNewObjectIdInstance(final Object o) {
        throw new IllegalArgumentException("The id type \"class org.apache.openjpa.util.ObjectId\" specified by persistent type \"class ca.openosp.openo.caisi_integrator.dao.CachedDemographicDocumentContents\" does not have a public class org.apache.openjpa.util.ObjectId(String) or class org.apache.openjpa.util.ObjectId(Class, String) constructor.");
    }
    
    public Object pcNewObjectIdInstance() {
        return new ObjectId((CachedDemographicDocumentContents.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicDocumentContents != null) ? CachedDemographicDocumentContents.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicDocumentContents : (CachedDemographicDocumentContents.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicDocumentContents = class$("ca.openosp.openo.caisi_integrator.dao.CachedDemographicDocumentContents")), (Object)this.facilityIntegerCompositePk);
    }
    
    private static final FacilityIdIntegerCompositePk pcGetfacilityIntegerCompositePk(final CachedDemographicDocumentContents cachedDemographicDocumentContents) {
        if (cachedDemographicDocumentContents.pcStateManager == null) {
            return cachedDemographicDocumentContents.facilityIntegerCompositePk;
        }
        cachedDemographicDocumentContents.pcStateManager.accessingField(CachedDemographicDocumentContents.pcInheritedFieldCount + 0);
        return cachedDemographicDocumentContents.facilityIntegerCompositePk;
    }
    
    private static final void pcSetfacilityIntegerCompositePk(final CachedDemographicDocumentContents cachedDemographicDocumentContents, final FacilityIdIntegerCompositePk facilityIntegerCompositePk) {
        if (cachedDemographicDocumentContents.pcStateManager == null) {
            cachedDemographicDocumentContents.facilityIntegerCompositePk = facilityIntegerCompositePk;
            return;
        }
        cachedDemographicDocumentContents.pcStateManager.settingObjectField((PersistenceCapable)cachedDemographicDocumentContents, CachedDemographicDocumentContents.pcInheritedFieldCount + 0, (Object)cachedDemographicDocumentContents.facilityIntegerCompositePk, (Object)facilityIntegerCompositePk, 0);
    }
    
    private static final byte[] pcGetfileContents(final CachedDemographicDocumentContents cachedDemographicDocumentContents) {
        if (cachedDemographicDocumentContents.pcStateManager == null) {
            return cachedDemographicDocumentContents.fileContents;
        }
        cachedDemographicDocumentContents.pcStateManager.accessingField(CachedDemographicDocumentContents.pcInheritedFieldCount + 1);
        return cachedDemographicDocumentContents.fileContents;
    }
    
    private static final void pcSetfileContents(final CachedDemographicDocumentContents cachedDemographicDocumentContents, final byte[] fileContents) {
        if (cachedDemographicDocumentContents.pcStateManager == null) {
            cachedDemographicDocumentContents.fileContents = fileContents;
            return;
        }
        cachedDemographicDocumentContents.pcStateManager.settingObjectField((PersistenceCapable)cachedDemographicDocumentContents, CachedDemographicDocumentContents.pcInheritedFieldCount + 1, (Object)cachedDemographicDocumentContents.fileContents, (Object)fileContents, 0);
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
