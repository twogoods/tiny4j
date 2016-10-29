package com.tg.tiny4j.core.aop.advice;

/**
 * Created by twogoods on 16/10/24.
 */
public interface AopInterceptor {

    Object invoke(Methodinvocation invocation) throws Throwable;

}

