package com.beyzasoy.socialmedia.controller;
import com.beyzasoy.socialmedia.dto.CommentResponse;
import com.beyzasoy.socialmedia.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {

        this.commentService = commentService;
    }

    private String extractToken(String authHeader) {
        return authHeader.replace("Bearer ", "");
    }

    @PostMapping("/posts/{id}/comments")
    public void addComment(@RequestHeader("Authorization") String authHeader,
                           @PathVariable Long id,
                           @RequestBody String content) {

        String token = extractToken(authHeader);

        commentService.addComment(token, id, content);
    }

    //Belirli bir posttaki yorumları getir
    @GetMapping("/posts/{id}/comments")
    public List<CommentResponse> getComments(@PathVariable Long id) {

        return commentService.getComments(id);
    }

    @DeleteMapping("/comments/{id}")
    public void deleteComment(@RequestHeader("Authorization") String authHeader,
                              @PathVariable Long id) {

        String token = extractToken(authHeader);

        commentService.deleteComment(token, id);
    }
}