package com.github.anicolaspp.Hx;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.junit.Test;

public class RequestContextInitializerTest implements RequestContextInitializer {
    @Test
    public void testInitizalize() {
        
        assert HystrixRequestContext.isCurrentThreadInitialized() == false;
        
        initializeRequestContext();
        
        assert HystrixRequestContext.isCurrentThreadInitialized() == true;
    }
}
