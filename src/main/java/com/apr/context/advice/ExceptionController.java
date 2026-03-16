package com.apr.context.advice;

import com.apr.friend.domain.InsufficientPermissionException;
import com.apr.friend.service.impl.FriendNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

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
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}
