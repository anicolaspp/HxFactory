package com.github.anicolaspp.Hx;

@FunctionalInterface
public interface BreakerSupplier<T> {
    T get() throws Exception;
}
