package com.tg.tiny4j.web.reader;

import com.tg.tiny4j.commons.utils.StringUtils;
import com.tg.tiny4j.web.annotation.*;
import com.tg.tiny4j.web.metadata.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by twogoods on 16/11/2.
 */
public abstract class AbstractClassReader implements Reader {
    private static Logger log = LogManager.getLogger(AbstractClassReader.class);

    private RequestMapper requestMapper = new RequestMapper();

    @Override
    public BaseInfo read(Class clazz) throws Exception {
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
            String beanName = getBeanName(api.name(), clazz);
            controllerInfo.setName(beanName);
            controllerInfo.setClassName(clazz.getName());
            controllerInfo.setClazz(clazz);
            requestMapper.addApis(beanName, controllerInfo);
            prasemethods(clazz.getMethods(), apiBaseUrl, clazz.getName(),beanName);
            return controllerInfo;
        } else if (clazz.isAnnotationPresent(Interceptor.class)) {
            Interceptor interceptor = (Interceptor) clazz.getAnnotation(Interceptor.class);
            InterceptorInfo interceptorInfo = new InterceptorInfo();
            String beanName=getBeanName(interceptor.name(), clazz);
            interceptorInfo.setName(beanName);
            interceptorInfo.setClassName(clazz.getName());
            interceptorInfo.setClazz(clazz);
            interceptorInfo.setPathPatterns(interceptor.pathPatterns());
            interceptorInfo.setExcludePathPatterns(interceptor.excludePathPatterns());
            interceptorInfo.setOrder(interceptor.order());
            requestMapper.addInterceptors(beanName,interceptorInfo);
            requestMapper.addInterceptorList(interceptorInfo);
            return interceptorInfo;
        }
        return null;
    }


    private void prasemethods(Method[] methods, String baseUrl, String className,String beanName) throws Exception {
        for (Method method : methods) {
            // 必须要有RequestMapping 才能响应请求
            if (method.isAnnotationPresent(RequestMapping.class)) {
                Annotation[] annotations = method.getAnnotations();
                RequestHandleInfo requestHandleInfo = new RequestHandleInfo();
                requestHandleInfo.setMethodName(method.getName());
                requestHandleInfo.setMethod(method);
                requestHandleInfo.setClassName(className);
                String requestUrl = baseUrl;
                for (Annotation annotation : annotations) {
                    if (annotation.annotationType() == RequestMapping.class) {
                        String url = ((RequestMapping) annotation).mapUrl();
                        //TODO 考虑占位符


                        requestUrl = urlJoin(baseUrl, url);
                        requestHandleInfo.setRequestUrl(requestUrl);
                        requestHandleInfo.setRequestmethod(((RequestMapping) annotation).method());
                    } else if (annotation.annotationType() == CROS.class) {
                        requestHandleInfo.setCros(true);
                    } else if (annotation.annotationType() == InterceptorExclude.class) {
                        requestHandleInfo.setExcludeInterceptors(((InterceptorExclude) annotation).interceptors());
                    } else if (annotation.annotationType() == InterceptorInclude.class) {
                        requestHandleInfo.setIncludeInterceptors(((InterceptorInclude) annotation).interceptors());
                    }
                }
                requestMapper.addRequestHandleMap(requestUrl, requestHandleInfo);
            } else if (method.isAnnotationPresent(ExceptionHandler.class)) {
                ExceptionHandler exceptionHandler = method.getAnnotation(ExceptionHandler.class);
                requestMapper.addExceptionHandle(className, new ExceptionHandleInfo(exceptionHandler.value().getName(), method.getName()));
            }
        }
    }

    public void initRequestMap() throws Exception {
        /**
         * 处理拦截器关系
         * 拦截器列表排序
         * 每个请求对象的处理函数依次检查,要拦截的放入doInterceptors里
         */
        requestMapper.sortInterceptorList();
        Map<String, RequestHandleInfo> requestHandleInfoMap = requestMapper.getRequestHandleMap();
        for (String requestUrl : requestHandleInfoMap.keySet()) {
            RequestHandleInfo info = requestHandleInfoMap.get(requestUrl);
            //先检查  excludeInterceptors  再includeInterceptors
            for (InterceptorInfo interceptor : requestMapper.getInterceptorList()) {
                //优先检查响应方法自己注解上的拦截器信息
                if(info.getExcludeInterceptors().contains(interceptor.getName())){
                    continue;
                }
                if(info.getIncludeInterceptors().contains(interceptor.getName())){
                    info.addDoInterceptors(interceptor.getName());
                    continue;
                }

                //匹配url
                String[] excludes = interceptor.getExcludePathPatterns();
                for (String exclude : excludes) {
                    //TODO url的匹配
                    if (requestUrl.startsWith(exclude)){
                        continue;
                    }
                }

                String[] includes=interceptor.getPathPatterns();
                for (String include: includes) {
                    if (requestUrl.startsWith(include)){
                        info.addDoInterceptors(interceptor.getName());
                    }
                }
            }
        }
        System.out.println(requestMapper);
    }


    public void instance4SingleMode() throws Exception {
        //-----初始化controller类
        Map<String, ControllerInfo> controllerInfoMap = requestMapper.getApis();
        for (String key : controllerInfoMap.keySet()) {
            ControllerInfo controller = controllerInfoMap.get(key);
            controller.setObject(controller.getClazz().newInstance());
        }
        //-----初始化interceptor类
        for (InterceptorInfo interceptor : requestMapper.getInterceptorList()) {
            interceptor.setObj(interceptor.getClazz().newInstance());
        }
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

    @Override
    public RequestMapper getRequestMapper() {
        return requestMapper;
    }
}