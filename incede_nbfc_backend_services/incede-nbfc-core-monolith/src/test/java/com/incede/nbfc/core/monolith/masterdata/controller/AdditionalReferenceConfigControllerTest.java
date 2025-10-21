package com.incede.nbfc.core.monolith.masterdata.controller;

import com.incede.nbfc.core.monolith.exception.ResourceNotFoundException;
import com.incede.nbfc.core.monolith.masterdata.dto.AdditionalReferenceConfigDto;
import com.incede.nbfc.core.monolith.masterdata.service.AdditionalReferenceConfigService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdditionalReferenceConfigControllerTest {

    @Mock
    private AdditionalReferenceConfigService additionalReferenceConfigService;

    @InjectMocks
    private AdditionalReferenceConfigController additionalReferenceConfigController;

    private UUID tenantIdentity;
    private UUID productServiceIdentity;
    private AdditionalReferenceConfigDto dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tenantIdentity = UUID.randomUUID();
        productServiceIdentity = UUID.randomUUID();

    }

    @Test
    void testGetAdditionalReferenceConfigs_Success() {
        List<AdditionalReferenceConfigDto> mockList = Arrays.asList(dto);
        when(additionalReferenceConfigService.getAdditionalReferenceConfig(tenantIdentity, productServiceIdentity))
                .thenReturn(mockList);

        ResponseEntity<List<AdditionalReferenceConfigDto>> response =
                additionalReferenceConfigController.getAdditionalReferenceConfigs(tenantIdentity, productServiceIdentity);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        verify(additionalReferenceConfigService, times(1))
                .getAdditionalReferenceConfig(tenantIdentity, productServiceIdentity);
    }

    @Test
    void testGetAdditionalReferenceConfigs_NoConfigsFound() {
        when(additionalReferenceConfigService.getAdditionalReferenceConfig(tenantIdentity, productServiceIdentity))
                .thenReturn(List.of());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                additionalReferenceConfigController.getAdditionalReferenceConfigs(tenantIdentity, productServiceIdentity));

        assertEquals("No active reference configs found for the provided details.", exception.getMessage());
        verify(additionalReferenceConfigService, times(1))
                .getAdditionalReferenceConfig(tenantIdentity, productServiceIdentity);
    }

    @Test
    void testGetAdditionalReferenceConfigs_NullParams() {
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                additionalReferenceConfigController.getAdditionalReferenceConfigs(null, null));

        assertEquals("Tenant Identity and Product Service Identity are required.", exception.getMessage());
        verifyNoInteractions(additionalReferenceConfigService);
    }
}
