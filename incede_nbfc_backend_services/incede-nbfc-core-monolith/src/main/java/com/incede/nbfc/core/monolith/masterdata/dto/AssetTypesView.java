package com.incede.nbfc.core.monolith.masterdata.dto;

import java.util.UUID;

public interface AssetTypesView {

    String getName();
    String getCode();
    UUID getIdentity();
    Boolean getIsActive();
}
