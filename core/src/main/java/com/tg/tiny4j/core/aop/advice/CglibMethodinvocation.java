package com.tg.tiny4j.core.aop.advice;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created by twogoods on 16/10/25.
 */
public class CglibMethodinvocation extends Methodinvocation{
    private MethodProxy methodProxy;

    public CglibMethodinvocation(Object target, Method method, Object[] args,MethodProxy methodProxy) {
        super(target, method, args);
        this.methodProxy=methodProxy;
    }

    @Override
    public Object proceed() throws Throwable {
        return methodProxy.invokeSuper(target,args);
    }
}
