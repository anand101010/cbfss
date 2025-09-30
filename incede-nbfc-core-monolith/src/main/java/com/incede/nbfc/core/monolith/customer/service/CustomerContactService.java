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
import com.incede.nbfc.core.monolith.masterdata.domain.entity.CustomerStatus;
import com.incede.nbfc.core.monolith.masterdata.repository.ContactTypesRepository;
import com.incede.nbfc.core.monolith.masterdata.repository.CustomerStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Service layer for managing Customer Contacts.
 * Handles creating, updating, retrieving, and soft-deleting contact details.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerContactService {

    private final CustomerRepository customerRepository;
    private final CustomerContactRepository contactRepository;
    private final CustomerContactMapper contactMapper;
    private final ContactTypesRepository contactTypesRepository;
    private final CustomerStatusRepository customerStatusRepository;

    /**
     * save contact for customer
     * @param customerId
     * @param dto
     * @return
     */
    @Transactional
    public CustomerContactResponseDto saveContact(UUID customerId, CustomerContactRequestDto dto) {
        log.info("Saving contact for customer [{}]", customerId);


        Customer customer = customerRepository.findByIdentity(customerId)
                .orElseThrow(() -> new BusinessException(
                        CommonConstants.NOT_FOUND_MESSAGE, ErrorCodes.RESOURCE_NOT_FOUND));

        ContactTypes contactType = contactTypesRepository.findByIdentity(dto.getContactType())
                .orElseThrow(() -> new BusinessException(
                        "Contact type not found", ErrorCodes.RESOURCE_NOT_FOUND));

        boolean isPrimary = Boolean.TRUE.equals(dto.getIsPrimary());


        if (isPrimary && contactRepository
                .existsByContactValueAndIsPrimaryTrueAndIsActiveTrue(dto.getContactDetails())) {
            throw new BusinessException(CommonConstants.CONTACT_ALREADY_EXIST, ErrorCodes.CONFLICT);
        }

        if (contactRepository.existsByContactValue(dto.getContactDetails())) {
            throw new BusinessException(CommonConstants.CONFLICT_MESSAGE, ErrorCodes.CONFLICT);
        }

        if (isPrimary) {
            contactRepository.findByCustomerAndContactTypeAndIsActiveTrue(customer, contactType)
                    .forEach(c -> {
                        c.setIsPrimary(false);
                        c.setUpdatedAt(LocalDateTime.now());
                        contactRepository.save(c);
                    });
        }


        CustomerContact contact = contactMapper.toEntity(dto);
        contact.setCustomer(customer);
        contact.setContactType(contactType);
        contact.setIsPrimary(isPrimary);
        contact.setIsActive(true);
        contact.setCreatedAt(LocalDateTime.now());
        contact.setUpdatedAt(LocalDateTime.now());

        CustomerContact saved = contactRepository.save(contact);

        customer.setOnboardingStatus(CommonConstants.COMPLETED);

        return CustomerContactResponseDto.builder()
                .identity(customer.getIdentity())
                .contacts(List.of(contactMapper.toResponseDto(saved)))
                .build();
    }

    /**
     * update the contact for customer
     * @param customerId
     * @param contactId
     * @param dto
     * @return
     */
    @Transactional
    public CustomerContactResponseDto updateContact(UUID customerId, UUID contactId, CustomerContactRequestDto dto) {
        log.info("Updating contact [{}] for customer [{}]", contactId, customerId);


        Customer customer = customerRepository.findByIdentity(customerId)
                .orElseThrow(() -> new BusinessException(
                        CommonConstants.CUSTOMER_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND));


        CustomerContact contact = contactRepository.findByIdentityAndCustomer(contactId, customer)
                .orElseThrow(() -> new BusinessException(
                        CommonConstants.CUSTOMER_CONTACT_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND));


        ContactTypes contactType = contactTypesRepository.findByIdentity(dto.getContactType())
                .orElseThrow(() -> new BusinessException(
                        CommonConstants.CONTACT_TYPE_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND));


        if (contactRepository.existsByContactValueAndIdentityNotAndIsPrimaryTrueAndIsActiveTrue(dto.getContactDetails(), contactId)) {
            throw new BusinessException(CommonConstants.CONTACT_ALREADY_EXIST, ErrorCodes.CONFLICT);
        }
        if (contactRepository.existsByContactValueAndIdentityNot(dto.getContactDetails(), contactId)) {
            throw new BusinessException(CommonConstants.CONTACT_EXISTS, ErrorCodes.CONFLICT);
        }

        boolean isPrimary = Boolean.TRUE.equals(dto.getIsPrimary());


        if (isPrimary) {
            contactRepository.findByCustomerAndContactTypeAndIsActiveTrue(customer, contactType)
                    .forEach(c -> {
                        if (!c.getIdentity().equals(contactId)) {
                            c.setIsPrimary(false);
                            c.setUpdatedAt(LocalDateTime.now());
                            contactRepository.save(c);
                        }
                    });
        }


        contactMapper.updateEntityFromDto(contact, dto);
        contact.setContactType(contactType);
        contact.setIsPrimary(isPrimary);
        contact.setUpdatedAt(LocalDateTime.now());

        contactRepository.save(contact);

        return CustomerContactResponseDto.builder()
                .identity(customer.getIdentity())
                .contacts(List.of(contactMapper.toResponseDto(contact)))
                .build();
    }

    /**
     * Get all active contacts for a given customer.
     */
    @Transactional(readOnly = true)
    public CustomerContactResponseDto getContacts(UUID customerId) {
        Customer customer = customerRepository.findByIdentity(customerId)
                .orElseThrow(() -> new BusinessException(
                        CommonConstants.CUSTOMER_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND));

        List<CustomerContactResponseDto.Contact> contacts =
                contactRepository.findByCustomerAndIsActiveTrue(customer).stream()
                        .map(contactMapper::toResponseDto)
                        .toList();

        return CustomerContactResponseDto.builder()
                .identity(customer.getIdentity())
                .contacts(contacts)
                .build();
    }

    /**
     * Soft delete a contact by marking it inactive.
     */
    @Transactional
    public void softDeleteContact(UUID customerId, UUID contactId) {
        log.info("Soft deleting contact [{}] for customer [{}]", contactId, customerId);

        Customer customer = customerRepository.findByIdentity(customerId)
                .orElseThrow(() -> new BusinessException(
                        CommonConstants.CUSTOMER_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND));

        CustomerContact contact = contactRepository.findByIdentityAndCustomer(contactId, customer)
                .orElseThrow(() -> new BusinessException(
                        CommonConstants.CUSTOMER_CONTACT_NOT_FOUND, ErrorCodes.RESOURCE_NOT_FOUND));

        contact.setIsActive(false);
        contact.setUpdatedAt(LocalDateTime.now());
        contactRepository.save(contact);
    }
}
