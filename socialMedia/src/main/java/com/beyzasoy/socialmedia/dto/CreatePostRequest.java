package com.beyzasoy.socialmedia.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreatePostRequest {

    @NotBlank
    private String imageUrl;

    private String postDescription;

}