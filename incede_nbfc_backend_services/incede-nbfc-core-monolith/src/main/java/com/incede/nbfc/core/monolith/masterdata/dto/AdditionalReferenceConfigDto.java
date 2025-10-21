package com.incede.nbfc.core.monolith.masterdata.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdditionalReferenceConfigDto
{
    private UUID identity;
    private String referenceFieldName;
    private String referenceFieldCode;
    private String dataType;
    private Integer maxLength;
    private String placeholderText;
    private String helpText;
    private Integer sortOrder;
    private Boolean isMandatory;
    private Boolean isActive;
}
