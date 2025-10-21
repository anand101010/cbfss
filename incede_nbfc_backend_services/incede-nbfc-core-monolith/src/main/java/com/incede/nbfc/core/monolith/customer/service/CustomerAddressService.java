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

    private final AddressTypeRepository addressTypeRepository;
    private final PostOfficesRepository postOfficesRepository;
    private final AddressProofTypeRepository addressProofTypeRepository;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * Creates and saves a new customer address after validating all related entities.
     *
     * @param customerIdentity UUID of the customer for whom the address is being created
     * @param dto              Request body containing address details
     * @return CustomerAddressResponseDto containing created address information
     */
    @Transactional
    public CustomerAddressResponseDto createAddress(UUID customerIdentity, CustomerAddressRequestDto dto){
        log.info("Creating new address for customerIdentity: {}", customerIdentity);



        Customer customer = customerRepository.findByIdentity(customerIdentity)
                .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.CUSTOMER_NOT_FOUND));

        CustomerAddress address = addressMapper.toEntity(customer, dto);

        AddressType addressType = addressTypeRepository.findByIdentity(dto.getAddressType())
                .orElseThrow(() -> new BusinessException(CommonConstants.INVALID_ADDRESS_TYPE, ErrorCodes.VALIDATION_FAILED));
        address.setAddressType(addressType);

        PostOffices postOffice = postOfficesRepository.findByIdentity(dto.getPostOfficeId())
                .orElseThrow(() -> new BusinessException(CommonConstants.INVALID_POST_OFFICE, ErrorCodes.VALIDATION_FAILED));
        address.setPostOffice(postOffice);

        AddressProofType addressProof = addressProofTypeRepository.findByIdentity(dto.getAddressProofType())
                .orElseThrow(() -> new BusinessException(CommonConstants.INVALID_ADDRESS_PROOF_TYPE, ErrorCodes.VALIDATION_FAILED));
        address.setAddressProofType(addressProof);


        CustomerAddress savedAddress = addressRepository.save(address);
        CustomerAddressResponseDto.AddressDetail detail = addressMapper.toAddressDetail(savedAddress);
        return addressMapper.toResponse(customer, CommonConstants.CUSTOMER_ADDRESS_STATUS_IN_PROGRESS, List.of(detail));
    }

    /**
     * Updates an existing customer address by validating the provided data and linked entities.
     *
     * @param customerIdentity UUID of the customer
     * @param addressIdentity  UUID of the address to be updated
     * @param dto              Request body containing updated address details
     * @return CustomerAddressResponseDto with updated address information
     */
    @Transactional
    public CustomerAddressResponseDto updateAddress(UUID customerIdentity, UUID addressIdentity, CustomerAddressRequestDto dto){
        log.info("Updating address {} for customer {}", addressIdentity, customerIdentity);

        validateDto(dto);

        Customer customer = customerRepository.findByIdentity(customerIdentity)
                .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.CUSTOMER_NOT_FOUND));

        CustomerAddress address = addressRepository.findByIdentity(addressIdentity)
                .filter(a -> a.getCustomer().getCustomerId().equals(customer.getCustomerId()))
                .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.ADDRESS_NOT_FOUND));

        addressMapper.updateEntity(address, dto);

        AddressType addressType = addressTypeRepository.findByIdentity(dto.getAddressType())
                .orElseThrow(() -> new BusinessException(CommonConstants.INVALID_ADDRESS_TYPE, ErrorCodes.VALIDATION_FAILED));
        address.setAddressType(addressType);

        PostOffices postOffice = postOfficesRepository.findByIdentity(dto.getPostOfficeId())
                .orElseThrow(() -> new BusinessException(CommonConstants.INVALID_POST_OFFICE, ErrorCodes.VALIDATION_FAILED));
        address.setPostOffice(postOffice);

        AddressProofType addressProof = addressProofTypeRepository.findByIdentity(dto.getAddressProofType())
                .orElseThrow(() -> new BusinessException(CommonConstants.INVALID_ADDRESS_PROOF_TYPE, ErrorCodes.VALIDATION_FAILED));
        address.setAddressProofType(addressProof);

        CustomerAddress updatedAddress = addressRepository.save(address);
        CustomerAddressResponseDto.AddressDetail detail = addressMapper.toAddressDetail(updatedAddress);
        return addressMapper.toResponse(customer, CommonConstants.CUSTOMER_ADDRESS_STATUS_UPDATED, List.of(detail));
    }

    /**
     * Soft deletes a customer address by marking it inactive and deleted.
     *
     * @param customerIdentity UUID of the customer
     * @param addressIdentity  UUID of the address to be deleted
     */

    @Transactional
    public void deleteAddress(UUID customerIdentity, UUID addressIdentity) {
        Customer customer = customerRepository.findByIdentity(customerIdentity)
                .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.CUSTOMER_NOT_FOUND));

        CustomerAddress address = addressRepository.findByIdentity(addressIdentity)
                .filter(a -> a.getCustomer().getCustomerId().equals(customer.getCustomerId()))
                .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.ADDRESS_NOT_FOUND));

        address.setIsDel(true);
        address.setIsActive(false);
        addressRepository.save(address);
    }


    /**
     * Retrieves all active addresses associated with a given customer.
     *
     * @param customerIdentity UUID of the customer
     * @return CustomerAddressResponseDto containing a list of active addresses
     */
    @Transactional(readOnly = true)
    public CustomerAddressResponseDto getActiveAddressesByCustomerIdentity(UUID customerIdentity) {
        Customer customer = customerRepository.findByIdentityAndIsDelFalse(customerIdentity)
                .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.ENTITY_CUSTOMER, customerIdentity.toString()));

        List<CustomerAddress> addresses = addressRepository.findByCustomerAndIsDelFalse(customer);


        List<CustomerAddressResponseDto.AddressDetail> addressDetails = addresses.stream()
                .map(addressMapper::toAddressDetail)
                .toList();

        return addressMapper.toResponse(customer, CommonConstants.CUSTOMER_ADDRESS_STATUS_SUCCESS, addressDetails);
    }

    /**
     * Validates the CustomerAddressRequestDto using Bean Validation.
     *
     * @param dto DTO containing address details to validate
     * @throws BusinessException if validation fails with constraint violations
     */
    private void validateDto(CustomerAddressRequestDto dto) {
        Set<ConstraintViolation<CustomerAddressRequestDto>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            String errorMsg = violations.stream()
                    .map(v -> v.getPropertyPath() + " " + v.getMessage())
                    .reduce((m1, m2) -> m1 + ", " + m2)
                    .orElse(CommonConstants.VALIDATION_ERROR_MESSAGE);
            throw new BusinessException(errorMsg, ErrorCodes.VALIDATION_FAILED);
        }
    }

}
