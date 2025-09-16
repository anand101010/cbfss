package com.incede.nbfc.core.monolith.customer.service;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerAddress;
import com.incede.nbfc.core.monolith.customer.domain.entity.Nominee;
import com.incede.nbfc.core.monolith.customer.dto.NomineeAddressDto;
import com.incede.nbfc.core.monolith.customer.dto.NomineeDetailsRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.NomineeDetailsResponseDto;
import com.incede.nbfc.core.monolith.customer.dto.NomineeDto;
import com.incede.nbfc.core.monolith.customer.mapper.NomineeDetailsMapper;
import com.incede.nbfc.core.monolith.customer.repository.CustomerAddressRepository;
import com.incede.nbfc.core.monolith.customer.repository.CustomerRepository;
import com.incede.nbfc.core.monolith.customer.repository.NomineeRepository;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ResourceNotFoundException;
import com.incede.nbfc.core.monolith.masterdata.repository.AddressTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NomineeDetailsServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private NomineeRepository nomineeRepository;

    @Mock
    private NomineeDetailsMapper nomineeMapper;

    @InjectMocks
    private NomineeDetailsService service;

    private UUID customerId;
    private UUID nomineeId;
    private Customer customer;
    private Nominee nominee;
    private NomineeDetailsRequestDto requestDto;
    private NomineeAddressDto addressDto;
    private NomineeDto nomineeDto;

    @BeforeEach
    void setUp() {
        customerId = UUID.randomUUID();
        nomineeId = UUID.randomUUID();

        // Customer setup
        customer = new Customer();
        customer.setIdentity(customerId);
        customer.setCustomerCode("CUST001");

        // Nominee entity setup
        nominee = new Nominee();
        nominee.setNomineeId(1);
        nominee.setIdentity(nomineeId);
        nominee.setCustomer(customer);
        nominee.setPercentageShare(BigDecimal.valueOf(50));

        // NomineeAddressDto setup
        addressDto = NomineeAddressDto.builder()
                .addressTypeId(1)
                .doorNumber("123")
                .addressLine1("MG Road")
                .landmark("City Mall")
                .placeName("Mumbai")
                .cityId(101)
                .districtId(10)
                .stateId(1)
                .countryId(1)
                .pincode(676528)
                .postOfficeId(25)
                .latitude(new BigDecimal("12.971598"))
                .longitude(new BigDecimal("77.594566"))
                .digipin("DIG210003")
                .build();

        // NomineeDetailsRequestDto setup
        requestDto = new NomineeDetailsRequestDto();
        requestDto.setFullName("Anu");
        requestDto.setRelationship(2);
        requestDto.setDob(LocalDate.of(1995, 5, 15));
        requestDto.setContactNumber("8089883076");
        requestDto.setPercentageShare(BigDecimal.valueOf(1.00));
        requestDto.setIsMinor(false);
        requestDto.setGuardianName("Alan");
        requestDto.setGuardianDob(LocalDate.of(1995, 5, 15));
        requestDto.setGuardianEmail("alanajoy@gmail.com");
        requestDto.setGuardianContactNumber("9192345678");

        requestDto.setIsSameAddress(false);
        requestDto.setAddressTypeId(1);
        requestDto.setDoorNumber("123");
        requestDto.setAddressLine1("MG Road");
        requestDto.setLandmark("City Mall");
        requestDto.setPlaceName("Mumbai");
        requestDto.setCityId(101);
        requestDto.setDistrictId(10);
        requestDto.setStateId(1);
        requestDto.setCountryId(1);
        requestDto.setPincode(676528);
        requestDto.setPostOfficeId(25);
        requestDto.setLatitude(new BigDecimal("12.971598"));
        requestDto.setLongitude(new BigDecimal("77.594566"));
        requestDto.setDigipin("DIG210003");


        // NomineeDto setup for response mapping
        nomineeDto = NomineeDto.builder()
                .nomineeIdentity(nomineeId)
                .fullName("Anu")
                .relationship(2)
                .percentageShare(BigDecimal.valueOf(1.00))
                .isMinor(false)
                .isSameAddress(false)
                .doorNumber("123")
                .addressLine1("MG Road")
                .landmark("City Mall")
                .placeName("Mumbai")
                .cityId(101)
                .districtId(10)
                .stateId(1)
                .countryId(1)
                .pincode(676528)
                .postOfficeId(25)
                .latitude(new BigDecimal("12.971598"))
                .longitude(new BigDecimal("77.594566"))
                .digipin("DIG210003")
                .build();
    }

    @Test
    void testCreateNominee_Success() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(nomineeRepository.existsByCustomerAndFullNameAndRelationshipAndIsDelFalse(customer, requestDto.getFullName(), requestDto.getRelationship()))
                .thenReturn(false);
        when(nomineeRepository.findByCustomerIdentity(customerId)).thenReturn(List.of());
        when(nomineeMapper.toEntity(customer, requestDto, addressDto)).thenReturn(nominee);
        when(nomineeRepository.save(nominee)).thenReturn(nominee);
        when(nomineeMapper.toNomineeDto(nominee)).thenReturn(nomineeDto);
        when(nomineeMapper.toResponse(eq(customer), anyString(), ArgumentMatchers.<NomineeDto>anyList()))
                .thenReturn(NomineeDetailsResponseDto.builder().identity(customerId).customerCode("CUST001").status("SUCCESS").build());

        NomineeDetailsResponseDto response = service.createNominee(customerId, requestDto);

        assertNotNull(response);
        assertEquals(customerId, response.getIdentity());
        verify(nomineeRepository, times(1)).save(nominee);
    }

    @Test
    void testCreateNominee_ThrowsBusinessException_WhenNomineeExists() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(nomineeRepository.existsByCustomerAndFullNameAndRelationshipAndIsDelFalse(customer, requestDto.getFullName(), requestDto.getRelationship()))
                .thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> service.createNominee(customerId, requestDto));

        assertEquals("A nominee with the same name and relationship already exists.", exception.getMessage());
    }

    @Test
    void testUpdateNominee_Success() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(nomineeRepository.findByIdentityAndCustomer(nomineeId, customer)).thenReturn(Optional.of(nominee));
        when(nomineeRepository.findByCustomerIdentity(customerId)).thenReturn(List.of(nominee));
        when(nomineeMapper.toNomineeDto(nominee)).thenReturn(nomineeDto);
        when(nomineeMapper.toResponse(eq(customer), anyString(), ArgumentMatchers.<NomineeDto>anyList()))
                .thenReturn(NomineeDetailsResponseDto.builder().identity(customerId).customerCode("CUST001").status("SUCCESS").build());
        when(nomineeRepository.save(nominee)).thenReturn(nominee);

        NomineeDetailsResponseDto response = service.updateNominee(customerId, nomineeId, requestDto);

        assertNotNull(response);
        assertEquals(customerId, response.getIdentity());
        verify(nomineeRepository, times(1)).save(nominee);
    }

    @Test
    void testDeleteNominee_Success() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(nomineeRepository.findByIdentityAndCustomer(nomineeId, customer)).thenReturn(Optional.of(nominee));
        when(nomineeRepository.save(nominee)).thenReturn(nominee);

        service.deleteNominee(customerId, nomineeId);

        assertTrue(nominee.getIsDel());
        verify(nomineeRepository, times(1)).save(nominee);
    }

    @Test
    void testGetNomineesByCustomerIdentity_Success() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(nomineeRepository.findByCustomerIdentityAndIsDelFalse(customerId)).thenReturn(List.of(nominee));
        when(nomineeMapper.toNomineeDto(nominee)).thenReturn(nomineeDto);
        when(nomineeMapper.toResponse(eq(customer), anyString(), ArgumentMatchers.<NomineeDto>anyList()))
                .thenReturn(NomineeDetailsResponseDto.builder().identity(customerId).customerCode("CUST001").status("SUCCESS").build());

        NomineeDetailsResponseDto response = service.getNomineesByCustomerIdentity(customerId.toString());

        assertNotNull(response);
        assertEquals(customerId, response.getIdentity());
    }

    @Test
    void testValidateMinorNominee_ThrowsException() {
        requestDto.setIsMinor(true);
        requestDto.setGuardianName(null);

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> service.createNominee(customerId, requestDto));

        assertTrue(exception.getMessage().contains("Guardian's name is required for minor nominees."));
    }
}
