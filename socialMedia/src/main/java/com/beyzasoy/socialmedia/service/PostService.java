package com.beyzasoy.socialmedia.service;

import com.beyzasoy.socialmedia.dto.*;
import com.beyzasoy.socialmedia.entity.*;
import com.beyzasoy.socialmedia.exception.ApiException;
import com.beyzasoy.socialmedia.repository.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final AuthService authService;

    public PostService(PostRepository postRepository,
                       AuthService authService) {
        this.postRepository = postRepository;
        this.authService = authService;
    }

    public void createPost(String token, CreatePostRequest request) {

        User user = authService.validateToken(token);

        Post post = new Post();
        post.setImageUrl(request.getImageUrl());
        post.setPostDescription(request.getPostDescription());
        post.setUser(user);

        postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {

        return postRepository.findAll()
                .stream()
                .map(post -> new PostResponse(
                        post.getId(),
                        post.getImageUrl(),
                        post.getPostDescription(),
                        post.getUser().getUsername(),
                        post.getCreatedAt()
                ))
                .toList();
    }

    public PostDetailResponse getPostDetail(Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new ApiException(HttpStatus.NOT_FOUND, "Post not found"));

        return new PostDetailResponse(
                post.getId(),
                post.getImageUrl(),
                post.getPostDescription(),
                post.getUser().getId(),
                post.getUser().getUsername(),
                post.getPostLikeCount(),
                post.getPostViewCount(),
                post.getComments().size(),
                post.getComments()
                        .stream()
                        .map(c -> new CommentResponse(
                                c.getId(),
                                c.getCommentContent(),
                                c.getCommentUser().getId(),
                                c.getCommentUser().getUsername(),
                                c.getCreatedAt()
                        ))
                        .toList(),
                post.getCreatedAt()
        );
    }

    public void updatePost(String token, Long postId, UpdatePostRequest request) {

        User currentUser = authService.validateToken(token);

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new ApiException(HttpStatus.NOT_FOUND, "Post not found"));

        if (currentUser.getRole() == Role.ADMIN ||
                post.getUser().getId().equals(currentUser.getId())) {

            post.setImageUrl(request.getImageUrl());
            post.setPostDescription(request.getPostDescription());
            return;
        }

        throw new ApiException(HttpStatus.FORBIDDEN, "You cannot update this post");
    }

    public void deletePost(String token, Long postId) {

        User currentUser = authService.validateToken(token);

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new ApiException(HttpStatus.NOT_FOUND, "Post not found"));

        if (currentUser.getRole() == Role.ADMIN ||
                post.getUser().getId().equals(currentUser.getId())) {

            postRepository.delete(post);
            return;
        }

        throw new ApiException(HttpStatus.FORBIDDEN, "You cannot delete this post");
    }

    public void viewPost(String token, Long postId) {

        authService.validateToken(token);

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new ApiException(HttpStatus.NOT_FOUND, "Post not found"));

        post.setPostViewCount(post.getPostViewCount() + 1);
    }
}