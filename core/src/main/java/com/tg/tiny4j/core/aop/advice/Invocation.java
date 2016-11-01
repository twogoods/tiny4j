package com.tg.tiny4j.core.aop.advice;

import java.lang.reflect.Method;


/**
 * Created by twogoods on 16/10/24.
 */
interface Invocation {
    Object proceed() throws Throwable;

    Object getTarget();

    Object[] getArguments();

    Method getMethod();

    Class getObjClass();

}

