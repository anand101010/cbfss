package com.incede.nbfc.core.monolith.customer.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerNotificationPreferenceRequestDto {


    @NotNull(message = "SMS consent flag must be set")
    private Boolean consentSms ;


    @NotNull(message = "Email consent flag must be set")
    private Boolean consentEmail ;


    @NotNull(message = "WhatsApp consent flag must be set")
    private Boolean consentWhatsapp ;





}
