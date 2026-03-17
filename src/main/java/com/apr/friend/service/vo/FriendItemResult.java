package com.apr.friend.service.vo;

import com.apr.friend.domain.Friend;

import java.time.OffsetDateTime;

public record FriendItemResult(
        Long userId,
        Long fromUserId,
        Long toUserId,
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
