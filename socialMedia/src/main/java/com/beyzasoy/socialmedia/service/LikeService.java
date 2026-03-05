package com.beyzasoy.socialmedia.service;

import com.beyzasoy.socialmedia.entity.*;
import com.beyzasoy.socialmedia.exception.ApiException;
import com.beyzasoy.socialmedia.repository.PostLikeRepository;
import com.beyzasoy.socialmedia.repository.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeService {

    private final AuthService authService;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;

    public LikeService(AuthService authService,
                       PostRepository postRepository,
                       PostLikeRepository postLikeRepository) {
        this.authService = authService;
        this.postRepository = postRepository;
        this.postLikeRepository = postLikeRepository;
    }

    @Transactional
    public void likePost(String token, Long postId) {

        User user = authService.validateToken(token);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Post not found"));

        if (postLikeRepository.existsByLikedByAndLikedPost(user, post)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Already liked");
        }

        PostLike like = new PostLike();
        like.setLikedBy(user);
        like.setLikedPost(post);

        postLikeRepository.save(like);

        post.setPostLikeCount(post.getPostLikeCount() + 1);
        postRepository.save(post);
    }

    @Transactional
    public void unlikePost(String token, Long postId) {

        User user = authService.validateToken(token);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Post not found"));

        PostLike like = postLikeRepository
                .findByLikedByAndLikedPost(user, post)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Like not found"));

        postLikeRepository.delete(like);

        post.setPostLikeCount(Math.max(post.getPostLikeCount() - 1, 0));
        postRepository.save(post);
    }
}