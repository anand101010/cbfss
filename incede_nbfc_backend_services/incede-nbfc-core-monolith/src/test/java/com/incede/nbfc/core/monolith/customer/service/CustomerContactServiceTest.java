package com.incede.nbfc.core.monolith.customer.service;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerContact;
import com.incede.nbfc.core.monolith.customer.dto.CustomerContactRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerContactResponseDto;
import com.incede.nbfc.core.monolith.customer.mapper.CustomerContactMapper;
import com.incede.nbfc.core.monolith.customer.repository.CustomerContactRepository;
import com.incede.nbfc.core.monolith.customer.repository.CustomerRepository;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ErrorCodes;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.ContactTypes;
import com.incede.nbfc.core.monolith.masterdata.repository.ContactTypesRepository;
import com.incede.nbfc.core.monolith.masterdata.repository.CustomerStatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerContactServiceTest {

    @InjectMocks
    private CustomerContactService customerContactService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerContactRepository contactRepository;

    @Mock
    private CustomerContactMapper contactMapper;

    @Mock
    private ContactTypesRepository contactTypesRepository;

    @Mock
    private CustomerStatusRepository customerStatusRepository;

    private UUID customerId;
    private UUID contactId;
    private UUID contactTypeId;
    private Customer customer;
    private CustomerContact contact;
    private ContactTypes contactType;
    private CustomerContactRequestDto requestDto;
    private CustomerContactResponseDto.Contact contactResponseDto;

    @BeforeEach
    void setUp() {
        customerId = UUID.randomUUID();
        contactId = UUID.randomUUID();
        contactTypeId = UUID.randomUUID();

        customer = new Customer();
        customer.setIdentity(customerId);
        customer.setOnboardingStatus(CommonConstants.IN_PROGRESS);

        contactType = new ContactTypes();
        contactType.setIdentity(contactTypeId);

        contact = new CustomerContact();
        contact.setIdentity(contactId);
        contact.setCustomer(customer);
        contact.setContactType(contactType);
        contact.setContactValue("1234567890");
        contact.setIsPrimary(true);
        contact.setIsActive(true);
        contact.setCreatedAt(LocalDateTime.now());
        contact.setUpdatedAt(LocalDateTime.now());

        requestDto = new CustomerContactRequestDto();
        requestDto.setContactType(contactTypeId);
        requestDto.setContactDetails("1234567890");
        requestDto.setIsPrimary(true);

        contactResponseDto = CustomerContactResponseDto.Contact.builder()
                .contactDetails("1234567890")
                .isPrimary(true)
                .build();
    }

    @Test
    void saveContact_success() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(contactTypesRepository.findByIdentity(contactTypeId)).thenReturn(Optional.of(contactType));
        when(contactRepository.existsByContactValueAndIsPrimaryTrueAndIsActiveTrue(requestDto.getContactDetails())).thenReturn(false);
        when(contactRepository.existsByContactValue(requestDto.getContactDetails())).thenReturn(false);
        when(contactMapper.toEntity(requestDto)).thenReturn(contact);
        when(contactRepository.save(contact)).thenReturn(contact);
        when(contactMapper.toResponseDto(contact)).thenReturn(contactResponseDto);

        when(contactRepository.findByCustomerAndContactTypeAndIsActiveTrue(customer, contactType))
                .thenReturn(List.of());

        CustomerContactResponseDto response = customerContactService.saveContact(customerId, requestDto);

        assertNotNull(response);
        assertEquals(customerId, response.getIdentity());
        assertEquals(1, response.getContacts().size());
        verify(contactRepository).save(contact);
        verify(contactRepository).findByCustomerAndContactTypeAndIsActiveTrue(customer, contactType);
    }

    @Test
    void saveContact_successWithExistingPrimaryContact() {
        CustomerContact existingPrimaryContact = new CustomerContact();
        existingPrimaryContact.setIdentity(UUID.randomUUID());
        existingPrimaryContact.setIsPrimary(true);
        existingPrimaryContact.setIsActive(true);

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(contactTypesRepository.findByIdentity(contactTypeId)).thenReturn(Optional.of(contactType));
        when(contactRepository.existsByContactValueAndIsPrimaryTrueAndIsActiveTrue(requestDto.getContactDetails())).thenReturn(false);
        when(contactRepository.existsByContactValue(requestDto.getContactDetails())).thenReturn(false);
        when(contactRepository.findByCustomerAndContactTypeAndIsActiveTrue(customer, contactType))
                .thenReturn(List.of(existingPrimaryContact));
        when(contactMapper.toEntity(requestDto)).thenReturn(contact);
        when(contactRepository.save(any(CustomerContact.class))).thenReturn(contact);
        when(contactMapper.toResponseDto(contact)).thenReturn(contactResponseDto);

        CustomerContactResponseDto response = customerContactService.saveContact(customerId, requestDto);

        assertNotNull(response);
        verify(contactRepository).save(existingPrimaryContact); // Verify existing contact is updated to non-primary
        verify(contactRepository).save(contact); // Verify new contact is saved
        assertFalse(existingPrimaryContact.getIsPrimary());
    }

    @Test
    void saveContact_customerNotFound() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> customerContactService.saveContact(customerId, requestDto));

        assertEquals(CommonConstants.NOT_FOUND_MESSAGE, exception.getMessage());
        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void saveContact_contactTypeNotFound() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(contactTypesRepository.findByIdentity(contactTypeId)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> customerContactService.saveContact(customerId, requestDto));

        assertEquals("Contact type not found", exception.getMessage());
        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void saveContact_primaryContactAlreadyExists() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(contactTypesRepository.findByIdentity(contactTypeId)).thenReturn(Optional.of(contactType));
        when(contactRepository.existsByContactValueAndIsPrimaryTrueAndIsActiveTrue(requestDto.getContactDetails())).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> customerContactService.saveContact(customerId, requestDto));

        assertEquals(CommonConstants.CONTACT_ALREADY_EXIST, exception.getMessage());
        assertEquals(ErrorCodes.CONFLICT, exception.getErrorCode());
    }

    @Test
    void saveContact_contactValueAlreadyExists() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(contactTypesRepository.findByIdentity(contactTypeId)).thenReturn(Optional.of(contactType));
        when(contactRepository.existsByContactValueAndIsPrimaryTrueAndIsActiveTrue(requestDto.getContactDetails())).thenReturn(false);
        when(contactRepository.existsByContactValue(requestDto.getContactDetails())).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> customerContactService.saveContact(customerId, requestDto));

        assertEquals(CommonConstants.CONFLICT_MESSAGE, exception.getMessage());
        assertEquals(ErrorCodes.CONFLICT, exception.getErrorCode());
    }

    @Test
    void saveContact_nonPrimaryContact() {
        requestDto.setIsPrimary(false);

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(contactTypesRepository.findByIdentity(contactTypeId)).thenReturn(Optional.of(contactType));
        when(contactRepository.existsByContactValue(requestDto.getContactDetails())).thenReturn(false);
        when(contactMapper.toEntity(requestDto)).thenReturn(contact);
        when(contactRepository.save(contact)).thenReturn(contact);
        when(contactMapper.toResponseDto(contact)).thenReturn(contactResponseDto);

        CustomerContactResponseDto response = customerContactService.saveContact(customerId, requestDto);

        assertNotNull(response);
        verify(contactRepository).existsByContactValue(requestDto.getContactDetails());
        verify(contactRepository, never()).existsByContactValueAndIsPrimaryTrueAndIsActiveTrue(any());
        verify(contactRepository, never()).findByCustomerAndContactTypeAndIsActiveTrue(any(), any());
    }

    @Test
    void updateContact_success() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(contactRepository.findByIdentityAndCustomer(contactId, customer)).thenReturn(Optional.of(contact));
        when(contactTypesRepository.findByIdentity(contactTypeId)).thenReturn(Optional.of(contactType));
        when(contactRepository.existsByContactValueAndIdentityNotAndIsPrimaryTrueAndIsActiveTrue(requestDto.getContactDetails(), contactId)).thenReturn(false);
        when(contactRepository.existsByContactValueAndIdentityNot(requestDto.getContactDetails(), contactId)).thenReturn(false);
        when(contactMapper.toResponseDto(contact)).thenReturn(contactResponseDto);

        when(contactRepository.findByCustomerAndContactTypeAndIsActiveTrue(customer, contactType))
                .thenReturn(List.of());

        CustomerContactResponseDto response = customerContactService.updateContact(customerId, contactId, requestDto);

        assertNotNull(response);
        assertEquals(customerId, response.getIdentity());
        assertEquals(1, response.getContacts().size());
        verify(contactMapper).updateEntityFromDto(contact, requestDto);
        verify(contactRepository).save(contact);
        verify(contactRepository).findByCustomerAndContactTypeAndIsActiveTrue(customer, contactType);
    }

    @Test
    void updateContact_withExistingPrimaryContacts() {
        CustomerContact existingPrimaryContact = new CustomerContact();
        existingPrimaryContact.setIdentity(UUID.randomUUID());
        existingPrimaryContact.setIsPrimary(true);
        existingPrimaryContact.setIsActive(true);

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(contactRepository.findByIdentityAndCustomer(contactId, customer)).thenReturn(Optional.of(contact));
        when(contactTypesRepository.findByIdentity(contactTypeId)).thenReturn(Optional.of(contactType));
        when(contactRepository.existsByContactValueAndIdentityNotAndIsPrimaryTrueAndIsActiveTrue(requestDto.getContactDetails(), contactId)).thenReturn(false);
        when(contactRepository.existsByContactValueAndIdentityNot(requestDto.getContactDetails(), contactId)).thenReturn(false);
        when(contactRepository.findByCustomerAndContactTypeAndIsActiveTrue(customer, contactType))
                .thenReturn(List.of(existingPrimaryContact));
        when(contactMapper.toResponseDto(contact)).thenReturn(contactResponseDto);

        CustomerContactResponseDto response = customerContactService.updateContact(customerId, contactId, requestDto);

        assertNotNull(response);
        verify(contactRepository).save(existingPrimaryContact);
        assertFalse(existingPrimaryContact.getIsPrimary());
    }

    @Test
    void updateContact_customerNotFound() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> customerContactService.updateContact(customerId, contactId, requestDto));

        assertEquals(CommonConstants.CUSTOMER_NOT_FOUND, exception.getMessage());
        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void updateContact_contactNotFound() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(contactRepository.findByIdentityAndCustomer(contactId, customer)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> customerContactService.updateContact(customerId, contactId, requestDto));

        assertEquals(CommonConstants.CUSTOMER_CONTACT_NOT_FOUND, exception.getMessage());
        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void updateContact_contactTypeNotFound() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(contactRepository.findByIdentityAndCustomer(contactId, customer)).thenReturn(Optional.of(contact));
        when(contactTypesRepository.findByIdentity(contactTypeId)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> customerContactService.updateContact(customerId, contactId, requestDto));

        assertEquals(CommonConstants.CONTACT_TYPE_NOT_FOUND, exception.getMessage());
        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void updateContact_primaryContactAlreadyExists() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(contactRepository.findByIdentityAndCustomer(contactId, customer)).thenReturn(Optional.of(contact));
        when(contactTypesRepository.findByIdentity(contactTypeId)).thenReturn(Optional.of(contactType));
        when(contactRepository.existsByContactValueAndIdentityNotAndIsPrimaryTrueAndIsActiveTrue(requestDto.getContactDetails(), contactId)).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> customerContactService.updateContact(customerId, contactId, requestDto));

        assertEquals(CommonConstants.CONTACT_ALREADY_EXIST, exception.getMessage());
        assertEquals(ErrorCodes.CONFLICT, exception.getErrorCode());
    }

    @Test
    void updateContact_contactValueAlreadyExists() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(contactRepository.findByIdentityAndCustomer(contactId, customer)).thenReturn(Optional.of(contact));
        when(contactTypesRepository.findByIdentity(contactTypeId)).thenReturn(Optional.of(contactType));
        when(contactRepository.existsByContactValueAndIdentityNotAndIsPrimaryTrueAndIsActiveTrue(requestDto.getContactDetails(), contactId)).thenReturn(false);
        when(contactRepository.existsByContactValueAndIdentityNot(requestDto.getContactDetails(), contactId)).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> customerContactService.updateContact(customerId, contactId, requestDto));

        assertEquals(CommonConstants.CONTACT_EXISTS, exception.getMessage());
        assertEquals(ErrorCodes.CONFLICT, exception.getErrorCode());
    }

    @Test
    void updateContact_nonPrimaryContact() {
        requestDto.setIsPrimary(false);

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(contactRepository.findByIdentityAndCustomer(contactId, customer)).thenReturn(Optional.of(contact));
        when(contactTypesRepository.findByIdentity(contactTypeId)).thenReturn(Optional.of(contactType));
        when(contactRepository.existsByContactValueAndIdentityNotAndIsPrimaryTrueAndIsActiveTrue(requestDto.getContactDetails(), contactId)).thenReturn(false);
        when(contactRepository.existsByContactValueAndIdentityNot(requestDto.getContactDetails(), contactId)).thenReturn(false);
        when(contactMapper.toResponseDto(contact)).thenReturn(contactResponseDto);

        CustomerContactResponseDto response = customerContactService.updateContact(customerId, contactId, requestDto);

        assertNotNull(response);

        verify(contactRepository).existsByContactValueAndIdentityNotAndIsPrimaryTrueAndIsActiveTrue(requestDto.getContactDetails(), contactId);
        verify(contactRepository).existsByContactValueAndIdentityNot(requestDto.getContactDetails(), contactId);

        verify(contactRepository, never()).findByCustomerAndContactTypeAndIsActiveTrue(any(), any());
    }

    @Test
    void getContacts_success() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(contactRepository.findByCustomerAndIsActiveTrue(customer)).thenReturn(List.of(contact));
        when(contactMapper.toResponseDto(contact)).thenReturn(contactResponseDto);

        CustomerContactResponseDto response = customerContactService.getContacts(customerId);

        assertNotNull(response);
        assertEquals(customerId, response.getIdentity());
        assertEquals(1, response.getContacts().size());
        verify(contactRepository).findByCustomerAndIsActiveTrue(customer);
    }

    @Test
    void getContacts_customerNotFound() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> customerContactService.getContacts(customerId));

        assertEquals(CommonConstants.CUSTOMER_NOT_FOUND, exception.getMessage());
        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void getContacts_noContactsFound() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(contactRepository.findByCustomerAndIsActiveTrue(customer)).thenReturn(List.of());

        CustomerContactResponseDto response = customerContactService.getContacts(customerId);

        assertNotNull(response);
        assertEquals(customerId, response.getIdentity());
        assertTrue(response.getContacts().isEmpty());
    }

    @Test
    void softDeleteContact_success() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(contactRepository.findByIdentityAndCustomer(contactId, customer)).thenReturn(Optional.of(contact));

        customerContactService.softDeleteContact(customerId, contactId);

        assertFalse(contact.getIsActive());
        assertNotNull(contact.getUpdatedAt());
        verify(contactRepository).save(contact);
    }

    @Test
    void softDeleteContact_customerNotFound() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> customerContactService.softDeleteContact(customerId, contactId));

        assertEquals(CommonConstants.CUSTOMER_NOT_FOUND, exception.getMessage());
        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void softDeleteContact_contactNotFound() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(contactRepository.findByIdentityAndCustomer(contactId, customer)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> customerContactService.softDeleteContact(customerId, contactId));

        assertEquals(CommonConstants.CUSTOMER_CONTACT_NOT_FOUND, exception.getMessage());
        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void saveContact_withNullIsPrimary() {
        requestDto.setIsPrimary(null);

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(contactTypesRepository.findByIdentity(contactTypeId)).thenReturn(Optional.of(contactType));
        when(contactRepository.existsByContactValue(requestDto.getContactDetails())).thenReturn(false);
        when(contactMapper.toEntity(requestDto)).thenReturn(contact);
        when(contactRepository.save(contact)).thenReturn(contact);
        when(contactMapper.toResponseDto(contact)).thenReturn(contactResponseDto);

        CustomerContactResponseDto response = customerContactService.saveContact(customerId, requestDto);

        assertNotNull(response);
        assertFalse(contact.getIsPrimary()); // Should default to false when null
        verify(contactRepository, never()).existsByContactValueAndIsPrimaryTrueAndIsActiveTrue(any());
        verify(contactRepository, never()).findByCustomerAndContactTypeAndIsActiveTrue(any(), any());
    }

    @Test
    void updateContact_withNullIsPrimary() {
        requestDto.setIsPrimary(null);

        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(contactRepository.findByIdentityAndCustomer(contactId, customer)).thenReturn(Optional.of(contact));
        when(contactTypesRepository.findByIdentity(contactTypeId)).thenReturn(Optional.of(contactType));
        when(contactRepository.existsByContactValueAndIdentityNotAndIsPrimaryTrueAndIsActiveTrue(requestDto.getContactDetails(), contactId)).thenReturn(false);
        when(contactRepository.existsByContactValueAndIdentityNot(requestDto.getContactDetails(), contactId)).thenReturn(false);
        when(contactMapper.toResponseDto(contact)).thenReturn(contactResponseDto);

        CustomerContactResponseDto response = customerContactService.updateContact(customerId, contactId, requestDto);

        assertNotNull(response);
        assertFalse(contact.getIsPrimary()); // Should default to false when null

        // Both existence checks should be called even when isPrimary is null
        verify(contactRepository).existsByContactValueAndIdentityNotAndIsPrimaryTrueAndIsActiveTrue(requestDto.getContactDetails(), contactId);
        verify(contactRepository).existsByContactValueAndIdentityNot(requestDto.getContactDetails(), contactId);

        // But findByCustomerAndContactTypeAndIsActiveTrue should NOT be called for non-primary contacts
        verify(contactRepository, never()).findByCustomerAndContactTypeAndIsActiveTrue(any(), any());
    }
}