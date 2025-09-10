package com.incede.nbfc.core.monolith.masterdata.dto;

import java.util.UUID;

public interface DocumentTypeView {

    String getCode();
    String getDisplayName();
    String getDescription();
    UUID getIdentity();
}
