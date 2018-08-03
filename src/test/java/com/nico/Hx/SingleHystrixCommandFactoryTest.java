package com.nico.Hx;

import com.netflix.hystrix.exception.HystrixRuntimeException;
import lombok.val;
import org.junit.Test;

import static junit.framework.TestCase.fail;

public class SingleHystrixCommandFactoryTest {

    @Test
    public void testCommandExecute() {
        val command = Command.create("testCommandExecute", () -> "hello");

        assert command.execute().equals("hello");
    }

    @Test
    public void testCommandKey() {
        val command = Command.create("testCommandKey", () -> "hello");

        assert command
                .getCommandKey()
                .name()
                .equals("testCommandKey");
    }

    @Test
    public void testHystrixExceptionIsThrown() {
        try {
            val command = Command.create("testHystrixExceptionIsThrown",
                    () -> {
                        throw new RuntimeException("error");
                    });

            command.execute();

            assert false;
        } catch (HystrixRuntimeException e) {
            assert true;
        }
    }

    @Test
    public void testBreakerIsEnabled() {

        val command = Command.create("testBreakerIsEnabled", () -> "hello");

        assert command.getProperties().circuitBreakerEnabled().get();
    }
    
    @Test
    public void testTimeout() {
        try {
            val command = Command.create(
                    "testTimeout",
                    () -> {
                        Thread.sleep(10000);
                        
                        return "";
                    },
                    1000);
            
            command.execute();
            
            fail("Command did not timeout");
        } catch (HystrixRuntimeException e) {
    
            System.out.println(e);
            
            assert true;
        }
    }
}

