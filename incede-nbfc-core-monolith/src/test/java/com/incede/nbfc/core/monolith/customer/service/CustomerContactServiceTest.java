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
import com.incede.nbfc.core.monolith.masterdata.domain.entity.ContactTypes;
import com.incede.nbfc.core.monolith.masterdata.repository.ContactTypesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    private UUID customerId;
    private UUID contactId;
    private Customer customer;
    private CustomerContact contact;
    private ContactTypes contactType;
    private CustomerContactRequestDto requestDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customerId = UUID.randomUUID();
        contactId = UUID.randomUUID();

        customer = new Customer();
        customer.setIdentity(customerId);

        contactType = new ContactTypes();
        contactType.setIdentity(UUID.randomUUID());

        contact = new CustomerContact();
        contact.setIdentity(contactId);
        contact.setCustomer(customer);
        contact.setContactType(contactType);

        requestDto = new CustomerContactRequestDto();
        requestDto.setContactType(contactType.getIdentity());
        requestDto.setContactDetails("1234567890");
        requestDto.setIsPrimary(true);
    }

    @Test
    void saveContact_success() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(contactTypesRepository.findByIdentity(requestDto.getContactType())).thenReturn(Optional.of(contactType));
        when(contactRepository.existsByContactValue(requestDto.getContactDetails())).thenReturn(false);
        when(contactRepository.existsByContactValueAndIsPrimaryTrueAndIsActiveTrue(requestDto.getContactDetails())).thenReturn(false);
        when(contactMapper.toEntity(requestDto)).thenReturn(contact);
        when(contactRepository.save(contact)).thenReturn(contact);
        when(contactMapper.toResponseDto(contact)).thenReturn(CustomerContactResponseDto.Contact.builder()
                .contactDetails(requestDto.getContactDetails())
                .build());

        CustomerContactResponseDto response = customerContactService.saveContact(customerId, requestDto);

        assertNotNull(response);
        assertEquals(customerId, response.getIdentity());
        assertEquals(1, response.getContacts().size());
        verify(contactRepository).save(contact);
    }

    @Test
    void saveContact_customerNotFound_throwsException() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> customerContactService.saveContact(customerId, requestDto));

        assertEquals(CommonConstants.NOT_FOUND_MESSAGE, exception.getMessage());
    }

    @Test
    void updateContact_success() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(contactRepository.findByIdentityAndCustomer(contactId, customer)).thenReturn(Optional.of(contact));
        when(contactTypesRepository.findByIdentity(requestDto.getContactType())).thenReturn(Optional.of(contactType));
        when(contactRepository.existsByContactValueAndIdentityNotAndIsPrimaryTrueAndIsActiveTrue(any(), any()))
                .thenReturn(false);
        when(contactRepository.existsByContactValueAndIdentityNot(any(), any())).thenReturn(false);

        doAnswer(invocation -> {
            CustomerContact c = invocation.getArgument(0);
            CustomerContactRequestDto dto = invocation.getArgument(1);
            c.setContactValue(dto.getContactDetails());
            return null;
        }).when(contactMapper).updateEntityFromDto(any(CustomerContact.class), any(CustomerContactRequestDto.class));

        when(contactMapper.toResponseDto(contact))
                .thenReturn(CustomerContactResponseDto.Contact.builder()
                        .contactDetails(requestDto.getContactDetails())
                        .build());

        CustomerContactResponseDto response =
                customerContactService.updateContact(customerId, contactId, requestDto);

        assertNotNull(response);
        assertEquals(customerId, response.getIdentity());
        assertEquals(1, response.getContacts().size());
        verify(contactRepository).save(contact);
    }

    @Test
    void getContacts_success() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(contactRepository.findByCustomerAndIsActiveTrue(customer)).thenReturn(List.of(contact));
        when(contactMapper.toResponseDto(contact)).thenReturn(CustomerContactResponseDto.Contact.builder()
                .contactDetails("1234567890")
                .build());

        CustomerContactResponseDto response = customerContactService.getContacts(customerId);

        assertNotNull(response);
        assertEquals(customerId, response.getIdentity());
        assertEquals(1, response.getContacts().size());
    }

    @Test
    void softDeleteContact_success() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(contactRepository.findByIdentityAndCustomer(contactId, customer)).thenReturn(Optional.of(contact));

        customerContactService.softDeleteContact(customerId, contactId);

        assertFalse(contact.getIsActive());
        verify(contactRepository).save(contact);
    }
}
