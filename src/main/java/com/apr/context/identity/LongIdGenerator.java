package com.apr.context.identity;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;

/*
 * 전역 ID 생성기
 * 64-bit ID: [Timestamp 32] + [Sequence 20] + [Random 12]
 * Total: 64 bits (fits into signed Java long)
 */
public class LongIdGenerator {

    private static final long DRIFT_BIT = 1;
    private static final long RANDOM_BITS = 10;
    private static final long SEQUENCE_BITS = 20;

    private static final long RANDOM_MASK = (1 << RANDOM_BITS) - 1;
    private static final long MAX_SEQUENCE = (1 << SEQUENCE_BITS) - 1;
    private static final ReentrantLock LOCK = new ReentrantLock();
    private static long lastEpochSecond = -1L;
    private static long sequence = 0L;

    private LongIdGenerator() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static long nextId() {
        long random = ThreadLocalRandom.current().nextInt() & RANDOM_MASK;
        long driftFlag = 0L;
        LOCK.lock();
        try {
            long currentSecond = System.currentTimeMillis() / 1000;

            if (currentSecond < lastEpochSecond) {
                currentSecond = lastEpochSecond;
                driftFlag = 1L;
            }

            if (currentSecond == lastEpochSecond) {
                if (++sequence > MAX_SEQUENCE) {
                    throw new IdGenerationOverflowException(MAX_SEQUENCE);
                }
            } else {
                sequence = 0;
                lastEpochSecond = currentSecond;
            }
            return (currentSecond << (SEQUENCE_BITS + DRIFT_BIT + RANDOM_BITS))
                    | (sequence << (DRIFT_BIT + RANDOM_BITS))
                    | (driftFlag << RANDOM_BITS)
                    | random;
        } finally {
            LOCK.unlock();
        }
    }
}
