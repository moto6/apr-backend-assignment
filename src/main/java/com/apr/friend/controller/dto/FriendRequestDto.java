package com.apr.friend.controller.dto;

import jakarta.validation.constraints.NotNull;

public record FriendRequestDto(
        @NotNull Long toAccountId
) {
}