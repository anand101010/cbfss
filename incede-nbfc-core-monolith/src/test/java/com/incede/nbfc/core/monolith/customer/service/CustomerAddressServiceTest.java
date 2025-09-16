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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerAddressServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerAddressRepository addressRepository;

    @Mock
    private CustomerAddressMapper addressMapper;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private CustomerAddressService service;

    private UUID customerId;
    private UUID addressId;
    private Customer customer;
    private CustomerAddress address;
    private CustomerAddressRequestDto requestDto;
    private CustomerAddressResponseDto responseDto;
    private String requestJson;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        customerId = UUID.randomUUID();
        addressId = UUID.randomUUID();

        customer = new Customer();
        customer.setCustomerId(1);

        address = new CustomerAddress();
        address.setCustomer(customer);

        requestDto = CustomerAddressRequestDto.builder()
                .addressTypeId(1)
                .doorNumber("12C")
                .addressLine1("MG Road")
                .isSameAsPermanent(false)
                .build();

        requestJson = "{}"; // objectMapper is mocked
        responseDto = new CustomerAddressResponseDto();

        // Lenient stubbing
        lenient().when(objectMapper.readValue(requestJson, CustomerAddressRequestDto.class)).thenReturn(requestDto);
    }


    @Test
    void testCreateAddress_Success() throws JsonProcessingException {
        MultipartFile file = null;

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(addressMapper.toEntity(customer, requestDto)).thenReturn(address);
        when(addressRepository.save(address)).thenReturn(address);
        when(addressMapper.toAddressDetail(address)).thenReturn(new CustomerAddressResponseDto.AddressDetail());
        when(addressMapper.toResponse(eq(customer), anyString(), anyList())).thenReturn(responseDto);

        CustomerAddressResponseDto result = service.createAddress(customerId, requestJson, file);

        assertEquals(responseDto, result);
        verify(addressRepository, times(1)).save(address);
    }

    @Test
    void testCreateAddress_WithIsSameAsPermanentAndFile() throws JsonProcessingException {
        MockMultipartFile file = new MockMultipartFile("file", "doc.txt", "text/plain", "dummy".getBytes());
        requestDto.setIsSameAsPermanent(true);

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(addressMapper.toEntity(customer, requestDto)).thenReturn(address);
        when(addressRepository.save(address)).thenReturn(address);
        when(addressMapper.toAddressDetail(address)).thenReturn(new CustomerAddressResponseDto.AddressDetail());
        when(addressMapper.toResponse(eq(customer), anyString(), anyList())).thenReturn(responseDto);

        CustomerAddressResponseDto result = service.createAddress(customerId, requestJson, file);

        assertEquals(responseDto, result);
        assertNotNull(address.getDocumentRefId());
        verify(addressRepository, times(1)).save(address);
    }

    @Test
    void testCreateAddress_ThrowsBusinessExceptionWhenFileMissing() {
        requestDto.setIsSameAsPermanent(true);
        MultipartFile file = null;

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));

        BusinessException exception = assertThrows(BusinessException.class, () ->
                service.createAddress(customerId, requestJson, file));

        assertEquals("Document file must be provided if 'isSameAsPermanent' is true", exception.getMessage());
    }




    @Test
    void testGetActiveAddresses_Success() {
        List<CustomerAddress> addresses = List.of(address);

        when(customerRepository.findByIdentityAndIsDelFalse(customerId)).thenReturn(Optional.of(customer));
        when(addressRepository.findByCustomerAndIsDelFalse(customer)).thenReturn(addresses);
        when(addressMapper.toAddressDetail(address)).thenReturn(new CustomerAddressResponseDto.AddressDetail());
        when(addressMapper.toResponse(eq(customer), anyString(), anyList())).thenReturn(responseDto);

        CustomerAddressResponseDto result = service.getActiveAddressesByCustomerIdentity(customerId);

        assertEquals(responseDto, result);
    }

    @Test
    void testGetActiveAddresses_ThrowsResourceNotFound() {
        when(customerRepository.findByIdentityAndIsDelFalse(customerId)).thenReturn(Optional.of(customer));
        when(addressRepository.findByCustomerAndIsDelFalse(customer)).thenReturn(List.of());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> service.getActiveAddressesByCustomerIdentity(customerId));

        assertTrue(exception.getMessage().contains("No active addresses found for customer identity"));
    }
}
