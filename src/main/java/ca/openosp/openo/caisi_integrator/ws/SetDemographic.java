package ca.openosp.openo.caisi_integrator.ws;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "setDemographic", propOrder = { "arg0" })
public class SetDemographic implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected DemographicTransfer arg0;
    
    public DemographicTransfer getArg0() {
        return this.arg0;
    }
    
    public void setArg0(final DemographicTransfer arg0) {
        this.arg0 = arg0;
    }
}
