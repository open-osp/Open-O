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
import ca.openosp.openo.caisi_integrator.util.ImageIoUtils;
import org.apache.openjpa.enhance.StateManager;
import javax.persistence.Column;
import javax.persistence.TemporalType;
import javax.persistence.Temporal;
import java.util.Date;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class CachedDemographicImage extends AbstractModel<FacilityIdIntegerCompositePk> implements PersistenceCapable
{
    @EmbeddedId
    private FacilityIdIntegerCompositePk facilityDemographicPk;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @Column(columnDefinition = "mediumblob")
    private byte[] image;
    private static int pcInheritedFieldCount;
    private static String[] pcFieldNames;
    private static Class[] pcFieldTypes;
    private static byte[] pcFieldFlags;
    private static Class pcPCSuperclass;
    protected transient StateManager pcStateManager;
    static /* synthetic */ Class class$Lca$openosp$openo$caisi_integrator$dao$FacilityIdIntegerCompositePk;
    static /* synthetic */ Class class$L$B;
    static /* synthetic */ Class class$Ljava$util$Date;
    static /* synthetic */ Class class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicImage;
    private transient Object pcDetachedState;
    private static final long serialVersionUID;
    
    public CachedDemographicImage() {
        this.updateDate = null;
        this.image = null;
    }
    
    public FacilityIdIntegerCompositePk getFacilityIdIntegerCompositePk() {
        return pcGetfacilityDemographicPk(this);
    }
    
    public void setFacilityIdIntegerCompositePk(final FacilityIdIntegerCompositePk facilityDemographicPk) {
        pcSetfacilityDemographicPk(this, facilityDemographicPk);
    }
    
    public Date getUpdateDate() {
        return pcGetupdateDate(this);
    }
    
    public void setUpdateDate(final Date updateDate) {
        pcSetupdateDate(this, updateDate);
    }
    
    public byte[] getImage() {
        return pcGetimage(this);
    }
    
    public void setImage(final byte[] original) {
        pcSetimage(this, ImageIoUtils.scaleJpgSmallerProportionally(original, 200, 200, 0.9f));
    }
    
    @Override
    public FacilityIdIntegerCompositePk getId() {
        return pcGetfacilityDemographicPk(this);
    }
    
    public int pcGetEnhancementContractVersion() {
        return 2;
    }
    
    static {
        serialVersionUID = -9132848329454932558L;
        CachedDemographicImage.pcFieldNames = new String[] { "facilityDemographicPk", "image", "updateDate" };
        CachedDemographicImage.pcFieldTypes = new Class[] { (CachedDemographicImage.class$Lca$openosp$openo$caisi_integrator$dao$FacilityIdIntegerCompositePk != null) ? CachedDemographicImage.class$Lca$openosp$openo$caisi_integrator$dao$FacilityIdIntegerCompositePk : (CachedDemographicImage.class$Lca$openosp$openo$caisi_integrator$dao$FacilityIdIntegerCompositePk = class$("ca.openosp.openo.caisi_integrator.dao.FacilityIdIntegerCompositePk")), (CachedDemographicImage.class$L$B != null) ? CachedDemographicImage.class$L$B : (CachedDemographicImage.class$L$B = class$("[B")), (CachedDemographicImage.class$Ljava$util$Date != null) ? CachedDemographicImage.class$Ljava$util$Date : (CachedDemographicImage.class$Ljava$util$Date = class$("java.util.Date")) };
        CachedDemographicImage.pcFieldFlags = new byte[] { 26, 26, 26 };
        PCRegistry.register((CachedDemographicImage.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicImage != null) ? CachedDemographicImage.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicImage : (CachedDemographicImage.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicImage = class$("ca.openosp.openo.caisi_integrator.dao.CachedDemographicImage")), CachedDemographicImage.pcFieldNames, CachedDemographicImage.pcFieldTypes, CachedDemographicImage.pcFieldFlags, CachedDemographicImage.pcPCSuperclass, "CachedDemographicImage", (PersistenceCapable)new CachedDemographicImage());
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
        this.facilityDemographicPk = null;
        this.image = null;
        this.updateDate = null;
    }
    
    public PersistenceCapable pcNewInstance(final StateManager pcStateManager, final Object o, final boolean b) {
        final CachedDemographicImage cachedDemographicImage = new CachedDemographicImage();
        if (b) {
            cachedDemographicImage.pcClearFields();
        }
        cachedDemographicImage.pcStateManager = pcStateManager;
        cachedDemographicImage.pcCopyKeyFieldsFromObjectId(o);
        return (PersistenceCapable)cachedDemographicImage;
    }
    
    public PersistenceCapable pcNewInstance(final StateManager pcStateManager, final boolean b) {
        final CachedDemographicImage cachedDemographicImage = new CachedDemographicImage();
        if (b) {
            cachedDemographicImage.pcClearFields();
        }
        cachedDemographicImage.pcStateManager = pcStateManager;
        return (PersistenceCapable)cachedDemographicImage;
    }
    
    protected static int pcGetManagedFieldCount() {
        return 3;
    }
    
    public void pcReplaceField(final int n) {
        final int n2 = n - CachedDemographicImage.pcInheritedFieldCount;
        if (n2 < 0) {
            throw new IllegalArgumentException();
        }
        switch (n2) {
            case 0: {
                this.facilityDemographicPk = (FacilityIdIntegerCompositePk)this.pcStateManager.replaceObjectField((PersistenceCapable)this, n);
                return;
            }
            case 1: {
                this.image = (byte[])this.pcStateManager.replaceObjectField((PersistenceCapable)this, n);
                return;
            }
            case 2: {
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
        final int n2 = n - CachedDemographicImage.pcInheritedFieldCount;
        if (n2 < 0) {
            throw new IllegalArgumentException();
        }
        switch (n2) {
            case 0: {
                this.pcStateManager.providedObjectField((PersistenceCapable)this, n, (Object)this.facilityDemographicPk);
                return;
            }
            case 1: {
                this.pcStateManager.providedObjectField((PersistenceCapable)this, n, (Object)this.image);
                return;
            }
            case 2: {
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
    
    protected void pcCopyField(final CachedDemographicImage cachedDemographicImage, final int n) {
        final int n2 = n - CachedDemographicImage.pcInheritedFieldCount;
        if (n2 < 0) {
            throw new IllegalArgumentException();
        }
        switch (n2) {
            case 0: {
                this.facilityDemographicPk = cachedDemographicImage.facilityDemographicPk;
                return;
            }
            case 1: {
                this.image = cachedDemographicImage.image;
                return;
            }
            case 2: {
                this.updateDate = cachedDemographicImage.updateDate;
                return;
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
    }
    
    public void pcCopyFields(final Object o, final int[] array) {
        final CachedDemographicImage cachedDemographicImage = (CachedDemographicImage)o;
        if (cachedDemographicImage.pcStateManager != this.pcStateManager) {
            throw new IllegalArgumentException();
        }
        if (this.pcStateManager == null) {
            throw new IllegalStateException();
        }
        for (int i = 0; i < array.length; ++i) {
            this.pcCopyField(cachedDemographicImage, array[i]);
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
        fieldConsumer.storeObjectField(0 + CachedDemographicImage.pcInheritedFieldCount, ((ObjectId)o).getId());
    }
    
    public void pcCopyKeyFieldsFromObjectId(final Object o) {
        this.facilityDemographicPk = (FacilityIdIntegerCompositePk)((ObjectId)o).getId();
    }
    
    public Object pcNewObjectIdInstance(final Object o) {
        throw new IllegalArgumentException("The id type \"class org.apache.openjpa.util.ObjectId\" specified by persistent type \"class ca.openosp.openo.caisi_integrator.dao.CachedDemographicImage\" does not have a public class org.apache.openjpa.util.ObjectId(String) or class org.apache.openjpa.util.ObjectId(Class, String) constructor.");
    }
    
    public Object pcNewObjectIdInstance() {
        return new ObjectId((CachedDemographicImage.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicImage != null) ? CachedDemographicImage.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicImage : (CachedDemographicImage.class$Lca$openosp$openo$caisi_integrator$dao$CachedDemographicImage = class$("ca.openosp.openo.caisi_integrator.dao.CachedDemographicImage")), (Object)this.facilityDemographicPk);
    }
    
    private static final FacilityIdIntegerCompositePk pcGetfacilityDemographicPk(final CachedDemographicImage cachedDemographicImage) {
        if (cachedDemographicImage.pcStateManager == null) {
            return cachedDemographicImage.facilityDemographicPk;
        }
        cachedDemographicImage.pcStateManager.accessingField(CachedDemographicImage.pcInheritedFieldCount + 0);
        return cachedDemographicImage.facilityDemographicPk;
    }
    
    private static final void pcSetfacilityDemographicPk(final CachedDemographicImage cachedDemographicImage, final FacilityIdIntegerCompositePk facilityDemographicPk) {
        if (cachedDemographicImage.pcStateManager == null) {
            cachedDemographicImage.facilityDemographicPk = facilityDemographicPk;
            return;
        }
        cachedDemographicImage.pcStateManager.settingObjectField((PersistenceCapable)cachedDemographicImage, CachedDemographicImage.pcInheritedFieldCount + 0, (Object)cachedDemographicImage.facilityDemographicPk, (Object)facilityDemographicPk, 0);
    }
    
    private static final byte[] pcGetimage(final CachedDemographicImage cachedDemographicImage) {
        if (cachedDemographicImage.pcStateManager == null) {
            return cachedDemographicImage.image;
        }
        cachedDemographicImage.pcStateManager.accessingField(CachedDemographicImage.pcInheritedFieldCount + 1);
        return cachedDemographicImage.image;
    }
    
    private static final void pcSetimage(final CachedDemographicImage cachedDemographicImage, final byte[] image) {
        if (cachedDemographicImage.pcStateManager == null) {
            cachedDemographicImage.image = image;
            return;
        }
        cachedDemographicImage.pcStateManager.settingObjectField((PersistenceCapable)cachedDemographicImage, CachedDemographicImage.pcInheritedFieldCount + 1, (Object)cachedDemographicImage.image, (Object)image, 0);
    }
    
    private static final Date pcGetupdateDate(final CachedDemographicImage cachedDemographicImage) {
        if (cachedDemographicImage.pcStateManager == null) {
            return cachedDemographicImage.updateDate;
        }
        cachedDemographicImage.pcStateManager.accessingField(CachedDemographicImage.pcInheritedFieldCount + 2);
        return cachedDemographicImage.updateDate;
    }
    
    private static final void pcSetupdateDate(final CachedDemographicImage cachedDemographicImage, final Date updateDate) {
        if (cachedDemographicImage.pcStateManager == null) {
            cachedDemographicImage.updateDate = updateDate;
            return;
        }
        cachedDemographicImage.pcStateManager.settingObjectField((PersistenceCapable)cachedDemographicImage, CachedDemographicImage.pcInheritedFieldCount + 2, (Object)cachedDemographicImage.updateDate, (Object)updateDate, 0);
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
