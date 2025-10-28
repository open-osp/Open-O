package ca.openosp.openo.caisi_integrator.ws;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "facilityIdIntegerCompositePk", propOrder = { "caisiItemId", "integratorFacilityId" })
public class FacilityIdIntegerCompositePk implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected Integer caisiItemId;
    protected Integer integratorFacilityId;
    
    public Integer getCaisiItemId() {
        return this.caisiItemId;
    }
    
    public void setCaisiItemId(final Integer caisiItemId) {
        this.caisiItemId = caisiItemId;
    }
    
    public Integer getIntegratorFacilityId() {
        return this.integratorFacilityId;
    }
    
    public void setIntegratorFacilityId(final Integer integratorFacilityId) {
        this.integratorFacilityId = integratorFacilityId;
    }
}
