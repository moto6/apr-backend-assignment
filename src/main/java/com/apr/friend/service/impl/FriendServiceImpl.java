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
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendServiceImpl implements FriendService {

    public FriendServiceImpl(FriendRepository friendRepository) {
        this.friendRepository = friendRepository;
    }

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
        return null;
    }

    @Override
    public void requestFriend(FriendRequestCommand command) {

    }

    @Override
    public void acceptFriend(FriendActionCommand command) {

    }

    @Override
    public void rejectFriend(FriendActionCommand command) {

    }
}
