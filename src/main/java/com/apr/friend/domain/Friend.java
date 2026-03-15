package com.apr.friend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Friend {

    @Id
    private Long friendId;

    @Column(name = "from_account_id", insertable = false, updatable = false)
    private Long fromAccountId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_account_id")
    private Account fromAccount; // 친구 요청자

    @Column(name = "to_account_id", insertable = false, updatable = false)
    private Long toAccountId;
    
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
