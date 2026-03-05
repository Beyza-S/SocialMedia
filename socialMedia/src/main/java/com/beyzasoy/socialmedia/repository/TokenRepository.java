package com.beyzasoy.socialmedia.repository;

import com.beyzasoy.socialmedia.entity.Token;
import com.beyzasoy.socialmedia.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByToken(String token);  //Verilen String tokena sahip token ı bul getir
    void deleteByUser(User user);   //Kullanıcının tokenlarını sil
}