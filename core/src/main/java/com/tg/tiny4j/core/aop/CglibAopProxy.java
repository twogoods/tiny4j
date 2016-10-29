package com.tg.tiny4j.core.aop;

import com.tg.tiny4j.core.aop.advice.AopAdvice;
import com.tg.tiny4j.core.aop.advice.CglibMethodinvocation;
import com.tg.tiny4j.core.aop.exception.AdviceDefinitionException;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created by twogoods on 16/10/24.
 */
public class CglibAopProxy extends AbstractAopProxy implements MethodInterceptor {
    private Enhancer enhancer = new Enhancer();

    public CglibAopProxy(AopAdvice aopAdvice) {
        super(aopAdvice);
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        if (aopAdvice.getInterceptor() == null) {
            return methodProxy.invokeSuper(o, objects);
        } else {
            return aopAdvice.getInterceptor().invoke(new CglibMethodinvocation(aopAdvice.getTarget().getTargetObj(), method, objects,methodProxy));
        }
    }

    @Override
    public Object getProxy() throws AdviceDefinitionException {
        checkAopAdvice();
        enhancer.setSuperclass(aopAdvice.getTarget().getClazz());
        enhancer.setCallback(this);
        return enhancer.create();
    }


}
