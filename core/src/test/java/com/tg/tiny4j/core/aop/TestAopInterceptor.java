package com.tg.tiny4j.core.aop;

import com.tg.tiny4j.commons.utils.StringUtils;
import com.tg.tiny4j.core.aop.advice.AopInterceptor;
import com.tg.tiny4j.core.aop.advice.Methodinvocation;
import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Created by twogoods on 16/10/24.
 */
public class TestAopInterceptor implements AopInterceptor {
    @Override
    public Object invoke(Methodinvocation invocation) throws Throwable {


        String methodName=invocation.getMethod().getName();
        if(methodName.startsWith("set")){
            Field f=invocation.getObjClass().getDeclaredField(getFieldNameBySetter(methodName));
            f.setAccessible(true);
            f.set(invocation.getTarget(), invocation.getArguments()[0]);
            System.out.println("do setter....");
            return null;
        }

        System.out.println(String.format("***--before--method:{%s},args:{%s}",invocation.getMethod().getName(), Arrays.asList(invocation.getArguments())));
        Object result=invocation.proceed();
        System.out.println(String.format("***--after--result:{%s}",result));
        return result;
    }

    private String getFieldNameBySetter(String setterName){
        return StringUtils.firstCharLowercase(setterName.substring(3));
    }
}
