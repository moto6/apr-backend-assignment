package com.apr.friend.controller.dto;

import java.util.List;

public record FriendListResponse(
        int totalPages,
        long totalCount,
        List<FriendItemResponse> items
) {
}