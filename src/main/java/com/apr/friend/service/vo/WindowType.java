package com.apr.friend.service.vo;

import java.time.LocalDateTime;
import java.util.Arrays;

public enum WindowType {
    ONE_DAY("1d"),
    SEVEN_DAYS("7d"),
    THIRTY_DAYS("30d"),
    NINETY_DAYS("90d"),
    OVER_NINETY("over");

    private final String value;

    WindowType(String value) {
        this.value = value;
    }

    public static WindowType from(String value) {
        return Arrays.stream(values())
                .filter(w -> w.value.equals(value))
                .findFirst()
                .orElse(ONE_DAY); // Default 값
    }

    public LocalDateTime getFromDateTime(LocalDateTime now) {
        return switch (this) {
            case ONE_DAY -> now.minusDays(1);
            case SEVEN_DAYS -> now.minusDays(7);
            case THIRTY_DAYS -> now.minusDays(30);
            case NINETY_DAYS -> now.minusDays(90);
            case OVER_NINETY -> LocalDateTime.MIN; // 아주 과거부터
        };
    }

    public LocalDateTime getToDateTime(LocalDateTime now) {
        return switch (this) {
            case OVER_NINETY -> now.minusDays(90).minusNanos(1); // 90일 이전까지
            default -> now;
        };
    }

    public String stringValue() {
        return this.value;
    }
}