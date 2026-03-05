package com.beyzasoy.socialmedia.service;

import com.beyzasoy.socialmedia.dto.CommentResponse;
import com.beyzasoy.socialmedia.entity.*;
import com.beyzasoy.socialmedia.exception.ApiException;
import com.beyzasoy.socialmedia.repository.CommentRepository;
import com.beyzasoy.socialmedia.repository.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class CommentService {

    private final AuthService authService;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public CommentService(AuthService authService,
                          PostRepository postRepository,
                          CommentRepository commentRepository) {
        this.authService = authService;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @Transactional
    public void addComment(String token, Long postId, String content) {

        User user = authService.validateToken(token);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Post not found"));

        Comment comment = new Comment();
        comment.setCommentContent(content);
        comment.setCommentUser(user); //Yorumu yazan kişi
        comment.setCommentPost(post); //Yorum hangi posta yapıldı

        commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getComments(Long postId) {

        if (!postRepository.existsById(postId)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Post not found");
        }

        return commentRepository.findByCommentPostId(postId)
                .stream()
                .map(comment -> new CommentResponse(
                        comment.getId(),
                        comment.getCommentContent(),
                        comment.getCommentUser().getId(),
                        comment.getCommentUser().getUsername(),
                        comment.getCreatedAt()
                ))
                .toList();
    }

    @Transactional
    public void deleteComment(String token, Long commentId) {

        User currentUser = authService.validateToken(token);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Comment not found"));

        boolean isAdmin = currentUser.getRole() == Role.ADMIN;
        boolean isOwner = comment.getCommentUser().getId().equals(currentUser.getId());
        boolean isPostOwner = comment.getCommentPost().getUser().getId().equals(currentUser.getId());

        if (!(isAdmin || isOwner || isPostOwner)) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Cannot delete this comment");
        }

        commentRepository.delete(comment);
    }
}