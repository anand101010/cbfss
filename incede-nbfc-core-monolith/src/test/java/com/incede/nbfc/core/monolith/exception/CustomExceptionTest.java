package com.incede.nbfc.core.monolith.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for custom exception classes.
 *
 * Tests the constructors, message handling, and additional properties
 * of BusinessException, ResourceNotFoundException, UnauthorizedException,
 * and ForbiddenException.
 *
 * @author Incede NBFC Development Team
 * @version 1.0.0
 */
class CustomExceptionTest {

    @Test
    void testBusinessException() {
        String message = "Business rule violation occurred";
        String errorCode = "BLC-001";

        BusinessException exception = new BusinessException(message, errorCode);

        assertEquals(message, exception.getMessage());
        assertEquals(errorCode, exception.getErrorCode());
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void testBusinessExceptionWithNullErrorCode() {
        String message = "Business rule violation occurred";

        BusinessException exception = new BusinessException(message, (String) null);

        assertEquals(message, exception.getMessage());
        assertNull(exception.getErrorCode());
    }

    @Test
    void testResourceNotFoundException() {
        String resourceType = "Customer";
        String resourceId = "12345";
        String expectedMessage = "Customer with id '12345' not found";

        ResourceNotFoundException exception = new ResourceNotFoundException(resourceType, resourceId);

        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(resourceType, exception.getResourceType());
        assertEquals(resourceId, exception.getResourceId());
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void testResourceNotFoundExceptionWithNullValues() {
        ResourceNotFoundException exception = new ResourceNotFoundException((String) null, (String) null);

        assertEquals("null with id 'null' not found", exception.getMessage());
        assertNull(exception.getResourceType());
        assertNull(exception.getResourceId());
    }

    @Test
    void testUnauthorizedException() {
        String message = "Authentication required";

        UnauthorizedException exception = new UnauthorizedException(message);

        assertEquals(message, exception.getMessage());
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void testUnauthorizedExceptionWithNullMessage() {
        UnauthorizedException exception = new UnauthorizedException(null);

        assertNull(exception.getMessage());
    }

    @Test
    void testForbiddenException() {
        String message = "Access denied";
        String requiredPermission = "READ_CUSTOMER";

        ForbiddenException exception = new ForbiddenException(message, requiredPermission);

        assertEquals(message, exception.getMessage());
        assertEquals(requiredPermission, exception.getRequiredPermission());
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void testForbiddenExceptionWithNullRequiredPermission() {
        String message = "Access denied";

        ForbiddenException exception = new ForbiddenException(message, (String) null);

        assertEquals("Access denied", exception.getMessage());
        assertNull(exception.getRequiredPermission());
    }

    @Test
    void testForbiddenExceptionWithNullMessage() {
        String requiredPermission = "READ_CUSTOMER";

        ForbiddenException exception = new ForbiddenException((String) null, requiredPermission);

        assertEquals(null, exception.getMessage());
        assertEquals(requiredPermission, exception.getRequiredPermission());
    }

    @Test
    void testExceptionInheritance() {
        // Verify all custom exceptions inherit from RuntimeException
        assertTrue(new BusinessException("test", "TEST-001") instanceof RuntimeException);
        assertTrue(new ResourceNotFoundException("test", "123") instanceof RuntimeException);
        assertTrue(new UnauthorizedException("test") instanceof RuntimeException);
        assertTrue(new ForbiddenException("test", "action") instanceof RuntimeException);
    }

    @Test
    void testExceptionMessageFormatting() {
        // Test ResourceNotFoundException message formatting
        ResourceNotFoundException exception1 = new ResourceNotFoundException("Product", "ABC123");
        assertEquals("Product with id 'ABC123' not found", exception1.getMessage());

        // Test ForbiddenException message formatting
        ForbiddenException exception2 = new ForbiddenException("Permission denied", "WRITE_USER");
        assertEquals("Permission denied", exception2.getMessage());
    }
}