package com.tg.tiny4j.web.metadata;

import com.tg.tiny4j.commons.constants.HttpMethod;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by twogoods on 16/11/2.
 */
public class RequestHandleInfo {
    /**url,controller对象,方法,请求方法,拦截器信息
     * url对应一个请求方法<methodname,objname(controller名),requestmethod(请求方法),是否CROS,拦截器信息>
     * 在一个map,存controller对象,或者ioc里拿
     */
    private String methodName;
    private String className;
    private Object instance;
    private String requestmethod= HttpMethod.GET;
    private boolean cros;
    private List<String> includeInterceptors;
    private List<String> excludeInterceptors;
    private Map<String,String> doInterceptors;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Object getInstance() {
        return instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }

    public String getRequestmethod() {
        return requestmethod;
    }

    public void setRequestmethod(String requestmethod) {
        this.requestmethod = requestmethod;
    }

    public boolean isCros() {
        return cros;
    }

    public void setCros(boolean cros) {
        this.cros = cros;
    }

    public List<String> getIncludeInterceptors() {
        return includeInterceptors;
    }

    public void setIncludeInterceptors(String[] includeInterceptors) {
        this.includeInterceptors= Arrays.asList(includeInterceptors);
    }

    public void setExcludeInterceptors(String[] excludeInterceptors) {
        this.excludeInterceptors = Arrays.asList(excludeInterceptors);
    }

    public List<String> getExcludeInterceptors() {
        return excludeInterceptors;
    }

    public Map<String, String> getDoInterceptors() {
        return doInterceptors;
    }
}
