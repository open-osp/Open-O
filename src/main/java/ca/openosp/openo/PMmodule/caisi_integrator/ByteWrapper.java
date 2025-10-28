package ca.openosp.openo.PMmodule.caisi_integrator;

import java.io.Serializable;

public class ByteWrapper implements Serializable
{
    private byte[] data;
    
    public ByteWrapper() {
    }
    
    public ByteWrapper(final byte[] data) {
        this.data = data;
    }
    
    public byte[] getData() {
        return this.data;
    }
    
    public void setData(final byte[] data) {
        this.data = data;
    }
}
