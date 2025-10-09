package com.incede.nbfc.core.monolith.masterdata.mapper;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.IfscCodes;
import com.incede.nbfc.core.monolith.masterdata.dto.BankDto;
import com.incede.nbfc.core.monolith.masterdata.dto.IfscCodesDto;
import org.springframework.stereotype.Component;

@Component
public class IfscCodeMapper {

    public IfscCodesDto convertToDto(IfscCodes entity) {

        return IfscCodesDto.builder()
                .ifscCode(entity.getIfscCode())
                .bankName(entity.getBank() != null ? entity.getBank().getName() : null)
                .branchName(entity.getBranchName())
                .branchPlace(entity.getBranchPlace())
                .rbiFlag(entity.getRbiFlag())
                .isActive(entity.getIsActive())
                .identity(entity.getIdentity())

                .build();
    }
}
