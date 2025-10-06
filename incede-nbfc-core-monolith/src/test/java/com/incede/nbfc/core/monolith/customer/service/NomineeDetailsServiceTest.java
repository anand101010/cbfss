package com.incede.nbfc.core.monolith.customer.service;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.Nominee;
import com.incede.nbfc.core.monolith.customer.dto.NomineeDetailsRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.NomineeDetailsResponseDto;
import com.incede.nbfc.core.monolith.customer.dto.NomineeDto;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NomineeDetailsServiceTest {

    @InjectMocks
    private NomineeDetailsService nomineeDetailsService;

    @Mock private CustomerRepository customerRepository;
    @Mock private CustomerAddressRepository addressRepository;
    @Mock private NomineeRepository nomineeRepository;
    @Mock private AddressTypeRepository addressTypeRepository;
    @Mock private NomineeDetailsMapper nomineeMapper;
    @Mock private RelationshipsRepository relationshipsRepository;
    @Mock private PostOfficesRepository postOfficesRepository;

    private UUID customerId;
    private UUID nomineeId;
    private Customer customer;
    private NomineeDetailsRequestDto dto;
    private Relationships relationship;
    private AddressType permanentAddressType;
    private PostOffices postOffice;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customerId = UUID.randomUUID();
        nomineeId = UUID.randomUUID();

        customer = new Customer();
        customer.setIdentity(customerId);

        relationship = new Relationships();
        relationship.setIdentity(UUID.randomUUID());

        permanentAddressType = new AddressType();
        permanentAddressType.setIdentity(UUID.randomUUID());
        permanentAddressType.setAddressTypeName(CommonConstants.ADDRESS_TYPE_PERMANENT);

        postOffice = new PostOffices();
        postOffice.setIdentity(UUID.randomUUID());

        dto = new NomineeDetailsRequestDto();
        dto.setFullName("John Doe");
        dto.setRelationship(relationship.getIdentity());
        dto.setIsMinor(false);
        dto.setIsSameAddress(true);
        dto.setPercentageShare(BigDecimal.valueOf(50));
    }

    @Test
    void testCreateNominee_Success_DifferentAddress() {
        dto.setIsSameAddress(false);
        dto.setAddressTypeId(permanentAddressType.getIdentity());
        dto.setPostOfficeId(postOffice.getIdentity());
        dto.setDoorNumber("12A");
        dto.setAddressLine1("Street 1");
        dto.setCity("City");
        dto.setDistrict("District");
        dto.setState("State");
        dto.setCountry("Country");
        dto.setPincode("123456");

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(relationshipsRepository.findByIdentity(dto.getRelationship())).thenReturn(Optional.of(relationship));
        when(nomineeRepository.existsByCustomerAndFullNameAndRelationshipAndIsDelFalse(any(), any(), any())).thenReturn(false);
        when(nomineeRepository.findByCustomerIdentityAndIsDelFalse(customerId)).thenReturn(List.of());
        when(addressTypeRepository.findByIdentity(dto.getAddressTypeId())).thenReturn(Optional.of(permanentAddressType));
        when(postOfficesRepository.findByIdentity(dto.getPostOfficeId())).thenReturn(Optional.of(postOffice));
        when(nomineeMapper.getCreatedBy()).thenReturn(1);
        when(nomineeRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(nomineeMapper.toNomineeDto(any())).thenReturn(new NomineeDto());
        when(nomineeMapper.toResponse(any(), anyString(), anyList())).thenReturn(new NomineeDetailsResponseDto());

        NomineeDetailsResponseDto response = nomineeDetailsService.createNominee(customerId, dto);

        assertNotNull(response);
    }

    @Test
    void testCreateNominee_DuplicateNominee() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(relationshipsRepository.findByIdentity(dto.getRelationship())).thenReturn(Optional.of(relationship));
        when(nomineeRepository.existsByCustomerAndFullNameAndRelationshipAndIsDelFalse(any(), any(), any())).thenReturn(true);

        assertThrows(BusinessException.class, () -> nomineeDetailsService.createNominee(customerId, dto));
    }

    @Test
    void testCreateNominee_PercentageShareExceeds() {
        Nominee existingNominee = new Nominee();
        existingNominee.setPercentageShare(BigDecimal.valueOf(60));

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(relationshipsRepository.findByIdentity(dto.getRelationship())).thenReturn(Optional.of(relationship));
        when(nomineeRepository.existsByCustomerAndFullNameAndRelationshipAndIsDelFalse(any(), any(), any())).thenReturn(false);
        when(nomineeRepository.findByCustomerIdentityAndIsDelFalse(customerId)).thenReturn(List.of(existingNominee));

        assertThrows(BusinessException.class, () -> nomineeDetailsService.createNominee(customerId, dto));
    }

    // Minor nominee tests with guardian validations
    @Test
    void testCreateNominee_MinorWithoutGuardianDetails_AllMissing() {
        dto.setIsMinor(true);
        dto.setGuardianName(null);
        dto.setGuardianDob(null);
        dto.setGuardianEmail(null);
        dto.setGuardianContactNumber(null);

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> nomineeDetailsService.createNominee(customerId, dto));

        assertEquals("Guardian's name is required for minor nominees.", exception.getMessage());
    }

    @Test
    void testCreateNominee_MinorWithoutGuardianDob() {
        dto.setIsMinor(true);
        dto.setGuardianName("Guardian Name");
        dto.setGuardianDob(null);
        dto.setGuardianEmail("guardian@example.com");
        dto.setGuardianContactNumber("9876543210");

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> nomineeDetailsService.createNominee(customerId, dto));

        assertEquals("Guardian's date of birth is required for minor nominees.", exception.getMessage());
    }

    @Test
    void testCreateNominee_MinorWithoutGuardianEmail() {
        dto.setIsMinor(true);
        dto.setGuardianName("Guardian Name");
        dto.setGuardianDob(LocalDate.of(1980, 1, 1));
        dto.setGuardianEmail(null);
        dto.setGuardianContactNumber("9876543210");

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> nomineeDetailsService.createNominee(customerId, dto));

        assertEquals("Guardian's email is required for minor nominees.", exception.getMessage());
    }

    @Test
    void testCreateNominee_MinorWithoutGuardianContactNumber() {
        dto.setIsMinor(true);
        dto.setGuardianName("Guardian Name");
        dto.setGuardianDob(LocalDate.of(1980, 1, 1));
        dto.setGuardianEmail("guardian@example.com");
        dto.setGuardianContactNumber(null);

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> nomineeDetailsService.createNominee(customerId, dto));

        assertEquals("Guardian's contact number is required for minor nominees.", exception.getMessage());
    }

    @Test
    void testUpdateNominee_NotFound() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(nomineeRepository.findByIdentityAndCustomer(nomineeId, customer)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> nomineeDetailsService.updateNominee(customerId, nomineeId, dto));
    }

    @Test
    void testDeleteNominee_Success() {
        Nominee nominee = new Nominee();

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(nomineeRepository.findByIdentityAndCustomer(nomineeId, customer)).thenReturn(Optional.of(nominee));
        when(nomineeMapper.getUpdatedBy()).thenReturn(1);

        nomineeDetailsService.deleteNominee(customerId, nomineeId);

        assertTrue(nominee.getIsDel());
        verify(nomineeRepository).save(any());
    }

    @Test
    void testDeleteNominee_NotFound() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(nomineeRepository.findByIdentityAndCustomer(nomineeId, customer)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> nomineeDetailsService.deleteNominee(customerId, nomineeId));
    }

    @Test
    void testGetNomineesByCustomerIdentity() {
        Nominee nominee = new Nominee();
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(nomineeRepository.findByCustomerIdentityAndIsDelFalse(customerId)).thenReturn(List.of(nominee));
        when(nomineeMapper.toNomineeDto(any())).thenReturn(new NomineeDto());
        when(nomineeMapper.toResponse(any(), anyString(), anyList())).thenReturn(new NomineeDetailsResponseDto());

        NomineeDetailsResponseDto response = nomineeDetailsService.getNomineesByCustomerIdentity(customerId);

        assertNotNull(response);
    }
}
