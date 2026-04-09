package com.mitocode.external.visa.chaos;

import lombok.Getter;

import java.util.concurrent.atomic.AtomicInteger;

public class ChaosState {

    @Getter
    private ChaosConfig config = ChaosConfig.disabled();
    private final AtomicInteger counter = new AtomicInteger(0);

    public void update(ChaosConfig config) {
        this.config = config;
        counter.set(0);
    }

    public boolean shouldFail() {
        return counter.incrementAndGet() <= config.failTimes();
    }

}
