package com.github.anicolaspp.Hx.commands;

import com.github.anicolaspp.Hx.BreakerSupplier;
import com.netflix.hystrix.HystrixCommand;

class SingleCommand<Result> extends HystrixCommand<Result> {
    
    private final BreakerSupplier<Result> supplier;
    
    SingleCommand(BreakerSupplier<Result> supplier, Setter setter) {
        super(setter);
        this.supplier = supplier;
    }
    
    @Override
    protected Result run() throws Exception {
        return supplier.get();
    }
}



