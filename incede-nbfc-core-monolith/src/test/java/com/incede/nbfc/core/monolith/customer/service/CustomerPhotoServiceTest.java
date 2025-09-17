package com.incede.nbfc.core.monolith.customer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerPhoto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerPhotoRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerPhotoResponseDto;
import com.incede.nbfc.core.monolith.customer.enums.PhotoStatus;
import com.incede.nbfc.core.monolith.customer.mapper.CustomerPhotoMapper;
import com.incede.nbfc.core.monolith.customer.repository.CustomerPhotoRepository;
import com.incede.nbfc.core.monolith.customer.repository.CustomerRepository;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ErrorCodes;
import com.incede.nbfc.core.monolith.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CustomerPhotoServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerPhotoRepository photoRepository;

    @Mock
    private CustomerPhotoMapper customerPhotoMapper;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private CustomerPhotoService customerPhotoService;

    private UUID customerId;
    private Customer customer;
    private CustomerPhotoRequestDto requestDto;
    private CustomerPhoto photo;
    private CustomerPhotoResponseDto responseDto;
    private MultipartFile mockFile;
    private String requestJson;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        customerId = UUID.randomUUID();

        customer = new Customer();
        customer.setIdentity(customerId);
        customer.setCustomerCode("CUST123");

        requestDto = new CustomerPhotoRequestDto();
        requestDto.setLatitude(BigDecimal.valueOf(12.34));
        requestDto.setLongitude(BigDecimal.valueOf(56.78));
        requestDto.setCapturedBy(1);
        requestDto.setCaptureDevice("mobile");
        requestDto.setCaptureTime("2025-09-08T17:00:00");


        requestJson = "{ \"latitude\": 12.34, \"longitude\": 56.78, \"capturedBy\": 1, \"captureDevice\": \"mobile\", \"captureTime\": \"2025-09-08T17:00:00\", \"filePath\": \"/images/photo.png\", \"status\": \"PENDING\", \"createdBy\": 1 }";

        photo = new CustomerPhoto();
        photo.setPhotoId(1);
        photo.setPhotoRefId(12345);
        photo.setCustomer(customer);
        photo.setStatus(PhotoStatus.PENDING);

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
    void testCreatePhoto_Success() throws Exception {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(objectMapper.readValue(requestJson, CustomerPhotoRequestDto.class)).thenReturn(requestDto);
        when(customerPhotoMapper.toEntity(requestDto)).thenReturn(photo);
        when(photoRepository.save(any(CustomerPhoto.class))).thenReturn(photo);
        when(customerPhotoMapper.toResponseDto(customer, List.of(photo))).thenReturn(responseDto);

        CustomerPhotoResponseDto result = customerPhotoService.createPhoto(customerId, requestJson, mockFile);

        assertNotNull(result);
        assertEquals("CUST123", result.getCustomerCode());
        verify(photoRepository, times(1)).save(any(CustomerPhoto.class));
    }

    @Test
    void testCreatePhoto_CustomerNotFound() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.empty());
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> customerPhotoService.createPhoto(customerId, requestJson, mockFile));
        assertEquals(CommonConstants.NOT_FOUND_MESSAGE, ex.getMessage());
    }

    @Test
    void testCreatePhoto_DataIntegrityViolation() throws Exception {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(objectMapper.readValue(requestJson, CustomerPhotoRequestDto.class)).thenReturn(requestDto);
        when(customerPhotoMapper.toEntity(requestDto)).thenReturn(photo);
        when(photoRepository.save(any(CustomerPhoto.class))).thenThrow(new DataIntegrityViolationException("constraint"));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> customerPhotoService.createPhoto(customerId, requestJson, mockFile));
        assertEquals(CommonConstants.CONSTRAIN_VIOLATION, ex.getMessage());
        assertEquals(ErrorCodes.CONSTRAINT_VIOLATION, ex.getErrorCode());
    }

    @Test
    void testCreatePhoto_ValidationFailure() throws Exception {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(objectMapper.readValue(requestJson, CustomerPhotoRequestDto.class))
                .thenThrow(new IllegalArgumentException("Invalid data"));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> customerPhotoService.createPhoto(customerId, requestJson, mockFile));
        assertEquals("Invalid data", ex.getMessage());
        assertEquals(ErrorCodes.VALIDATION_FAILED, ex.getErrorCode());
    }

    @Test
    void testCreatePhoto_WhenFileIsNull() throws Exception {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(objectMapper.readValue(requestJson, CustomerPhotoRequestDto.class)).thenReturn(requestDto);
        when(customerPhotoMapper.toEntity(requestDto)).thenReturn(photo);
        when(photoRepository.save(any(CustomerPhoto.class))).thenReturn(photo);
        when(customerPhotoMapper.toResponseDto(customer, List.of(photo))).thenReturn(responseDto);

        CustomerPhotoResponseDto result = customerPhotoService.createPhoto(customerId, requestJson, null);

        assertNotNull(result);
        assertEquals("CUST123", result.getCustomerCode());
    }

    @Test
    void testCreatePhoto_WhenRepositoryThrowsUnexpectedError() throws Exception {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(objectMapper.readValue(requestJson, CustomerPhotoRequestDto.class)).thenReturn(requestDto);
        when(customerPhotoMapper.toEntity(requestDto)).thenReturn(photo);
        when(photoRepository.save(any(CustomerPhoto.class))).thenThrow(new RuntimeException("DB down"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> customerPhotoService.createPhoto(customerId, requestJson, mockFile));
        assertEquals("DB down", ex.getMessage());
    }



    @Test
    void testGetCustomerPhotos_Success() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(photoRepository.findByCustomerIdentityAndIsDelFalseOrderByCaptureTimeDesc(customerId))
                .thenReturn(List.of(photo));
        when(customerPhotoMapper.toResponseDto(customer, List.of(photo))).thenReturn(responseDto);

        CustomerPhotoResponseDto result = customerPhotoService.getCustomerPhotos(customerId);

        assertNotNull(result);
        assertEquals("CUST123", result.getCustomerCode());
        assertEquals(1, result.getPhoto().size());
    }

    @Test
    void testGetCustomerPhotos_NoPhotosFound() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(photoRepository.findByCustomerIdentityAndIsDelFalseOrderByCaptureTimeDesc(customerId))
                .thenReturn(List.of());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> customerPhotoService.getCustomerPhotos(customerId));
        assertEquals(CommonConstants.NOT_FOUND_MESSAGE, ex.getMessage());
    }

    @Test
    void testGetCustomerPhotos_CustomerNotFound() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> customerPhotoService.getCustomerPhotos(customerId));
        assertEquals(CommonConstants.NOT_FOUND_MESSAGE, ex.getMessage());
    }

    @Test
    void testGetCustomerPhotos_WhenRepositoryThrowsUnexpectedError() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(photoRepository.findByCustomerIdentityAndIsDelFalseOrderByCaptureTimeDesc(customerId))
                .thenThrow(new RuntimeException("DB issue"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> customerPhotoService.getCustomerPhotos(customerId));
        assertEquals("DB issue", ex.getMessage());
    }
}
