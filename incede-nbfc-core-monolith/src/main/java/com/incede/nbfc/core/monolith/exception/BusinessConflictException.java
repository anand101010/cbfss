package com.incede.nbfc.core.monolith.exception;

import java.util.List;
import java.util.Map;

public class BusinessConflictException extends RuntimeException {

    private final String errorCode;
    private final String existingIdentity;
    private final Map<String, Object> existingDetails;
    private final List<String> suggestedActions;

    public BusinessConflictException(String message, String errorCode, String existingIdentity,
                                     Map<String, Object> existingDetails, List<String> suggestedActions) {
        super(message);
        this.errorCode = errorCode;
        this.existingIdentity = existingIdentity;
        this.existingDetails = existingDetails;
        this.suggestedActions = suggestedActions;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getExistingIdentity() {
        return existingIdentity;
    }

    public Map<String, Object> getExistingDetails() {
        return existingDetails;
    }

    public List<String> getSuggestedActions() {
        return suggestedActions;
    }
}
