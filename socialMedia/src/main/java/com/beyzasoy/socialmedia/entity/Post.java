package com.beyzasoy.socialmedia.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String imageUrl;

    @Column(nullable = false,length = 2000)
    private String postDescription;

    @ManyToOne(fetch = FetchType.LAZY)  //Postlar gelirken gerek yoksa user gelmez
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private long postLikeCount = 0;   // primitive long seçtim çünkü null olamaz defult olarak 0 gelir

    @Column(nullable = false)
    private long postViewCount = 0;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Post silinince comment ve like da silinsin (FK sıkıntısı olmasın) İlişki comment trafında yönetiliyor öncelik o
    @OneToMany(mappedBy = "commentPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "likedPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostLike> likes = new ArrayList<>();
}