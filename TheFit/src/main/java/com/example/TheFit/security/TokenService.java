package com.example.TheFit.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TokenService {
    @Value("${jwt.secretKey}")
    private String secretKey;
    @Value("${jwt.expiration}")
    private Integer expiration;
    @Autowired
    private final RedisTemplate<String,String> redisTemplate1;
    public TokenService(RedisTemplate<String, String> redisTemplate1) {
        this.redisTemplate1 = redisTemplate1;
    }

    public String createAccessToken(String email,String name,String role){
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("userName",name);
        claims.put("role",role);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+expiration*60*1000L))
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();
    }

    public String createRefreshToken(String email){
        String refreshTokenValue = UUID.randomUUID().toString();
        RefreshToken refreshToken = new RefreshToken(email,refreshTokenValue);
        final ValueOperations<String,String> valueOperations = redisTemplate1.opsForValue();
        valueOperations.set(email,refreshToken.getRefreshTokenValue());
        return refreshTokenValue;
    }

    public Map<String,Object> decodeAccessTokenPayload(String token) throws JsonProcessingException {
        String[] values = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String payload = new String(decoder.decode(values[1]));
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,Object> map = objectMapper.readValue(payload,Map.class);
        return map;
    }
}
