package ca.openosp.openo.caisi_integrator.ws;

import ca.openosp.openo.ws.DuplicateHinException;
import javax.xml.ws.WebFault;

@WebFault(name = "DuplicateHinException", targetNamespace = "http://ws.hnr.oscarehr.org/")
public class DuplicateHinExceptionException extends Exception
{
    private DuplicateHinException duplicateHinException;
    
    public DuplicateHinExceptionException() {
    }
    
    public DuplicateHinExceptionException(final String message) {
        super(message);
    }
    
    public DuplicateHinExceptionException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public DuplicateHinExceptionException(final String message, final DuplicateHinException duplicateHinException) {
        super(message);
        this.duplicateHinException = duplicateHinException;
    }
    
    public DuplicateHinExceptionException(final String message, final DuplicateHinException duplicateHinException, final Throwable cause) {
        super(message, cause);
        this.duplicateHinException = duplicateHinException;
    }
    
    public DuplicateHinException getFaultInfo() {
        return this.duplicateHinException;
    }
}
