package com.incede.nbfc.core.monolith.masterdata.dto;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.Pincodes;

import java.math.BigDecimal;
import java.util.UUID;

public interface PostOfficesView {
    Integer getPostOfficeId();
   // PincodesView getPincode();
    String getOfficeName();
    String getOfficeType();
    String getDeliveryStatus();
    String getTaluk();
    String getRegion();
    String getDivision();
    BigDecimal getLatitude();
    BigDecimal getLongitude();
    UUID getIdentity();


}
