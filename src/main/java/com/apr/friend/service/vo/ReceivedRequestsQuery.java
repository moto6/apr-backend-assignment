package com.apr.friend.service.vo;

import static java.util.Objects.requireNonNull;

public record ReceivedRequestsQuery(
        Long userId,
        int maxSize,
        WindowType window
) {
    public static ReceivedRequestsQuery of(Long userId, int maxSize, String windowString) {
        requireNonNull(userId);
        return new ReceivedRequestsQuery(
                userId,
                Math.min(maxSize, 100),
                WindowType.from(windowString)
        );
    }
}
