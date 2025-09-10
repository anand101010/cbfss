package com.incede.nbfc.core.monolith.masterdata.dto;

import java.util.UUID;

public interface BanksView {

    String getName();
    String getCode();
    String getSwiftBic();
    UUID getIdentity();
}
