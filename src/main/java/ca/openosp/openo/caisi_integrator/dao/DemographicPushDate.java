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
import java.util.GregorianCalendar;
import org.apache.openjpa.enhance.StateManager;
import org.apache.openjpa.persistence.jdbc.Index;
import javax.persistence.Column;
import javax.persistence.TemporalType;
import javax.persistence.Temporal;
import java.util.Calendar;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class DemographicPushDate extends AbstractModel<FacilityIdIntegerCompositePk> implements PersistenceCapable
{
    @EmbeddedId
    private FacilityIdIntegerCompositePk id;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @Index
    private Calendar lastPushDate;
    private static int pcInheritedFieldCount;
    private static String[] pcFieldNames;
    private static Class[] pcFieldTypes;
    private static byte[] pcFieldFlags;
    private static Class pcPCSuperclass;
    protected transient StateManager pcStateManager;
    static /* synthetic */ Class class$Lca$openosp$openo$caisi_integrator$dao$FacilityIdIntegerCompositePk;
    static /* synthetic */ Class class$Ljava$util$Calendar;
    static /* synthetic */ Class class$Lca$openosp$openo$caisi_integrator$dao$DemographicPushDate;
    private transient Object pcDetachedState;
    private static final long serialVersionUID;
    
    public DemographicPushDate() {
        this.id = null;
        this.lastPushDate = new GregorianCalendar();
    }
    
    @Override
    public FacilityIdIntegerCompositePk getId() {
        return pcGetid(this);
    }
    
    public Calendar getLastPushDate() {
        return pcGetlastPushDate(this);
    }
    
    public void setLastPushDate(final Calendar lastPushDate) {
        pcSetlastPushDate(this, lastPushDate);
    }
    
    public void setId(final FacilityIdIntegerCompositePk id) {
        pcSetid(this, id);
    }
    
    public int pcGetEnhancementContractVersion() {
        return 2;
    }
    
    static {
        serialVersionUID = 6913977537728838681L;
        DemographicPushDate.pcFieldNames = new String[] { "id", "lastPushDate" };
        DemographicPushDate.pcFieldTypes = new Class[] { (DemographicPushDate.class$Lca$openosp$openo$caisi_integrator$dao$FacilityIdIntegerCompositePk != null) ? DemographicPushDate.class$Lca$openosp$openo$caisi_integrator$dao$FacilityIdIntegerCompositePk : (DemographicPushDate.class$Lca$openosp$openo$caisi_integrator$dao$FacilityIdIntegerCompositePk = class$("ca.openosp.openo.caisi_integrator.dao.FacilityIdIntegerCompositePk")), (DemographicPushDate.class$Ljava$util$Calendar != null) ? DemographicPushDate.class$Ljava$util$Calendar : (DemographicPushDate.class$Ljava$util$Calendar = class$("java.util.Calendar")) };
        DemographicPushDate.pcFieldFlags = new byte[] { 26, 26 };
        PCRegistry.register((DemographicPushDate.class$Lca$openosp$openo$caisi_integrator$dao$DemographicPushDate != null) ? DemographicPushDate.class$Lca$openosp$openo$caisi_integrator$dao$DemographicPushDate : (DemographicPushDate.class$Lca$openosp$openo$caisi_integrator$dao$DemographicPushDate = class$("ca.openosp.openo.caisi_integrator.dao.DemographicPushDate")), DemographicPushDate.pcFieldNames, DemographicPushDate.pcFieldTypes, DemographicPushDate.pcFieldFlags, DemographicPushDate.pcPCSuperclass, "DemographicPushDate", (PersistenceCapable)new DemographicPushDate());
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
        this.id = null;
        this.lastPushDate = null;
    }
    
    public PersistenceCapable pcNewInstance(final StateManager pcStateManager, final Object o, final boolean b) {
        final DemographicPushDate demographicPushDate = new DemographicPushDate();
        if (b) {
            demographicPushDate.pcClearFields();
        }
        demographicPushDate.pcStateManager = pcStateManager;
        demographicPushDate.pcCopyKeyFieldsFromObjectId(o);
        return (PersistenceCapable)demographicPushDate;
    }
    
    public PersistenceCapable pcNewInstance(final StateManager pcStateManager, final boolean b) {
        final DemographicPushDate demographicPushDate = new DemographicPushDate();
        if (b) {
            demographicPushDate.pcClearFields();
        }
        demographicPushDate.pcStateManager = pcStateManager;
        return (PersistenceCapable)demographicPushDate;
    }
    
    protected static int pcGetManagedFieldCount() {
        return 2;
    }
    
    public void pcReplaceField(final int n) {
        final int n2 = n - DemographicPushDate.pcInheritedFieldCount;
        if (n2 < 0) {
            throw new IllegalArgumentException();
        }
        switch (n2) {
            case 0: {
                this.id = (FacilityIdIntegerCompositePk)this.pcStateManager.replaceObjectField((PersistenceCapable)this, n);
                return;
            }
            case 1: {
                this.lastPushDate = (Calendar)this.pcStateManager.replaceObjectField((PersistenceCapable)this, n);
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
        final int n2 = n - DemographicPushDate.pcInheritedFieldCount;
        if (n2 < 0) {
            throw new IllegalArgumentException();
        }
        switch (n2) {
            case 0: {
                this.pcStateManager.providedObjectField((PersistenceCapable)this, n, (Object)this.id);
                return;
            }
            case 1: {
                this.pcStateManager.providedObjectField((PersistenceCapable)this, n, (Object)this.lastPushDate);
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
    
    protected void pcCopyField(final DemographicPushDate demographicPushDate, final int n) {
        final int n2 = n - DemographicPushDate.pcInheritedFieldCount;
        if (n2 < 0) {
            throw new IllegalArgumentException();
        }
        switch (n2) {
            case 0: {
                this.id = demographicPushDate.id;
                return;
            }
            case 1: {
                this.lastPushDate = demographicPushDate.lastPushDate;
                return;
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
    }
    
    public void pcCopyFields(final Object o, final int[] array) {
        final DemographicPushDate demographicPushDate = (DemographicPushDate)o;
        if (demographicPushDate.pcStateManager != this.pcStateManager) {
            throw new IllegalArgumentException();
        }
        if (this.pcStateManager == null) {
            throw new IllegalStateException();
        }
        for (int i = 0; i < array.length; ++i) {
            this.pcCopyField(demographicPushDate, array[i]);
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
        fieldConsumer.storeObjectField(0 + DemographicPushDate.pcInheritedFieldCount, ((ObjectId)o).getId());
    }
    
    public void pcCopyKeyFieldsFromObjectId(final Object o) {
        this.id = (FacilityIdIntegerCompositePk)((ObjectId)o).getId();
    }
    
    public Object pcNewObjectIdInstance(final Object o) {
        throw new IllegalArgumentException("The id type \"class org.apache.openjpa.util.ObjectId\" specified by persistent type \"class ca.openosp.openo.caisi_integrator.dao.DemographicPushDate\" does not have a public class org.apache.openjpa.util.ObjectId(String) or class org.apache.openjpa.util.ObjectId(Class, String) constructor.");
    }
    
    public Object pcNewObjectIdInstance() {
        return new ObjectId((DemographicPushDate.class$Lca$openosp$openo$caisi_integrator$dao$DemographicPushDate != null) ? DemographicPushDate.class$Lca$openosp$openo$caisi_integrator$dao$DemographicPushDate : (DemographicPushDate.class$Lca$openosp$openo$caisi_integrator$dao$DemographicPushDate = class$("ca.openosp.openo.caisi_integrator.dao.DemographicPushDate")), (Object)this.id);
    }
    
    private static final FacilityIdIntegerCompositePk pcGetid(final DemographicPushDate demographicPushDate) {
        if (demographicPushDate.pcStateManager == null) {
            return demographicPushDate.id;
        }
        demographicPushDate.pcStateManager.accessingField(DemographicPushDate.pcInheritedFieldCount + 0);
        return demographicPushDate.id;
    }
    
    private static final void pcSetid(final DemographicPushDate demographicPushDate, final FacilityIdIntegerCompositePk id) {
        if (demographicPushDate.pcStateManager == null) {
            demographicPushDate.id = id;
            return;
        }
        demographicPushDate.pcStateManager.settingObjectField((PersistenceCapable)demographicPushDate, DemographicPushDate.pcInheritedFieldCount + 0, (Object)demographicPushDate.id, (Object)id, 0);
    }
    
    private static final Calendar pcGetlastPushDate(final DemographicPushDate demographicPushDate) {
        if (demographicPushDate.pcStateManager == null) {
            return demographicPushDate.lastPushDate;
        }
        demographicPushDate.pcStateManager.accessingField(DemographicPushDate.pcInheritedFieldCount + 1);
        return demographicPushDate.lastPushDate;
    }
    
    private static final void pcSetlastPushDate(final DemographicPushDate demographicPushDate, final Calendar lastPushDate) {
        if (demographicPushDate.pcStateManager == null) {
            demographicPushDate.lastPushDate = lastPushDate;
            return;
        }
        demographicPushDate.pcStateManager.settingObjectField((PersistenceCapable)demographicPushDate, DemographicPushDate.pcInheritedFieldCount + 1, (Object)demographicPushDate.lastPushDate, (Object)lastPushDate, 0);
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
