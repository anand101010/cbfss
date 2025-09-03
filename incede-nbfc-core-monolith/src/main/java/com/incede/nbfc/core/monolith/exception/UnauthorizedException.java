package com.incede.nbfc.core.monolith.exception;

/**
 * Exception thrown when a user is not authenticated.
 * 
 * Used for cases where the user needs to provide valid
 * authentication credentials.
 * 
 * @author Incede NBFC Development Team
 * @version 1.0.0
 */
public class UnauthorizedException extends RuntimeException {

    private final String requiredAction;

    public UnauthorizedException(String message) {
        super(message);
        this.requiredAction = null;
    }

    public UnauthorizedException(String message, String requiredAction) {
        super(message);
        this.requiredAction = requiredAction;
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
        this.requiredAction = null;
    }

    public UnauthorizedException(String message, String requiredAction, Throwable cause) {
        super(message, cause);
        this.requiredAction = requiredAction;
    }

    public String getRequiredAction() {
        return requiredAction;
    }
} 