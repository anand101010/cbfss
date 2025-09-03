package com.incede.nbfc.core.monolith.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ErrorCodes utility class.
 *
 * Tests error code categorization, validation, and HTTP status mapping.
 *
 * @author Incede NBFC Development Team
 * @version 1.0.0
 */
class ErrorCodesTest {

    @Test
    void testHttpStatusCodeValidation() {
        assertTrue(ErrorCodes.isHttpStatusCode("400"));
        assertTrue(ErrorCodes.isHttpStatusCode("401"));
        assertTrue(ErrorCodes.isHttpStatusCode("500"));
        assertFalse(ErrorCodes.isHttpStatusCode("BLC-001"));
        assertFalse(ErrorCodes.isHttpStatusCode("invalid"));
        assertFalse(ErrorCodes.isHttpStatusCode(null));
    }

    @Test
    void testBusinessErrorCodeValidation() {
        assertTrue(ErrorCodes.isBusinessErrorCode("BLC-001"));
        assertTrue(ErrorCodes.isBusinessErrorCode("VAL-001"));
        assertTrue(ErrorCodes.isBusinessErrorCode("AUTH-001"));
        assertFalse(ErrorCodes.isBusinessErrorCode("400"));
        assertFalse(ErrorCodes.isBusinessErrorCode("invalid"));
        assertFalse(ErrorCodes.isBusinessErrorCode(null));
    }

    @Test
    void testErrorCodeCategorization() {
        assertEquals("4XX", ErrorCodes.getCategory("400"));
        assertEquals("4XX", ErrorCodes.getCategory("401"));
        assertEquals("4XX", ErrorCodes.getCategory("404"));
        assertEquals("5XX", ErrorCodes.getCategory("500"));
        assertEquals("5XX", ErrorCodes.getCategory("503"));
        assertEquals("BLC", ErrorCodes.getCategory("BLC-001"));
        assertEquals("VAL", ErrorCodes.getCategory("VAL-001"));
        assertEquals("AUTH", ErrorCodes.getCategory("AUTH-001"));
        assertEquals("AUTHZ", ErrorCodes.getCategory("AUTHZ-001"));
        assertEquals("RES", ErrorCodes.getCategory("RES-001"));
        assertEquals("SYS", ErrorCodes.getCategory("SYS-001"));
        assertEquals("RATE", ErrorCodes.getCategory("RATE-001"));
        assertEquals("VER", ErrorCodes.getCategory("VER-001"));
        assertEquals("REQ", ErrorCodes.getCategory("REQ-001"));
        assertEquals("UNK", ErrorCodes.getCategory("invalid"));
        assertEquals("UNK", ErrorCodes.getCategory(null));
    }

    @Test
    void testCategoryValidation() {
        assertTrue(ErrorCodes.isCategory("400", "4XX"));
        assertTrue(ErrorCodes.isCategory("500", "5XX"));
        assertTrue(ErrorCodes.isCategory("BLC-001", "BLC"));
        assertTrue(ErrorCodes.isCategory("VAL-001", "VAL"));
        assertFalse(ErrorCodes.isCategory("400", "5XX"));
        assertFalse(ErrorCodes.isCategory("BLC-001", "VAL"));
    }


    @Test
    void testConstantsAreAccessible() {
        // Test that all constants are accessible and not null
        assertNotNull(ErrorCodes.BAD_REQUEST);
        assertNotNull(ErrorCodes.UNAUTHORIZED);
        assertNotNull(ErrorCodes.FORBIDDEN);
        assertNotNull(ErrorCodes.NOT_FOUND);
        assertNotNull(ErrorCodes.INTERNAL_SERVER_ERROR);

        assertNotNull(ErrorCodes.BUSINESS_RULE_VIOLATION);
        assertNotNull(ErrorCodes.VALIDATION_FAILED);
        assertNotNull(ErrorCodes.INVALID_CREDENTIALS);
        assertNotNull(ErrorCodes.RESOURCE_NOT_FOUND);
        assertNotNull(ErrorCodes.DATABASE_ERROR);
    }

    @Test
    void testConstantsHaveExpectedValues() {
        assertEquals("400", ErrorCodes.BAD_REQUEST);
        assertEquals("401", ErrorCodes.UNAUTHORIZED);
        assertEquals("403", ErrorCodes.FORBIDDEN);
        assertEquals("404", ErrorCodes.NOT_FOUND);
        assertEquals("500", ErrorCodes.INTERNAL_SERVER_ERROR);

        assertEquals("BLC-001", ErrorCodes.BUSINESS_RULE_VIOLATION);
        assertEquals("VAL-001", ErrorCodes.VALIDATION_FAILED);
        assertEquals("AUTH-001", ErrorCodes.INVALID_CREDENTIALS);
        assertEquals("RES-001", ErrorCodes.RESOURCE_NOT_FOUND);
        assertEquals("SYS-001", ErrorCodes.DATABASE_ERROR);
    }
}