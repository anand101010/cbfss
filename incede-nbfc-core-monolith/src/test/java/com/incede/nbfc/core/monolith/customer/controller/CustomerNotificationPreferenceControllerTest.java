package com.incede.nbfc.core.monolith.customer.controller;

import com.incede.nbfc.core.monolith.customer.dto.CustomerNotificationPreferenceRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerNotificationPreferenceResponseDto;
import com.incede.nbfc.core.monolith.customer.service.CustomerNotificationPreferenceService;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ErrorCodes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class CustomerNotificationPreferenceControllerTest {

    @Mock
    private CustomerNotificationPreferenceService notificationService;

    @InjectMocks
    private CustomerNotificationPreferenceController controller;

    private UUID customerId;
    private CustomerNotificationPreferenceRequestDto requestDto;
    private CustomerNotificationPreferenceResponseDto responseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customerId = UUID.randomUUID();

        requestDto = CustomerNotificationPreferenceRequestDto.builder()
                .consentSms(true)
                .consentEmail(true)
                .consentWhatsapp(false)
                .build();

        responseDto = CustomerNotificationPreferenceResponseDto.builder()
                .identity(customerId)
                .notificationPreference(
                        CustomerNotificationPreferenceResponseDto.notificationPreference.builder()
                                .consentSms(true)
                                .consentEmail(true)
                                .consentWhatsapp(false)
                                .build()
                )
                .build();
    }

    @Test
    void testCreateNotificationPreference_Success() {
        when(notificationService.saveNotification(eq(customerId), any(CustomerNotificationPreferenceRequestDto.class)))
                .thenReturn(responseDto);

        ResponseEntity<CustomerNotificationPreferenceResponseDto> result =
                controller.createNotificationPreference(customerId, requestDto);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(responseDto, result.getBody());
    }

    @Test
    void testUpdateNotificationPreference_Success() {
        when(notificationService.updateNotification(eq(customerId), any(CustomerNotificationPreferenceRequestDto.class)))
                .thenReturn(responseDto);

        ResponseEntity<CustomerNotificationPreferenceResponseDto> result =
                controller.updateNotificationPreference(customerId, requestDto);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(responseDto, result.getBody());
    }

    @Test
    void testGetNotificationPreferences_Success() {
        when(notificationService.getNotificationPreferences(eq(customerId)))
                .thenReturn(responseDto);

        ResponseEntity<CustomerNotificationPreferenceResponseDto> result =
                controller.getNotificationPreferences(customerId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(responseDto, result.getBody());
    }

    @Test
    void testCreateNotificationPreference_BusinessException() {
        when(notificationService.saveNotification(eq(customerId), any(CustomerNotificationPreferenceRequestDto.class)))
                .thenThrow(new BusinessException("Conflict", ErrorCodes.CONFLICT));

        BusinessException ex = assertThrows(BusinessException.class, () ->
                controller.createNotificationPreference(customerId, requestDto)
        );

        assertEquals("Conflict", ex.getMessage());
        assertEquals(ErrorCodes.CONFLICT, ex.getErrorCode());
    }

    @Test
    void testUpdateNotificationPreference_BusinessException() {
        when(notificationService.updateNotification(eq(customerId), any(CustomerNotificationPreferenceRequestDto.class)))
                .thenThrow(new BusinessException("Not found", ErrorCodes.RESOURCE_NOT_FOUND));

        BusinessException ex = assertThrows(BusinessException.class, () ->
                controller.updateNotificationPreference(customerId, requestDto)
        );

        assertEquals("Not found", ex.getMessage());
        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, ex.getErrorCode());
    }

    @Test
    void testGetNotificationPreferences_BusinessException() {
        when(notificationService.getNotificationPreferences(eq(customerId)))
                .thenThrow(new BusinessException("Not found", ErrorCodes.RESOURCE_NOT_FOUND));

        BusinessException ex = assertThrows(BusinessException.class, () ->
                controller.getNotificationPreferences(customerId)
        );

        assertEquals("Not found", ex.getMessage());
        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, ex.getErrorCode());
    }
}
