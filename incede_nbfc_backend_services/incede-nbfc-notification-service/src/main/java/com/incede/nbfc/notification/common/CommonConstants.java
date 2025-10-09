package com.incede.nbfc.notification.common;

/**
 * Common constants used throughout the application.
 * 
 * Contains application-wide constants for configuration,
 * API paths, pagination, and other common values.
 * 
 * @author Incede NBFC Development Team
 * @version 1.0.0
 */
public final class CommonConstants {

    private CommonConstants() {
        // Utility class - prevent instantiation
    }

    // Application Information
    public static final String APPLICATION_NAME = "incede-nbfc-core-monolith";
    public static final String APPLICATION_VERSION = "1.0.0";
    public static final String APPLICATION_DESCRIPTION = "Incede NBFC Core Monolith Service";

    // API Configuration
    public static final String API_BASE_PATH = "/api/v1";
    public static final String API_TITLE = "Incede NBFC Core API";
    public static final String API_DESCRIPTION = "Core API for Incede NBFC Services";


    // Pagination Defaults
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;
    public static final String DEFAULT_SORT_DIRECTION = "ASC";
    public static final String DEFAULT_SORT_FIELD = "id";

    // Date Formats
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String ISO_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    // Validation Constants
    public static final int MIN_NAME_LENGTH = 2;
    public static final int MAX_NAME_LENGTH = 100;
    public static final int MIN_DESCRIPTION_LENGTH = 10;
    public static final int MAX_DESCRIPTION_LENGTH = 500;
    public static final int MIN_PHONE_LENGTH = 10;
    public static final int MAX_PHONE_LENGTH = 15;

    // Security Constants
    public static final String JWT_TOKEN_PREFIX = "Bearer ";
    public static final String JWT_TOKEN_HEADER = "Authorization";
    public static final int JWT_TOKEN_EXPIRATION_HOURS = 24;
    public static final int JWT_REFRESH_TOKEN_EXPIRATION_DAYS = 7;

    // File Upload Constants
    public static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    public static final String[] ALLOWED_FILE_TYPES = {
        "application/pdf", "image/jpeg", "image/png", "image/gif"
    };

    // Cache Constants
    public static final String CACHE_PREFIX = "incede-nbfc";
    public static final int DEFAULT_CACHE_TTL_SECONDS = 300; // 5 minutes
    public static final int USER_CACHE_TTL_SECONDS = 1800; // 30 minutes

    // Rate Limiting
    public static final int DEFAULT_RATE_LIMIT_PER_MINUTE = 100;
    public static final int AUTH_RATE_LIMIT_PER_MINUTE = 10;

    // Business Constants
    public static final String DEFAULT_CURRENCY = "INR";
    public static final String DEFAULT_TIMEZONE = "Asia/Kolkata";
    public static final String DEFAULT_COUNTRY_CODE = "IN";

    // Error Messages
    public static final String GENERIC_ERROR_MESSAGE = "An unexpected error occurred";
    public static final String VALIDATION_ERROR_MESSAGE = "Validation failed";
    public static final String NOT_FOUND_MESSAGE = "Resource not found";
    public static final String UNAUTHORIZED_MESSAGE = "Authentication required";
    public static final String FORBIDDEN_MESSAGE = "Access denied";

    //Fallback Constants
    public static final String SERVICE_UNAVAILABLE="Service unavailable. Please try again later.";
    public static final String FAILURE = "failure";

    ///OTP CONSTANTSS
    public static final String CREATED = "CREATED";
    public static final String VERIFIED = "VERIFIED";
    public static final String FAILED = "FAILED";
    public static final String LOCKED = "LOCKED";
    public static final String INVALID = "INVALID";
    public static final String EXPIRED = "EXPIRED";
    public static final String CANCELLED = "CANCELLED";
    public static final String SENT = "SENT";
    public static final String OTPREQUESTNOTFOUND  = "OTP request not found";
    public static final short DEFAULT_MAX_ATTEMPTS = 5;
    public static final short INITIAL_ATTEMPT_COUNT = 0;
    public static final int INITIAL_RANDOM_SALT_LENGTH = 16;


} 