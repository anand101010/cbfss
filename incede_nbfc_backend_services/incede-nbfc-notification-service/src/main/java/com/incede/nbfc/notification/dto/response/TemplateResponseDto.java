package com.incede.nbfc.notification.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateResponseDto
{
    private Integer tenantId;
    private String code;
    private String name;
    private String description;
    private UUID identity;
}
