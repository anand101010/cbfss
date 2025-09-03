package com.incede.nbfc.core.monolith.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for common constants.
 *
 * Tests the values and accessibility of common constants used
 * throughout the application.
 *
 * @author Incede NBFC Development Team
 * @version 1.0.0
 */
class CommonConstantsTest {

    @Test
    void testConstantsAreAccessible() {
        // Test that all constants are accessible and not null
        assertNotNull(CommonConstants.APPLICATION_NAME);
        assertNotNull(CommonConstants.APPLICATION_VERSION);
        assertNotNull(CommonConstants.API_BASE_PATH);
        assertNotNull(CommonConstants.DEFAULT_PAGE_SIZE);
        assertNotNull(CommonConstants.MAX_PAGE_SIZE);
        assertNotNull(CommonConstants.DEFAULT_SORT_DIRECTION);
        assertNotNull(CommonConstants.DEFAULT_SORT_FIELD);
    }

    @Test
    void testConstantsHaveExpectedValues() {
        assertEquals("incede-nbfc-core-monolith", CommonConstants.APPLICATION_NAME);
        assertEquals("1.0.0", CommonConstants.APPLICATION_VERSION);
        assertEquals("/api/v1", CommonConstants.API_BASE_PATH);
        assertEquals(20, CommonConstants.DEFAULT_PAGE_SIZE);
        assertEquals(100, CommonConstants.MAX_PAGE_SIZE);
        assertEquals("ASC", CommonConstants.DEFAULT_SORT_DIRECTION);
        assertEquals("id", CommonConstants.DEFAULT_SORT_FIELD);
    }

    @Test
    void testConstantsAreFinal() {
        // Test that constants are final (compile-time constant)
        assertTrue(CommonConstants.APPLICATION_NAME instanceof String);
        assertTrue(CommonConstants.APPLICATION_VERSION instanceof String);
        assertTrue(CommonConstants.API_BASE_PATH instanceof String);
        // For int primitives, we can't use instanceof, but we can test their values
        assertEquals(20, CommonConstants.DEFAULT_PAGE_SIZE);
        assertEquals(100, CommonConstants.MAX_PAGE_SIZE);
        assertTrue(CommonConstants.DEFAULT_SORT_DIRECTION instanceof String);
        assertTrue(CommonConstants.DEFAULT_SORT_FIELD instanceof String);
    }

    @Test
    void testConstantsArePublic() {
        // Test that constants are publicly accessible
        assertDoesNotThrow(() -> {
            String appName = CommonConstants.APPLICATION_NAME;
            String appVersion = CommonConstants.APPLICATION_VERSION;
            String apiPath = CommonConstants.API_BASE_PATH;
            Integer pageSize = CommonConstants.DEFAULT_PAGE_SIZE;
            Integer maxPageSize = CommonConstants.MAX_PAGE_SIZE;
            String sortDir = CommonConstants.DEFAULT_SORT_DIRECTION;
            String sortField = CommonConstants.DEFAULT_SORT_FIELD;
        });
    }

    @Test
    void testConstantsAreStatic() {
        // Test that constants are static (can be accessed without instantiation)
        assertDoesNotThrow(() -> {
            // Access constants without creating an instance
            CommonConstants.class.getDeclaredField("APPLICATION_NAME");
            CommonConstants.class.getDeclaredField("APPLICATION_VERSION");
            CommonConstants.class.getDeclaredField("API_BASE_PATH");
            CommonConstants.class.getDeclaredField("DEFAULT_PAGE_SIZE");
            CommonConstants.class.getDeclaredField("MAX_PAGE_SIZE");
            CommonConstants.class.getDeclaredField("DEFAULT_SORT_DIRECTION");
            CommonConstants.class.getDeclaredField("DEFAULT_SORT_FIELD");
        });
    }
}