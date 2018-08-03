package com.nico.Hx;

import lombok.val;
import org.junit.Test;

public class CacheCommandTest {
    
    @Test
    public void testCache() {
        val command = Command.WithCacheContext
                .WithCacheKey
                .create("key",
                        "testCache",
                        () -> "hello"
                );
        
        assert command.execute().equals("hello");
        
        val secondCommand = Command.WithCacheContext
                .WithCacheKey
                .create("key",
                        "testCache",
                        () -> "me"
                );
        
        // notice we are executing second command but we are getting data from first one.
        val result = secondCommand.execute();
        
        assert result.equals("hello");
    }

    @Test
    public void testCacheFallback() {
        val command = Command.WithCacheContext.WithCacheKey.create(
                "testCacheFallback",
                "testCache",
                () -> {
                    throw new RuntimeException("Error");
                },
                () -> "fallback"
        );

        assert command.execute().equals("fallback");

        val secondCommand = Command.WithCacheContext.WithCacheKey.create(
                "testCacheFallback",
                "testCache",
                () -> "me"
        );

        assert secondCommand.execute().equals("fallback");
    }
}
