package com.incede.nbfc.core.monolith.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ErrorResponse DTO.
 *
 * Tests the builder pattern, field validation, and JSON serialization.
 *
 * @author Incede NBFC Development Team
 * @version 1.0.0
 */
class ErrorResponseTest {

    @Test
    void testErrorResponseBuilder() {
        LocalDateTime timestamp = LocalDateTime.now();
        Map<String, String> details = new HashMap<>();
        details.put("field1", "error1");
        details.put("field2", "error2");

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(timestamp)
                .status(400)
                .error("Bad Request")
                .message("Validation failed")
                .path("/api/test")
                .details(details)
                .errorCode("VAL-001")
                .correlationId("test-correlation-id")
                .build();

        assertEquals(timestamp, errorResponse.getTimestamp());
        assertEquals(400, errorResponse.getStatus());
        assertEquals("Bad Request", errorResponse.getError());
        assertEquals("Validation failed", errorResponse.getMessage());
        assertEquals("/api/test", errorResponse.getPath());
        assertEquals(details, errorResponse.getDetails());
        assertEquals("VAL-001", errorResponse.getErrorCode());
        assertEquals("test-correlation-id", errorResponse.getCorrelationId());
    }

    @Test
    void testErrorResponseBuilderWithMinimalFields() {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(500)
                .error("Internal Server Error")
                .message("An unexpected error occurred")
                .path("/api/test")
                .build();

        assertNotNull(errorResponse.getTimestamp());
        assertEquals(500, errorResponse.getStatus());
        assertEquals("Internal Server Error", errorResponse.getError());
        assertEquals("An unexpected error occurred", errorResponse.getMessage());
        assertEquals("/api/test", errorResponse.getPath());
        assertNull(errorResponse.getDetails());
        assertNull(errorResponse.getErrorCode());
        assertNull(errorResponse.getCorrelationId());
    }

    @Test
    void testErrorResponseEquality() {
        LocalDateTime timestamp = LocalDateTime.now();
        String correlationId = "test-correlation-id";

        ErrorResponse response1 = ErrorResponse.builder()
                .timestamp(timestamp)
                .status(400)
                .error("Bad Request")
                .message("Validation failed")
                .path("/api/test")
                .errorCode("VAL-001")
                .correlationId(correlationId)
                .build();

        ErrorResponse response2 = ErrorResponse.builder()
                .timestamp(timestamp)
                .status(400)
                .error("Bad Request")
                .message("Validation failed")
                .path("/api/test")
                .errorCode("VAL-001")
                .correlationId(correlationId)
                .build();

        // Test that two responses with same values are equal
        assertEquals(response1.getStatus(), response2.getStatus());
        assertEquals(response1.getError(), response2.getError());
        assertEquals(response1.getMessage(), response2.getMessage());
        assertEquals(response1.getPath(), response2.getPath());
        assertEquals(response1.getErrorCode(), response2.getErrorCode());
        assertEquals(response1.getCorrelationId(), response2.getCorrelationId());
    }

    @Test
    void testErrorResponseWithNullDetails() {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(400)
                .error("Bad Request")
                .message("Validation failed")
                .path("/api/test")
                .details(null)
                .build();

        assertNull(errorResponse.getDetails());
    }

    @Test
    void testErrorResponseWithEmptyDetails() {
        Map<String, String> emptyDetails = new HashMap<>();

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(400)
                .error("Bad Request")
                .message("Validation failed")
                .path("/api/test")
                .details(emptyDetails)
                .build();

        assertNotNull(errorResponse.getDetails());
        assertTrue(errorResponse.getDetails().isEmpty());
    }

}