package com.apr.friend.controller;

import com.apr.context.ApiResponse;
import com.apr.context.FriendMapper;
import com.apr.friend.controller.dto.FriendListResponse;
import com.apr.friend.controller.dto.FriendRequestDto;
import com.apr.friend.controller.dto.FriendRequestListResponse;
import com.apr.friend.service.FriendService;
import com.apr.friend.service.vo.FriendActionCommand;
import com.apr.friend.service.vo.FriendListQuery;
import com.apr.friend.service.vo.FriendRequestCommand;
import com.apr.friend.service.vo.ReceivedRequestsQuery;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/friend")
@Tag(name = "Friend API")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;
    private final FriendMapper friendMapper;

    @GetMapping
    public ApiResponse<FriendListResponse> getFriends(
            @RequestHeader("X-User-Id") Long userId,
            @PageableDefault(sort = "approvedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        var result = friendService.getFriendList(new FriendListQuery(userId, pageable));
        return ApiResponse.ok(friendMapper.toFriendListResponse(result));
    }

    @GetMapping("/requests")
    public ApiResponse<FriendRequestListResponse> getReceivedRequests(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam(defaultValue = "20") int maxSize,
            @RequestParam(defaultValue = "1d") String window) {
        var result = friendService.getReceivedRequests(
                ReceivedRequestsQuery.of(userId, maxSize, window)
        );
        return ApiResponse.ok(friendMapper.toResponse(result));
    }

    @PostMapping("/request")
    public ResponseEntity<Void> requestFriend(
            @RequestHeader("X-User-Id") Long fromAccountId,
            @Valid @RequestBody FriendRequestDto requestDto) {
        friendService.requestFriend(new FriendRequestCommand(fromAccountId, requestDto.toAccountId()));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/accept/{requestId}")
    public ApiResponse<Void> acceptFriend(
            @RequestHeader("X-User-Id") Long accountId,
            @PathVariable UUID requestId) {
        friendService.acceptFriend(new FriendActionCommand(accountId, requestId));
        return ApiResponse.ok();
    }

    @PostMapping("/reject/{requestId}")
    public ApiResponse<Void> rejectFriend(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable UUID requestId) {

        friendService.rejectFriend(new FriendActionCommand(userId, requestId));
        return ApiResponse.ok();
    }
}