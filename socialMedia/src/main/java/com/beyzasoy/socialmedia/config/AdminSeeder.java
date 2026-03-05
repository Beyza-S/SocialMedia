package com.beyzasoy.socialmedia.config;

import com.beyzasoy.socialmedia.entity.Role;
import com.beyzasoy.socialmedia.entity.User;
import com.beyzasoy.socialmedia.repository.UserRepository;
import com.beyzasoy.socialmedia.service.PasswordService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordService passwordService;

    public AdminSeeder(UserRepository userRepository,
                       PasswordService passwordService) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
    }

    @Override
    public void run(String... args) {

        if (userRepository.existsByUsername("admin")) {
            return;
        }

        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordService.hash("admin12345"));
        admin.setRole(Role.ADMIN);

        userRepository.save(admin);

        System.out.println("Default ADMIN created.");
    }
}