package com.tg.web.controller;

/**
 * Created by twogoods on 16/12/1.
 */
public class TestException extends RuntimeException{
    public TestException() {
    }

    public TestException(String message) {
        super(message);
    }
}
