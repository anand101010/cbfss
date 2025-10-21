package com.incede.nbfc.core.monolith.masterdata.service;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.masterdata.dto.*;
import com.incede.nbfc.core.monolith.masterdata.repository.*;
import com.incede.nbfc.core.monolith.tenant.domain.entity.Tenant;
import com.incede.nbfc.core.monolith.tenant.repository.TenantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LeadMasterDataServiceTest {

    private AdditionalReferenceConfigRepository additionalReferenceConfigRepository;
    private LeadSourceRepository leadSourceRepository;
    private LeadStageRepository leadStageRepository;
    private FollowUpTypeRepository followUpTypeRepository;
    private LeadStatusRepository leadStatusRepository;
    private ProductServiceRepository productServiceRepository;
    private TenantRepository tenantRepository;

    private LeadMasterDataService service;
    private UUID tenantIdentity;
    private Tenant tenant;

    @BeforeEach
    void setUp() {
        additionalReferenceConfigRepository = mock(AdditionalReferenceConfigRepository.class);
        leadSourceRepository = mock(LeadSourceRepository.class);
        leadStageRepository = mock(LeadStageRepository.class);
        followUpTypeRepository = mock(FollowUpTypeRepository.class);
        leadStatusRepository = mock(LeadStatusRepository.class);
        productServiceRepository = mock(ProductServiceRepository.class);
        tenantRepository = mock(TenantRepository.class);

        service = new LeadMasterDataService(
                additionalReferenceConfigRepository,
                leadSourceRepository,
                leadStageRepository,
                followUpTypeRepository,
                leadStatusRepository,
                productServiceRepository,
                tenantRepository
        );

        tenantIdentity = UUID.randomUUID();
        tenant = new Tenant();
        tenant.setTenantId(10);
        tenant.setIdentity(tenantIdentity);
    }

    @Test
    void testGetAllAdditionalReferenceConfigs_EmptyList() {
        when(additionalReferenceConfigRepository.findAllByTenantIdOrAll(null))
                .thenReturn(Collections.emptyList());

        List<AdditionalReferenceConfigView> result =
                service.getAllAdditionalReferenceConfigs(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllAdditionalReferenceConfigs_WithData() {
        AdditionalReferenceConfigView mockView = mock(AdditionalReferenceConfigView.class);
        when(mockView.getReferenceFieldName()).thenReturn("PAN");

        when(additionalReferenceConfigRepository.findAllByTenantIdOrAll(null))
                .thenReturn(List.of(mockView));

        List<AdditionalReferenceConfigView> result =
                service.getAllAdditionalReferenceConfigs(null);

        assertEquals(1, result.size());
        assertEquals("PAN", result.get(0).getReferenceFieldName());
    }

    @Test
    void testGetAllLeadSources_EmptyList() {
        when(leadSourceRepository.findAllByTenantIdOrAll(null))
                .thenReturn(Collections.emptyList());

        List<LeadSourceView> result = service.getAllLeadSources(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllLeadSources_WithTenant() {
        when(tenantRepository.findByIdentity(tenantIdentity)).thenReturn(Optional.of(tenant));

        LeadSourceView mockView = mock(LeadSourceView.class);
        when(mockView.getName()).thenReturn("Website");

        when(leadSourceRepository.findAllByTenantIdOrAll(10))
                .thenReturn(List.of(mockView));

        List<LeadSourceView> result = service.getAllLeadSources(tenantIdentity);

        assertEquals(1, result.size());
        assertEquals("Website", result.get(0).getName());
    }

    @Test
    void testGetAllLeadStages_EmptyList() {
        when(leadStageRepository.findAllByTenantIdOrAll(null))
                .thenReturn(Collections.emptyList());

        List<LeadStageView> result = service.getAllLeadStages(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllLeadStages_WithTenant() {
        when(tenantRepository.findByIdentity(tenantIdentity)).thenReturn(Optional.of(tenant));

        LeadStageView mockView = mock(LeadStageView.class);
        when(mockView.getName()).thenReturn("Qualified");

        when(leadStageRepository.findAllByTenantIdOrAll(10))
                .thenReturn(List.of(mockView));

        List<LeadStageView> result = service.getAllLeadStages(tenantIdentity);

        assertEquals(1, result.size());
        assertEquals("Qualified", result.get(0).getName());
    }

    @Test
    void testGetAllFollowUpTypes_EmptyList() {
        when(followUpTypeRepository.findAllByTenantIdOrAll(null))
                .thenReturn(Collections.emptyList());

        List<FollowUpTypeView> result = service.getAllFollowUpTypes(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllFollowUpTypes_WithTenant() {
        when(tenantRepository.findByIdentity(tenantIdentity)).thenReturn(Optional.of(tenant));

        FollowUpTypeView mockView = mock(FollowUpTypeView.class);
        when(mockView.getName()).thenReturn("Call");

        when(followUpTypeRepository.findAllByTenantIdOrAll(10))
                .thenReturn(List.of(mockView));

        List<FollowUpTypeView> result = service.getAllFollowUpTypes(tenantIdentity);

        assertEquals(1, result.size());
        assertEquals("Call", result.get(0).getName());
    }

    @Test
    void testGetAllLeadStatuses_EmptyList() {
        when(leadStatusRepository.findAllByTenantIdOrAll(null))
                .thenReturn(Collections.emptyList());

        List<LeadStatusView> result = service.getAllLeadStatuses(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllLeadStatuses_WithTenant() {
        when(tenantRepository.findByIdentity(tenantIdentity)).thenReturn(Optional.of(tenant));

        LeadStatusView mockView = mock(LeadStatusView.class);
        when(mockView.getName()).thenReturn("Converted");

        when(leadStatusRepository.findAllByTenantIdOrAll(10))
                .thenReturn(List.of(mockView));

        List<LeadStatusView> result = service.getAllLeadStatuses(tenantIdentity);

        assertEquals(1, result.size());
        assertEquals("Converted", result.get(0).getName());
    }

    @Test
    void testGetAllProductServices_EmptyList() {
        when(productServiceRepository.findAllByTenantIdOrAll(null))
                .thenReturn(Collections.emptyList());

        List<ProductServiceView> result = service.getAllProductServices(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllProductServices_WithTenant() {
        when(tenantRepository.findByIdentity(tenantIdentity)).thenReturn(Optional.of(tenant));

        ProductServiceView mockView = mock(ProductServiceView.class);
        when(mockView.getName()).thenReturn("Loan Processing");

        when(productServiceRepository.findAllByTenantIdOrAll(10))
                .thenReturn(List.of(mockView));

        List<ProductServiceView> result = service.getAllProductServices(tenantIdentity);

        assertEquals(1, result.size());
        assertEquals("Loan Processing", result.get(0).getName());
    }

    @Test
    void testGetTenantId_WhenNotFound_ShouldThrowException() {
        when(tenantRepository.findByIdentity(tenantIdentity)).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class,
                () -> service.getTenantId(tenantIdentity));

        assertEquals(CommonConstants.TENANT_NOT_FOUND, ex.getMessage());
    }
}
