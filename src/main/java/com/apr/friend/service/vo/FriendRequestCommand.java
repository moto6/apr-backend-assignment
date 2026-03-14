package com.apr.friend.service.vo;

public record FriendRequestCommand(
        Long fromUserId,
        Long toUserId
) {
}
