package com.nico.Hx;

import lombok.val;
import org.junit.Test;

public class FallbackCommandTest {
    
    @Test
    public void testCommandExecute() {
        val command = Command.WithFallback.create(
                "testCommandExecute",
                () -> "hello",
                () -> "fallback"
        );
        
        assert command.execute().equals("hello");
    }
    
    @Test
    public void testCommandFallback() {
        val command = Command.WithFallback.create(
                "testCommandFallback",
                () -> {
                    throw new RuntimeException("");
                },
                () -> "fallback"
        );
        
        assert command.execute().equals("fallback");
    }
    
    @Test
    public void testFallbackTimeouts() {
        
        val command = Command.WithFallback.create(
                "testFallbackTimeouts",
                () -> {
                    Thread.sleep(10000);
                    
                    return "";
                },
                () -> "fallback",
                1000);
        
        assert command.execute().equals("fallback");
    }
    
    @Test
    public void testFallbackCommandOpensClose() throws InterruptedException {
        val cmd1 = Command.WithFallback.create(
                "testFallbackCommandOpensClose",
                () -> "hello",
                () -> "bye bye");
        
        cmd1.execute();
        
        assert !cmd1.isCircuitBreakerOpen();
        
        for (int i = 0; i < 1000; i++) {
            val failedCmd = Command.WithFallback.create(
                    "testFallbackCommandOpensClose",
                    () -> {
                        throw new RuntimeException("error");
                    },
                    () -> "fallback"
            );
            
            assert failedCmd.execute().equals("fallback");
        }
        
        Thread.sleep(1000);
        
        assert cmd1.isCircuitBreakerOpen();
        
        val ensureFallBackWhenOpenCommand = Command.WithFallback.create(
                "testFallbackCommandOpensClose",
                () -> "should not return this",
                () -> "from fall back");
        
        assert ensureFallBackWhenOpenCommand.execute().equals("from fall back");
    }
}

