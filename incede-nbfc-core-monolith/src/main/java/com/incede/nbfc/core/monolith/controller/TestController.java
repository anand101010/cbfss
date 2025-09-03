package com.incede.nbfc.core.monolith.controller;

import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ErrorCodes;
import com.incede.nbfc.core.monolith.exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Test Controller for demonstrating exception handling capabilities.
 * 
 * This controller provides endpoints to test various exception scenarios
 * and verify that the GlobalExceptionHandler works correctly.
 * 
 * @author Incede NBFC Development Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/test")
public class TestController {

    /**
     * Test endpoint that throws a business exception.
     */
    @GetMapping("/business-error")
    public ResponseEntity<String> testBusinessError() {
        throw new BusinessException("This is a test business rule violation", ErrorCodes.BUSINESS_RULE_VIOLATION);
    }

    /**
     * Test endpoint that throws a resource not found exception.
     */
    @GetMapping("/not-found")
    public ResponseEntity<String> testResourceNotFound() {
        throw new ResourceNotFoundException("Customer", "12345");
    }

    /**
     * Test endpoint for validation errors.
     */
    @PostMapping("/validation")
    public ResponseEntity<String> testValidation(@Valid @RequestBody TestRequest request) {
        return ResponseEntity.ok("Validation passed for: " + request.getName());
    }

    /**
     * Test endpoint that throws a generic exception.
     */
    @GetMapping("/generic-error")
    public ResponseEntity<String> testGenericError() {
        throw new RuntimeException("This is a test generic error");
    }

    /**
     * Test endpoint for missing parameter.
     */
    @GetMapping("/missing-param")
    public ResponseEntity<String> testMissingParam(@RequestParam String requiredParam) {
        return ResponseEntity.ok("Parameter received: " + requiredParam);
    }

    /**
     * Test endpoint for type mismatch.
     */
    @GetMapping("/type-mismatch")
    public ResponseEntity<String> testTypeMismatch(@RequestParam Integer number) {
        return ResponseEntity.ok("Number received: " + number);
    }

    /**
     * Test endpoint for HTTP status code error codes.
     */
    @GetMapping("/http-status-codes")
    public ResponseEntity<String> testHttpStatusCodes() {
        return ResponseEntity.ok("Available HTTP status codes: " + 
            "400 (Bad Request), 401 (Unauthorized), 403 (Forbidden), 404 (Not Found), 500 (Internal Server Error)");
    }

    /**
     * Test endpoint for business error codes.
     */
    @GetMapping("/business-error-codes")
    public ResponseEntity<String> testBusinessErrorCodes() {
        return ResponseEntity.ok("Available business error codes: " + 
            "BLC-001 (Business Rule Violation), VAL-001 (Validation Failed), AUTH-001 (Invalid Credentials)");
    }

    /**
     * Test request DTO for validation testing.
     */
    public static class TestRequest {
        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
        private String name;

        @Size(max = 200, message = "Description cannot exceed 200 characters")
        private String description;

        // Getters and setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
} 