package com.incede.nbfc.core.monolith.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

/**
 * Utility class for common validation operations.
 * 
 * Provides validation methods for emails, phone numbers, Indian financial
 * documents (PAN, Aadhaar, IFSC), account numbers, amounts, and dates.
 * 
 * @author Incede NBFC Development Team
 * @version 1.0.0
 */
public final class ValidationUtil {

    private ValidationUtil() {
        // Utility class - prevent instantiation
    }

    // Regex patterns
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^[+]?[0-9\\-\\s()]{10,15}$");
    
    private static final Pattern PAN_PATTERN = Pattern.compile(
        "^[A-Z]{5}[0-9]{4}[A-Z]{1}$");
    
    private static final Pattern AADHAAR_PATTERN = Pattern.compile(
        "^[0-9]{12}$");
    
    private static final Pattern IFSC_PATTERN = Pattern.compile(
        "^[A-Z]{4}0[A-Z0-9]{6}$");
    
    private static final Pattern ACCOUNT_NUMBER_PATTERN = Pattern.compile(
        "^[0-9]{9,18}$");
    
    private static final Pattern AMOUNT_PATTERN = Pattern.compile(
        "^[0-9]+(\\.[0-9]{1,2})?$");

    /**
     * Validates if the given string is a valid email address.
     * 
     * @param email the email string to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Validates if the given string is a valid phone number.
     * 
     * @param phoneNumber the phone number string to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phoneNumber.trim()).matches();
    }

    /**
     * Validates if the given string is a valid PAN (Permanent Account Number).
     * 
     * @param pan the PAN string to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPAN(String pan) {
        if (pan == null || pan.trim().isEmpty()) {
            return false;
        }
        return PAN_PATTERN.matcher(pan.trim().toUpperCase()).matches();
    }

    /**
     * Validates if the given string is a valid Aadhaar number.
     * 
     * @param aadhaar the Aadhaar string to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidAadhaar(String aadhaar) {
        if (aadhaar == null || aadhaar.trim().isEmpty()) {
            return false;
        }
        return AADHAAR_PATTERN.matcher(aadhaar.trim()).matches();
    }

    /**
     * Validates if the given string is a valid IFSC code.
     * 
     * @param ifsc the IFSC string to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidIFSC(String ifsc) {
        if (ifsc == null || ifsc.trim().isEmpty()) {
            return false;
        }
        return IFSC_PATTERN.matcher(ifsc.trim().toUpperCase()).matches();
    }

    /**
     * Validates if the given string is a valid account number.
     * 
     * @param accountNumber the account number string to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            return false;
        }
        return ACCOUNT_NUMBER_PATTERN.matcher(accountNumber.trim()).matches();
    }

    /**
     * Validates if the given string is a valid amount.
     * 
     * @param amount the amount string to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidAmount(String amount) {
        if (amount == null || amount.trim().isEmpty()) {
            return false;
        }
        
        if (!AMOUNT_PATTERN.matcher(amount.trim()).matches()) {
            return false;
        }
        
        try {
            double value = Double.parseDouble(amount.trim());
            return value >= 0; // Amount should be non-negative
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validates if the given string is a valid date in YYYY-MM-DD format.
     * 
     * @param date the date string to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidDate(String date) {
        if (date == null || date.trim().isEmpty()) {
            return false;
        }
        
        try {
            LocalDate.parse(date.trim(), DateTimeFormatter.ISO_LOCAL_DATE);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Validates if the given string is not null, empty, or only whitespace.
     * 
     * @param str the string to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidString(String str) {
        return str != null && !str.trim().isEmpty();
    }

    /**
     * Validates if the given string meets minimum length requirement.
     * 
     * @param str the string to validate
     * @param minLength the minimum required length
     * @return true if valid, false otherwise
     */
    public static boolean isValidString(String str, int minLength) {
        return isValidString(str) && str.trim().length() >= minLength;
    }

    /**
     * Validates if the given string meets length range requirements.
     * 
     * @param str the string to validate
     * @param minLength the minimum required length
     * @param maxLength the maximum allowed length
     * @return true if valid, false otherwise
     */
    public static boolean isValidString(String str, int minLength, int maxLength) {
        if (!isValidString(str)) {
            return false;
        }
        
        int length = str.trim().length();
        return length >= minLength && length <= maxLength;
    }
} 