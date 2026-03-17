package com.apr.friend.repository.impl;

import com.apr.friend.domain.Friend;
import com.apr.friend.domain.FriendStatus;
import com.apr.friend.domain.QFriend;
import com.apr.friend.repository.FriendRepository;
import com.apr.friend.service.vo.FriendItemResult;
import com.apr.friend.service.vo.QRequestItemResult;
import com.apr.friend.service.vo.ReceivedRequestsQuery;
import com.apr.friend.service.vo.RequestItemResult;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNullElse;

@Repository
@RequiredArgsConstructor
public class FriendRepositoryImpl implements FriendRepository {

    private final FriendRepositoryJpa friendRepositoryJpa;
    private final JPAQueryFactory queryFactory;
    private final QFriend friend = QFriend.friend;

    @Override
    public Page<FriendItemResult> findAllFriends(Long userId, Pageable pageable) {
        List<FriendItemResult> content = queryFactory
                .select(Projections.constructor(FriendItemResult.class,
                        new CaseBuilder()
                                .when(friend.fromAccountId.eq(userId)).then(friend.toAccountId)
                                .otherwise(friend.fromAccountId),
                        friend.fromAccountId,
                        friend.toAccountId,
                        friend.approvedAt
                ))
                .from(friend)
                .where(
                        isMyFriend(userId),
                        friend.friendStatus.eq(FriendStatus.ACCEPTED)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(friend.approvedAt.desc())
                .fetch();
        long total = this.countFriends(userId);

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression isMyFriend(Long userId) {
        return friend.fromAccountId.eq(userId).or(friend.toAccountId.eq(userId));
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

    @Override
    public long countFriends(Long userId) {
        Long friendsCount = queryFactory
                .select(
                        friend.count())
                .from(friend)
                .where(
                        isMyFriend(userId),
                        friend.friendStatus.eq(FriendStatus.ACCEPTED))
                .fetchOne();
        return requireNonNullElse(friendsCount, 0L);
    }
}
