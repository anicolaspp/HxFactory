package io.github.anicolaspp.Hx;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.junit.Test;

public class RequestContextInitializerTest {
    @Test
    public void testInitizalize() {
        
        assert HystrixRequestContext.isCurrentThreadInitialized() == false;
        
        RequestContextInitializer.initializeRequestContext();
        
        assert HystrixRequestContext.isCurrentThreadInitialized() == true;
    }
}
