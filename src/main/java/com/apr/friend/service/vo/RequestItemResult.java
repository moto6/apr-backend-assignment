package com.apr.friend.service.vo;

import com.querydsl.core.annotations.QueryProjection;

import java.time.OffsetDateTime;

public record RequestItemResult(
        Long friendRequestId,
        Long requestUserId,
        OffsetDateTime requestedAt
) {
    @QueryProjection
    public RequestItemResult {
    }
}