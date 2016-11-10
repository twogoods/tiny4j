package com.tg.tiny4j.web.metadata;

import com.tg.tiny4j.commons.utils.Validate;
import com.tg.tiny4j.web.exception.ExceptionHandleException;
import com.tg.tiny4j.web.exception.InterceptorDuplicatedException;

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
    //异常信息
    private Map<String, Set<ExceptionHandleInfo>> exceptionHandles = new HashMap<>();

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

    public void addRequestHandleMap(String name, RequestHandleInfo requestHandleInfo) {
        this.requestHandleMap.put(name, requestHandleInfo);
    }

    public void addExceptionHandle(String className, ExceptionHandleInfo exceptionHandleInfo) throws ExceptionHandleException {
        Set<ExceptionHandleInfo> set = exceptionHandles.get(className);
        if (Validate.isEmpty(set)) {
            set = new HashSet<>();
            set.add(exceptionHandleInfo);
            exceptionHandles.put(className, set);
        } else {
            if (!set.add(exceptionHandleInfo)) {
                throw new ExceptionHandleException(String.format("%s duplicated exceptionhandle for '%s'", className, exceptionHandleInfo.getExceptionName()));
            }
        }
    }

    public Map<String, ControllerInfo> getApis() {
        return apis;
    }

    public void addInterceptors(String name, InterceptorInfo interceptor) throws InterceptorDuplicatedException {
        if(this.interceptors.put(name,interceptor)!=null){
            throw new InterceptorDuplicatedException(String.format("name '%s' is Duplicated, check interceptor '%s'",name,interceptor.getClassName()));
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

    public Map<String, Set<ExceptionHandleInfo>> getExceptionHandles() {
        return exceptionHandles;
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
