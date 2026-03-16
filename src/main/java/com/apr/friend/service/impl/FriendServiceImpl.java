package com.apr.friend.service.impl;

import com.apr.friend.domain.Friend;
import com.apr.friend.repository.FriendRepository;
import com.apr.friend.service.FriendService;
import com.apr.friend.service.vo.FriendActionCommand;
import com.apr.friend.service.vo.FriendItemResult;
import com.apr.friend.service.vo.FriendListQuery;
import com.apr.friend.service.vo.FriendListResult;
import com.apr.friend.service.vo.FriendRequestCommand;
import com.apr.friend.service.vo.FriendRequestListResult;
import com.apr.friend.service.vo.ReceivedRequestsQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final FriendRepository friendRepository;

    @Override
    public FriendListResult getFriendList(FriendListQuery query) {
        Page<Friend> friendPage = friendRepository.findAllFriends(query.userId(), query.pageable());
        List<FriendItemResult> items = friendPage.getContent().stream()
                .map(friend -> FriendItemResult.from(friend, query.userId()))
                .toList();
        return new FriendListResult(
                friendPage.getTotalPages(),
                friendPage.getTotalElements(),
                items
        );
    }

    @Override
    public FriendRequestListResult getReceivedRequests(ReceivedRequestsQuery query) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime from = query.window().getFromDateTime(now);
        LocalDateTime to = query.window().getToDateTime(now);
        var results = friendRepository.findAllReceivedRequests(
                query,
                from,
                to
        );

        return new FriendRequestListResult(
                query.window().stringValue(),
                results.size(),
                results
        );
    }

    @Override
    public void requestFriend(FriendRequestCommand command) {
        Friend friend = Friend.requestOf(command.fromAccountId(), command.toAccountId());
        friendRepository.save(friend);
    }

    @Override
    public void acceptFriend(FriendActionCommand command) {
        var friend = friendRepository.findById(command.friendRequestId())
                .orElseThrow(() -> new FriendNotFoundException(command.friendRequestId()));
        friend.accept(command.accountId());
    }

    @Override
    public void rejectFriend(FriendActionCommand command) {
        var friend = friendRepository.findById(command.friendRequestId())
                .orElseThrow(() -> new FriendNotFoundException(command.friendRequestId()));
        friend.reject(command.accountId());
    }
}
