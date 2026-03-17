package com.apr.friend.service.impl;

import com.apr.context.error.FriendNotFoundException;
import com.apr.friend.domain.Friend;
import com.apr.friend.domain.component.FriendValidator;
import com.apr.friend.repository.FriendRepository;
import com.apr.friend.service.FriendService;
import com.apr.friend.service.vo.FriendActionCommand;
import com.apr.friend.service.vo.FriendItemResult;
import com.apr.friend.service.vo.FriendListQuery;
import com.apr.friend.service.vo.FriendListResult;
import com.apr.friend.service.vo.FriendRequestCommand;
import com.apr.friend.service.vo.FriendRequestListResult;
import com.apr.friend.service.vo.ReceivedRequestsQuery;
import com.apr.friend.service.vo.RequestItemResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FriendServiceImpl implements FriendService {

    private final FriendRepository friendRepository;
    private final FriendValidator friendValidator;

    @Override
    public FriendListResult getFriendList(FriendListQuery query) {
        Page<FriendItemResult> items = friendRepository.findAllFriends(query.userId(), query.pageable());
        return new FriendListResult(
                items.getTotalPages(),
                items.getTotalElements(),
                items.toList()
        );
    }

    @Override
    public FriendRequestListResult getReceivedRequests(ReceivedRequestsQuery query) {
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime from = query.window().getFromDateTime(now);
        OffsetDateTime to = query.window().getToDateTime(now);
        List<RequestItemResult> results = friendRepository.findAllReceivedRequests(
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
        friendValidator.validateCanAccept(command.accountId());
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
