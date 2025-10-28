package ca.openosp.openo.caisi_integrator.ws;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "setCachedBillingOnItem", propOrder = { "arg0" })
public class SetCachedBillingOnItem implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected List<CachedBillingOnItem> arg0;
    
    public List<CachedBillingOnItem> getArg0() {
        if (this.arg0 == null) {
            this.arg0 = new ArrayList<CachedBillingOnItem>();
        }
        return this.arg0;
    }
}
