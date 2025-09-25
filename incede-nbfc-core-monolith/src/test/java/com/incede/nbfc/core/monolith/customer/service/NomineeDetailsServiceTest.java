package com.incede.nbfc.core.monolith.customer.service;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerAddress;
import com.incede.nbfc.core.monolith.customer.domain.entity.Nominee;
import com.incede.nbfc.core.monolith.customer.dto.NomineeDetailsRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.NomineeDetailsResponseDto;
import com.incede.nbfc.core.monolith.customer.mapper.NomineeDetailsMapper;
import com.incede.nbfc.core.monolith.customer.repository.CustomerAddressRepository;
import com.incede.nbfc.core.monolith.customer.repository.CustomerRepository;
import com.incede.nbfc.core.monolith.customer.repository.NomineeRepository;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ResourceNotFoundException;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.AddressType;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.PostOffices;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.Relationships;
import com.incede.nbfc.core.monolith.masterdata.repository.AddressTypeRepository;
import com.incede.nbfc.core.monolith.masterdata.repository.PostOfficesRepository;
import com.incede.nbfc.core.monolith.masterdata.repository.RelationshipsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NomineeDetailsServiceTest {

    @InjectMocks
    private NomineeDetailsService nomineeDetailsService;

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CustomerAddressRepository addressRepository;
    @Mock
    private NomineeRepository nomineeRepository;
    @Mock
    private AddressTypeRepository addressTypeRepository;
    @Mock
    private NomineeDetailsMapper nomineeMapper;
    @Mock
    private RelationshipsRepository relationshipsRepository;
    @Mock
    private PostOfficesRepository postOfficesRepository;

    private UUID customerId;
    private UUID nomineeId;
    private Customer customer;
    private NomineeDetailsRequestDto dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customerId = UUID.randomUUID();
        nomineeId = UUID.randomUUID();

        customer = new Customer();
        customer.setIdentity(customerId);

        dto = new NomineeDetailsRequestDto();
        dto.setAddressTypeId(UUID.randomUUID());
        dto.setFullName("John Doe");
        dto.setRelationship(UUID.randomUUID());
        dto.setIsMinor(false);
        dto.setIsSameAddress(true);
        dto.setPercentageShare(BigDecimal.valueOf(50));
    }

//    @Test
//    void testCreateNominee_Success() {
//        Customer customer = new Customer();
//        customer.setIdentity(customerId);
//
//        AddressType addressType = new AddressType();
//        addressType.setIdentity(UUID.randomUUID());
//        addressType.setAddressTypeName(CommonConstants.ADDRESS_TYPE_PERMANENT);
//        addressType.setIsDel(false); // important
//
//        CustomerAddress address = new CustomerAddress();
//        address.setAddressType(addressType);
//        address.setPostOffice(new PostOffices());
//        address.setCity("City");
//        address.setDistrict("District");
//        address.setState("State");
//        address.setCountry("Country");
//        address.setPincode("123456");
//        address.setIsActive(true); // important
//        address.setIsDel(false);   // important
//
//        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
//        when(addressTypeRepository.findByAddressTypeNameAndIsDelFalse(CommonConstants.ADDRESS_TYPE_PERMANENT))
//                .thenReturn(Optional.of(addressType));
//        when(addressRepository.findByCustomerAndAddressTypeAndIsActiveTrueAndIsDelFalse(customer, addressType))
//                .thenReturn(List.of(address));
//        when(relationshipsRepository.findByIdentity(dto.getRelationship()))
//                .thenReturn(Optional.of(new Relationships() {{
//                    setIdentity(dto.getRelationship());
//                }}));
//        when(nomineeRepository.existsByCustomerAndFullNameAndRelationshipAndIsDelFalse(any(), any(), any()))
//                .thenReturn(false);
//        when(nomineeRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
//        when(nomineeMapper.toNomineeAddressDtoFromCustomerAddress(any()))
//                .thenReturn(new com.incede.nbfc.core.monolith.customer.dto.NomineeAddressDto() {{
//                    setAddressTypeId(UUID.randomUUID());
//                }});
//        when(nomineeMapper.toResponse(any(), anyString(), anyList()))
//                .thenReturn(new NomineeDetailsResponseDto());
//
//        NomineeDetailsResponseDto response = nomineeDetailsService.createNominee(customerId, dto);
//
//        assertNotNull(response);
//    }


    @Test
    void testCreateNominee_CustomerNotFound() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () ->
                nomineeDetailsService.createNominee(customerId, dto));
    }

    @Test
    void testCreateNominee_DuplicateNominee() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(relationshipsRepository.findByIdentity(dto.getRelationship())).thenReturn(Optional.of(new Relationships()));
        when(nomineeRepository.existsByCustomerAndFullNameAndRelationshipAndIsDelFalse(any(), any(), any()))
                .thenReturn(true);

        assertThrows(BusinessException.class, () ->
                nomineeDetailsService.createNominee(customerId, dto));
    }

//    @Test
//    void testUpdateNominee_Success() {
//        Nominee existing = new Nominee();
//        existing.setNomineeId(1);
//        existing.setFullName("Old Name");
//        existing.setRelationship(new Relationships() {{
//            setIdentity(UUID.randomUUID());
//        }});
//
//        AddressType addressType = new AddressType();
//        addressType.setIdentity(UUID.randomUUID());
//        addressType.setAddressTypeName(CommonConstants.ADDRESS_TYPE_PERMANENT);
//
//        CustomerAddress address = new CustomerAddress();
//        address.setAddressType(addressType);
//        address.setPostOffice(new PostOffices());
//        address.setCity("City");
//        address.setDistrict("District");
//        address.setState("State");
//        address.setCountry("Country");
//        address.setPincode("123456");
//
//        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
//        when(nomineeRepository.findByIdentityAndCustomer(nomineeId, customer)).thenReturn(Optional.of(existing));
//        when(relationshipsRepository.findByIdentity(dto.getRelationship())).thenReturn(Optional.of(new Relationships() {{
//            setIdentity(dto.getRelationship());
//        }}));
//        when(nomineeRepository.findByCustomerIdentityAndIsDelFalse(customerId)).thenReturn(List.of(existing));
//        when(addressTypeRepository.findByAddressTypeNameAndIsDelFalse(CommonConstants.ADDRESS_TYPE_PERMANENT))
//                .thenReturn(Optional.of(addressType));
//        when(addressRepository.findByCustomerAndAddressTypeAndIsActiveTrueAndIsDelFalse(any(), any()))
//                .thenReturn(List.of(address));
//        when(nomineeMapper.toNomineeAddressDtoFromCustomerAddress(any()))
//                .thenReturn(new com.incede.nbfc.core.monolith.customer.dto.NomineeAddressDto() {{
//                    setAddressTypeId(UUID.randomUUID());
//                }});
//        when(nomineeMapper.getUpdatedBy()).thenReturn(1);
//        when(nomineeRepository.save(any())).thenReturn(existing);
//        when(nomineeMapper.toNomineeDto(any())).thenReturn(new com.incede.nbfc.core.monolith.customer.dto.NomineeDto());
//        when(nomineeMapper.toResponse(any(), anyString(), anyList())).thenReturn(new NomineeDetailsResponseDto());
//
//        NomineeDetailsResponseDto response = nomineeDetailsService.updateNominee(customerId, nomineeId, dto);
//
//        assertNotNull(response);
//        verify(nomineeRepository).save(any());
//    }

    @Test
    void testUpdateNominee_NotFound() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(nomineeRepository.findByIdentityAndCustomer(nomineeId, customer)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                nomineeDetailsService.updateNominee(customerId, nomineeId, dto));
    }

    @Test
    void testDeleteNominee_Success() {
        Nominee nominee = new Nominee();
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(nomineeRepository.findByIdentityAndCustomer(nomineeId, customer)).thenReturn(Optional.of(nominee));
        when(nomineeMapper.getUpdatedBy()).thenReturn(1);

        nomineeDetailsService.deleteNominee(customerId, nomineeId);

        verify(nomineeRepository).save(any());
        assertTrue(nominee.getIsDel());
    }

    @Test
    void testDeleteNominee_NotFound() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(nomineeRepository.findByIdentityAndCustomer(nomineeId, customer)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                nomineeDetailsService.deleteNominee(customerId, nomineeId));
    }

    @Test
    void testGetNomineesByCustomerIdentity() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(nomineeRepository.findByCustomerIdentityAndIsDelFalse(customerId)).thenReturn(List.of(new Nominee()));
        when(nomineeMapper.toNomineeDto(any())).thenReturn(new com.incede.nbfc.core.monolith.customer.dto.NomineeDto());
        when(nomineeMapper.toResponse(any(), anyString(), anyList())).thenReturn(new NomineeDetailsResponseDto());

        NomineeDetailsResponseDto response = nomineeDetailsService.getNomineesByCustomerIdentity(customerId);

        assertNotNull(response);
    }
}
