package com.beyzasoy.socialmedia.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public class AuthResponse {   //Token verisi serverden client a gönderildiği için response
                              //Login yapıldığında tokenve expiry döner
    private String accessToken;
    private LocalDateTime expiresAt;  //zamanı ne zaman bitecek geçersiz olacak token

}