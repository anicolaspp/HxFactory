package com.anicolaspp.Hx;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;

public interface RequestContextInitializer {
    default void initialize() {
        if (!HystrixRequestContext.isCurrentThreadInitialized()) {
            HystrixRequestContext.initializeContext();
        }
    }
}
