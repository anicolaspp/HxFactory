package io.github.anicolaspp.Hx.commands;

import io.github.anicolaspp.Hx.BreakerSupplier;
import io.github.anicolaspp.Hx.RequestContextInitializer;

import java.util.function.Supplier;

class CacheCommandWithFallback<Result> extends CommandWithFallback<Result>  {
    
    private final String key;
    
    CacheCommandWithFallback(String key, BreakerSupplier<Result> supplier,
                             Supplier<Result> fallback,
                             Setter setter) {
        
        super(supplier, fallback, setter);
        this.key = key;
    
        RequestContextInitializer.initializeRequestContext();
    }
    
    @Override
    protected String getCacheKey() {
        return key;
    }
}
