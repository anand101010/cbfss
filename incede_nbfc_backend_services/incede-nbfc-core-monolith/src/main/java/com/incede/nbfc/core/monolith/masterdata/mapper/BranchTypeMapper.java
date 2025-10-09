package com.incede.nbfc.core.monolith.masterdata.mapper;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.BranchTypes;
import com.incede.nbfc.core.monolith.masterdata.dto.BranchTypeDto;
import org.springframework.stereotype.Component;

@Component
public class BranchTypeMapper {

    public BranchTypeDto convertToDto(BranchTypes branchTypes) {
        BranchTypeDto dto = new BranchTypeDto();
        dto.setBranchTypeId(branchTypes.getBranchTypeId());
        dto.setName(branchTypes.getName());
        dto.setDescription(branchTypes.getDescription());
        dto.setCode(branchTypes.getCode());
        dto.setIdentity(branchTypes.getIdentity());
        dto.setIsActive(branchTypes.getIsActive());
        return dto;
    }

}
