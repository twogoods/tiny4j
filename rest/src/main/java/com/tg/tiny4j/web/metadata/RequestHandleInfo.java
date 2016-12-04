package com.tg.tiny4j.web.metadata;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by twogoods on 16/11/2.
 */
public class RequestHandleInfo {
    /**
     * url,controller对象,方法,请求方法,拦截器信息
     * url对应一个请求方法<methodname,objname(controller名),requestmethod(请求方法),是否CROS,拦截器信息>
     * 在一个map,存controller对象,或者ioc里拿
     */
    private String methodName;
    private Method method;
    private String className;
    private String beanName;
    private Object instance;
    private String requestUrl;
    private String requestmethod;
    private UrlPraseInfo urlPraseInfo;
    private CrosInfo cros;
    private List<String> includeInterceptors = new ArrayList<>();
    private List<String> excludeInterceptors = new ArrayList<>();
    private Map<String, String> doInterceptors = new HashMap<>();

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public Object getInstance() {
        return instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRequestmethod() {
        return requestmethod;
    }

    public void setRequestmethod(String requestmethod) {
        this.requestmethod = requestmethod;
    }

    public UrlPraseInfo getUrlPraseInfo() {
        return urlPraseInfo;
    }

    public void setUrlPraseInfo(UrlPraseInfo urlPraseInfo) {
        this.urlPraseInfo = urlPraseInfo;
    }

    public CrosInfo getCros() {
        return cros;
    }

    public void setCros(CrosInfo cros) {
        this.cros = cros;
    }

    public List<String> getIncludeInterceptors() {
        return includeInterceptors;
    }

    public void setIncludeInterceptors(String[] includeInterceptors) {
        this.includeInterceptors.addAll(Arrays.asList(includeInterceptors));
    }

    public void setExcludeInterceptors(String[] excludeInterceptors) {
        this.excludeInterceptors.addAll(Arrays.asList(excludeInterceptors));
    }

    public List<String> getExcludeInterceptors() {
        return excludeInterceptors;
    }

    public Map<String, String> getDoInterceptors() {
        return doInterceptors;
    }

    public void addDoInterceptors(String interceptor) {
        this.doInterceptors.put(interceptor, interceptor);
    }
}
