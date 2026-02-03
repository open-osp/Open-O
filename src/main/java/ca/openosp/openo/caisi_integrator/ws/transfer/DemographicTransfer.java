package ca.openosp.openo.caisi_integrator.ws.transfer;

import ca.openosp.openo.caisi_integrator.dao.CachedDemographic;
import java.util.Date;
import java.io.Serializable;

public class DemographicTransfer implements Serializable
{
    private static final long serialVersionUID = 570194986641348591L;
    private Integer integratorFacilityId;
    private int caisiDemographicId;
    private String caisiProviderId;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private CachedDemographic.Gender gender;
    private String hin;
    private String hinType;
    private String hinVersion;
    private Date hinValidStart;
    private Date hinValidEnd;
    private String sin;
    private String province;
    private String city;
    private String streetAddress;
    private String phone1;
    private String phone2;
    private String lastUpdateUser;
    private Date lastUpdateDate;
    private Date photoUpdateDate;
    private byte[] photo;
    private boolean removeId;
    
    public DemographicTransfer() {
        this.integratorFacilityId = null;
        this.caisiDemographicId = 0;
        this.caisiProviderId = null;
        this.firstName = null;
        this.lastName = null;
        this.birthDate = null;
        this.gender = null;
        this.hin = null;
        this.hinType = null;
        this.hinVersion = null;
        this.hinValidStart = null;
        this.hinValidEnd = null;
        this.sin = null;
        this.province = null;
        this.city = null;
        this.photoUpdateDate = null;
        this.photo = null;
        this.removeId = false;
    }
    
    public Integer getIntegratorFacilityId() {
        return this.integratorFacilityId;
    }
    
    public void setIntegratorFacilityId(final Integer integratorFacilityId) {
        this.integratorFacilityId = integratorFacilityId;
    }
    
    public int getCaisiDemographicId() {
        return this.caisiDemographicId;
    }
    
    public void setCaisiDemographicId(final int caisiDemographicId) {
        this.caisiDemographicId = caisiDemographicId;
    }
    
    public String getCaisiProviderId() {
        return this.caisiProviderId;
    }
    
    public void setCaisiProviderId(final String caisiProviderId) {
        this.caisiProviderId = caisiProviderId;
    }
    
    public String getFirstName() {
        return this.firstName;
    }
    
    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return this.lastName;
    }
    
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }
    
    public Date getBirthDate() {
        return this.birthDate;
    }
    
    public void setBirthDate(final Date birthDate) {
        this.birthDate = birthDate;
    }
    
    public CachedDemographic.Gender getGender() {
        return this.gender;
    }
    
    public void setGender(final CachedDemographic.Gender gender) {
        this.gender = gender;
    }
    
    public String getHin() {
        return this.hin;
    }
    
    public void setHin(final String hin) {
        this.hin = hin;
    }
    
    public String getHinVersion() {
        return this.hinVersion;
    }
    
    public void setHinVersion(final String hinVersion) {
        this.hinVersion = hinVersion;
    }
    
    public String getSin() {
        return this.sin;
    }
    
    public void setSin(final String sin) {
        this.sin = sin;
    }
    
    public String getProvince() {
        return this.province;
    }
    
    public void setProvince(final String province) {
        this.province = province;
    }
    
    public String getCity() {
        return this.city;
    }
    
    public void setCity(final String city) {
        this.city = city;
    }
    
    public Date getPhotoUpdateDate() {
        return this.photoUpdateDate;
    }
    
    public void setPhotoUpdateDate(final Date photoUpdateDate) {
        this.photoUpdateDate = photoUpdateDate;
    }
    
    public byte[] getPhoto() {
        return this.photo;
    }
    
    public void setPhoto(final byte[] photo) {
        this.photo = photo;
    }
    
    public String getHinType() {
        return this.hinType;
    }
    
    public void setHinType(final String hinType) {
        this.hinType = hinType;
    }
    
    public boolean getRemoveId() {
        return this.removeId;
    }
    
    public void setRemoveId(final boolean removeId) {
        this.removeId = removeId;
    }
    
    public String getStreetAddress() {
        return this.streetAddress;
    }
    
    public void setStreetAddress(final String streetAddress) {
        this.streetAddress = streetAddress;
    }
    
    public String getPhone1() {
        return this.phone1;
    }
    
    public void setPhone1(final String phone1) {
        this.phone1 = phone1;
    }
    
    public String getPhone2() {
        return this.phone2;
    }
    
    public void setPhone2(final String phone2) {
        this.phone2 = phone2;
    }
    
    public Date getHinValidStart() {
        return this.hinValidStart;
    }
    
    public void setHinValidStart(final Date hinValidStart) {
        this.hinValidStart = hinValidStart;
    }
    
    public Date getHinValidEnd() {
        return this.hinValidEnd;
    }
    
    public void setHinValidEnd(final Date hinValidEnd) {
        this.hinValidEnd = hinValidEnd;
    }
    
    public String getLastUpdateUser() {
        return this.lastUpdateUser;
    }
    
    public void setLastUpdateUser(final String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }
    
    public Date getLastUpdateDate() {
        return this.lastUpdateDate;
    }
    
    public void setLastUpdateDate(final Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
}
