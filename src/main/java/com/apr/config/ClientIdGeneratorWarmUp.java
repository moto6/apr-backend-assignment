package com.apr.config;


import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClientIdGeneratorWarmUp {

    private final ClientIdGenerator generator;

    @PostConstruct
    public void warmup() throws InterruptedException {
        int threads = Runtime.getRuntime().availableProcessors();
        try (ExecutorService pool = Executors.newFixedThreadPool(threads)) {
            for (int i = 0; i < threads; i++) {
                pool.submit(generator::nextHexId);
            }
        }
    }
}
