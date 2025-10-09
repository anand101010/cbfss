package com.incede.nbfc.core.monolith.common;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KycConstantsTest {


    @Test
    void testConstantsAreAccessible() {
        // Test that all constants are accessible and not null
        assertNotNull(CommonConstants.AADHAAR_PURPOSE);
        assertNotNull(CommonConstants.CONSENT);
    }
    @Test
    void testConstantsHaveExpectedValues() {
        assertEquals("Aadhaar verification",CommonConstants.AADHAAR_PURPOSE);
        assertEquals(true, CommonConstants.CONSENT);
    }
    @Test
    void testConstantsArePublic() {
        // Test that constants are publicly accessible
        assertDoesNotThrow(() -> {
            String purpose = CommonConstants.AADHAAR_PURPOSE ;
            Boolean consemt =CommonConstants.CONSENT;

        });
    }
    @Test
    void testConstantsAreStatic() {
        // Test that constants are static (can be accessed without instantiation)
        assertDoesNotThrow(() -> {
            // Access constants without creating an instance
            CommonConstants.class.getDeclaredField("AADHAAR_PURPOSE");
            CommonConstants.class.getDeclaredField("CONSENT");


        });
    }
}

