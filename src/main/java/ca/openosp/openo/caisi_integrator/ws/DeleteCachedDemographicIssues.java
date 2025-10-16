package ca.openosp.openo.caisi_integrator.ws;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "deleteCachedDemographicIssues", propOrder = { "arg0", "arg1" })
public class DeleteCachedDemographicIssues implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected Integer arg0;
    protected List<FacilityIdDemographicIssueCompositePk> arg1;
    
    public Integer getArg0() {
        return this.arg0;
    }
    
    public void setArg0(final Integer arg0) {
        this.arg0 = arg0;
    }
    
    public List<FacilityIdDemographicIssueCompositePk> getArg1() {
        if (this.arg1 == null) {
            this.arg1 = new ArrayList<FacilityIdDemographicIssueCompositePk>();
        }
        return this.arg1;
    }
}
