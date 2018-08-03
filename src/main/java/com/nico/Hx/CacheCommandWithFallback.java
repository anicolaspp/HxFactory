package com.nico.Hx;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;

import java.util.function.Supplier;

class CacheCommandWithFallback<Result> extends CommandWithFallback<Result> implements RequestContextInitializer {
    
    private final String key;
    
    CacheCommandWithFallback(String key, BreakerSupplier<Result> supplier,
                             Supplier<Result> fallback,
                             Setter setter) {
        
        super(supplier, fallback, setter);
        this.key = key;
        
        initialize();
    }
    
    @Override
    protected String getCacheKey() {
        return key;
    }
    
    @Override
    public void initialize() {
        if (!HystrixRequestContext.isCurrentThreadInitialized()) {
            HystrixRequestContext.initializeContext();
        }
    }
}
