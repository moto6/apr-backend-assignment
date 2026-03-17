package com.apr.context.error;

import com.apr.friend.domain.FriendStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FriendRequestConflictException extends RuntimeException {
    private static final String MESSAGE = "유효하지 않은 요청";
    private static final Logger log = LoggerFactory.getLogger(FriendRequestConflictException.class);

    public FriendRequestConflictException(Long friendRequestId, FriendStatus friendStatus) {
        super(MESSAGE);
        log.info(String.format("[%s] 요청 중복 처리 시도 / 현상태[%s]", friendRequestId, friendStatus));
    }
}
