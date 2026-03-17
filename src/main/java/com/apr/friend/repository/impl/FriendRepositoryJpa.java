package com.apr.friend.repository.impl;

import com.apr.friend.domain.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FriendRepositoryJpa extends JpaRepository<Friend, Long> {
    Optional<Friend> findFirstByFriendRequestId(Long friendRequestId);
}
