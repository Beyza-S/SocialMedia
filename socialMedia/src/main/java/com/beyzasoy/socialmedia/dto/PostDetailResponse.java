package com.beyzasoy.socialmedia.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class PostDetailResponse {

    private Long id;
    private String imageUrl;
    private String postDescription;
    private Long userId;
    private String username;
    private long likeCount;
    private long viewCount;
    private int commentCount;
    private List<CommentResponse> comments;
    private LocalDateTime createdAt;
}