package com.incede.nbfc.core.monolith.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for validation utilities.
 * 
 * Tests common validation operations and utilities that can be used
 * across different parts of the application.
 * 
 * @author Incede NBFC Development Team
 * @version 1.0.0
 */
class ValidationUtilTest {

    @Test
    void testIsValidEmail() {
        assertTrue(ValidationUtil.isValidEmail("test@example.com"));
        assertTrue(ValidationUtil.isValidEmail("user.name@domain.co.uk"));
        assertTrue(ValidationUtil.isValidEmail("test+tag@example.org"));
        
        assertFalse(ValidationUtil.isValidEmail("invalid-email"));
        assertFalse(ValidationUtil.isValidEmail("@example.com"));
        assertFalse(ValidationUtil.isValidEmail("test@"));
        assertFalse(ValidationUtil.isValidEmail(""));
        assertFalse(ValidationUtil.isValidEmail(null));
    }

    @Test
    void testIsValidPhoneNumber() {
        assertTrue(ValidationUtil.isValidPhoneNumber("+91-9876543210"));
        assertTrue(ValidationUtil.isValidPhoneNumber("9876543210"));
        assertTrue(ValidationUtil.isValidPhoneNumber("+1-555-123-4567"));
        
        assertFalse(ValidationUtil.isValidPhoneNumber("123"));
        assertFalse(ValidationUtil.isValidPhoneNumber("abc-def-ghij"));
        assertFalse(ValidationUtil.isValidPhoneNumber(""));
        assertFalse(ValidationUtil.isValidPhoneNumber(null));
    }

    @Test
    void testIsValidPAN() {
        assertTrue(ValidationUtil.isValidPAN("ABCDE1234F"));
        assertTrue(ValidationUtil.isValidPAN("XYZAB5678C"));
        
        assertFalse(ValidationUtil.isValidPAN("ABCD1234F")); // Wrong length
        assertFalse(ValidationUtil.isValidPAN("ABCDE12345")); // No letter at end
        assertFalse(ValidationUtil.isValidPAN("12345ABCDE")); // Wrong format
        assertFalse(ValidationUtil.isValidPAN(""));
        assertFalse(ValidationUtil.isValidPAN(null));
    }

    @Test
    void testIsValidAadhaar() {
        assertTrue(ValidationUtil.isValidAadhaar("123456789012"));
        assertTrue(ValidationUtil.isValidAadhaar("987654321098"));
        
        assertFalse(ValidationUtil.isValidAadhaar("12345678901")); // Too short
        assertFalse(ValidationUtil.isValidAadhaar("1234567890123")); // Too long
        assertFalse(ValidationUtil.isValidAadhaar("ABCDEFGHIJKL")); // Contains letters
        assertFalse(ValidationUtil.isValidAadhaar(""));
        assertFalse(ValidationUtil.isValidAadhaar(null));
    }

    @Test
    void testIsValidIFSC() {
        assertTrue(ValidationUtil.isValidIFSC("SBIN0001234"));
        assertTrue(ValidationUtil.isValidIFSC("HDFC0001234"));
        
        assertFalse(ValidationUtil.isValidIFSC("SBIN001234")); // Too short
        assertFalse(ValidationUtil.isValidIFSC("SBIN00012345")); // Too long
        assertFalse(ValidationUtil.isValidIFSC("1234000SBIN")); // Wrong format
        assertFalse(ValidationUtil.isValidIFSC(""));
        assertFalse(ValidationUtil.isValidIFSC(null));
    }

    @Test
    void testIsValidAccountNumber() {
        assertTrue(ValidationUtil.isValidAccountNumber("1234567890"));
        assertTrue(ValidationUtil.isValidAccountNumber("987654321098765"));
        
        assertFalse(ValidationUtil.isValidAccountNumber("123")); // Too short
        assertFalse(ValidationUtil.isValidAccountNumber("123456789012345678901")); // Too long
        assertFalse(ValidationUtil.isValidAccountNumber("123456789A")); // Contains letters
        assertFalse(ValidationUtil.isValidAccountNumber(""));
        assertFalse(ValidationUtil.isValidAccountNumber(null));
    }

    @Test
    void testIsValidAmount() {
        assertTrue(ValidationUtil.isValidAmount("100.00"));
        assertTrue(ValidationUtil.isValidAmount("1000"));
        assertTrue(ValidationUtil.isValidAmount("0.01"));
        assertTrue(ValidationUtil.isValidAmount("999999.99"));
        
        assertFalse(ValidationUtil.isValidAmount("-100.00")); // Negative
        assertFalse(ValidationUtil.isValidAmount("100.001")); // Too many decimal places
        assertFalse(ValidationUtil.isValidAmount("abc"));
        assertFalse(ValidationUtil.isValidAmount(""));
        assertFalse(ValidationUtil.isValidAmount(null));
    }

    @Test
    void testIsValidDate() {
        assertTrue(ValidationUtil.isValidDate("2023-12-01"));
        assertTrue(ValidationUtil.isValidDate("2024-02-29")); // Leap year
        
        assertFalse(ValidationUtil.isValidDate("2023-13-01")); // Invalid month
        assertFalse(ValidationUtil.isValidDate("2023-12-32")); // Invalid day
        assertFalse(ValidationUtil.isValidDate("2023/12/01")); // Wrong format
        assertFalse(ValidationUtil.isValidDate(""));
        assertFalse(ValidationUtil.isValidDate(null));
    }

    @Test
    void testIsValidString() {
        assertTrue(ValidationUtil.isValidString("valid string"));
        assertTrue(ValidationUtil.isValidString("a"));
        
        assertFalse(ValidationUtil.isValidString(""));
        assertFalse(ValidationUtil.isValidString("   ")); // Only whitespace
        assertFalse(ValidationUtil.isValidString(null));
    }

    @Test
    void testIsValidStringWithMinLength() {
        assertTrue(ValidationUtil.isValidString("test", 3));
        assertTrue(ValidationUtil.isValidString("testing", 5));
        
        assertFalse(ValidationUtil.isValidString("ab", 3)); // Too short
        assertFalse(ValidationUtil.isValidString("", 1));
        assertFalse(ValidationUtil.isValidString(null, 1));
    }

    @Test
    void testIsValidStringWithLengthRange() {
        assertTrue(ValidationUtil.isValidString("test", 3, 10));
        assertTrue(ValidationUtil.isValidString("testing", 5, 10));
        assertFalse(ValidationUtil.isValidString("ab", 3, 10)); // Too short
        assertFalse(ValidationUtil.isValidString("very long string", 3, 10)); // Too long
        assertFalse(ValidationUtil.isValidString("", 1, 10));
        assertFalse(ValidationUtil.isValidString(null, 1, 10));
    }
} 