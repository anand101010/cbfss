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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Service layer for managing Customer Contacts
 * Handles creating, updating, retrieving and soft-deleting contact details.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerContactService {

    private final CustomerRepository customerRepository;
    private final CustomerContactRepository contactRepository;
    private final CustomerContactMapper contactMapper;

    /**
     * Save a new contact for the given customer.
     * - Checks if customer exists
     * - Ensures no duplicate contact value exists
     * - Handles primary contact logic (sets others as non-primary)
     */
    @Transactional
    public CustomerContactResponseDto saveContact(UUID customerId, CustomerContactRequestDto dto) {
        log.info("Saving contact for customer [{}]", customerId);

        Customer customer = customerRepository.findByIdentity(customerId)
                .orElseThrow(() -> new BusinessException(
                        CommonConstants.NOT_FOUND_MESSAGE, ErrorCodes.RESOURCE_NOT_FOUND));

        boolean isPrimary = Boolean.TRUE.equals(dto.getIsPrimary());

        if (isPrimary) {
            boolean primaryExists = contactRepository
                    .existsByContactValueAndIsPrimaryTrueAndIsActiveTrue(dto.getContactDetails());
            if (primaryExists) {
                throw new BusinessException(
              CommonConstants.CONTACT_ALREADY_EXIST,ErrorCodes.CONFLICT
                );
            }
        }

        if (contactRepository.existsByContactValue(dto.getContactDetails())) {
            throw new BusinessException(CommonConstants.CONFLICT_MESSAGE, ErrorCodes.CONFLICT);
        }

        if (isPrimary) {
            contactRepository.findByCustomerAndContactTypeAndIsActiveTrue(customer, dto.getContactType())
                    .forEach(c -> {
                        c.setIsPrimary(false);
                        c.setUpdatedAt(LocalDateTime.now());
                        contactRepository.save(c);
                    });
        }

        CustomerContact contact = contactMapper.toEntity(dto);
        contact.setCustomer(customer);
        contact.setIsPrimary(isPrimary);
        contact.setIsActive(true);
        contact.setCreatedAt(LocalDateTime.now());
        contact.setUpdatedAt(LocalDateTime.now());

        CustomerContact saved = contactRepository.save(contact);

        return CustomerContactResponseDto.builder()
                .identity(customer.getIdentity())
                .contacts(List.of(contactMapper.toResponseDto(saved)))
                .build();
    }




    /**
     * Update an existing contact for the given customer.
     * - Validates both customer and contact exist
     * - Checks for duplicate contact value (excluding current contact)
     * - Updates primary flag and related existing contacts if needed
     */
    @Transactional
    public CustomerContactResponseDto updateContact(UUID customerId, UUID contactId, CustomerContactRequestDto dto) {
        log.info("Updating contact [{}] for customer [{}]", contactId, customerId);

        Customer customer = customerRepository.findByIdentity(customerId)
                .orElseThrow(() -> new BusinessException(
                        CommonConstants.NOT_FOUND_MESSAGE, ErrorCodes.RESOURCE_NOT_FOUND));

        CustomerContact contact = contactRepository.findByIdentityAndCustomer(contactId, customer)
                .orElseThrow(() -> new BusinessException(
                        CommonConstants.NOT_FOUND_MESSAGE, ErrorCodes.RESOURCE_NOT_FOUND));

        if (contactRepository.existsByContactValueAndIdentityNotAndIsPrimaryTrueAndIsActiveTrue(dto.getContactDetails(), contactId)) {
            throw new BusinessException(
            CommonConstants.CONTACT_ALREADY_EXIST,ErrorCodes.CONFLICT
            );
        }

        if (contactRepository.existsByContactValueAndIdentityNot(dto.getContactDetails(), contactId)) {
            throw new BusinessException(CommonConstants.CONTACT_EXISTS, ErrorCodes.CONFLICT);
        }

        boolean isPrimary = Boolean.TRUE.equals(dto.getIsPrimary());

        if (isPrimary) {
            contactRepository.findByCustomerAndContactTypeAndIsActiveTrue(customer, dto.getContactType())
                    .forEach(c -> {
                        if (!c.getIdentity().equals(contactId)) {
                            c.setIsPrimary(false);
                            c.setUpdatedAt(LocalDateTime.now());
                            contactRepository.save(c);
                        }
                    });
        }

        contactMapper.updateEntityFromDto(contact, dto);
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
     * - Throws exception if customer does not exist
     */
    @Transactional(readOnly = true)
    public CustomerContactResponseDto getContacts(UUID customerId) {
        // Validate customer exists
        Customer customer = customerRepository.findByIdentity(customerId)
                .orElseThrow(() -> new BusinessException(
                        CommonConstants.NOT_FOUND_MESSAGE, ErrorCodes.RESOURCE_NOT_FOUND));

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
     * - Validates customer and contact exist
     */
    @Transactional
    public void softDeleteContact(UUID customerId, UUID contactId) {
        log.info("Soft deleting contact [{}] for customer [{}]", contactId, customerId);

        Customer customer = customerRepository.findByIdentity(customerId)
                .orElseThrow(() -> new BusinessException(
                        CommonConstants.NOT_FOUND_MESSAGE, ErrorCodes.RESOURCE_NOT_FOUND));

        CustomerContact contact = contactRepository.findByIdentityAndCustomer(contactId, customer)
                .orElseThrow(() -> new BusinessException(
                        CommonConstants.NOT_FOUND_MESSAGE, ErrorCodes.RESOURCE_NOT_FOUND));

        contact.setIsActive(false);
        contact.setUpdatedAt(LocalDateTime.now());
        contactRepository.save(contact);
    }
}
