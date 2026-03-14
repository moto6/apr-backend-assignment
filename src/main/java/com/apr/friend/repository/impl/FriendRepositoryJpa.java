package com.apr.friend.repository.impl;

import com.apr.friend.domain.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepositoryJpa extends JpaRepository<Friend, Long> {
}
