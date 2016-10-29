package com.tg.tiny4j.core.ioc.exception;

/**
 * Created by twogoods on 16/10/26.
 */
public class BeanException extends Exception{
    public BeanException(String message) {
        super(message);
    }

    public BeanException(String message, Throwable cause) {
        super(message, cause);
    }
}
