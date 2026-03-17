package com.apr.context.ratelimit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class TokenBucketTest {
    @Test
    @DisplayName("초당 10회 제한일 때, 10번까지는 성공하고 11번째는 실패해야 한다")
    void tryConsume_LimitTest() {
        // Given: 초당 10개 한도의 버킷 생성
        TokenBucket bucket = new TokenBucket(10);

        // When & Then: 10번 소모 시도
        for (int i = 0; i < 10; i++) {
            assertThat(bucket.tryConsume()).isTrue();
        }

        // 11번째는 실패해야 함
        assertThat(bucket.tryConsume()).isFalse();
    }

    @Test
    @DisplayName("시간이 지나면 토큰이 충전(Refill)되어 다시 사용 가능해야 한다")
    void tryConsume_RefillTest() throws InterruptedException {
        // Given: 초당 10개 한도, 모두 소모
        TokenBucket bucket = new TokenBucket(10);
        for (int i = 0; i < 10; i++) bucket.tryConsume();
        assertThat(bucket.tryConsume()).isFalse();

        // When: 1.1초 대기 (충분한 충전 시간)
        Thread.sleep(1100);

        // Then: 다시 소모 가능
        assertThat(bucket.tryConsume()).isTrue();
    }
}