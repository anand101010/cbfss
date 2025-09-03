//package com.incede.nbfc.core.monolith.controller;
//
//import com.incede.nbfc.core.monolith.client.dto.Post;
//import com.incede.nbfc.core.monolith.client.dto.User;
//import com.incede.nbfc.core.monolith.service.PostService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
///**
// * REST Controller for post and user operations
// * Demonstrates FeignClient integration with free JSONPlaceholder API
// */
//@Slf4j
//@RestController
//@RequestMapping("/api/v1/posts")
//@RequiredArgsConstructor
//@Tag(name = "Post API", description = "Post and user information endpoints using FeignClient")
//public class PostController {
//
//    private final PostService postService;
//
//    /**
//     * Get all posts
//     *
//     * @return List of all posts
//     */
//    @GetMapping
//    @Operation(summary = "Get all posts", description = "Retrieve all posts from external API")
//    public ResponseEntity<List<Post>> getAllPosts() {
//        log.info("Get all posts request received");
//        List<Post> posts = postService.getAllPosts();
//        return ResponseEntity.ok(posts);
//    }
//
//    /**
//     * Get post by ID
//     *
//     * @param id Post ID
//     * @return Post details
//     */
//    @GetMapping("/{id}")
//    @Operation(summary = "Get post by ID", description = "Retrieve a specific post by its ID")
//    public ResponseEntity<Post> getPostById(
//            @Parameter(description = "Post ID", example = "1")
//            @PathVariable Long id) {
//
//        log.info("Get post request received for ID: {}", id);
//        Post post = postService.getPostById(id);
//        return ResponseEntity.ok(post);
//    }
//
//    /**
//     * Get posts by user ID
//     *
//     * @param userId User ID
//     * @return List of posts by user
//     */
//    @GetMapping("/user/{userId}")
//    @Operation(summary = "Get posts by user", description = "Retrieve all posts by a specific user")
//    public ResponseEntity<List<Post>> getPostsByUserId(
//            @Parameter(description = "User ID", example = "1")
//            @PathVariable Long userId) {
//
//        log.info("Get posts by user request received for user ID: {}", userId);
//        List<Post> posts = postService.getPostsByUserId(userId);
//        return ResponseEntity.ok(posts);
//    }
//
//    /**
//     * Get user by ID
//     *
//     * @param id User ID
//     * @return User details
//     */
//    @GetMapping("/users/{id}")
//    @Operation(summary = "Get user by ID", description = "Retrieve a specific user by their ID")
//    public ResponseEntity<User> getUserById(
//            @Parameter(description = "User ID", example = "1")
//            @PathVariable Long id) {
//
//        log.info("Get user request received for ID: {}", id);
//        User user = postService.getUserById(id);
//        return ResponseEntity.ok(user);
//    }
//
//    /**
//     * Get all users
//     *
//     * @return List of all users
//     */
//    @GetMapping("/users")
//    @Operation(summary = "Get all users", description = "Retrieve all users from external API")
//    public ResponseEntity<List<User>> getAllUsers() {
//        log.info("Get all users request received");
//        List<User> users = postService.getAllUsers();
//        return ResponseEntity.ok(users);
//    }
//
//    /**
//     * Get post summary
//     *
//     * @param id Post ID
//     * @return Post summary as text
//     */
//    @GetMapping("/{id}/summary")
//    @Operation(summary = "Get post summary", description = "Retrieve a human-readable summary of a specific post")
//    public ResponseEntity<String> getPostSummary(
//            @Parameter(description = "Post ID", example = "1")
//            @PathVariable Long id) {
//
//        log.info("Post summary request received for ID: {}", id);
//        String summary = postService.getPostSummary(id);
//        return ResponseEntity.ok(summary);
//    }
//
//    /**
//     * Health check endpoint for post service
//     *
//     * @return Service status
//     */
//    @GetMapping("/health")
//    @Operation(summary = "Health check", description = "Check the health status of the post service")
//    public ResponseEntity<String> healthCheck() {
//        log.debug("Health check request received");
//        return ResponseEntity.ok("Post service is healthy and ready to serve requests");
//    }
//}