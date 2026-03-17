package com.apr.friend.service.impl;

import com.apr.context.error.FriendLimitExceededException;
import com.apr.context.error.FriendNotFoundException;
import com.apr.friend.domain.Friend;
import com.apr.friend.domain.FriendStatus;
import com.apr.friend.domain.component.FriendValidator;
import com.apr.friend.repository.FriendRepository;
import com.apr.friend.service.vo.FriendActionCommand;
import com.apr.friend.service.vo.FriendListQuery;
import com.apr.friend.service.vo.FriendListResult;
import com.apr.friend.service.vo.FriendRequestCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FriendServiceImplTest {

    @InjectMocks
    private FriendServiceImpl friendService;

    @Mock
    private FriendRepository friendRepository;

    @Mock
    private FriendValidator friendValidator;

    @Test
    @DisplayName("수락 시 현재 친구가 9999 명이면 정상동작한다")
    void acceptFriend_ShouldSucceed_WhenUnderLimit() {
        // given
        Long myId = 2L;
        Long requestId = 200100L;
        Friend pendingFriend = Friend.requestOf(1L, myId);
        when(friendRepository.findById(requestId))
                .thenReturn(Optional.of(pendingFriend));
        doNothing().when(friendValidator).validateCanAccept(myId);

        // When
        friendService.acceptFriend(new FriendActionCommand(myId, requestId));

        // Then
        assertThat(pendingFriend.getFriendStatus()).isEqualTo(FriendStatus.ACCEPTED);
    }

    @Test
    @DisplayName("친구 요청을 보내면 저장소에 저장된다")
    void requestFriend_Success() {
        // given
        FriendRequestCommand command = new FriendRequestCommand(1L, 2L);

        // when
        friendService.requestFriend(command);

        // then
        verify(friendRepository, times(1)).save(any(Friend.class));
    }

    @Test
    @DisplayName("친구 목록 조회 시 페이징 정보와 결과를 반환한다")
    void getFriendList_Success() {
        // given
        FriendListQuery query = new FriendListQuery(1L, PageRequest.of(0, 10));
        given(friendRepository.findAllFriends(any(), any()))
                .willReturn(new PageImpl<>(List.of()));

        // when
        FriendListResult result = friendService.getFriendList(query);

        // then
        assertThat(result).isNotNull();
        verify(friendRepository).findAllFriends(eq(1L), any());
    }

    @Test
    @DisplayName("친구 거절 시 내부 로직에 의해 거절 처리된다")
    void rejectFriend_Success() {
        // given
        Long myId = 1L;
        Long requestId = 100L;
        Friend pendingFriend = Friend.requestOf(2L, myId);
        given(friendRepository.findById(requestId)).willReturn(Optional.of(pendingFriend));

        // when
        friendService.rejectFriend(new FriendActionCommand(myId, requestId));

        // then
        assertThat(pendingFriend.getFriendStatus()).isEqualTo(FriendStatus.REJECTED);
    }

    @Nested
    @DisplayName("친구 요청 수락 테스트")
    class AcceptFriendTest {

        @Test
        @DisplayName("수락 성공 시 상태가 ACCEPTED로 변경된다")
        void acceptFriend_Success() {
            // given
            Long myId = 1L;
            Long requestId = 100L;
            FriendActionCommand command = new FriendActionCommand(myId, requestId);
            Friend pendingFriend = Friend.requestOf(2L, myId); // 상대방이 나에게 보낸 요청

            given(friendRepository.findById(requestId)).willReturn(Optional.of(pendingFriend));

            // when
            friendService.acceptFriend(command);

            // then
            assertThat(pendingFriend.getFriendStatus()).isEqualTo(FriendStatus.ACCEPTED);
            verify(friendValidator).validateCanAccept(myId); // 검증 로직이 호출되었는지 확인
        }

        @Test
        @DisplayName("친구가 10,000명이 넘으면 수락 시 FriendLimitExceededException이 발생한다")
        void acceptFriend_ThrowsException_WhenLimitExceeded() {
            // given
            Long myId = 1L;
            FriendActionCommand command = new FriendActionCommand(myId, 100L);
            doThrow(new FriendLimitExceededException(10000))
                    .when(friendValidator).validateCanAccept(myId);

            // when & then
            assertThatThrownBy(() -> friendService.acceptFriend(command))
                    .isInstanceOf(FriendLimitExceededException.class)
                    .describedAs("validator에서 예외가 발생하면 repository 조회는 일어나지 않는다");
            verify(friendRepository, never()).findById(any());
        }

        @Test
        @DisplayName("요청 ID가 존재하지 않으면 FriendNotFoundException이 발생한다")
        void acceptFriend_ThrowsException_WhenNotFound() {
            // given
            Long requestId = 999L;
            given(friendRepository.findById(requestId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> friendService.acceptFriend(new FriendActionCommand(1L, requestId)))
                    .isInstanceOf(FriendNotFoundException.class);
        }
    }

}