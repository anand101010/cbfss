package com.incede.nbfc.core.monolith.masterdata.mapper;

import com.incede.nbfc.core.monolith.masterdata.domain.entity.States;
import com.incede.nbfc.core.monolith.masterdata.dto.StatesDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class StatesMapper {
    public StatesDto convertToDto(States states) {

        StatesDto dto = new StatesDto();
        dto.setState(states.getState());
        dto.setStateId(states.getStateId());
        dto.setIdentity(states.getIdentity());
        dto.setIsActive(states.getIsActive());
        return dto;
    }
}
