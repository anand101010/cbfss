package com.incede.nbfc.core.monolith.common;

import com.incede.nbfc.core.monolith.customer.enums.PhotoStatus;
import jakarta.validation.constraints.NotBlank;

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

    public static final String CONFLICT_MESSAGE ="Resource already exist";
    public static final String CONSTRAIN_VIOLATION =" Constrain Violation";
    //customer onboarding
    public static  String DRAFT="Draft";
    public static  String IN_PROGRESS="In_progress";
    public static  String CREATED_BY_REQUIRED="CreatedBy is required";
    public static  String UPDATED_BY_REQUIRED="UpdatedBy is required";
    public static final String INVALID_JSON = "Invalid Json format";
    public static final Integer CREATED_BY = 1;
    public static final Integer UPDATED_BY=1;
    public static  String EMPLOYMENT_NOT_FOUND="Employment info not found";
    public static  String REFERRAL_NOT_FOUND="Referral info not found";
    public static  String PEP_NOT_FOUND="PEP info not found";
    public static  String PROFILE_EXTRA_NOT_FOUND="Profile-Extra info not found";
    public static   String FILE_PATH = "/C/customer/photo";







    //Fallback Constants
    public static final String SERVICE_UNAVAILABLE="Service unavailable. Please try again later.";
    public static final String FAILURE = "failure";

    //kyc constants
    public static final String AADHAAR_PURPOSE = "Aadhaar verification";
    public static final String CONSENT_PURPOSE = "For bank account purpose only";
    public static final String FACE_MATCH_CONSENT = "Y";
    public static final boolean CONSENT =true;
    public static final boolean PDFOUT =true;
    public static final boolean DETECTIONBASEDMASKING =false;
    public static final String FAILUREMESSAGE = "FAILURE";
    public static final String KYCCONSENT="Y";
    public static final String FALLBACKMESSAGE="Service unavailable. Please try again later.";

    //Error messages
    public static final String DOB_REQUIRED_FOR_DL = "Date of birth is required for Driving License";
    public static final String INVALID_ID_TYPE = "Invalid ID type provided";
    public static final String ACCURACYPERCENTAGE ="100";
    public static final String PURPOSE_MESSAGE = "This is a penny drop transaction";
    public static final String PENNYDROP_AMNT = "1";
    public static final String CUSTOMER_BANK_ACCOUNT_MISMATCH="Bank account does not match to  the given customer";
    public static final String CUSTOMER_BANK_ACCOUNT_CONFLICT="Bank account  already exist for this account number ";

    /**
     * Interceptor Constants
     */
    public static final String CLIENTID = "client_id";
    public static final String CLIENTSECREAT = "client_secret";
    public static final String MODULESECREAT = "module_secret";
    public static final String PROVIDERSECREAT = "provider_secret";
    public static final String KEY = "key";
    public static final String APIV2VALIADTOR = "v2/kyc";

    // Entity Names
    public static final String ENTITY_CUSTOMER = "Customer";
    public static final String ENTITY_ADDRESS = "Address";
    public static final String ENTITY_BANK_ACCOUNT = "CustomerBankAccount";
    public static final String ADDRESS_TYPE_PERMANENT = "PERMANENT";
    public static final String ENTITY_NOMINEE = "Nominee";

    // Customer Address Status
    public static final String CUSTOMER_ADDRESS_STATUS_IN_PROGRESS = "IN_PROGRESS";
    public static final String CUSTOMER_ADDRESS_STATUS_UPDATED = "UPDATED";
    public static final String CUSTOMER_ADDRESS_STATUS_SUCCESS = "SUCCESS";

    //  Customer Bank Account Status
    public static final String CUSTOMER_STATUS_IN_PROGRESS = "IN_PROGRESS";
    public static final String CUSTOMER_STATUS_ACTIVE   = "ACTIVE";


} 
