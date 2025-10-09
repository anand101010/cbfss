package com.incede.nbfc.core.monolith.customer.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerContactResponseDto {

    private UUID identity;
    private List<Contact> contacts;



    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Contact {
        private UUID contactType;

        @ToString.Exclude
        private String contactDetails;

        private Boolean isPrimary;

        private Boolean isActive;
        private Boolean isOptOutPromotionalNotification;
        private UUID  contactIdentity;

    }
}
