package com.incede.nbfc.core.monolith.fallback;

import com.incede.nbfc.core.monolith.client.dto.Post;
import com.incede.nbfc.core.monolith.client.dto.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Fallback implementation for PostApiClient
 * Provides default responses when the external API is unavailable
 * Demonstrates fallback mechanism support in Spring Boot 3.5.4
 */
@Slf4j
@Component
public class PostApiFallback  {


    public List<Post> getAllPosts() {
        log.warn("Post API unavailable, returning fallback response for getAllPosts");
        
        Post fallbackPost = new Post();
        fallbackPost.setId(999L);
        fallbackPost.setUserId(999L);
        fallbackPost.setTitle("Fallback Post - API Unavailable");
        fallbackPost.setBody("This is a fallback post generated when the external API is temporarily unavailable.");
        
        log.info("Fallback posts response generated");
        return List.of(fallbackPost);
    }

    // Fallback methods for circuit breaker
    public List<Post> getAllPostsFallback(Exception ex) {
        log.warn("Circuit breaker triggered for getAllPosts, returning fallback response. Exception: {}", ex.getMessage());
        
        Post fallbackPost = new Post();
        fallbackPost.setId(999L);
        fallbackPost.setUserId(999L);
        fallbackPost.setTitle("Circuit Breaker Fallback Post - API Unavailable");
        fallbackPost.setBody("This is a circuit breaker fallback post generated when the external API is temporarily unavailable.");
        
        log.info("Circuit breaker fallback posts response generated");
        return List.of(fallbackPost);
    }


    public Post getPostById(Long id) {
        log.warn("Post API unavailable, returning fallback response for post ID: {}", id);
        
        Post fallbackPost = new Post();
        fallbackPost.setId(id);
        fallbackPost.setUserId(999L);
        fallbackPost.setTitle("Fallback Post " + id + " - API Unavailable");
        fallbackPost.setBody("This is a fallback post for ID " + id + " generated when the external API is temporarily unavailable.");
        
        log.info("Fallback post response generated for ID: {}", id);
        return fallbackPost;
    }

    public Post getPostByIdFallback(Long id, Exception ex) {
        log.warn("Circuit breaker triggered for getPostById with ID: {}, returning fallback response. Exception: {}", id, ex.getMessage());
        
        Post fallbackPost = new Post();
        fallbackPost.setId(id);
        fallbackPost.setUserId(999L);
        fallbackPost.setTitle("Circuit Breaker Fallback Post " + id + " - API Unavailable");
        fallbackPost.setBody("This is a circuit breaker fallback post for ID " + id + " generated when the external API is temporarily unavailable.");
        
        log.info("Circuit breaker fallback post response generated for ID: {}", id);
        return fallbackPost;
    }


    public List<Post> getPostsByUserId(Long userId) {
        log.warn("Post API unavailable, returning fallback response for user ID: {}", userId);
        
        Post fallbackPost = new Post();
        fallbackPost.setId(999L);
        fallbackPost.setUserId(userId);
        fallbackPost.setTitle("Fallback Post for User " + userId + " - API Unavailable");
        fallbackPost.setBody("This is a fallback post for user " + userId + " generated when the external API is temporarily unavailable.");
        
        log.info("Fallback posts response generated for user ID: {}", userId);
        return List.of(fallbackPost);
    }

    public List<Post> getPostsByUserIdFallback(Long userId, Exception ex) {
        log.warn("Circuit breaker triggered for getPostsByUserId with user ID: {}, returning fallback response. Exception: {}", userId, ex.getMessage());
        
        Post fallbackPost = new Post();
        fallbackPost.setId(999L);
        fallbackPost.setUserId(userId);
        fallbackPost.setTitle("Circuit Breaker Fallback Post for User " + userId + " - API Unavailable");
        fallbackPost.setBody("This is a circuit breaker fallback post for user " + userId + " generated when the external API is temporarily unavailable.");
        
        log.info("Circuit breaker fallback posts response generated for user ID: {}", userId);
        return List.of(fallbackPost);
    }


    public User getUserById(Long id) {
        log.warn("User API unavailable, returning fallback response for user ID: {}", id);
        
        User fallbackUser = new User();
        fallbackUser.setId(id);
        fallbackUser.setName("Fallback User " + id);
        fallbackUser.setUsername("fallback_user_" + id);
        fallbackUser.setEmail("fallback" + id + "@example.com");
        fallbackUser.setPhone("+1-555-FALLBACK");
        fallbackUser.setWebsite("https://fallback.example.com");
        
        log.info("Fallback user response generated for ID: {}", id);
        return fallbackUser;
    }

    public User getUserByIdFallback(Long id, Exception ex) {
        log.warn("Circuit breaker triggered for getUserById with ID: {}, returning fallback response. Exception: {}", id, ex.getMessage());
        
        User fallbackUser = new User();
        fallbackUser.setId(id);
        fallbackUser.setName("Circuit Breaker Fallback User " + id);
        fallbackUser.setUsername("circuit_breaker_fallback_user_" + id);
        fallbackUser.setEmail("circuit_breaker_fallback" + id + "@example.com");
        fallbackUser.setPhone("+1-555-CIRCUIT-BREAKER");
        fallbackUser.setWebsite("https://circuit-breaker-fallback.example.com");
        
        log.info("Circuit breaker fallback user response generated for ID: {}", id);
        return fallbackUser;
    }


    public List<User> getAllUsers() {
        log.warn("User API unavailable, returning fallback response for getAllUsers");
        
        User fallbackUser = new User();
        fallbackUser.setId(999L);
        fallbackUser.setName("Fallback User - API Unavailable");
        fallbackUser.setUsername("fallback_user");
        fallbackUser.setEmail("fallback@example.com");
        fallbackUser.setPhone("+1-555-FALLBACK");
        fallbackUser.setWebsite("https://fallback.example.com");
        
        log.info("Fallback users response generated");
        return List.of(fallbackUser);
    }

    public List<User> getAllUsersFallback(Exception ex) {
        log.warn("Circuit breaker triggered for getAllUsers, returning fallback response. Exception: {}", ex.getMessage());
        
        User fallbackUser = new User();
        fallbackUser.setId(999L);
        fallbackUser.setName("Circuit Breaker Fallback User - API Unavailable");
        fallbackUser.setUsername("circuit_breaker_fallback_user");
        fallbackUser.setEmail("circuit_breaker_fallback@example.com");
        fallbackUser.setPhone("+1-555-CIRCUIT-BREAKER");
        fallbackUser.setWebsite("https://circuit-breaker-fallback.example.com");
        
        log.info("Circuit breaker fallback users response generated");
        return List.of(fallbackUser);
    }
} 