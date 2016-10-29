package com.tg.tiny4j.core.aop;

import com.tg.tiny4j.core.aop.advice.AopAdvice;
import com.tg.tiny4j.core.aop.advice.Target;
import com.tg.tiny4j.core.aop.exception.AdviceDefinitionException;
import org.junit.Test;

/**
 * Created by twogoods on 16/10/24.
 */
public class JdkDynamicAopProxyTest {

    @Test
    public void test() throws AdviceDefinitionException {
        Operate add=new Add();
        AopAdvice aopAdvice=new AopAdvice();
        aopAdvice.setTarget(new Target(add,Add.class,Add.class.getInterfaces()));
        aopAdvice.setInterceptor(new TestAopInterceptor());
        JdkDynamicAopProxy jdkDynamicAopProxy=new JdkDynamicAopProxy(aopAdvice);
        Operate addproxy=(Operate)jdkDynamicAopProxy.getProxy();
        int result=addproxy.add(2,4);
        result=addproxy.add(8);
    }
}