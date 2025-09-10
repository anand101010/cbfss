package com.incede.nbfc.core.monolith.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for GlobalExceptionHandler.
 * 
 * Tests various exception handling scenarios and verifies
 * the correct ErrorResponse structure.
 * 
 * @author Incede NBFC Development Team
 * @version 1.0.0
 */
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;
    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
        request = new MockHttpServletRequest();
        request.setRequestURI("/api/test");
    }

    @Test
    void testHandleBusinessException() {
        BusinessException ex = new BusinessException("Test business error", ErrorCodes.BUSINESS_RULE_VIOLATION);
        
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleBusinessException(ex, request);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("BLC-001", errorResponse.getErrorCode());
        assertEquals("Test business error", errorResponse.getMessage());
        assertNotNull(errorResponse.getCorrelationId());
    }



    @Test
    void testHandleUnauthorizedException() {
        UnauthorizedException ex = new UnauthorizedException("Authentication required");
        
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleUnauthorized(ex, request);
        
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("401", errorResponse.getErrorCode());
        assertEquals("Authentication required", errorResponse.getMessage());
        assertNotNull(errorResponse.getCorrelationId());
    }

    @Test
    void testHandleForbiddenException() {
        ForbiddenException ex = new ForbiddenException("Access denied", "READ_CUSTOMER");
        
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleForbidden(ex, request);
        
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("403", errorResponse.getErrorCode());
        assertEquals("Access denied", errorResponse.getMessage());
        assertNotNull(errorResponse.getCorrelationId());
    }



    @Test
    void testHandleNoHandlerFoundException() {
        NoHandlerFoundException ex = new NoHandlerFoundException("GET", "/invalid", null);
        
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleNoHandlerFound(ex, request);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("404", errorResponse.getErrorCode());
        assertNotNull(errorResponse.getCorrelationId());
    }

    @Test
    void testHandleMethodArgumentTypeMismatchException() {
        MethodArgumentTypeMismatchException ex = new MethodArgumentTypeMismatchException(
            "42", Integer.class, "number", null, null);
        
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleTypeMismatch(ex, request);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertNotNull(errorResponse.getCorrelationId());
    }

    @Test
    void testHandleValidationExceptions() {
        FieldError fieldError = new FieldError("testObject", "name", "Name is required");
        BindingResult bindingResult = new org.springframework.validation.BeanPropertyBindingResult(new Object(), "testObject");
        bindingResult.addError(fieldError);
        
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);
        
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleValidationExceptions(ex, request);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("VAL-001", errorResponse.getErrorCode());
        assertNotNull(errorResponse.getDetails());
        assertEquals("Name is required", errorResponse.getDetails().get("name"));
        assertNotNull(errorResponse.getCorrelationId());
    }
} 