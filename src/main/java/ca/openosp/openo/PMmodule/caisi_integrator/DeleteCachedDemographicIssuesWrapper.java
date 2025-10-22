package ca.openosp.openo.PMmodule.caisi_integrator;

import ca.openosp.openo.caisi_integrator.dao.FacilityIdDemographicIssueCompositePk;
import java.util.List;
import java.io.Serializable;

public class DeleteCachedDemographicIssuesWrapper implements Serializable
{
    private Integer demographicNo;
    private List<FacilityIdDemographicIssueCompositePk> keys;
    
    public DeleteCachedDemographicIssuesWrapper() {
    }
    
    public DeleteCachedDemographicIssuesWrapper(final Integer demographicNo, final List<FacilityIdDemographicIssueCompositePk> keys) {
        this.demographicNo = demographicNo;
        this.keys = keys;
    }
    
    public Integer getDemographicNo() {
        return this.demographicNo;
    }
    
    public void setDemographicNo(final Integer demographicNo) {
        this.demographicNo = demographicNo;
    }
    
    public List<FacilityIdDemographicIssueCompositePk> getKeys() {
        return this.keys;
    }
    
    public void setKeys(final List<FacilityIdDemographicIssueCompositePk> keys) {
        this.keys = keys;
    }
}
