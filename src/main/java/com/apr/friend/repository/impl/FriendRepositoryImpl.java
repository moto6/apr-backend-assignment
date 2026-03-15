package com.apr.friend.repository.impl;

import com.apr.friend.domain.Friend;
import com.apr.friend.domain.FriendStatus;
import com.apr.friend.repository.FriendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FriendRepositoryImpl implements FriendRepository {

    private final FriendRepositoryJpa friendRepositoryJpa;

    @Override
    public Page<Friend> findAllFriends(Long userId, Pageable pageable) {
        return friendRepositoryJpa.findFriendsByStatus(userId, FriendStatus.ACCEPTED, pageable);
    }
}
