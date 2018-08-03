package com.nico.Hx;

@FunctionalInterface
public interface BreakerSupplier<T> {
    T get() throws Exception;
}
