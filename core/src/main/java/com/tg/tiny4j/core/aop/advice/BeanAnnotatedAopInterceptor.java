package com.tg.tiny4j.core.aop.advice;

import com.tg.tiny4j.commons.utils.StringUtils;
import com.tg.tiny4j.commons.utils.Validate;
import com.tg.tiny4j.core.ioc.beans.BeanDefinition;
import com.tg.tiny4j.core.ioc.beans.factory.BeanFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by twogoods on 16/10/30.
 */

public class BeanAnnotatedAopInterceptor implements AopInterceptor {
    private static final Logger log = LogManager.getLogger(BeanAnnotatedAopInterceptor.class);

    private Map<String, String> methodsMap;

    Map<String, BeanDefinition> beans;

    private BeanFactory beanFactory;


    public BeanAnnotatedAopInterceptor(Map<String, String> methodInfos, BeanFactory beanFactory) {
        this.methodsMap = methodInfos;
        this.beanFactory = beanFactory;
    }

    public BeanAnnotatedAopInterceptor(Map<String, String> methodsMap, Map<String, BeanDefinition> beans, BeanFactory beanFactory) {
        this.methodsMap = methodsMap;
        this.beans = beans;
        this.beanFactory = beanFactory;
    }

    @Override
    public Object invoke(Methodinvocation invocation) throws Throwable {

        String methodName = invocation.getMethod().getName();
        if (invocation instanceof AutoSetterCglibMethodinvocation) {
            Map<String, String> autoSetter = ((AutoSetterCglibMethodinvocation) invocation).getAutoSetter();
            if (!Validate.isEmpty(autoSetter.get(methodName))) {
                log.debug("do setter method:{} ", methodName);
                Field f = invocation.getObjClass().getDeclaredField(getFieldNameBySetter(methodName));
                f.setAccessible(true);
                f.set(invocation.getTarget(), invocation.getArguments()[0]);
                return null;
            }
        }

        if (methodsMap.containsKey(methodName)) {
            log.debug("interceptor the @Bean method:{}", methodName);
            Object bean = beans.get(methodsMap.get(methodName)).getBean();
            if (bean == null) {
                //这里是调用用户方法创建的一个对象,放回容器
                Object obj = invocation.proceed();
                beans.get(methodsMap.get(methodName)).setBean(obj);
                return obj;
            } else {
                return bean;
            }
        }
        return invocation.proceed();
    }

    private String getFieldNameBySetter(String setterName) {
        return StringUtils.firstCharLowercase(setterName.substring(3));
    }
}
