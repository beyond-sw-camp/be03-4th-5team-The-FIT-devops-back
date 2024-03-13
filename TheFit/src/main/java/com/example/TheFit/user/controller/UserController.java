package com.example.TheFit.user.controller;

import com.example.TheFit.common.TheFitResponse;
import com.example.TheFit.redis.RedisPublisher;
import com.example.TheFit.redis.RedisSubscriber;
import com.example.TheFit.security.TokenService;
import com.example.TheFit.sse.SseController;
import com.example.TheFit.user.service.UserService;
import com.example.TheFit.user.dto.UserIdPassword;
import com.example.TheFit.user.dto.UserLoginRequestDto;
import com.example.TheFit.user.member.service.MemberService;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    private final TokenService tokenService;
    private final UserService userService;
    private final RedisPublisher redisPublisher;
    private final RedisSubscriber redisSubscriber;
    private final RedisMessageListenerContainer redisMessageListener;

    public UserController(MemberService memberService, TokenService tokenService, UserService userService, RedisPublisher redisPublisher, RedisSubscriber redisSubscriber, RedisMessageListenerContainer redisMessageListener) {
        this.tokenService = tokenService;
        this.userService = userService;
        this.redisPublisher = redisPublisher;
        this.redisSubscriber = redisSubscriber;
        this.redisMessageListener = redisMessageListener;
    }

    @PostMapping("/dologin")
    public ResponseEntity<TheFitResponse> doLogin(@RequestBody UserLoginRequestDto userLoginRequestDto){
        UserIdPassword userIdPassword = userService.login(userLoginRequestDto);
        String accessToken = tokenService.createAccessToken(userIdPassword.getEmail(),userIdPassword.getName(),userIdPassword.getRole().toString());
        String refreshToken = tokenService.createRefreshToken(userIdPassword.getEmail());
        Map<String,Object> memberInfo = new HashMap<>();
        memberInfo.put("token",accessToken);
        memberInfo.put("refreshToken",refreshToken);
        ChannelTopic channel = new ChannelTopic(userIdPassword.getEmail());
        redisMessageListener.addMessageListener(redisSubscriber, channel);
        return new ResponseEntity<>(new TheFitResponse(HttpStatus.OK,"success login",memberInfo),HttpStatus.OK);
    }
}
