package com.incede.nbfc.core.monolith.masterdata.dto;

import java.util.UUID;

public interface PepVerificationSourceView {

    String getName();
    String getCode();
    Boolean getIsActive();
    UUID getIdentity();
}
