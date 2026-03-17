package com.apr.context.web;

public record ApiResponse<T>(
        T data
) {
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(data);
    }

    public static ApiResponse<Void> ok() {
        return new ApiResponse<>(null);
    }
}