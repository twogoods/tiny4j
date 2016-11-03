package com.tg.tiny4j.web.metadata;

import java.util.List;

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
    private String requestmethod;
    private boolean cros;
    private List<String> includeInterceptor;
    private List<String> excludeInterceptor;
    private List<String> doInterceptor;


}
