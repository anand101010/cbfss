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
        requestDto.setIsSameAsPermanent(false); // By default test the document-required scenario
        requestDto.setDoorNumber("123");
        requestDto.setAddressLine1("Main Street");
        requestDto.setPlaceName("Some Place");
        requestDto.setDistrict("Central District");
        requestDto.setCity("Metropolis");
        requestDto.setState("StateName");
        requestDto.setCountry("CountryName");
    }

    @Test
    void testCreateAddress_SuccessWithDocument() throws Exception {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);

        CustomerAddress newAddress = new CustomerAddress();

        when(objectMapper.readValue(anyString(), eq(CustomerAddressRequestDto.class))).thenReturn(requestDto);
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(addressMapper.toEntity(customer, requestDto)).thenReturn(newAddress);
        when(addressTypeRepository.findByIdentity(requestDto.getAddressType())).thenReturn(Optional.of(new AddressType()));
        when(postOfficesRepository.findByIdentity(requestDto.getPostOfficeId())).thenReturn(Optional.of(new PostOffices()));
        when(addressProofTypeRepository.findByIdentity(requestDto.getAddressProofType())).thenReturn(Optional.of(new AddressProofType()));
        when(addressRepository.save(newAddress)).thenReturn(newAddress);
        when(addressMapper.toAddressDetail(newAddress)).thenReturn(new CustomerAddressResponseDto.AddressDetail());
        when(addressMapper.toResponse(eq(customer), anyString(), anyList())).thenReturn(new CustomerAddressResponseDto());

        CustomerAddressResponseDto response = customerAddressService.createAddress(customerId, "{}", file);

        assertNotNull(response);
        assertNotNull(newAddress.getDocumentRefId(), "DocumentRefId should be set when file is provided");
        verify(addressRepository).save(newAddress);
    }

    @Test
    void testCreateAddress_SuccessWithoutDocumentWhenSameAsPermanent() throws Exception {
        requestDto.setIsSameAsPermanent(true); // Document optional
        MultipartFile file = null;

        CustomerAddress newAddress = new CustomerAddress();

        when(objectMapper.readValue(anyString(), eq(CustomerAddressRequestDto.class))).thenReturn(requestDto);
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(addressMapper.toEntity(customer, requestDto)).thenReturn(newAddress);
        when(addressTypeRepository.findByIdentity(requestDto.getAddressType())).thenReturn(Optional.of(new AddressType()));
        when(postOfficesRepository.findByIdentity(requestDto.getPostOfficeId())).thenReturn(Optional.of(new PostOffices()));
        when(addressProofTypeRepository.findByIdentity(requestDto.getAddressProofType())).thenReturn(Optional.of(new AddressProofType()));
        when(addressRepository.save(newAddress)).thenReturn(newAddress);
        when(addressMapper.toAddressDetail(newAddress)).thenReturn(new CustomerAddressResponseDto.AddressDetail());
        when(addressMapper.toResponse(eq(customer), anyString(), anyList())).thenReturn(new CustomerAddressResponseDto());

        CustomerAddressResponseDto response = customerAddressService.createAddress(customerId, "{}", file);

        assertNotNull(response);
        assertNull(newAddress.getDocumentRefId(), "DocumentRefId should be null when sameAsPermanent=true");
        verify(addressRepository).save(newAddress);
    }

    @Test
    void testCreateAddress_ThrowsBusinessException_WhenDocumentMissing() throws JsonProcessingException {
        requestDto.setIsSameAsPermanent(false);
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(true);

        when(objectMapper.readValue(anyString(), eq(CustomerAddressRequestDto.class))).thenReturn(requestDto);
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(addressMapper.toEntity(customer, requestDto)).thenReturn(address);
        when(addressTypeRepository.findByIdentity(requestDto.getAddressType())).thenReturn(Optional.of(new AddressType()));
        when(postOfficesRepository.findByIdentity(requestDto.getPostOfficeId())).thenReturn(Optional.of(new PostOffices()));
        when(addressProofTypeRepository.findByIdentity(requestDto.getAddressProofType())).thenReturn(Optional.of(new AddressProofType()));

        BusinessException ex = assertThrows(BusinessException.class, () ->
                customerAddressService.createAddress(customerId, "{}", file));

        assertEquals("Document file must be provided", ex.getMessage());
    }

    @Test
    void testUpdateAddress_SuccessWithDocument() throws Exception {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);

        when(objectMapper.readValue(anyString(), eq(CustomerAddressRequestDto.class))).thenReturn(requestDto);
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(addressRepository.findByIdentity(addressId)).thenReturn(Optional.of(address));
        when(addressTypeRepository.findByIdentity(requestDto.getAddressType())).thenReturn(Optional.of(new AddressType()));
        when(postOfficesRepository.findByIdentity(requestDto.getPostOfficeId())).thenReturn(Optional.of(new PostOffices()));
        when(addressProofTypeRepository.findByIdentity(requestDto.getAddressProofType())).thenReturn(Optional.of(new AddressProofType()));
        when(addressRepository.save(address)).thenReturn(address);
        when(addressMapper.toAddressDetail(address)).thenReturn(new CustomerAddressResponseDto.AddressDetail());
        when(addressMapper.toResponse(eq(customer), anyString(), anyList())).thenReturn(new CustomerAddressResponseDto());

        CustomerAddressResponseDto response = customerAddressService.updateAddress(customerId, addressId, "{}", file);

        assertNotNull(response);
        assertNotNull(address.getDocumentRefId());
        verify(addressRepository).save(address);
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
    void testGetActiveAddressesByCustomerIdentity_Success() {
        when(customerRepository.findByIdentityAndIsDelFalse(customerId)).thenReturn(Optional.of(customer));
        when(addressRepository.findByCustomerAndIsDelFalse(customer)).thenReturn(List.of(address));
        when(addressMapper.toAddressDetail(address)).thenReturn(new CustomerAddressResponseDto.AddressDetail());
        when(addressMapper.toResponse(eq(customer), anyString(), anyList())).thenReturn(new CustomerAddressResponseDto());

        CustomerAddressResponseDto response = customerAddressService.getActiveAddressesByCustomerIdentity(customerId);

        assertNotNull(response);
        verify(addressRepository).findByCustomerAndIsDelFalse(customer);
        verify(addressMapper).toResponse(eq(customer), anyString(), anyList());
    }

    @Test
    void testGetActiveAddressesByCustomerIdentity_NotFound() {
        when(customerRepository.findByIdentityAndIsDelFalse(customerId)).thenReturn(Optional.of(customer));
        when(addressRepository.findByCustomerAndIsDelFalse(customer)).thenReturn(List.of());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> customerAddressService.getActiveAddressesByCustomerIdentity(customerId));

        assertTrue(ex.getMessage().contains("No active addresses found"));
    }
}
