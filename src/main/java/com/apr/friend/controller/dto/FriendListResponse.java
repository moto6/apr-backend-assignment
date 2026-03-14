package com.apr.friend.controller.dto;

import com.apr.friend.service.vo.FriendListResult;

import java.util.List;

public record FriendListResponse(
        int totalPages,
        long totalCount,
        List<FriendItemDto> items
) {
    public static FriendListResponse of(FriendListResult result) {
        /*
        public record FriendListResult(
        int totalPages,
        long totalCount,
        List<FriendItemResult> items
) {
}
         */
        return null;
    }


}