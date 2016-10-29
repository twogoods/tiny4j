package com.tg.tiny4j.core.ioc.exception;

/**
 * Created by twogoods on 16/10/29.
 */
public class ConfigurationException extends Exception{
    public ConfigurationException(String message) {
        super(message);
    }

    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
