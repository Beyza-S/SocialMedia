package com.beyzasoy.socialmedia.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateCommentRequest {

    @NotBlank
    private String commentContent;

    public String getCommentContent() {
        return commentContent;
    }
}