package com.example.TheFit.security;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "email",timeToLive = 6000)
@Data
public class RefreshToken {
    @Id
    private String email;
    private String refreshTokenValue;

    public RefreshToken(String email,String refreshTokenValue){
        this.email = email;
        this.refreshTokenValue =refreshTokenValue;
    }
}
