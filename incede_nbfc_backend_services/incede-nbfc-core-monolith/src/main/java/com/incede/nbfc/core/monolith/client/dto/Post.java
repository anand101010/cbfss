package com.incede.nbfc.core.monolith.client.dto;

import lombok.Data;

/**
 * DTO for Post from JSONPlaceholder API
 */
@Data
public class Post {
    private Long id;
    private Long userId;
    private String title;
    private String body;
} 