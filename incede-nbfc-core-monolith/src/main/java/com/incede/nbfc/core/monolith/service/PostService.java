//package com.incede.nbfc.core.monolith.service;
//
//import com.incede.nbfc.core.monolith.client.PostApiClient;
//import com.incede.nbfc.core.monolith.client.dto.Post;
//import com.incede.nbfc.core.monolith.client.dto.User;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
///**
// * Service for post and user operations using FeignClient
// * Demonstrates integration with free JSONPlaceholder API
// */
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class PostService {
//
//    private final PostApiClient postApiClient;
//
//    /**
//     * Get all posts
//     *
//     * @return List of all posts
//     */
//    public List<Post> getAllPosts() {
//        log.info("Fetching all posts from external API");
//
//        try {
//            List<Post> posts = postApiClient.getAllPosts();
//            log.info("Successfully retrieved {} posts", posts.size());
//            return posts;
//        } catch (Exception e) {
//            log.error("Error fetching all posts", e);
//            throw new RuntimeException("Failed to fetch posts", e);
//        }
//    }
//
//    /**
//     * Get post by ID
//     *
//     * @param id Post ID
//     * @return Post details
//     */
//    public Post getPostById(Long id) {
//        log.info("Fetching post with ID: {}", id);
//
//        try {
//            Post post = postApiClient.getPostById(id);
//            log.info("Successfully retrieved post: {}", post.getTitle());
//            return post;
//        } catch (Exception e) {
//            log.error("Error fetching post with ID: {}", id, e);
//            throw new RuntimeException("Failed to fetch post", e);
//        }
//    }
//
//    /**
//     * Get posts by user ID
//     *
//     * @param userId User ID
//     * @return List of posts by user
//     */
//    public List<Post> getPostsByUserId(Long userId) {
//        log.info("Fetching posts for user ID: {}", userId);
//
//        try {
//            List<Post> posts = postApiClient.getPostsByUserId(userId);
//            log.info("Successfully retrieved {} posts for user {}", posts.size(), userId);
//            return posts;
//        } catch (Exception e) {
//            log.error("Error fetching posts for user ID: {}", userId, e);
//            throw new RuntimeException("Failed to fetch user posts", e);
//        }
//    }
//
//    /**
//     * Get user by ID
//     *
//     * @param id User ID
//     * @return User details
//     */
//    public User getUserById(Long id) {
//        log.info("Fetching user with ID: {}", id);
//
//        try {
//            User user = postApiClient.getUserById(id);
//            log.info("Successfully retrieved user: {}", user.getName());
//            return user;
//        } catch (Exception e) {
//            log.error("Error fetching user with ID: {}", id, e);
//            throw new RuntimeException("Failed to fetch user", e);
//        }
//    }
//
//    /**
//     * Get all users
//     *
//     * @return List of all users
//     */
//    public List<User> getAllUsers() {
//        log.info("Fetching all users from external API");
//
//        try {
//            List<User> users = postApiClient.getAllUsers();
//            log.info("Successfully retrieved {} users", users.size());
//            return users;
//        } catch (Exception e) {
//            log.error("Error fetching all users", e);
//            throw new RuntimeException("Failed to fetch users", e);
//        }
//    }
//
//    /**
//     * Get post summary
//     *
//     * @param id Post ID
//     * @return Post summary
//     */
//    public String getPostSummary(Long id) {
//        try {
//            Post post = getPostById(id);
//            return String.format("Post %d: '%s' by User %d - %s",
//                    post.getId(),
//                    post.getTitle(),
//                    post.getUserId(),
//                    post.getBody().substring(0, Math.min(post.getBody().length(), 100)) + "...");
//        } catch (Exception e) {
//            log.error("Error getting post summary for ID: {}", id, e);
//            return "Unable to retrieve post summary for ID " + id;
//        }
//    }
//}