package com.tg.tiny4j.core.aop;

import com.tg.tiny4j.core.aop.advice.AopAdvice;
import com.tg.tiny4j.core.aop.exception.AdviceDefinitionException;

/**
 * Created by twogoods on 16/10/24.
 */
public abstract class AbstractAopProxy implements AopProxy{

    protected AopAdvice aopAdvice;

    public AbstractAopProxy(AopAdvice aopAdvice) {
        this.aopAdvice = aopAdvice;
    }

    public void checkAopAdvice() throws AdviceDefinitionException {
        if(aopAdvice.getTarget()==null){
            throw new AdviceDefinitionException("AopAdvice's target can't be null");
        }
    }
}
