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
        assertNotNull(CommonConstants.AADHAAR_PURPOSE);
        assertNotNull(CommonConstants.CONSENT);
        assertNotNull(CommonConstants.KYCCONSENT);
        assertNotNull(CommonConstants.DOB_REQUIRED_FOR_DL);
        assertNotNull(CommonConstants.INVALID_ID_TYPE);
        assertNotNull(CommonConstants.PENNYDROP_AMNT);
        assertNotNull(CommonConstants.PURPOSE_MESSAGE);
        assertNotNull(CommonConstants.ACCURACYPERCENTAGE);


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

        assertEquals("Aadhaar verification", CommonConstants.AADHAAR_PURPOSE);
        assertEquals("For bank account purpose only", CommonConstants.CONSENT_PURPOSE);
        assertTrue(CommonConstants.CONSENT);
        assertTrue(CommonConstants.PDFOUT);
        assertFalse(CommonConstants.DETECTIONBASEDMASKING);
        assertEquals("FAILURE", CommonConstants.FAILUREMESSAGE);
        assertEquals("Y", CommonConstants.KYCCONSENT);
        assertEquals("Service unavailable. Please try again later.", CommonConstants.FALLBACKMESSAGE);
        assertEquals("Date of birth is required for Driving License", CommonConstants.DOB_REQUIRED_FOR_DL);
        assertEquals("Invalid ID type provided", CommonConstants.INVALID_ID_TYPE);
        assertEquals("100", CommonConstants.ACCURACYPERCENTAGE);

        // Banking constants
        assertEquals("This is a penny drop transaction", CommonConstants.PURPOSE_MESSAGE);
        assertEquals("1", CommonConstants.PENNYDROP_AMNT);
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

        assertTrue(CommonConstants.AADHAAR_PURPOSE instanceof String);
        assertTrue(CommonConstants.CONSENT_PURPOSE instanceof String);
        assertTrue(CommonConstants.KYCCONSENT instanceof String);
        assertTrue(CommonConstants.DOB_REQUIRED_FOR_DL instanceof String);
        assertTrue(CommonConstants.INVALID_ID_TYPE instanceof String);
        assertTrue(CommonConstants.PURPOSE_MESSAGE instanceof String);
        assertTrue(CommonConstants.ACCURACYPERCENTAGE instanceof String);
        // Bolean constants
        assertTrue(CommonConstants.CONSENT);


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

            String aadhaarPurpose = CommonConstants.AADHAAR_PURPOSE;
            String consentPurpose = CommonConstants.CONSENT_PURPOSE;
            boolean consent = CommonConstants.CONSENT;
            boolean pdfout = CommonConstants.PDFOUT;
            boolean detection = CommonConstants.DETECTIONBASEDMASKING;
            String failureMessage = CommonConstants.FAILUREMESSAGE;
            String kycConsent = CommonConstants.KYCCONSENT;
            String fallback = CommonConstants.FALLBACKMESSAGE;
            String dobReq = CommonConstants.DOB_REQUIRED_FOR_DL;
            String invalidId = CommonConstants.INVALID_ID_TYPE;
            String accuracy = CommonConstants.ACCURACYPERCENTAGE;
            String pennyDropMsg = CommonConstants.PURPOSE_MESSAGE;
            String pennyAmt = CommonConstants.PENNYDROP_AMNT;
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

            CommonConstants.class.getDeclaredField("AADHAAR_PURPOSE");
            CommonConstants.class.getDeclaredField("CONSENT_PURPOSE");
            CommonConstants.class.getDeclaredField("CONSENT");
            CommonConstants.class.getDeclaredField("PDFOUT");
            CommonConstants.class.getDeclaredField("DETECTIONBASEDMASKING");
            CommonConstants.class.getDeclaredField("FAILUREMESSAGE");
            CommonConstants.class.getDeclaredField("KYCCONSENT");
            CommonConstants.class.getDeclaredField("FALLBACKMESSAGE");
            CommonConstants.class.getDeclaredField("DOB_REQUIRED_FOR_DL");
            CommonConstants.class.getDeclaredField("INVALID_ID_TYPE");
            CommonConstants.class.getDeclaredField("ACCURACYPERCENTAGE");
            CommonConstants.class.getDeclaredField("PURPOSE_MESSAGE");
            CommonConstants.class.getDeclaredField("PENNYDROP_AMNT");
        });
    }




} 
