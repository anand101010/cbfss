package com.incede.nbfc.core.monolith.masterdata.dto;

import java.math.BigDecimal;
import java.util.UUID;

public interface PincodesView {

    Integer getPincodeId();
    StatesView getState();
    DistrictsView getDistrict();
    CitiesView getCity();
    Integer getPincode();
    BigDecimal getLatitude();
    BigDecimal getLongitude();
    UUID getIdentity();
}
