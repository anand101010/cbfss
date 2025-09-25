package com.incede.nbfc.core.monolith.customer.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdditionalInfoCustomerDto {

    @NotNull(message = "nationality cannot be null")
    private UUID nationality;
    @NotNull(message = "preferredLanguageId cannot be null")
    private UUID preferredLanguageId;
    @NotNull(message = "residentialStatusId cannot be null")
    private UUID residentialStatusId;
    private UUID customerGroupId;
    private UUID riskCategory;
    private UUID categoryId;

}
