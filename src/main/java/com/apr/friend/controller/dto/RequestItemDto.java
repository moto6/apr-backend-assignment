package com.apr.friend.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public record RequestItemDto(
        @JsonProperty("request_id")
        Long friendRequestId,

        @JsonProperty("request_user_id")
        Long requestUserId,

        @JsonProperty("requestedAt")
        OffsetDateTime requestedAt
) {
}