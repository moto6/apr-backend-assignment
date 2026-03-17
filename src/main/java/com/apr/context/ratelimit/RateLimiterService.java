package com.apr.context.ratelimit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class RateLimiterService {

    private final RateLimitBuckets rateLimitBuckets;

    public boolean isAllowed(String key, int limit) {
        // 설계 명세: computeIfAbsent로 원자적 생성 보장
        TokenBucket bucket = rateLimitBuckets.computeIfAbsense(key,limit);
        return bucket.tryConsume();
    }

    public void evictIdleBuckets() {
        // TODO: lastAccessTime 기준으로 맵에서 제거하는 로직
    }
}
