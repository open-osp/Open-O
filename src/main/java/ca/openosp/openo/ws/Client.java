package ca.openosp.openo.ws;

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
@XmlType(name = "client", propOrder = { "birthDate", "city", "firstName", "gender", "hidden", "hiddenChangeDate", "hin", "hinType", "hinValidEnd", "hinValidStart", "hinVersion", "image", "lastName", "linkingId", "lockbox", "province", "sin", "streetAddress", "updated", "updatedBy" })
public class Client extends AbstractModel implements Serializable
{
    private static final long serialVersionUID = 1L;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1.class)
    @XmlSchemaType(name = "dateTime")
    protected Calendar birthDate;
    protected String city;
    protected String firstName;
    @XmlSchemaType(name = "string")
    protected Gender gender;
    protected boolean hidden;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1.class)
    @XmlSchemaType(name = "dateTime")
    protected Calendar hiddenChangeDate;
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
    protected byte[] image;
    protected String lastName;
    protected Integer linkingId;
    protected boolean lockbox;
    protected String province;
    protected String sin;
    protected String streetAddress;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1.class)
    @XmlSchemaType(name = "dateTime")
    protected Calendar updated;
    protected String updatedBy;
    
    public Calendar getBirthDate() {
        return this.birthDate;
    }
    
    public void setBirthDate(final Calendar birthDate) {
        this.birthDate = birthDate;
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
    
    public boolean isHidden() {
        return this.hidden;
    }
    
    public void setHidden(final boolean hidden) {
        this.hidden = hidden;
    }
    
    public Calendar getHiddenChangeDate() {
        return this.hiddenChangeDate;
    }
    
    public void setHiddenChangeDate(final Calendar hiddenChangeDate) {
        this.hiddenChangeDate = hiddenChangeDate;
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
    
    public byte[] getImage() {
        return this.image;
    }
    
    public void setImage(final byte[] image) {
        this.image = image;
    }
    
    public String getLastName() {
        return this.lastName;
    }
    
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }
    
    public Integer getLinkingId() {
        return this.linkingId;
    }
    
    public void setLinkingId(final Integer linkingId) {
        this.linkingId = linkingId;
    }
    
    public boolean isLockbox() {
        return this.lockbox;
    }
    
    public void setLockbox(final boolean lockbox) {
        this.lockbox = lockbox;
    }
    
    public String getProvince() {
        return this.province;
    }
    
    public void setProvince(final String province) {
        this.province = province;
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
    
    public Calendar getUpdated() {
        return this.updated;
    }
    
    public void setUpdated(final Calendar updated) {
        this.updated = updated;
    }
    
    public String getUpdatedBy() {
        return this.updatedBy;
    }
    
    public void setUpdatedBy(final String updatedBy) {
        this.updatedBy = updatedBy;
    }
}
