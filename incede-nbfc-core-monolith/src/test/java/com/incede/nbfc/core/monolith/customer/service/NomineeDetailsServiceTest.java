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
import com.incede.nbfc.core.monolith.masterdata.domain.entity.AddressType;
import com.incede.nbfc.core.monolith.masterdata.repository.AddressTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class NomineeDetailsServiceTest {

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

    @InjectMocks
    private NomineeDetailsService nomineeDetailsService;

    private UUID customerId;
    private Customer customer;
    private UUID nomineeId;

    @BeforeEach
    void setUp() {
        customerId = UUID.randomUUID();
        nomineeId = UUID.randomUUID();

        customer = new Customer();
        customer.setIdentity(customerId);
        customer.setCustomerCode("CUST001");
    }

    // ----- CREATE NOMINEE TESTS -----
    @Test
    void createNominee_success_sameAddressFalse() {
        NomineeDetailsRequestDto dto = new NomineeDetailsRequestDto();
        dto.setFullName("John");
        dto.setRelationship(1);
        dto.setPercentageShare(BigDecimal.valueOf(50));
        dto.setIsMinor(false);
        dto.setIsSameAddress(false);
        dto.setAddressTypeId(1);
        dto.setDoorNumber("101");
        dto.setAddressLine1("Main Street");

        NomineeAddressDto addressDto = new NomineeAddressDto();
        addressDto.setAddressTypeId(1);
        addressDto.setDoorNumber("101");
        addressDto.setAddressLine1("Main Street");

        Nominee nominee = new Nominee();
        nominee.setIdentity(nomineeId);
        nominee.setNomineeId(1);
        nominee.setFullName("John");

        NomineeDto nomineeDto = new NomineeDto();
        nomineeDto.setFullName("John");

        NomineeDetailsResponseDto responseDto = new NomineeDetailsResponseDto();

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(nomineeRepository.existsByCustomerAndFullNameAndRelationshipAndIsDelFalse(customer, "John", 1))
                .thenReturn(false);
        when(nomineeRepository.findByCustomerIdentityAndIsDelFalse(customerId)).thenReturn(List.of());
        when(nomineeMapper.toEntity(customer, dto, addressDto)).thenReturn(nominee);
        when(nomineeRepository.save(nominee)).thenReturn(nominee);
        when(nomineeMapper.toNomineeDto(nominee)).thenReturn(nomineeDto);
        when(nomineeMapper.toResponse(customer, CommonConstants.CUSTOMER_ADDRESS_STATUS_SUCCESS, List.of(nomineeDto)))
                .thenReturn(responseDto);

        NomineeDetailsResponseDto result = nomineeDetailsService.createNominee(customerId, dto);

        assertThat(result).isNotNull();
        verify(nomineeRepository).save(nominee);
        verify(nomineeMapper).toEntity(customer, dto, addressDto);
    }

    @Test
    void createNominee_success_sameAddressTrue() {
        NomineeDetailsRequestDto dto = new NomineeDetailsRequestDto();
        dto.setFullName("Alice");
        dto.setRelationship(2);
        dto.setPercentageShare(BigDecimal.valueOf(50));
        dto.setIsMinor(false);
        dto.setIsSameAddress(true);

        AddressType permanentAddressType = new AddressType();
        permanentAddressType.setAddressTypeId(1);

        CustomerAddress customerAddress = new CustomerAddress();
        customerAddress.setAddressId(100);

        NomineeAddressDto addressDto = new NomineeAddressDto();

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(nomineeRepository.existsByCustomerAndFullNameAndRelationshipAndIsDelFalse(customer, "Alice", 2))
                .thenReturn(false);
        when(nomineeRepository.findByCustomerIdentityAndIsDelFalse(customerId)).thenReturn(List.of());
        when(addressTypeRepository.findByAddressTypeNameAndIsDelFalse(CommonConstants.ADDRESS_TYPE_PERMANENT))
                .thenReturn(Optional.of(permanentAddressType));
        when(addressRepository.findByCustomerAndAddressTypeIdAndIsActiveTrueAndIsDelFalse(customer, 1))
                .thenReturn(List.of(customerAddress));
        when(nomineeMapper.toNomineeAddressDtoFromCustomerAddress(customerAddress)).thenReturn(addressDto);

        Nominee nominee = new Nominee();
        nominee.setIdentity(nomineeId);
        nominee.setNomineeId(1);
        nominee.setFullName("Alice");

        NomineeDto nomineeDto = new NomineeDto();
        nomineeDto.setFullName("Alice");

        NomineeDetailsResponseDto responseDto = new NomineeDetailsResponseDto();
        when(nomineeMapper.toEntity(customer, dto, addressDto)).thenReturn(nominee);
        when(nomineeRepository.save(nominee)).thenReturn(nominee);
        when(nomineeMapper.toNomineeDto(nominee)).thenReturn(nomineeDto);
        when(nomineeMapper.toResponse(customer, CommonConstants.CUSTOMER_ADDRESS_STATUS_SUCCESS, List.of(nomineeDto)))
                .thenReturn(responseDto);

        NomineeDetailsResponseDto result = nomineeDetailsService.createNominee(customerId, dto);
        assertThat(result).isNotNull();
        verify(nomineeMapper).toEntity(customer, dto, addressDto);
        verify(addressRepository).findByCustomerAndAddressTypeIdAndIsActiveTrueAndIsDelFalse(customer, 1);
    }

    @Test
    void createNominee_minorMissingGuardian_throwsException() {
        // Arrange
        NomineeDetailsRequestDto dto = new NomineeDetailsRequestDto();
        dto.setFullName("John");
        dto.setRelationship(1);
        dto.setIsMinor(true); // Minor nominee, but missing guardian fields
        dto.setIsSameAddress(false);
        dto.setAddressTypeId(1);
        dto.setDoorNumber("101");
        dto.setAddressLine1("Main Street");

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));

        BusinessException exception = assertThrows(BusinessException.class, () ->
                nomineeDetailsService.createNominee(customerId, dto));

        assertThat(exception.getMessage())
                .isEqualTo("Guardian's name is required for minor nominees.");
    }

    @Test
    void createNominee_totalShareExceeds100_throwsException() {
        NomineeDetailsRequestDto dto = new NomineeDetailsRequestDto();
        dto.setFullName("John");
        dto.setRelationship(1);
        dto.setPercentageShare(BigDecimal.valueOf(60));
        dto.setIsMinor(false);
        dto.setIsSameAddress(false);
        dto.setAddressTypeId(1);
        dto.setDoorNumber("101");
        dto.setAddressLine1("Main Street");

        Nominee existing = new Nominee();
        existing.setNomineeId(1);
        existing.setPercentageShare(BigDecimal.valueOf(50));

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(nomineeRepository.existsByCustomerAndFullNameAndRelationshipAndIsDelFalse(customer, "John", 1))
                .thenReturn(false);
        when(nomineeRepository.findByCustomerIdentityAndIsDelFalse(customerId)).thenReturn(List.of(existing));

        assertThrows(BusinessException.class, () -> nomineeDetailsService.createNominee(customerId, dto));
    }

    @Test
    void updateNominee_notFound_throwsException() {
        NomineeDetailsRequestDto dto = new NomineeDetailsRequestDto();
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(nomineeRepository.findByIdentityAndCustomer(nomineeId, customer)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> nomineeDetailsService.updateNominee(customerId, nomineeId, dto));
    }

    @Test
    void deleteNominee_success() {
        Nominee nominee = new Nominee();
        nominee.setIdentity(nomineeId);

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(nomineeRepository.findByIdentityAndCustomer(nomineeId, customer)).thenReturn(Optional.of(nominee));

        nomineeDetailsService.deleteNominee(customerId, nomineeId);

        assertThat(nominee.getIsDel()).isTrue();
        verify(nomineeRepository).save(nominee);
    }

    @Test
    void deleteNominee_notFound_throwsException() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(nomineeRepository.findByIdentityAndCustomer(nomineeId, customer)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> nomineeDetailsService.deleteNominee(customerId, nomineeId));
    }

    // ----- GET NOMINEES -----
    @Test
    void getNomineesByCustomerIdentity_success() {
        Nominee nominee = new Nominee();
        nominee.setIdentity(nomineeId);
        nominee.setNomineeId(1);

        NomineeDto nomineeDto = new NomineeDto();
        NomineeDetailsResponseDto responseDto = new NomineeDetailsResponseDto();

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(nomineeRepository.findByCustomerIdentityAndIsDelFalse(customerId)).thenReturn(List.of(nominee));
        when(nomineeMapper.toNomineeDto(nominee)).thenReturn(nomineeDto);
        when(nomineeMapper.toResponse(customer, CommonConstants.CUSTOMER_ADDRESS_STATUS_SUCCESS, List.of(nomineeDto)))
                .thenReturn(responseDto);

        NomineeDetailsResponseDto result = nomineeDetailsService.getNomineesByCustomerIdentity(customerId);
        assertThat(result).isNotNull();
    }

    @Test
    void updateNominee_duplicateNameRelationship_throwsException() {
        NomineeDetailsRequestDto dto = new NomineeDetailsRequestDto();
        dto.setFullName("John");
        dto.setRelationship(1);
        dto.setIsMinor(false);

        Nominee existingNominee = new Nominee();
        existingNominee.setIdentity(nomineeId);
        existingNominee.setNomineeId(1);
        existingNominee.setFullName("Old");
        existingNominee.setRelationship(2);
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(nomineeRepository.findByIdentityAndCustomer(nomineeId, customer)).thenReturn(Optional.of(existingNominee));
        when(nomineeRepository.existsByCustomerAndFullNameAndRelationshipAndIsDelFalse(customer, "John", 1))
                .thenReturn(true);

        assertThrows(BusinessException.class,
                () -> nomineeDetailsService.updateNominee(customerId, nomineeId, dto));
    }

    @Test
    void updateNominee_minorMissingGuardian_throwsException() {
        NomineeDetailsRequestDto dto = new NomineeDetailsRequestDto();
        dto.setFullName("John");
        dto.setRelationship(1);
        dto.setIsMinor(true); // minor without guardian

        Nominee existingNominee = new Nominee();
        existingNominee.setIdentity(nomineeId);
        existingNominee.setNomineeId(1);

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(nomineeRepository.findByIdentityAndCustomer(nomineeId, customer)).thenReturn(Optional.of(existingNominee));

        assertThrows(BusinessException.class,
                () -> nomineeDetailsService.updateNominee(customerId, nomineeId, dto));
    }
    @Test
    void createNominee_duplicateNominee_throwsBusinessException() {
        // Arrange
        NomineeDetailsRequestDto dto = new NomineeDetailsRequestDto();
        dto.setFullName("John");
        dto.setRelationship(1);
        dto.setIsMinor(false);
        dto.setIsSameAddress(false);
        dto.setAddressTypeId(1);
        dto.setDoorNumber("101");
        dto.setAddressLine1("Main Street");

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));

        when(nomineeRepository.existsByCustomerAndFullNameAndRelationshipAndIsDelFalse(customer, "John", 1))
                .thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> nomineeDetailsService.createNominee(customerId, dto));

        assertThat(exception.getMessage()).isEqualTo("A nominee with the same name and relationship already exists.");

        verify(nomineeRepository, never()).save(any());
        verify(nomineeMapper, never()).toEntity(any(), any(), any());
    }
    @Test
    void createNominee_minorMissingGuardianName_throwsException() {
        NomineeDetailsRequestDto dto = new NomineeDetailsRequestDto();
        dto.setIsMinor(true);
        dto.setGuardianDob(java.time.LocalDate.now());
        dto.setGuardianEmail("guardian@example.com");
        dto.setGuardianContactNumber("1234567890");

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> nomineeDetailsService.createNominee(customerId, dto));
        assertThat(exception.getMessage()).isEqualTo("Guardian's name is required for minor nominees.");
    }

    @Test
    void createNominee_minorMissingGuardianDob_throwsException() {
        NomineeDetailsRequestDto dto = new NomineeDetailsRequestDto();
        dto.setIsMinor(true);
        dto.setGuardianName("Guardian Name");
        dto.setGuardianEmail("guardian@example.com");
        dto.setGuardianContactNumber("1234567890");

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> nomineeDetailsService.createNominee(customerId, dto));
        assertThat(exception.getMessage()).isEqualTo("Guardian's date of birth is required for minor nominees.");
    }

    @Test
    void createNominee_minorMissingGuardianEmail_throwsException() {
        NomineeDetailsRequestDto dto = new NomineeDetailsRequestDto();
        dto.setIsMinor(true);
        dto.setGuardianName("Guardian Name");
        dto.setGuardianDob(java.time.LocalDate.now());
        dto.setGuardianContactNumber("1234567890");

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> nomineeDetailsService.createNominee(customerId, dto));
        assertThat(exception.getMessage()).isEqualTo("Guardian's email is required for minor nominees.");
    }

    @Test
    void createNominee_minorMissingGuardianContactNumber_throwsException() {
        NomineeDetailsRequestDto dto = new NomineeDetailsRequestDto();
        dto.setIsMinor(true);
        dto.setGuardianName("Guardian Name");
        dto.setGuardianDob(java.time.LocalDate.now());
        dto.setGuardianEmail("guardian@example.com");

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> nomineeDetailsService.createNominee(customerId, dto));
        assertThat(exception.getMessage()).isEqualTo("Guardian's contact number is required for minor nominees.");
    }
    @Test
    void createNominee_sameAddressTrue_noActivePermanentAddress_throwsResourceNotFoundException() {
        NomineeDetailsRequestDto dto = new NomineeDetailsRequestDto();
        dto.setFullName("Alice");
        dto.setRelationship(1);
        dto.setPercentageShare(BigDecimal.valueOf(50));
        dto.setIsMinor(false);
        dto.setIsSameAddress(true);

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(addressTypeRepository.findByAddressTypeNameAndIsDelFalse(CommonConstants.ADDRESS_TYPE_PERMANENT))
                .thenReturn(Optional.of(new AddressType()));
        when(addressRepository.findByCustomerAndAddressTypeIdAndIsActiveTrueAndIsDelFalse(any(), anyInt()))
                .thenReturn(List.of());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> nomineeDetailsService.createNominee(customerId, dto));

        assertThat(exception.getMessage())
                .contains("No active permanent address found for customer identity");

        verify(nomineeRepository, never()).save(any());
        verify(nomineeMapper, never()).toEntity(any(), any(), any());
    }
    @Test
    void createNominee_isSameAddressFalse_missingAddressFields_throwsBusinessException() {
        NomineeDetailsRequestDto dto = new NomineeDetailsRequestDto();
        dto.setFullName("Bob");
        dto.setRelationship(1);
        dto.setPercentageShare(BigDecimal.valueOf(50));
        dto.setIsMinor(false);
        dto.setIsSameAddress(false);

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));

        BusinessException exception = assertThrows(BusinessException.class,
                () -> nomineeDetailsService.createNominee(customerId, dto));

        assertThat(exception.getMessage())
                .isEqualTo("Address must be provided when isSameAddress is false");

        verify(nomineeRepository, never()).save(any());
        verify(nomineeMapper, never()).toEntity(any(), any(), any());
    }
    @Test
    void calculateTotalShareForUpdate_success() throws Exception {
        NomineeDetailsRequestDto dto = new NomineeDetailsRequestDto();
        dto.setPercentageShare(BigDecimal.valueOf(40)); // new nominee share

        Nominee existingNominee = new Nominee();
        existingNominee.setNomineeId(1);
        existingNominee.setPercentageShare(BigDecimal.valueOf(50));
        existingNominee.setIdentity(UUID.randomUUID());

        Nominee otherNominee = new Nominee();
        otherNominee.setNomineeId(2);
        otherNominee.setPercentageShare(BigDecimal.valueOf(30));
        otherNominee.setIdentity(UUID.randomUUID());

        Customer customer = new Customer();
        customer.setIdentity(UUID.randomUUID());

        when(nomineeRepository.findByCustomerIdentityAndIsDelFalse(customer.getIdentity()))
                .thenReturn(List.of(existingNominee, otherNominee));

        java.lang.reflect.Method method = NomineeDetailsService.class
                .getDeclaredMethod("calculateTotalShareForUpdate", Customer.class, Nominee.class, NomineeDetailsRequestDto.class);
        method.setAccessible(true);

        BigDecimal totalShare = (BigDecimal) method.invoke(nomineeDetailsService, customer, existingNominee, dto);

        assertThat(totalShare).isEqualByComparingTo(BigDecimal.valueOf(70));

        verify(nomineeRepository).findByCustomerIdentityAndIsDelFalse(customer.getIdentity());
    }

}
