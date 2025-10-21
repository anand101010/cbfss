package com.incede.nbfc.core.monolith.masterdata.service;

import com.incede.nbfc.core.monolith.exception.ResourceNotFoundException;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.AdditionalReferenceConfig;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.ProductService;
import com.incede.nbfc.core.monolith.masterdata.dto.AdditionalReferenceConfigDto;
import com.incede.nbfc.core.monolith.masterdata.repository.AdditionalReferenceConfigRepository;
import com.incede.nbfc.core.monolith.masterdata.repository.ProductServiceRepository;
import com.incede.nbfc.core.monolith.tenant.domain.entity.Tenant;
import com.incede.nbfc.core.monolith.tenant.repository.TenantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdditionalReferenceConfigServiceTest {

    @Mock
    private AdditionalReferenceConfigRepository configRepository;

    @Mock
    private TenantRepository tenantRepository;

    @Mock
    private ProductServiceRepository productServiceRepository;

    @InjectMocks
    private AdditionalReferenceConfigService service;

    private final UUID tenantIdentity = UUID.randomUUID();
    private final UUID productServiceIdentity = UUID.randomUUID();

    private final Tenant tenant = new Tenant() {{
        setTenantId(1);
    }};

    private final ProductService productService = new ProductService() {{
        setProductServiceId(1);
    }};

    @Test
    void testGetAdditionalReferenceConfig_success() {
        AdditionalReferenceConfig config = new AdditionalReferenceConfig();

        when(tenantRepository.findByIdentity(tenantIdentity)).thenReturn(Optional.of(tenant));
        when(productServiceRepository.findByIdentity(productServiceIdentity)).thenReturn(Optional.of(productService));
        when(configRepository.findRefernceActiveByTenantAndProduct(1, 1)).thenReturn(List.of(config));

        List<AdditionalReferenceConfigDto> result = service.getAdditionalReferenceConfig(tenantIdentity, productServiceIdentity);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetAdditionalReferenceConfig_tenantNotFound() {
        when(tenantRepository.findByIdentity(tenantIdentity)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                service.getAdditionalReferenceConfig(tenantIdentity, productServiceIdentity));

        assertEquals("Invalid Tenant Identity", exception.getMessage());
    }

    @Test
    void testGetAdditionalReferenceConfig_productServiceNotFound() {
        when(tenantRepository.findByIdentity(tenantIdentity)).thenReturn(Optional.of(tenant));
        when(productServiceRepository.findByIdentity(productServiceIdentity)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                service.getAdditionalReferenceConfig(tenantIdentity, productServiceIdentity));

        assertEquals("Invalid Product Service Identity", exception.getMessage());
    }


}
