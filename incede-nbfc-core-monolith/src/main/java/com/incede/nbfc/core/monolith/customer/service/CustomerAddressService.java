package com.incede.nbfc.core.monolith.customer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerAddress;
import com.incede.nbfc.core.monolith.customer.dto.CustomerAddressRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerAddressResponseDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerBankAccountRequestDto;
import com.incede.nbfc.core.monolith.customer.mapper.CustomerAddressMapper;
import com.incede.nbfc.core.monolith.customer.repository.CustomerAddressRepository;
import com.incede.nbfc.core.monolith.customer.repository.CustomerRepository;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ErrorCodes;
import com.incede.nbfc.core.monolith.exception.ResourceNotFoundException;
import com.incede.nbfc.core.monolith.masterdata.repository.AddressTypeRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerAddressService {

    private final CustomerRepository customerRepository;
    private final CustomerAddressRepository addressRepository;
    private final CustomerAddressMapper addressMapper;
    private final ObjectMapper objectMapper;
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * create address
     * @param customerIdentity
     * @return
     */
    @Transactional
    public CustomerAddressResponseDto createAddress(UUID customerIdentity, String requestJson, MultipartFile file) throws JsonProcessingException {


        CustomerAddressRequestDto CustomerAddressRequestDto = objectMapper.readValue(requestJson, CustomerAddressRequestDto.class);
        Set<ConstraintViolation<CustomerAddressRequestDto>> violations = validator.validate(CustomerAddressRequestDto);

        if (!violations.isEmpty()) {
            String errorMsg = violations.stream()
                    .map(v -> v.getPropertyPath() + " " + v.getMessage())
                    .reduce((m1, m2) -> m1 + ", " + m2)
                    .orElse("Validation failed");
            throw new BusinessException(errorMsg, ErrorCodes.VALIDATION_FAILED);
        }
        Customer customer = customerRepository.findByIdentity(customerIdentity)
                .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.ENTITY_CUSTOMER, customerIdentity.toString()));


        CustomerAddress address = addressMapper.toEntity(customer, CustomerAddressRequestDto);


        if (Boolean.TRUE.equals(CustomerAddressRequestDto.getIsSameAsPermanent())) {
            if (file == null || file.isEmpty()) {
                throw new BusinessException("Document file must be provided if 'isSameAsPermanent' is true");
            }
            Integer documentRefId = uploadDocument(file);
            address.setDocumentRefId(documentRefId);
        } else {
            address.setDocumentRefId(null);
        }


        CustomerAddress savedAddress = addressRepository.save(address);


        CustomerAddressResponseDto.AddressDetail detail = addressMapper.toAddressDetail(savedAddress);
        return addressMapper.toResponse(customer, CommonConstants.CUSTOMER_ADDRESS_STATUS_IN_PROGRESS, List.of(detail));
    }

    /**
     *update address
     * @param customerIdentity
     * @param addressIdentity
     * @return
     */
    @Transactional
    public CustomerAddressResponseDto updateAddress(UUID customerIdentity, UUID addressIdentity,
                                                    String requestJson, MultipartFile file) throws JsonProcessingException {

        CustomerAddressRequestDto requestDTO = objectMapper.readValue(requestJson, CustomerAddressRequestDto.class);

        Customer customer = customerRepository.findByIdentity(customerIdentity)
                .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.ENTITY_CUSTOMER, customerIdentity.toString()));

        CustomerAddress address = addressRepository
                .findByIdentity(addressIdentity)
                .filter(a -> a.getCustomer().getCustomerId().equals(customer.getCustomerId()))
                .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.ENTITY_ADDRESS, addressIdentity.toString()));

        if (Boolean.TRUE.equals(requestDTO.getIsSameAsPermanent())) {
            if (file == null || file.isEmpty()) {
                throw new BusinessException("Document file must be provided if 'isSameAsPermanent' is true");
            }
            Integer documentRefId = uploadDocument(file);
            address.setDocumentRefId(documentRefId);
        } else {
            address.setDocumentRefId(null);
        }

        CustomerAddress updatedAddress = addressRepository.save(address);

        CustomerAddressResponseDto.AddressDetail detail = addressMapper.toAddressDetail(updatedAddress);
        return addressMapper.toResponse(customer, CommonConstants.CUSTOMER_ADDRESS_STATUS_UPDATED, List.of(detail));
    }



    /**
     *
     * @param customerIdentity
     * @param addressIdentity
     */

    @Transactional
    public void deleteAddress(UUID customerIdentity, UUID addressIdentity) {
        Customer customer = customerRepository.findByIdentity(customerIdentity)
                .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.ENTITY_CUSTOMER, customerIdentity.toString()));

        CustomerAddress address = addressRepository
                .findByIdentity(addressIdentity)
                .filter(a -> a.getCustomer().getCustomerId().equals(customer.getCustomerId()))
                .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.ENTITY_ADDRESS, addressIdentity.toString()));

        address.setIsDel(true);
        address.setIsActive(false);
        addressRepository.save(address);
    }

    /**
     * get all active address by Customer-Identity
     * @param customerIdentity
     * @return
     */
    @Transactional(readOnly = true)
    public CustomerAddressResponseDto getActiveAddressesByCustomerIdentity(UUID customerIdentity) {
        Customer customer = customerRepository.findByIdentityAndIsDelFalse(customerIdentity)
                .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.ENTITY_CUSTOMER, customerIdentity.toString()));

        List<CustomerAddress> addresses = addressRepository.findByCustomerAndIsDelFalse(customer);

        if (addresses.isEmpty()) {
            throw new ResourceNotFoundException(CommonConstants.ENTITY_ADDRESS,
                    "No active addresses found for customer identity: " + customerIdentity);
        }
        List<CustomerAddressResponseDto.AddressDetail> addressDetails = addresses.stream()
                .map(addressMapper::toAddressDetail)
                .toList();
        return addressMapper.toResponse(customer, CommonConstants.CUSTOMER_ADDRESS_STATUS_SUCCESS, addressDetails);
    }
    public Integer uploadDocument(MultipartFile file) {
        return Math.abs(UUID.randomUUID().hashCode());
    }


}
