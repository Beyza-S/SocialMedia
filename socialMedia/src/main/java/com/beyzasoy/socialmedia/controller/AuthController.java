package com.beyzasoy.socialmedia.controller;

import com.beyzasoy.socialmedia.dto.*;
import com.beyzasoy.socialmedia.entity.User;
import com.beyzasoy.socialmedia.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    private String extractToken(String authHeader) {
        return authHeader.replace("Bearer ", "");
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignupRequest request) {
        authService.signup(request);
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader) {

        String token = extractToken(authHeader);

        authService.logout(token);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public MeResponse me(@RequestHeader("Authorization") String authHeader) {

        String token = extractToken(authHeader);

        User user = authService.validateToken(token);

        return new MeResponse(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );
    }

}