package ca.openosp.openo.commn.exception;

/**
 * This exception is thrown when a user session is not found in the registry.
 */
public class UserSessionNotFoundException extends IllegalArgumentException {

    public UserSessionNotFoundException(String message) {
        super(message);
    }

}
