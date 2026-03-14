package com.apr.friend.service.impl;

import com.apr.friend.service.FriendService;
import com.apr.friend.service.vo.FriendActionCommand;
import com.apr.friend.service.vo.FriendListQuery;
import com.apr.friend.service.vo.FriendListResult;
import com.apr.friend.service.vo.FriendRequestCommand;
import com.apr.friend.service.vo.FriendRequestListResult;
import com.apr.friend.service.vo.ReceivedRequestsQuery;
import org.springframework.stereotype.Service;

@Service
public class FriendServiceImpl implements FriendService {

    @Override
    public FriendListResult getFriendList(FriendListQuery query) {
        return null;
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
