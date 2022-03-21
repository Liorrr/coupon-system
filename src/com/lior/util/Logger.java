package com.lior.util;


import java.util.logging.Logger;

public class Logger {

    public static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(String.valueOf(java.util.logging.Logger.class));

    public static java.util.logging.Logger getLogger() {
        return LOGGER;
    }


}
