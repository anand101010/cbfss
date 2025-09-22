package com.incede.nbfc.core.monolith.customer.mapper;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerKyc;
import com.incede.nbfc.core.monolith.customer.dto.CustomerKycRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerKycResponseDto;
import com.incede.nbfc.core.monolith.customer.dto.InitialCustomerProfileDto;
import com.incede.nbfc.core.monolith.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CustomerKycMapper {

    private final CustomerRepository customerRepository;

    public Customer toCustomerEntity(CustomerKycRequestDto request, String customerCode) {
        Customer customer = new Customer();
        customer.setIdentity(UUID.randomUUID());
        customer.setOnboardingStatus(CommonConstants.DRAFT);
        customer.setTenantId(request.getTenantId());
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setDob(request.getDob());
        customer.setDisplayName(request.getFirstName() + " " +
                request.getLastName());
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
        customer.setCustomerCode(customerCode);
        return customer;
    }

    public CustomerKyc toKycEntity(CustomerKycRequestDto request, Customer customer) {
        CustomerKyc customerKyc = new CustomerKyc();
        customerKyc.setIdType(request.getIdType());
        customerKyc.setIdNumber(request.getIdNumber());
        customerKyc.setPlaceOfIssue(request.getPlaceOfIssue());
        customerKyc.setIssuingAuthority(request.getIssuingAuthority());
        customerKyc.setValidFrom(request.getValidFrom());
        customerKyc.setValidTo(request.getValidTo());
        customerKyc.setDocumentRefId(request.getDocumentRefId());
        customerKyc.setCreatedBy(1);
        customerKyc.setCustomer(customer);
        return customerKyc;
    }

    public CustomerKycResponseDto toResponseDto(Customer savedCustomer, CustomerKyc savedCustomerKyc) {
        CustomerKycResponseDto response = new CustomerKycResponseDto();
        response.setCustomerId(savedCustomer.getCustomerId());
        response.setCustomerCode(savedCustomer.getCustomerCode());
        response.setStatus(savedCustomer.getOnboardingStatus());
        response.setIdentity(savedCustomer.getIdentity());
        response.setIdType(savedCustomerKyc.getIdType());
        response.setIdNumber(savedCustomerKyc.getIdNumber());
        response.setPlaceOfIssue(savedCustomerKyc.getPlaceOfIssue());
        response.setIssuingAuthority(savedCustomerKyc.getIssuingAuthority());
        response.setValidFrom(savedCustomerKyc.getValidFrom());
        response.setValidTo(savedCustomerKyc.getValidTo());
        response.setDocumentRefId(savedCustomerKyc.getDocumentRefId());

        InitialCustomerProfileDto initialCustomerProfileDto = new InitialCustomerProfileDto();
        initialCustomerProfileDto.setTenantId(savedCustomer.getTenantId());
        initialCustomerProfileDto.setFirstName(savedCustomer.getFirstName());
        initialCustomerProfileDto.setLastName(savedCustomer.getLastName());
        initialCustomerProfileDto.setDob(savedCustomer.getDob());

        response.setInitialProfile(initialCustomerProfileDto);
        return response;
    }

    public String generateCustomerCode(String branchCode, String customerType) {
        Customer lastCustomer = customerRepository.findTopByOrderByCustomerIdDesc();
        String customerTypeShort = customerType.length() >= 3
                ? customerType.substring(0, 3).toUpperCase()
                : customerType.toUpperCase();
        int incrementalId = 1;
        if (lastCustomer != null) {
            String lastCode = lastCustomer.getCustomerCode();
            String serialPart = lastCode.substring(lastCode.lastIndexOf("-") + 1);
            int lastIncrementalId = Integer.parseInt(serialPart);
            incrementalId = lastIncrementalId + 1;
        }
        String serialFormatted = "0" + incrementalId;
        return branchCode + "-" + customerTypeShort + "-" + serialFormatted;
    }
}
