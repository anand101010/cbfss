package com.incede.nbfc.core.monolith.customer.controller;

import com.incede.nbfc.core.monolith.customer.dto.CustomerPhotoRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerPhotoResponseDto;
import com.incede.nbfc.core.monolith.customer.enums.PhotoStatus;
import com.incede.nbfc.core.monolith.customer.service.CustomerPhotoService;
import com.incede.nbfc.core.monolith.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CustomerPhotoControllerTest {

    @Mock
    private CustomerPhotoService customerPhotoService;

    @InjectMocks
    private CustomerPhotoController customerPhotoController;

    private UUID customerId;
    private UUID capturedById;
    private CustomerPhotoRequestDto requestDto;
    private CustomerPhotoResponseDto responseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customerId = UUID.randomUUID();
        capturedById = UUID.randomUUID();

        // Create request DTO with all required fields
        requestDto = new CustomerPhotoRequestDto();
        requestDto.setCapturedBy(capturedById);
        requestDto.setLatitude(BigDecimal.valueOf(12.9716));
        requestDto.setLongitude(BigDecimal.valueOf(77.5946));
        requestDto.setPhotoLivenessStatus("verified");
        requestDto.setAccuracy(BigDecimal.valueOf(1.0));
        requestDto.setCaptureDevice("mobile");
        requestDto.setLocationDescription("Office location");
        requestDto.setCaptureTime("2025-09-08T17:00:00");
        requestDto.setPhotoRefId("PHOTO12345");
        requestDto.setFilePath("/photos/customer/photo.png");

        // Create response DTO
        CustomerPhotoResponseDto.PhotoDetail detail = CustomerPhotoResponseDto.PhotoDetail.builder()
                .photoId(UUID.randomUUID())
                .photoRefId("PHOTO12345")
                .status(PhotoStatus.PENDING)
                .captureTime(LocalDateTime.now())
                .build();

        responseDto = CustomerPhotoResponseDto.builder()
                .identity(customerId)
                .customerCode("CUST123")
                .status("IN_PROGRESS")
                .photo(List.of(detail))
                .build();
    }

    @Test
    void testCreatePhoto_Success() {
        when(customerPhotoService.createPhoto(any(UUID.class), any(CustomerPhotoRequestDto.class)))
                .thenReturn(responseDto);

        ResponseEntity<CustomerPhotoResponseDto> response =
                customerPhotoController.createPhoto(customerId, requestDto);

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(responseDto, response.getBody());
        assertEquals(customerId, response.getBody().getIdentity());
    }

    @Test
    void testGetPhoto_Success() {
        when(customerPhotoService.getCustomerPhotos(customerId)).thenReturn(responseDto);

        ResponseEntity<CustomerPhotoResponseDto> response =
                customerPhotoController.getPhoto(customerId);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(responseDto, response.getBody());
    }

    @Test
    void testCreatePhoto_WhenCustomerNotFound_ThrowsException() {
        when(customerPhotoService.createPhoto(any(UUID.class), any(CustomerPhotoRequestDto.class)))
                .thenThrow(new ResourceNotFoundException("Customer not found"));

        Exception exception = assertThrows(ResourceNotFoundException.class, () ->
                customerPhotoController.createPhoto(customerId, requestDto));

        assertTrue(exception.getMessage().contains("Customer"));
    }

    @Test
    void testGetPhoto_WhenNoPhotosFound_ThrowsException() {
        when(customerPhotoService.getCustomerPhotos(customerId))
                .thenThrow(new ResourceNotFoundException("No photos found for customer"));

        Exception exception = assertThrows(ResourceNotFoundException.class, () ->
                customerPhotoController.getPhoto(customerId));

        assertTrue(exception.getMessage().contains("photos"));
    }

    @Test
    void testCreatePhoto_WhenUnexpectedErrorOccurs_ThrowsRuntimeException() {
        when(customerPhotoService.createPhoto(any(UUID.class), any(CustomerPhotoRequestDto.class)))
                .thenThrow(new RuntimeException("Unexpected error"));

        Exception exception = assertThrows(RuntimeException.class, () ->
                customerPhotoController.createPhoto(customerId, requestDto));

        assertEquals("Unexpected error", exception.getMessage());
    }

    @Test
    void testCreatePhoto_WhenCapturedByNotFound_ThrowsException() {
        when(customerPhotoService.createPhoto(any(UUID.class), any(CustomerPhotoRequestDto.class)))
                .thenThrow(new ResourceNotFoundException("Captured by user not found"));

        Exception exception = assertThrows(ResourceNotFoundException.class, () ->
                customerPhotoController.createPhoto(customerId, requestDto));

        assertTrue(exception.getMessage().contains("user"));
    }
}