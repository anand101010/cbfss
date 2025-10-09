package com.incede.nbfc.notification.exception;

/**
 * Exception thrown when a user is authenticated but not authorized.
 * 
 * Used for cases where the user has valid credentials but lacks
 * the required permissions to access a resource or perform an action.
 * 
 * @author Incede NBFC Development Team
 * @version 1.0.0
 */
public class ForbiddenException extends RuntimeException {

    private final String requiredPermission;
    private final String resource;

    public ForbiddenException(String message) {
        super(message);
        this.requiredPermission = null;
        this.resource = null;
    }

    public ForbiddenException(String message, String requiredPermission) {
        super(message);
        this.requiredPermission = requiredPermission;
        this.resource = null;
    }

    public ForbiddenException(String message, String requiredPermission, String resource) {
        super(message);
        this.requiredPermission = requiredPermission;
        this.resource = resource;
    }

    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
        this.requiredPermission = null;
        this.resource = null;
    }

    public ForbiddenException(String message, String requiredPermission, String resource, Throwable cause) {
        super(message, cause);
        this.requiredPermission = requiredPermission;
        this.resource = resource;
    }

    public String getRequiredPermission() {
        return requiredPermission;
    }

    public String getResource() {
        return resource;
    }
} 