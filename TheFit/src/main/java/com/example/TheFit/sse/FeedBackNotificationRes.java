package com.example.TheFit.sse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "Note")
@Data
public class FeedBackNotificationRes {
    @Id
    String email;
    String type;
    String date;
    String name;
}
