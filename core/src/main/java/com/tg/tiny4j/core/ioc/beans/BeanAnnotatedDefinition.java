package com.tg.tiny4j.core.ioc.beans;

import com.tg.tiny4j.core.aop.advice.AopAdvice;
import com.tg.tiny4j.core.aop.advice.AopInterceptor;
import com.tg.tiny4j.core.aop.advice.Methodinvocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by twogoods on 16/10/30.
 */
public class BeanAnnotatedDefinition extends BeanDefinition{
    public BeanAnnotatedDefinition(String id, String classname) throws ClassNotFoundException {
        super(id, classname);
    }

    public BeanAnnotatedDefinition(String id, Class clazz) {
        super(id, clazz);
    }

    private Map<String,String> methodInfos=new HashMap<>();

    private AopAdvice aopAdvice;

    public void putmethodInfo(String methodName,String beanName) {
        methodInfos.put(methodName,beanName);
    }
    public void putmethodInfos(Map<String ,String> infos) {
        methodInfos.putAll(infos);
    }

    public Map<String, String> getMethodInfos() {
        return methodInfos;
    }
}
