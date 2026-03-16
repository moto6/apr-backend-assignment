package com.apr.context.advice;

public record ApiErrorResponse(
        int code,
        String message
) {
}
