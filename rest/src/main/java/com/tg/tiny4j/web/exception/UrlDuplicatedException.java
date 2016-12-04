package com.tg.tiny4j.web.exception;


/**
 * Created by twogoods on 16/12/3.
 */
public class UrlDuplicatedException extends Exception{
    public UrlDuplicatedException(String message) {
        super(message);
    }

    public UrlDuplicatedException(String message, Throwable cause) {
        super(message, cause);
    }
}
