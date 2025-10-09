package com.incede.nbfc.core.monolith.client.dto;

import lombok.Data;

/**
 * DTO for User from JSONPlaceholder API
 */
@Data
public class User {
    private Long id;
    private String name;
    private String username;
    private String email;
    private String phone;
    private String website;
} 