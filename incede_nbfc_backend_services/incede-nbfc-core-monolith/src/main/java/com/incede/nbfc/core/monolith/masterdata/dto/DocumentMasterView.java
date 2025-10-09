package com.incede.nbfc.core.monolith.masterdata.dto;

import java.util.UUID;


public interface DocumentMasterView {

      String getDocname();
      String getDocCode();
      Boolean getIsIdentityProof();
      Boolean getIsAddressProof();
      Character getDocCategory();
      Boolean getIsActive();
      UUID getIdentity();

}
