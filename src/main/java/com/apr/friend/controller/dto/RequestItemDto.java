package com.apr.friend.controller.dto;

import java.time.LocalDateTime;

public record RequestItemDto(
        Long friendRequestId,
        Long requestUserId,
        LocalDateTime requestedAt
) {
}