package com.beyzasoy.socialmedia.repository;

import com.beyzasoy.socialmedia.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);   //Optinial=Bu kullanıcı boş olabilir bakmadan geçme null kontrolü zorunlu

}