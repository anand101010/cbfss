package com.incede.nbfc.core.monolith.customer.service;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerAddress;
import com.incede.nbfc.core.monolith.customer.dto.CustomerAddressRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerAddressResponseDto;
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

    @Transactional
    public CustomerAddressResponseDto createAddress(UUID identity, CustomerAddressRequestDto requestDto) {
        Customer customer = customerRepository.findByIdentity(identity)
                .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.ENTITY_CUSTOMER, identity.toString()));

        CustomerAddress address = addressMapper.toEntity(customer, requestDto);
        CustomerAddress savedAddress = addressRepository.save(address);

        CustomerAddressResponseDto.AddressDetail detail = addressMapper.toAddressDetail(savedAddress);
        return addressMapper.toResponse(customer, CommonConstants.CUSTOMER_ADDRESS_STATUS_IN_PROGRESS, List.of(detail));
    }

    @Transactional
    public CustomerAddressResponseDto updateAddress(UUID customerIdentity, Integer addressId,
                                                    CustomerAddressRequestDto requestDto) {
        Customer customer = customerRepository.findByIdentity(customerIdentity)
                .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.ENTITY_CUSTOMER, customerIdentity.toString()));

        CustomerAddress address = addressRepository
                .findByAddressIdAndCustomer_CustomerId(addressId, customer.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.ENTITY_ADDRESS, addressId.toString()));

        addressMapper.updateEntity(address, requestDto);
        CustomerAddress updatedAddress = addressRepository.save(address);

        CustomerAddressResponseDto.AddressDetail detail = addressMapper.toAddressDetail(updatedAddress);
        return addressMapper.toResponse(customer, CommonConstants.CUSTOMER_ADDRESS_STATUS_UPDATED, List.of(detail));
    }

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

    @Transactional(readOnly = true)
    public CustomerAddressResponseDto getActiveAddressesByCustomerIdentity(UUID identity) {
        Customer customer = customerRepository.findByIdentityAndIsDelFalse(identity)
                .orElseThrow(() -> new ResourceNotFoundException(CommonConstants.ENTITY_CUSTOMER, identity.toString()));

        List<CustomerAddress> addresses = addressRepository.findByCustomerAndIsDelFalse(customer);

        if (addresses.isEmpty()) {
            throw new ResourceNotFoundException(CommonConstants.ENTITY_ADDRESS,
                    "No active addresses found for customer identity: " + identity);
        }

        List<CustomerAddressResponseDto.AddressDetail> addressDetails = addresses.stream()
                .map(addressMapper::toAddressDetail)
                .toList();

        return addressMapper.toResponse(customer, CommonConstants.CUSTOMER_ADDRESS_STATUS_SUCCESS, addressDetails);
    }
}
