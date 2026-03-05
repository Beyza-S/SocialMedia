package com.beyzasoy.socialmedia.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)   // Token hangi kullanıcıya ait
    private User user;

    @Column(nullable = false)
    private LocalDateTime tokenExpiryDate;   //Token bitme süresini tutuyorum

    @Column(nullable = false)
    private boolean isActive;
}