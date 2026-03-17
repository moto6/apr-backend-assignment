package com.apr.context.ratelimit;

import lombok.Getter;

@Getter
public class TokenBucket {
    private final long capacity;
    private final double refillRate; // 초당 보충되는 토큰 수
    private double tokens;
    private long lastRefillTimestamp; // 나노초 단위

    public TokenBucket(int limitPerSecond) {
        this.capacity = limitPerSecond;
        this.refillRate = limitPerSecond;
        this.tokens = limitPerSecond;
        this.lastRefillTimestamp = System.nanoTime();
    }

    /**
     * 토큰 소모 시도
     */
    public synchronized boolean tryConsume() {
        refill();
        if (tokens >= 1) {
            tokens -= 1;
            return true;
        }
        return false;
    }

    /**
     * 설계 명세: Lazy Refill 계산 로직
     */
    private void refill() {
        long now = System.nanoTime();
        // 시간 간격 계산 (초 단위)
        double deltaTime = (now - lastRefillTimestamp) / 1_000_000_000.0;

        // 보충 공식: tokens = min(capacity, tokens + (dt * rate))
        double refillAmount = deltaTime * refillRate;
        tokens = Math.min(capacity, tokens + refillAmount);

        lastRefillTimestamp = now;
    }
}