package com.github.anicolaspp.Hx.Setters;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import lombok.val;

public class CommandSetter {
    public static Integer defaultTimeoutInMilliseconds = 1000;
    
    public static HystrixCommand.Setter getSetterFor(String name) {
        return getSetterFor(name, defaultTimeoutInMilliseconds);
    }
    
    public static HystrixCommand.Setter getSetterFor(String name, int timeoutInMilliseconds) {
        return getSetterFor(name, "", timeoutInMilliseconds);
    }
    
    public static HystrixCommand.Setter getSetterFor(String name, String group) {
        return getSetterFor(name, group, defaultTimeoutInMilliseconds);
    }
    
    private static HystrixCommand.Setter getSetterFor(String name, String group, int timeoutInMilliseconds) {
        val groupKey = HystrixCommandGroupKey.Factory.asKey(group);
        val nameKey = HystrixCommandKey.Factory.asKey(name);
        
        val defaultProperties = HystrixCommandProperties
                .defaultSetter()
                .withExecutionTimeoutInMilliseconds(timeoutInMilliseconds);
        
        return HystrixCommand
                .Setter
                .withGroupKey(groupKey)
                .andCommandKey(nameKey)
                .andCommandPropertiesDefaults(defaultProperties);
    }
}
