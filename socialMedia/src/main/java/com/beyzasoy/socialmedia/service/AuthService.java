package com.beyzasoy.socialmedia.service;

import com.beyzasoy.socialmedia.dto.*;
import com.beyzasoy.socialmedia.entity.*;
import com.beyzasoy.socialmedia.exception.ApiException;
import com.beyzasoy.socialmedia.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordService passwordService;

    //Constructor Injection
    public AuthService(UserRepository userRepository,
                       TokenRepository tokenRepository,
                       PasswordService passwordService) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordService = passwordService;
    }

    public void signup(SignupRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ApiException(HttpStatus.CONFLICT, "Username already exists");
        }

        //Kullanıcı yoksa DTO-Entity map lemesi oluşur.
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordService.hash(request.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request) {   //Login yapılınca clienta dönecek token işlemleri=AuthResponse, user işlemleri=LoginRequest

        User user = userRepository.findByUsername(request.getUsername())  //Optional method
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "User not found"));

        if (!passwordService.matches(request.getPassword(), user.getPassword())) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Wrong password");
        }

        //Token süresi 60 dk expiry ile dönüş yapıyorsun
        String tokenValue = UUID.randomUUID().toString();
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(60);

        Token token = new Token();
        token.setToken(tokenValue);
        token.setUser(user);
        token.setTokenExpiryDate(expiry);
        token.setActive(true);

        tokenRepository.save(token);

        return new AuthResponse(tokenValue, expiry);
    }

    public void logout(String tokenValue) {

        Token token = tokenRepository.findByToken(tokenValue)
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Token not found"));

        token.setActive(false);   //logout yapınca token inaktif oldu
        tokenRepository.save(token);
    }

    public User validateToken(String rawToken) {   //Token aktif mi var mı

        Token token = tokenRepository.findByToken(rawToken)
                .orElseThrow(() ->
                        new ApiException(HttpStatus.UNAUTHORIZED, "Invalid token"));

        if (!token.isActive()) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Token is inactive");
        }

        //Defensive Programming
        if (token.getTokenExpiryDate() == null ||    //Token expiry date bozulmuş ve null olabilir güvenlik için inactive ederiz
                token.getTokenExpiryDate().isBefore(LocalDateTime.now())) {   //Son zamanı geçmiş tokenın

            token.setActive(false);
            tokenRepository.save(token);

            throw new ApiException(HttpStatus.UNAUTHORIZED, "Token expired");
        }

        return token.getUser();
    }
}