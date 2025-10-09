package com.incede.nbfc.core.monolith.masterdata.dto;

import java.util.UUID;

public interface CustomerCategoryView {

    String getCategoryName();
    String getDescription();
    UUID getIdentity();
}
