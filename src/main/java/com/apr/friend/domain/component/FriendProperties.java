package com.apr.friend.domain.component;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.friend")
public record FriendProperties(
        int maxLimit
) {
}
