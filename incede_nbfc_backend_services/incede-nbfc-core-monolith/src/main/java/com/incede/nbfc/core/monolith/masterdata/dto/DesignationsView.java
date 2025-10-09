package com.incede.nbfc.core.monolith.masterdata.dto;

import java.util.UUID;

public interface DesignationsView {

    String getName();
    String getCode();
    String getDescription();
    Short getLevel();
    UUID getIdentity();
}
