package com.beyzasoy.socialmedia.controller;

import com.beyzasoy.socialmedia.dto.*;
import com.beyzasoy.socialmedia.service.PostService;
import com.beyzasoy.socialmedia.service.LikeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final LikeService likeService;

    public PostController(PostService postService,
                          LikeService likeService) {
        this.postService = postService;
        this.likeService = likeService;
    }

    private String extractToken(String authHeader) {
        return authHeader.replace("Bearer ", "");
    }


    @PostMapping
    public ResponseEntity<Void> createPost(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody CreatePostRequest request) {

        String token = extractToken(authHeader);

        postService.createPost(token, request);
        return ResponseEntity.status(201).build();
    }

    @GetMapping
    public List<PostResponse> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{id}")
    public PostDetailResponse getPostDetail(@PathVariable Long id) {
        return postService.getPostDetail(id);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePost(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id,
            @Valid @RequestBody UpdatePostRequest request) {

        String token = extractToken(authHeader);

        postService.updatePost(token, id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {

        String token = extractToken(authHeader);

        postService.deletePost(token, id);
        return ResponseEntity.noContent().build();
    }

    // Postu beğen
    @PostMapping("/{id}/likes")
    public void likePost(@RequestHeader("Authorization") String authHeader,
                         @PathVariable Long id) {

        String token = extractToken(authHeader);

        likeService.likePost(token, id);
    }

    // Post beğenisini geri al
    @DeleteMapping("/{id}/likes")
    public void unlikePost(@RequestHeader("Authorization") String authHeader,
                           @PathVariable Long id) {

        String token = extractToken(authHeader);

        likeService.unlikePost(token, id);
    }

    // Postu görüntüle
    @PostMapping("/{id}/view")
    public void viewPost(@RequestHeader("Authorization") String authHeader,
                         @PathVariable Long id) {

        String token = extractToken(authHeader);

        postService.viewPost(token, id);
    }
}