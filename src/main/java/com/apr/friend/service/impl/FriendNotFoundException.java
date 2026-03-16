package com.apr.friend.service.impl;

import java.util.UUID;

public class FriendNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Friend request not found. friendRequestId=[%s]";

    public FriendNotFoundException(Long id) {
        super(String.format(MESSAGE, id));
    }
}
