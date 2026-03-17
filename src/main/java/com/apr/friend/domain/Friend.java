package com.apr.friend.domain;

import com.apr.context.error.FriendRequestConflictException;
import com.apr.context.error.InsufficientPermissionException;
import com.apr.context.identity.LongIdGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

import static java.util.Objects.requireNonNull;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Friend {

    @Id
    @Column(name = "friend_request_id", updatable = false)
    private Long friendRequestId;

    @Column(name = "from_account_id")
    private Long fromAccountId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_account_id", insertable = false, updatable = false)
    private Account fromAccount; // 친구 요청자

    @Column(name = "to_account_id")
    private Long toAccountId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_account_id", insertable = false, updatable = false)
    private Account toAccount; // 친구 수신자

    @Enumerated(EnumType.STRING)
    private FriendStatus friendStatus;

    @Column
    private OffsetDateTime requestedAt;

    @Column
    private OffsetDateTime approvedAt;

    @Column
    private OffsetDateTime rejectedAt; // TODO : DDL 반영필요

    public static Friend requestOf(Long fromAccountId, Long toAccountId) {
        requireNonNull(fromAccountId);
        requireNonNull(toAccountId);
        return Friend.builder()
                .friendRequestId(LongIdGenerator.nextId())
                .toAccountId(toAccountId)
                .fromAccountId(fromAccountId)
                .friendStatus(FriendStatus.PENDING)
                .requestedAt(OffsetDateTime.now())
                .build();
    }

    public void accept(Long accountId) {
        checkPermission(accountId);
        checkTransition();
        this.friendStatus = FriendStatus.ACCEPTED;
        this.approvedAt = OffsetDateTime.now();
    }

    public void reject(Long accountId) {
        checkPermission(accountId);
        checkTransition();
        this.friendStatus = FriendStatus.REJECTED;
        this.rejectedAt = OffsetDateTime.now();
    }

    private void checkPermission(Long accountId) {
        requireNonNull(accountId);
        if (!toAccountId.equals(accountId)) {
            throw new InsufficientPermissionException(toAccountId, accountId);
        }
    }

    private void checkTransition() {
        if (this.friendStatus != FriendStatus.PENDING) {
            throw new FriendRequestConflictException(this.friendRequestId, this.friendStatus);
        }
    }
}
