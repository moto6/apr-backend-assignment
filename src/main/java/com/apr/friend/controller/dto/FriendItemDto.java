package com.apr.friend.controller.dto;

import java.time.LocalDateTime;

public record FriendItemDto(
        Long userId,
        Long fromUserId,
        Long toUserId,
        LocalDateTime approvedAt
) {
}