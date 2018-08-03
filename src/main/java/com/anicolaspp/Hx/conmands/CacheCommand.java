package com.anicolaspp.Hx.conmands;

import com.anicolaspp.Hx.BreakerSupplier;
import com.anicolaspp.Hx.RequestContextInitializer;

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
}

