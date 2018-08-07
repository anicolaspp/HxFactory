package com.github.anicolaspp.Hx.Setters;

import com.github.anicolaspp.Hx.commands.Command;
import com.netflix.hystrix.HystrixCommandProperties;
import lombok.val;
import org.junit.Test;

public class CommandFromSetter {
    
    @Test
    public void testCommandFromSetter() {
        val customSetter = CommandSetter
                .getSetterFor("customSetterCommand")
                .andCommandPropertiesDefaults(
                        HystrixCommandProperties
                                .defaultSetter()
                                .withCircuitBreakerRequestVolumeThreshold(20)
                                .withCircuitBreakerEnabled(false)
                                .withFallbackEnabled(false)
                );
        
        val command = Command.fromSetter(customSetter, () -> "hello");
        
        assert command.getProperties().circuitBreakerRequestVolumeThreshold().get() == 20;
        assert command.getProperties().circuitBreakerEnabled().get() == false;
        assert command.getProperties().fallbackEnabled().get() == false;
        
        assert command.execute().equals("hello");
    }
    
}

