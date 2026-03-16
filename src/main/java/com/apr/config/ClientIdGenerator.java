package com.apr.config;


import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class ClientIdGenerator {

    private final ThreadLocal<SecureRandom> localRandom =
            ThreadLocal.withInitial(SecureRandom::new);

    public String nextHexId() {
        long v = localRandom.get().nextLong();

        char[] buf = new char[16];
        for (int i = 15; i >= 0; i--) {
            int hex = (int) (v & 0xF);
            buf[i] = Character.forDigit(hex, 16);
            v >>>= 4;
        }
        return new String(buf);
    }
}
