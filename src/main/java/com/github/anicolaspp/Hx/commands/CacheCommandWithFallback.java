package com.github.anicolaspp.Hx.commands;

import com.github.anicolaspp.Hx.BreakerSupplier;
import com.github.anicolaspp.Hx.RequestContextInitializer;

import java.util.function.Supplier;

class CacheCommandWithFallback<Result> extends CommandWithFallback<Result> implements RequestContextInitializer {
    
    private final String key;
    
    CacheCommandWithFallback(String key, BreakerSupplier<Result> supplier,
                             Supplier<Result> fallback,
                             Setter setter) {
        
        super(supplier, fallback, setter);
        this.key = key;
    
        initializeRequestContext();
    }
    
    @Override
    protected String getCacheKey() {
        return key;
    }
}
