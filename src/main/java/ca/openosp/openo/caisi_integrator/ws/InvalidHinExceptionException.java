package ca.openosp.openo.caisi_integrator.ws;

import ca.openosp.openo.ws.InvalidHinException;
import javax.xml.ws.WebFault;

@WebFault(name = "InvalidHinException", targetNamespace = "http://ws.hnr.oscarehr.org/")
public class InvalidHinExceptionException extends Exception
{
    private InvalidHinException invalidHinException;
    
    public InvalidHinExceptionException() {
    }
    
    public InvalidHinExceptionException(final String message) {
        super(message);
    }
    
    public InvalidHinExceptionException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public InvalidHinExceptionException(final String message, final InvalidHinException invalidHinException) {
        super(message);
        this.invalidHinException = invalidHinException;
    }
    
    public InvalidHinExceptionException(final String message, final InvalidHinException invalidHinException, final Throwable cause) {
        super(message, cause);
        this.invalidHinException = invalidHinException;
    }
    
    public InvalidHinException getFaultInfo() {
        return this.invalidHinException;
    }
}
