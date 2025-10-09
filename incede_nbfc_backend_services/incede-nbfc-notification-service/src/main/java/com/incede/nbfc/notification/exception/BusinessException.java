package com.incede.nbfc.notification.exception;

/**
 * Exception thrown when business logic rules are violated.
 * 
 * Used for cases where the request is syntactically correct
 * but violates business constraints or rules.
 * 
 * @author Incede NBFC Development Team
 * @version 1.0.0
 */
public class BusinessException extends RuntimeException {

    private final String errorCode;

    public BusinessException(String message) {
        super(message);
        this.errorCode = null;
    }

    public BusinessException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = null;
    }

    public BusinessException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
} 