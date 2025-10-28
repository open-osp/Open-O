package ca.openosp.openo.caisi_integrator.ws;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getLinkedCachedDemographicIssuesByDemographicIdResponse", propOrder = { "_return" })
public class GetLinkedCachedDemographicIssuesByDemographicIdResponse implements Serializable
{
    private static final long serialVersionUID = 1L;
    @XmlElement(name = "return")
    protected List<CachedDemographicIssue> _return;
    
    public List<CachedDemographicIssue> getReturn() {
        if (this._return == null) {
            this._return = new ArrayList<CachedDemographicIssue>();
        }
        return this._return;
    }
}
