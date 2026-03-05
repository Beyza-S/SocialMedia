package com.beyzasoy.socialmedia.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private String commentContent;
    private Long userId;
    private String username;
    private LocalDateTime createdAt;
}