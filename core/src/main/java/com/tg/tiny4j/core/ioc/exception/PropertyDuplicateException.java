package com.tg.tiny4j.core.ioc.exception;

/**
 * Created by twogoods on 16/10/26.
 */
public class PropertyDuplicateException extends Exception {
    public PropertyDuplicateException(String message) {
        super(message);
    }

    public PropertyDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }
}
