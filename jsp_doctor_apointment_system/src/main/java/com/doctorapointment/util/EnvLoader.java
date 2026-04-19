package com.doctorapointment.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnvLoader {
    private static final Logger log = LoggerFactory.getLogger(EnvLoader.class);
        
    public static String get(String key) {
        String value = System.getenv(key);
        
        if (value == null) {
            log.warn("Environment variable '{}' is not set", key);
        } else {
            log.debug("Environment variable '{}' loaded successfully", key);
        }
        
        return value;
    }
}