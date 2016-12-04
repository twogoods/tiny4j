package com.tg.tiny4j.web.reader;

import com.tg.tiny4j.commons.utils.Validate;
import com.tg.tiny4j.web.annotation.*;
import com.tg.tiny4j.web.exception.UrlDuplicatedException;
import com.tg.tiny4j.web.metadata.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
         * 拦截器的先后执行顺序
         */
        if (clazz.isAnnotationPresent(Api.class)) {
            Api api = (Api) clazz.getAnnotation(Api.class);
            //TODO 可以增加一个可配置的全局的contextPath
            String apiBaseUrl = api.value();
            ControllerInfo controllerInfo = new ControllerInfo();
            String beanName = getBeanName(api.name(), clazz);
            controllerInfo.setName(beanName);
            controllerInfo.setClassName(clazz.getName());
            controllerInfo.setClazz(clazz);
            requestMapper.addApis(beanName, controllerInfo);
            prasemethods(clazz.getMethods(), apiBaseUrl, clazz.getName(), beanName);
            return controllerInfo;
        } else if (clazz.isAnnotationPresent(Interceptor.class)) {
            Interceptor interceptor = (Interceptor) clazz.getAnnotation(Interceptor.class);
            InterceptorInfo interceptorInfo = new InterceptorInfo();
            String beanName = getBeanName(interceptor.name(), clazz);
            interceptorInfo.setName(beanName);
            interceptorInfo.setClassName(clazz.getName());
            interceptorInfo.setClazz(clazz);
            interceptorInfo.setPathPatterns(interceptor.pathPatterns());
            interceptorInfo.setExcludePathPatterns(interceptor.excludePathPatterns());
            interceptorInfo.setOrder(interceptor.order());
            requestMapper.addInterceptors(beanName, interceptorInfo);
            requestMapper.addInterceptorList(interceptorInfo);
            return interceptorInfo;
        }
        return null;
    }


    private void prasemethods(Method[] methods, String baseUrl, String className, String beanName) throws Exception {
        for (Method method : methods) {
            // 必须要有RequestMapping 才能响应请求
            if (method.isAnnotationPresent(RequestMapping.class)) {
                Annotation[] annotations = method.getAnnotations();
                RequestHandleInfo requestHandleInfo = new RequestHandleInfo();
                requestHandleInfo.setMethodName(method.getName());
                requestHandleInfo.setMethod(method);
                requestHandleInfo.setBeanName(beanName);
                requestHandleInfo.setClassName(className);
                String requestUrl = baseUrl;
                for (Annotation annotation : annotations) {
                    if (annotation.annotationType() == RequestMapping.class) {
                        String url = ((RequestMapping) annotation).mapUrl();
                        requestUrl = urlJoin(baseUrl, url);
                        // TODO url查重
                        UrlPraseInfo urlPraseInfo = praseConfigPathPattern(requestUrl);
                        if (!Validate.isEmpty(urlPraseInfo)) {
                            requestUrl = urlPraseInfo.getMatch();
                            checkUrlDeplicated(requestUrl);
                            requestHandleInfo.setUrlPraseInfo(urlPraseInfo);
                            requestMapper.addUrlPraseResult(urlPraseInfo.getMatch());
                        } else {
                            checkUrlDeplicated(requestUrl);
                        }
                        requestHandleInfo.setRequestUrl(requestUrl);
                        requestHandleInfo.setRequestmethod(((RequestMapping) annotation).method());
                    } else if (annotation.annotationType() == CROS.class) {
                        CROS cros = ((CROS) annotation);
                        CrosInfo crosInfo = new CrosInfo(cros.origins(), cros.methods(), cros.maxAge(), cros.headers(), cros.cookie());
                        requestHandleInfo.setCros(crosInfo);
                    } else if (annotation.annotationType() == InterceptorExclude.class) {
                        requestHandleInfo.setExcludeInterceptors(((InterceptorExclude) annotation).interceptors());
                    } else if (annotation.annotationType() == InterceptorInclude.class) {
                        requestHandleInfo.setIncludeInterceptors(((InterceptorInclude) annotation).interceptors());
                    } else if (annotation.annotationType() == InterceptorSelect.class) {
                        requestHandleInfo.setIncludeInterceptors(((InterceptorSelect) annotation).include());
                        requestHandleInfo.setExcludeInterceptors(((InterceptorSelect) annotation).exclude());
                    }
                }
                requestMapper.addRequestHandleMap(requestUrl, requestHandleInfo);
            } else if (method.isAnnotationPresent(ExceptionHandler.class)) {
                ExceptionHandler exceptionHandler = method.getAnnotation(ExceptionHandler.class);
                requestMapper.addExceptionHandle(className, new ExceptionHandleInfo(beanName, exceptionHandler.value().getName(), method));
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
                if (info.getExcludeInterceptors().contains(interceptor.getName())) {
                    continue;
                }
                if (info.getIncludeInterceptors().contains(interceptor.getName())) {
                    info.addDoInterceptors(interceptor.getName());
                    continue;
                }
                boolean matched = false;
                //匹配url
                String[] excludes = interceptor.getExcludePathPatterns();
                for (String exclude : excludes) {
                    //TODO restful url的匹配
                    if (requestUrl.startsWith(exclude)) {
                        matched = true;
                        break;
                    }
                }
                if (matched) continue;

                String[] includes = interceptor.getPathPatterns();
                for (String include : includes) {
                    if (requestUrl.startsWith(include)) {
                        info.addDoInterceptors(interceptor.getName());
                        break;
                    }
                }
            }
        }
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
        String requestUrl;
        if (frontUrl.endsWith("/")) {
            if (afterUrl.startsWith("/")) {
                requestUrl = frontUrl + afterUrl.substring(1);
            } else {
                requestUrl = frontUrl + afterUrl;
            }
        } else {
            if (afterUrl.startsWith("/")) {
                requestUrl = frontUrl + afterUrl;
            } else {
                requestUrl = frontUrl + "/" + afterUrl;
            }
        }

        if (!requestUrl.startsWith("/")) {
            requestUrl = "/" + requestUrl;
        }
        if (requestUrl.endsWith("/")) {
            requestUrl = requestUrl.substring(0, requestUrl.length() - 1);
        }
        return requestUrl;
    }

    protected String getBeanName(String annotName, Class clazz) {
        if (StringUtils.isEmpty(annotName)) {
            return WordUtils.uncapitalize(clazz.getSimpleName());
        }
        return annotName;
    }


    private String patternStr = "\\{\\w*\\}*";
    private Pattern pattern = Pattern.compile(patternStr);

    private UrlPraseInfo praseConfigPathPattern(String url) {
        Matcher m = pattern.matcher(url);
        List<String> params = new ArrayList();
        while (m.find()) {
            String r = m.group();
            params.add(r.substring(1, r.length() - 1));
        }
        if (params.size() == 0) {
            return null;
        }
        String matchStr = m.replaceAll("\\\\w*");
        return new UrlPraseInfo(matchStr, params);
    }

    private void checkUrlDeplicated(String requestUrl) throws UrlDuplicatedException {
        for (String matchStr : requestMapper.getUrlPraseResult()) {
            if (matchStr.equals(requestUrl)) {
                throw new UrlDuplicatedException(String.format("url duplicated ! two url: '%s' , '%s'", matchStr.replace("\\w*", "**"), requestUrl.replace("\\w*", "**")));
            }
            Pattern pattern = Pattern.compile(matchStr);
            Matcher m = pattern.matcher(requestUrl);
            if (m.matches()) {
                throw new UrlDuplicatedException(String.format("url duplicated ! two url: '%s' , '%s'", matchStr.replace("\\w*", "**"), requestUrl.replace("\\w*", "**")));
            }
        }
    }

    @Override
    public RequestMapper getRequestMapper() {
        return requestMapper;
    }
}