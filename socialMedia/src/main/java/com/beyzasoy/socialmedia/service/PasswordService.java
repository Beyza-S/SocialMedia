package com.beyzasoy.socialmedia.service;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {

    public String hash(String password) {   //Şifreyi eşsiz şekilde hashler

        return BCrypt.hashpw(password, BCrypt.gensalt());

    }

    public boolean matches(String raw, String hashed) {

        return BCrypt.checkpw(raw, hashed);

    }
}