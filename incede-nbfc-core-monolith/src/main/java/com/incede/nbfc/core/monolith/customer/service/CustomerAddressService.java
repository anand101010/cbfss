package com.incede.nbfc.core.monolith.customer.service;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerAddress;
import com.incede.nbfc.core.monolith.customer.dto.CustomerAddressRequestDTO;
import com.incede.nbfc.core.monolith.customer.dto.CustomerAddressResponseDTO;
import com.incede.nbfc.core.monolith.customer.mapper.CustomerAddressMapper;
import com.incede.nbfc.core.monolith.customer.repository.CustomerAddressRepository;
import com.incede.nbfc.core.monolith.customer.repository.CustomerRepository;
import com.incede.nbfc.core.monolith.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class CustomerAddressService {

    private final CustomerRepository customerRepository;
    private final CustomerAddressRepository addressRepository;
    private final CustomerAddressMapper addressMapper;

    public CustomerAddressService(CustomerRepository customerRepository,
                                  CustomerAddressRepository addressRepository,
                                  CustomerAddressMapper addressMapper) {
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
    }

    /**
     *
     * @param identity
     * @param requestDTO
     * @return
     */
    @Transactional
    public CustomerAddressResponseDTO createAddress(UUID identity, CustomerAddressRequestDTO requestDTO) {
        Customer customer = customerRepository.findByIdentity(identity)
                .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.ENTITY_CUSTOMER, identity.toString()));

        CustomerAddress address = addressMapper.toEntity(customer, requestDTO);
        CustomerAddress savedAddress = addressRepository.save(address);

        CustomerAddressResponseDTO.AddressDetail detail = addressMapper.toAddressDetail(savedAddress);
        return addressMapper.toResponse(customer, CommonConstants.CUSTOMER_ADDRESS_STATUS_IN_PROGRESS, List.of(detail));
    }

    /**
     *
     * @param customerIdentity
     * @param addressId
     * @param requestDTO
     * @return
     */
    @Transactional
    public CustomerAddressResponseDTO updateAddress(UUID customerIdentity, Integer addressId,
                                                    CustomerAddressRequestDTO requestDTO) {
        Customer customer = customerRepository.findByIdentity(customerIdentity)
                .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.ENTITY_CUSTOMER, customerIdentity.toString()));

        CustomerAddress address = addressRepository
                .findByAddressIdAndCustomer_CustomerId(addressId, customer.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.ENTITY_ADDRESS, addressId.toString()));

        addressMapper.updateEntity(address, requestDTO);
        CustomerAddress updatedAddress = addressRepository.save(address);

        CustomerAddressResponseDTO.AddressDetail detail = addressMapper.toAddressDetail(updatedAddress);
        return addressMapper.toResponse(customer, CommonConstants.CUSTOMER_ADDRESS_STATUS_UPDATED, List.of(detail));
    }

    /**
     *
     * @param customerIdentity
     * @param addressId
     */

    @Transactional
    public void deleteAddress(UUID customerIdentity, Integer addressId) {
        Customer customer = customerRepository.findByIdentity(customerIdentity)
                .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.ENTITY_CUSTOMER, customerIdentity.toString()));

        CustomerAddress address = addressRepository
                .findByAddressIdAndCustomer_CustomerId(addressId, customer.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.ENTITY_ADDRESS, addressId.toString()));

        address.setIsDel(true);
        address.setIsActive(false);
        addressRepository.save(address);
    }

    /**
     *
     * @param identity
     * @return
     */

    @Transactional(readOnly = true)
    public CustomerAddressResponseDTO getActiveAddressesByCustomerIdentity(UUID identity) {
        Customer customer = customerRepository.findByIdentityAndIsDelFalse(identity)
                .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.ENTITY_CUSTOMER, identity.toString()));

        List<CustomerAddress> addresses = addressRepository.findByCustomerAndIsDelFalse(customer);

        if (addresses.isEmpty()) {
            throw new ResourceNotFoundException(CommonConstants.ENTITY_ADDRESS,
                    "No active addresses found for customer identity: " + identity);
        }

        List<CustomerAddressResponseDTO.AddressDetail> addressDetails = addresses.stream()
                .map(addressMapper::toAddressDetail)
                .toList();

        return addressMapper.toResponse(customer, CommonConstants.CUSTOMER_ADDRESS_STATUS_SUCCESS, addressDetails);
    }
}
