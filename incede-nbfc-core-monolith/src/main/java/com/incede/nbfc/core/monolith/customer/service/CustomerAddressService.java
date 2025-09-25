package com.incede.nbfc.core.monolith.customer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerAddress;
import com.incede.nbfc.core.monolith.customer.dto.CustomerAddressRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerAddressResponseDto;
import com.incede.nbfc.core.monolith.customer.mapper.CustomerAddressMapper;
import com.incede.nbfc.core.monolith.customer.repository.CustomerAddressRepository;
import com.incede.nbfc.core.monolith.customer.repository.CustomerRepository;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ErrorCodes;
import com.incede.nbfc.core.monolith.exception.ResourceNotFoundException;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.AddressProofType;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.AddressType;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.PostOffices;
import com.incede.nbfc.core.monolith.masterdata.repository.AddressProofTypeRepository;
import com.incede.nbfc.core.monolith.masterdata.repository.AddressTypeRepository;
import com.incede.nbfc.core.monolith.masterdata.repository.PostOfficesRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerAddressService {

    private static final Logger log = LoggerFactory.getLogger(CustomerAddressService.class);

    private final CustomerRepository customerRepository;
    private final CustomerAddressRepository addressRepository;
    private final CustomerAddressMapper addressMapper;
    private final ObjectMapper objectMapper;

    private final AddressTypeRepository addressTypeRepository;
    private final PostOfficesRepository postOfficesRepository;
    private final AddressProofTypeRepository addressProofTypeRepository;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Transactional
    public CustomerAddressResponseDto createAddress(UUID customerIdentity, String requestJson, MultipartFile file) throws JsonProcessingException {
        log.info("Creating new address for customerIdentity: {}", customerIdentity);

        CustomerAddressRequestDto dto = objectMapper.readValue(requestJson, CustomerAddressRequestDto.class);
        validateDto(dto);

        Customer customer = customerRepository.findByIdentity(customerIdentity)
                .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.ENTITY_CUSTOMER, customerIdentity.toString()));
        log.debug("Found customer: {}", customer.getCustomerCode());

        CustomerAddress address = addressMapper.toEntity(customer, dto);
        log.debug("Mapped CustomerAddress entity from DTO: {}", address);

        AddressType addressType = addressTypeRepository.findByIdentity(dto.getAddressType())
                .orElseThrow(() -> new BusinessException("Invalid Address Type", ErrorCodes.VALIDATION_FAILED));
        address.setAddressType(addressType);

        PostOffices postOffice = postOfficesRepository.findByIdentity(dto.getPostOfficeId())
                .orElseThrow(() -> new BusinessException("Invalid Post Office", ErrorCodes.VALIDATION_FAILED));
        address.setPostOffice(postOffice);

        AddressProofType addressProof = addressProofTypeRepository.findByIdentity(dto.getAddressProofType())
                .orElseThrow(() -> new BusinessException("Invalid Address Proof Type", ErrorCodes.VALIDATION_FAILED));
        address.setAddressProofType(addressProof);
        log.debug("Set AddressProofType: {}", addressProof.getName());

        if (Boolean.TRUE.equals(dto.getIsSameAsPermanent())) {
            if (file == null || file.isEmpty()) {
                log.error("'isSameAsPermanent' is true but no file provided");
                throw new BusinessException("Document file must be provided if 'isSameAsPermanent' is true");
            }
            Integer documentRefId = uploadDocument(file);
            address.setDocumentRefId(documentRefId);
            log.debug("Uploaded document and set documentRefId: {}", documentRefId);
        } else {
            address.setDocumentRefId(null);
        }

        CustomerAddress savedAddress = addressRepository.save(address);
        log.info("Saved CustomerAddress with id: {}", savedAddress.getAddressId());

        CustomerAddressResponseDto.AddressDetail detail = addressMapper.toAddressDetail(savedAddress);
        return addressMapper.toResponse(customer, CommonConstants.CUSTOMER_ADDRESS_STATUS_IN_PROGRESS, List.of(detail));
    }

    @Transactional
    public CustomerAddressResponseDto updateAddress(UUID customerIdentity, UUID addressIdentity, String requestJson, MultipartFile file) throws JsonProcessingException {
        log.info("Updating address {} for customer {}", addressIdentity, customerIdentity);

        CustomerAddressRequestDto dto = objectMapper.readValue(requestJson, CustomerAddressRequestDto.class);
        validateDto(dto);

        Customer customer = customerRepository.findByIdentity(customerIdentity)
                .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.ENTITY_CUSTOMER, customerIdentity.toString()));
        log.debug("Found customer: {}", customer.getCustomerCode());

        CustomerAddress address = addressRepository.findByIdentity(addressIdentity)
                .filter(a -> a.getCustomer().getCustomerId().equals(customer.getCustomerId()))
                .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.ENTITY_ADDRESS, addressIdentity.toString()));
        log.debug("Found address to update: {}", address.getAddressId());

        addressMapper.updateEntity(address, dto);
        log.debug("Updated CustomerAddress entity from DTO");

        AddressType addressType = addressTypeRepository.findByIdentity(dto.getAddressType())
                .orElseThrow(() -> new BusinessException("Invalid Address Type", ErrorCodes.VALIDATION_FAILED));
        address.setAddressType(addressType);

        PostOffices postOffice = postOfficesRepository.findByIdentity(dto.getPostOfficeId())
                .orElseThrow(() -> new BusinessException("Invalid Post Office", ErrorCodes.VALIDATION_FAILED));
        address.setPostOffice(postOffice);

        AddressProofType addressProof = addressProofTypeRepository.findByIdentity(dto.getAddressProofType())
                .orElseThrow(() -> new BusinessException("Invalid Address Proof Type", ErrorCodes.VALIDATION_FAILED));
        address.setAddressProofType(addressProof);

        if (Boolean.TRUE.equals(dto.getIsSameAsPermanent())) {
            if (file == null || file.isEmpty()) {
                log.error("'isSameAsPermanent' is true but no file provided");
                throw new BusinessException("Document file must be provided if 'isSameAsPermanent' is true");
            }
            Integer documentRefId = uploadDocument(file);
            address.setDocumentRefId(documentRefId);
            log.debug("Uploaded document and set documentRefId: {}", documentRefId);
        } else {
            address.setDocumentRefId(null);
        }

        CustomerAddress updatedAddress = addressRepository.save(address);
        log.info("Updated CustomerAddress with id: {}", updatedAddress.getAddressId());

        CustomerAddressResponseDto.AddressDetail detail = addressMapper.toAddressDetail(updatedAddress);
        return addressMapper.toResponse(customer, CommonConstants.CUSTOMER_ADDRESS_STATUS_UPDATED, List.of(detail));
    }

    @Transactional
    public void deleteAddress(UUID customerIdentity, UUID addressIdentity) {
        log.info("Deleting address {} for customer {}", addressIdentity, customerIdentity);

        Customer customer = customerRepository.findByIdentity(customerIdentity)
                .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.ENTITY_CUSTOMER, customerIdentity.toString()));

        CustomerAddress address = addressRepository.findByIdentity(addressIdentity)
                .filter(a -> a.getCustomer().getCustomerId().equals(customer.getCustomerId()))
                .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.ENTITY_ADDRESS, addressIdentity.toString()));

        address.setIsDel(true);
        address.setIsActive(false);
        addressRepository.save(address);

        log.info("Soft-deleted CustomerAddress with id: {}", address.getAddressId());
    }

    @Transactional(readOnly = true)
    public CustomerAddressResponseDto getActiveAddressesByCustomerIdentity(UUID customerIdentity) {
        log.info("Fetching active addresses for customerIdentity: {}", customerIdentity);

        Customer customer = customerRepository.findByIdentityAndIsDelFalse(customerIdentity)
                .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.ENTITY_CUSTOMER, customerIdentity.toString()));

        List<CustomerAddress> addresses = addressRepository.findByCustomerAndIsDelFalse(customer);

        if (addresses.isEmpty()) {
            log.warn("No active addresses found for customerIdentity: {}", customerIdentity);
            throw new ResourceNotFoundException(CommonConstants.ENTITY_ADDRESS,
                    "No active addresses found for customer identity: " + customerIdentity);
        }

        List<CustomerAddressResponseDto.AddressDetail> addressDetails = addresses.stream()
                .map(addressMapper::toAddressDetail)
                .toList();

        log.info("Found {} active addresses for customerIdentity: {}", addressDetails.size(), customerIdentity);
        return addressMapper.toResponse(customer, CommonConstants.CUSTOMER_ADDRESS_STATUS_SUCCESS, addressDetails);
    }

    private void validateDto(CustomerAddressRequestDto dto) {
        Set<ConstraintViolation<CustomerAddressRequestDto>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            String errorMsg = violations.stream()
                    .map(v -> v.getPropertyPath() + " " + v.getMessage())
                    .reduce((m1, m2) -> m1 + ", " + m2)
                    .orElse("Validation failed");
            log.error("DTO validation failed: {}", errorMsg);
            throw new BusinessException(errorMsg, ErrorCodes.VALIDATION_FAILED);
        } else {
            log.debug("DTO validation passed");
        }
    }

    private Integer uploadDocument(MultipartFile file) {
        Integer refId = Math.abs(UUID.randomUUID().hashCode());
        log.debug("Simulated document upload, generated documentRefId: {}", refId);
        return refId;
    }
}
