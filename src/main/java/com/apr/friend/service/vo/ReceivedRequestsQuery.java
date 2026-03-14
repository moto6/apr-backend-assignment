package com.apr.friend.service.vo;

public record ReceivedRequestsQuery(
        Long userId,
        int maxSize,
        String window
) {
}
