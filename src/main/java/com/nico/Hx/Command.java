package com.nico.Hx;

import com.netflix.hystrix.HystrixCommand;

import java.util.function.Supplier;

public class Command {
    
    private Command() { }
    
    public static <Result> HystrixCommand<Result> create(String name, BreakerSupplier<Result> fn) {
        return new SingleCommand<>(fn, CommandSetter.getSetterFor(name));
    }
    
    public static <Result> HystrixCommand<Result> create(String name, BreakerSupplier<Result> fn, int timeoutInMilliseconds) {
        return new SingleCommand<>(fn, CommandSetter.getSetterFor(name, timeoutInMilliseconds));
    }
    
    static class WithFallback {
        public static <Result> HystrixCommand<Result> create(String name, BreakerSupplier<Result> fn, Supplier<Result> fallback) {
            return new CommandWithFallback<>(fn, fallback, CommandSetter.getSetterFor(name));
        }
    
        public static <Result> HystrixCommand<Result> create(String name, BreakerSupplier<Result> fn, Supplier<Result> fallback, int timeoutInMilliseconds) {
            return new CommandWithFallback<>(fn, fallback, CommandSetter.getSetterFor(name, timeoutInMilliseconds));
        }
    }
    
    static class WithCacheContext {
        
        static class WithCacheKey {
            
            public static  <Result> HystrixCommand<Result> create(String cacheKey,
                                                                  String commandName,
                                                                  BreakerSupplier<Result> supplier) {
                
                return new CacheCommand<>(cacheKey, supplier, CommandSetter.getSetterFor(commandName));
            }
    
            public static <Result> HystrixCommand<Result> create(String cacheKey,
                                                                      String commandName,
                                                                      BreakerSupplier<Result> supplier,
                                                                      Supplier<Result> fallback) {
                
                return new CacheCommandWithFallback<>(cacheKey, supplier, fallback, CommandSetter.getSetterFor(commandName));
            }
        }
    }
}

