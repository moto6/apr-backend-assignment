package com.apr.friend.service.vo;

import com.querydsl.core.annotations.QueryProjection;

import java.time.LocalDateTime;
import java.util.UUID;

public record RequestItemResult(
        UUID requestId,
        Long requestUserId,
        LocalDateTime requestedAt
) {
    @QueryProjection
    public RequestItemResult {
    }
}