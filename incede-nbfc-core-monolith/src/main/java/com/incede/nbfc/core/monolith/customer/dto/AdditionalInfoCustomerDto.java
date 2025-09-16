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
public class AdditionalInfoCustomerDto {

    @NotNull(message = "nationality cannot be null")
    private Integer nationality;
    @NotNull(message = "preferredLanguageId cannot be null")
    private Integer preferredLanguageId;
    @NotNull(message = "residentialStatusId cannot be null")
    private Integer residentialStatusId;

}
