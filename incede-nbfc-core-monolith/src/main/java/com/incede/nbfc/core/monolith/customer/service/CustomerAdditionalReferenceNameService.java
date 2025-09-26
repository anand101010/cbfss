package com.incede.nbfc.core.monolith.customer.service;

import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerAdditionalReferenceName;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerAdditionalReferenceValue;
import com.incede.nbfc.core.monolith.customer.dto.CustomerAdditionalReferenceNameResponseDto;
import com.incede.nbfc.core.monolith.customer.repository.CustomerAdditionalReferenceValueRepository;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ErrorCodes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerAdditionalReferenceNameService {

    private final CustomerAdditionalReferenceValueRepository customerAdditionalReferenceValueRepository;

    /**
     * get additional reference name by tenant identity.
     * tenant table not created yet
     * @param referenceValueIdentity
     * @return
     */

    @Transactional(readOnly = true)
    public CustomerAdditionalReferenceNameResponseDto getReferenceName(UUID referenceValueIdentity) {
        CustomerAdditionalReferenceValue customerAdditionalReferenceValue = customerAdditionalReferenceValueRepository.findByIdentity(referenceValueIdentity)
                .orElseThrow(() -> new BusinessException("Customer additional reference value not found", ErrorCodes.RESOURCE_NOT_FOUND));

        CustomerAdditionalReferenceName customerAdditionalReferenceName = customerAdditionalReferenceValue.getCustomerAdditionalReferenceName();

        CustomerAdditionalReferenceNameResponseDto customerAdditionalReferenceNameDto= new CustomerAdditionalReferenceNameResponseDto();
        customerAdditionalReferenceNameDto.setIdentity(customerAdditionalReferenceName.getIdentity());
        customerAdditionalReferenceNameDto.setCustomerRefName(customerAdditionalReferenceName.getCustomerRefName());
        customerAdditionalReferenceNameDto.setIsActive(customerAdditionalReferenceName.getIsActive());
        customerAdditionalReferenceNameDto.setIsMandatory(customerAdditionalReferenceName.getIsMandatory());
        customerAdditionalReferenceNameDto.setValueType(customerAdditionalReferenceName.getValueType());

        return customerAdditionalReferenceNameDto;
    }
}
