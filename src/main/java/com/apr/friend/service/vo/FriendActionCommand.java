package com.apr.friend.service.vo;

public record FriendActionCommand(
        Long accountId,
        Long friendRequestId
) {
}