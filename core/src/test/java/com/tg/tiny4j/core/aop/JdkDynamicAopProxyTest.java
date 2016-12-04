package com.tg.tiny4j.core.aop;

import com.tg.tiny4j.core.aop.advice.AopAdvice;
import com.tg.tiny4j.core.aop.advice.Target;
import com.tg.tiny4j.core.aop.exception.AdviceDefinitionException;
import org.junit.Test;

import java.lang.reflect.Method;

/**
 * Created by twogoods on 16/10/24.
 */
public class JdkDynamicAopProxyTest {

    @Test
    public void test() throws Exception {
        AddOperate add=new AddOperate();
        add.setProp("jdk...");
        AopAdvice aopAdvice=new AopAdvice();
        aopAdvice.setTarget(new Target(add,AddOperate.class,AddOperate.class.getInterfaces()));
        aopAdvice.setInterceptor(new TestAopInterceptor());
        JdkDynamicAopProxy jdkDynamicAopProxy=new JdkDynamicAopProxy(aopAdvice);
        Operate addproxy=(Operate)jdkDynamicAopProxy.getProxy();
        //int result=addproxy.cal(2, 4);

//        Method m=addproxy.getClass().getDeclaredMethod("cal",int.class,int.class);
//        m.invoke(addproxy,2,4);

        addproxy.calno();
    }
}