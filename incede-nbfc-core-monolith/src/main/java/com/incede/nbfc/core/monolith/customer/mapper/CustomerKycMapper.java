package com.incede.nbfc.core.monolith.customer.mapper;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.*;
import com.incede.nbfc.core.monolith.customer.dto.*;
import com.incede.nbfc.core.monolith.customer.repository.CustomerRepository;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.DocumentType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

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
        customerKyc.setIdentity(UUID.randomUUID());

        customerKyc.setIsVerified(request.getIsVerified() != null ? request.getIsVerified() : false);
        customerKyc.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);

        return customerKyc;
    }

    public CustomerKycResponseDto toResponseDto(Customer customer, List<CustomerKyc> customerKycList, MultipartFile file) {
        Objects.requireNonNull(customer);

        List<KycDocumentResponseDto> kycDocuments = customerKycList.stream()
                .map(kyc -> toKycDocumentResponseDto(kyc, file))
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


    private KycDocumentResponseDto toKycDocumentResponseDto(CustomerKyc customerKyc,MultipartFile file) {
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
                .kycUploads(toKycUploadResponseDtos(customerKyc,file))
                .build();
    }



    public CustomerKycUpload toKycUploadEntity(
            CustomerKyc customerKyc,
            MultipartFile file) {

        Objects.requireNonNull(customerKyc, "CustomerKyc must not be null");

        CustomerKycUpload upload = new CustomerKycUpload();

        upload.setKyc(customerKyc);
        upload.setCustomer(customerKyc.getCustomer());


        String originalFilename = file != null ? file.getOriginalFilename() : "document";
        String fileType = file != null ? file.getContentType() : "application/octet-stream";

        upload.setFileName(originalFilename);
        upload.setFileType(fileType);
        upload.setUploadStatus(CustomerKycUpload.UploadStatus.SUCCESS);

        upload.setUploadDate(Timestamp.valueOf(LocalDateTime.now()));

        upload.setResponsePayload(null);
        upload.setCreatedBy(getCreatedBy());

        upload.setIsDel(false);

        return upload;
    }


    private List<KycUploadResponseDto> toKycUploadResponseDtos(CustomerKyc customerKyc, MultipartFile file) {
        String originalFilename = file != null ? file.getOriginalFilename() : "document";
        String fileExtension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        }

        String fileType = file != null ? file.getContentType() : "application/octet-stream";

        return List.of(KycUploadResponseDto.builder()
                .documentReference("DOC_REF_" + (customerKyc.getDocumentRefId() != null ? customerKyc.getDocumentRefId() : "UNKNOWN"))
                .fileName(originalFilename != null ? originalFilename : "document")
                .fileType(fileType != null ? fileType : "application/octet-stream")
                .uploadStatus("UPLOADED")
                .version(1)
                .build());
    }

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


    public Integer getCreatedBy() {
        return CommonConstants.CREATED_BY;
    }

    public Integer getUpdatedBy() {
        return CommonConstants.UPDATED_BY;
    }
}
