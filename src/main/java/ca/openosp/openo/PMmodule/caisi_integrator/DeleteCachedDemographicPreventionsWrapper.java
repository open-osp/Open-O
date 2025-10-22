package ca.openosp.openo.PMmodule.caisi_integrator;

import java.util.List;
import java.io.Serializable;

public class DeleteCachedDemographicPreventionsWrapper implements Serializable
{
    private Integer demographicNo;
    private List<Integer> nonDeletedIds;
    
    public DeleteCachedDemographicPreventionsWrapper() {
    }
    
    public DeleteCachedDemographicPreventionsWrapper(final Integer demographicNo, final List<Integer> nonDeletedIds) {
        this.demographicNo = demographicNo;
        this.nonDeletedIds = nonDeletedIds;
    }
    
    public Integer getDemographicNo() {
        return this.demographicNo;
    }
    
    public void setDemographicNo(final Integer demographicNo) {
        this.demographicNo = demographicNo;
    }
    
    public List<Integer> getNonDeletedIds() {
        return this.nonDeletedIds;
    }
    
    public void setNonDeletedIds(final List<Integer> nonDeletedIds) {
        this.nonDeletedIds = nonDeletedIds;
    }
}
