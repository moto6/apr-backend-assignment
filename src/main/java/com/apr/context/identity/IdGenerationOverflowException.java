package com.apr.context.identity;

public class IdGenerationOverflowException extends RuntimeException {
    public IdGenerationOverflowException(long maxPerSecond) {
        super(String.format("ID generation overflow. Max allowed per second : %d", maxPerSecond));
    }
}