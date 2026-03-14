package com.apr.friend.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

@Entity
public class Friend {

    @Id
    private Long friendId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_account_id")
    private Account fromAccount; // 친구 요청자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_account_id")
    private Account toAccount; // 친구 수신자

    @Enumerated(EnumType.STRING)
    private FriendStatus friendStatus;

    private LocalDateTime requestedAt;

    private LocalDateTime approvedAt;

    public void accept() {
        this.friendStatus = FriendStatus.ACCEPTED;
        this.approvedAt = LocalDateTime.now();
    }
}
