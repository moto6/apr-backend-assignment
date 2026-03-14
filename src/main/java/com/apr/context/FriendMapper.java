package com.apr.context;

import com.apr.friend.controller.dto.FriendRequestListResponse;
import com.apr.friend.service.vo.FriendRequestListResult;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FriendMapper {
    FriendRequestListResponse toResponse(FriendRequestListResult result);
}