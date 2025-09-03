//package com.incede.nbfc.core.monolith.client;
//
//import com.incede.nbfc.core.monolith.config.FeignConfiguration;
//import com.incede.nbfc.core.monolith.fallback.PostApiFallback;
//import com.incede.nbfc.core.monolith.client.dto.Post;
//import com.incede.nbfc.core.monolith.client.dto.User;
//import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
///**
// * Feign client for free JSONPlaceholder API
// * Demonstrates FeignClient configuration with fallback support
// * Uses: https://jsonplaceholder.typicode.com (completely free, no API key required)
// */
//@FeignClient(
//    name = "post-api",
//    url = "${post.api.base-url:https://jsonplaceholder.typicode.com}",
//    configuration = FeignConfiguration.class,
//    fallback = PostApiFallback.class
//)
//public interface PostApiClient {
//
//    /**
//     * Get all posts
//     *
//     * @return List of all posts
//     */
//    @GetMapping("/posts")
//    @CircuitBreaker(name = "post-api", fallbackMethod = "getAllPostsFallback")
//    List<Post> getAllPosts();
//
//    /**
//     * Get post by ID
//     *
//     * @param id Post ID
//     * @return Post details
//     */
//    @GetMapping("/posts/{id}")
//    @CircuitBreaker(name = "post-api", fallbackMethod = "getPostByIdFallback")
//    Post getPostById(@PathVariable("id") Long id);
//
//    /**
//     * Get posts by user ID
//     *
//     * @param userId User ID
//     * @return List of posts by user
//     */
//    @GetMapping("/posts")
//    @CircuitBreaker(name = "post-api", fallbackMethod = "getPostsByUserIdFallback")
//    List<Post> getPostsByUserId(@RequestParam("userId") Long userId);
//
//    /**
//     * Get user by ID
//     *
//     * @param id User ID
//     * @return User details
//     */
//    @GetMapping("/users/{id}")
//    @CircuitBreaker(name = "post-api", fallbackMethod = "getUserByIdFallback")
//    User getUserById(@PathVariable("id") Long id);
//
//    /**
//     * Get all users
//     *
//     * @return List of all users
//     */
//    @GetMapping("/users")
//    @CircuitBreaker(name = "post-api", fallbackMethod = "getAllUsersFallback")
//    List<User> getAllUsers();
//
//    // ========== FALLBACK METHODS ==========
//    // These methods are called by the circuit breaker when the main methods fail
//
//    /**
//     * Fallback method for getAllPosts
//     */
//    default List<Post> getAllPostsFallback(Throwable ex) {
//        Post fallbackPost = new Post();
//        fallbackPost.setId(999L);
//        fallbackPost.setUserId(999L);
//        fallbackPost.setTitle("Circuit Breaker Fallback Post - API Unavailable");
//        fallbackPost.setBody("This is a circuit breaker fallback post generated when the external API is temporarily unavailable.");
//        return List.of(fallbackPost);
//    }
//
//    /**
//     * Fallback method for getPostById
//     */
//    default Post getPostByIdFallback(Long id, Throwable ex) {
//        Post fallbackPost = new Post();
//        fallbackPost.setId(id);
//        fallbackPost.setUserId(999L);
//        fallbackPost.setTitle("Circuit Breaker Fallback Post " + id + " - API Unavailable");
//        fallbackPost.setBody("This is a circuit breaker fallback post for ID " + id + " generated when the external API is temporarily unavailable.");
//        return fallbackPost;
//    }
//
//    /**
//     * Fallback method for getPostsByUserId
//     */
//    default List<Post> getPostsByUserIdFallback(Long userId, Throwable ex) {
//        Post fallbackPost = new Post();
//        fallbackPost.setId(999L);
//        fallbackPost.setUserId(userId);
//        fallbackPost.setTitle("Circuit Breaker Fallback Post for User " + userId + " - API Unavailable");
//        fallbackPost.setBody("This is a circuit breaker fallback post for user " + userId + " generated when the external API is temporarily unavailable.");
//        return List.of(fallbackPost);
//    }
//
//    /**
//     * Fallback method for getUserById
//     */
//    default User getUserByIdFallback(Long id, Throwable ex) {
//        User fallbackUser = new User();
//        fallbackUser.setId(id);
//        fallbackUser.setName("Circuit Breaker Fallback User " + id);
//        fallbackUser.setUsername("circuit_breaker_fallback_user_" + id);
//        fallbackUser.setEmail("circuit_breaker_fallback" + id + "@example.com");
//        fallbackUser.setPhone("+1-555-CIRCUIT-BREAKER");
//        fallbackUser.setWebsite("https://circuit-breaker-fallback.example.com");
//        return fallbackUser;
//    }
//
//    /**
//     * Fallback method for getAllUsers
//     */
//    default List<User> getAllUsersFallback(Throwable ex) {
//        User fallbackUser = new User();
//        fallbackUser.setId(999L);
//        fallbackUser.setName("Circuit Breaker Fallback User - API Unavailable");
//        fallbackUser.setUsername("circuit_breaker_fallback_user");
//        fallbackUser.setEmail("circuit_breaker_fallback@example.com");
//        fallbackUser.setPhone("+1-555-CIRCUIT-BREAKER");
//        fallbackUser.setWebsite("https://circuit-breaker-fallback.example.com");
//        return List.of(fallbackUser);
//    }
//}