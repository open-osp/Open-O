package ca.openosp.openo.caisi_integrator.ws;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getLinkedCachedDemographicForms", propOrder = { "arg0", "arg1" })
public class GetLinkedCachedDemographicForms implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected Integer arg0;
    protected String arg1;
    
    public Integer getArg0() {
        return this.arg0;
    }
    
    public void setArg0(final Integer arg0) {
        this.arg0 = arg0;
    }
    
    public String getArg1() {
        return this.arg1;
    }
    
    public void setArg1(final String arg1) {
        this.arg1 = arg1;
    }
}
