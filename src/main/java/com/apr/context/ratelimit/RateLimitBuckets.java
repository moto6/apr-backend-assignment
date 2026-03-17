package com.apr.context.ratelimit;

public interface RateLimitBuckets {
    TokenBucket computeIfAbsense(String key, int limit);
}
