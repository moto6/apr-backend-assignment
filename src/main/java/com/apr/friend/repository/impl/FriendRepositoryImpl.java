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
    public Optional<Friend> findById(UUID friendRequestId) {
        return friendRepositoryJpa.findFirstByFriendRequestId(friendRequestId);
    }
}
