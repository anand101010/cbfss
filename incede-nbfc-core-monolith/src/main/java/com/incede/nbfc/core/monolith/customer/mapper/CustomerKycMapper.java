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

import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CustomerKycMapper {

    private final CustomerRepository customerRepository;

    /**
     * Converts a KYC request DTO into a Customer entity.
     *
     * @param request          Customer KYC request DTO
     * @param customerCode     Generated customer code
     * @param customerIdentity Generated UUID identity
     * @return Customer entity
     */
    public Customer toCustomerEntity(CustomerKycRequestDto request, String customerCode, UUID customerIdentity) {
        Objects.requireNonNull(request, "CustomerKycRequestDto must not be null");
        Objects.requireNonNull(customerIdentity, "Customer identity must not be null");
        Objects.requireNonNull(customerCode, "Customer code must not be null");

        Customer customer = new Customer();
        customer.setIdentity(customerIdentity);
        customer.setOnboardingStatus(CommonConstants.DRAFT);
        customer.setTenantId(request.getTenantId());
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setDisplayName(CommonConstants.DRAFT);
        customer.setDob(request.getDob());
        customer.setIsFirm(false);
        customer.setIsBusiness(false);
        customer.setIsMinor(false);
        customer.setCreatedBy(getCreatedBy());
        customer.setCustomerCode(customerCode);

        return customer;
    }

    /**
     * Converts KYC request DTO into a CustomerKyc entity.
     *
     * @param request  Customer KYC request DTO
     * @param customer Linked Customer entity
     * @return CustomerKyc entity
     */
    public CustomerKyc toKycEntity(CustomerKycRequestDto request, Customer customer) {
        Objects.requireNonNull(request, "CustomerKycRequestDto must not be null");
        Objects.requireNonNull(customer, "Customer must not be null");

        CustomerKyc customerKyc = new CustomerKyc();
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

    /**
     * Maps saved Customer and CustomerKyc entities into a response DTO.
     *
     * @param savedCustomer    Saved Customer entity
     * @param savedCustomerKyc Saved CustomerKyc entity
     * @return CustomerKycResponseDto
     */
    public CustomerKycResponseDto toResponseDto(Customer savedCustomer, CustomerKyc savedCustomerKyc) {
        Objects.requireNonNull(savedCustomer, "Saved Customer must not be null");
        Objects.requireNonNull(savedCustomerKyc, "Saved CustomerKyc must not be null");

        CustomerKycResponseDto response = new CustomerKycResponseDto();
        response.setCustomerId(savedCustomer.getCustomerId());
        response.setCustomerCode(savedCustomer.getCustomerCode());
        response.setStatus(savedCustomer.getOnboardingStatus());
        response.setIdentity(savedCustomer.getIdentity());
        response.setIdType(savedCustomerKyc.getIdType().getIdentity());
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

    /**
     * Generates a unique customer code based on the last inserted customer.
     *
     * @param branchCode   Branch code
     * @param customerType Customer type
     * @return Generated customer code
     */
    public String generateCustomerCode(String branchCode, String customerType) {
        Objects.requireNonNull(branchCode, "Branch code must not be null");
        Objects.requireNonNull(customerType, "Customer type must not be null");

        Customer lastCustomer = customerRepository.findTopByOrderByCustomerIdDesc();
        String customerTypeShort = customerType.length() >= 3
                ? customerType.substring(0, 3).toUpperCase()
                : customerType.toUpperCase();
        int incrementalId = 1;

        if (lastCustomer != null && lastCustomer.getCustomerCode() != null) {
            String lastCode = lastCustomer.getCustomerCode();
            String serialPart = lastCode.substring(lastCode.lastIndexOf("-") + 1);
            try {
                int lastIncrementalId = Integer.parseInt(serialPart);
                incrementalId = lastIncrementalId + 1;
            } catch (NumberFormatException e) {
                incrementalId = 1; // fallback if parsing fails
            }
        }

        String serialFormatted = String.format("%03d", incrementalId);
        return branchCode + "-" + customerTypeShort + "-" + serialFormatted;


    }
    public Integer getCreatedBy() {
        return CommonConstants.CREATED_BY;
    }

    public Integer getUpdatedBy() {
        return CommonConstants.UPDATED_BY;
    }
}
