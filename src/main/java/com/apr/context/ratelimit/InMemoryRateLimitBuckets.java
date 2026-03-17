package com.apr.context.ratelimit;

import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryRateLimitBuckets implements RateLimitBuckets {

    private static final ConcurrentHashMap<String, TokenBucket> IN_MEMORY_BUCKET = new ConcurrentHashMap<>();

    @Override
    public TokenBucket computeIfAbsense(String key, int limit) {
        return IN_MEMORY_BUCKET.computeIfAbsent(key, k -> new TokenBucket(limit));
    }

    @Override
    public void removeIdleBuckets(long idleTimeoutNanos) {
        long now = System.nanoTime();
        IN_MEMORY_BUCKET.entrySet().removeIf(entry ->
                (now - entry.getValue().getLastRefillTimestamp()) > idleTimeoutNanos
        );
    }
}
