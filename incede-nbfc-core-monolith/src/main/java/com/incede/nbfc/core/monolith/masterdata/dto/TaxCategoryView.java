package com.incede.nbfc.core.monolith.masterdata.dto;

import java.util.UUID;


public interface TaxCategoryView {

      String getTaxCatName();
      Boolean getIsActive();
      UUID getIdentity();
}
