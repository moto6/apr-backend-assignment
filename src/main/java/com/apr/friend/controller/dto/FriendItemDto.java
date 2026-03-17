package com.apr.friend.controller.dto;

import java.time.OffsetDateTime;

public record FriendItemDto(
        Long userId,
        Long fromUserId,
        Long toUserId,
        OffsetDateTime approvedAt
) {
}