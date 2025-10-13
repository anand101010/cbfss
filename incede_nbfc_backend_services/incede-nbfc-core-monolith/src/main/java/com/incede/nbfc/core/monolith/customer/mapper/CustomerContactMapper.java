package com.incede.nbfc.core.monolith.customer.mapper;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerContact;
import com.incede.nbfc.core.monolith.customer.dto.CustomerContactRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerContactResponseDto;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Mapper for converting between CustomerContact entity and DTOs.
 * Automatically sets createdBy and updatedBy using the current user context.
 */
@Component
public class CustomerContactMapper {



    /**
     * Convert a request DTO to a CustomerContact entity.
     * Sets createdBy and updatedBy automatically.
     */
    public CustomerContact toEntity(CustomerContactRequestDto dto) {
        Objects.requireNonNull(dto, "CustomerContactRequestDto must not be null");

        CustomerContact contact = new CustomerContact();
        contact.setContactValue(Objects.requireNonNull(dto.getContactDetails(), "contactDetails must not be null"));
        contact.setIsPrimary(Objects.requireNonNull(dto.getIsPrimary(), "isPrimary must not be null"));
        contact.setIsVerified(Objects.requireNonNull(dto.getIsVerified(),"isVerified must not be null "));
        contact.setIsActive(Objects.requireNonNull(dto.getIsActive(), "isActive must not be null"));
        contact.setCreatedBy(getCreatedBy());
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

        contact.setContactValue(Objects.requireNonNull(dto.getContactDetails(), "contactDetails must not be null"));
        contact.setIsPrimary(Objects.requireNonNull(dto.getIsPrimary(), "isPrimary must not be null"));
        contact.setIsActive(Objects.requireNonNull(dto.getIsActive(), "isActive must not be null"));
        contact.setUpdatedBy(getUpdatedBy());
        contact.setIsPromotionalOptOut(
                Objects.requireNonNull(dto.getIsOptOutPromotionalNotification(),
                        "isOptOutPromotionalNotification must not be null")
        );
    }

    /**
     * Convert a CustomerContact entity to a response DTO.
     */
    public CustomerContactResponseDto.Contact toResponseDto(CustomerContact entity) {
        Objects.requireNonNull(entity, "CustomerContact entity must not be null");

        return CustomerContactResponseDto.Contact.builder()
                .contactType(entity.getContactType().getIdentity())
                .contactDetails(entity.getContactValue())
                .isPrimary(entity.getIsPrimary())
                .isActive(entity.getIsActive())
                .isOptOutPromotionalNotification(entity.getIsPromotionalOptOut())
                .contactIdentity(entity.getIdentity())
                .build();
    }
    public  Integer getCreatedBy(){
        return CommonConstants.CREATED_BY;

    }
    public  Integer getUpdatedBy(){
        return CommonConstants.UPDATED_BY;

    }
}
