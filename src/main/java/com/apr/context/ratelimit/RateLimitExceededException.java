package com.apr.context.ratelimit;

public class RateLimitExceededException extends RuntimeException {
    public RateLimitExceededException(int actualRate) {
        super(String.format("초당 전송 제한 도달 [%d]", actualRate));
    }
}
