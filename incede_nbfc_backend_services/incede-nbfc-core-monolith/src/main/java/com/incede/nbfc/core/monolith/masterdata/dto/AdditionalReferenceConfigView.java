package com.incede.nbfc.core.monolith.masterdata.dto;


import java.util.UUID;

public interface AdditionalReferenceConfigView {
    String getReferenceFieldName();

    String getReferenceFieldCode();

    String getDataType();

    Integer getSortOrder();

    Boolean getIsMandatory();

    Boolean getIsActive();

    UUID getIdentity();

}
