package ca.openosp.openo.caisi_integrator.ws;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "errorImportLog", propOrder = { "arg0" })
public class ErrorImportLog implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected Integer arg0;
    
    public Integer getArg0() {
        return this.arg0;
    }
    
    public void setArg0(final Integer arg0) {
        this.arg0 = arg0;
    }
}
