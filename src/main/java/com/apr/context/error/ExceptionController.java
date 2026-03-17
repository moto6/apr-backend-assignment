package com.apr.context.error;

import com.apr.context.ratelimit.RateLimitExceededException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.stream.Collectors;


@RestControllerAdvice
public class ExceptionController {

    private static final Logger log = LoggerFactory.getLogger(ExceptionController.class);

    @ExceptionHandler(FriendNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleFriendNotFound(FriendNotFoundException e) {
        ApiErrorResponse body = new ApiErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(InsufficientPermissionException.class)
    public ResponseEntity<ApiErrorResponse> insufficientPermission(InsufficientPermissionException e) {
        ApiErrorResponse body = new ApiErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
    }

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<ApiErrorResponse> handleRateLimit(RateLimitExceededException e) {
        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.TOO_MANY_REQUESTS.value(),
                e.getMessage()
        );

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(error);
    }

    @ExceptionHandler(FriendRequestConflictException.class)
    public ResponseEntity<ApiErrorResponse> handleRequestConflict(FriendRequestConflictException e) {
        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.CONFLICT.value(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiErrorResponse> handleNullPointerException(NullPointerException e) {
        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "수행할 수 없는 요청입니다"
        );
        log.error("NullPointerException", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleAny(Exception e, HttpServletRequest request) {
        log.error("Unhandled exception occurred. uri={}, method={}",
                request.getRequestURI(),
                request.getMethod(),
                e);
        log.debug("Headers: {}", Collections.list(request.getHeaderNames())
                .stream()
                .collect(Collectors.toMap(h -> h, request::getHeader)));
        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                "사용에 불편을 드려 죄송합니다"
        );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
    }

}
