package ca.openosp.openo.caisi_integrator.ws;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "unLinkDemographics", propOrder = { "caisiDemographicIdAtCurrentFacility", "integratorDemographicFacilityIdOnIntegrator", "caisiDemographicIdOnIntegrator" })
public class UnLinkDemographics implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected Integer caisiDemographicIdAtCurrentFacility;
    protected Integer integratorDemographicFacilityIdOnIntegrator;
    protected Integer caisiDemographicIdOnIntegrator;
    
    public Integer getCaisiDemographicIdAtCurrentFacility() {
        return this.caisiDemographicIdAtCurrentFacility;
    }
    
    public void setCaisiDemographicIdAtCurrentFacility(final Integer caisiDemographicIdAtCurrentFacility) {
        this.caisiDemographicIdAtCurrentFacility = caisiDemographicIdAtCurrentFacility;
    }
    
    public Integer getIntegratorDemographicFacilityIdOnIntegrator() {
        return this.integratorDemographicFacilityIdOnIntegrator;
    }
    
    public void setIntegratorDemographicFacilityIdOnIntegrator(final Integer integratorDemographicFacilityIdOnIntegrator) {
        this.integratorDemographicFacilityIdOnIntegrator = integratorDemographicFacilityIdOnIntegrator;
    }
    
    public Integer getCaisiDemographicIdOnIntegrator() {
        return this.caisiDemographicIdOnIntegrator;
    }
    
    public void setCaisiDemographicIdOnIntegrator(final Integer caisiDemographicIdOnIntegrator) {
        this.caisiDemographicIdOnIntegrator = caisiDemographicIdOnIntegrator;
    }
}
