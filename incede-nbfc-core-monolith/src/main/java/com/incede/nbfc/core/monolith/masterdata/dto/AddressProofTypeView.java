package com.incede.nbfc.core.monolith.masterdata.dto;


import java.util.UUID;

public interface AddressProofTypeView {

    String getCode();
    String getName();
    String getConciseDescription();
    UUID getIdentity();
}
