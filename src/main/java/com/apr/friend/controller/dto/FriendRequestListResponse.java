package com.apr.friend.controller.dto;

import java.util.List;

public record FriendRequestListResponse(
        String window,
        long totalCount,
        List<RequestItemDto> items
) {
}