package com.nico.Hx;

import lombok.val;
import org.junit.Test;

public class CommandSetterTest {
    
    @Test
    public void testSetterName() {
        val command = Command.create("testSetterName", () -> "");
        
        assert command.getCommandKey().name().equals("command");
    }
    
    @Test
    public void testSetterDefaultTimeout() {
        val command = Command.create("testSetterDefaultTimeout", () -> "");
        
        assert command
                .getProperties()
                .executionTimeoutInMilliseconds()
                .get()
                .equals(CommandSetter.defaultTimeoutInMilliseconds);
    }
    
    @Test
    public void testSetterCustomTimeout() {
        val command = Command.create("testSetterCustomTimeout", () -> "", 23000);
        
        assert command
                .getProperties()
                .executionTimeoutInMilliseconds()
                .get()
                .equals(23000);
    }
}
