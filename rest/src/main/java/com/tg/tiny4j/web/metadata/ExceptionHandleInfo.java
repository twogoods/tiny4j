package com.tg.tiny4j.web.metadata;


import java.lang.reflect.Method;

/**
 * Created by twogoods on 16/11/8.
 */
public class ExceptionHandleInfo {
    private String beanName;
    private String exceptionName;
    private Method method;

    public ExceptionHandleInfo() {
    }

    public ExceptionHandleInfo(String beanName, String exceptionName, Method method) {
        this.beanName = beanName;
        this.exceptionName = exceptionName;
        this.method = method;
    }

    public String getExceptionName() {
        return exceptionName;
    }

    public void setExceptionName(String exceptionName) {
        this.exceptionName = exceptionName;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
}
