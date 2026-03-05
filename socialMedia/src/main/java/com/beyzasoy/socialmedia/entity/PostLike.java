package com.beyzasoy.socialmedia.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "post_like",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "post_id"}) //Aynı kullanıcı bir postu birden fazla beğenemez
)
public class PostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User likedBy;  //postu beğenen kullanıcı

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post likedPost; //beğenilen post
}
