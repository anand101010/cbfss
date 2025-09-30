package com.incede.nbfc.core.monolith.exception;

/**
 * Exception thrown when a business conflict occurs,
 * such as attempting to create a duplicate resource.
 *
 * This extends BusinessException and carries additional
 * context (details) that can be returned in the response.
 *
 * Example: Duplicate customer KYC document.
 *
 * @author Incede NBFC
 * @version 1.0.0
 */
public class BusinessConflictException extends BusinessException {

    private final Object details;

    public BusinessConflictException(String message, String errorCode, Object details) {
        super(message, errorCode);
        this.details = details;
    }

    public Object getDetails() {
        return details;
    }
}
