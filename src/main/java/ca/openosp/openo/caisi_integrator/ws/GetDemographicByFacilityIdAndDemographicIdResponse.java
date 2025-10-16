package ca.openosp.openo.caisi_integrator.ws;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getDemographicByFacilityIdAndDemographicIdResponse", propOrder = { "_return" })
public class GetDemographicByFacilityIdAndDemographicIdResponse implements Serializable
{
    private static final long serialVersionUID = 1L;
    @XmlElement(name = "return")
    protected DemographicTransfer _return;
    
    public DemographicTransfer getReturn() {
        return this._return;
    }
    
    public void setReturn(final DemographicTransfer return1) {
        this._return = return1;
    }
}
