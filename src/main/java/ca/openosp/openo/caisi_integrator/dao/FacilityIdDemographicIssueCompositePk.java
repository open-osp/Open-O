package ca.openosp.openo.caisi_integrator.dao;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import org.apache.openjpa.enhance.FieldConsumer;
import org.apache.openjpa.enhance.FieldSupplier;
import org.apache.openjpa.enhance.RedefinitionHelper;
import org.apache.openjpa.enhance.PCRegistry;
import org.apache.openjpa.enhance.StateManager;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import ca.openosp.openo.caisi_integrator.util.CodeType;
import org.apache.openjpa.persistence.jdbc.Index;
import javax.persistence.Embeddable;
import org.apache.openjpa.enhance.PersistenceCapable;
import java.io.Serializable;

@Embeddable
public class FacilityIdDemographicIssueCompositePk implements Serializable, PersistenceCapable
{
    @Index
    private Integer integratorFacilityId;
    @Index
    private Integer caisiDemographicId;
    @Enumerated(EnumType.STRING)
    @Column(length = 64, nullable = false)
    private CodeType codeType;
    @Column(length = 64, nullable = false)
    private String issueCode;
    private static int pcInheritedFieldCount;
    private static String[] pcFieldNames;
    private static Class[] pcFieldTypes;
    private static byte[] pcFieldFlags;
    private static Class pcPCSuperclass;
    protected transient StateManager pcStateManager;
    static /* synthetic */ Class class$Ljava$lang$Integer;
    static /* synthetic */ Class class$Lca$openosp$openo$caisi_integrator$util$CodeType;
    static /* synthetic */ Class class$Ljava$lang$String;
    static /* synthetic */ Class class$Lca$openosp$openo$caisi_integrator$dao$FacilityIdDemographicIssueCompositePk;
    private transient Object pcDetachedState;
    private static final long serialVersionUID;
    
    public FacilityIdDemographicIssueCompositePk() {
        this.integratorFacilityId = null;
        this.caisiDemographicId = null;
        this.codeType = null;
        this.issueCode = null;
    }
    
    public Integer getIntegratorFacilityId() {
        return pcGetintegratorFacilityId(this);
    }
    
    public void setIntegratorFacilityId(final Integer integratorFacilityId) {
        pcSetintegratorFacilityId(this, integratorFacilityId);
    }
    
    public Integer getCaisiDemographicId() {
        return pcGetcaisiDemographicId(this);
    }
    
    public void setCaisiDemographicId(final Integer caisiDemographicId) {
        pcSetcaisiDemographicId(this, caisiDemographicId);
    }
    
    public String getIssueCode() {
        return pcGetissueCode(this);
    }
    
    public void setIssueCode(final String issueCode) {
        pcSetissueCode(this, issueCode);
    }
    
    public CodeType getCodeType() {
        return pcGetcodeType(this);
    }
    
    public void setCodeType(final CodeType codeType) {
        pcSetcodeType(this, codeType);
    }
    
    @Override
    public String toString() {
        return "" + pcGetintegratorFacilityId(this) + ':' + pcGetcaisiDemographicId(this) + ':' + pcGetcodeType(this) + ':' + pcGetissueCode(this);
    }
    
    @Override
    public int hashCode() {
        return pcGetcaisiDemographicId(this);
    }
    
    @Override
    public boolean equals(final Object o) {
        try {
            final FacilityIdDemographicIssueCompositePk o2 = (FacilityIdDemographicIssueCompositePk)o;
            return pcGetintegratorFacilityId(this).equals(pcGetintegratorFacilityId(o2)) && pcGetcaisiDemographicId(this).equals(pcGetcaisiDemographicId(o2)) && pcGetissueCode(this).equals(pcGetissueCode(o2)) && pcGetcodeType(this) == pcGetcodeType(o2);
        }
        catch (final RuntimeException e) {
            return false;
        }
    }
    
    public int pcGetEnhancementContractVersion() {
        return 2;
    }
    
    static {
        serialVersionUID = -6292964234525710903L;
        FacilityIdDemographicIssueCompositePk.pcFieldNames = new String[] { "caisiDemographicId", "codeType", "integratorFacilityId", "issueCode" };
        FacilityIdDemographicIssueCompositePk.pcFieldTypes = new Class[] { (FacilityIdDemographicIssueCompositePk.class$Ljava$lang$Integer != null) ? FacilityIdDemographicIssueCompositePk.class$Ljava$lang$Integer : (FacilityIdDemographicIssueCompositePk.class$Ljava$lang$Integer = class$("java.lang.Integer")), (FacilityIdDemographicIssueCompositePk.class$Lca$openosp$openo$caisi_integrator$util$CodeType != null) ? FacilityIdDemographicIssueCompositePk.class$Lca$openosp$openo$caisi_integrator$util$CodeType : (FacilityIdDemographicIssueCompositePk.class$Lca$openosp$openo$caisi_integrator$util$CodeType = class$("ca.openosp.openo.caisi_integrator.util.CodeType")), (FacilityIdDemographicIssueCompositePk.class$Ljava$lang$Integer != null) ? FacilityIdDemographicIssueCompositePk.class$Ljava$lang$Integer : (FacilityIdDemographicIssueCompositePk.class$Ljava$lang$Integer = class$("java.lang.Integer")), (FacilityIdDemographicIssueCompositePk.class$Ljava$lang$String != null) ? FacilityIdDemographicIssueCompositePk.class$Ljava$lang$String : (FacilityIdDemographicIssueCompositePk.class$Ljava$lang$String = class$("java.lang.String")) };
        FacilityIdDemographicIssueCompositePk.pcFieldFlags = new byte[] { 26, 26, 26, 26 };
        PCRegistry.register((FacilityIdDemographicIssueCompositePk.class$Lca$openosp$openo$caisi_integrator$dao$FacilityIdDemographicIssueCompositePk != null) ? FacilityIdDemographicIssueCompositePk.class$Lca$openosp$openo$caisi_integrator$dao$FacilityIdDemographicIssueCompositePk : (FacilityIdDemographicIssueCompositePk.class$Lca$openosp$openo$caisi_integrator$dao$FacilityIdDemographicIssueCompositePk = class$("ca.openosp.openo.caisi_integrator.dao.FacilityIdDemographicIssueCompositePk")), FacilityIdDemographicIssueCompositePk.pcFieldNames, FacilityIdDemographicIssueCompositePk.pcFieldTypes, FacilityIdDemographicIssueCompositePk.pcFieldFlags, FacilityIdDemographicIssueCompositePk.pcPCSuperclass, (String)null, (PersistenceCapable)new FacilityIdDemographicIssueCompositePk());
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
        this.codeType = null;
        this.integratorFacilityId = null;
        this.issueCode = null;
    }
    
    public PersistenceCapable pcNewInstance(final StateManager pcStateManager, final Object o, final boolean b) {
        final FacilityIdDemographicIssueCompositePk facilityIdDemographicIssueCompositePk = new FacilityIdDemographicIssueCompositePk();
        if (b) {
            facilityIdDemographicIssueCompositePk.pcClearFields();
        }
        facilityIdDemographicIssueCompositePk.pcStateManager = pcStateManager;
        facilityIdDemographicIssueCompositePk.pcCopyKeyFieldsFromObjectId(o);
        return (PersistenceCapable)facilityIdDemographicIssueCompositePk;
    }
    
    public PersistenceCapable pcNewInstance(final StateManager pcStateManager, final boolean b) {
        final FacilityIdDemographicIssueCompositePk facilityIdDemographicIssueCompositePk = new FacilityIdDemographicIssueCompositePk();
        if (b) {
            facilityIdDemographicIssueCompositePk.pcClearFields();
        }
        facilityIdDemographicIssueCompositePk.pcStateManager = pcStateManager;
        return (PersistenceCapable)facilityIdDemographicIssueCompositePk;
    }
    
    protected static int pcGetManagedFieldCount() {
        return 4;
    }
    
    public void pcReplaceField(final int n) {
        final int n2 = n - FacilityIdDemographicIssueCompositePk.pcInheritedFieldCount;
        if (n2 < 0) {
            throw new IllegalArgumentException();
        }
        switch (n2) {
            case 0: {
                this.caisiDemographicId = (Integer)this.pcStateManager.replaceObjectField((PersistenceCapable)this, n);
                return;
            }
            case 1: {
                this.codeType = (CodeType)this.pcStateManager.replaceObjectField((PersistenceCapable)this, n);
                return;
            }
            case 2: {
                this.integratorFacilityId = (Integer)this.pcStateManager.replaceObjectField((PersistenceCapable)this, n);
                return;
            }
            case 3: {
                this.issueCode = this.pcStateManager.replaceStringField((PersistenceCapable)this, n);
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
        final int n2 = n - FacilityIdDemographicIssueCompositePk.pcInheritedFieldCount;
        if (n2 < 0) {
            throw new IllegalArgumentException();
        }
        switch (n2) {
            case 0: {
                this.pcStateManager.providedObjectField((PersistenceCapable)this, n, (Object)this.caisiDemographicId);
                return;
            }
            case 1: {
                this.pcStateManager.providedObjectField((PersistenceCapable)this, n, (Object)this.codeType);
                return;
            }
            case 2: {
                this.pcStateManager.providedObjectField((PersistenceCapable)this, n, (Object)this.integratorFacilityId);
                return;
            }
            case 3: {
                this.pcStateManager.providedStringField((PersistenceCapable)this, n, this.issueCode);
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
    
    protected void pcCopyField(final FacilityIdDemographicIssueCompositePk facilityIdDemographicIssueCompositePk, final int n) {
        final int n2 = n - FacilityIdDemographicIssueCompositePk.pcInheritedFieldCount;
        if (n2 < 0) {
            throw new IllegalArgumentException();
        }
        switch (n2) {
            case 0: {
                this.caisiDemographicId = facilityIdDemographicIssueCompositePk.caisiDemographicId;
                return;
            }
            case 1: {
                this.codeType = facilityIdDemographicIssueCompositePk.codeType;
                return;
            }
            case 2: {
                this.integratorFacilityId = facilityIdDemographicIssueCompositePk.integratorFacilityId;
                return;
            }
            case 3: {
                this.issueCode = facilityIdDemographicIssueCompositePk.issueCode;
                return;
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
    }
    
    public void pcCopyFields(final Object o, final int[] array) {
        final FacilityIdDemographicIssueCompositePk facilityIdDemographicIssueCompositePk = (FacilityIdDemographicIssueCompositePk)o;
        if (facilityIdDemographicIssueCompositePk.pcStateManager != this.pcStateManager) {
            throw new IllegalArgumentException();
        }
        if (this.pcStateManager == null) {
            throw new IllegalStateException();
        }
        for (int i = 0; i < array.length; ++i) {
            this.pcCopyField(facilityIdDemographicIssueCompositePk, array[i]);
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
    
    private static final Integer pcGetcaisiDemographicId(final FacilityIdDemographicIssueCompositePk facilityIdDemographicIssueCompositePk) {
        if (facilityIdDemographicIssueCompositePk.pcStateManager == null) {
            return facilityIdDemographicIssueCompositePk.caisiDemographicId;
        }
        facilityIdDemographicIssueCompositePk.pcStateManager.accessingField(FacilityIdDemographicIssueCompositePk.pcInheritedFieldCount + 0);
        return facilityIdDemographicIssueCompositePk.caisiDemographicId;
    }
    
    private static final void pcSetcaisiDemographicId(final FacilityIdDemographicIssueCompositePk facilityIdDemographicIssueCompositePk, final Integer caisiDemographicId) {
        if (facilityIdDemographicIssueCompositePk.pcStateManager == null) {
            facilityIdDemographicIssueCompositePk.caisiDemographicId = caisiDemographicId;
            return;
        }
        facilityIdDemographicIssueCompositePk.pcStateManager.settingObjectField((PersistenceCapable)facilityIdDemographicIssueCompositePk, FacilityIdDemographicIssueCompositePk.pcInheritedFieldCount + 0, (Object)facilityIdDemographicIssueCompositePk.caisiDemographicId, (Object)caisiDemographicId, 0);
    }
    
    private static final CodeType pcGetcodeType(final FacilityIdDemographicIssueCompositePk facilityIdDemographicIssueCompositePk) {
        if (facilityIdDemographicIssueCompositePk.pcStateManager == null) {
            return facilityIdDemographicIssueCompositePk.codeType;
        }
        facilityIdDemographicIssueCompositePk.pcStateManager.accessingField(FacilityIdDemographicIssueCompositePk.pcInheritedFieldCount + 1);
        return facilityIdDemographicIssueCompositePk.codeType;
    }
    
    private static final void pcSetcodeType(final FacilityIdDemographicIssueCompositePk facilityIdDemographicIssueCompositePk, final CodeType codeType) {
        if (facilityIdDemographicIssueCompositePk.pcStateManager == null) {
            facilityIdDemographicIssueCompositePk.codeType = codeType;
            return;
        }
        facilityIdDemographicIssueCompositePk.pcStateManager.settingObjectField((PersistenceCapable)facilityIdDemographicIssueCompositePk, FacilityIdDemographicIssueCompositePk.pcInheritedFieldCount + 1, (Object)facilityIdDemographicIssueCompositePk.codeType, (Object)codeType, 0);
    }
    
    private static final Integer pcGetintegratorFacilityId(final FacilityIdDemographicIssueCompositePk facilityIdDemographicIssueCompositePk) {
        if (facilityIdDemographicIssueCompositePk.pcStateManager == null) {
            return facilityIdDemographicIssueCompositePk.integratorFacilityId;
        }
        facilityIdDemographicIssueCompositePk.pcStateManager.accessingField(FacilityIdDemographicIssueCompositePk.pcInheritedFieldCount + 2);
        return facilityIdDemographicIssueCompositePk.integratorFacilityId;
    }
    
    private static final void pcSetintegratorFacilityId(final FacilityIdDemographicIssueCompositePk facilityIdDemographicIssueCompositePk, final Integer integratorFacilityId) {
        if (facilityIdDemographicIssueCompositePk.pcStateManager == null) {
            facilityIdDemographicIssueCompositePk.integratorFacilityId = integratorFacilityId;
            return;
        }
        facilityIdDemographicIssueCompositePk.pcStateManager.settingObjectField((PersistenceCapable)facilityIdDemographicIssueCompositePk, FacilityIdDemographicIssueCompositePk.pcInheritedFieldCount + 2, (Object)facilityIdDemographicIssueCompositePk.integratorFacilityId, (Object)integratorFacilityId, 0);
    }
    
    private static final String pcGetissueCode(final FacilityIdDemographicIssueCompositePk facilityIdDemographicIssueCompositePk) {
        if (facilityIdDemographicIssueCompositePk.pcStateManager == null) {
            return facilityIdDemographicIssueCompositePk.issueCode;
        }
        facilityIdDemographicIssueCompositePk.pcStateManager.accessingField(FacilityIdDemographicIssueCompositePk.pcInheritedFieldCount + 3);
        return facilityIdDemographicIssueCompositePk.issueCode;
    }
    
    private static final void pcSetissueCode(final FacilityIdDemographicIssueCompositePk facilityIdDemographicIssueCompositePk, final String issueCode) {
        if (facilityIdDemographicIssueCompositePk.pcStateManager == null) {
            facilityIdDemographicIssueCompositePk.issueCode = issueCode;
            return;
        }
        facilityIdDemographicIssueCompositePk.pcStateManager.settingStringField((PersistenceCapable)facilityIdDemographicIssueCompositePk, FacilityIdDemographicIssueCompositePk.pcInheritedFieldCount + 3, facilityIdDemographicIssueCompositePk.issueCode, issueCode, 0);
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
