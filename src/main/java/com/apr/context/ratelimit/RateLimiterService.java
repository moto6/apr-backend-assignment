package com.apr.context.ratelimit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RateLimiterService {

    private final RateLimitBuckets rateLimitBuckets;

    public boolean isAllowed(String key, int limit) {
        TokenBucket bucket = rateLimitBuckets.computeIfAbsense(key, limit);
        return bucket.tryConsume();
    }
}
