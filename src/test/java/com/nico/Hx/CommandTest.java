package com.nico.Hx;

import org.junit.Test;

public class CommandTest {
    
    @Test
    public void simpleTest() {
        assert Command.create(() -> "null") == null;
    }
}
