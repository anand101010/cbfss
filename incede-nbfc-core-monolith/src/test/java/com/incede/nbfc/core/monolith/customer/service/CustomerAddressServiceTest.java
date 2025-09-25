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
import com.incede.nbfc.core.monolith.exception.ResourceNotFoundException;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.AddressProofType;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.AddressType;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.PostOffices;
import com.incede.nbfc.core.monolith.masterdata.repository.AddressProofTypeRepository;
import com.incede.nbfc.core.monolith.masterdata.repository.AddressTypeRepository;
import com.incede.nbfc.core.monolith.masterdata.repository.PostOfficesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerAddressServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerAddressRepository addressRepository;

    @Mock
    private CustomerAddressMapper addressMapper;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AddressTypeRepository addressTypeRepository;

    @Mock
    private PostOfficesRepository postOfficesRepository;

    @Mock
    private AddressProofTypeRepository addressProofTypeRepository;

    @InjectMocks
    private CustomerAddressService customerAddressService;

    private UUID customerId;
    private UUID addressId;
    private Customer customer;
    private CustomerAddress address;
    private CustomerAddressRequestDto requestDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customerId = UUID.randomUUID();
        addressId = UUID.randomUUID();

        customer = new Customer();
        customer.setCustomerId(1);
        customer.setCustomerCode("CUST001");

        address = new CustomerAddress();
        address.setAddressId(1);
        address.setCustomer(customer);

        requestDto = new CustomerAddressRequestDto();
        requestDto.setAddressType(UUID.randomUUID());
        requestDto.setPostOfficeId(UUID.randomUUID());
        requestDto.setAddressProofType(UUID.randomUUID());
        requestDto.setIsSameAsPermanent(false);

        requestDto.setDoorNumber("123");
        requestDto.setAddressLine1("Main Street");
        requestDto.setPlaceName("Some Place");
        requestDto.setDistrict("Central District");
        requestDto.setCity("Metropolis");
        requestDto.setState("StateName");
        requestDto.setCountry("CountryName");
    }

    @Test
    void testCreateAddress_Success() throws JsonProcessingException {
        String jsonRequest = "{}";

        when(objectMapper.readValue(jsonRequest, CustomerAddressRequestDto.class)).thenReturn(requestDto);
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(addressMapper.toEntity(customer, requestDto)).thenReturn(address);
        when(addressTypeRepository.findByIdentity(requestDto.getAddressType())).thenReturn(Optional.of(new AddressType()));
        when(postOfficesRepository.findByIdentity(requestDto.getPostOfficeId())).thenReturn(Optional.of(new PostOffices()));
        when(addressProofTypeRepository.findByIdentity(requestDto.getAddressProofType())).thenReturn(Optional.of(new AddressProofType()));
        when(addressRepository.save(address)).thenReturn(address);
        when(addressMapper.toAddressDetail(address)).thenReturn(new CustomerAddressResponseDto.AddressDetail());
        when(addressMapper.toResponse(eq(customer), anyString(), anyList())).thenReturn(new CustomerAddressResponseDto());

        CustomerAddressResponseDto response = customerAddressService.createAddress(customerId, jsonRequest, null);

        assertNotNull(response);
        verify(addressRepository).save(address);
    }

    @Test
    void testCreateAddress_DocumentRequired() throws JsonProcessingException {
        String jsonRequest = "{}";
        requestDto.setIsSameAsPermanent(true);

        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(true);

        when(objectMapper.readValue(jsonRequest, CustomerAddressRequestDto.class)).thenReturn(requestDto);
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(addressMapper.toEntity(customer, requestDto)).thenReturn(new CustomerAddress()); // <- important
        when(addressTypeRepository.findByIdentity(requestDto.getAddressType())).thenReturn(Optional.of(new AddressType()));
        when(postOfficesRepository.findByIdentity(requestDto.getPostOfficeId())).thenReturn(Optional.of(new PostOffices()));
        when(addressProofTypeRepository.findByIdentity(requestDto.getAddressProofType())).thenReturn(Optional.of(new AddressProofType()));

        BusinessException exception = assertThrows(BusinessException.class, () ->
                customerAddressService.createAddress(customerId, jsonRequest, file));

        assertTrue(exception.getMessage().contains("Document file must be provided"));
    }


    @Test
    void testCreateAddress_InvalidCustomer() throws JsonProcessingException {
        String jsonRequest = "{}";
        when(objectMapper.readValue(jsonRequest, CustomerAddressRequestDto.class)).thenReturn(requestDto);
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                customerAddressService.createAddress(customerId, jsonRequest, null));
        assertTrue(exception.getMessage().contains(CommonConstants.ENTITY_CUSTOMER));
    }

    @Test
    void testCreateAddress_InvalidAddressType() throws JsonProcessingException {
        String jsonRequest = "{}";
        when(objectMapper.readValue(jsonRequest, CustomerAddressRequestDto.class)).thenReturn(requestDto);
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(addressMapper.toEntity(customer, requestDto)).thenReturn(address);
        when(addressTypeRepository.findByIdentity(requestDto.getAddressType())).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () ->
                customerAddressService.createAddress(customerId, jsonRequest, null));
        assertTrue(exception.getMessage().contains("Invalid Address Type"));
    }

    @Test
    void testUpdateAddress_Success() throws JsonProcessingException {
        String jsonRequest = "{}";

        when(objectMapper.readValue(jsonRequest, CustomerAddressRequestDto.class)).thenReturn(requestDto);
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(addressRepository.findByIdentity(addressId)).thenReturn(Optional.of(address));
        when(addressMapper.toAddressDetail(address)).thenReturn(new CustomerAddressResponseDto.AddressDetail());
        when(addressMapper.toResponse(eq(customer), anyString(), anyList())).thenReturn(new CustomerAddressResponseDto());
        when(addressTypeRepository.findByIdentity(requestDto.getAddressType())).thenReturn(Optional.of(new AddressType()));
        when(postOfficesRepository.findByIdentity(requestDto.getPostOfficeId())).thenReturn(Optional.of(new PostOffices()));
        when(addressProofTypeRepository.findByIdentity(requestDto.getAddressProofType())).thenReturn(Optional.of(new AddressProofType()));
        when(addressRepository.save(address)).thenReturn(address);

        CustomerAddressResponseDto response = customerAddressService.updateAddress(customerId, addressId, jsonRequest, null);

        assertNotNull(response);
        verify(addressRepository).save(address);
    }

    @Test
    void testUpdateAddress_InvalidCustomer() throws JsonProcessingException {
        String jsonRequest = "{}";
        when(objectMapper.readValue(jsonRequest, CustomerAddressRequestDto.class)).thenReturn(requestDto);
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                customerAddressService.updateAddress(customerId, addressId, jsonRequest, null));
        assertTrue(exception.getMessage().contains(CommonConstants.ENTITY_CUSTOMER));
    }

    @Test
    void testUpdateAddress_InvalidAddress() throws JsonProcessingException {
        String jsonRequest = "{}";
        when(objectMapper.readValue(jsonRequest, CustomerAddressRequestDto.class)).thenReturn(requestDto);
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(addressRepository.findByIdentity(addressId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                customerAddressService.updateAddress(customerId, addressId, jsonRequest, null));
        assertTrue(exception.getMessage().contains(CommonConstants.ENTITY_ADDRESS));
    }

    @Test
    void testDeleteAddress_Success() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(addressRepository.findByIdentity(addressId)).thenReturn(Optional.of(address));

        customerAddressService.deleteAddress(customerId, addressId);

        assertTrue(address.getIsDel());
        assertFalse(address.getIsActive());
        verify(addressRepository).save(address);
    }

    @Test
    void testDeleteAddress_InvalidCustomer() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                customerAddressService.deleteAddress(customerId, addressId));
        assertTrue(exception.getMessage().contains(CommonConstants.ENTITY_CUSTOMER));
    }

    @Test
    void testDeleteAddress_InvalidAddress() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(addressRepository.findByIdentity(addressId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                customerAddressService.deleteAddress(customerId, addressId));
        assertTrue(exception.getMessage().contains(CommonConstants.ENTITY_ADDRESS));
    }

    @Test
    void testGetActiveAddressesByCustomerIdentity_Success() {
        when(customerRepository.findByIdentityAndIsDelFalse(customerId)).thenReturn(Optional.of(customer));
        when(addressRepository.findByCustomerAndIsDelFalse(customer)).thenReturn(List.of(address));
        when(addressMapper.toAddressDetail(address)).thenReturn(new CustomerAddressResponseDto.AddressDetail());
        when(addressMapper.toResponse(eq(customer), anyString(), anyList())).thenReturn(new CustomerAddressResponseDto());

        CustomerAddressResponseDto response = customerAddressService.getActiveAddressesByCustomerIdentity(customerId);

        assertNotNull(response);
        verify(addressRepository).findByCustomerAndIsDelFalse(customer);
    }

    @Test
    void testGetActiveAddressesByCustomerIdentity_NotFound() {
        when(customerRepository.findByIdentityAndIsDelFalse(customerId)).thenReturn(Optional.of(customer));
        when(addressRepository.findByCustomerAndIsDelFalse(customer)).thenReturn(List.of());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                customerAddressService.getActiveAddressesByCustomerIdentity(customerId));

        assertTrue(exception.getMessage().contains("No active addresses found"));
    }

    @Test
    void testCreateAddress_WithDocumentUpload() throws Exception {
        String jsonRequest = "{}";

        requestDto.setIsSameAsPermanent(true);
        requestDto.setDoorNumber("123");
        requestDto.setAddressLine1("Main Street");
        requestDto.setPlaceName("Some Place");
        requestDto.setDistrict("Central District");
        requestDto.setCity("Metropolis");
        requestDto.setState("StateName");
        requestDto.setCountry("CountryName");
        requestDto.setAddressType(UUID.randomUUID());
        requestDto.setPostOfficeId(UUID.randomUUID());
        requestDto.setAddressProofType(UUID.randomUUID());

        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);

        CustomerAddress address = new CustomerAddress();
        when(objectMapper.readValue(jsonRequest, CustomerAddressRequestDto.class)).thenReturn(requestDto);
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(addressMapper.toEntity(customer, requestDto)).thenReturn(address);
        when(addressTypeRepository.findByIdentity(requestDto.getAddressType())).thenReturn(Optional.of(new AddressType()));
        when(postOfficesRepository.findByIdentity(requestDto.getPostOfficeId())).thenReturn(Optional.of(new PostOffices()));
        when(addressProofTypeRepository.findByIdentity(requestDto.getAddressProofType())).thenReturn(Optional.of(new AddressProofType()));
        when(addressRepository.save(address)).thenReturn(address);
        when(addressMapper.toAddressDetail(address)).thenReturn(new CustomerAddressResponseDto.AddressDetail());
        when(addressMapper.toResponse(eq(customer), anyString(), anyList())).thenReturn(new CustomerAddressResponseDto());

        CustomerAddressResponseDto response = customerAddressService.createAddress(customerId, jsonRequest, file);

        assertNotNull(response);
        assertNotNull(address.getDocumentRefId(), "DocumentRefId should be set when file is provided");
        verify(addressRepository).save(address);
    }

}
