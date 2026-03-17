package com.apr.friend.domain.component;

import com.apr.context.error.FriendLimitExceededException;
import com.apr.friend.repository.FriendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FriendValidator {

    private final FriendRepository friendRepository;
    private final FriendProperties friendProperties;

    public void validateCanAccept(Long accountId) {
        long currentFriendsCount = friendRepository.countFriends(accountId);
        if (currentFriendsCount >= friendProperties.maxLimit()) {
            throw new FriendLimitExceededException(friendProperties.maxLimit());
        }
    }
}
