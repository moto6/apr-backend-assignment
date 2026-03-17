package com.apr.friend.domain.component;

import com.apr.context.error.FriendLimitExceededException;
import com.apr.friend.repository.FriendRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FriendValidatorTest {
    private final Long targetAccountId = 1L;
    @InjectMocks
    private FriendValidator friendValidator;
    @Mock
    private FriendRepository friendRepository;
    @Mock
    private FriendProperties friendProperties;

    @Test
    @DisplayName("현재 친구 수가 제한(10,000)보다 적으면 검증을 통과한다")
    void validate_Success_WhenUnderLimit() {
        // given
        when(friendProperties.maxLimit()).thenReturn(10000);
        when(friendRepository.countFriends(targetAccountId)).thenReturn(9999L);

        // when
        // then: 예외가 발생하지 않아야 한다
        assertThatCode(() -> friendValidator.validateCanAccept(targetAccountId))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("현재 친구 수가 제한과 같거나 많으면 FriendLimitExceededException이 발생한다")
    void validate_Fail_WhenAtLimit() {
        int friendCountMaxLimit = 10000;
        // given
        when(friendProperties.maxLimit()).thenReturn(friendCountMaxLimit);
        when(friendRepository.countFriends(targetAccountId)).thenReturn(Long.valueOf(friendCountMaxLimit));

        // when
        // then
        assertThatThrownBy(() -> friendValidator.validateCanAccept(targetAccountId))
                .isInstanceOf(FriendLimitExceededException.class)
                .hasMessageContaining(String.valueOf(friendCountMaxLimit));
    }
}