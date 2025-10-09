package com.incede.nbfc.core.monolith.masterdata.dto;

import java.util.UUID;


public interface OccupationView {

      String getOccupationName();
      Boolean getIsActive();
      UUID getIdentity();

}
