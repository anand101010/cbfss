package com.incede.nbfc.core.monolith.customer.service;

import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerAdditionalReferenceName;
import com.incede.nbfc.core.monolith.customer.dto.CustomerAdditionalReferenceNameResponseDto;
import com.incede.nbfc.core.monolith.customer.repository.CustomerAdditionalReferenceNameRepository;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ErrorCodes;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.Tenant;
import com.incede.nbfc.core.monolith.masterdata.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerAdditionalReferenceNameService {

    private final TenantRepository tenantRepository;
    private final CustomerAdditionalReferenceNameRepository customerAdditionalReferenceNameRepository;

    /**
     * get additional reference name by tenant identity.
     * tenant table not created yet
     *
     * @param tenantIdentity
     * @return
     */
    @Transactional(readOnly = true)
    public List<CustomerAdditionalReferenceNameResponseDto> getReferenceName(UUID tenantIdentity) {

        Tenant tenant = tenantRepository.findByIdentity(tenantIdentity)
                .orElseThrow(() -> new BusinessException("Tenant not found", ErrorCodes.RESOURCE_NOT_FOUND));

        List<CustomerAdditionalReferenceName> refNames =
                customerAdditionalReferenceNameRepository.findByTenant(tenant);

        if (refNames.isEmpty()) {
            throw new BusinessException("Customer additional reference value not found", ErrorCodes.RESOURCE_NOT_FOUND);
        }

        return refNames.stream()
                .map(ref -> {
                    CustomerAdditionalReferenceNameResponseDto dto = new CustomerAdditionalReferenceNameResponseDto();
                    dto.setIdentity(ref.getIdentity());
                    dto.setCustomerRefName(ref.getCustomerRefName());
                    dto.setIsActive(ref.getIsActive());
                    dto.setIsMandatory(ref.getIsMandatory());
                    dto.setValueType(ref.getValueType());
                    return dto;
                })
                .toList();
    }
}
