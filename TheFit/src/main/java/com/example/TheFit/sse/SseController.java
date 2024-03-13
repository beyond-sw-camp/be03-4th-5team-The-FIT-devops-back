package com.example.TheFit.sse;

import com.example.TheFit.common.ErrorCode;
import com.example.TheFit.common.TheFitBizException;
import com.example.TheFit.redis.RedisPublisher;
import com.example.TheFit.user.dto.UserIdPassword;
import com.example.TheFit.user.entity.Role;
import com.example.TheFit.user.member.domain.Member;
import com.example.TheFit.user.repo.UserRepository;
import com.example.TheFit.user.trainer.domain.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SseController {
    private final SseEmittersService sseEmittersService;
    private final UserRepository userRepository;
    private final RedisPublisher redisPublisher;
    @Qualifier("2")
    private final RedisTemplate<String,Object> redisTemplate2;
    @Autowired
    public SseController(SseEmittersService sseEmitter, UserRepository userRepository, RedisPublisher redisPublisher, @Qualifier("2") RedisTemplate<String, Object> redisTemplate2) {
        this.sseEmittersService = sseEmitter;
        this.userRepository = userRepository;
        this.redisPublisher = redisPublisher;
        this.redisTemplate2 = redisTemplate2;
    }


    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect(){
        //        만료시간 설정
        SseEmitter emitter = new SseEmitter(30*60*1000L);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        sseEmittersService.add(authentication.getName(),emitter);
        try {
            emitter.send(SseEmitter.event().name("connect").data("connected!"));
        }catch (IOException e){
            //TODO : 추후 Error 코드 변경
            throw new TheFitBizException(ErrorCode.S3_SERVER_ERROR);
        }
        if(Boolean.TRUE.equals(redisTemplate2.hasKey(authentication.getName()))){
            ListOperations<String,Object> valueOperations = redisTemplate2.opsForList();
            System.out.println(valueOperations.size(authentication.getName()));
            Long size = valueOperations.size(authentication.getName());
            List<FeedBackNotificationRes> feedBackNotificationResList = valueOperations.range(authentication.getName(),0,size-1)
                    .stream()
                    .map(a->(FeedBackNotificationRes)a)
                    .collect(Collectors.toList());
            UserIdPassword userIdPassword = userRepository.findByEmail(authentication.getName()).orElseThrow(
                    ()-> new TheFitBizException(ErrorCode.NOT_FOUND_MEMBER));
            if(userIdPassword.getRole().equals(Role.MEMBER)){
                for(FeedBackNotificationRes feedBackNotificationRes : feedBackNotificationResList) {
                    sendLastInfoToMember(authentication.getName(), feedBackNotificationRes);
                }
            }else if(userIdPassword.getRole().equals(Role.TRAINER)){
                for(FeedBackNotificationRes feedBackNotificationRes : feedBackNotificationResList) {
                    sendLastInfoToTrainer(authentication.getName(), feedBackNotificationRes);
                }
            }
            redisTemplate2.delete(authentication.getName());
        }
        return ResponseEntity.ok(emitter);
    }

    public void sendToMember(Member member, String tag, String uploadData, String trainerName) {
        ChannelTopic channel = new ChannelTopic(member.getEmail());
        String message = tag+"_"+uploadData+"_"+trainerName+"_"+member.getEmail()+"_sendToMember";
        redisPublisher.publish(channel, message);
    }
    public void sendLastInfoToMember(String email,FeedBackNotificationRes feedBackNotificationRes) {
        try {
            sseEmittersService.get(email).send(SseEmitter.event().name("sendToMember").data(feedBackNotificationRes));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendToTrainer(Trainer trainer, String type,String uploadDate,String memberName) {
        ChannelTopic channel = new ChannelTopic(trainer.getEmail());
        String message = type+"_"+uploadDate+"_"+memberName+"_"+trainer.getEmail()+"_sendToTrainer";
        redisPublisher.publish(channel, message);
    }
    public void sendLastInfoToTrainer(String email, FeedBackNotificationRes feedBackNotificationRes) {
        if(sseEmittersService.containKey(email)){
            try {
                sseEmittersService.get(email).send(SseEmitter.event().name("sendToTrainer").data(feedBackNotificationRes));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}