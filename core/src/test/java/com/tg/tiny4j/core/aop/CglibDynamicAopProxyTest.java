package com.tg.tiny4j.core.aop;

import com.tg.tiny4j.core.aop.advice.AopAdvice;
import com.tg.tiny4j.core.aop.advice.Target;
import com.tg.tiny4j.core.aop.exception.AdviceDefinitionException;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.junit.Test;

import java.io.File;
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
        AopAdvice aopAdvice = new AopAdvice();
        aopAdvice.setTarget(new Target(Add.class));
        aopAdvice.setInterceptor(new TestAopInterceptor());
        CglibAopProxy cglibDynamicAopProxy = new CglibAopProxy(aopAdvice);
        Add operate = (Add) cglibDynamicAopProxy.getProxy();
        System.out.println(operate.add(1, 2));
    }



    @Test
    public void originTest() throws Exception {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(Add.class);
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy)
                    throws Throwable {
                if (method.getDeclaringClass() != Object.class && method.getReturnType() == String.class) {
                    return "Hello cglib!";
                } else {
                    return proxy.invokeSuper(obj, args);
                }
            }
        });
        Add proxy = (Add) enhancer.create();
        System.out.println(proxy.getProp());
        System.out.println(proxy.getClass().getSuperclass());
    }

    @Test
    public void anotherTest() throws Exception {
        AopAdvice aopAdvice = new AopAdvice();
        aopAdvice.setTarget(new Target(Add.class));
        aopAdvice.setInterceptor(new TestAopInterceptor());
        CglibAopProxy cglibDynamicAopProxy = new CglibAopProxy(aopAdvice);
        Add operate = (Add) cglibDynamicAopProxy.getProxy();
        System.out.println("--------------field-------------");
        Field[] fields = operate.getClass().getFields();
        for (Field f : fields) {
            System.out.println(f);
        }
        //上面无法得到属性的field,利用这种方式给代理对象赋值
        Field f=Add.class.getDeclaredField("prop");
        f.setAccessible(true);
        f.set(operate,"twogoods");
        System.out.println(operate.getProp());
    }

    @Test
    public void autoSetTest() throws Exception {
        AopAdvice aopAdvice = new AopAdvice();
        aopAdvice.setTarget(new Target(Add.class));
        //TestAopInterceptor拦截了set方法,实现本质上与上一个测试类是一样的
        aopAdvice.setInterceptor(new TestAopInterceptor());
        AutoSetterCglibAopProxy cglibDynamicAopProxy = new AutoSetterCglibAopProxy(aopAdvice);
        Add operate = (Add) cglibDynamicAopProxy.getProxy();

        System.out.println(operate.add(1, 2));

        System.out.println("*******");
        Method setM = operate.getClass().getMethod("setProp", String.class);
        System.out.println(setM);
        setM.invoke(operate, "twogoods");
        System.out.println(operate.getProp());
    }
}