package com.incede.nbfc.core.monolith.masterdata.dto;

import java.util.UUID;

public interface StatesView {
    Integer getStateId();
    String getState();
    Boolean getIsActive();
    UUID getIdentity();

}
