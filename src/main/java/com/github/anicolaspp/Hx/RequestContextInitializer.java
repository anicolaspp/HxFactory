package com.github.anicolaspp.Hx;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;

public interface RequestContextInitializer {
    default void initializeRequestContext() {
        if (!HystrixRequestContext.isCurrentThreadInitialized()) {
            HystrixRequestContext.initializeContext();
        }
    }
}
