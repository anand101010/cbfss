package com.incede.nbfc.core.monolith.customer.service;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerKyc;
import com.incede.nbfc.core.monolith.customer.dto.CustomerKycRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerKycResponseDto;
import com.incede.nbfc.core.monolith.customer.mapper.CustomerKycMapper;
import com.incede.nbfc.core.monolith.customer.repository.CustomerKycRepository;
import com.incede.nbfc.core.monolith.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerKycService {

    private final CustomerRepository customerRepository;
    private final CustomerKycRepository customerKycRepository;
    private final CustomerKycMapper customerKycMapper;

    @Transactional
    public CustomerKycResponseDto createInitialCustomer(CustomerKycRequestDto request) {

//       Customer customer = initialOnboardingMapper.toCustomerEntity(request);

        Customer customer = new Customer();

        customer.setIdentity(UUID.randomUUID());
        customer.setOnboardingStatus(CommonConstants.DRAFT);
        customer.setTenantId(request.getTenantId());
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setDob(request.getDob());


        customer.setDisplayName(request.getFirstName() + " " + request.getLastName());
        customer.setBranchId(1);
        customer.setTaxCategory(1);
        customer.setCustomerStatus(1);
        customer.setFatherName("XYZ");
        customer.setMotherName("ABC");
        customer.setSpouseName("PQR");
        customer.setIsFirm(false);
        customer.setIsBusiness(false);
        customer.setIsMinor(false);
        customer.setCreatedBy(1);

        String branchCode = request.getBranchCode();
        String customerType = request.getCustomerType();

        String customerCode = customerKycMapper.generateCustomerCode(branchCode, customerType);
        customer.setCustomerCode(customerCode);

        Customer savedCustomer = customerRepository.save(customer);



//        CustomerKyc customerKyc = initialOnboardingMapper.toKycEntity(request);

        CustomerKyc customerKyc = new CustomerKyc();

        customerKyc.setIdType(request.getIdType());
        customerKyc.setIdNumber(request.getIdNumber());
        customerKyc.setPlaceOfIssue(request.getPlaceOfIssue());
        customerKyc.setIssuingAuthority(request.getIssuingAuthority());
        customerKyc.setValidFrom(request.getValidFrom());
        customerKyc.setValidTo(request.getValidTo());
        customerKyc.setDocumentRefId(request.getDocumentRefId());

        customerKyc.setCreatedBy(1);

        customerKyc.setCustomer(savedCustomer);

        CustomerKyc savedCustomerKyc = customerKycRepository.save(customerKyc);

        CustomerKycResponseDto response = customerKycMapper.toResponseDto(savedCustomer, savedCustomerKyc);

        return response;
    }

}
