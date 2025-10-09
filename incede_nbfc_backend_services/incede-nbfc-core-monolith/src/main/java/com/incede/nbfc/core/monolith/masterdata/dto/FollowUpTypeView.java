package com.incede.nbfc.core.monolith.masterdata.dto;

import java.util.UUID;

public interface FollowUpTypeView {

    String getName();
    String getDescription();
    Integer getSortOrder();
    Boolean getIsActive();
    UUID getIdentity();
}
