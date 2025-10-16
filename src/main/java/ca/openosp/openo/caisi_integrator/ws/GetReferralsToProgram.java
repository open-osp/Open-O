package ca.openosp.openo.caisi_integrator.ws;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getReferralsToProgram", propOrder = { "destinationCaisiProgramId" })
public class GetReferralsToProgram implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected Integer destinationCaisiProgramId;
    
    public Integer getDestinationCaisiProgramId() {
        return this.destinationCaisiProgramId;
    }
    
    public void setDestinationCaisiProgramId(final Integer destinationCaisiProgramId) {
        this.destinationCaisiProgramId = destinationCaisiProgramId;
    }
}
