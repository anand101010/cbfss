package com.incede.nbfc.core.monolith.masterdata.dto;

import java.util.UUID;

public interface LeadSourceView {

    String getName();
    String getDescription();
    Boolean getIsActive();
    UUID getIdentity();
}
