package com.nico.Hx;

import com.netflix.hystrix.exception.HystrixRuntimeException;
import lombok.val;
import org.junit.Test;

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
//
//    @Test
//    public void testBreakerOpensAndCloses() throws InterruptedException {
//        val cmd1 = Command.factory().createWithName(
//                "testBreakerOpensAndCloses",
//                () -> "hello",
//                () -> "bye bye");
//
//        cmd1.execute();
//
//        assert !cmd1.isCircuitBreakerOpen();
//
//        for (int i = 0; i < 1000; i++) {
//            val failedCmd = Command.factory().createWithName(
//                    "testBreakerOpensAndCloses",
//                    () -> {
//                        throw new RuntimeException("error");
//                    },
//                    () -> "fallback"
//            );
//
//            failedCmd.execute();
//        }
//
//
//        Thread.sleep(1000);
//
//        assert cmd1.isCircuitBreakerOpen();
//
//        Thread.sleep(cmd1.getProperties().circuitBreakerSleepWindowInMilliseconds().get());
//
//        val resetCmd = Command.factory()
//                .createWithName(
//                        "testBreakerOpensAndCloses",
//                        () -> "hello");
//
//        resetCmd.execute();
//
//        Thread.sleep(1000);
//
//        assert !cmd1.getProperties().circuitBreakerForceOpen().get();
//        assert !cmd1.isCircuitBreakerOpen();
//    }
}
