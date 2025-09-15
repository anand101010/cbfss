package com.incede.nbfc.core.monolith.customer.mapper;

import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerContact;
import com.incede.nbfc.core.monolith.customer.dto.CustomerContactRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerContactResponceDto;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Mapper for converting between CustomerContact entity and DTOs.
 * Automatically sets createdBy and updatedBy using the current user context.
 */
@Component
public class CustomerContactMapper {

    /**
     * Simulate fetching the current user ID from security context or token.
     * Replace this with actual authentication logic in production.
     */
    private Integer getCurrentUserId() {
        // TODO: Replace with actual user ID from JWT or security context
        return 1;
    }

    /**
     * Convert a request DTO to a CustomerContact entity.
     * Sets createdBy and updatedBy automatically.
     */
    public CustomerContact toEntity(CustomerContactRequestDto dto) {
        Objects.requireNonNull(dto, "CustomerContactRequestDto must not be null");

        CustomerContact contact = new CustomerContact();
        contact.setContactType(Objects.requireNonNull(dto.getContactType(), "contactType must not be null"));
        contact.setContactValue(Objects.requireNonNull(dto.getContactDetails(), "contactDetails must not be null"));
        contact.setIsPrimary(Objects.requireNonNull(dto.getIsPrimary(), "isPrimary must not be null"));
        contact.setIsActive(Objects.requireNonNull(dto.getIsActive(), "isActive must not be null"));
        contact.setCreatedBy(getCurrentUserId());
        contact.setUpdatedBy(getCurrentUserId());
        contact.setIsPromotionalOptOut(
                Objects.requireNonNull(dto.getIsOptOutPromotionalNotification(),
                        "isOptOutPromotionalNotification must not be null")
        );

        return contact;
    }

    /**
     * Update an existing CustomerContact entity from DTO.
     * Sets updatedBy automatically.
     */
    public void updateEntityFromDto(CustomerContact contact, CustomerContactRequestDto dto) {
        Objects.requireNonNull(contact, "CustomerContact must not be null");
        Objects.requireNonNull(dto, "CustomerContactRequestDto must not be null");

        contact.setContactType(Objects.requireNonNull(dto.getContactType(), "contactType must not be null"));
        contact.setContactValue(Objects.requireNonNull(dto.getContactDetails(), "contactDetails must not be null"));
        contact.setIsPrimary(Objects.requireNonNull(dto.getIsPrimary(), "isPrimary must not be null"));
        contact.setIsActive(Objects.requireNonNull(dto.getIsActive(), "isActive must not be null"));
        contact.setUpdatedBy(getCurrentUserId());
        contact.setIsPromotionalOptOut(
                Objects.requireNonNull(dto.getIsOptOutPromotionalNotification(),
                        "isOptOutPromotionalNotification must not be null")
        );
    }

    /**
     * Convert a CustomerContact entity to a response DTO.
     */
    public CustomerContactResponceDto.Contact toResponseDto(CustomerContact entity) {
        Objects.requireNonNull(entity, "CustomerContact entity must not be null");

        return CustomerContactResponceDto.Contact.builder()
                .contactType(entity.getContactType())
                .contactDetails(entity.getContactValue())
                .isPrimary(entity.getIsPrimary())
                .isActive(entity.getIsActive())
                .isOptOutPromotionalNotification(entity.getIsPromotionalOptOut())
                .contactIdentity(entity.getIdentity())
                .build();
    }
}
