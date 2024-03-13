package com.example.TheFit.redis;

import com.example.TheFit.sse.EmitterRepository;
import com.example.TheFit.sse.FeedBackNotificationRes;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class RedisSubscriber implements MessageListener {
    private final ObjectMapper objectMapper;
    @Qualifier("3")
    private final RedisTemplate<String, String> redisTemplate;
    private final EmitterRepository emitterRepository;

    public RedisSubscriber(ObjectMapper objectMapper, RedisTemplate<String, String> redisTemplate, EmitterRepository emitterRepository) {
        this.objectMapper = objectMapper;
        this.redisTemplate = redisTemplate;
        this.emitterRepository = emitterRepository;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String[] strings = message.toString().split("_");
            String type =strings[0];
            String uploadDate = strings[1];
            String name =strings[2];
            String email = strings[3];
            String category = strings[4];
            SseEmitter emitter =emitterRepository.get(email);
            FeedBackNotificationRes feedBackNotificationRes =new FeedBackNotificationRes(email,type,uploadDate,name);
            if(category.equals("sendToMember")){
                emitter.send(SseEmitter.event().name("sendToMember").data(feedBackNotificationRes));
            }else{
                emitter.send(SseEmitter.event().name("sendToTrainer").data(feedBackNotificationRes));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}