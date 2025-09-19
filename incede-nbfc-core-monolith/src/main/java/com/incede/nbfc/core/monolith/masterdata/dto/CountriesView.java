package com.incede.nbfc.core.monolith.masterdata.dto;

import java.util.UUID;

public interface CountriesView {

    Integer getCountryId();
    String getCountry();
    Boolean getIsActive();
    UUID getIdentity();
}
