package com.tg.tiny4j.core.aop.advice;

import java.lang.reflect.Method;

/**
 * Created by twogoods on 16/10/24.
 */
public class Methodinvocation implements Invocation {

    protected Object target;
    protected Method method;
    protected Object[] args;

    public Methodinvocation(Object target, Method method, Object[] args) {
        this.target = target;
        this.method = method;
        this.args = args;
    }

    @Override
    public Object proceed() throws Throwable{
        return method.invoke(target,args);
    }

    @Override
    public Object getTarget() {
        return target;
    }

    @Override
    public Object[] getArguments() {
        return args;
    }

    @Override
    public Method getMethod() {
        return method;
    }
}
