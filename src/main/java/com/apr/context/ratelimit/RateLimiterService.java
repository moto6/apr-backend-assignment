package com.apr.context.ratelimit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RateLimiterService {

    private final RateLimitBuckets rateLimitBuckets;

    public boolean isAllowed(String key, int limit) {
        // 설계 명세: computeIfAbsent로 원자적 생성 보장
        TokenBucket bucket = rateLimitBuckets.computeIfAbsense(key, limit);
        return bucket.tryConsume();
    }
}
