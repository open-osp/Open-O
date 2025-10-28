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
public class CachedDemographicForm extends AbstractModel<FacilityIdIntegerCompositePk> implements PersistenceCapable
{
    @EmbeddedId
    private FacilityIdIntegerCompositePk facilityIdIntegerCompositePk;
    @Column(nullable = false, length = 16)
    private String caisiProviderId;
    @Column(nullable = false)
    @Index
    private Integer caisiDemographicId;
    @Temporal(TemporalType.DATE)
    private Date editDate;
    @Column(nullable = false, length = 128)
    private String formName;
    @Column(columnDefinition = "mediumblob")
    private String formData;
    private static int pcInheritedFieldCount;
    private static String[] pcFieldNames;
    private static Class[] pcFieldTypes;
    private static byte[] pcFieldFlags;
    private static Class pcPCSuperclass;
    protected transient StateManager pcStateManager;
    static /* synthetic */ Class class$Ljava$lang$Integer;
    static /* synthetic */ Class class$Ljava$lang$String;
    static /* synthetic */ Class class$Ljava$util$Date;
    static /* synthetic */ Class class$Lca$openosp$openo$caisi_integrator$dao$FacilityIdIntegerCompositePk;
    static /* synthetic */ Class class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicForm;
    private transient Object pcDetachedState;
    private static final long serialVersionUID;
    
    public CachedDemographicForm() {
        this.caisiProviderId = null;
        this.caisiDemographicId = null;
        this.editDate = null;
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
    
    public Date getEditDate() {
        return pcGeteditDate(this);
    }
    
    public void setEditDate(final Date editDate) {
        pcSeteditDate(this, editDate);
    }
    
    public String getFormData() {
        return pcGetformData(this);
    }
    
    public void setFormData(final String formData) {
        pcSetformData(this, formData);
    }
    
    public String getFormName() {
        return pcGetformName(this);
    }
    
    public void setFormName(final String formName) {
        pcSetformName(this, formName);
    }
    
    public int pcGetEnhancementContractVersion() {
        return 2;
    }
    
    static {
        serialVersionUID = 5367902933529831098L;
        CachedDemographicForm.pcFieldNames = new String[] { "caisiDemographicId", "caisiProviderId", "editDate", "facilityIdIntegerCompositePk", "formData", "formName" };
        CachedDemographicForm.pcFieldTypes = new Class[] { (CachedDemographicForm.class$Ljava$lang$Integer != null) ? CachedDemographicForm.class$Ljava$lang$Integer : (CachedDemographicForm.class$Ljava$lang$Integer = class$("java.lang.Integer")), (CachedDemographicForm.class$Ljava$lang$String != null) ? CachedDemographicForm.class$Ljava$lang$String : (CachedDemographicForm.class$Ljava$lang$String = class$("java.lang.String")), (CachedDemographicForm.class$Ljava$util$Date != null) ? CachedDemographicForm.class$Ljava$util$Date : (CachedDemographicForm.class$Ljava$util$Date = class$("java.util.Date")), (CachedDemographicForm.class$Lca$openosp$openo$caisi_integrator$dao$FacilityIdIntegerCompositePk != null) ? CachedDemographicForm.class$Lca$openosp$openo$caisi_integrator$dao$FacilityIdIntegerCompositePk : (CachedDemographicForm.class$Lca$openosp$openo$caisi_integrator$dao$FacilityIdIntegerCompositePk = class$("ca.openosp.openo.caisi_integrator.dao.FacilityIdIntegerCompositePk")), (CachedDemographicForm.class$Ljava$lang$String != null) ? CachedDemographicForm.class$Ljava$lang$String : (CachedDemographicForm.class$Ljava$lang$String = class$("java.lang.String")), (CachedDemographicForm.class$Ljava$lang$String != null) ? CachedDemographicForm.class$Ljava$lang$String : (CachedDemographicForm.class$Ljava$lang$String = class$("java.lang.String")) };
        CachedDemographicForm.pcFieldFlags = new byte[] { 26, 26, 26, 26, 26, 26 };
        PCRegistry.register((CachedDemographicForm.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicForm != null) ? CachedDemographicForm.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicForm : (CachedDemographicForm.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicForm = class$("ca.openosp.openo.caisi_integrator.dao.CachedDemographicForm")), CachedDemographicForm.pcFieldNames, CachedDemographicForm.pcFieldTypes, CachedDemographicForm.pcFieldFlags, CachedDemographicForm.pcPCSuperclass, "CachedDemographicForm", (PersistenceCapable)new CachedDemographicForm());
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
        this.caisiDemographicId = null;
        this.caisiProviderId = null;
        this.editDate = null;
        this.facilityIdIntegerCompositePk = null;
        this.formData = null;
        this.formName = null;
    }
    
    public PersistenceCapable pcNewInstance(final StateManager pcStateManager, final Object o, final boolean b) {
        final CachedDemographicForm cachedDemographicForm = new CachedDemographicForm();
        if (b) {
            cachedDemographicForm.pcClearFields();
        }
        cachedDemographicForm.pcStateManager = pcStateManager;
        cachedDemographicForm.pcCopyKeyFieldsFromObjectId(o);
        return (PersistenceCapable)cachedDemographicForm;
    }
    
    public PersistenceCapable pcNewInstance(final StateManager pcStateManager, final boolean b) {
        final CachedDemographicForm cachedDemographicForm = new CachedDemographicForm();
        if (b) {
            cachedDemographicForm.pcClearFields();
        }
        cachedDemographicForm.pcStateManager = pcStateManager;
        return (PersistenceCapable)cachedDemographicForm;
    }
    
    protected static int pcGetManagedFieldCount() {
        return 6;
    }
    
    public void pcReplaceField(final int n) {
        final int n2 = n - CachedDemographicForm.pcInheritedFieldCount;
        if (n2 < 0) {
            throw new IllegalArgumentException();
        }
        switch (n2) {
            case 0: {
                this.caisiDemographicId = (Integer)this.pcStateManager.replaceObjectField((PersistenceCapable)this, n);
                return;
            }
            case 1: {
                this.caisiProviderId = this.pcStateManager.replaceStringField((PersistenceCapable)this, n);
                return;
            }
            case 2: {
                this.editDate = (Date)this.pcStateManager.replaceObjectField((PersistenceCapable)this, n);
                return;
            }
            case 3: {
                this.facilityIdIntegerCompositePk = (FacilityIdIntegerCompositePk)this.pcStateManager.replaceObjectField((PersistenceCapable)this, n);
                return;
            }
            case 4: {
                this.formData = this.pcStateManager.replaceStringField((PersistenceCapable)this, n);
                return;
            }
            case 5: {
                this.formName = this.pcStateManager.replaceStringField((PersistenceCapable)this, n);
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
        final int n2 = n - CachedDemographicForm.pcInheritedFieldCount;
        if (n2 < 0) {
            throw new IllegalArgumentException();
        }
        switch (n2) {
            case 0: {
                this.pcStateManager.providedObjectField((PersistenceCapable)this, n, (Object)this.caisiDemographicId);
                return;
            }
            case 1: {
                this.pcStateManager.providedStringField((PersistenceCapable)this, n, this.caisiProviderId);
                return;
            }
            case 2: {
                this.pcStateManager.providedObjectField((PersistenceCapable)this, n, (Object)this.editDate);
                return;
            }
            case 3: {
                this.pcStateManager.providedObjectField((PersistenceCapable)this, n, (Object)this.facilityIdIntegerCompositePk);
                return;
            }
            case 4: {
                this.pcStateManager.providedStringField((PersistenceCapable)this, n, this.formData);
                return;
            }
            case 5: {
                this.pcStateManager.providedStringField((PersistenceCapable)this, n, this.formName);
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
    
    protected void pcCopyField(final CachedDemographicForm cachedDemographicForm, final int n) {
        final int n2 = n - CachedDemographicForm.pcInheritedFieldCount;
        if (n2 < 0) {
            throw new IllegalArgumentException();
        }
        switch (n2) {
            case 0: {
                this.caisiDemographicId = cachedDemographicForm.caisiDemographicId;
                return;
            }
            case 1: {
                this.caisiProviderId = cachedDemographicForm.caisiProviderId;
                return;
            }
            case 2: {
                this.editDate = cachedDemographicForm.editDate;
                return;
            }
            case 3: {
                this.facilityIdIntegerCompositePk = cachedDemographicForm.facilityIdIntegerCompositePk;
                return;
            }
            case 4: {
                this.formData = cachedDemographicForm.formData;
                return;
            }
            case 5: {
                this.formName = cachedDemographicForm.formName;
                return;
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
    }
    
    public void pcCopyFields(final Object o, final int[] array) {
        final CachedDemographicForm cachedDemographicForm = (CachedDemographicForm)o;
        if (cachedDemographicForm.pcStateManager != this.pcStateManager) {
            throw new IllegalArgumentException();
        }
        if (this.pcStateManager == null) {
            throw new IllegalStateException();
        }
        for (int i = 0; i < array.length; ++i) {
            this.pcCopyField(cachedDemographicForm, array[i]);
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
        fieldConsumer.storeObjectField(3 + CachedDemographicForm.pcInheritedFieldCount, ((ObjectId)o).getId());
    }
    
    public void pcCopyKeyFieldsFromObjectId(final Object o) {
        this.facilityIdIntegerCompositePk = (FacilityIdIntegerCompositePk)((ObjectId)o).getId();
    }
    
    public Object pcNewObjectIdInstance(final Object o) {
        throw new IllegalArgumentException("The id type \"class org.apache.openjpa.util.ObjectId\" specified by persistent type \"class ca.openosp.openo.caisi_integrator.dao.CachedDemographicForm\" does not have a public class org.apache.openjpa.util.ObjectId(String) or class org.apache.openjpa.util.ObjectId(Class, String) constructor.");
    }
    
    public Object pcNewObjectIdInstance() {
        return new ObjectId((CachedDemographicForm.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicForm != null) ? CachedDemographicForm.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicForm : (CachedDemographicForm.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicForm = class$("ca.openosp.openo.caisi_integrator.dao.CachedDemographicForm")), (Object)this.facilityIdIntegerCompositePk);
    }
    
    private static final Integer pcGetcaisiDemographicId(final CachedDemographicForm cachedDemographicForm) {
        if (cachedDemographicForm.pcStateManager == null) {
            return cachedDemographicForm.caisiDemographicId;
        }
        cachedDemographicForm.pcStateManager.accessingField(CachedDemographicForm.pcInheritedFieldCount + 0);
        return cachedDemographicForm.caisiDemographicId;
    }
    
    private static final void pcSetcaisiDemographicId(final CachedDemographicForm cachedDemographicForm, final Integer caisiDemographicId) {
        if (cachedDemographicForm.pcStateManager == null) {
            cachedDemographicForm.caisiDemographicId = caisiDemographicId;
            return;
        }
        cachedDemographicForm.pcStateManager.settingObjectField((PersistenceCapable)cachedDemographicForm, CachedDemographicForm.pcInheritedFieldCount + 0, (Object)cachedDemographicForm.caisiDemographicId, (Object)caisiDemographicId, 0);
    }
    
    private static final String pcGetcaisiProviderId(final CachedDemographicForm cachedDemographicForm) {
        if (cachedDemographicForm.pcStateManager == null) {
            return cachedDemographicForm.caisiProviderId;
        }
        cachedDemographicForm.pcStateManager.accessingField(CachedDemographicForm.pcInheritedFieldCount + 1);
        return cachedDemographicForm.caisiProviderId;
    }
    
    private static final void pcSetcaisiProviderId(final CachedDemographicForm cachedDemographicForm, final String caisiProviderId) {
        if (cachedDemographicForm.pcStateManager == null) {
            cachedDemographicForm.caisiProviderId = caisiProviderId;
            return;
        }
        cachedDemographicForm.pcStateManager.settingStringField((PersistenceCapable)cachedDemographicForm, CachedDemographicForm.pcInheritedFieldCount + 1, cachedDemographicForm.caisiProviderId, caisiProviderId, 0);
    }
    
    private static final Date pcGeteditDate(final CachedDemographicForm cachedDemographicForm) {
        if (cachedDemographicForm.pcStateManager == null) {
            return cachedDemographicForm.editDate;
        }
        cachedDemographicForm.pcStateManager.accessingField(CachedDemographicForm.pcInheritedFieldCount + 2);
        return cachedDemographicForm.editDate;
    }
    
    private static final void pcSeteditDate(final CachedDemographicForm cachedDemographicForm, final Date editDate) {
        if (cachedDemographicForm.pcStateManager == null) {
            cachedDemographicForm.editDate = editDate;
            return;
        }
        cachedDemographicForm.pcStateManager.settingObjectField((PersistenceCapable)cachedDemographicForm, CachedDemographicForm.pcInheritedFieldCount + 2, (Object)cachedDemographicForm.editDate, (Object)editDate, 0);
    }
    
    private static final FacilityIdIntegerCompositePk pcGetfacilityIdIntegerCompositePk(final CachedDemographicForm cachedDemographicForm) {
        if (cachedDemographicForm.pcStateManager == null) {
            return cachedDemographicForm.facilityIdIntegerCompositePk;
        }
        cachedDemographicForm.pcStateManager.accessingField(CachedDemographicForm.pcInheritedFieldCount + 3);
        return cachedDemographicForm.facilityIdIntegerCompositePk;
    }
    
    private static final void pcSetfacilityIdIntegerCompositePk(final CachedDemographicForm cachedDemographicForm, final FacilityIdIntegerCompositePk facilityIdIntegerCompositePk) {
        if (cachedDemographicForm.pcStateManager == null) {
            cachedDemographicForm.facilityIdIntegerCompositePk = facilityIdIntegerCompositePk;
            return;
        }
        cachedDemographicForm.pcStateManager.settingObjectField((PersistenceCapable)cachedDemographicForm, CachedDemographicForm.pcInheritedFieldCount + 3, (Object)cachedDemographicForm.facilityIdIntegerCompositePk, (Object)facilityIdIntegerCompositePk, 0);
    }
    
    private static final String pcGetformData(final CachedDemographicForm cachedDemographicForm) {
        if (cachedDemographicForm.pcStateManager == null) {
            return cachedDemographicForm.formData;
        }
        cachedDemographicForm.pcStateManager.accessingField(CachedDemographicForm.pcInheritedFieldCount + 4);
        return cachedDemographicForm.formData;
    }
    
    private static final void pcSetformData(final CachedDemographicForm cachedDemographicForm, final String formData) {
        if (cachedDemographicForm.pcStateManager == null) {
            cachedDemographicForm.formData = formData;
            return;
        }
        cachedDemographicForm.pcStateManager.settingStringField((PersistenceCapable)cachedDemographicForm, CachedDemographicForm.pcInheritedFieldCount + 4, cachedDemographicForm.formData, formData, 0);
    }
    
    private static final String pcGetformName(final CachedDemographicForm cachedDemographicForm) {
        if (cachedDemographicForm.pcStateManager == null) {
            return cachedDemographicForm.formName;
        }
        cachedDemographicForm.pcStateManager.accessingField(CachedDemographicForm.pcInheritedFieldCount + 5);
        return cachedDemographicForm.formName;
    }
    
    private static final void pcSetformName(final CachedDemographicForm cachedDemographicForm, final String formName) {
        if (cachedDemographicForm.pcStateManager == null) {
            cachedDemographicForm.formName = formName;
            return;
        }
        cachedDemographicForm.pcStateManager.settingStringField((PersistenceCapable)cachedDemographicForm, CachedDemographicForm.pcInheritedFieldCount + 5, cachedDemographicForm.formName, formName, 0);
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
