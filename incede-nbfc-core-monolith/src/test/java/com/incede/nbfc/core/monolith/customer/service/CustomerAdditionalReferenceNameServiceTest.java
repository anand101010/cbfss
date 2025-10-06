package com.incede.nbfc.core.monolith.customer.service;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerAdditionalReferenceName;
import com.incede.nbfc.core.monolith.customer.dto.CustomerAdditionalReferenceNameResponseDto;
import com.incede.nbfc.core.monolith.customer.repository.CustomerAdditionalReferenceNameRepository;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ErrorCodes;
import com.incede.nbfc.core.monolith.tenant.domain.entity.Tenant;
import com.incede.nbfc.core.monolith.tenant.repository.TenantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerAdditionalReferenceNameServiceTest {

    @Mock
    private TenantRepository tenantRepository;

    @Mock
    private CustomerAdditionalReferenceNameRepository customerAdditionalReferenceNameRepository;

    @InjectMocks
    private CustomerAdditionalReferenceNameService service;

    private UUID tenantIdentity;
    private Tenant tenant;
    private CustomerAdditionalReferenceName refName1;
    private CustomerAdditionalReferenceName refName2;

    @BeforeEach
    void setUp() {
        tenantIdentity = UUID.randomUUID();

        tenant = new Tenant();
        tenant.setIdentity(tenantIdentity);
        tenant.setTenantId(1);

        refName1 = new CustomerAdditionalReferenceName();
        refName1.setIdentity(UUID.randomUUID());
        refName1.setCustomerRefName("Reference 1");
        refName1.setIsActive(true);
        refName1.setIsMandatory(false);
        refName1.setValueType("TEXT");
        refName1.setTenant(tenant);

        refName2 = new CustomerAdditionalReferenceName();
        refName2.setIdentity(UUID.randomUUID());
        refName2.setCustomerRefName("Reference 2");
        refName2.setIsActive(true);
        refName2.setIsMandatory(true);
        refName2.setValueType("NUMBER");
        refName2.setTenant(tenant);
    }

    @Test
    void getReferenceName_Success() {

        when(tenantRepository.findByIdentity(tenantIdentity)).thenReturn(Optional.of(tenant));
        when(customerAdditionalReferenceNameRepository.findByTenant(tenant))
                .thenReturn(List.of(refName1, refName2));


        List<CustomerAdditionalReferenceNameResponseDto> result = service.getReferenceName(tenantIdentity);


        assertNotNull(result);
        assertEquals(2, result.size());


        CustomerAdditionalReferenceNameResponseDto dto1 = result.get(0);
        assertEquals(refName1.getIdentity(), dto1.getIdentity());
        assertEquals(refName1.getCustomerRefName(), dto1.getCustomerRefName());
        assertEquals(refName1.getIsActive(), dto1.getIsActive());
        assertEquals(refName1.getIsMandatory(), dto1.getIsMandatory());
        assertEquals(refName1.getValueType(), dto1.getValueType());


        CustomerAdditionalReferenceNameResponseDto dto2 = result.get(1);
        assertEquals(refName2.getIdentity(), dto2.getIdentity());
        assertEquals(refName2.getCustomerRefName(), dto2.getCustomerRefName());
        assertEquals(refName2.getIsActive(), dto2.getIsActive());
        assertEquals(refName2.getIsMandatory(), dto2.getIsMandatory());
        assertEquals(refName2.getValueType(), dto2.getValueType());

        verify(tenantRepository).findByIdentity(tenantIdentity);
        verify(customerAdditionalReferenceNameRepository).findByTenant(tenant);
    }

    @Test
    void getReferenceName_TenantNotFound() {
        when(tenantRepository.findByIdentity(tenantIdentity)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> service.getReferenceName(tenantIdentity));

        assertEquals(CommonConstants.TENANT_NOT_FOUND, exception.getMessage());
        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, exception.getErrorCode());

        verify(tenantRepository).findByIdentity(tenantIdentity);
        verify(customerAdditionalReferenceNameRepository, never()).findByTenant(any());
    }

    @Test
    void getReferenceName_NoReferenceNamesFound() {
        when(tenantRepository.findByIdentity(tenantIdentity)).thenReturn(Optional.of(tenant));
        when(customerAdditionalReferenceNameRepository.findByTenant(tenant)).thenReturn(List.of());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> service.getReferenceName(tenantIdentity));

        assertEquals(CommonConstants.CUSTOMER_ADDITIONAL_REF_VALUE_NOT_FOUND, exception.getMessage());
        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, exception.getErrorCode());

        verify(tenantRepository).findByIdentity(tenantIdentity);
        verify(customerAdditionalReferenceNameRepository).findByTenant(tenant);
    }

    @Test
    void getReferenceName_SingleReferenceName() {
        when(tenantRepository.findByIdentity(tenantIdentity)).thenReturn(Optional.of(tenant));
        when(customerAdditionalReferenceNameRepository.findByTenant(tenant))
                .thenReturn(List.of(refName1));

        List<CustomerAdditionalReferenceNameResponseDto> result = service.getReferenceName(tenantIdentity);

        assertNotNull(result);
        assertEquals(1, result.size());

        CustomerAdditionalReferenceNameResponseDto dto = result.get(0);
        assertEquals(refName1.getIdentity(), dto.getIdentity());
        assertEquals(refName1.getCustomerRefName(), dto.getCustomerRefName());
        assertEquals(refName1.getIsActive(), dto.getIsActive());
        assertEquals(refName1.getIsMandatory(), dto.getIsMandatory());
        assertEquals(refName1.getValueType(), dto.getValueType());

        verify(tenantRepository).findByIdentity(tenantIdentity);
        verify(customerAdditionalReferenceNameRepository).findByTenant(tenant);
    }

    @Test
    void getReferenceName_WithInactiveReferenceNames() {
        CustomerAdditionalReferenceName inactiveRefName = new CustomerAdditionalReferenceName();
        inactiveRefName.setIdentity(UUID.randomUUID());
        inactiveRefName.setCustomerRefName("Inactive Reference");
        inactiveRefName.setIsActive(false);
        inactiveRefName.setIsMandatory(false);
        inactiveRefName.setValueType("TEXT");
        inactiveRefName.setTenant(tenant);

        when(tenantRepository.findByIdentity(tenantIdentity)).thenReturn(Optional.of(tenant));
        when(customerAdditionalReferenceNameRepository.findByTenant(tenant))
                .thenReturn(List.of(refName1, inactiveRefName));

        List<CustomerAdditionalReferenceNameResponseDto> result = service.getReferenceName(tenantIdentity);

        assertNotNull(result);
        assertEquals(2, result.size());

        CustomerAdditionalReferenceNameResponseDto inactiveDto = result.stream()
                .filter(dto -> !dto.getIsActive())
                .findFirst()
                .orElse(null);

        assertNotNull(inactiveDto);
        assertEquals(inactiveRefName.getCustomerRefName(), inactiveDto.getCustomerRefName());
        assertFalse(inactiveDto.getIsActive());

        verify(tenantRepository).findByIdentity(tenantIdentity);
        verify(customerAdditionalReferenceNameRepository).findByTenant(tenant);
    }

    @Test
    void getReferenceName_VerifyMappingAllFields() {
        CustomerAdditionalReferenceName completeRefName = new CustomerAdditionalReferenceName();
        UUID refIdentity = UUID.randomUUID();
        completeRefName.setIdentity(refIdentity);
        completeRefName.setCustomerRefName("Complete Reference");
        completeRefName.setIsActive(true);
        completeRefName.setIsMandatory(true);
        completeRefName.setValueType("BOOLEAN");
        completeRefName.setTenant(tenant);

        when(tenantRepository.findByIdentity(tenantIdentity)).thenReturn(Optional.of(tenant));
        when(customerAdditionalReferenceNameRepository.findByTenant(tenant))
                .thenReturn(List.of(completeRefName));

        List<CustomerAdditionalReferenceNameResponseDto> result = service.getReferenceName(tenantIdentity);

        assertNotNull(result);
        assertEquals(1, result.size());

        CustomerAdditionalReferenceNameResponseDto dto = result.get(0);
        assertEquals(refIdentity, dto.getIdentity());
        assertEquals("Complete Reference", dto.getCustomerRefName());
        assertTrue(dto.getIsActive());
        assertTrue(dto.getIsMandatory());
        assertEquals("BOOLEAN", dto.getValueType());

        verify(tenantRepository).findByIdentity(tenantIdentity);
        verify(customerAdditionalReferenceNameRepository).findByTenant(tenant);
    }

    @Test
    void getReferenceName_EmptyListThrowsException() {
        when(tenantRepository.findByIdentity(tenantIdentity)).thenReturn(Optional.of(tenant));
        when(customerAdditionalReferenceNameRepository.findByTenant(tenant)).thenReturn(List.of());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> service.getReferenceName(tenantIdentity));

        assertEquals(CommonConstants.CUSTOMER_ADDITIONAL_REF_VALUE_NOT_FOUND, exception.getMessage());
        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, exception.getErrorCode());

        verify(tenantRepository).findByIdentity(tenantIdentity);
        verify(customerAdditionalReferenceNameRepository).findByTenant(tenant);
    }

    @Test
    void getReferenceName_NullTenantIdentity() {
        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> service.getReferenceName(null));

        assertEquals(CommonConstants.TENANT_NOT_FOUND, exception.getMessage());
        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, exception.getErrorCode());

        verify(tenantRepository).findByIdentity(null);
        verify(customerAdditionalReferenceNameRepository, never()).findByTenant(any());
    }
}
