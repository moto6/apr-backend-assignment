package com.apr.context.ratelimit;

public class RateLimitExceededException extends RuntimeException {
    public RateLimitExceededException(int actualRate) {
        super("초당 전송 제한 도달");
    }
}
