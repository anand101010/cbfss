package com.incede.nbfc.core.monolith.customer.service;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerAddressServiceTest {

    @Mock private CustomerRepository customerRepository;
    @Mock private CustomerAddressRepository addressRepository;
    @Mock private CustomerAddressMapper addressMapper;
    @Mock private AddressTypeRepository addressTypeRepository;
    @Mock private PostOfficesRepository postOfficesRepository;
    @Mock private AddressProofTypeRepository addressProofTypeRepository;

    @InjectMocks
    private CustomerAddressService customerAddressService;

    private UUID customerId;
    private UUID addressId;
    private Customer customer;
    private CustomerAddress address;
    private CustomerAddressRequestDto requestDto;
    private CustomerAddressResponseDto responseDto;
    private AddressType addressType;
    private PostOffices postOffice;
    private AddressProofType addressProofType;

    @BeforeEach
    void setUp() {
        customerId = UUID.randomUUID();
        addressId = UUID.randomUUID();

        customer = new Customer();
        customer.setCustomerId(1);
        customer.setIdentity(customerId);
        customer.setCustomerCode("CUST001");

        address = new CustomerAddress();
        address.setAddressId(1);
        address.setIdentity(addressId);
        address.setCustomer(customer);
        address.setIsActive(true);
        address.setIsDel(false);

        addressType = new AddressType();
        addressType.setIdentity(UUID.randomUUID());

        postOffice = new PostOffices();
        postOffice.setIdentity(UUID.randomUUID());

        addressProofType = new AddressProofType();
        addressProofType.setIdentity(UUID.randomUUID());

        requestDto = CustomerAddressRequestDto.builder()
                .addressType(addressType.getIdentity())
                .doorNumber("123")
                .addressLine1("Main Street")
                .placeName("Some Place")
                .city("Metropolis")
                .district("Central District")
                .state("StateName")
                .country("CountryName")
                .pincode("560001")
                .postOfficeId(postOffice.getIdentity())
                .latitude(new BigDecimal("12.9716"))
                .longitude(new BigDecimal("77.5946"))
                .geoAccuracy(new BigDecimal("10.50"))
                .addressProofType(addressProofType.getIdentity())
                .isActive(true)
                .isSameAsPermanent(false)
                .digipin("DIG210003")
                .customerCode("CUST001")
                .documentRefId("DOC123")
                .build();

        CustomerAddressResponseDto.AddressDetail addressDetail = CustomerAddressResponseDto.AddressDetail.builder()
                .addressIdentity(addressId)
                .addressType(addressType.getIdentity())
                .doorNumber("123")
                .addressLine1("Main Street")
                .placeName("Some Place")
                .city("Metropolis")
                .district("Central District")
                .state("StateName")
                .country("CountryName")
                .pincode("560001")
                .postOffice(postOffice.getIdentity())
                .latitude(new BigDecimal("12.9716"))
                .longitude(new BigDecimal("77.5946"))
                .geoAccuracy(new BigDecimal("10.50"))
                .addressProofType(addressProofType.getIdentity())
                .isActive(true)
                .digipin("DIG210003")
                .documentRefId("DOC123")
                .build();

        responseDto = CustomerAddressResponseDto.builder()
                .identity(customerId)
                .customerCode("CUST001")
                .status("SUCCESS")
                .addresses(List.of(addressDetail))
                .build();
    }

    @Test
    void testCreateAddress_Success() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(addressMapper.toEntity(customer, requestDto)).thenReturn(address);
        when(addressTypeRepository.findByIdentity(requestDto.getAddressType())).thenReturn(Optional.of(addressType));
        when(postOfficesRepository.findByIdentity(requestDto.getPostOfficeId())).thenReturn(Optional.of(postOffice));
        when(addressProofTypeRepository.findByIdentity(requestDto.getAddressProofType())).thenReturn(Optional.of(addressProofType));
        when(addressRepository.save(address)).thenReturn(address);
        when(addressMapper.toAddressDetail(address)).thenReturn(responseDto.getAddresses().get(0));
        when(addressMapper.toResponse(eq(customer), eq(CommonConstants.CUSTOMER_ADDRESS_STATUS_IN_PROGRESS), anyList())).thenReturn(responseDto);

        CustomerAddressResponseDto result = customerAddressService.createAddress(customerId, requestDto);

        assertNotNull(result);
        assertEquals(responseDto, result);
        verify(addressRepository, times(1)).save(address);
        verify(addressMapper, times(1)).toResponse(eq(customer), eq(CommonConstants.CUSTOMER_ADDRESS_STATUS_IN_PROGRESS), anyList());
    }

    @Test
    void testCreateAddress_CustomerNotFound() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> customerAddressService.createAddress(customerId, requestDto));

        assertEquals(CommonConstants.CUSTOMER_NOT_FOUND, ex.getMessage());
        verify(addressRepository, never()).save(any());
    }

    @Test
    void testCreateAddress_InvalidAddressType() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(addressMapper.toEntity(customer, requestDto)).thenReturn(address);
        when(addressTypeRepository.findByIdentity(requestDto.getAddressType())).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class,
                () -> customerAddressService.createAddress(customerId, requestDto));

        assertEquals(ErrorCodes.VALIDATION_FAILED, ex.getErrorCode());
        assertEquals(CommonConstants.INVALID_ADDRESS_TYPE, ex.getMessage());
        verify(addressRepository, never()).save(any());
    }

    @Test
    void testCreateAddress_InvalidPostOffice() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(addressMapper.toEntity(customer, requestDto)).thenReturn(address);
        when(addressTypeRepository.findByIdentity(requestDto.getAddressType())).thenReturn(Optional.of(addressType));
        when(postOfficesRepository.findByIdentity(requestDto.getPostOfficeId())).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class,
                () -> customerAddressService.createAddress(customerId, requestDto));

        assertEquals(ErrorCodes.VALIDATION_FAILED, ex.getErrorCode());
        assertEquals(CommonConstants.INVALID_POST_OFFICE, ex.getMessage());
        verify(addressRepository, never()).save(any());
    }

    @Test
    void testCreateAddress_InvalidAddressProofType() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(addressMapper.toEntity(customer, requestDto)).thenReturn(address);
        when(addressTypeRepository.findByIdentity(requestDto.getAddressType())).thenReturn(Optional.of(addressType));
        when(postOfficesRepository.findByIdentity(requestDto.getPostOfficeId())).thenReturn(Optional.of(postOffice));
        when(addressProofTypeRepository.findByIdentity(requestDto.getAddressProofType())).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class,
                () -> customerAddressService.createAddress(customerId, requestDto));

        assertEquals(ErrorCodes.VALIDATION_FAILED, ex.getErrorCode());
        assertEquals(CommonConstants.INVALID_ADDRESS_PROOF_TYPE, ex.getMessage());
        verify(addressRepository, never()).save(any());
    }

    @Test
    void testUpdateAddress_Success() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(addressRepository.findByIdentity(addressId)).thenReturn(Optional.of(address));
        when(addressTypeRepository.findByIdentity(requestDto.getAddressType())).thenReturn(Optional.of(addressType));
        when(postOfficesRepository.findByIdentity(requestDto.getPostOfficeId())).thenReturn(Optional.of(postOffice));
        when(addressProofTypeRepository.findByIdentity(requestDto.getAddressProofType())).thenReturn(Optional.of(addressProofType));
        when(addressRepository.save(address)).thenReturn(address);
        when(addressMapper.toAddressDetail(address)).thenReturn(responseDto.getAddresses().get(0));
        when(addressMapper.toResponse(eq(customer), eq(CommonConstants.CUSTOMER_ADDRESS_STATUS_UPDATED), anyList())).thenReturn(responseDto);

        CustomerAddressResponseDto result = customerAddressService.updateAddress(customerId, addressId, requestDto);

        assertNotNull(result);
        assertEquals(responseDto, result);
        verify(addressMapper, times(1)).updateEntity(address, requestDto);
        verify(addressRepository, times(1)).save(address);
        verify(addressMapper, times(1)).toResponse(eq(customer), eq(CommonConstants.CUSTOMER_ADDRESS_STATUS_UPDATED), anyList());
    }

    @Test
    void testUpdateAddress_CustomerNotFound() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> customerAddressService.updateAddress(customerId, addressId, requestDto));

        assertEquals(CommonConstants.CUSTOMER_NOT_FOUND, ex.getMessage());
        verify(addressRepository, never()).save(any());
    }

    @Test
    void testUpdateAddress_AddressNotFound() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(addressRepository.findByIdentity(addressId)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> customerAddressService.updateAddress(customerId, addressId, requestDto));

        assertEquals(CommonConstants.ADDRESS_NOT_FOUND, ex.getMessage());
        verify(addressRepository, never()).save(any());
    }

    @Test
    void testDeleteAddress_Success() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(addressRepository.findByIdentity(addressId)).thenReturn(Optional.of(address));

        customerAddressService.deleteAddress(customerId, addressId);

        assertTrue(address.getIsDel());
        assertFalse(address.getIsActive());
        verify(addressRepository, times(1)).save(address);
    }

    @Test
    void testDeleteAddress_CustomerNotFound() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> customerAddressService.deleteAddress(customerId, addressId));

        assertEquals(CommonConstants.CUSTOMER_NOT_FOUND, ex.getMessage());
        verify(addressRepository, never()).save(any());
    }

    @Test
    void testDeleteAddress_AddressNotFound() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(addressRepository.findByIdentity(addressId)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> customerAddressService.deleteAddress(customerId, addressId));

        assertEquals(CommonConstants.ADDRESS_NOT_FOUND, ex.getMessage());
        verify(addressRepository, never()).save(any());
    }

    @Test
    void testGetActiveAddressesByCustomerIdentity_Success() {
        when(customerRepository.findByIdentityAndIsDelFalse(customerId)).thenReturn(Optional.of(customer));
        when(addressRepository.findByCustomerAndIsDelFalse(customer)).thenReturn(List.of(address));
        when(addressMapper.toAddressDetail(address)).thenReturn(responseDto.getAddresses().get(0));
        when(addressMapper.toResponse(eq(customer), eq(CommonConstants.CUSTOMER_ADDRESS_STATUS_SUCCESS), anyList())).thenReturn(responseDto);

        CustomerAddressResponseDto result = customerAddressService.getActiveAddressesByCustomerIdentity(customerId);

        assertNotNull(result);
        assertEquals(responseDto, result);
        verify(addressRepository, times(1)).findByCustomerAndIsDelFalse(customer);
    }



    @Test
    void testGetActiveAddressesByCustomerIdentity_CustomerNotFound() {
        when(customerRepository.findByIdentityAndIsDelFalse(customerId)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> customerAddressService.getActiveAddressesByCustomerIdentity(customerId));

        assertTrue(ex.getMessage().contains(customerId.toString()));
        verify(addressRepository, never()).findByCustomerAndIsDelFalse(any());
    }
}