package com.incede.nbfc.core.monolith.customer.mapper;

import com.incede.nbfc.core.monolith.common.CommonConstants;
import com.incede.nbfc.core.monolith.customer.domain.entity.Customer;
import com.incede.nbfc.core.monolith.customer.domain.entity.CustomerNotificationPreference;
import com.incede.nbfc.core.monolith.customer.dto.CustomerNotificationPreferenceRequestDto;
import com.incede.nbfc.core.monolith.customer.dto.CustomerNotificationPreferenceResponseDto;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Component
public class CustomerNotificationPreferenceMapper {

    /**
     * Convert request DTO to entity.
     */
    public CustomerNotificationPreference toEntity(CustomerNotificationPreferenceRequestDto dto, Customer customer) {
        Objects.requireNonNull(dto, "CustomerNotificationPreferenceRequestDto must not be null");
        Objects.requireNonNull(customer, "Customer must not be null");

        CustomerNotificationPreference entity = new CustomerNotificationPreference();
        entity.setCustomer(customer);
        entity.setConsentSms(Objects.requireNonNull(dto.getConsentSms(), "consentSms must not be null"));
        entity.setConsentEmail(Objects.requireNonNull(dto.getConsentEmail(), "consentEmail must not be null"));
        entity.setConsentWhatsapp(Objects.requireNonNull(dto.getConsentWhatsapp(), "consentWhatsapp must not be null"));
        entity.setCreatedBy(getCreatedBy());
        entity.setIdentity(UUID.randomUUID());
        return entity;
    }

    /**
     * Update existing entity from DTO.
     */
    public void updateEntityFromDto(CustomerNotificationPreference entity, CustomerNotificationPreferenceRequestDto dto) {
        Objects.requireNonNull(entity, "CustomerNotificationPreference entity must not be null");
        Objects.requireNonNull(dto, "CustomerNotificationPreferenceRequestDto must not be null");

        entity.setConsentSms(Objects.requireNonNull(dto.getConsentSms(), "consentSms must not be null"));
        entity.setConsentEmail(Objects.requireNonNull(dto.getConsentEmail(), "consentEmail must not be null"));
        entity.setConsentWhatsapp(Objects.requireNonNull(dto.getConsentWhatsapp(), "consentWhatsapp must not be null"));
        entity.setUpdatedBy(getUpdatedBy());
    }

    /**
     * Convert entity to response DTO.
     */
    public CustomerNotificationPreferenceResponseDto toResponseDto(CustomerNotificationPreference entity) {
        Objects.requireNonNull(entity, "CustomerNotificationPreference entity must not be null");

        CustomerNotificationPreferenceResponseDto.NotificationPreference preference =
                CustomerNotificationPreferenceResponseDto.NotificationPreference.builder()
                        .consentSms(entity.getConsentSms())
                        .consentEmail(entity.getConsentEmail())
                        .consentWhatsapp(entity.getConsentWhatsapp())
                        .build();

        return CustomerNotificationPreferenceResponseDto.builder()
                .identity(entity.getIdentity())
                .notificationPreference(preference)
                .build();
    }

    public  Integer getCreatedBy(){
        return CommonConstants.CREATED_BY;

    }
    public  Integer getUpdatedBy(){
        return CommonConstants.UPDATED_BY;

    }
}
