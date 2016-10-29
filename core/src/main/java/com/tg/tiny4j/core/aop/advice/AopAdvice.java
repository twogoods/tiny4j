package com.tg.tiny4j.core.aop.advice;

/**
 * Created by twogoods on 16/10/25.
 */
public class AopAdvice {
    private Target target;
    private AopInterceptor interceptor;

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public AopInterceptor getInterceptor() {
        return interceptor;
    }

    public void setInterceptor(AopInterceptor interceptor) {
        this.interceptor = interceptor;
    }
}
