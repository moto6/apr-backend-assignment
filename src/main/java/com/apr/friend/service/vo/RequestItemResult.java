package com.apr.friend.service.vo;

import com.querydsl.core.annotations.QueryProjection;

import java.time.LocalDateTime;

public record RequestItemResult(
        Long friendRequestId,
        Long requestUserId,
        LocalDateTime requestedAt
) {
    @QueryProjection
    public RequestItemResult {
    }
}