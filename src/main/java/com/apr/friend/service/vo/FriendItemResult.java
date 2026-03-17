package com.apr.friend.service.vo;

import com.apr.friend.domain.Friend;

import java.time.OffsetDateTime;

public record FriendItemResult(
        Long userId,
        Long fromAccountId,
        Long toAccountId,
        OffsetDateTime approvedAt
) {
    public static FriendItemResult from(Friend friend, Long requestUserId) {
        return new FriendItemResult(
                requestUserId,
                friend.getFromAccountId(),
                friend.getToAccountId(),
                friend.getApprovedAt()
        );
    }
}
