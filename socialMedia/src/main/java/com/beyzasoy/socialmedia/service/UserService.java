package com.beyzasoy.socialmedia.service;

import com.beyzasoy.socialmedia.dto.UserResponse;
import com.beyzasoy.socialmedia.entity.Role;
import com.beyzasoy.socialmedia.entity.Token;
import com.beyzasoy.socialmedia.entity.User;
import com.beyzasoy.socialmedia.exception.ApiException;
import com.beyzasoy.socialmedia.repository.TokenRepository;
import com.beyzasoy.socialmedia.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final AuthService authService;
    private final PasswordService passwordService;

    public UserService(UserRepository userRepository,
                       TokenRepository tokenRepository,
                       AuthService authService,
                       PasswordService passwordService) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.authService = authService;
        this.passwordService = passwordService;
    }

    public UserResponse getUserById(String token, Long id) {

        authService.validateToken(token); //Herhangi birisinin tokenı (Herkes istediği kişiyi bulabilir)

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ApiException(HttpStatus.NOT_FOUND, "User not found"));

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getRole().name()
        );
    }

    public void deleteMyAccount(String token) {

        User currentUser = authService.validateToken(token); //Silme işlemini yapacak kişinin tokenı(Sadece kendisi silebilir)

        tokenRepository.deleteByUser(currentUser); //FK hatası almamak için önce kendi tokenları silinir

        userRepository.delete(currentUser); //Sonra kullanıcı silinir
    }

    public void deleteUserByAdmin(String token, Long userId) {

        User currentUser = authService.validateToken(token);

        if (currentUser.getRole() != Role.ADMIN) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Only ADMIN can delete users");
        }

        User userToDelete = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ApiException(HttpStatus.NOT_FOUND, "User not found"));

        tokenRepository.deleteByUser(userToDelete);

        userRepository.delete(userToDelete);
    }

    public void updatePassword(String token, String currentPassword, String newPassword) {

        User user = authService.validateToken(token);

        if (!passwordService.matches(currentPassword, user.getPassword())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Current password is wrong");
        }

        user.setPassword(passwordService.hash(newPassword));

        userRepository.save(user);
    }
}