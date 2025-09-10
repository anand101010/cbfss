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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

    @InjectMocks
    private CustomerAddressService customerAddressService;

    private Customer customer;
    private CustomerAddressRequestDTO requestDTO;
    private UUID customerIdentity;
    private CustomerAddress address;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customerIdentity = UUID.randomUUID();
        customer = new Customer();
        customer.setCustomerId(1);
        customer.setIdentity(customerIdentity);
        customer.setCustomerCode("CUST001");

        requestDTO = CustomerAddressRequestDTO.builder()
                .addressTypeId(1)
                .doorNumber("123")
                .addressLine1("Street 1")
                .addressLine2("Street 2")
                .landmark("Landmark")
                .placeName("Place")
                .cityId(1)
                .districtId(1)
                .stateId(1)
                .countryId(1)
                .pincode(12345)
                .postOfficeId(101)
                .latitude(null)
                .longitude(null)
                .geoAccuracy(null)
                .addressProofType(1)
                .isActive(true)
                .createdBy(1001)
                .updatedBy(1002)
                .isDel(false)
                .digipin("1234")
                .customerCode("CUST001")
                .build();

        address = new CustomerAddress();
        address.setAddressId(1);
        address.setCustomer(customer);
    }


    @Test
    void testCreateAddressSuccess() {
        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.of(customer));
        when(addressMapper.toEntity(customer, requestDTO)).thenReturn(address);
        when(addressRepository.save(address)).thenReturn(address);
        when(addressMapper.toAddressDetail(address)).thenReturn(CustomerAddressResponseDTO.AddressDetail.builder().build());
        when(addressMapper.toResponse(eq(customer), eq(CommonConstants.CUSTOMER_ADDRESS_STATUS_IN_PROGRESS), anyList()))
                .thenReturn(new CustomerAddressResponseDTO());

        CustomerAddressResponseDTO response = customerAddressService.createAddress(customerIdentity, requestDTO);

        assertNotNull(response);
        verify(addressRepository, times(1)).save(address);
    }

    @Test
    void testCreateAddressCustomerNotFound() {
        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> customerAddressService.createAddress(customerIdentity, requestDTO));

        assertEquals(CommonConstants.ENTITY_CUSTOMER, exception.getResourceType());
        assertEquals(customerIdentity.toString(), exception.getResourceId());
    }


    @Test
    void testUpdateAddressSuccess() {
        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.of(customer));
        when(addressRepository.findByAddressIdAndCustomer_CustomerId(1, customer.getCustomerId())).thenReturn(Optional.of(address));
        when(addressRepository.save(address)).thenReturn(address);
        when(addressMapper.toAddressDetail(address)).thenReturn(CustomerAddressResponseDTO.AddressDetail.builder().build());
        when(addressMapper.toResponse(eq(customer), eq(CommonConstants.CUSTOMER_ADDRESS_STATUS_UPDATED), anyList()))
                .thenReturn(new CustomerAddressResponseDTO());

        CustomerAddressResponseDTO response = customerAddressService.updateAddress(customerIdentity, 1, requestDTO);

        assertNotNull(response);
        verify(addressRepository, times(1)).save(address);
    }

    @Test
    void testUpdateAddressCustomerNotFound() {
        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> customerAddressService.updateAddress(customerIdentity, 1, requestDTO));

        assertEquals(CommonConstants.ENTITY_CUSTOMER, exception.getResourceType());
        assertEquals(customerIdentity.toString(), exception.getResourceId());
    }

    @Test
    void testUpdateAddressAddressNotFound() {
        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.of(customer));
        when(addressRepository.findByAddressIdAndCustomer_CustomerId(1, customer.getCustomerId()))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> customerAddressService.updateAddress(customerIdentity, 1, requestDTO));

        assertEquals(CommonConstants.ENTITY_ADDRESS, exception.getResourceType());
        assertEquals("1", exception.getResourceId());
    }


    @Test
    void testDeleteAddressSuccess() {
        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.of(customer));
        when(addressRepository.findByAddressIdAndCustomer_CustomerId(1, customer.getCustomerId())).thenReturn(Optional.of(address));
        when(addressRepository.save(address)).thenReturn(address);

        customerAddressService.deleteAddress(customerIdentity, 1);

        assertTrue(address.getIsDel());
        assertFalse(address.getIsActive());
        verify(addressRepository, times(1)).save(address);
    }

    @Test
    void testDeleteAddressCustomerNotFound() {
        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> customerAddressService.deleteAddress(customerIdentity, 1));

        assertEquals(CommonConstants.ENTITY_CUSTOMER, exception.getResourceType());
        assertEquals(customerIdentity.toString(), exception.getResourceId());
    }

    @Test
    void testDeleteAddressAddressNotFound() {
        when(customerRepository.findByIdentity(customerIdentity)).thenReturn(Optional.of(customer));
        when(addressRepository.findByAddressIdAndCustomer_CustomerId(1, customer.getCustomerId()))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> customerAddressService.deleteAddress(customerIdentity, 1));

        assertEquals(CommonConstants.ENTITY_ADDRESS, exception.getResourceType());
        assertEquals("1", exception.getResourceId());
    }


    @Test
    void testGetActiveAddressesByCustomerIdentitySuccess() {
        when(customerRepository.findByIdentityAndIsDelFalse(customerIdentity)).thenReturn(Optional.of(customer));
        when(addressRepository.findByCustomerAndIsDelFalse(customer)).thenReturn(List.of(address));
        when(addressMapper.toAddressDetail(address)).thenReturn(CustomerAddressResponseDTO.AddressDetail.builder().build());
        when(addressMapper.toResponse(eq(customer), eq(CommonConstants.CUSTOMER_ADDRESS_STATUS_SUCCESS), anyList()))
                .thenReturn(new CustomerAddressResponseDTO());

        CustomerAddressResponseDTO response = customerAddressService.getActiveAddressesByCustomerIdentity(customerIdentity);

        assertNotNull(response);
        verify(addressRepository, times(1)).findByCustomerAndIsDelFalse(customer);
    }

    @Test
    void testGetActiveAddressesByCustomerIdentityCustomerNotFound() {
        when(customerRepository.findByIdentityAndIsDelFalse(customerIdentity)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> customerAddressService.getActiveAddressesByCustomerIdentity(customerIdentity));

        assertEquals(CommonConstants.ENTITY_CUSTOMER, exception.getResourceType());
        assertEquals(customerIdentity.toString(), exception.getResourceId());
    }

    @Test
    void testGetActiveAddressesByCustomerIdentityNoAddresses() {
        when(customerRepository.findByIdentityAndIsDelFalse(customerIdentity)).thenReturn(Optional.of(customer));
        when(addressRepository.findByCustomerAndIsDelFalse(customer)).thenReturn(List.of());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> customerAddressService.getActiveAddressesByCustomerIdentity(customerIdentity));

        assertEquals(CommonConstants.ENTITY_ADDRESS, exception.getResourceType());
    }
}
 