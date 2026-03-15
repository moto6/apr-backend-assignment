package com.apr.friend.repository;

import com.apr.friend.domain.Friend;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FriendRepository {
    Page<Friend> findAllFriends(Long userId, Pageable pageable);
}
