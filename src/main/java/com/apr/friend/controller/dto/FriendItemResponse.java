package com.apr.friend.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public record FriendItemResponse(
        @JsonProperty("user_id")
        Long userId,
        @JsonProperty("from_user_id")
        Long fromAccountId,
        @JsonProperty("to_user_id")
        Long toAccountId,
        OffsetDateTime approvedAt
) {
}