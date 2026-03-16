package com.apr.friend.service.impl;

import java.util.UUID;

public class FriendNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Friend request not found. requestId=[%s]";

    public FriendNotFoundException(UUID uuid) {
        super(String.format(MESSAGE, uuid));
    }
}
