package com.ibizabroker.lms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatRateLimiter {
    private static class Counter {
        volatile long windowStartMillis;
        volatile int count;
    }

    @Value("${chat.rate.window-millis:60000}")
    private long windowMillis; // default 1 minute

    @Value("${chat.rate.max-requests:10}")
    private int maxRequests;   // default 10 reqs per window

    @Value("${chat.rate.backend:auto}")
    private String backend;    // auto | redis | memory

    @Autowired(required = false)
    private StringRedisTemplate redis;

    private final Map<String, Counter> counters = new ConcurrentHashMap<>();

    public boolean allow(String key) {
        if (useRedis()) {
            return allowRedis(key);
        }
        return allowMemory(key);
    }

    private boolean useRedis() {
        if ("redis".equalsIgnoreCase(backend)) return redis != null;
        if ("memory".equalsIgnoreCase(backend)) return false;
        // auto
        return redis != null;
    }

    private boolean allowRedis(String key) {
        try {
            String redisKey = "rl:" + key;
            Long count = redis.opsForValue().increment(redisKey);
            if (count != null && count == 1L) {
                redis.expire(redisKey, Duration.ofMillis(windowMillis));
            }
            return count != null && count <= maxRequests;
        } catch (Exception e) {
            // Fallback to memory if Redis unavailable
            return allowMemory(key);
        }
    }

    private boolean allowMemory(String key) {
        long now = Instant.now().toEpochMilli();
        Counter c = counters.computeIfAbsent(key, k -> new Counter());
        synchronized (c) {
            if (now - c.windowStartMillis >= windowMillis) {
                c.windowStartMillis = now;
                c.count = 0;
            }
            if (c.count >= maxRequests) {
                return false;
            }
            c.count++;
            return true;
        }
    }
}
