package com.apr.friend.service.vo;

import org.springframework.data.domain.Pageable;

public record FriendListQuery(
        Long userId,
        Pageable pageable
) {
}
