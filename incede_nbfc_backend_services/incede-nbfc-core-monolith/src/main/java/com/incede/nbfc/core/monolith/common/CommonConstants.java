package com.incede.nbfc.core.monolith.common;



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


    public static final String CUSTOMER_TYPE = "TEST";
    public static final String FILE_REQUIRED ="File required" ;
    public static final String CUSTOMER_GROUP_NOT_FOUND = "Customer group not found";
    public static final String CUSTOMER_RISK_CATEGORY_NOT_FOUND ="Customer risk-category not found" ;
    public static final String CATEGORY_NOT_FOUND = "Category not found";
    public static final String CATEGORY_CODE ="000" ;
    public static final String MOBILE_NUMBER_CONFLICT_MESSAGE = "Customer already exist with this mobile number";
    public static final String CAPTURED_BY_NOT_FOUND = "Captured user does not  exist";
    public static final String KYC_NOT_FOUND = "No KYC document found ";
    public static final String MOBILE_NOT_FOUND ="Mobile contact type not found" ;
    public static final String KYC_NOT_FOUND_FOR_CUSTOMER = "No KYC document found for this customer";
    public static final String KYC_UPLOAD_NOT_FOUND_FOR_CUSTOMER = "KYC uploaded document not found for this customer";
    public static final String FAILED_TO_CREATE_BANK_ACCOUNT = "Failed to create bank account";
    public static final String AADHAR_CANNOT_BE_MASKED ="Aadhar number cannot be masked" ;
    public static final String CANNOT_GENERATE_FORM60_REPORT = "Unable to generate Form60 report";
    public static final String FORM_60_NOT_FOUND ="Form60 Not Found" ;
    public static final String BRANCH_NOT_FOUND_IN_FORM60 = "branch not found for the  form 60 identity";
    public static final Object CUSTOMER = "CUSTOMER";
    public static final Object AGENT = "AGENT";
    public static final Object STAFF = "STAFF";
    public static final String CANVASSER_NOT_FOUND ="Canvasser not found" ;

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
    public static final int MAXIMUM_FILE_SIZE = 50 * 1024 * 1024;

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
    public static  String DRAFT="DRAFT";
    public static  String IN_PROGRESS="IN_PROGRESS";
    public static  String CREATED_BY_REQUIRED="CreatedBy is required";
    public static  String UPDATED_BY_REQUIRED="UpdatedBy is required";
    public static final String INVALID_JSON = "Invalid Json format";
    public static final Integer CREATED_BY = 1;
    public static final Integer UPDATED_BY=1;
    public static  String EMPLOYMENT_NOT_FOUND="Employment info not found";
    public static  String REFERRAL_NOT_FOUND="Referral info not found";
    public static  String PEP_NOT_FOUND="PEP info not found";
    public static  String PROFILE_EXTRA_NOT_FOUND="Profile-Extra info not found";
    public static  String ASSET_NOT_FOUND = "Asset not found";
    public static  String CUSTOMER_BANK_UPI_CONFLICT = "UPI-id already exists";
    public static final String CONTACT_ALREADY_EXIST ="A primary contact with this contact value already exists for another customer" ;
    public static final String CONTACT_EXISTS = "A contact with the given value already exists";
    public static final String DOCUMENT_ALREADY_EXISTS = "Document already exists";







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
    public static final String SUCCESS = "SUCCESS";
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
    public static final String CUSTOMER_STATUS_ACTIVE = "ACTIVE";

    public static final String COMPLETED = "COMPLETED";
    public static final String CUSTOMER_NOT_FOUND = "Customer not found";
    public static final String CUSTOMER_CONFLICT_MESSAGE ="Customer with this Aadhar-vault-id already exist";
    public static final String INVALID_GENDER = "Invalid gender";
    public static final String INVALID_MARITAL_STATUS = "Invalid marital status";
    public static final String INVALID_TAX_CATEGORY = "Invalid tax category";
    public static final String INVALID_OCCUPATION = "Invalid occupation";
    public static final String INVALID_BRANCH = "Invalid branch";
    public static final String INVALID_SALUTATION = "Invalid salutation";
    public static final String INVALID_CUSTOMER_STATUS = "Invalid customer status";
    public static final String GUARDIAN_NOT_FOUND = "Guardian not found";
    public static final String OCCUPATION_NOT_FOUND = "Occupation not found";
    public static final String DESIGNATION_NOT_FOUND = "Designation not found";
    public static final String INCOME_SOURCE_NOT_FOUND = "Income source not found";
    public static final String REFERRAL_SOURCE_NOT_FOUND = "Referral source not found";
    public static final String CANVASSED_TYPE_NOT_FOUND = "Canvassed type not found";
    public static final String EDUCATION_LEVEL_NOT_FOUND = "Education level not found";
    public static final String PURPOSE_NOT_FOUND = "Purpose not found";
    public static final String ASSET_TYPE_NOT_FOUND = "Asset type not found";
    public static final String NATIONALITY_NOT_FOUND = "Nationality not found";
    public static final String PREFERRED_LANGUAGE_NOT_FOUND = "Preferred language not found";
    public static final String RESIDENTIAL_STATUS_NOT_FOUND = "Residential status not found";
    public static final String CUSTOMER_ADDITIONAL_REF_NAME_NOT_FOUND = "Customer additional reference name not found";
    public static final String TENANT_NOT_FOUND = "Tenant not found";
    public static final String CUSTOMER_ADDITIONAL_REF_VALUE_NOT_FOUND = "Customer additional reference value not found";
    public static final String INVALID_REQUEST = "Invalid request";
    public static final String DOCUMENT_TYPE_NOT_FOUND = "Document type not found";
    public static final String FILE_PROCESSING_FAILED = "Error processing file upload";
    public static final String PHOTOS_NOT_FOUND = "No photos found";

    public static final String CONTACT_TYPE_NOT_FOUND = "Contact type not found";
    public static final String CUSTOMER_CONTACT_NOT_FOUND = "Customer contact not found";

    public static final String BANK_ACCOUNT_REQUEST_VALIDATION_ERROR = "Bank account validation error";
    public static final String INVALID_ACCOUNT_TYPE = "Invalid account type";
    public static final String INVALID_ACCOUNT_STATUS = "Invalid account status";
    public static final String FAILED_TO_UPDATE_BANK_ACCOUNT = "Failed to update bank account";
    public static final String INVALID_POST_OFFICE = "Post office not found";
    public static final String PID_DOCUMENT_NOT_FOUND = "PID document not found";
    public static final String ADDRESS_DOC_NOT_FOUND = "Address document not found";
    public static final String TRANSACTION_AMOUNT_NOT_NULL = "Transaction amount must be provided";
    public static final String TRANSACTION_DATE_NOT_NULL = "Transaction date must be provided";
    public static final String FORM_60_NOT_FOUND_FOR_THE_CUSTOMER = "Form 60 not found for the customer";
    public static final String TRANSACTION_AMOUNT_MUST_BE_NONZERO = "Transaction amount must be greater than zero";
    public static final String MAXIMUM_TRANSACTION_AMOUNT_CONSTRAINT = "Form 60 cannot be used for transactions above ₹5,00,000. Please provide PAN";
    public static final String NOTIFICATION_PREFERENCE_NOT_FOUND = "Notification preference not found";
    public static final String NOTIFICATION_PREFERENCE_ALREADY_EXIST = "Notification preference already exists for the customer";
    public static final String RELATIONSHIP_NOT_FOUND = "Relationship not found";
    public static final String NOMINEE_ALREADY_EXIST = "Nominee already exists with the same name and relationship";
    public static final String NOMINEE_NOT_FOUND_FOR_GIVEN_CUSTOMER = "Nominee not found for the given customer";
    public static final String NOMINEE_NAME_AND_RELATIONSHIP_ALREADY_EXIST = "A nominee with the same name and relationship already exists";
    public static final String GUARDIAN_NAME_REQUIRED = "Guardian's name is required for minor nominees";
    public static final String GUARDIAN_DOB_REQUIRED = "Guardian's date of birth is required for minor nominees";
    public static final String GUARDIAN_EMAIL_REQUIRED = "Guardian's email is required for minor nominees";
    public static final String GUARDIAN_CONTACT_NUMBER_REQUIRED = "Guardian's contact number is required for minor nominees";
    public static final String TOTAL_SHARE_VALIDATION = "Total percentage share cannot exceed 100";
    public static final String PERMANENT_ADDRESS_NOT_FOUND = "Permanent address not found";
    public static final String NO_VALID_CUSTOMER_TYPE = "Customer's permanent address has no valid address type";
    public static final String NO_VALID_POST_OFFICE_FOR_PERMANENT_ADDRESS = "Customer's permanent address has no valid post office";
    public static final String ADDRESS_REQUIRED_FOR_IS_SAME_ADDRESS_TRUE = "Address must be provided when isSameAddress is false";
    public static final String ADDRESS_NOT_FOUND = "Address not found";
    public static final String RELATIONSHIP_IS_REQUIRED = "Relationship is required";
    public static final String INVALID_RELATIONSHIP = "Invalid relationship";
    public static final String NO_ACTIVE_ADDRESSES_FOUND_FOR_CUSTOMER_IDENTITY = "No active address found for customer";
    public static final String ADDRESS_IS_REQUIRED = "Address is required";
    public static final String POST_OFFICE_IS_REQUIRED = "Post office is required";
    public static final String CITY_IS_REQUIRED = "City is required";
    public static final String DISTRICT_IS_REQUIRED = "District is required";
    public static final String STATE_IS_REQUIRED = "State is required";
    public static final String COUNTRY_IS_REQUIRED = "Country is required";
    public static final String PIN_CODE_IS_REQUIRED = "Pin code is required";
    public static final String INVALID_ADDRESS_TYPE = "Invalid address type";
    public static final String INVALID_ADDRESS_PROOF_TYPE = "Invalid address proof type";
    public static final String IS_SAME_AS_PERMANENT_CONSTRAINT = "Document file must be provided if 'isSameAsPermanent' is true";
    public static final String NO_ACTIVE_ADDRESSES_FOUND_FOR_CUSTOMER = "No active address found for the customer";
    public static final String DUPLICATE_IDENTIFIER = "DUPLICATE_IDENTIFIER" ;
    public static final String FILE_UPLOAD_FAILED ="File upload failed" ;
    // CUSTOMER LEADS
    public static final String LEAD_FOLLOW_UP_CREATED   = "Follow-up Created";
    public static final String LEAD_FOLLOW_UP_UPDATED   = "Follow-up Updated";
    public static final String LEAD_FOLLOW_UP_FETCHED   = "Fetched successfully";


    public static final String LEAD_FOLLOW_UP_HISTORY_CREATED   = "Follow-up-history Created";
    public static final String LEAD_FOLLOW_UP_HISTORY_FETCHED   = "Fetched successfully";

    // CUSTOMER LEADS ASSIGNMENT
    public static final String LEAD_ASSIGNMENT_UPDATED   = "Assignment updated";
    public static final String LEAD_ASSIGNMENT_STATUS_ACTIVE   = "ACTIVE";
    public static final String LEAD_ASSIGNMENT_STATUS_REASSIGNED   = "INACTIVE";
    public static final String LEAD_ASSIGNMENT_REMARK_REASSIGNED   = "Lead reassigned";


}
