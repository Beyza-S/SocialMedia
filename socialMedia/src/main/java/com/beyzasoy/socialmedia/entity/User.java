package com.beyzasoy.socialmedia.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)   // Kullanıcı adı benzersiz olacak
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)      // Rol (admin / user)
    @Column(nullable = false)
    private Role role;

    private LocalDateTime createdAt = LocalDateTime.now();  // Oluşturulma tarihi

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true) //Kullanıcının postları da olsa kullanıcıyı silebiliriz
    private List<Post> posts;
}
