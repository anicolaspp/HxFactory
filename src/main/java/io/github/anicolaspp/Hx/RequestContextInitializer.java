package io.github.anicolaspp.Hx;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;

public class RequestContextInitializer {
    public static void initializeRequestContext() {
        if (!HystrixRequestContext.isCurrentThreadInitialized()) {
            HystrixRequestContext.initializeContext();
        }
    }
}
