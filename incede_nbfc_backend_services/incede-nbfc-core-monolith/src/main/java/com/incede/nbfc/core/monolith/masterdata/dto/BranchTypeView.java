package com.incede.nbfc.core.monolith.masterdata.dto;

import java.util.UUID;

public interface BranchTypeView {

    Integer getBranchTypeId();
    String getCode();
    String getName();
    String getDescription();
    Boolean getIsActive();
    UUID getIdentity();
}
