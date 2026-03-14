package com.apr.friend.repository.impl;

import com.apr.friend.repository.FriendRepository;
import org.springframework.stereotype.Repository;

@Repository
public class FriendRepositoryImpl implements FriendRepository {

    public FriendRepositoryImpl(FriendRepositoryJpa friendRepositoryJpa) {
        this.friendRepositoryJpa = friendRepositoryJpa;
    }

    private final FriendRepositoryJpa friendRepositoryJpa;


}
