package ca.openosp.openo.PMmodule.caisi_integrator;

import java.util.Date;
import java.io.Serializable;

public class IntegratorFileHeader implements Serializable
{
    private Date lastDate;
    private Date date;
    private String dependsOn;
    private Integer cachedFacilityId;
    private String cachedFacilityName;
    private String username;
    public static final int VERSION = 1;
    
    public Date getDate() {
        return this.date;
    }
    
    public void setDate(final Date date) {
        this.date = date;
    }
    
    public Date getLastDate() {
        return this.lastDate;
    }
    
    public void setLastDate(final Date lastDate) {
        this.lastDate = lastDate;
    }
    
    public String getDependsOn() {
        return this.dependsOn;
    }
    
    public void setDependsOn(final String dependsOn) {
        this.dependsOn = dependsOn;
    }
    
    public int getVersion() {
        return 1;
    }
    
    public Integer getCachedFacilityId() {
        return this.cachedFacilityId;
    }
    
    public void setCachedFacilityId(final Integer cachedFacilityId) {
        this.cachedFacilityId = cachedFacilityId;
    }
    
    public String getCachedFacilityName() {
        return this.cachedFacilityName;
    }
    
    public void setCachedFacilityName(final String cachedFacilityName) {
        this.cachedFacilityName = cachedFacilityName;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public void setUsername(final String username) {
        this.username = username;
    }
}
