package com.incede.nbfc.core.monolith.masterdata.mapper;



import com.incede.nbfc.core.monolith.masterdata.domain.entity.AdditionalReferenceConfig;
import com.incede.nbfc.core.monolith.masterdata.dto.AdditionalReferenceConfigDto;

public class AdditionalReferenceConfigMapper {

    public static AdditionalReferenceConfigDto toDto(AdditionalReferenceConfig entity) {
        if (entity == null) return null;

        return new AdditionalReferenceConfigDto(
                entity.getIdentity(),
                entity.getReferenceFieldName(),
                entity.getReferenceFieldCode(),
                entity.getDataType(),
                entity.getMaxLength(),        // now properly populated
                entity.getPlaceholderText(),  // now properly populated
                entity.getHelpText(),         // now properly populated
                entity.getSortOrder(),        // now properly populated
                entity.getIsMandatory(),
                entity.getIsActive()
        );
    }

}
