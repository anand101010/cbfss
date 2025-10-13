package com.incede.nbfc.core.monolith.user.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class UserResponseDto {
    private UUID identity;
    private Integer userId;
    private String userCode;
    private String userName;
}
