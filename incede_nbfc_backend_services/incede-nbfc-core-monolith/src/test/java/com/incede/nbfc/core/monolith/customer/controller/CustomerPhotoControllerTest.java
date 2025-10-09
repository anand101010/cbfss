package com.incede.nbfc.core.monolith.customer.controller;

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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

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
    private String requestJson;
    private CustomerPhotoResponseDto responseDto;
    private MultipartFile mockFile;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customerId = UUID.randomUUID();

        // requestJson as a JSON string
        requestJson = "{\n" +
                "  \"latitude\": 12.34,\n" +
                "  \"longitude\": 56.78,\n" +
                "  \"capturedBy\": 1,\n" +
                "  \"captureDevice\": \"mobile\",\n" +
                "  \"captureTime\": \"2025-09-08T17:00:00\",\n" +
                "  \"status\": \"PENDING\",\n" +
                "  \"createdBy\": 1\n" +
                "}";

        CustomerPhotoResponseDto.PhotoDetail detail = CustomerPhotoResponseDto.PhotoDetail.builder()
                .photoId(1)
                .photoRefId(12345)
                .status(PhotoStatus.PENDING)
                .captureTime(LocalDateTime.now())
                .build();

        responseDto = CustomerPhotoResponseDto.builder()
                .identity(customerId)
                .customerCode("CUST123")
                .status("IN_PROGRESS")
                .photo(List.of(detail))
                .build();

        mockFile = new MockMultipartFile(
                "file",
                "photo.png",
                "image/png",
                "dummy image content".getBytes()
        );
    }

    @Test
    void testCreatePhoto_Success() {
        when(customerPhotoService.createPhoto(any(UUID.class), any(String.class), any(MultipartFile.class)))
                .thenReturn(responseDto);

        ResponseEntity<CustomerPhotoResponseDto> response =
                customerPhotoController.createPhoto(customerId, requestJson, mockFile);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(responseDto, response.getBody());
    }

    @Test
    void testGetPhoto_Success() {
        when(customerPhotoService.getCustomerPhotos(customerId)).thenReturn(responseDto);

        ResponseEntity<CustomerPhotoResponseDto> response =
                customerPhotoController.getPhoto(customerId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(responseDto, response.getBody());
    }

    @Test
    void testCreatePhoto_WhenCustomerNotFound_ThrowsException() {
        when(customerPhotoService.createPhoto(any(UUID.class), any(String.class), any(MultipartFile.class)))
                .thenThrow(new ResourceNotFoundException("Customer", customerId.toString()));

        Exception exception = assertThrows(ResourceNotFoundException.class, () ->
                customerPhotoController.createPhoto(customerId, requestJson, mockFile));

        assertTrue(exception.getMessage().contains("Customer"));
    }

    @Test
    void testGetPhoto_WhenNoPhotosFound_ThrowsException() {
        when(customerPhotoService.getCustomerPhotos(customerId))
                .thenThrow(new ResourceNotFoundException("CustomerPhoto", customerId.toString()));

        Exception exception = assertThrows(ResourceNotFoundException.class, () ->
                customerPhotoController.getPhoto(customerId));

        assertTrue(exception.getMessage().contains("CustomerPhoto"));
    }

    @Test
    void testCreatePhoto_WhenUnexpectedErrorOccurs_ThrowsRuntimeException() {
        when(customerPhotoService.createPhoto(any(UUID.class), any(String.class), any(MultipartFile.class)))
                .thenThrow(new RuntimeException("Unexpected error"));

        Exception exception = assertThrows(RuntimeException.class, () ->
                customerPhotoController.createPhoto(customerId, requestJson, mockFile));

        assertEquals("Unexpected error", exception.getMessage());
    }
}
