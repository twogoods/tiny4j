package com.tg.tiny4j.core.aop;

import com.tg.tiny4j.core.aop.advice.AopAdvice;
import com.tg.tiny4j.core.aop.advice.Target;
import com.tg.tiny4j.core.aop.exception.AdviceDefinitionException;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Created by twogoods on 16/10/24.
 */
public class CglibDynamicAopProxyTest {
    @Test
    public void test() throws Exception {
        AopAdvice aopAdvice=new AopAdvice();
        aopAdvice.setTarget(new Target(Add.class));
        aopAdvice.setInterceptor(new TestAopInterceptor());
        AutoSetterCglibAopProxy cglibDynamicAopProxy=new AutoSetterCglibAopProxy(aopAdvice);
        Add operate=(Add) cglibDynamicAopProxy.getProxy();
        System.out.println(operate.add(1, 2));

        System.out.println("*******");
        Method[] methods=operate.getClass().getMethods();
        for(Method m:methods){
            System.out.println(m);
        }
        System.out.println("*******");


        Method setM=operate.getClass().getMethod("setProp", String.class);
        System.out.println(setM);
        setM.invoke(operate, "set");
        System.out.println(operate.getProp());

//        Method m=operate.getClass().getMethod("add", Integer.TYPE, Integer.TYPE);
//        m.setAccessible(true);
//        Object result=m.invoke(operate, 1, 2);
//        System.out.println(result instanceof Integer);
    }
}