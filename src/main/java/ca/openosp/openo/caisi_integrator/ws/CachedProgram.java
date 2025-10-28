package ca.openosp.openo.caisi_integrator.ws;

import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cachedProgram", propOrder = { "abstinenceSupport", "address", "alcohol", "bedProgramAffiliated", "description", "email", "emergencyNumber", "facilityIdIntegerCompositePk", "fax", "firstNation", "gender", "housing", "maxAge", "mentalHealth", "minAge", "name", "phone", "physicalHealth", "status", "type", "url" })
public class CachedProgram extends AbstractModel implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected String abstinenceSupport;
    protected String address;
    protected boolean alcohol;
    protected boolean bedProgramAffiliated;
    protected String description;
    protected String email;
    protected String emergencyNumber;
    protected FacilityIdIntegerCompositePk facilityIdIntegerCompositePk;
    protected String fax;
    protected boolean firstNation;
    @XmlSchemaType(name = "string")
    protected Gender gender;
    protected boolean housing;
    protected int maxAge;
    protected boolean mentalHealth;
    protected int minAge;
    protected String name;
    protected String phone;
    protected boolean physicalHealth;
    protected String status;
    protected String type;
    protected String url;
    
    public String getAbstinenceSupport() {
        return this.abstinenceSupport;
    }
    
    public void setAbstinenceSupport(final String abstinenceSupport) {
        this.abstinenceSupport = abstinenceSupport;
    }
    
    public String getAddress() {
        return this.address;
    }
    
    public void setAddress(final String address) {
        this.address = address;
    }
    
    public boolean isAlcohol() {
        return this.alcohol;
    }
    
    public void setAlcohol(final boolean alcohol) {
        this.alcohol = alcohol;
    }
    
    public boolean isBedProgramAffiliated() {
        return this.bedProgramAffiliated;
    }
    
    public void setBedProgramAffiliated(final boolean bedProgramAffiliated) {
        this.bedProgramAffiliated = bedProgramAffiliated;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
    
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(final String email) {
        this.email = email;
    }
    
    public String getEmergencyNumber() {
        return this.emergencyNumber;
    }
    
    public void setEmergencyNumber(final String emergencyNumber) {
        this.emergencyNumber = emergencyNumber;
    }
    
    public FacilityIdIntegerCompositePk getFacilityIdIntegerCompositePk() {
        return this.facilityIdIntegerCompositePk;
    }
    
    public void setFacilityIdIntegerCompositePk(final FacilityIdIntegerCompositePk facilityIdIntegerCompositePk) {
        this.facilityIdIntegerCompositePk = facilityIdIntegerCompositePk;
    }
    
    public String getFax() {
        return this.fax;
    }
    
    public void setFax(final String fax) {
        this.fax = fax;
    }
    
    public boolean isFirstNation() {
        return this.firstNation;
    }
    
    public void setFirstNation(final boolean firstNation) {
        this.firstNation = firstNation;
    }
    
    public Gender getGender() {
        return this.gender;
    }
    
    public void setGender(final Gender gender) {
        this.gender = gender;
    }
    
    public boolean isHousing() {
        return this.housing;
    }
    
    public void setHousing(final boolean housing) {
        this.housing = housing;
    }
    
    public int getMaxAge() {
        return this.maxAge;
    }
    
    public void setMaxAge(final int maxAge) {
        this.maxAge = maxAge;
    }
    
    public boolean isMentalHealth() {
        return this.mentalHealth;
    }
    
    public void setMentalHealth(final boolean mentalHealth) {
        this.mentalHealth = mentalHealth;
    }
    
    public int getMinAge() {
        return this.minAge;
    }
    
    public void setMinAge(final int minAge) {
        this.minAge = minAge;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getPhone() {
        return this.phone;
    }
    
    public void setPhone(final String phone) {
        this.phone = phone;
    }
    
    public boolean isPhysicalHealth() {
        return this.physicalHealth;
    }
    
    public void setPhysicalHealth(final boolean physicalHealth) {
        this.physicalHealth = physicalHealth;
    }
    
    public String getStatus() {
        return this.status;
    }
    
    public void setStatus(final String status) {
        this.status = status;
    }
    
    public String getType() {
        return this.type;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    public String getUrl() {
        return this.url;
    }
    
    public void setUrl(final String url) {
        this.url = url;
    }
}
