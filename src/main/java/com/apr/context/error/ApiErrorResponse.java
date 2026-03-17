package com.apr.context.error;

public record ApiErrorResponse(
        int code,
        String message
) {
}
