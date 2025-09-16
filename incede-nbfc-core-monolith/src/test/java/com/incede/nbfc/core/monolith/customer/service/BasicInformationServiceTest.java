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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

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
    private BasicInformationService basicInformationService;

    private BasicInformationRequestDto requestDto;
    private Customer customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        requestDto = new BasicInformationRequestDto();
        requestDto.setTenantId(1);
        requestDto.setAadharVault("");

        customer = new Customer();
        customer.setIdentity(UUID.randomUUID());
        customer.setAadharVaultId("vault123");
    }

    @Test
    void testSaveBasicInformation_success() {
        when(customerRepository.existsByTenantIdAndAadharVaultId(
                requestDto.getTenantId(), requestDto.getAadharVault()))
                .thenReturn(false);
        when(customerMapper.toEntity(requestDto)).thenReturn(customer);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerMapper.toResponseDto(customer)).thenReturn(new BasicInformationResponseDto());

        BasicInformationResponseDto response = basicInformationService.saveBasicInformation(requestDto);

        assertNotNull(response);
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void testSaveBasicInformation_dataIntegrityViolation() {
        when(customerRepository.existsByTenantIdAndAadharVaultId(
                requestDto.getTenantId(), requestDto.getAadharVault()))
                .thenReturn(false);
        when(customerMapper.toEntity(requestDto)).thenReturn(customer);
        when(customerRepository.save(customer)).thenThrow(new DataIntegrityViolationException("DB constraint"));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> basicInformationService.saveBasicInformation(requestDto));

        assertEquals(CommonConstants.CONSTRAIN_VIOLATION, exception.getMessage());
        assertEquals(ErrorCodes.CONSTRAINT_VIOLATION, exception.getErrorCode());
    }

    @Test
    void testUpdateBasicInformation_success() {
        UUID identity = UUID.randomUUID();
        Customer existingCustomer = new Customer();
        existingCustomer.setIdentity(identity);
        existingCustomer.setAadharVaultId("vault123");

        when(customerRepository.findByIdentity(identity)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.findByTenantIdAndAadharVaultId(
                requestDto.getTenantId(), requestDto.getAadharVault()))
                .thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(existingCustomer)).thenReturn(existingCustomer);
        when(customerMapper.toResponseDto(existingCustomer)).thenReturn(new BasicInformationResponseDto());

        BasicInformationResponseDto response = basicInformationService.updateBasicInformation(identity, requestDto);

        assertNotNull(response);
        verify(customerRepository, times(1)).save(existingCustomer);
    }

    @Test
    void testUpdateBasicInformation_duplicateAadharVault() {
        UUID identity = UUID.randomUUID();
        Customer duplicateCustomer = new Customer();
        duplicateCustomer.setIdentity(UUID.randomUUID());
        duplicateCustomer.setAadharVaultId("vault123");

        when(customerRepository.findByIdentity(identity)).thenReturn(Optional.of(customer));
        when(customerRepository.findByTenantIdAndAadharVaultId(
                requestDto.getTenantId(), requestDto.getAadharVault()))
                .thenReturn(Optional.of(duplicateCustomer));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> basicInformationService.updateBasicInformation(identity, requestDto));

        assertEquals(CommonConstants.CONFLICT_MESSAGE, exception.getMessage());
        assertEquals(ErrorCodes.CONFLICT, exception.getErrorCode());
    }

    @Test
    void testUpdateBasicInformation_dataIntegrityViolation() {
        UUID identity = UUID.randomUUID();

        when(customerRepository.findByIdentity(identity)).thenReturn(Optional.of(customer));
        doNothing().when(customerMapper).updateEntityFromDto(customer, requestDto);
        when(customerRepository.findByTenantIdAndAadharVaultId(
                requestDto.getTenantId(), requestDto.getAadharVault()))
                .thenReturn(Optional.empty());
        when(customerRepository.save(customer)).thenThrow(new DataIntegrityViolationException("DB constraint"));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> basicInformationService.updateBasicInformation(identity, requestDto));

        assertEquals(CommonConstants.CONSTRAIN_VIOLATION, exception.getMessage());
        assertEquals(ErrorCodes.CONSTRAINT_VIOLATION, exception.getErrorCode());
    }

    @Test
    void testGetBasicInformationByUuid_success() {
        UUID identity = UUID.randomUUID();
        customer.setIdentity(identity);

        when(customerRepository.findByIdentity(identity)).thenReturn(Optional.of(customer));
        when(customerMapper.toResponseDto(customer)).thenReturn(new BasicInformationResponseDto());

        BasicInformationResponseDto response = basicInformationService.getBasicInformationByUuid(identity);

        assertNotNull(response);
        verify(customerRepository, times(1)).findByIdentity(identity);
    }

    @Test
    void testGetBasicInformationByUuid_notFound() {
        UUID identity = UUID.randomUUID();
        when(customerRepository.findByIdentity(identity)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> basicInformationService.getBasicInformationByUuid(identity));

        assertEquals(CommonConstants.NOT_FOUND_MESSAGE, exception.getMessage());
        assertEquals(ErrorCodes.NOT_FOUND, exception.getErrorCode());
    }

}
