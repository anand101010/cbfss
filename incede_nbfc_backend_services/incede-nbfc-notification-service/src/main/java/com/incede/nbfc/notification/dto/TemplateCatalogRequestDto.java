package com.incede.nbfc.notification.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemplateCatalogRequestDto
{

    @NotNull(message = "Tenant ID cannot be null")
    private Integer tenantId;

    @NotBlank(message = "Code cannot be blank")
    @Size(max = 64, message = "Code must not exceed 64 characters")
    private String code;

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 120, message = "Name must not exceed 120 characters")
    private String name;

    private String description;

    @NotNull(message = "category ID cannot be null")
    private Integer category;

    @NotNull(message = "Channel ID cannot be null")
    private Integer Channel;

    @NotNull(message = "isActive flag cannot be null")
    private Boolean isActive ;

    @NotNull(message = "isDel flag cannot be null")
    private Boolean isDel ;


}
