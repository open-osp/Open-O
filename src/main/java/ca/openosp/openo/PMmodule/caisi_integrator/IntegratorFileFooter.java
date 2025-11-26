package ca.openosp.openo.PMmodule.caisi_integrator;

import java.io.Serializable;

public class IntegratorFileFooter implements Serializable
{
    private int checksum;
    
    public IntegratorFileFooter() {
        this.checksum = -1;
    }
    
    public IntegratorFileFooter(final int checksum) {
        this.checksum = checksum;
    }
    
    public int getChecksum() {
        return this.checksum;
    }
    
    public void setChecksum(final int checksum) {
        this.checksum = checksum;
    }
}
