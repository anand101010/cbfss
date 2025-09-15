package com.incede.nbfc.core.monolith.customer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerContactResponceDto {

    private UUID identity;
    private List<Contact> contacts;



    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Contact {
        private Integer contactType;

        @ToString.Exclude
        private String contactDetails;

        private Boolean isPrimary;

        private Boolean isActive;
        private Boolean isOptOutPromotionalNotification;
        private UUID  contactIdentity;

    }
}
