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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.PostLoad;
import java.util.Iterator;
import javax.xml.bind.annotation.XmlTransient;
import java.util.HashSet;
import org.apache.openjpa.enhance.StateManager;
import javax.persistence.Transient;
import javax.persistence.JoinColumn;
import javax.persistence.CollectionTable;
import javax.persistence.FetchType;
import javax.persistence.ElementCollection;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.TemporalType;
import javax.persistence.Temporal;
import java.util.Date;
import org.apache.openjpa.persistence.jdbc.Index;
import javax.persistence.EmbeddedId;
import java.util.Comparator;
import javax.persistence.Entity;

@Entity
public class CachedDemographicNote extends AbstractModel<CachedDemographicNoteCompositePk> implements PersistenceCapable
{
    public static final Comparator<CachedDemographicNote> OBSERVATION_DATE_COMPARATOR;
    @EmbeddedId
    @Index
    private CachedDemographicNoteCompositePk cachedDemographicNoteCompositePk;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date updateDate;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date observationDate;
    @Column(nullable = false)
    private int caisiDemographicId;
    @Column(nullable = false, length = 16)
    private String observationCaisiProviderId;
    @Column(length = 16)
    private String signingCaisiProviderId;
    @Column(nullable = false, length = 100)
    private String encounterType;
    @Column(nullable = false)
    private int caisiProgramId;
    @Column(columnDefinition = "mediumtext")
    private String note;
    @Column(nullable = false, length = 64)
    private String role;
    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "CachedDemographicNoteIssues", joinColumns = { @JoinColumn(name = "integratorFacilityId", referencedColumnName = "integratorFacilityId"), @JoinColumn(name = "uuid", referencedColumnName = "uuid") })
    @Column(name = "noteIssue")
    private Set<String> noteIssues;
    @Transient
    private Set<NoteIssue> issues;
    private static int pcInheritedFieldCount;
    private static String[] pcFieldNames;
    private static Class[] pcFieldTypes;
    private static byte[] pcFieldFlags;
    private static Class pcPCSuperclass;
    protected transient StateManager pcStateManager;
    static /* synthetic */ Class class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicNoteCompositePk;
    static /* synthetic */ Class class$Ljava$lang$String;
    static /* synthetic */ Class class$Ljava$util$Set;
    static /* synthetic */ Class class$Ljava$util$Date;
    static /* synthetic */ Class class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicNote;
    private transient Object pcDetachedState;
    private static final long serialVersionUID;
    
    public CachedDemographicNote() {
        this.updateDate = null;
        this.observationDate = null;
        this.caisiDemographicId = 0;
        this.observationCaisiProviderId = null;
        this.signingCaisiProviderId = null;
        this.encounterType = null;
        this.caisiProgramId = 0;
        this.note = null;
        this.role = null;
        this.noteIssues = new HashSet<String>();
        this.issues = new HashSet<NoteIssue>();
    }
    
    public CachedDemographicNote(final Integer integratedFacilityId, final String uuid, final int caisiDemographicId, final int caisiProgramId, final String observationCaisiProviderId, final Date observationDate, final String role) {
        this.updateDate = null;
        this.observationDate = null;
        this.caisiDemographicId = 0;
        this.observationCaisiProviderId = null;
        this.signingCaisiProviderId = null;
        this.encounterType = null;
        this.caisiProgramId = 0;
        this.note = null;
        this.role = null;
        this.noteIssues = new HashSet<String>();
        this.issues = new HashSet<NoteIssue>();
        this.cachedDemographicNoteCompositePk = new CachedDemographicNoteCompositePk(integratedFacilityId, uuid);
        this.caisiDemographicId = caisiDemographicId;
        this.caisiProgramId = caisiProgramId;
        this.observationCaisiProviderId = observationCaisiProviderId;
        this.observationDate = observationDate;
        this.role = role;
    }
    
    public void setCachedDemographicNoteCompositePk(final CachedDemographicNoteCompositePk cachedDemographicNoteCompositePk) {
        pcSetcachedDemographicNoteCompositePk(this, cachedDemographicNoteCompositePk);
    }
    
    public CachedDemographicNoteCompositePk getCachedDemographicNoteCompositePk() {
        return pcGetcachedDemographicNoteCompositePk(this);
    }
    
    @Override
    public CachedDemographicNoteCompositePk getId() {
        return pcGetcachedDemographicNoteCompositePk(this);
    }
    
    public Date getUpdateDate() {
        return pcGetupdateDate(this);
    }
    
    public void setUpdateDate(final Date updateDate) {
        pcSetupdateDate(this, updateDate);
    }
    
    public Date getObservationDate() {
        return pcGetobservationDate(this);
    }
    
    public void setObservationDate(final Date observationDate) {
        pcSetobservationDate(this, observationDate);
    }
    
    public int getCaisiDemographicId() {
        return pcGetcaisiDemographicId(this);
    }
    
    public void setCaisiDemographicId(final int caisiDemographicId) {
        pcSetcaisiDemographicId(this, caisiDemographicId);
    }
    
    public String getObservationCaisiProviderId() {
        return pcGetobservationCaisiProviderId(this);
    }
    
    public void setObservationCaisiProviderId(final String observationCaisiProviderId) {
        pcSetobservationCaisiProviderId(this, observationCaisiProviderId);
    }
    
    public int getCaisiProgramId() {
        return pcGetcaisiProgramId(this);
    }
    
    public void setCaisiProgramId(final int caisiProgramId) {
        pcSetcaisiProgramId(this, caisiProgramId);
    }
    
    public String getRole() {
        return pcGetrole(this);
    }
    
    public void setRole(final String role) {
        pcSetrole(this, role);
    }
    
    public String getNote() {
        return pcGetnote(this);
    }
    
    public void setNote(final String note) {
        pcSetnote(this, note);
    }
    
    public String getSigningCaisiProviderId() {
        return pcGetsigningCaisiProviderId(this);
    }
    
    public void setSigningCaisiProviderId(final String signingCaisiProviderId) {
        pcSetsigningCaisiProviderId(this, signingCaisiProviderId);
    }
    
    public String getEncounterType() {
        return pcGetencounterType(this);
    }
    
    public void setEncounterType(final String encounterType) {
        pcSetencounterType(this, encounterType);
    }
    
    public Set<NoteIssue> getIssues() {
        return this.issues;
    }
    
    public void setIssues(final Set<NoteIssue> issues) {
        this.issues = issues;
    }
    
    @XmlTransient
    public Set<String> getNoteIssues() {
        return pcGetnoteIssues(this);
    }
    
    public void setNoteIssues(final Set<String> noteIssues) {
        pcSetnoteIssues(this, noteIssues);
    }
    
    @PostLoad
    protected void logRead() {
        for (final Object obj : pcGetnoteIssues(this)) {
            final String noteIssue = (String)obj;
            this.getIssues().add(NoteIssue.valueOf(noteIssue));
        }
    }
    
    @PreUpdate
    @PrePersist
    protected void logWrite() {
        for (final NoteIssue issue : this.issues) {
            this.getNoteIssues().add(issue.asString());
        }
    }
    
    static {
        serialVersionUID = 3202392287993714226L;
        OBSERVATION_DATE_COMPARATOR = new Comparator<CachedDemographicNote>() {
            @Override
            public int compare(final CachedDemographicNote o1, final CachedDemographicNote o2) {
                return o1.getObservationDate().compareTo(o2.getObservationDate());
            }
        };
        CachedDemographicNote.pcFieldNames = new String[] { "cachedDemographicNoteCompositePk", "caisiDemographicId", "caisiProgramId", "encounterType", "note", "noteIssues", "observationCaisiProviderId", "observationDate", "role", "signingCaisiProviderId", "updateDate" };
        CachedDemographicNote.pcFieldTypes = new Class[] { (CachedDemographicNote.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicNoteCompositePk != null) ? CachedDemographicNote.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicNoteCompositePk : (CachedDemographicNote.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicNoteCompositePk = class$("ca.openosp.openo.caisi_integrator.dao.CachedDemographicNoteCompositePk")), Integer.TYPE, Integer.TYPE, (CachedDemographicNote.class$Ljava$lang$String != null) ? CachedDemographicNote.class$Ljava$lang$String : (CachedDemographicNote.class$Ljava$lang$String = class$("java.lang.String")), (CachedDemographicNote.class$Ljava$lang$String != null) ? CachedDemographicNote.class$Ljava$lang$String : (CachedDemographicNote.class$Ljava$lang$String = class$("java.lang.String")), (CachedDemographicNote.class$Ljava$util$Set != null) ? CachedDemographicNote.class$Ljava$util$Set : (CachedDemographicNote.class$Ljava$util$Set = class$("java.util.Set")), (CachedDemographicNote.class$Ljava$lang$String != null) ? CachedDemographicNote.class$Ljava$lang$String : (CachedDemographicNote.class$Ljava$lang$String = class$("java.lang.String")), (CachedDemographicNote.class$Ljava$util$Date != null) ? CachedDemographicNote.class$Ljava$util$Date : (CachedDemographicNote.class$Ljava$util$Date = class$("java.util.Date")), (CachedDemographicNote.class$Ljava$lang$String != null) ? CachedDemographicNote.class$Ljava$lang$String : (CachedDemographicNote.class$Ljava$lang$String = class$("java.lang.String")), (CachedDemographicNote.class$Ljava$lang$String != null) ? CachedDemographicNote.class$Ljava$lang$String : (CachedDemographicNote.class$Ljava$lang$String = class$("java.lang.String")), (CachedDemographicNote.class$Ljava$util$Date != null) ? CachedDemographicNote.class$Ljava$util$Date : (CachedDemographicNote.class$Ljava$util$Date = class$("java.util.Date")) };
        CachedDemographicNote.pcFieldFlags = new byte[] { 26, 26, 26, 26, 26, 10, 26, 26, 26, 26, 26 };
        PCRegistry.register((CachedDemographicNote.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicNote != null) ? CachedDemographicNote.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicNote : (CachedDemographicNote.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicNote = class$("ca.openosp.openo.caisi_integrator.dao.CachedDemographicNote")), CachedDemographicNote.pcFieldNames, CachedDemographicNote.pcFieldTypes, CachedDemographicNote.pcFieldFlags, CachedDemographicNote.pcPCSuperclass, "CachedDemographicNote", (PersistenceCapable)new CachedDemographicNote());
    }
    
    public int pcGetEnhancementContractVersion() {
        return 2;
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
        this.cachedDemographicNoteCompositePk = null;
        this.caisiDemographicId = 0;
        this.caisiProgramId = 0;
        this.encounterType = null;
        this.note = null;
        this.noteIssues = null;
        this.observationCaisiProviderId = null;
        this.observationDate = null;
        this.role = null;
        this.signingCaisiProviderId = null;
        this.updateDate = null;
    }
    
    public PersistenceCapable pcNewInstance(final StateManager pcStateManager, final Object o, final boolean b) {
        final CachedDemographicNote cachedDemographicNote = new CachedDemographicNote();
        if (b) {
            cachedDemographicNote.pcClearFields();
        }
        cachedDemographicNote.pcStateManager = pcStateManager;
        cachedDemographicNote.pcCopyKeyFieldsFromObjectId(o);
        return (PersistenceCapable)cachedDemographicNote;
    }
    
    public PersistenceCapable pcNewInstance(final StateManager pcStateManager, final boolean b) {
        final CachedDemographicNote cachedDemographicNote = new CachedDemographicNote();
        if (b) {
            cachedDemographicNote.pcClearFields();
        }
        cachedDemographicNote.pcStateManager = pcStateManager;
        return (PersistenceCapable)cachedDemographicNote;
    }
    
    protected static int pcGetManagedFieldCount() {
        return 11;
    }
    
    public void pcReplaceField(final int n) {
        final int n2 = n - CachedDemographicNote.pcInheritedFieldCount;
        if (n2 < 0) {
            throw new IllegalArgumentException();
        }
        switch (n2) {
            case 0: {
                this.cachedDemographicNoteCompositePk = (CachedDemographicNoteCompositePk)this.pcStateManager.replaceObjectField((PersistenceCapable)this, n);
                return;
            }
            case 1: {
                this.caisiDemographicId = this.pcStateManager.replaceIntField((PersistenceCapable)this, n);
                return;
            }
            case 2: {
                this.caisiProgramId = this.pcStateManager.replaceIntField((PersistenceCapable)this, n);
                return;
            }
            case 3: {
                this.encounterType = this.pcStateManager.replaceStringField((PersistenceCapable)this, n);
                return;
            }
            case 4: {
                this.note = this.pcStateManager.replaceStringField((PersistenceCapable)this, n);
                return;
            }
            case 5: {
                this.noteIssues = (Set)this.pcStateManager.replaceObjectField((PersistenceCapable)this, n);
                return;
            }
            case 6: {
                this.observationCaisiProviderId = this.pcStateManager.replaceStringField((PersistenceCapable)this, n);
                return;
            }
            case 7: {
                this.observationDate = (Date)this.pcStateManager.replaceObjectField((PersistenceCapable)this, n);
                return;
            }
            case 8: {
                this.role = this.pcStateManager.replaceStringField((PersistenceCapable)this, n);
                return;
            }
            case 9: {
                this.signingCaisiProviderId = this.pcStateManager.replaceStringField((PersistenceCapable)this, n);
                return;
            }
            case 10: {
                this.updateDate = (Date)this.pcStateManager.replaceObjectField((PersistenceCapable)this, n);
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
        final int n2 = n - CachedDemographicNote.pcInheritedFieldCount;
        if (n2 < 0) {
            throw new IllegalArgumentException();
        }
        switch (n2) {
            case 0: {
                this.pcStateManager.providedObjectField((PersistenceCapable)this, n, (Object)this.cachedDemographicNoteCompositePk);
                return;
            }
            case 1: {
                this.pcStateManager.providedIntField((PersistenceCapable)this, n, this.caisiDemographicId);
                return;
            }
            case 2: {
                this.pcStateManager.providedIntField((PersistenceCapable)this, n, this.caisiProgramId);
                return;
            }
            case 3: {
                this.pcStateManager.providedStringField((PersistenceCapable)this, n, this.encounterType);
                return;
            }
            case 4: {
                this.pcStateManager.providedStringField((PersistenceCapable)this, n, this.note);
                return;
            }
            case 5: {
                this.pcStateManager.providedObjectField((PersistenceCapable)this, n, (Object)this.noteIssues);
                return;
            }
            case 6: {
                this.pcStateManager.providedStringField((PersistenceCapable)this, n, this.observationCaisiProviderId);
                return;
            }
            case 7: {
                this.pcStateManager.providedObjectField((PersistenceCapable)this, n, (Object)this.observationDate);
                return;
            }
            case 8: {
                this.pcStateManager.providedStringField((PersistenceCapable)this, n, this.role);
                return;
            }
            case 9: {
                this.pcStateManager.providedStringField((PersistenceCapable)this, n, this.signingCaisiProviderId);
                return;
            }
            case 10: {
                this.pcStateManager.providedObjectField((PersistenceCapable)this, n, (Object)this.updateDate);
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
    
    protected void pcCopyField(final CachedDemographicNote cachedDemographicNote, final int n) {
        final int n2 = n - CachedDemographicNote.pcInheritedFieldCount;
        if (n2 < 0) {
            throw new IllegalArgumentException();
        }
        switch (n2) {
            case 0: {
                this.cachedDemographicNoteCompositePk = cachedDemographicNote.cachedDemographicNoteCompositePk;
                return;
            }
            case 1: {
                this.caisiDemographicId = cachedDemographicNote.caisiDemographicId;
                return;
            }
            case 2: {
                this.caisiProgramId = cachedDemographicNote.caisiProgramId;
                return;
            }
            case 3: {
                this.encounterType = cachedDemographicNote.encounterType;
                return;
            }
            case 4: {
                this.note = cachedDemographicNote.note;
                return;
            }
            case 5: {
                this.noteIssues = cachedDemographicNote.noteIssues;
                return;
            }
            case 6: {
                this.observationCaisiProviderId = cachedDemographicNote.observationCaisiProviderId;
                return;
            }
            case 7: {
                this.observationDate = cachedDemographicNote.observationDate;
                return;
            }
            case 8: {
                this.role = cachedDemographicNote.role;
                return;
            }
            case 9: {
                this.signingCaisiProviderId = cachedDemographicNote.signingCaisiProviderId;
                return;
            }
            case 10: {
                this.updateDate = cachedDemographicNote.updateDate;
                return;
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
    }
    
    public void pcCopyFields(final Object o, final int[] array) {
        final CachedDemographicNote cachedDemographicNote = (CachedDemographicNote)o;
        if (cachedDemographicNote.pcStateManager != this.pcStateManager) {
            throw new IllegalArgumentException();
        }
        if (this.pcStateManager == null) {
            throw new IllegalStateException();
        }
        for (int i = 0; i < array.length; ++i) {
            this.pcCopyField(cachedDemographicNote, array[i]);
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
        fieldConsumer.storeObjectField(0 + CachedDemographicNote.pcInheritedFieldCount, ((ObjectId)o).getId());
    }
    
    public void pcCopyKeyFieldsFromObjectId(final Object o) {
        this.cachedDemographicNoteCompositePk = (CachedDemographicNoteCompositePk)((ObjectId)o).getId();
    }
    
    public Object pcNewObjectIdInstance(final Object o) {
        throw new IllegalArgumentException("The id type \"class org.apache.openjpa.util.ObjectId\" specified by persistent type \"class ca.openosp.openo.caisi_integrator.dao.CachedDemographicNote\" does not have a public class org.apache.openjpa.util.ObjectId(String) or class org.apache.openjpa.util.ObjectId(Class, String) constructor.");
    }
    
    public Object pcNewObjectIdInstance() {
        return new ObjectId((CachedDemographicNote.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicNote != null) ? CachedDemographicNote.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicNote : (CachedDemographicNote.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicNote = class$("ca.openosp.openo.caisi_integrator.dao.CachedDemographicNote")), (Object)this.cachedDemographicNoteCompositePk);
    }
    
    private static final CachedDemographicNoteCompositePk pcGetcachedDemographicNoteCompositePk(final CachedDemographicNote cachedDemographicNote) {
        if (cachedDemographicNote.pcStateManager == null) {
            return cachedDemographicNote.cachedDemographicNoteCompositePk;
        }
        cachedDemographicNote.pcStateManager.accessingField(CachedDemographicNote.pcInheritedFieldCount + 0);
        return cachedDemographicNote.cachedDemographicNoteCompositePk;
    }
    
    private static final void pcSetcachedDemographicNoteCompositePk(final CachedDemographicNote cachedDemographicNote, final CachedDemographicNoteCompositePk cachedDemographicNoteCompositePk) {
        if (cachedDemographicNote.pcStateManager == null) {
            cachedDemographicNote.cachedDemographicNoteCompositePk = cachedDemographicNoteCompositePk;
            return;
        }
        cachedDemographicNote.pcStateManager.settingObjectField((PersistenceCapable)cachedDemographicNote, CachedDemographicNote.pcInheritedFieldCount + 0, (Object)cachedDemographicNote.cachedDemographicNoteCompositePk, (Object)cachedDemographicNoteCompositePk, 0);
    }
    
    private static final int pcGetcaisiDemographicId(final CachedDemographicNote cachedDemographicNote) {
        if (cachedDemographicNote.pcStateManager == null) {
            return cachedDemographicNote.caisiDemographicId;
        }
        cachedDemographicNote.pcStateManager.accessingField(CachedDemographicNote.pcInheritedFieldCount + 1);
        return cachedDemographicNote.caisiDemographicId;
    }
    
    private static final void pcSetcaisiDemographicId(final CachedDemographicNote cachedDemographicNote, final int caisiDemographicId) {
        if (cachedDemographicNote.pcStateManager == null) {
            cachedDemographicNote.caisiDemographicId = caisiDemographicId;
            return;
        }
        cachedDemographicNote.pcStateManager.settingIntField((PersistenceCapable)cachedDemographicNote, CachedDemographicNote.pcInheritedFieldCount + 1, cachedDemographicNote.caisiDemographicId, caisiDemographicId, 0);
    }
    
    private static final int pcGetcaisiProgramId(final CachedDemographicNote cachedDemographicNote) {
        if (cachedDemographicNote.pcStateManager == null) {
            return cachedDemographicNote.caisiProgramId;
        }
        cachedDemographicNote.pcStateManager.accessingField(CachedDemographicNote.pcInheritedFieldCount + 2);
        return cachedDemographicNote.caisiProgramId;
    }
    
    private static final void pcSetcaisiProgramId(final CachedDemographicNote cachedDemographicNote, final int caisiProgramId) {
        if (cachedDemographicNote.pcStateManager == null) {
            cachedDemographicNote.caisiProgramId = caisiProgramId;
            return;
        }
        cachedDemographicNote.pcStateManager.settingIntField((PersistenceCapable)cachedDemographicNote, CachedDemographicNote.pcInheritedFieldCount + 2, cachedDemographicNote.caisiProgramId, caisiProgramId, 0);
    }
    
    private static final String pcGetencounterType(final CachedDemographicNote cachedDemographicNote) {
        if (cachedDemographicNote.pcStateManager == null) {
            return cachedDemographicNote.encounterType;
        }
        cachedDemographicNote.pcStateManager.accessingField(CachedDemographicNote.pcInheritedFieldCount + 3);
        return cachedDemographicNote.encounterType;
    }
    
    private static final void pcSetencounterType(final CachedDemographicNote cachedDemographicNote, final String encounterType) {
        if (cachedDemographicNote.pcStateManager == null) {
            cachedDemographicNote.encounterType = encounterType;
            return;
        }
        cachedDemographicNote.pcStateManager.settingStringField((PersistenceCapable)cachedDemographicNote, CachedDemographicNote.pcInheritedFieldCount + 3, cachedDemographicNote.encounterType, encounterType, 0);
    }
    
    private static final String pcGetnote(final CachedDemographicNote cachedDemographicNote) {
        if (cachedDemographicNote.pcStateManager == null) {
            return cachedDemographicNote.note;
        }
        cachedDemographicNote.pcStateManager.accessingField(CachedDemographicNote.pcInheritedFieldCount + 4);
        return cachedDemographicNote.note;
    }
    
    private static final void pcSetnote(final CachedDemographicNote cachedDemographicNote, final String note) {
        if (cachedDemographicNote.pcStateManager == null) {
            cachedDemographicNote.note = note;
            return;
        }
        cachedDemographicNote.pcStateManager.settingStringField((PersistenceCapable)cachedDemographicNote, CachedDemographicNote.pcInheritedFieldCount + 4, cachedDemographicNote.note, note, 0);
    }
    
    private static final Set pcGetnoteIssues(final CachedDemographicNote cachedDemographicNote) {
        if (cachedDemographicNote.pcStateManager == null) {
            return cachedDemographicNote.noteIssues;
        }
        cachedDemographicNote.pcStateManager.accessingField(CachedDemographicNote.pcInheritedFieldCount + 5);
        return cachedDemographicNote.noteIssues;
    }
    
    private static final void pcSetnoteIssues(final CachedDemographicNote cachedDemographicNote, final Set noteIssues) {
        if (cachedDemographicNote.pcStateManager == null) {
            cachedDemographicNote.noteIssues = noteIssues;
            return;
        }
        cachedDemographicNote.pcStateManager.settingObjectField((PersistenceCapable)cachedDemographicNote, CachedDemographicNote.pcInheritedFieldCount + 5, (Object)cachedDemographicNote.noteIssues, (Object)noteIssues, 0);
    }
    
    private static final String pcGetobservationCaisiProviderId(final CachedDemographicNote cachedDemographicNote) {
        if (cachedDemographicNote.pcStateManager == null) {
            return cachedDemographicNote.observationCaisiProviderId;
        }
        cachedDemographicNote.pcStateManager.accessingField(CachedDemographicNote.pcInheritedFieldCount + 6);
        return cachedDemographicNote.observationCaisiProviderId;
    }
    
    private static final void pcSetobservationCaisiProviderId(final CachedDemographicNote cachedDemographicNote, final String observationCaisiProviderId) {
        if (cachedDemographicNote.pcStateManager == null) {
            cachedDemographicNote.observationCaisiProviderId = observationCaisiProviderId;
            return;
        }
        cachedDemographicNote.pcStateManager.settingStringField((PersistenceCapable)cachedDemographicNote, CachedDemographicNote.pcInheritedFieldCount + 6, cachedDemographicNote.observationCaisiProviderId, observationCaisiProviderId, 0);
    }
    
    private static final Date pcGetobservationDate(final CachedDemographicNote cachedDemographicNote) {
        if (cachedDemographicNote.pcStateManager == null) {
            return cachedDemographicNote.observationDate;
        }
        cachedDemographicNote.pcStateManager.accessingField(CachedDemographicNote.pcInheritedFieldCount + 7);
        return cachedDemographicNote.observationDate;
    }
    
    private static final void pcSetobservationDate(final CachedDemographicNote cachedDemographicNote, final Date observationDate) {
        if (cachedDemographicNote.pcStateManager == null) {
            cachedDemographicNote.observationDate = observationDate;
            return;
        }
        cachedDemographicNote.pcStateManager.settingObjectField((PersistenceCapable)cachedDemographicNote, CachedDemographicNote.pcInheritedFieldCount + 7, (Object)cachedDemographicNote.observationDate, (Object)observationDate, 0);
    }
    
    private static final String pcGetrole(final CachedDemographicNote cachedDemographicNote) {
        if (cachedDemographicNote.pcStateManager == null) {
            return cachedDemographicNote.role;
        }
        cachedDemographicNote.pcStateManager.accessingField(CachedDemographicNote.pcInheritedFieldCount + 8);
        return cachedDemographicNote.role;
    }
    
    private static final void pcSetrole(final CachedDemographicNote cachedDemographicNote, final String role) {
        if (cachedDemographicNote.pcStateManager == null) {
            cachedDemographicNote.role = role;
            return;
        }
        cachedDemographicNote.pcStateManager.settingStringField((PersistenceCapable)cachedDemographicNote, CachedDemographicNote.pcInheritedFieldCount + 8, cachedDemographicNote.role, role, 0);
    }
    
    private static final String pcGetsigningCaisiProviderId(final CachedDemographicNote cachedDemographicNote) {
        if (cachedDemographicNote.pcStateManager == null) {
            return cachedDemographicNote.signingCaisiProviderId;
        }
        cachedDemographicNote.pcStateManager.accessingField(CachedDemographicNote.pcInheritedFieldCount + 9);
        return cachedDemographicNote.signingCaisiProviderId;
    }
    
    private static final void pcSetsigningCaisiProviderId(final CachedDemographicNote cachedDemographicNote, final String signingCaisiProviderId) {
        if (cachedDemographicNote.pcStateManager == null) {
            cachedDemographicNote.signingCaisiProviderId = signingCaisiProviderId;
            return;
        }
        cachedDemographicNote.pcStateManager.settingStringField((PersistenceCapable)cachedDemographicNote, CachedDemographicNote.pcInheritedFieldCount + 9, cachedDemographicNote.signingCaisiProviderId, signingCaisiProviderId, 0);
    }
    
    private static final Date pcGetupdateDate(final CachedDemographicNote cachedDemographicNote) {
        if (cachedDemographicNote.pcStateManager == null) {
            return cachedDemographicNote.updateDate;
        }
        cachedDemographicNote.pcStateManager.accessingField(CachedDemographicNote.pcInheritedFieldCount + 10);
        return cachedDemographicNote.updateDate;
    }
    
    private static final void pcSetupdateDate(final CachedDemographicNote cachedDemographicNote, final Date updateDate) {
        if (cachedDemographicNote.pcStateManager == null) {
            cachedDemographicNote.updateDate = updateDate;
            return;
        }
        cachedDemographicNote.pcStateManager.settingObjectField((PersistenceCapable)cachedDemographicNote, CachedDemographicNote.pcInheritedFieldCount + 10, (Object)cachedDemographicNote.updateDate, (Object)updateDate, 0);
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
