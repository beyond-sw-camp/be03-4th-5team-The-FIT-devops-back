package com.example.TheFit.sse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@RequiredArgsConstructor
public class EmitterRepository {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public void save(String email, SseEmitter emitter) {
        emitters.put(email, emitter);
    }

    public void deleteByEmail(String email) {
        emitters.remove(email);
    }

    public SseEmitter get(String email) {
        return emitters.get(email);
    }

    public boolean containKey(String email){return emitters.containsKey(email);}
}