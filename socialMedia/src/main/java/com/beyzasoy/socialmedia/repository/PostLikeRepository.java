package com.beyzasoy.socialmedia.repository;

import com.beyzasoy.socialmedia.entity.Post;
import com.beyzasoy.socialmedia.entity.PostLike;
import com.beyzasoy.socialmedia.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    boolean existsByLikedByAndLikedPost(User likedBy, Post likedPost);  //Kullanıcı bu postu daha önce beğendi mi

    Optional<PostLike> findByLikedByAndLikedPost(User likedBy, Post likedPost); //Kullanıcı beğendiyse getir silicez Delete endpointi

}