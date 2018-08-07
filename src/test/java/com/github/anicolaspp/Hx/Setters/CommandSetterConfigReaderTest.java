package com.github.anicolaspp.Hx.Setters;

import org.junit.Test;

public class CommandSetterConfigReaderTest {
    @Test
    public void testSetterShouldUserTimeoutFromConfig() {
        
        assert CommandSetterConfigReader.getDefaultTimeout() == 5000;
    }
}
