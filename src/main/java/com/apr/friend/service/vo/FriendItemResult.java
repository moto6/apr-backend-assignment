package com.apr.friend.service.vo;

import java.time.LocalDateTime;

public record FriendItemResult(
        Long userId,
        Long fromUserId,
        Long toUserId,
        LocalDateTime approvedAt
) {
}
