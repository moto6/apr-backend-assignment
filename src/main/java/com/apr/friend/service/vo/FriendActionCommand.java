package com.apr.friend.service.vo;

import java.util.UUID;

public record FriendActionCommand(
        Long userId,
        UUID requestId
) {
}