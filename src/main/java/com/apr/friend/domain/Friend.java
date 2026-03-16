package com.apr.friend.domain;

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

import java.time.LocalDateTime;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Friend {

    @Id
    private UUID friendRequestId;

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
    private LocalDateTime requestedAt;

    @Column
    private LocalDateTime approvedAt;

    @Column
    private LocalDateTime rejectedAt; // TODO : DDL 반영필요

    public static Friend requestOf(Long fromAccountId, Long toAccountId) {
        requireNonNull(fromAccountId);
        requireNonNull(toAccountId);
        return Friend.builder()
                .friendRequestId(UUID.randomUUID())
                .toAccountId(toAccountId)
                .fromAccountId(fromAccountId)
                .friendStatus(FriendStatus.PENDING)
                .requestedAt(LocalDateTime.now())
                .build();
    }

    public void accept(Long accountId) {
        if (hasPermission(accountId)) {
            validateTransition(FriendStatus.ACCEPTED);
            this.friendStatus = FriendStatus.ACCEPTED;
            this.approvedAt = LocalDateTime.now();
        }
    }

    private boolean hasPermission(Long accountId) {
        requireNonNull(accountId);
        return toAccountId.equals(accountId);
    }

    public void reject(Long accountId) {
        if (hasPermission(accountId)) {
            validateTransition(FriendStatus.REJECTED);
            this.friendStatus = FriendStatus.REJECTED;
            this.rejectedAt = LocalDateTime.now();
        }
    }

    private void validateTransition(FriendStatus nextStatus) {
        if (this.friendStatus != FriendStatus.PENDING) {
            throw new IllegalStateException(
                    String.format("이미 처리된 요청입니다. (현재 상태: %s)", this.friendStatus)
            );
        }
    }
}
