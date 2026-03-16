package com.apr.friend.repository.impl;

import com.apr.friend.domain.Friend;
import com.apr.friend.domain.FriendStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FriendRepositoryJpa extends JpaRepository<Friend, Long> {
    @Query("SELECT f FROM Friend f " +
            "WHERE f.friendStatus = :status " +
            "AND (f.fromAccount.accountId = :userId OR f.toAccount.accountId = :userId)")
    Page<Friend> findFriendsByStatus(
            @Param("userId") Long userId,
            @Param("code") FriendStatus status,
            Pageable pageable
    );

    Optional<Friend> findFirstByFriendRequestId(Long friendRequestId);
}
