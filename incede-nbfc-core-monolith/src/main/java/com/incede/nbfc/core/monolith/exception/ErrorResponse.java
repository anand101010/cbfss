package com.incede.nbfc.core.monolith.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Standardized error response structure for all exceptions.
 * 
 * Provides consistent error information across the application
 * including timestamp, status, error type, message, and optional details.
 * 
 * @author Incede NBFC Development Team
 * @version 1.0.0
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    /**
     * Timestamp when the error occurred.
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    /**
     * HTTP status code.
     */
    private int status;

    /**
     * Error type/category.
     */
    private String error;

    /**
     * Human-readable error message.
     */
    private String message;

    /**
     * Request path where the error occurred.
     */
    private String path;

    /**
     * Optional additional error details (e.g., validation errors).
     */
    private Map<String, String> details;

    /**
     * Optional error code for client-side handling.
     */
    private String errorCode;

    /**
     * Optional correlation ID for tracking errors across systems.
     */
    private String correlationId;
} 