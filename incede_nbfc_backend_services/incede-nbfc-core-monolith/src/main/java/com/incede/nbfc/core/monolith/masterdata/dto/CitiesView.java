package com.incede.nbfc.core.monolith.masterdata.dto;

import java.util.UUID;

public interface CitiesView {

    Integer getCityId();
    String getCity();
    Boolean getIsActive();
    UUID getIdentity();

}
