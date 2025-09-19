package com.incede.nbfc.core.monolith.customer.service;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.dto.BasicInformationRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.BasicInformationResponseDto;
import com.incede.nbfc.core.monolith.customer.mapper.BasicInformationMapper;
import com.incede.nbfc.core.monolith.customer.repository.CustomerRepository;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ErrorCodes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BasicInformationServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private BasicInformationMapper customerMapper;

    @InjectMocks
    private BasicInformationService service;

    private BasicInformationRequestDto requestDto;
    private Customer customer;
    private BasicInformationResponseDto responseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        requestDto = new BasicInformationRequestDto();
        requestDto.setTenantId(1);
        requestDto.setAadharVault("vault123");

        customer = new Customer();
        customer.setIdentity(UUID.randomUUID());
        customer.setAadharVaultId("vault123");
        customer.setTenantId(1);

        responseDto = new BasicInformationResponseDto();
    }

    // -------------------- SAVE ----------------------

    @Test
    void saveBasicInformation_success() {
        when(customerRepository.existsByTenantIdAndAadharVaultId(1, "vault123")).thenReturn(false);
        when(customerMapper.toEntity(requestDto)).thenReturn(customer);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerMapper.toResponseDto(customer)).thenReturn(responseDto);

        BasicInformationResponseDto result = service.saveBasicInformation(requestDto);

        assertNotNull(result);
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void saveBasicInformation_duplicate() {
        when(customerRepository.existsByTenantIdAndAadharVaultId(1, "vault123")).thenReturn(true);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> service.saveBasicInformation(requestDto));

        assertEquals(ErrorCodes.CONFLICT, ex.getErrorCode());
    }

    @Test
    void saveBasicInformation_dataIntegrityViolation() {
        when(customerRepository.existsByTenantIdAndAadharVaultId(1, "vault123")).thenReturn(false);
        when(customerMapper.toEntity(requestDto)).thenReturn(customer);
        when(customerRepository.save(any())).thenThrow(new DataIntegrityViolationException("duplicate"));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> service.saveBasicInformation(requestDto));

        assertEquals(ErrorCodes.CONSTRAINT_VIOLATION, ex.getErrorCode());
    }

    @Test
    void saveBasicInformation_illegalArgument() {
        when(customerRepository.existsByTenantIdAndAadharVaultId(1, "vault123")).thenReturn(false);
        when(customerMapper.toEntity(requestDto)).thenThrow(new IllegalArgumentException("invalid"));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> service.saveBasicInformation(requestDto));

        assertEquals(ErrorCodes.VALIDATION_FAILED, ex.getErrorCode());
    }



    @Test
    void updateBasicInformation_notFound() {
        UUID id = UUID.randomUUID();
        when(customerRepository.findByIdentity(id)).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class,
                () -> service.updateBasicInformation(id, requestDto));

        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, ex.getErrorCode());
    }

    @Test
    void updateBasicInformation_duplicate() {
        UUID id = UUID.randomUUID();
        Customer other = new Customer();
        other.setIdentity(UUID.randomUUID());

        when(customerRepository.findByIdentity(id)).thenReturn(Optional.of(customer));
        when(customerRepository.findByTenantIdAndAadharVaultId(1, "vault123"))
                .thenReturn(Optional.of(other));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> service.updateBasicInformation(id, requestDto));

        assertEquals(ErrorCodes.CONFLICT, ex.getErrorCode());
    }

    @Test
    void updateBasicInformation_success() {
        UUID id = UUID.randomUUID();
        customer.setIdentity(id); // ensure same ID to avoid false duplicate
        when(customerRepository.findByIdentity(id)).thenReturn(Optional.of(customer));
        when(customerRepository.findByTenantIdAndAadharVaultId(1, "vault123"))
                .thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerMapper.toResponseDto(customer)).thenReturn(responseDto);

        BasicInformationResponseDto result = service.updateBasicInformation(id, requestDto);

        assertNotNull(result);
        verify(customerRepository).save(customer);
    }

    @Test
    void updateBasicInformation_dataIntegrityViolation() {
        UUID id = UUID.randomUUID();
        customer.setIdentity(id); // same ID => not treated as duplicate
        when(customerRepository.findByIdentity(id)).thenReturn(Optional.of(customer));
        when(customerRepository.findByTenantIdAndAadharVaultId(1, "vault123"))
                .thenReturn(Optional.of(customer));
        when(customerRepository.save(any()))
                .thenThrow(new DataIntegrityViolationException("duplicate"));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> service.updateBasicInformation(id, requestDto));

        assertEquals(ErrorCodes.CONSTRAINT_VIOLATION, ex.getErrorCode());
    }


    @Test
    void updateBasicInformation_illegalArgument() {
        UUID id = UUID.randomUUID();
        when(customerRepository.findByIdentity(id)).thenReturn(Optional.of(customer));
        doThrow(new IllegalArgumentException("invalid"))
                .when(customerMapper).updateEntityFromDto(customer, requestDto);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> service.updateBasicInformation(id, requestDto));

        assertEquals(ErrorCodes.VALIDATION_FAILED, ex.getErrorCode());
    }

    // -------------------- GET ----------------------

    @Test
    void getBasicInformationByUuid_success() {
        UUID id = UUID.randomUUID();
        when(customerRepository.findByIdentity(id)).thenReturn(Optional.of(customer));
        when(customerMapper.toResponseDto(customer)).thenReturn(responseDto);

        BasicInformationResponseDto result = service.getBasicInformationByUuid(id);

        assertNotNull(result);
    }

    @Test
    void getBasicInformationByUuid_notFound() {
        UUID id = UUID.randomUUID();
        when(customerRepository.findByIdentity(id)).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class,
                () -> service.getBasicInformationByUuid(id));

        assertEquals(ErrorCodes.NOT_FOUND, ex.getErrorCode());
    }
}
