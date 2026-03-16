package com.apr.friend.repository.impl;

import com.apr.friend.domain.Friend;
import com.apr.friend.domain.FriendStatus;
import com.apr.friend.domain.QFriend;
import com.apr.friend.repository.FriendRepository;
import com.apr.friend.service.vo.QRequestItemResult;
import com.apr.friend.service.vo.ReceivedRequestsQuery;
import com.apr.friend.service.vo.RequestItemResult;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class FriendRepositoryImpl implements FriendRepository {

    private final FriendRepositoryJpa friendRepositoryJpa;
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Friend> findAllFriends(Long userId, Pageable pageable) {
        return friendRepositoryJpa.findFriendsByStatus(userId, FriendStatus.ACCEPTED, pageable);
    }

    @Override
    public List<RequestItemResult> findAllReceivedRequests(ReceivedRequestsQuery query, LocalDateTime from, LocalDateTime to) {
        QFriend friend = QFriend.friend;
        return queryFactory.select(new QRequestItemResult(
                        friend.friendRequestId,
                        friend.fromAccountId,
                        friend.requestedAt
                ))
                .from(friend)
                .where(
                        friend.toAccountId.eq(query.userId()),
                        friend.friendStatus.eq(FriendStatus.PENDING),
                        friend.requestedAt.between(from, to)
                ).orderBy(friend.requestedAt.desc())
                .limit(query.maxSize())
                .fetch();
    }

    @Override
    public void save(Friend friend) {
        friendRepositoryJpa.save(friend);
    }

    @Override
    public Optional<Friend> findById(Long friendRequestId) {
        return friendRepositoryJpa.findFirstByFriendRequestId(friendRequestId);
    }
}
