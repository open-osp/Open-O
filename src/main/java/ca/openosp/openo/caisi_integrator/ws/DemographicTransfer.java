package ca.openosp.openo.caisi_integrator.ws;

import javax.xml.bind.annotation.XmlSchemaType;
import org.w3._2001.xmlschema.Adapter1;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.XmlElement;
import java.util.Calendar;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "demographicTransfer", propOrder = { "birthDate", "caisiDemographicId", "caisiProviderId", "city", "firstName", "gender", "hin", "hinType", "hinValidEnd", "hinValidStart", "hinVersion", "integratorFacilityId", "lastName", "lastUpdateDate", "lastUpdateUser", "phone1", "phone2", "photo", "photoUpdateDate", "province", "removeId", "sin", "streetAddress" })
public class DemographicTransfer implements Serializable
{
    private static final long serialVersionUID = 1L;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1.class)
    @XmlSchemaType(name = "dateTime")
    protected Calendar birthDate;
    protected int caisiDemographicId;
    protected String caisiProviderId;
    protected String city;
    protected String firstName;
    @XmlSchemaType(name = "string")
    protected Gender gender;
    protected String hin;
    protected String hinType;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1.class)
    @XmlSchemaType(name = "dateTime")
    protected Calendar hinValidEnd;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1.class)
    @XmlSchemaType(name = "dateTime")
    protected Calendar hinValidStart;
    protected String hinVersion;
    protected Integer integratorFacilityId;
    protected String lastName;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1.class)
    @XmlSchemaType(name = "dateTime")
    protected Calendar lastUpdateDate;
    protected String lastUpdateUser;
    protected String phone1;
    protected String phone2;
    protected byte[] photo;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1.class)
    @XmlSchemaType(name = "dateTime")
    protected Calendar photoUpdateDate;
    protected String province;
    protected boolean removeId;
    protected String sin;
    protected String streetAddress;
    
    public Calendar getBirthDate() {
        return this.birthDate;
    }
    
    public void setBirthDate(final Calendar birthDate) {
        this.birthDate = birthDate;
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
    
    public String getCity() {
        return this.city;
    }
    
    public void setCity(final String city) {
        this.city = city;
    }
    
    public String getFirstName() {
        return this.firstName;
    }
    
    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }
    
    public Gender getGender() {
        return this.gender;
    }
    
    public void setGender(final Gender gender) {
        this.gender = gender;
    }
    
    public String getHin() {
        return this.hin;
    }
    
    public void setHin(final String hin) {
        this.hin = hin;
    }
    
    public String getHinType() {
        return this.hinType;
    }
    
    public void setHinType(final String hinType) {
        this.hinType = hinType;
    }
    
    public Calendar getHinValidEnd() {
        return this.hinValidEnd;
    }
    
    public void setHinValidEnd(final Calendar hinValidEnd) {
        this.hinValidEnd = hinValidEnd;
    }
    
    public Calendar getHinValidStart() {
        return this.hinValidStart;
    }
    
    public void setHinValidStart(final Calendar hinValidStart) {
        this.hinValidStart = hinValidStart;
    }
    
    public String getHinVersion() {
        return this.hinVersion;
    }
    
    public void setHinVersion(final String hinVersion) {
        this.hinVersion = hinVersion;
    }
    
    public Integer getIntegratorFacilityId() {
        return this.integratorFacilityId;
    }
    
    public void setIntegratorFacilityId(final Integer integratorFacilityId) {
        this.integratorFacilityId = integratorFacilityId;
    }
    
    public String getLastName() {
        return this.lastName;
    }
    
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }
    
    public Calendar getLastUpdateDate() {
        return this.lastUpdateDate;
    }
    
    public void setLastUpdateDate(final Calendar lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
    
    public String getLastUpdateUser() {
        return this.lastUpdateUser;
    }
    
    public void setLastUpdateUser(final String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
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
    
    public byte[] getPhoto() {
        return this.photo;
    }
    
    public void setPhoto(final byte[] photo) {
        this.photo = photo;
    }
    
    public Calendar getPhotoUpdateDate() {
        return this.photoUpdateDate;
    }
    
    public void setPhotoUpdateDate(final Calendar photoUpdateDate) {
        this.photoUpdateDate = photoUpdateDate;
    }
    
    public String getProvince() {
        return this.province;
    }
    
    public void setProvince(final String province) {
        this.province = province;
    }
    
    public boolean isRemoveId() {
        return this.removeId;
    }
    
    public void setRemoveId(final boolean removeId) {
        this.removeId = removeId;
    }
    
    public String getSin() {
        return this.sin;
    }
    
    public void setSin(final String sin) {
        this.sin = sin;
    }
    
    public String getStreetAddress() {
        return this.streetAddress;
    }
    
    public void setStreetAddress(final String streetAddress) {
        this.streetAddress = streetAddress;
    }
}
