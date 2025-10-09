package com.incede.nbfc.notification.exception;

/**
 * Standardized error codes for Incede NBFC Core Monolith Service.
 * 
 * Provides consistent error codes that can be used by clients
 * to handle specific error scenarios programmatically.
 * 
 * @author Incede NBFC Development Team
 * @version 1.0.0
 */
public final class ErrorCodes {

    // 4xx Client Errors (HTTP Status Codes)
    public static final String BAD_REQUEST = "400";
    public static final String UNAUTHORIZED = "401";
    public static final String FORBIDDEN = "403";
    public static final String NOT_FOUND = "404";
    public static final String METHOD_NOT_ALLOWED = "405";
    public static final String NOT_ACCEPTABLE = "406";
    public static final String CONFLICT = "409";
    public static final String UNSUPPORTED_MEDIA_TYPE = "415";
    public static final String UNPROCESSABLE_ENTITY = "422";
    public static final String TOO_MANY_REQUESTS = "429";

    // 5xx Server Errors (HTTP Status Codes)
    public static final String INTERNAL_SERVER_ERROR = "500";
    public static final String NOT_IMPLEMENTED = "501";
    public static final String BAD_GATEWAY = "502";
    public static final String SERVICE_UNAVAILABLE = "503";
    public static final String GATEWAY_TIMEOUT = "504";

    // Business Logic Error Codes (BLC-xxx)
    public static final String BUSINESS_RULE_VIOLATION = "BLC-001";
    public static final String INVALID_STATE_TRANSITION = "BLC-002";
    public static final String OPERATION_NOT_ALLOWED = "BLC-003";
    public static final String QUOTA_EXCEEDED = "BLC-004";
    public static final String DEPENDENCY_CONFLICT = "BLC-005";
    public static final String INSUFFICIENT_FUNDS = "BLC-006";
    public static final String ACCOUNT_LOCKED = "BLC-007";
    public static final String TRANSACTION_FAILED = "BLC-008";

    // Validation Error Codes (VAL-xxx)
    public static final String VALIDATION_FAILED = "VAL-001";
    public static final String MISSING_REQUIRED_FIELD = "VAL-002";
    public static final String INVALID_FORMAT = "VAL-003";
    public static final String CONSTRAINT_VIOLATION = "VAL-004";
    public static final String INVALID_VALUE_RANGE = "VAL-005";
    public static final String INVALID_EMAIL_FORMAT = "VAL-006";
    public static final String INVALID_PHONE_FORMAT = "VAL-007";
    public static final String INVALID_PAN_FORMAT = "VAL-008";
    public static final String INVALID_AADHAR_FORMAT = "VAL-009";

    // Authentication Error Codes (AUTH-xxx)
    public static final String INVALID_CREDENTIALS = "AUTH-001";
    public static final String TOKEN_EXPIRED = "AUTH-002";
    public static final String TOKEN_INVALID = "AUTH-003";
    public static final String SESSION_EXPIRED = "AUTH-004";
    public static final String INVALID_OTP = "AUTH-005";
    public static final String OTP_EXPIRED = "AUTH-006";

    // Authorization Error Codes (AUTHZ-xxx)
    public static final String INSUFFICIENT_PERMISSIONS = "AUTHZ-001";
    public static final String ROLE_REQUIRED = "AUTHZ-002";
    public static final String RESOURCE_ACCESS_DENIED = "AUTHZ-003";
    public static final String API_ACCESS_DENIED = "AUTHZ-004";

    // Resource Error Codes (RES-xxx)
    public static final String RESOURCE_NOT_FOUND = "RES-001";
    public static final String RESOURCE_ALREADY_EXISTS = "RES-002";
    public static final String RESOURCE_DELETED = "RES-003";
    public static final String RESOURCE_IN_USE = "RES-004";

    // System Error Codes (SYS-xxx)
    public static final String DATABASE_ERROR = "SYS-001";
    public static final String EXTERNAL_SERVICE_ERROR = "SYS-002";
    public static final String TIMEOUT_ERROR = "SYS-003";
    public static final String CONFIGURATION_ERROR = "SYS-004";
    public static final String ENCRYPTION_ERROR = "SYS-005";
    public static final String DECRYPTION_ERROR = "SYS-006";

    // Rate Limiting Error Codes (RATE-xxx)
    public static final String RATE_LIMIT_EXCEEDED = "RATE-001";
    public static final String THROTTLE_LIMIT_EXCEEDED = "RATE-002";

    // Version/Deprecation Error Codes (VER-xxx)
    public static final String DEPRECATED_API = "VER-001";
    public static final String UNSUPPORTED_VERSION = "VER-002";
    public static final String VERSION_MISMATCH = "VER-003";

    // Request Error Codes (REQ-xxx)
    public static final String INVALID_REQUEST_FORMAT = "REQ-001";

    private ErrorCodes() {
        // Utility class - prevent instantiation
    }

    /**
     * Get error code category from error code.
     * 
     * @param errorCode The error code
     * @return The category (e.g., "VAL", "AUTH", "BLC")
     */
    public static String getCategory(String errorCode) {
        if (errorCode == null || errorCode.length() < 3) {
            return "UNK";
        }
        
        // Handle HTTP status codes (3 digits)
        if (errorCode.matches("\\d{3}")) {
            if (errorCode.startsWith("4")) {
                return "4XX";
            } else if (errorCode.startsWith("5")) {
                return "5XX";
            }
            return "HTTP";
        }
        
        // Handle business error codes (XXX-xxx format)
        if (errorCode.contains("-")) {
            return errorCode.substring(0, errorCode.indexOf("-"));
        }
        
        return "UNK";
    }

    /**
     * Check if error code belongs to a specific category.
     * 
     * @param errorCode The error code to check
     * @param category The category to check against
     * @return true if the error code belongs to the category
     */
    public static boolean isCategory(String errorCode, String category) {
        return category.equals(getCategory(errorCode));
    }

    /**
     * Check if error code is an HTTP status code.
     * 
     * @param errorCode The error code to check
     * @return true if the error code is an HTTP status code
     */
    public static boolean isHttpStatusCode(String errorCode) {
        return errorCode != null && errorCode.matches("\\d{3}");
    }

    /**
     * Check if error code is a business error code.
     * 
     * @param errorCode The error code to check
     * @return true if the error code is a business error code
     */
    public static boolean isBusinessErrorCode(String errorCode) {
        return errorCode != null && errorCode.matches("[A-Z]{3,4}-\\d{3}");
    }

    /**
     * Get HTTP status code from error code if it's a business error.
     * Maps business error codes to appropriate HTTP status codes.
     * 
     * @param errorCode The business error code
     * @return The corresponding HTTP status code
     */
    public static String getHttpStatusCode(String errorCode) {
        if (isHttpStatusCode(errorCode)) {
            return errorCode;
        }
        
        if (isBusinessErrorCode(errorCode)) {
            String category = getCategory(errorCode);
            switch (category) {
                case "VAL":
                    return BAD_REQUEST;
                case "AUTH":
                    return UNAUTHORIZED;
                case "AUTHZ":
                    return FORBIDDEN;
                case "RES":
                    return NOT_FOUND;
                case "BLC":
                    return BAD_REQUEST;
                case "SYS":
                    return INTERNAL_SERVER_ERROR;
                case "RATE":
                    return TOO_MANY_REQUESTS;
                case "VER":
                    return BAD_REQUEST;
                case "REQ":
                    return BAD_REQUEST;
                default:
                    return INTERNAL_SERVER_ERROR;
            }
        }
        
        return INTERNAL_SERVER_ERROR;
    }
} 