package com.tg.tiny4j.web.metadata;

import com.tg.tiny4j.web.exception.ExceptionHandleException;
import com.tg.tiny4j.web.exception.InterceptorDuplicatedException;
import com.tg.tiny4j.web.exception.UrlDuplicatedException;

import java.util.*;

/**
 * Created by twogoods on 16/11/8.
 */
public class RequestMapper {
    //Controller 类信息和实例化的对象
    private Map<String, ControllerInfo> apis = new HashMap<>();
    //拦截器信息
    private Map<String, InterceptorInfo> interceptors = new HashMap<>();
    //list的顺序就是拦截顺序
    private List<InterceptorInfo> interceptorList = new ArrayList<>();
    //处理请求的信息
    private Map<String, RequestHandleInfo> requestHandleMap = new HashMap<>();

    private List<String> urlPraseResult = new ArrayList<>();

    //异常信息
    private Map<String, Map<String, ExceptionHandleInfo>> exceptionHandles = new HashMap<>();

    public void sortInterceptorList() {
        Collections.sort(interceptorList, new Comparator<InterceptorInfo>() {
            @Override
            public int compare(InterceptorInfo o1, InterceptorInfo o2) {
                return o1.getOrder() - o2.getOrder();
            }
        });
    }

    public void addApis(String name, ControllerInfo controllerInfo) {
        this.apis.put(name, controllerInfo);
    }

    public void addInterceptorList(InterceptorInfo interceptorInfo) {
        this.interceptorList.add(interceptorInfo);
    }

    public void addRequestHandleMap(String url, RequestHandleInfo requestHandleInfo) throws UrlDuplicatedException {
        if (this.requestHandleMap.putIfAbsent(url, requestHandleInfo) != null) {
            throw new UrlDuplicatedException(String.format("check duplicated url : '%s'", url.replace("\\w*", "**")));
        }
    }

    public void addExceptionHandle(String className, ExceptionHandleInfo exceptionHandleInfo) throws ExceptionHandleException {
        Map<String, ExceptionHandleInfo> map = exceptionHandles.get(className);
        if (map == null) {
            map = new HashMap<>();
            map.put(exceptionHandleInfo.getExceptionName(), exceptionHandleInfo);
            exceptionHandles.put(className, map);
        } else {
            if (map.putIfAbsent(exceptionHandleInfo.getExceptionName(), exceptionHandleInfo) != null) {
                throw new ExceptionHandleException(String.format("%s duplicated exceptionhandle for '%s'", className, exceptionHandleInfo.getExceptionName()));
            }
        }
    }

    public Map<String, ControllerInfo> getApis() {
        return apis;
    }

    public void addInterceptors(String name, InterceptorInfo interceptor) throws InterceptorDuplicatedException {
        if (this.interceptors.put(name, interceptor) != null) {
            throw new InterceptorDuplicatedException(String.format("name '%s' is Duplicated, check interceptor '%s'", name, interceptor.getClassName()));
        }
    }

    public Map<String, InterceptorInfo> getInterceptors() {
        return interceptors;
    }

    public List<InterceptorInfo> getInterceptorList() {
        return interceptorList;
    }

    public Map<String, RequestHandleInfo> getRequestHandleMap() {
        return requestHandleMap;
    }

    public Map<String, Map<String, ExceptionHandleInfo>> getExceptionHandles() {
        return exceptionHandles;
    }

    public List<String> getUrlPraseResult() {
        return urlPraseResult;
    }

    public void addUrlPraseResult(String prasedUrl) throws UrlDuplicatedException {
        for (String s : urlPraseResult) {
            if (s.equals(prasedUrl)) {
                throw new UrlDuplicatedException(String.format("url duplicated ! two url: '%s' , '%s'", prasedUrl.replace("\\w*", "**"), s.replace("\\w*", "**")));
            }
        }
        urlPraseResult.add(prasedUrl);
    }

    @Override
    public String toString() {
        return "RequestMapper{" +
                "apis=" + apis +
                ", interceptors=" + interceptors +
                ", interceptorList=" + interceptorList +
                ", requestHandleMap=" + requestHandleMap +
                ", exceptionHandles=" + exceptionHandles +
                '}';
    }
}
