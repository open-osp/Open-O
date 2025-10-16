package ca.openosp.openo.caisi_integrator.ws;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "setCachedEformData", propOrder = { "arg0" })
public class SetCachedEformData implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected List<CachedEformData> arg0;
    
    public List<CachedEformData> getArg0() {
        if (this.arg0 == null) {
            this.arg0 = new ArrayList<CachedEformData>();
        }
        return this.arg0;
    }
}
