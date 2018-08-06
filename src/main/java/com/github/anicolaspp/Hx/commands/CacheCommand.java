package com.github.anicolaspp.Hx.commands;

import com.github.anicolaspp.Hx.BreakerSupplier;
import com.github.anicolaspp.Hx.RequestContextInitializer;

class CacheCommand<Result> extends SingleCommand<Result> implements RequestContextInitializer {
    
    private final String key;
    
    CacheCommand(String key, BreakerSupplier<Result> supplier, Setter setter) {
        super(supplier, setter);
        this.key = key;
    
        initializeRequestContext();
    }
    
    @Override
    protected String getCacheKey() {
        return key;
    }
}

