package ca.openosp.openo.PMmodule.caisi_integrator;

import java.util.List;
import java.io.Serializable;

public class ProgramDeleteIdWrapper implements Serializable
{
    private List<Integer> ids;
    
    public ProgramDeleteIdWrapper() {
        this.ids = null;
    }
    
    public ProgramDeleteIdWrapper(final List<Integer> ids) {
        this.ids = ids;
    }
    
    public List<Integer> getIds() {
        return this.ids;
    }
    
    public void setIds(final List<Integer> ids) {
        this.ids = ids;
    }
}
