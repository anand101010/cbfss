package com.incede.nbfc.core.monolith.masterdata.dto;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.Cities;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.Districts;
import com.incede.nbfc.core.monolith.masterdata.domain.entity.States;

import java.math.BigDecimal;
import java.util.UUID;

public interface PincodesView {

    BigDecimal getLongitude();
    BigDecimal getLatitude();
    States getState();
    Districts getDistrict();
    Cities getCity();
    UUID getIdentity();
}
