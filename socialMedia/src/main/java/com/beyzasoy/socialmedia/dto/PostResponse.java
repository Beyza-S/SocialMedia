package com.beyzasoy.socialmedia.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PostResponse {

    private Long id;
    private String imageUrl;
    private String postDescription;
    private String username;
    private LocalDateTime createdAt;
}