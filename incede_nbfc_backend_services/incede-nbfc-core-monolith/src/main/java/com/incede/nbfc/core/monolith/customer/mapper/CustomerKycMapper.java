package com.incede.nbfc.core.monolith.customer.mapper;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.*;
import com.incede.nbfc.core.monolith.customer.dto.*;
import com.incede.nbfc.core.monolith.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CustomerKycMapper {

    private final CustomerRepository customerRepository;

    /**
     * Convert a request DTO to a Customer entity.
     *
     * @param request           The KYC request DTO
     * @param customerCode      The generated customer code
     * @param customerIdentity  The customer identity UUID
     * @return Customer entity
     */
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

    /**
     * Convert a request DTO to a CustomerKyc entity and link it to a Customer.
     *
     * @param request   The KYC request DTO
     * @param customer  The customer entity
     * @return CustomerKyc entity
     */
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
        customerKyc.setIdentity(UUID.randomUUID());
        customerKyc.setIsVerified(request.getIsVerified() != null ? request.getIsVerified() : false);
        customerKyc.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);

        return customerKyc;
    }

    /**
     * Convert a Customer and its KYC details to a response DTO.
     *
     * @param customer                 The customer entity
     * @param customerKycList         List of CustomerKyc entities
     * @param customerKycUploadList   List of CustomerKycUpload entities
     * @return CustomerKycResponseDto
     */
    public CustomerKycResponseDto toResponseDto(Customer customer, List<CustomerKyc> customerKycList, List<CustomerKycUpload> customerKycUploadList) {
        Objects.requireNonNull(customer);

        List<KycDocumentResponseDto> kycDocuments = customerKycList.stream()
                .map(kyc -> toKycDocumentResponseDto(kyc, customerKycUploadList))
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

    /**
     * Convert a CustomerKyc entity to a KycDocumentResponseDto.
     *
     * @param customerKyc            The CustomerKyc entity
     * @param customerKycUploadList  List of CustomerKycUpload entities
     * @return KycDocumentResponseDto
     */
    private KycDocumentResponseDto toKycDocumentResponseDto(CustomerKyc customerKyc, List<CustomerKycUpload> customerKycUploadList) {
        return KycDocumentResponseDto.builder()
                .identity(customerKyc.getIdentity())
                .idType(customerKyc.getIdType().getIdentity())
                .idNumber(customerKyc.getIdNumber())
                .placeOfIssue(customerKyc.getPlaceOfIssue())
                .issuingAuthority(customerKyc.getIssuingAuthority())
                .validFrom(customerKyc.getValidFrom() != null ? customerKyc.getValidFrom().toString() : null)
                .validTo(customerKyc.getValidTo() != null ? customerKyc.getValidTo().toString() : null)
                .isVerified(customerKyc.getIsVerified())
                .isActive(customerKyc.getIsActive())
                .kycUploads(toKycUploadResponseDtos(customerKyc, customerKycUploadList))
                .build();
    }

    /**
     * Convert a request DTO to a CustomerKycUpload entity.
     *
     * @param customerKyc             The CustomerKyc entity
     * @param customerKycRequestDto   The KYC request DTO
     * @return CustomerKycUpload entity
     */
    public CustomerKycUpload toKycUploadEntity(CustomerKyc customerKyc, CustomerKycRequestDto customerKycRequestDto) {
        Objects.requireNonNull(customerKyc, "CustomerKyc must not be null");

        CustomerKycUpload upload = new CustomerKycUpload();
        upload.setKyc(customerKyc);
        upload.setCustomer(customerKyc.getCustomer());
        upload.setFileName(customerKycRequestDto.getFileName());
        upload.setFileType(customerKycRequestDto.getFileType());
        upload.setFilePath(customerKycRequestDto.getFilePath());
        upload.setUploadStatus(CustomerKycUpload.UploadStatus.SUCCESS);
        upload.setUploadDate(Timestamp.valueOf(LocalDateTime.now()));
        upload.setResponsePayload(null);
        upload.setCreatedBy(getCreatedBy());
        upload.setIsDel(false);

        return upload;
    }

    /**
     * Convert a list of CustomerKycUpload entities to KycUploadResponseDto list.
     *
     * @param customerKyc             The CustomerKyc entity
     * @param customerKycUploadList   List of CustomerKycUpload entities
     * @return List of KycUploadResponseDto
     */
    private List<KycUploadResponseDto> toKycUploadResponseDtos(CustomerKyc customerKyc, List<CustomerKycUpload> customerKycUploadList) {
        if (customerKycUploadList == null || customerKycUploadList.isEmpty()) {
            return List.of();
        }

        return customerKycUploadList.stream()
                .map(upload -> KycUploadResponseDto.builder()
                        .documentReference(upload.getDocumentReference())
                        .fileName(upload.getFileName())
                        .fileType(upload.getFileType())
                        .filePath(upload.getFilePath())
                        .uploadStatus(upload.getUploadStatus())
                        .uploadDate(upload.getUploadDate())
                        .identity(upload.getIdentity())
                        .version(1)
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Generate a customer code using branch code and the last record.
     *
     * @param branchCode    The branch code
     * @param customerType  The customer type
     * @return Generated customer code
     */
    public String generateCustomerCode(String branchCode, String customerType) {
        Objects.requireNonNull(branchCode);

        String categoryCode = CommonConstants.CATEGORY_CODE;
        Customer lastCustomer = customerRepository.findTopByOrderByCustomerIdDesc();

        int incrementalId = 1;
        if (lastCustomer != null && lastCustomer.getCustomerCode() != null) {
            String lastCode = lastCustomer.getCustomerCode();
            String serialPart = lastCode.substring(lastCode.lastIndexOf("-") + 1);
            try {
                incrementalId = Integer.parseInt(serialPart) + 1;
            } catch (NumberFormatException ignored) {
            }
        }

        String serialNumber = String.format("%05d", incrementalId);
        return branchCode + "-" + categoryCode + "-" + serialNumber;
    }

    /**
     * Get createdBy value.
     *
     * @return createdBy
     */
    public Integer getCreatedBy() {
        return CommonConstants.CREATED_BY;
    }

    /**
     * Get updatedBy value.
     *
     * @return updatedBy
     */
    public Integer getUpdatedBy() {
        return CommonConstants.UPDATED_BY;
    }
}
