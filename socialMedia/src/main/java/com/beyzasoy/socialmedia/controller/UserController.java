package com.beyzasoy.socialmedia.controller;

import com.beyzasoy.socialmedia.dto.UpdatePasswordRequest;
import com.beyzasoy.socialmedia.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {

        this.userService = userService;
    }

    private String extractToken(String authHeader) {
        return authHeader.replace("Bearer ", "");
    }


    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {

        String token = extractToken(authHeader);

        return ResponseEntity.ok(userService.getUserById(token, id));
    }

    //Kendi şifreni güncelle
    @PutMapping("/users/me/password")
    public ResponseEntity<Void> updatePassword(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody UpdatePasswordRequest request) {

        String token = extractToken(authHeader);

        userService.updatePassword(
                token,
                request.getCurrentPassword(),
                request.getNewPassword()
        );

        return ResponseEntity.noContent().build();
    }

    //Kendi hesabını sil
    @DeleteMapping("/users/me")
    public ResponseEntity<Void> deleteMyAccount(
            @RequestHeader("Authorization") String authHeader) {

        String token = extractToken(authHeader);

        userService.deleteMyAccount(token);
        return ResponseEntity.noContent().build();
    }

    //Admin user ı siler
    @DeleteMapping("/admin/users/{id}")
    public ResponseEntity<Void> deleteUserByAdmin(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {

        String token = extractToken(authHeader);

        userService.deleteUserByAdmin(token, id);
        return ResponseEntity.noContent().build();
    }
}