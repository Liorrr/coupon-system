package com.lior.util;


import java.util.logging.Logger;

public class Log {

    public static final Logger LOGGER = Logger.getLogger(String.valueOf(Logger.class));

    public static Logger getLogger() {
        return LOGGER;
    }


}
