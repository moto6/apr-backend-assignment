package com.apr.friend.service;

import com.apr.friend.service.vo.FriendActionCommand;
import com.apr.friend.service.vo.FriendListQuery;
import com.apr.friend.service.vo.FriendListResult;
import com.apr.friend.service.vo.FriendRequestCommand;
import com.apr.friend.service.vo.FriendRequestListResult;
import com.apr.friend.service.vo.ReceivedRequestsQuery;

public interface FriendService {
    FriendListResult getFriendList(FriendListQuery query);

    FriendRequestListResult getReceivedRequests(ReceivedRequestsQuery query);

    void requestFriend(FriendRequestCommand command);

    void acceptFriend(FriendActionCommand command);

    void rejectFriend(FriendActionCommand command);
}
