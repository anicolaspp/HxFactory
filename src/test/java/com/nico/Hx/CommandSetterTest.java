package com.nico.Hx;

import lombok.val;
import org.junit.Test;

public class CommandSetterTest {
    
    @Test
    public void testSetterName() {
        val command = Command.create("command", () -> "");
        
        assert command.getCommandKey().name().equals("command");
    }
    
    @Test
    public void testSetterTimeout() {
        val command = Command.create("command", () -> "");
        
        assert command
                .getProperties()
                .executionTimeoutInMilliseconds()
                .get()
                .equals(CommandSetter.defaultTimeoutInMilliseconds);
    }
}
