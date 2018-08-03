package com.nico.Hx;

import com.netflix.hystrix.HystrixCommand;

public class Command {
    
    private Command() { }
    
    public static <Result> HystrixCommand<Result> create(String name, BreakerSupplier<Result> fn) {
        return new SingleCommand<>(fn, CommandSetter.getSetterFor(name));
    }
}

