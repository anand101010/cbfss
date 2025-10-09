package com.incede.nbfc.core.monolith.customer.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerGroupDto implements Serializable {

    @NotNull(message = "Tenant ID is required")
    @Min(value = 1, message = "Tenant id must be a positive integer")
    private Integer tenantId;


    private Integer customerGroupId;

    @NotBlank(message = "Group name must not be blank")
    @Size(max = 100, message = "Group name must not exceed 100 characters")
    private String groupName;



    @NotNull(message = "Active flag must be specified")
    private Boolean isActive = true;
}
