package com.apr.friend.controller;

import com.apr.context.mapper.FriendMapper;
import com.apr.context.ratelimit.RateLimit;
import com.apr.context.web.ApiResponse;
import com.apr.friend.controller.dto.FriendListResponse;
import com.apr.friend.controller.dto.FriendRequestDto;
import com.apr.friend.controller.dto.FriendRequestListResponse;
import com.apr.friend.service.FriendService;
import com.apr.friend.service.vo.FriendActionCommand;
import com.apr.friend.service.vo.FriendListQuery;
import com.apr.friend.service.vo.FriendRequestCommand;
import com.apr.friend.service.vo.ReceivedRequestsQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

@RestController
@RequestMapping("/api/v1/friend")
@Tag(name = "Friend API")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;
    private final FriendMapper friendMapper;

    @Operation(summary = "친구 목록 조회", description = "승인된 친구 목록을 페이징하여 조회합니다.")
    @GetMapping
    public ApiResponse<FriendListResponse> getFriends(
            @Parameter(description = "사용자 ID", required = true)
            @RequestHeader("X-User-Id") Long accountId,
            @PageableDefault(sort = "approvedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        var result = friendService.getFriendList(new FriendListQuery(accountId, pageable));
        return ApiResponse.ok(friendMapper.toFriendListResponse(result));
    }

    @Operation(summary = "받은 친구 요청 목록 조회", description = "대기 중인 친구 요청을 윈도우(기간) 기반으로 조회합니다.")
    @GetMapping("/requests")
    public ApiResponse<FriendRequestListResponse> getReceivedRequests(
            @Parameter(description = "사용자 ID", required = true)
            @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "최대 조회 개수")
            @RequestParam(defaultValue = "20") int maxSize,
            @Parameter(description = "조회 윈도우 (예: 1d, 7d, 30d)")
            @RequestParam(defaultValue = "1d") String window) {
        var result = friendService.getReceivedRequests(
                ReceivedRequestsQuery.of(userId, maxSize, window)
        );
        return ApiResponse.ok(friendMapper.toResponse(result));
    }

    @Operation(summary = "친구 요청 전송", description = "상대방에게 친구 요청을 보냅니다. (Rate Limit: 10회/초)")
    @RateLimit(10)
    @PostMapping("/request")
    public ResponseEntity<Void> requestFriend(
            @Parameter(description = "요청자 ID", required = true)
            @RequestHeader("X-User-Id") Long fromAccountId,
            @Valid @RequestBody FriendRequestDto requestDto) {
        friendService.requestFriend(new FriendRequestCommand(fromAccountId, requestDto.toAccountId()));
        return ResponseEntity.ok()
                .build();
    }

    @Operation(summary = "친구 요청 수락", description = "전달받은 요청 ID를 통해 친구 요청을 수락합니다.")
    @PostMapping("/accept/{requestId}")
    public ResponseEntity<Void> acceptFriend(
            @Parameter(description = "수락자 ID", required = true)
            @RequestHeader("X-User-Id") Long accountId,
            @Parameter(description = "친구 요청 키")
            @PathVariable Long requestId) {
        friendService.acceptFriend(new FriendActionCommand(accountId, requestId));
        return ResponseEntity.ok()
                .build();
    }

    @Operation(summary = "친구 요청 거절", description = "전달받은 요청 ID를 통해 친구 요청을 거절합니다.")
    @PostMapping("/reject/{requestId}")
    public ResponseEntity<Void> rejectFriend(
            @Parameter(description = "거절할 유저의 ID", required = true)
            @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "친구 요청 PK")
            @PathVariable Long requestId) {
        friendService.rejectFriend(new FriendActionCommand(userId, requestId));
        return ResponseEntity.ok()
                .build();
    }
}