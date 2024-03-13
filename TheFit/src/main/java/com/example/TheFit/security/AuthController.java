package com.example.TheFit.security;

import com.example.TheFit.login.TmpResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {
    @Autowired
    private final TokenService tokenService;
    @Autowired
    private final RedisTemplate<String,Object> redisTemplate1;

    public AuthController(TokenService tokenService, RedisTemplate<String, Object> redisTemplate1) {
        this.tokenService = tokenService;
        this.redisTemplate1 = redisTemplate1;
    }

    @GetMapping("/reCreateAccessToken")
    public ResponseEntity<TmpResponse> reCreateAccessToken(@RequestHeader("Authorization") String accessToken,
                                                           @RequestHeader("refreshToken") String refreshToken) throws JsonProcessingException {
        Map<String, Object> memberInfo = tokenService.decodeAccessTokenPayload(accessToken);
        String email = memberInfo.get("sub").toString();
        String saveRefreshToken = null;
        ValueOperations<String,Object> valueOperations = redisTemplate1.opsForValue();
        try {
            saveRefreshToken = (String) valueOperations.get(email);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new TmpResponse(HttpStatus.BAD_REQUEST, "다시 로그인 해주세요", null), HttpStatus.BAD_REQUEST);
        }
        if (refreshToken != null && refreshToken.equals(saveRefreshToken)) {
            String newAccessToken = tokenService.createAccessToken(email, memberInfo.get("userName").toString(), memberInfo.get("role").toString());
            String newRefreshToken = tokenService.createRefreshToken(email);
            Map<String, Object> newMemberInfo = new HashMap<>();
            newMemberInfo.put("token", newAccessToken);
            newMemberInfo.put("refreshToken", newRefreshToken);
            return new ResponseEntity<>(new TmpResponse(HttpStatus.OK, "login success", newMemberInfo), HttpStatus.OK);
        }
        redisTemplate1.delete(email);
        return new ResponseEntity<>(new TmpResponse(HttpStatus.BAD_REQUEST, "다시 로그인 해주세요", null), HttpStatus.BAD_REQUEST);
    }
}
