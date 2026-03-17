package com.apr.context.error;

public class FriendLimitExceededException extends RuntimeException {
    private static final String MESSAGE = "친구 수는 최대 %d명에 도달하였습니다.";

    public FriendLimitExceededException(long limit) {
        super(String.format(MESSAGE, limit));
    }
}
