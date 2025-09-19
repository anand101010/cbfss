package com.incede.nbfc.core.monolith.customer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerPhoto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerPhotoRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerPhotoResponseDto;
import com.incede.nbfc.core.monolith.customer.mapper.CustomerPhotoMapper;
import com.incede.nbfc.core.monolith.customer.repository.CustomerPhotoRepository;
import com.incede.nbfc.core.monolith.customer.repository.CustomerRepository;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ResourceNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerPhotoServiceTest {

    @Mock private CustomerRepository customerRepository;
    @Mock private CustomerPhotoRepository photoRepository;
    @Mock private CustomerPhotoMapper customerPhotoMapper;
    @Mock private ObjectMapper objectMapper;
    @Mock private Validator validator;
    @Mock private MultipartFile file;

    @InjectMocks private CustomerPhotoService service;

    private final UUID customerId = UUID.randomUUID();
    private Customer customer;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        customer = new Customer();
        customer.setIdentity(customerId);
    }

    @Test
    void createPhoto_success() throws Exception {
        // Populate all mandatory fields
        CustomerPhotoRequestDto dto = new CustomerPhotoRequestDto();
        dto.setCaptureTime("2025-09-19T10:00:00");
        dto.setLocationDescription("Office");
        dto.setCaptureDevice("Mobile");
        dto.setAccuracy(BigDecimal.valueOf(1.0));
        dto.setCapturedBy(1);
        dto.setPhotoLivenessStatus("verified");
        dto.setLongitude(BigDecimal.valueOf(77.5946));
        dto.setLatitude(BigDecimal.valueOf(12.9716));

        CustomerPhoto entity = new CustomerPhoto();
        CustomerPhoto saved = new CustomerPhoto();
        CustomerPhotoResponseDto response = new CustomerPhotoResponseDto();

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(objectMapper.readValue(anyString(), eq(CustomerPhotoRequestDto.class))).thenReturn(dto);
        when(validator.validate(dto)).thenReturn(Collections.emptySet());
        when(customerPhotoMapper.toEntity(dto)).thenReturn(entity);
        when(photoRepository.save(entity)).thenReturn(saved);
        when(customerPhotoMapper.toResponseDto(customer, List.of(saved))).thenReturn(response);

        CustomerPhotoResponseDto result = service.createPhoto(customerId, "{}", file);

        assertNotNull(result);
        verify(photoRepository).save(entity);
    }

    @Test
    void createPhoto_validationFails() throws Exception {
        CustomerPhotoRequestDto dto = new CustomerPhotoRequestDto();

        // Mock a violation (we don't check exact message)
        @SuppressWarnings("unchecked")
        ConstraintViolation<CustomerPhotoRequestDto> violation = mock(ConstraintViolation.class);
        Path path = mock(Path.class);
        when(path.toString()).thenReturn("someField");
        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn("must not be null");

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(objectMapper.readValue(anyString(), eq(CustomerPhotoRequestDto.class))).thenReturn(dto);
        when(validator.validate(dto)).thenReturn(Set.of(violation));

        assertThrows(BusinessException.class,
                () -> service.createPhoto(customerId, "{}", file));
    }

    @Test
    void createPhoto_invalidJson() throws Exception {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(objectMapper.readValue(anyString(), eq(CustomerPhotoRequestDto.class)))
                .thenThrow(new JsonProcessingException("bad json") {});

        assertThrows(BusinessException.class,
                () -> service.createPhoto(customerId, "{bad}", file));
    }

    @Test
    void createPhoto_constraintViolation() throws Exception {
        CustomerPhotoRequestDto dto = new CustomerPhotoRequestDto();
        dto.setCaptureTime("2025-09-19T10:00:00");
        dto.setLocationDescription("Office");
        dto.setCaptureDevice("Mobile");
        dto.setAccuracy(BigDecimal.valueOf(1.0));
        dto.setCapturedBy(1);
        dto.setPhotoLivenessStatus("verified");
        dto.setLongitude(BigDecimal.valueOf(77.5946));
        dto.setLatitude(BigDecimal.valueOf(12.9716));

        CustomerPhoto entity = new CustomerPhoto();

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(objectMapper.readValue(anyString(), eq(CustomerPhotoRequestDto.class))).thenReturn(dto);
        when(validator.validate(dto)).thenReturn(Collections.emptySet());
        when(customerPhotoMapper.toEntity(dto)).thenReturn(entity);
        when(photoRepository.save(entity)).thenThrow(new DataIntegrityViolationException("constraint"));

        assertThrows(BusinessException.class,
                () -> service.createPhoto(customerId, "{}", file));
    }

    @Test
    void getCustomerPhotos_success() {
        CustomerPhoto photo = new CustomerPhoto();
        List<CustomerPhoto> photos = List.of(photo);
        CustomerPhotoResponseDto response = new CustomerPhotoResponseDto();

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(photoRepository.findByCustomerIdentityAndIsDelFalseOrderByCaptureTimeDesc(customerId))
                .thenReturn(photos);
        when(customerPhotoMapper.toResponseDto(customer, photos)).thenReturn(response);

        CustomerPhotoResponseDto result = service.getCustomerPhotos(customerId);

        assertNotNull(result);
        verify(customerPhotoMapper).toResponseDto(customer, photos);
    }

    @Test
    void getCustomerPhotos_noCustomer() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.getCustomerPhotos(customerId));
    }

    @Test
    void getCustomerPhotos_noPhotos() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(photoRepository.findByCustomerIdentityAndIsDelFalseOrderByCaptureTimeDesc(customerId))
                .thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class,
                () -> service.getCustomerPhotos(customerId));
    }
}
