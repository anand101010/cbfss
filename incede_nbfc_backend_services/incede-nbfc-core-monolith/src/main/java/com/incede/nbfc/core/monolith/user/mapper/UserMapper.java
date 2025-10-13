package com.incede.nbfc.core.monolith.user.mapper;

import com.incede.nbfc.core.monolith.user.domain.entity.User;
import com.incede.nbfc.core.monolith.user.dto.UserResponseDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponseDto toResponseDto(User user) {
        if (user == null) return null;
        UserResponseDto dto = new UserResponseDto();
        dto.setIdentity(user.getIdentity());
        dto.setUserId(user.getUserId());
        dto.setUserCode(user.getUserCode());
        dto.setUserName(user.getUserName());
        return dto;
    }
}
