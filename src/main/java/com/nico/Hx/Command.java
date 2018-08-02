package com.nico.Hx;

import com.netflix.hystrix.HystrixCommand;

import java.util.function.Supplier;

public class Command {
    
    private Command() {}
    
    public static <Result> HystrixCommand<Result> create(Supplier<Result> fn) {
        return null;
    }
}

