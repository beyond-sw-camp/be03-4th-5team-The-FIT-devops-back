package com.example.TheFit.sse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class SseEmittersService {
    private static final AtomicLong counter = new AtomicLong();
    private final EmitterRepository emitterRepository;

    public SseEmittersService(EmitterRepository emitterRepository) {
        this.emitterRepository = emitterRepository;
    }

    SseEmitter add(String email,SseEmitter emitter){
        emitterRepository.save(email,emitter);
        emitter.onCompletion(()->{
            emitterRepository.deleteByEmail(email);
        });
        emitter.onTimeout(()->{
            emitterRepository.get(email).complete();
        });
        return emitter;
    }

    SseEmitter get(String email){
        return emitterRepository.get(email);
    }

    boolean containKey(String email){
        return emitterRepository.containKey(email);
    }
}
