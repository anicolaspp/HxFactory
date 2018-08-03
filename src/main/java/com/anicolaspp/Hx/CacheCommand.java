package com.anicolaspp.Hx;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;

class CacheCommand<Result> extends SingleCommand<Result> implements RequestContextInitializer {
    
    private final String key;
    
    CacheCommand(String key, BreakerSupplier<Result> supplier, Setter setter) {
        super(supplier, setter);
        this.key = key;
        
        initialize();
    }
    
    @Override
    protected String getCacheKey() {
        return key;
    }
    
    private void initRequestContextIfNeeded() {
        if (!HystrixRequestContext.isCurrentThreadInitialized()) {
            HystrixRequestContext.initializeContext();
        }
    }
    
    @Override
    public void initialize() {
        initRequestContextIfNeeded();
    }
}

