package com.tg.tiny4j.web.exception;

/**
 * Created by twogoods on 16/11/9.
 */
public class InterceptorDuplicatedException extends Exception{
    public InterceptorDuplicatedException(String message) {
        super(message);
    }

    public InterceptorDuplicatedException(String message, Throwable cause) {
        super(message, cause);
    }
}
