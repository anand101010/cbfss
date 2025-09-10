package com.incede.nbfc.core.monolith.masterdata.dto;

import java.util.UUID;

public interface CustomerStatusView {

      String getStatusName();
      Boolean getIsActive();
      UUID getIdentity();
}
