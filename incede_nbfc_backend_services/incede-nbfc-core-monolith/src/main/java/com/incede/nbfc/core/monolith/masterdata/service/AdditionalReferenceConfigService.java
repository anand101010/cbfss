package com.incede.nbfc.core.monolith.masterdata.service;

import com.incede.nbfc.core.monolith.exception.ResourceNotFoundException;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.AdditionalReferenceConfig;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.ProductService;
import com.incede.nbfc.core.monolith.masterdata.dto.AdditionalReferenceConfigDto;
import com.incede.nbfc.core.monolith.masterdata.mapper.AdditionalReferenceConfigMapper;
import com.incede.nbfc.core.monolith.masterdata.repository.AdditionalReferenceConfigRepository;
import com.incede.nbfc.core.monolith.masterdata.repository.ProductServiceRepository;
import com.incede.nbfc.core.monolith.tenant.domain.entity.Tenant;
import com.incede.nbfc.core.monolith.tenant.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdditionalReferenceConfigService
{


        private final AdditionalReferenceConfigRepository additionalReferenceConfigRepository;
        private final TenantRepository tenantRepository;
        private final ProductServiceRepository productServiceRepository;

        /**
         * Create a new Additional Reference Config after validating tenant and product service.
         */
        @Transactional
        public List<AdditionalReferenceConfigDto> getAdditionalReferenceConfig(UUID tenantIdentity, UUID productServiceIdentity)
        {
            Tenant tenant = tenantRepository.findByIdentity(tenantIdentity)
                            .orElseThrow(() -> new ResourceNotFoundException("Invalid Tenant Identity"));
            ProductService productService = productServiceRepository.findByIdentity(productServiceIdentity)
                                             .orElseThrow(() -> new ResourceNotFoundException("Invalid Product Service Identity"));
            List<AdditionalReferenceConfig> configs = additionalReferenceConfigRepository
                                                     .findRefernceActiveByTenantAndProduct(tenant.getTenantId(), productService.getProductServiceId());
            return configs.stream()
                    .map(AdditionalReferenceConfigMapper::toDto)
                    .collect(Collectors.toList());
        }







}



