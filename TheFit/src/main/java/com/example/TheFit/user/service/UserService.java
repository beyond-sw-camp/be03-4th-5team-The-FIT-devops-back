package com.example.TheFit.user.service;

import com.example.TheFit.common.ErrorCode;
import com.example.TheFit.common.TheFitBizException;
import com.example.TheFit.sse.FeedBackNotificationRes;
import com.example.TheFit.sse.SseController;
import com.example.TheFit.user.dto.UserIdPassword;
import com.example.TheFit.user.dto.UserLoginRequestDto;
import com.example.TheFit.user.repo.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final SseController sseController;
    @Qualifier("2")
    private final RedisTemplate<String,Object> redisTemplate;

    public UserService(UserRepository userRepository, SseController sseController, @Qualifier("2")RedisTemplate<String, Object> redisTemplate) {
        this.userRepository = userRepository;
        this.sseController = sseController;
        this.redisTemplate = redisTemplate;
    }

    public UserIdPassword login(UserLoginRequestDto userLoginRequestDto) {
        UserIdPassword userIdPassword = userRepository.findByEmail(userLoginRequestDto.getEmail()).orElseThrow(()->new TheFitBizException(ErrorCode.INCORRECT_ID));
        if(userIdPassword.delYn.equals("Y")){
            throw new TheFitBizException(ErrorCode.LEAVE_MEMBER);
        }
        if(!userIdPassword.getPassword().equals(userLoginRequestDto.getPassword())){
            throw new TheFitBizException(ErrorCode.INCORRECT_PASSWORD);
        }


        return userIdPassword;
    }
}
