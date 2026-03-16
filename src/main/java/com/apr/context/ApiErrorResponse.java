package com.apr.context;

public record ApiErrorResponse(
        int code,
        String message
) {
}
