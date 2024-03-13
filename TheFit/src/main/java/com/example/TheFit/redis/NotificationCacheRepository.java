package com.example.TheFit.redis;

import com.example.TheFit.sse.FeedBackNotificationRes;
import org.springframework.data.repository.CrudRepository;


public interface NotificationCacheRepository extends CrudRepository<FeedBackNotificationRes,String> {
}
