package com.anicolaspp.Hx.conmands;

import com.anicolaspp.Hx.BreakerSupplier;
import com.netflix.hystrix.HystrixCommand;

import java.util.function.Supplier;

class CommandWithFallback<Result> extends HystrixCommand<Result> {
    
    private final BreakerSupplier<Result> supplier;
    private final Supplier<Result> fallback;
    
    CommandWithFallback(BreakerSupplier<Result> supplier,
                        Supplier<Result> fallback,
                        Setter setter) {
        super(setter);
        
        this.supplier = supplier;
        this.fallback = fallback;
    }
    
    @Override
    protected Result run() throws Exception {
        return supplier.get();
    }
    
    @Override
    public Result getFallback() {
        return fallback.get();
    }
}
