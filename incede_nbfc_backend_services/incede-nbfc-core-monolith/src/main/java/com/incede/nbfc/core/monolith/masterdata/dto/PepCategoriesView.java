package com.incede.nbfc.core.monolith.masterdata.dto;

import java.util.UUID;

public interface PepCategoriesView {

    String getCode();
    String getName();
    Boolean getIsActive();
    UUID getIdentity();
}
