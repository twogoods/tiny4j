package com.tg.tiny4j.web.exception;

/**
 * Description:
 *
 * @author twogoods
 * @version 0.1
 * @since 2017-03-13
 */
public class ConfigurationException extends Exception{
    public ConfigurationException(String message) {
        super(message);
    }

    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
