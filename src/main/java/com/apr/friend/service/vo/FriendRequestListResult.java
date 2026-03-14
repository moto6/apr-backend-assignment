package com.apr.friend.service.vo;

import java.util.List;

public record FriendRequestListResult(
        String window,
        long totalCount,
        List<RequestItemResult> items
) {
}