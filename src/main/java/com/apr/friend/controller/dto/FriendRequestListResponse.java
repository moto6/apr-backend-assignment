package com.apr.friend.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record FriendRequestListResponse(
        @JsonProperty("window")
        String window,

        @JsonProperty("totalCount")
        long totalCount,

        @JsonProperty("items")
        List<RequestItemDto> items
) {
}