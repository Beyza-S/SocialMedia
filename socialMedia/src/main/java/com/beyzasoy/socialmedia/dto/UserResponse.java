package com.beyzasoy.socialmedia.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {  //Kullanıcının verilerini ona göstermek için döndüğünden dolayı Response
                                    //GET /api/users/{id} endpointi
    private Long id;
    private String username;
    private String role;


}