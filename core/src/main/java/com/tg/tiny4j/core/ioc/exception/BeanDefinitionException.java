package com.tg.tiny4j.core.ioc.exception;

/**
 * Created by twogoods on 16/10/26.
 */
public class BeanDefinitionException extends Exception{
    public BeanDefinitionException(String message) {
        super(message);
    }

    public BeanDefinitionException(String message, Throwable cause) {
        super(message, cause);
    }
}
