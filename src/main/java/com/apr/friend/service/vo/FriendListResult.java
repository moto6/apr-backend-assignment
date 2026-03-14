package com.apr.friend.service.vo;

import java.util.List;

public record FriendListResult(
        int totalPages,
        long totalCount,
        List<FriendItemResult> items
) {
}