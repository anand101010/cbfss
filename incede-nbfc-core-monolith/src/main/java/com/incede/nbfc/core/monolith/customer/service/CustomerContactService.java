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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerContactService {

    private final CustomerRepository customerRepository;
    private final CustomerContactRepository contactRepository;
    private final CustomerContactMapper contactMapper;

    @Transactional
    public CustomerContactResponceDto saveContact(UUID customerId, CustomerContactRequestDto dto) {
        log.info("Saving contact for customer [{}]", customerId);

        Customer customer = customerRepository.findByIdentity(customerId)
                .orElseThrow(() -> new BusinessException(
                        CommonConstants.NOT_FOUND_MESSAGE, ErrorCodes.RESOURCE_NOT_FOUND));

        if (contactRepository.existsByContactValue(dto.getContactDetails())) {
            throw new BusinessException("Contact value already exists", ErrorCodes.CONFLICT);
        }

        boolean isPrimary = Boolean.TRUE.equals(dto.getIsPrimary());

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
        contact.setCreatedBy(dto.getCreatedBy());
        contact.setUpdatedBy(dto.getUpdatedBy());

        CustomerContact saved = contactRepository.save(contact);

        return CustomerContactResponceDto.builder()
                .identity(customer.getIdentity())
                .contacts(List.of(contactMapper.toResponseDto(saved)))
                .build();
    }

    @Transactional
    public CustomerContactResponceDto updateContact(UUID customerId, UUID contactId, CustomerContactRequestDto dto) {
        log.info("Updating contact [{}] for customer [{}]", contactId, customerId);

        Customer customer = customerRepository.findByIdentity(customerId)
                .orElseThrow(() -> new BusinessException(
                        CommonConstants.NOT_FOUND_MESSAGE, ErrorCodes.RESOURCE_NOT_FOUND));

        CustomerContact contact = contactRepository.findByIdentityAndCustomer(contactId, customer)
                .orElseThrow(() -> new BusinessException(
                        CommonConstants.NOT_FOUND_MESSAGE, ErrorCodes.RESOURCE_NOT_FOUND));

        if (contactRepository.existsByContactValueAndIdentityNot(dto.getContactDetails(), contactId)) {
            throw new BusinessException("Contact value already exists", ErrorCodes.CONFLICT);
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
        contact.setUpdatedBy(dto.getUpdatedBy());
        contactRepository.save(contact);

        return CustomerContactResponceDto.builder()
                .identity(customer.getIdentity())
                .contacts(List.of(contactMapper.toResponseDto(contact)))
                .build();
    }

    @Transactional(readOnly = true)
    public CustomerContactResponceDto getContacts(UUID customerId) {
        Customer customer = customerRepository.findByIdentity(customerId)
                .orElseThrow(() -> new BusinessException(
                        CommonConstants.NOT_FOUND_MESSAGE, ErrorCodes.RESOURCE_NOT_FOUND));

        List<CustomerContactResponceDto.Contact> contacts =
                contactRepository.findByCustomerAndIsActiveTrue(customer).stream()
                        .map(contactMapper::toResponseDto)
                        .toList();

        return CustomerContactResponceDto.builder()
                .identity(customer.getIdentity())
                .contacts(contacts)
                .build();
    }

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
