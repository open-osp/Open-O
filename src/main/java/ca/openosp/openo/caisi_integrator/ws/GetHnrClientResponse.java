package ca.openosp.openo.caisi_integrator.ws;

import javax.xml.bind.annotation.XmlElement;
import ca.openosp.openo.ws.Client;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getHnrClientResponse", propOrder = { "_return" })
public class GetHnrClientResponse implements Serializable
{
    private static final long serialVersionUID = 1L;
    @XmlElement(name = "return")
    protected Client _return;
    
    public Client getReturn() {
        return this._return;
    }
    
    public void setReturn(final Client return1) {
        this._return = return1;
    }
}
