package com.apr.context;

import com.apr.context.identity.LongIdGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class LongIdGeneratorTest {

    private static final Logger log = LoggerFactory.getLogger(LongIdGeneratorTest.class);

    @Test
    @DisplayName("매번 다른 ID 가 생성 되어야 한다")
    void shouldGenerateUniqueIds() {
        Set<Long> ids = new HashSet<>();
        for (int i = 0; i < 1000; i++) {
            long id = LongIdGenerator.nextId();
            log.info("id:{}", id);
            assertTrue(ids.add(id));
        }
    }

    @Test
    @DisplayName("매 루프마다 더 큰 ID 가 생성되어야 한다")
    void shouldBeTimeOrdered() {
        var prev = LongIdGenerator.nextId();
        var ONLY_TAIL = 1000000L;
        for (int i = 0; i < 1000; i++) {
            var next = LongIdGenerator.nextId();
            assertTrue(next >= prev);
            log.info("prev:{} next:{}", (prev % ONLY_TAIL), (next % ONLY_TAIL));
            prev = next;
        }
    }
}