package com.incede.nbfc.core.monolith.customer.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerNotificationPreferenceResponseDto {

    private UUID identity;
    private notificationPreference notificationPreference;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class notificationPreference {
    private Boolean consentSms;


    private Boolean consentEmail;


    private Boolean consentWhatsapp;
}

}
