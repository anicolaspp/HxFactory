package com.github.anicolaspp.Hx.Setters;

import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandSetterConfigReader {
    
    private static String HX_COMMANDS_DEFAULT_TIMEOUT_IN_MILLISECONDS = "hx.commnds.defaultTimeoutInMilliseconds";
    
    private static Logger LOGGER = Logger.getLogger("CommandSetterConfigReader");
    
    public static Integer getDefaultTimeout() {
        try {
            return ConfigFactory.load().getInt(HX_COMMANDS_DEFAULT_TIMEOUT_IN_MILLISECONDS);
        } catch (ConfigException e) {
            LOGGER.log(Level.CONFIG, e, e::getMessage);
            
            return 1000;
        }
    }
}
