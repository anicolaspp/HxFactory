package com.anicolaspp.Hx;

import com.netflix.hystrix.HystrixCommand;

import java.util.function.Supplier;

class CommandWithFallback<Result> extends HystrixCommand<Result> {
    
    protected final BreakerSupplier<Result> supplier;
    protected final Supplier<Result> fallback;
    
    protected CommandWithFallback(BreakerSupplier<Result> supplier,
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
