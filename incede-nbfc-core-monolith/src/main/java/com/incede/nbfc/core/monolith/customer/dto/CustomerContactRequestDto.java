package com.incede.nbfc.core.monolith.customer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerContactRequestDto {

    @NotNull(message = "contactType should not be null")
    private Integer contactType;

    @NotBlank(message = "contactDetails should not be null")
    @ToString.Exclude
    private String contactDetails;

    @NotNull(message = "contactType should not be null")
    private Boolean isPrimary;

    @NotNull(message = "isActive should not be null")
    private Boolean isActive;



    @ToString.Exclude
    @NotNull(message="isOptOutPromotionalNotification must not be null")
    private Boolean isOptOutPromotionalNotification;
}
