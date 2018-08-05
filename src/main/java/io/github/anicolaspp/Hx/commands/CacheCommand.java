package io.github.anicolaspp.Hx.commands;

import io.github.anicolaspp.Hx.BreakerSupplier;
import io.github.anicolaspp.Hx.RequestContextInitializer;

class CacheCommand<Result> extends SingleCommand<Result>  {
    
    private final String key;
    
    CacheCommand(String key, BreakerSupplier<Result> supplier, Setter setter) {
        super(supplier, setter);
        this.key = key;
    
        RequestContextInitializer.initializeRequestContext();
    }
    
    @Override
    protected String getCacheKey() {
        return key;
    }
}

