package com.incede.nbfc.core.monolith.masterdata.dto;

import java.util.UUID;


public interface MaritalStatusView {

      String getStatusName();
      Boolean getIsActive();
      UUID getIdentity();
}
