package ca.openosp.openo.caisi_integrator.ws;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cachedDemographicLabResult", propOrder = { "caisiDemographicId", "data", "facilityIdLabResultCompositePk", "type" })
public class CachedDemographicLabResult extends AbstractModel implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected int caisiDemographicId;
    protected String data;
    protected FacilityIdLabResultCompositePk facilityIdLabResultCompositePk;
    protected String type;
    
    public int getCaisiDemographicId() {
        return this.caisiDemographicId;
    }
    
    public void setCaisiDemographicId(final int caisiDemographicId) {
        this.caisiDemographicId = caisiDemographicId;
    }
    
    public String getData() {
        return this.data;
    }
    
    public void setData(final String data) {
        this.data = data;
    }
    
    public FacilityIdLabResultCompositePk getFacilityIdLabResultCompositePk() {
        return this.facilityIdLabResultCompositePk;
    }
    
    public void setFacilityIdLabResultCompositePk(final FacilityIdLabResultCompositePk facilityIdLabResultCompositePk) {
        this.facilityIdLabResultCompositePk = facilityIdLabResultCompositePk;
    }
    
    public String getType() {
        return this.type;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
}
