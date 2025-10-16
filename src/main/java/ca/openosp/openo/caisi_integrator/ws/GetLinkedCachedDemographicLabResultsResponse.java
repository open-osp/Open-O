package ca.openosp.openo.caisi_integrator.ws;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getLinkedCachedDemographicLabResultsResponse", propOrder = { "_return" })
public class GetLinkedCachedDemographicLabResultsResponse implements Serializable
{
    private static final long serialVersionUID = 1L;
    @XmlElement(name = "return")
    protected List<CachedDemographicLabResult> _return;
    
    public List<CachedDemographicLabResult> getReturn() {
        if (this._return == null) {
            this._return = new ArrayList<CachedDemographicLabResult>();
        }
        return this._return;
    }
}
