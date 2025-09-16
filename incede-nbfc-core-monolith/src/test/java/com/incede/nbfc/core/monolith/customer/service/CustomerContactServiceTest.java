package com.incede.nbfc.core.monolith.customer.service;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerContact;
import com.incede.nbfc.core.monolith.customer.dto.CustomerContactRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerContactResponceDto;
import com.incede.nbfc.core.monolith.customer.mapper.CustomerContactMapper;
import com.incede.nbfc.core.monolith.customer.repository.CustomerContactRepository;
import com.incede.nbfc.core.monolith.customer.repository.CustomerRepository;
import com.incede.nbfc.core.monolith.exception.BusinessException;
import com.incede.nbfc.core.monolith.exception.ErrorCodes;

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

class CustomerContactServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CustomerContactRepository contactRepository;
    @Mock
    private CustomerContactMapper contactMapper;

    @InjectMocks
    private CustomerContactService service;

    private Customer customer;
    private CustomerContact contact;
    private CustomerContactRequestDto requestDto;
    private UUID customerId;
    private UUID contactId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customerId = UUID.randomUUID();
        contactId = UUID.randomUUID();
        customer = new Customer();
        customer.setIdentity(customerId);

        contact = new CustomerContact();
        contact.setIdentity(contactId);

        requestDto = CustomerContactRequestDto.builder()
                .contactDetails("test@test.com")
                .contactType(1)
                .isPrimary(true)
                .build();
    }


    @Test
    void saveContact_success() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(contactRepository.existsByContactValue("test@test.com")).thenReturn(false);
        when(contactMapper.toEntity(requestDto)).thenReturn(contact);
        when(contactRepository.save(any())).thenReturn(contact);
        when(contactMapper.toResponseDto(contact))
                .thenReturn(new CustomerContactResponceDto.Contact());

        CustomerContactResponceDto result = service.saveContact(customerId, requestDto);

        assertEquals(customerId, result.getIdentity());
        verify(contactRepository).save(any());
    }

    @Test
    void saveContact_customerNotFound_throwsException() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class,
                () -> service.saveContact(customerId, requestDto));

        assertEquals(ErrorCodes.RESOURCE_NOT_FOUND, ex.getErrorCode());
    }

    @Test
    void saveContact_duplicateContact_throwsException() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(contactRepository.existsByContactValue("test@test.com")).thenReturn(true);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> service.saveContact(customerId, requestDto));

        assertEquals(ErrorCodes.CONFLICT, ex.getErrorCode());
    }


    @Test
    void updateContact_success() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(contactRepository.findByIdentityAndCustomer(contactId, customer))
                .thenReturn(Optional.of(contact));
        when(contactRepository.existsByContactValueAndIdentityNot("test@test.com", contactId))
                .thenReturn(false);
        when(contactMapper.toResponseDto(contact))
                .thenReturn(new CustomerContactResponceDto.Contact());

        CustomerContactResponceDto result = service.updateContact(customerId, contactId, requestDto);

        assertEquals(customerId, result.getIdentity());
        verify(contactRepository).save(contact);
    }

    @Test
    void updateContact_customerNotFound_throwsException() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class,
                () -> service.updateContact(customerId, contactId, requestDto));
    }

    @Test
    void updateContact_contactNotFound_throwsException() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(contactRepository.findByIdentityAndCustomer(contactId, customer))
                .thenReturn(Optional.empty());

        assertThrows(BusinessException.class,
                () -> service.updateContact(customerId, contactId, requestDto));
    }

    @Test
    void updateContact_duplicateContact_throwsException() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(contactRepository.findByIdentityAndCustomer(contactId, customer))
                .thenReturn(Optional.of(contact));
        when(contactRepository.existsByContactValueAndIdentityNot("test@test.com", contactId))
                .thenReturn(true);

        assertThrows(BusinessException.class,
                () -> service.updateContact(customerId, contactId, requestDto));
    }


    @Test
    void getContacts_success() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(contactRepository.findByCustomerAndIsActiveTrue(customer)).thenReturn(List.of(contact));
        when(contactMapper.toResponseDto(contact))
                .thenReturn(new CustomerContactResponceDto.Contact());

        CustomerContactResponceDto result = service.getContacts(customerId);

        assertEquals(customerId, result.getIdentity());
        assertEquals(1, result.getContacts().size());
    }

    @Test
    void getContacts_customerNotFound_throwsException() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class,
                () -> service.getContacts(customerId));
    }


    @Test
    void softDeleteContact_success() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(contactRepository.findByIdentityAndCustomer(contactId, customer))
                .thenReturn(Optional.of(contact));

        service.softDeleteContact(customerId, contactId);

        verify(contactRepository).save(contact);
        assertFalse(contact.getIsActive()); // should be false now
    }

    @Test
    void softDeleteContact_contactNotFound_throwsException() {
        when(customerRepository.findByIdentity(customerId)).thenReturn(Optional.of(customer));
        when(contactRepository.findByIdentityAndCustomer(contactId, customer))
                .thenReturn(Optional.empty());

        assertThrows(BusinessException.class,
                () -> service.softDeleteContact(customerId, contactId));
    }
}
