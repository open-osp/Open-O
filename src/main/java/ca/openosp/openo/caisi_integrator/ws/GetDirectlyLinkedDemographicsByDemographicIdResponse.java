package ca.openosp.openo.caisi_integrator.ws;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getDirectlyLinkedDemographicsByDemographicIdResponse", propOrder = { "_return" })
public class GetDirectlyLinkedDemographicsByDemographicIdResponse implements Serializable
{
    private static final long serialVersionUID = 1L;
    @XmlElement(name = "return")
    protected List<DemographicTransfer> _return;
    
    public List<DemographicTransfer> getReturn() {
        if (this._return == null) {
            this._return = new ArrayList<DemographicTransfer>();
        }
        return this._return;
    }
}
