package com.incede.nbfc.core.monolith.customer.service;

import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerPhoto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerPhotoRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerPhotoResponseDto;
import com.incede.nbfc.core.monolith.customer.mapper.CustomerPhotoMapper;
import com.incede.nbfc.core.monolith.customer.repository.CustomerPhotoRepository;
import com.incede.nbfc.core.monolith.customer.repository.CustomerRepository;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ResourceNotFoundException;
import com.incede.nbfc.core.monolith.user.domain.entity.User;
import com.incede.nbfc.core.monolith.user.repository.UserRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerPhotoServiceTest {

    @Mock private CustomerRepository customerRepository;
    @Mock private CustomerPhotoRepository photoRepository;
    @Mock private CustomerPhotoMapper customerPhotoMapper;
    @Mock private UserRepository userRepository;
    @Mock private Validator validator;

    @InjectMocks private CustomerPhotoService service;

    private final UUID customerId = UUID.randomUUID();
    private final UUID capturedById = UUID.randomUUID();
    private Customer customer;
    private User capturedByUser;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        customer = new Customer();
        customer.setIdentity(customerId);

        capturedByUser = new User();
        capturedByUser.setIdentity(capturedById);
    }

    @Test
    void createPhoto_success() {
        // Create complete request DTO with all mandatory fields
        CustomerPhotoRequestDto dto = new CustomerPhotoRequestDto();
        dto.setCapturedBy(capturedById);
        dto.setCaptureTime("2025-09-19T10:00:00");
        dto.setLocationDescription("Office");
        dto.setCaptureDevice("Mobile");
        dto.setAccuracy(BigDecimal.valueOf(1.0));
        dto.setPhotoLivenessStatus("verified");
        dto.setLongitude(BigDecimal.valueOf(77.5946));
        dto.setLatitude(BigDecimal.valueOf(12.9716));
        dto.setPhotoRefId("PHOTO12345");
        dto.setFilePath("/photos/customer/photo.png");

        CustomerPhoto entity = new CustomerPhoto();
        CustomerPhoto saved = new CustomerPhoto();
        saved.setPhotoId(1);
        CustomerPhotoResponseDto response = new CustomerPhotoResponseDto();

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(validator.validate(dto)).thenReturn(Collections.emptySet());
        when(userRepository.findByIdentity(capturedById)).thenReturn(Optional.of(capturedByUser));
        when(customerPhotoMapper.toEntity(dto)).thenReturn(entity);
        when(photoRepository.save(entity)).thenReturn(saved);
        when(customerPhotoMapper.toResponseDto(customer, List.of(saved))).thenReturn(response);

        CustomerPhotoResponseDto result = service.createPhoto(customerId, dto);

        assertNotNull(result);
        verify(customerRepository).findByIdentity(customerId);
        verify(userRepository).findByIdentity(capturedById);
        verify(photoRepository).save(entity);
        verify(customerPhotoMapper).toResponseDto(customer, List.of(saved));
    }

    @Test
    void createPhoto_customerNotFound() {
        CustomerPhotoRequestDto dto = new CustomerPhotoRequestDto();
        dto.setCapturedBy(capturedById);

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.createPhoto(customerId, dto));

        verify(customerRepository).findByIdentity(customerId);
        verify(photoRepository, never()).save(any());
    }

    @Test
    void createPhoto_validationFails() {
        CustomerPhotoRequestDto dto = new CustomerPhotoRequestDto();
        dto.setCapturedBy(capturedById);

        @SuppressWarnings("unchecked")
        ConstraintViolation<CustomerPhotoRequestDto> violation = mock(ConstraintViolation.class);
        Path path = mock(Path.class);
        when(path.toString()).thenReturn("captureTime");
        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn("must not be null");

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(validator.validate(dto)).thenReturn(Set.of(violation));

        assertThrows(BusinessException.class,
                () -> service.createPhoto(customerId, dto));

        verify(photoRepository, never()).save(any());
    }

    @Test
    void createPhoto_capturedByUserNotFound() {
        CustomerPhotoRequestDto dto = new CustomerPhotoRequestDto();
        dto.setCapturedBy(capturedById);
        dto.setCaptureTime("2025-09-19T10:00:00");
        dto.setLocationDescription("Office");
        dto.setCaptureDevice("Mobile");
        dto.setAccuracy(BigDecimal.valueOf(1.0));
        dto.setPhotoLivenessStatus("verified");
        dto.setLongitude(BigDecimal.valueOf(77.5946));
        dto.setLatitude(BigDecimal.valueOf(12.9716));
        dto.setPhotoRefId("PHOTO12345");
        dto.setFilePath("/photos/customer/photo.png");

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(validator.validate(dto)).thenReturn(Collections.emptySet());
        when(userRepository.findByIdentity(capturedById)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class,
                () -> service.createPhoto(customerId, dto));

        verify(photoRepository, never()).save(any());
    }

    @Test
    void createPhoto_constraintViolation() {
        CustomerPhotoRequestDto dto = new CustomerPhotoRequestDto();
        dto.setCapturedBy(capturedById);
        dto.setCaptureTime("2025-09-19T10:00:00");
        dto.setLocationDescription("Office");
        dto.setCaptureDevice("Mobile");
        dto.setAccuracy(BigDecimal.valueOf(1.0));
        dto.setPhotoLivenessStatus("verified");
        dto.setLongitude(BigDecimal.valueOf(77.5946));
        dto.setLatitude(BigDecimal.valueOf(12.9716));
        dto.setPhotoRefId("PHOTO12345");
        dto.setFilePath("/photos/customer/photo.png");

        CustomerPhoto entity = new CustomerPhoto();

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(validator.validate(dto)).thenReturn(Collections.emptySet());
        when(userRepository.findByIdentity(capturedById)).thenReturn(Optional.of(capturedByUser));
        when(customerPhotoMapper.toEntity(dto)).thenReturn(entity);
        when(photoRepository.save(entity)).thenThrow(new DataIntegrityViolationException("constraint"));

        assertThrows(BusinessException.class,
                () -> service.createPhoto(customerId, dto));
    }

    @Test
    void getCustomerPhotos_success() {
        CustomerPhoto photo = new CustomerPhoto();
        photo.setPhotoId(1);
        List<CustomerPhoto> photos = List.of(photo);
        CustomerPhotoResponseDto response = new CustomerPhotoResponseDto();

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(photoRepository.findByCustomerIdentityAndIsDelFalseOrderByCaptureTimeDesc(customerId))
                .thenReturn(photos);
        when(customerPhotoMapper.toResponseDto(customer, photos)).thenReturn(response);

        CustomerPhotoResponseDto result = service.getCustomerPhotos(customerId);

        assertNotNull(result);
        verify(customerRepository).findByIdentity(customerId);
        verify(photoRepository).findByCustomerIdentityAndIsDelFalseOrderByCaptureTimeDesc(customerId);
        verify(customerPhotoMapper).toResponseDto(customer, photos);
    }

    @Test
    void getCustomerPhotos_customerNotFound() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.getCustomerPhotos(customerId));

        verify(customerRepository).findByIdentity(customerId);
        verify(photoRepository, never()).findByCustomerIdentityAndIsDelFalseOrderByCaptureTimeDesc(any());
    }

    @Test
    void getCustomerPhotos_noPhotosFound() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(photoRepository.findByCustomerIdentityAndIsDelFalseOrderByCaptureTimeDesc(customerId))
                .thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class,
                () -> service.getCustomerPhotos(customerId));

        verify(customerRepository).findByIdentity(customerId);
        verify(photoRepository).findByCustomerIdentityAndIsDelFalseOrderByCaptureTimeDesc(customerId);
    }

    @Test
    void createPhoto_multipleValidationErrors() {
        CustomerPhotoRequestDto dto = new CustomerPhotoRequestDto();
        dto.setCapturedBy(capturedById);

        @SuppressWarnings("unchecked")
        ConstraintViolation<CustomerPhotoRequestDto> violation1 = mock(ConstraintViolation.class);
        Path path1 = mock(Path.class);
        when(path1.toString()).thenReturn("captureTime");
        when(violation1.getPropertyPath()).thenReturn(path1);
        when(violation1.getMessage()).thenReturn("must not be null");

        @SuppressWarnings("unchecked")
        ConstraintViolation<CustomerPhotoRequestDto> violation2 = mock(ConstraintViolation.class);
        Path path2 = mock(Path.class);
        when(path2.toString()).thenReturn("photoRefId");
        when(violation2.getPropertyPath()).thenReturn(path2);
        when(violation2.getMessage()).thenReturn("must not be blank");

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(validator.validate(dto)).thenReturn(Set.of(violation1, violation2));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> service.createPhoto(customerId, dto));

        assertTrue(exception.getMessage().contains("captureTime") ||
                exception.getMessage().contains("photoRefId"));
    }
}