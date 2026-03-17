package com.apr.friend.repository;

import com.apr.friend.domain.Friend;
import com.apr.friend.service.vo.FriendItemResult;
import com.apr.friend.service.vo.ReceivedRequestsQuery;
import com.apr.friend.service.vo.RequestItemResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface FriendRepository {
    Page<FriendItemResult> findAllFriends(Long accountId, Pageable pageable);

    List<RequestItemResult> findAllReceivedRequests(ReceivedRequestsQuery query, OffsetDateTime from, OffsetDateTime to);

    void save(Friend friend);

    Optional<Friend> findById(Long uuid);

    long countFriends(Long userId);
}
