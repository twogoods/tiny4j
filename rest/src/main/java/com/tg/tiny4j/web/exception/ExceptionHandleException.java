package com.tg.tiny4j.web.exception;

/**
 * Created by twogoods on 16/11/8.
 */
public class ExceptionHandleException extends Exception{

    public ExceptionHandleException(String message) {
        super(message);
    }

    public ExceptionHandleException(String message, Throwable cause) {
        super(message, cause);
    }
}
