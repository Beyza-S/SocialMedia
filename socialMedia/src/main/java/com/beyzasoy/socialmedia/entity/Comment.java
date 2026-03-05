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
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String commentContent;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User commentUser; //yorumu yapan kullanıcı

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post commentPost;  //yorumun yapıldığı post

    private LocalDateTime createdAt = LocalDateTime.now();

}