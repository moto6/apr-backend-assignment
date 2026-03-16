package com.apr.friend.service.vo;

public record FriendRequestCommand(
        Long fromAccountId,
        Long toAccountId
) {
}
