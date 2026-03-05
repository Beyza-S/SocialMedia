package com.beyzasoy.socialmedia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter

public class SignupRequest {

    @NotBlank(message = "Username boş olamaz")
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank(message = "Password boş olamaz")
    @Size(min = 6)
    private String password;

}