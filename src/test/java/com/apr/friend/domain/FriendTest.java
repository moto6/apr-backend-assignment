package com.apr.friend.domain;

import com.apr.context.error.InsufficientPermissionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class FriendTest {

    @Test
    @DisplayName("수락(accept) 호출 시 상태가 ACCEPTED로 변경되고 승인 시간이 기록되어야 한다")
    void accept_ShouldChangeStatusAndRecordTime() {
        // given: PENDING 상태의 친구
        Long fromAccountId = 111L;
        Long toAccountId = 707L;
        Friend friend = Friend.requestOf(fromAccountId, toAccountId);
        LocalDateTime beforeAccept = LocalDateTime.now();

        // when: 수락 호출
        friend.accept(toAccountId);

        // then
        assertThat(friend.getFriendStatus()).isEqualTo(FriendStatus.ACCEPTED);
        assertThat(friend.getApprovedAt()).isAfterOrEqualTo(beforeAccept);
        assertThat(friend.getApprovedAt()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    @DisplayName("이미 거절된 상태에서 다시 수락을 호출하면 예외가 발생해야 한다")
    void accept_ShouldThrowException_WhenAlreadyAccepted() {
        // given
        Long fromAccountId = 330L;
        Long toAccountId = 787L;
        Friend friend = Friend.requestOf(fromAccountId, toAccountId);

        // when
        friend.reject(toAccountId);

        // then
        assertThatThrownBy(() -> friend.accept(toAccountId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 처리된 요청입니다");
    }


    @Test
    @DisplayName("수락 시 수신자가 아닌 사용자가 호출하면 예외가 발생한다")
    void accept_ShouldThrowException_WhenCallerIsNotToAccount() {
        // given: 111L이 2L에게 친구 요청을 보낸 상황
        Long fromAccountId = 111L;
        Long toAccountId = 2L;
        Friend friend = Friend.requestOf(fromAccountId, toAccountId);

        // when & then: 요청자(111L)가 직접 수락을 시도하면 에러 발생
        assertThatThrownBy(() -> friend.accept(fromAccountId))
                .isInstanceOf(InsufficientPermissionException.class)
                .hasMessageContaining("권한이 없습니다");

        // when & then: 제3의 인물(999L)이 수락을 시도해도 에러 발생
        Long strangerId = 999L;
        assertThatThrownBy(() -> friend.accept(strangerId))
                .isInstanceOf(InsufficientPermissionException.class)
                .hasMessageContaining("권한이 없습니다");
    }

    @Test
    @DisplayName("거절 시 수신자가 아닌 사용자가 호출하면 예외가 발생한다")
    void reject_ShouldThrowException_WhenCallerIsNotToAccount() {
        // given
        Friend friend = Friend.requestOf(111L, 2L);

        // when & then: 요청을 받은 사람 이외에 거절을 시도하면 에러 발생
        assertThatThrownBy(() -> friend.reject(111L))
                .isInstanceOf(InsufficientPermissionException.class);
        assertThatThrownBy(() -> friend.accept(111L))
                .isInstanceOf(InsufficientPermissionException.class);
    }
}
