package com.incede.nbfc.core.monolith.exception;

import com.incede.nbfc.core.monolith.client.dto.GenericFeignErrorDto;
import com.incede.nbfc.core.monolith.common.CommonConstants;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Global Exception Handler for Incede NBFC Core Monolith Service.
 * 
 * Provides comprehensive error handling for all exceptions across the application.
 * Handles validation errors, business exceptions, and system errors securely.
 * 
 * @author Incede NBFC Development Team
 * @version 1.0.0
 */

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handle validation errors from @Valid annotations.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Failed")
                .message("Request validation failed")
                .path(request.getRequestURI())
                .details(fieldErrors)
                .errorCode(ErrorCodes.VALIDATION_FAILED)
                .correlationId(generateCorrelationId())
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Handle Conflict for KYC Document upload
     * @param ex
     * @return
     */
    @ExceptionHandler(BusinessConflictException.class)
    public ResponseEntity<Object> handleBusinessConflictException(BusinessConflictException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("errorCode", ex.getErrorCode());
        body.put("message", ex.getMessage());
        body.put("existingIdentity", ex.getExistingIdentity());
        body.put("existingDetails", ex.getExistingDetails());
        body.put("suggestedActions", ex.getSuggestedActions());
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }


    /**
     * Handle constraint violation exceptions.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex, HttpServletRequest request) {
        
        Map<String, String> violations = new HashMap<>();
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        
        constraintViolations.forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            violations.put(fieldName, errorMessage);
        });

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Constraint Violation")
                .message("Request constraint validation failed")
                .path(request.getRequestURI())
                .details(violations)
                .errorCode(ErrorCodes.CONSTRAINT_VIOLATION)
                .correlationId(generateCorrelationId())
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Handle missing request parameters.
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParameter(
            MissingServletRequestParameterException ex, HttpServletRequest request) {
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Missing Parameter")
                .message("Required parameter '" + ex.getParameterName() + "' is missing")
                .path(request.getRequestURI())
                .errorCode(ErrorCodes.MISSING_REQUIRED_FIELD)
                .correlationId(generateCorrelationId())
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Handle type mismatch in request parameters.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Type Mismatch")
                .message("Parameter '" + ex.getName() + "' should be of type " + 
                        ex.getRequiredType().getSimpleName())
                .path(request.getRequestURI())
                .errorCode(ErrorCodes.INVALID_FORMAT)
                .correlationId(generateCorrelationId())
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Handle malformed JSON requests.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpServletRequest request) {
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Invalid Request Body")
                .message("Request body is malformed or invalid")
                .path(request.getRequestURI())
                .errorCode(ErrorCodes.INVALID_REQUEST_FORMAT)
                .correlationId(generateCorrelationId())
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Handle 404 errors (No handler found).
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFound(
            NoHandlerFoundException ex, HttpServletRequest request) {
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Not Found")
                .message("No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL())
                .path(request.getRequestURI())
                .errorCode(ErrorCodes.NOT_FOUND)
                .correlationId(generateCorrelationId())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * Handle business logic exceptions.
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException ex, HttpServletRequest request) {
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Business Rule Violation")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .errorCode(ex.getErrorCode() != null ? ex.getErrorCode() : ErrorCodes.BUSINESS_RULE_VIOLATION)
                .correlationId(generateCorrelationId())
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Handle resource not found exceptions.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            ResourceNotFoundException ex, HttpServletRequest request) {
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Resource Not Found")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .errorCode(ErrorCodes.RESOURCE_NOT_FOUND)
                .correlationId(generateCorrelationId())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * Handle unauthorized access exceptions.
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(
            UnauthorizedException ex, HttpServletRequest request) {
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error("Unauthorized")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .errorCode(ErrorCodes.UNAUTHORIZED)
                .correlationId(generateCorrelationId())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    /**
     * Handle forbidden access exceptions.
     */
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbidden(
            ForbiddenException ex, HttpServletRequest request) {
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.FORBIDDEN.value())
                .error("Forbidden")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .errorCode(ErrorCodes.FORBIDDEN)
                .correlationId(generateCorrelationId())
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }
//    @ExceptionHandler(FeignException.FeignClientException.class)
//    public ResponseEntity<ErrorResponse> handleFeignException(RuntimeException ex, HttpServletRequest request) {
//        String correlationId = generateCorrelationId();
//        ErrorResponse errorResponse = ErrorResponse.builder()
//                .timestamp(LocalDateTime.now())
//                .status(HttpStatus.SERVICE_UNAVAILABLE.value())
//                .error("Feign Client Errorr")
//                .message("An unexpected error occurred")
//                .path(request.getRequestURI())
//                .errorCode(ErrorCodes.INTERNAL_SERVER_ERROR)
//                .correlationId(correlationId)
//                .build();
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
//    }

    /**
     * Handle all other unexpected exceptions.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, HttpServletRequest request) {
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message("An unexpected error occurred")
                .path(request.getRequestURI())
                .errorCode(ErrorCodes.INTERNAL_SERVER_ERROR)
                .correlationId(generateCorrelationId())
                .build();
        ex.printStackTrace();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }



    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex, HttpServletRequest request) {
        String correlationId = generateCorrelationId();
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message("An unexpected error occurred")
                .path(request.getRequestURI())
                .errorCode(ErrorCodes.INTERNAL_SERVER_ERROR)
                .correlationId(correlationId)
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
    @ExceptionHandler(FeignCustomException.class)
    public ResponseEntity<Map<String, Object>> handleFeignCustomException(FeignCustomException ex) {
        log.error("Handling FeignCustomException: {}", ex.getErrorResponse());
        Map<String, Object> errorResponse = ex.getErrorResponse();
        if (errorResponse == null) {
            errorResponse = Map.of("message", "Unknown error"); // fallback
        }
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(errorResponse);
    }



    // Handle Spring Security Authentication Failures (401 Unauthorized)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(
            AuthenticationException ex, HttpServletRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error("Unauthorized")
                .message("Invalid or missing JWT token")
                .path(request.getRequestURI())
                .errorCode(ErrorCodes.UNAUTHORIZED)
                .correlationId(generateCorrelationId())
                .build();

        log.error("AuthenticationException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    // Handle Spring Security Access Denied (403 Forbidden)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(
            AccessDeniedException ex, HttpServletRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.FORBIDDEN.value())
                .error("Forbidden")
                .message("You don’t have permission to access this resource")
                .path(request.getRequestURI())
                .errorCode(ErrorCodes.FORBIDDEN)
                .correlationId(generateCorrelationId())
                .build();

        log.error("AccessDeniedException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }


// File Upload Exception Handler
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceeded(
            MaxUploadSizeExceededException ex, HttpServletRequest request) {

        log.error("File upload exceeded max size: {}", ex.getMessage(), ex);

        Map<String, String> details = new HashMap<>();
        details.put("maxAllowedSize", String.valueOf(CommonConstants.MAXIMUM_FILE_SIZE)); // can be dynamic if needed
        details.put("actualSize", ex.getCause() != null ? ex.getCause().getMessage() : "unknown");

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.PAYLOAD_TOO_LARGE.value())
                .error("File Too Large")
                .message("File size exceeds the maximum allowed limit")
                .path(request.getRequestURI())
                .details(details)  // ✅ Map<String, String>
                .errorCode("FILE_SIZE_EXCEEDED")
                .correlationId(generateCorrelationId())
                .build();

        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(errorResponse);
    }





    /**
     * Generate a unique correlation ID for tracking errors across systems.
     * 
     * @return A unique correlation ID string
     */
    private String generateCorrelationId() {
        return UUID.randomUUID().toString();
    }
} 