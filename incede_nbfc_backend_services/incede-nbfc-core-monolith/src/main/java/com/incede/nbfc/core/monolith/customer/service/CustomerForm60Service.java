package com.incede.nbfc.core.monolith.customer.service;

import com.incede.nbfc.core.monolith.client.dto.FinaVaultResponseDto;
import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerAddress;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerForm60;
import com.incede.nbfc.core.monolith.customer.dto.*;
import com.incede.nbfc.core.monolith.customer.mapper.CustomerAddressMapper;
import com.incede.nbfc.core.monolith.customer.mapper.CustomerForm60Mapper;
import com.incede.nbfc.core.monolith.customer.mapper.JasperForm60Mapper;
import com.incede.nbfc.core.monolith.customer.repository.*;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ErrorCodes;
import com.incede.nbfc.core.monolith.exception.ResourceNotFoundException;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.*;
import com.incede.nbfc.core.monolith.masterdata.repository.AddressTypeRepository;
import com.incede.nbfc.core.monolith.masterdata.repository.BranchesRepository;
import com.incede.nbfc.core.monolith.masterdata.repository.DocumentMasterRepository;
import com.incede.nbfc.core.monolith.report.OutputFormat;
import com.incede.nbfc.core.monolith.report.ReportGenerator;
import com.incede.nbfc.core.monolith.report.ReportName;
import com.incede.nbfc.core.monolith.service.VaultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.incede.nbfc.core.monolith.masterdata.enums.AddressTypes;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerForm60Service {
    private final CustomerForm60Repository customerForm60Repository;
    private final CustomerForm60Mapper form60Mapper;
    private final CustomerRepository customerRepository;
    private final DocumentMasterRepository documentRepository;
    private final ReportGenerator reportGenerator;
    private final JasperForm60Mapper jasperForm60Mapper;
    private final CustomerAddressRepository customerAddressRepository;
    private final AddressTypeRepository addressTypeRepository;
    private final CustomerAddressService customerAddressService;
    private final CustomerProfileExtraRepository customerProfileExtraRepository;
    private final CustomerEmploymentRepository customerEmploymentRepository;
    private final CustomerAddressMapper customerAddressMapper;
    private final BranchesRepository branchesRepository;
    private final VaultService vaultService;


    private static final BigDecimal MAX_TRANSACTION_AMOUNT = new BigDecimal("500000");

    @Transactional
    public CustomerForm60ResponseDto saveForm60(CustomerForm60RequestDto request, UUID customerIdentity) {

        validateForm60Request(request);

        try {
            Customer customer = customerRepository.findByIdentity(customerIdentity)
                    .orElseThrow(() -> new BusinessException("Customer not found", ErrorCodes.NOT_FOUND));

            Branches branch = branchesRepository.findByIdentity(request.getBranchId())
                    .orElseThrow(() -> new BusinessException("Branch not found", ErrorCodes.NOT_FOUND));

            DocumentMaster pidDoc = Optional.ofNullable(request.getPidDocumentId())
                    .map(documentRepository::findByIdentity)
                    .orElse(Optional.empty())
                    .orElse(null);

            DocumentMaster addDoc = Optional.ofNullable(request.getAddDocumentId())
                    .map(documentRepository::findByIdentity)
                    .orElse(Optional.empty())
                    .orElse(null);

            if (request.getCreatedBy() == null) {
                throw new BusinessException(CommonConstants.CREATED_BY_REQUIRED, ErrorCodes.VALIDATION_FAILED);
            }

            CustomerForm60 entity = form60Mapper.toEntity(request, customer, branch, pidDoc, addDoc);

            if (request.getMaskedAdhar() != null) {
                FinaVaultResponseDto response = vaultService.generateVaultIdAndMaskAadhaar(request.getMaskedAdhar());
                if (response == null) {
                    throw new BusinessException("Adhar number cannot be masked", ErrorCodes.NOT_FOUND);
                }

                if(response.getStatus().equals("N")){
                    throw new BusinessException("Adhar number cannot be masked :"+response.getErrorCode(), ErrorCodes.NOT_FOUND);
                }
                entity.setMaskedAdhar(response.getUidForDisplay());
            }


            entity.setIdentity(UUID.randomUUID());
            entity.setCreatedBy(request.getCreatedBy());

            CustomerForm60 savedForm60 = customerForm60Repository.save(entity);
            return form60Mapper.toResponseDto(savedForm60);

        } catch (DataIntegrityViolationException e) {
            log.error("Constraint violation while saving Form60. DTO: {}", request, e);
            throw new BusinessException(CommonConstants.CONSTRAIN_VIOLATION, ErrorCodes.CONSTRAINT_VIOLATION, e);
        } catch (IllegalArgumentException e) {
            log.warn("Validation failed for Form60 DTO: {} - {}", request, e.getMessage());
            throw new BusinessException(e.getMessage(), ErrorCodes.VALIDATION_FAILED, e);
        }
    }

    @Transactional
    public CustomerForm60ResponseDto updateForm60(UUID customerIdentity, UUID form60Id, CustomerForm60RequestDto request) {
        try {

            Customer customer = customerRepository.findByIdentity(customerIdentity)
                    .orElseThrow(() -> new BusinessException(
                            CommonConstants.NOT_FOUND_MESSAGE,
                            ErrorCodes.NOT_FOUND));

            CustomerForm60 existingForm60 = customerForm60Repository
                    .findByIdentity(form60Id)
                    .orElseThrow(() -> new BusinessException(
                            "Form 60 not found with ID: " + form60Id + " for customer: " + customerIdentity,
                            ErrorCodes.NOT_FOUND));

            DocumentMaster pidDoc = Optional.ofNullable(request.getPidDocumentId())
                    .map(documentRepository::findByIdentity)
                    .orElse(Optional.empty())
                    .orElse(null);

            DocumentMaster addDoc = Optional.ofNullable(request.getAddDocumentId())
                    .map(documentRepository::findByIdentity)
                    .orElse(Optional.empty())
                    .orElse(null);

            validateForm60Request(request);

            form60Mapper.updateEntityFromDto(existingForm60, request, pidDoc, addDoc);
            existingForm60.setUpdatedAt(LocalDateTime.now());

            if (request.getMaskedAdhar() != null) {
                FinaVaultResponseDto response = vaultService.generateVaultIdAndMaskAadhaar(request.getMaskedAdhar());
                if (response == null) {
                    throw new BusinessException("Adhar number cannot be masked", ErrorCodes.NOT_FOUND);
                }

                if(response.getStatus().equals("N")){
                    throw new BusinessException("Adhar number cannot be masked :"+response.getErrorCode(), ErrorCodes.NOT_FOUND);
                }
                existingForm60.setMaskedAdhar(response.getUidForDisplay());
            }

            CustomerForm60 updatedForm60 = customerForm60Repository.save(existingForm60);

            return form60Mapper.toResponseDto(updatedForm60);

        } catch (DataIntegrityViolationException e) {
            log.error("Constraint violation while updating Form 60. Request: {}", request, e);
            throw new BusinessException(CommonConstants.CONSTRAIN_VIOLATION,
                    ErrorCodes.CONSTRAINT_VIOLATION, e);
        } catch (IllegalArgumentException e) {
            log.warn("Validation failed for Form 60 Request: {} - {}", request, e.getMessage());
            throw new BusinessException(e.getMessage(), ErrorCodes.VALIDATION_FAILED, e);
        }
    }

    private void validateForm60Request(CustomerForm60RequestDto request) {
        if (request.getTransactionAmount() == null) {
            throw new BusinessException("Transaction amount is mandatory", ErrorCodes.VALIDATION_FAILED);
        }
        if (request.getTransactionDate() == null) {
            throw new BusinessException("Transaction date is mandatory", ErrorCodes.VALIDATION_FAILED);
        }

        if (request.getTransactionAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Transaction amount must be greater than zero", ErrorCodes.VALIDATION_FAILED);
        }

        if (request.getTransactionAmount().compareTo(MAX_TRANSACTION_AMOUNT) > 0) {
            throw new BusinessException("Form 60 cannot be used for transactions above ₹5,00,000. Please provide PAN.",
                    ErrorCodes.VALIDATION_FAILED);
        }
    }

    @Transactional(readOnly = true)
    public CustomerForm60ResponseDto getForm60ByIdentity(UUID customerIdentity, UUID form60Identity) {
        Customer customer = customerRepository.findByIdentity(customerIdentity)
                .orElseThrow(() -> new BusinessException(
                        CommonConstants.NOT_FOUND_MESSAGE,
                        ErrorCodes.NOT_FOUND));

        CustomerForm60 form60 = customerForm60Repository
                .findByIdentity(form60Identity)
                .orElseThrow(() -> new BusinessException(
                        "Form 60 not found with ID: " + form60Identity + " for customer: " + customerIdentity,
                        ErrorCodes.NOT_FOUND));

        return form60Mapper.toResponseDto(form60);
    }

    /*
     *
     * preview/download form 60
     */
    @Transactional(readOnly = true)
    public byte[] generateForm60PreviewPdf(UUID customerIdentity, UUID form60Identity) {
        Assert.notNull(customerIdentity, "customer Identity must not be null");
        Assert.notNull(form60Identity, "form60 Identity  must not be null");

        Customer customer = customerRepository.findByIdentityAndIsDelFalse(customerIdentity)
                .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.ENTITY_CUSTOMER, customerIdentity.toString()));

        CustomerForm60ResponseDto customerForm60ResponseDto = getForm60ByIdentity(customerIdentity, form60Identity);
        CustomerAddressDetailDto permanentAddress = getPermanentAddress(customer);
        CustomerPurposeResponseDto customerPurpose = getCustomerPurpose(customer);
        CustomerDesignationResponseDto designation = getCustomerDesignation(customer);
        String branchPlace = getBranchPlaceByForm60Identity(form60Identity);

        Map<String, Object> jasperDto = jasperForm60Mapper.form60ToJasperDto(
                customerForm60ResponseDto, customer, permanentAddress, customerPurpose, designation, branchPlace
        );

        log.info("Generating Form60 PDF for customerIdentity={}, form60Id={}", customerIdentity, form60Identity);

        try {
            return reportGenerator.generate(ReportName.FORM60, jasperDto, null, OutputFormat.PDF);
        } catch (Exception e) {
            log.error("Failed to generate Form60 PDF for customerIdentity={}, form60Id={}", customerIdentity, form60Identity, e);
            throw new BusinessException("Unable to generate Form60 report", e);
        }
    }

    /*
     *
     * get designation
     */
    @Transactional(readOnly = true)
    public CustomerDesignationResponseDto getCustomerDesignation(Customer customer) {
        return customerEmploymentRepository.findByCustomer(customer)
                .map(employment -> {
                    Designations designation = employment.getDesignationId();
                    return new CustomerDesignationResponseDto(
                            designation.getDesignationId(),
                            designation.getCode(),
                            designation.getName()
                    );
                })
                .orElseThrow(() -> new RuntimeException("Designation not found for customerId: " + customer.getIdentity()));
    }


    /*
     *
     * get permanent address
     */
    public CustomerAddressDetailDto getPermanentAddress(Customer customer) {

        AddressType permanentType = addressTypeRepository.findByAddressTypeNameAndIsDelFalse(AddressTypes.PERMANENT.name())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "AddressType",
                        "Permanent address type not found" + customer.getIdentity()
                ));

        CustomerAddress permanentAddress = customerAddressRepository
                .findByCustomerAndAddressTypeAndIsActiveTrueAndIsDelFalse(customer, permanentType)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        CommonConstants.ENTITY_ADDRESS,
                        "Permanent address not found for customer: " + customer.getIdentity()
                ));

        return customerAddressMapper.mapToCustomerAddressDetailDto(customer, permanentAddress);
    }

    /*
     * get customer purpose
     */

    public CustomerPurposeResponseDto getCustomerPurpose(Customer customer) {

        return customerProfileExtraRepository.findByCustomer(customer)
                .map(profile -> {
                    Purpose purpose = profile.getPurposeId();
                    return new CustomerPurposeResponseDto(
                            purpose.getPurposeId(),
                            purpose.getCode(),
                            purpose.getName()
                    );
                })
                .orElseThrow(() -> new RuntimeException("Purpose not found for customerId: " + customer.getIdentity()));
    }


    /*
     *
     * get branch details
     */
    String getBranchPlaceByForm60Identity(UUID form60Identity) {
        return customerForm60Repository.findByIdentity(form60Identity)
                .stream() // turn Optional<CustomerForm60> into Stream<CustomerForm60>
                .map(CustomerForm60::getBranchId) // get Branch entity
                .filter(Objects::nonNull)
                .map(Branches ::getPlaceName) // format string
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("branch not found for the  form 60 identity:", form60Identity.toString()));
    }


    /*
     *upload signed form 60
     */
    @Transactional
    public Form60UploadResponseDto uploadSignedForm60(UUID customerIdentity, UUID form60Identity, MultipartFile file) {

        if (file.isEmpty()) {
            throw new BusinessException("Uploaded file is empty", ErrorCodes.INVALID_REQUEST_FORMAT);
        }

        Customer customer = customerRepository.findByIdentityAndIsDelFalse(customerIdentity)
                .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.ENTITY_CUSTOMER, customerIdentity.toString()));

        CustomerForm60 form60 = customerForm60Repository.findByIdentity(form60Identity)
                .orElseThrow(() -> new ResourceNotFoundException("Form60 is not uploaded for the id:", form60Identity.toString()));


        Form60UploadResponseDto response = new Form60UploadResponseDto();
        response.setForm60Identity(form60.getIdentity());
        response.setPdfDocRefId(form60.getIdentity());
        response.setFileName(file.getOriginalFilename());
        response.setUploadedAt(OffsetDateTime.now());

        return response;
    }

}
