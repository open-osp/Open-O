package ca.openosp.openo.caisi_integrator.ws;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "removeReferral", propOrder = { "referralId" })
public class RemoveReferral implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected Integer referralId;
    
    public Integer getReferralId() {
        return this.referralId;
    }
    
    public void setReferralId(final Integer referralId) {
        this.referralId = referralId;
    }
}
