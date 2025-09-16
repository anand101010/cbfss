package com.incede.nbfc.core.monolith.customer.service;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerNotificationPreference;
import com.incede.nbfc.core.monolith.customer.dto.CustomerNotificationPreferenceRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerNotificationPreferenceResponseDto;
import com.incede.nbfc.core.monolith.customer.repository.CustomerNotificationPreferenceRepository;
import com.incede.nbfc.core.monolith.customer.repository.CustomerRepository;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ErrorCodes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerNotificationPreferenceServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CustomerNotificationPreferenceRepository notificationRepository;

    @InjectMocks
    private CustomerNotificationPreferenceService service;

    private UUID customerId;
    private Customer customer;
    private CustomerNotificationPreference entity;
    private CustomerNotificationPreferenceRequestDto requestDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customerId = UUID.randomUUID();
        customer = new Customer();
        customer.setIdentity(customerId);
        entity = new CustomerNotificationPreference();

        requestDto = CustomerNotificationPreferenceRequestDto.builder()
                .consentSms(true)
                .consentEmail(true)
                .consentWhatsapp(true)
                .build();
    }

    @Test
    void testSaveNotification_Success() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(notificationRepository.findByCustomer(customer)).thenReturn(Optional.empty());
        when(notificationRepository.save(any(CustomerNotificationPreference.class))).thenReturn(entity);

        CustomerNotificationPreferenceResponseDto result = service.saveNotification(customerId, requestDto);

        assertNotNull(result);
        verify(notificationRepository).save(any(CustomerNotificationPreference.class));
    }

    @Test
    void testUpdateNotification_Success() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(notificationRepository.findByCustomer(customer)).thenReturn(Optional.of(entity));
        when(notificationRepository.save(entity)).thenReturn(entity);

        CustomerNotificationPreferenceResponseDto result = service.updateNotification(customerId, requestDto);

        assertNotNull(result);
        verify(notificationRepository).save(entity);
    }

    @Test
    void testGetNotificationPreferences_Success() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(notificationRepository.findByCustomer(customer)).thenReturn(Optional.of(entity));

        CustomerNotificationPreferenceResponseDto result = service.getNotificationPreferences(customerId);

        assertNotNull(result);
    }

    @Test
    void testSaveNotification_CustomerNotFound() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class,
                () -> service.saveNotification(customerId, requestDto));

        assertEquals(CommonConstants.NOT_FOUND_MESSAGE, ex.getMessage());
        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, ex.getErrorCode());
    }

    @Test
    void testSaveNotification_AlreadyExists() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(notificationRepository.findByCustomer(customer)).thenReturn(Optional.of(entity));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> service.saveNotification(customerId, requestDto));

        assertEquals(CommonConstants.CONFLICT_MESSAGE, ex.getMessage());
        assertEquals(ErrorCodes.CONFLICT, ex.getErrorCode());
    }

    @Test
    void testUpdateNotification_CustomerNotFound() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class,
                () -> service.updateNotification(customerId, requestDto));

        assertEquals(CommonConstants.NOT_FOUND_MESSAGE, ex.getMessage());
        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, ex.getErrorCode());
    }

    @Test
    void testUpdateNotification_PreferenceNotFound() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(notificationRepository.findByCustomer(customer)).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class,
                () -> service.updateNotification(customerId, requestDto));

        assertEquals(CommonConstants.NOT_FOUND_MESSAGE, ex.getMessage());
        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, ex.getErrorCode());
    }

    @Test
    void testGetNotificationPreferences_CustomerNotFound() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class,
                () -> service.getNotificationPreferences(customerId));

        assertEquals(CommonConstants.NOT_FOUND_MESSAGE, ex.getMessage());
        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, ex.getErrorCode());
    }

    @Test
    void testGetNotificationPreferences_PreferenceNotFound() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(notificationRepository.findByCustomer(customer)).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class,
                () -> service.getNotificationPreferences(customerId));

        assertEquals(CommonConstants.NOT_FOUND_MESSAGE, ex.getMessage());
        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, ex.getErrorCode());
    }
}
