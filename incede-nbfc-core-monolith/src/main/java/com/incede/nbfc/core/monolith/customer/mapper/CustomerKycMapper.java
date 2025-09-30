package com.incede.nbfc.core.monolith.customer.mapper;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.*;
import com.incede.nbfc.core.monolith.customer.dto.*;
import com.incede.nbfc.core.monolith.customer.repository.CustomerRepository;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.DocumentType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CustomerKycMapper {

    private final CustomerRepository customerRepository;

    public Customer toCustomerEntity(CustomerKycRequestDto request, String customerCode, UUID customerIdentity) {
        Objects.requireNonNull(request);
        Objects.requireNonNull(customerIdentity);
        Objects.requireNonNull(customerCode);

        Customer customer = new Customer();
        customer.setIdentity(customerIdentity);
        customer.setOnboardingStatus(CommonConstants.DRAFT);
        customer.setDisplayName(CommonConstants.DRAFT);
        customer.setIsFirm(false);
        customer.setIsBusiness(false);
        customer.setIsMinor(false);
        customer.setCreatedBy(getCreatedBy());
        customer.setCustomerCode(customerCode);


        return customer;
    }

    public CustomerKyc toKycEntity(CustomerKycRequestDto request, Customer customer) {
        Objects.requireNonNull(request);
        Objects.requireNonNull(customer);

        CustomerKyc customerKyc = new CustomerKyc();
        customerKyc.setIdNumber(request.getIdNumber());
        customerKyc.setPlaceOfIssue(request.getPlaceOfIssue());
        customerKyc.setIssuingAuthority(request.getIssuingAuthority());
        customerKyc.setValidFrom(request.getValidFrom());
        customerKyc.setValidTo(request.getValidTo());
        customerKyc.setCreatedBy(getCreatedBy());
        customerKyc.setCustomer(customer);

        customerKyc.setIsVerified(request.getIsVerified() != null ? request.getIsVerified() : false);
        customerKyc.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);

        return customerKyc;
    }

    public CustomerKycResponseDto toResponseDto(Customer customer, List<CustomerKyc> customerKycList) {
        Objects.requireNonNull(customer);

        List<KycDocumentResponseDto> kycDocuments = customerKycList.stream()
                .map(this::toKycDocumentResponseDto)
                .collect(Collectors.toList());

        return CustomerKycResponseDto.builder()
                .identity(customer.getIdentity())
                .customerCode(customer.getCustomerCode())
                .onboardingStatus(customer.getOnboardingStatus())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .dob(customer.getDob() != null ? customer.getDob().toString() : null)
                .gender(customer.getGender() != null ? customer.getGender().getIdentity() : null)
                .customerStatus(customer.getCustomerStatus() != null ? customer.getCustomerStatus().getIdentity() : null)
                .branchId(customer.getBranchId() != null ? customer.getBranchId().getIdentity() : null)
                .kycDocuments(kycDocuments)
                .build();
    }

    private KycDocumentResponseDto toKycDocumentResponseDto(CustomerKyc customerKyc) {
        return KycDocumentResponseDto.builder()
                .identity(customerKyc.getIdentity())
                .idType(customerKyc.getIdType() != null ? customerKyc.getIdType().getDisplayName() : null)
                .idNumber(customerKyc.getIdNumber())
                .placeOfIssue(customerKyc.getPlaceOfIssue())
                .issuingAuthority(customerKyc.getIssuingAuthority())
                .validFrom(customerKyc.getValidFrom() != null ? customerKyc.getValidFrom().toString() : null)
                .validTo(customerKyc.getValidTo() != null ? customerKyc.getValidTo().toString() : null)
                .isVerified(customerKyc.getIsVerified())
                .isActive(customerKyc.getIsActive())
                .kycUploads(toKycUploadResponseDtos(customerKyc))
                .build();
    }

    private List<KycUploadResponseDto> toKycUploadResponseDtos(CustomerKyc customerKyc) {
        return List.of(KycUploadResponseDto.builder()
                .identity(UUID.randomUUID())
                .documentReference("DOC_REF_" + (customerKyc.getDocumentRefId() != null ? customerKyc.getDocumentRefId() : "UNKNOWN"))
                .fileName("document.pdf")
                .fileType("application/pdf")
                .uploadStatus("UPLOADED")
                .version(1)
                .build());
    }

    public String generateCustomerCode(String branchCode, String customerType) {
        Objects.requireNonNull(branchCode);
        Objects.requireNonNull(customerType);

        Customer lastCustomer = customerRepository.findTopByOrderByCustomerIdDesc();
        String customerTypeShort = customerType.length() >= 3
                ? customerType.substring(0, 3).toUpperCase()
                : customerType.toUpperCase();

        int incrementalId = 1;
        if (lastCustomer != null && lastCustomer.getCustomerCode() != null) {
            String lastCode = lastCustomer.getCustomerCode();
            String serialPart = lastCode.substring(lastCode.lastIndexOf("-") + 1);
            try {
                incrementalId = Integer.parseInt(serialPart) + 1;
            } catch (NumberFormatException ignored) {
            }
        }

        return branchCode + "-" + customerTypeShort + "-" + String.format("%03d", incrementalId);
    }

    public Integer getCreatedBy() {
        return CommonConstants.CREATED_BY;
    }

    public Integer getUpdatedBy() {
        return CommonConstants.UPDATED_BY;
    }
}
