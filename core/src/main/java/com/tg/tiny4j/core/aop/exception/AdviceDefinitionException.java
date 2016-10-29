package com.tg.tiny4j.core.aop.exception;

/**
 * Created by twogoods on 16/10/25.
 */
public class AdviceDefinitionException extends Exception {
    public AdviceDefinitionException(String message) {
        super(message);
    }

    public AdviceDefinitionException(String message, Throwable cause) {
        super(message, cause);
    }
}
