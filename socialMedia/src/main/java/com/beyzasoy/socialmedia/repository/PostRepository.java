package com.beyzasoy.socialmedia.repository;

import com.beyzasoy.socialmedia.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}