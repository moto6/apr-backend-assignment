package com.apr.friend.controller.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record RequestItemDto(
        UUID requestId,
        Long requestUserId,
        LocalDateTime requestedAt
) {
}