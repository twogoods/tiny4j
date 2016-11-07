package com.tg.tiny4j.web.reader;

import com.tg.tiny4j.commons.data.Pair;
import com.tg.tiny4j.commons.utils.StringUtils;
import com.tg.tiny4j.web.annotation.*;
import com.tg.tiny4j.web.metadata.ControllerInfo;
import com.tg.tiny4j.web.metadata.InterceptorInfo;
import com.tg.tiny4j.web.metadata.RequestHandleInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by twogoods on 16/11/2.
 */
public abstract class AbstractClassReader implements Reader {
    private static Logger log = LogManager.getLogger(AbstractClassReader.class);


    //TODO 下面5个放在一个类里
    private Map<String, ControllerInfo> apis = new HashMap<>();

    private Map<String, InterceptorInfo> interceptors = new HashMap<>();

    private List<InterceptorInfo> interceptorList = new ArrayList<>();

    private Map<String, RequestHandleInfo> requestHandleMap = new HashMap<>();

    private Map<String, Pair<String,String>> exceptionHandle = new HashMap<>();

    @Override
    public ControllerInfo read(Class clazz) {
        /**url,controller对象,方法,请求方法,拦截器信息
         * url对应一个请求方法<methodname,objname(controller名),requestmethod(请求方法),是否CROS,拦截器信息>
         * 在一个map,存controller对象,或者ioc里拿
         *
         * 拦截器的先后执行顺序
         */

        if (clazz.isAnnotationPresent(Api.class)) {
            Api api = (Api) clazz.getAnnotation(Api.class);
            //TODO 配置的全局的contextPath
            String apiBaseUrl = api.value();
            ControllerInfo controllerInfo = new ControllerInfo();
            String beanName = getBeanName(apiBaseUrl, clazz);
            controllerInfo.setName(beanName);
            controllerInfo.setClassName(clazz.getName());
            controllerInfo.setClazz(clazz);

            apis.put(beanName, controllerInfo);
            prasemethods(clazz.getMethods(), apiBaseUrl, clazz.getName());
            return controllerInfo;
        } else if (clazz.isAnnotationPresent(Interceptor.class)) {
            Interceptor interceptor = (Interceptor) clazz.getAnnotation(Interceptor.class);
            InterceptorInfo interceptorInfo = new InterceptorInfo();
            interceptorInfo.setName(interceptor.name());
            interceptorInfo.setPathPatterns(interceptor.pathPatterns());
            interceptorInfo.setExcludePathPatterns(interceptor.excludePathPatterns());
            interceptorInfo.setOrder(interceptor.order());
            interceptorList.add(interceptorInfo);
        }
        return null;
    }


    private void prasemethods(Method[] methods, String baseUrl, String className) {
        for (Method method : methods) {
            // 必须要有RequestMapping 才能响应请求
            if (method.isAnnotationPresent(RequestMapping.class)) {
                Annotation[] annotations = method.getAnnotations();
                RequestHandleInfo requestHandleInfo = new RequestHandleInfo();
                requestHandleInfo.setMethodName(method.getName());
                requestHandleInfo.setClassName(className);
                String requestUrl = baseUrl;
                for (Annotation annotation : annotations) {
                    if (annotation.annotationType() == RequestMapping.class) {
                        String url = ((RequestMapping) annotation).mapUrl();
                        requestUrl = urlJoin(baseUrl, url);
                        requestHandleInfo.setRequestmethod(((RequestMapping) annotation).method());
                    } else if (annotation.annotationType() == CROS.class) {
                        requestHandleInfo.setCros(true);
                    } else if (annotation.annotationType() == InterceptorExclude.class) {
                        requestHandleInfo.setExcludeInterceptors(((InterceptorExclude) annotation).interceptors());
                    } else if (annotation.annotationType() == InterceptorInclude.class) {
                        requestHandleInfo.setIncludeInterceptors(((InterceptorInclude) annotation).interceptors());
                    }
                }
                requestHandleMap.put(requestUrl, requestHandleInfo);
            } else if (method.isAnnotationPresent(ExceptionHandler.class)) {
                ExceptionHandler exceptionHandler = method.getAnnotation(ExceptionHandler.class);
                //TODO 异常信息里异常类查重
                exceptionHandle.put(className,Pair.of(exceptionHandler.value().getName(), method.getName()));
            }
        }
    }

    public void initRequestMap() {
        System.out.println(apis);
        System.out.println(interceptors);
        System.out.println(interceptorList);
        System.out.println(requestHandleMap);
        System.out.println(exceptionHandle);
    }

    private String urlJoin(String frontUrl, String afterUrl) {
        if (frontUrl.endsWith("/")) {
            if (afterUrl.startsWith("/")) {
                return frontUrl + afterUrl.substring(1);
            } else {
                return frontUrl + afterUrl;
            }
        } else {
            if (afterUrl.startsWith("/")) {
                return frontUrl + afterUrl;
            } else {
                return frontUrl + "/" + afterUrl;
            }
        }
    }

    protected String getBeanName(String annotName, Class clazz) {
        if (StringUtils.isEmpty(annotName)) {
            return StringUtils.firstCharLowercase(clazz.getSimpleName());
        }
        return annotName;
    }

    public Map<String, ControllerInfo> getApis() {
        return apis;
    }

    public Map<String, RequestHandleInfo> getRequestHandleMap() {
        return requestHandleMap;
    }
}