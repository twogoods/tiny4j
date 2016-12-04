package com.tg.tiny4j.core.aop;

import com.tg.tiny4j.commons.utils.Validate;
import com.tg.tiny4j.core.aop.advice.AopAdvice;
import com.tg.tiny4j.core.aop.advice.Methodinvocation;
import com.tg.tiny4j.core.aop.exception.AdviceDefinitionException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by twogoods on 16/10/24.
 */
public class JdkDynamicAopProxy extends AbstractAopProxy implements InvocationHandler {

    public JdkDynamicAopProxy(AopAdvice aopAdvice) {
        super(aopAdvice);
    }

    @Override
    public Object getProxy() throws AdviceDefinitionException {
        checkAopAdvice();
        return Proxy.newProxyInstance(getClass().getClassLoader(), aopAdvice.getTarget().getInterfaceClazz(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (aopAdvice.getInterceptor() == null) {
            return method.invoke(aopAdvice.getTarget().getTargetObj(), args);
        } else {
            return aopAdvice.getInterceptor().invoke(new Methodinvocation(aopAdvice.getTarget().getTargetObj(), method, args));
        }
    }
}
