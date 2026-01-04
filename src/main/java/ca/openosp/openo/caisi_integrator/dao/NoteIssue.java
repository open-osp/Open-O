package ca.openosp.openo.caisi_integrator.dao;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import org.apache.openjpa.enhance.FieldConsumer;
import org.apache.openjpa.enhance.FieldSupplier;
import org.apache.openjpa.enhance.RedefinitionHelper;
import org.apache.openjpa.enhance.PersistenceCapable;
import org.apache.openjpa.enhance.PCRegistry;
import ca.openosp.openo.caisi_integrator.util.MiscUtils;
import java.util.List;
import java.util.Iterator;
import java.util.HashSet;
import org.apache.openjpa.enhance.StateManager;
import ca.openosp.openo.caisi_integrator.util.CodeType;
import org.apache.log4j.Logger;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class NoteIssue implements Comparable<NoteIssue>, Serializable, PersistenceCapable
{
    private static final long serialVersionUID = 1L;
    private static final Logger logger;
    private CodeType codeType;
    private String issueCode;
    private static int pcInheritedFieldCount;
    private static String[] pcFieldNames;
    private static Class[] pcFieldTypes;
    private static byte[] pcFieldFlags;
    private static Class pcPCSuperclass;
    protected transient StateManager pcStateManager;
    static /* synthetic */ Class class$Lca$openosp$openo$caisi_integrator$util$CodeType;
    static /* synthetic */ Class class$Ljava$lang$String;
    static /* synthetic */ Class class$Lca$openosp$openo$caisi_integrator$dao$NoteIssue;
    private transient Object pcDetachedState;
    
    public NoteIssue() {
    }
    
    private NoteIssue(final CodeType codeType, final String issueCode) {
        this.codeType = codeType;
        this.issueCode = issueCode;
    }
    
    public CodeType getCodeType() {
        return pcGetcodeType(this);
    }
    
    public void setCodeType(final CodeType codeType) {
        pcSetcodeType(this, codeType);
    }
    
    public String getIssueCode() {
        return pcGetissueCode(this);
    }
    
    public void setIssueCode(final String issueCode) {
        pcSetissueCode(this, issueCode);
    }
    
    public final String asString() {
        return String.format("%s.%s", this.getCodeType().name(), this.getIssueCode());
    }
    
    public static final HashSet<String> toStrings(final HashSet<NoteIssue> issues) {
        final HashSet<String> tmp = new HashSet<String>();
        for (final NoteIssue issue : issues) {
            tmp.add(String.format("%s.%s", issue.getCodeType().name(), issue.getIssueCode()));
        }
        return tmp;
    }
    
    public static final HashSet<NoteIssue> fromStrings(final List<String> strings) {
        final HashSet<NoteIssue> tmp = new HashSet<NoteIssue>();
        for (final String item : strings) {
            try {
                tmp.add(valueOf(item));
            }
            catch (final Exception e) {
                NoteIssue.logger.error((Object)"Unexpected error.", (Throwable)e);
            }
        }
        return tmp;
    }
    
    public static NoteIssue valueOf(final String s) {
        final String[] tempSplit = s.split("\\.");
        if (tempSplit.length == 2) {
            return new NoteIssue(CodeType.valueOf(tempSplit[0]), tempSplit[1]);
        }
        throw new IllegalArgumentException(s + ", split=" + tempSplit);
    }
    
    @Override
    public String toString() {
        return pcGetcodeType(this).name() + '.' + pcGetissueCode(this);
    }
    
    @Override
    public int hashCode() {
        return pcGetissueCode(this).hashCode();
    }
    
    @Override
    public boolean equals(final Object o) {
        try {
            final NoteIssue o2 = (NoteIssue)o;
            return pcGetcodeType(this).equals(pcGetcodeType(o2)) && pcGetissueCode(this).equals(pcGetissueCode(o2));
        }
        catch (final RuntimeException e) {
            return false;
        }
    }
    
    @Override
    public int compareTo(final NoteIssue o) {
        return this.toString().compareTo(o.toString());
    }
    
    static {
        logger = MiscUtils.getLogger();
        NoteIssue.pcFieldNames = new String[] { "codeType", "issueCode" };
        NoteIssue.pcFieldTypes = new Class[] { (NoteIssue.class$Lca$openosp$openo$caisi_integrator$util$CodeType != null) ? NoteIssue.class$Lca$openosp$openo$caisi_integrator$util$CodeType : (NoteIssue.class$Lca$openosp$openo$caisi_integrator$util$CodeType = class$("ca.openosp.openo.caisi_integrator.util.CodeType")), (NoteIssue.class$Ljava$lang$String != null) ? NoteIssue.class$Ljava$lang$String : (NoteIssue.class$Ljava$lang$String = class$("java.lang.String")) };
        NoteIssue.pcFieldFlags = new byte[] { 26, 26 };
        PCRegistry.register((NoteIssue.class$Lca$openosp$openo$caisi_integrator$dao$NoteIssue != null) ? NoteIssue.class$Lca$openosp$openo$caisi_integrator$dao$NoteIssue : (NoteIssue.class$Lca$openosp$openo$caisi_integrator$dao$NoteIssue = class$("ca.openosp.openo.caisi_integrator.dao.NoteIssue")), NoteIssue.pcFieldNames, NoteIssue.pcFieldTypes, NoteIssue.pcFieldFlags, NoteIssue.pcPCSuperclass, (String)null, (PersistenceCapable)new NoteIssue());
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
        this.codeType = null;
        this.issueCode = null;
    }
    
    public PersistenceCapable pcNewInstance(final StateManager pcStateManager, final Object o, final boolean b) {
        final NoteIssue noteIssue = new NoteIssue();
        if (b) {
            noteIssue.pcClearFields();
        }
        noteIssue.pcStateManager = pcStateManager;
        noteIssue.pcCopyKeyFieldsFromObjectId(o);
        return (PersistenceCapable)noteIssue;
    }
    
    public PersistenceCapable pcNewInstance(final StateManager pcStateManager, final boolean b) {
        final NoteIssue noteIssue = new NoteIssue();
        if (b) {
            noteIssue.pcClearFields();
        }
        noteIssue.pcStateManager = pcStateManager;
        return (PersistenceCapable)noteIssue;
    }
    
    protected static int pcGetManagedFieldCount() {
        return 2;
    }
    
    public void pcReplaceField(final int n) {
        final int n2 = n - NoteIssue.pcInheritedFieldCount;
        if (n2 < 0) {
            throw new IllegalArgumentException();
        }
        switch (n2) {
            case 0: {
                this.codeType = (CodeType)this.pcStateManager.replaceObjectField((PersistenceCapable)this, n);
                return;
            }
            case 1: {
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
        final int n2 = n - NoteIssue.pcInheritedFieldCount;
        if (n2 < 0) {
            throw new IllegalArgumentException();
        }
        switch (n2) {
            case 0: {
                this.pcStateManager.providedObjectField((PersistenceCapable)this, n, (Object)this.codeType);
                return;
            }
            case 1: {
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
    
    protected void pcCopyField(final NoteIssue noteIssue, final int n) {
        final int n2 = n - NoteIssue.pcInheritedFieldCount;
        if (n2 < 0) {
            throw new IllegalArgumentException();
        }
        switch (n2) {
            case 0: {
                this.codeType = noteIssue.codeType;
                return;
            }
            case 1: {
                this.issueCode = noteIssue.issueCode;
                return;
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
    }
    
    public void pcCopyFields(final Object o, final int[] array) {
        final NoteIssue noteIssue = (NoteIssue)o;
        if (noteIssue.pcStateManager != this.pcStateManager) {
            throw new IllegalArgumentException();
        }
        if (this.pcStateManager == null) {
            throw new IllegalStateException();
        }
        for (int i = 0; i < array.length; ++i) {
            this.pcCopyField(noteIssue, array[i]);
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
    
    private static final CodeType pcGetcodeType(final NoteIssue noteIssue) {
        if (noteIssue.pcStateManager == null) {
            return noteIssue.codeType;
        }
        noteIssue.pcStateManager.accessingField(NoteIssue.pcInheritedFieldCount + 0);
        return noteIssue.codeType;
    }
    
    private static final void pcSetcodeType(final NoteIssue noteIssue, final CodeType codeType) {
        if (noteIssue.pcStateManager == null) {
            noteIssue.codeType = codeType;
            return;
        }
        noteIssue.pcStateManager.settingObjectField((PersistenceCapable)noteIssue, NoteIssue.pcInheritedFieldCount + 0, (Object)noteIssue.codeType, (Object)codeType, 0);
    }
    
    private static final String pcGetissueCode(final NoteIssue noteIssue) {
        if (noteIssue.pcStateManager == null) {
            return noteIssue.issueCode;
        }
        noteIssue.pcStateManager.accessingField(NoteIssue.pcInheritedFieldCount + 1);
        return noteIssue.issueCode;
    }
    
    private static final void pcSetissueCode(final NoteIssue noteIssue, final String issueCode) {
        if (noteIssue.pcStateManager == null) {
            noteIssue.issueCode = issueCode;
            return;
        }
        noteIssue.pcStateManager.settingStringField((PersistenceCapable)noteIssue, NoteIssue.pcInheritedFieldCount + 1, noteIssue.issueCode, issueCode, 0);
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
