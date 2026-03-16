package com.apr.friend.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class FriendTest {

    @Test
    @DisplayName("수락(accept) 호출 시 상태가 ACCEPTED로 변경되고 승인 시간이 기록되어야 한다")
    void accept_ShouldChangeStatusAndRecordTime() {
        // given: PENDING 상태의 친구
        Friend friend = createPendingFriend(1L, 2L);
        LocalDateTime beforeAccept = LocalDateTime.now();

        // when: 수락 호출
        friend.accept(1L);

        // then
        assertThat(friend.getFriendStatus()).isEqualTo(FriendStatus.ACCEPTED);
        assertThat(friend.getApprovedAt()).isAfterOrEqualTo(beforeAccept);
        assertThat(friend.getApprovedAt()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    @DisplayName("이미 거절된 상태에서 다시 수락을 호출하면 예외가 발생해야 한다")
    void accept_ShouldThrowException_WhenAlreadyAccepted() {
        // given: ACCEPTED 상태인 친구 둘
        Friend friend = createPendingFriend(1L, 2L);
        friend.reject(1L);
        // when
        // then : 거절 후 수락 시 예외 발생
        assertThatThrownBy(() -> friend.accept(1L))
                .isInstanceOf(IllegalStateException.class);
//                .hasMessageContaining("거절된 요청입니다");
    }

    private Friend createPendingFriend(Long fromAccountId, Long toAccountId) {
        return Friend.builder()
                .friendRequestId(UUID.randomUUID())
                .fromAccountId(fromAccountId)
                .toAccountId(toAccountId)
                .friendStatus(FriendStatus.PENDING)
                .requestedAt(LocalDateTime.now())
                .build();
    }
}
