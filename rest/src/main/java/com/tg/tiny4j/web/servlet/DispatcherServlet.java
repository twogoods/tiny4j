package com.tg.tiny4j.web.servlet;

import com.tg.tiny4j.commons.constants.WebApplicationEnvironment;
import com.tg.tiny4j.commons.utils.JsonUtil;
import com.tg.tiny4j.commons.utils.Validate;
import com.tg.tiny4j.web.annotation.PathVariable;
import com.tg.tiny4j.web.annotation.RequestBody;
import com.tg.tiny4j.web.annotation.RequestParam;
import com.tg.tiny4j.web.metadata.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by twogoods on 16/11/7.
 */
public class DispatcherServlet extends HttpServlet {
    private static Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private RequestMapper requestMapper;

    private Map<String, RequestHandleInfo> requestHandleMap;

    private List<String> urlPraseResult;

    private Map<String, Map<String, ExceptionHandleInfo>> exceptionHandles;

    private Map<String, ControllerInfo> apis = new HashMap<>();


    @Override
    public void init() throws ServletException {
        log.info("DispatcherServlet init...");
        requestMapper = (RequestMapper) getServletContext().getAttribute(WebApplicationEnvironment.WEBREQUESTMAPPER);
        requestHandleMap = requestMapper.getRequestHandleMap();
        exceptionHandles = requestMapper.getExceptionHandles();
        apis = requestMapper.getApis();
        urlPraseResult = requestMapper.getUrlPraseResult();
    }

    private String getUrl(HttpServletRequest req) {
        log.info(req.getPathInfo());
        log.info(req.getRequestURI());
        log.info(req.getServletPath());
        log.info(req.getContextPath());
        String pathInfo = req.getPathInfo();
        if (pathInfo.endsWith("/")) {
            return pathInfo.substring(0, pathInfo.length() - 1);
        }
        return pathInfo;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = getUrl(req);
        log.info("url path: " + pathInfo);

        RequestHandleInfo requestHandleInfo;
        String matchResult = matchRequestHandleUrl(pathInfo);
        if (StringUtils.isEmpty(matchResult)) {
            requestHandleInfo = requestHandleMap.get(pathInfo);
        } else {
            requestHandleInfo = requestHandleMap.get(matchResult);
            req.setAttribute("pathParams", getPathParam(pathInfo, matchResult, requestHandleInfo.getUrlPraseInfo().getParams()));
        }
        if (requestHandleInfo != null) {
            if (!"".equals(requestHandleInfo.getRequestmethod()) && !req.getMethod().equals(requestHandleInfo.getRequestmethod())) {
                resp.setStatus(405);
                return;
            }
            try {
                cros(resp, requestHandleInfo.getCros());
                handle(req, resp, requestHandleInfo);
            } catch (Exception e) {
                log.error("error: {}", e);
                resp.setStatus(500);
            }
            return;
        }
        // 不考虑静态资源的问题
        resp.setStatus(404);
    }

    private String matchRequestHandleUrl(String requestUrl) {
        for (String matchStr : urlPraseResult) {
            Pattern pattern = Pattern.compile(matchStr);
            Matcher m = pattern.matcher(requestUrl);
            if (m.matches()) {
                return matchStr;
            }
        }
        return null;
    }

    private Map<String, String> getPathParam(String requestUrl, String matchUrl, List<String> propertyName) {
        List<String> params = new ArrayList();
        String[] urlSplit = requestUrl.split("/");
        String[] matchSplit = matchUrl.split("/");
        Map<String, String> pathParams = new HashMap<>(propertyName.size());
        int index = 0;
        for (int i = 0; i < matchSplit.length; i++) {
            if ("\\w*".equals(matchSplit[i])) {
                pathParams.put(propertyName.get(index), urlSplit[i]);
                index++;
            }
        }
        return pathParams;
    }

    private void cros(HttpServletResponse response, CrosInfo cros) {
        if (!Validate.isEmpty(cros)) {
            response.setHeader("Access-Control-Allow-Origin", cros.getOrigins());
            response.setHeader("Access-Control-Allow-Methods", StringUtils.join(cros.getMethods(), ","));
            response.setHeader("Access-Control-Max-Age", cros.getMaxAge());
            if (!StringUtils.isEmpty(cros.getHeaders())) {
                response.setHeader("Access-Control-Allow-Headers", cros.getHeaders());
            }
            if (cros.isCookie()) {
                response.setHeader("Access-Control-Allow-Credentials", "true");
            } else {
                response.setHeader("Access-Control-Allow-Credentials", "false");
            }
        }
    }


    private void handle(HttpServletRequest req, HttpServletResponse resp, RequestHandleInfo requestHandleInfo) throws Exception {
        if (doInterceptor(req, resp, requestHandleInfo)) {
            doRequestHandle(req, resp, requestHandleInfo);
        }
    }

    private boolean doInterceptor(HttpServletRequest req, HttpServletResponse resp, RequestHandleInfo requestHandleInfo) throws Exception {
        List<InterceptorInfo> interceptorInfoList = requestMapper.getInterceptorList();
        int index = 0;
        for (InterceptorInfo interceptor : interceptorInfoList) {
            index++;
            if (requestHandleInfo.getDoInterceptors().containsKey(interceptor.getName())) {
                Method method = interceptor.getClazz().getMethod("preHandle", HttpServletRequest.class, HttpServletResponse.class);
                Boolean flag = (Boolean) method.invoke(interceptor.getObj(), req, resp);
                if (flag) {
                    continue;
                } else {
                    break;
                }
            }
        }
        return interceptorInfoList.size() == index;
    }

    private void doRequestHandle(HttpServletRequest req, HttpServletResponse resp, RequestHandleInfo requestHandleInfo) throws Exception {
        Method method = requestHandleInfo.getMethod();
        method.setAccessible(true);
        Object result = null;
        try {
            if (method.getParameterCount() == 0) {
                System.out.println(apis.get(requestHandleInfo.getBeanName()).getObject());
                result = method.invoke(apis.get(requestHandleInfo.getBeanName()).getObject());
            } else {
                result = method.invoke(apis.get(requestHandleInfo.getBeanName()).getObject(), praseParam(method, req, resp));
            }
        } catch (Exception e) {
            log.error("error: {}", e.getCause());
            ExceptionHandleInfo exceptionHandleInfo = getHandleInfo(e.getCause().getClass(), requestHandleInfo.getClassName());
            if (exceptionHandleInfo != null) {
                doHandleException(exceptionHandleInfo.getMethod(), apis.get(requestHandleInfo.getBeanName()).getObject(), req, resp);
                return;
            } else {
                throw e;
            }
        }
        if (!Validate.isEmpty(result)) {
            returnJsonStr(result, resp);
        }
    }

    private void doHandleException(Method method, Object instance, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        method.setAccessible(true);
        Object result = null;
        try {
            if (method.getParameterCount() == 0) {
                result = method.invoke(instance);
            } else {
                result = method.invoke(instance, praseParam(method, req, resp));
            }
        } catch (Exception e) {
            throw e;
        }
        if (!Validate.isEmpty(result)) {
            returnJsonStr(result, resp);
        }
    }


    private void returnJsonStr(Object obj, HttpServletResponse resp) throws IOException {
        JsonUtil.writeValue(resp, obj);
    }


    private ExceptionHandleInfo getHandleInfo(Class exceptionClass, String className) {
        Map<String, ExceptionHandleInfo> handleMap = exceptionHandles.get(className);
        if (handleMap == null) {
            return null;
        } else {
            return getHandleInfo(handleMap, exceptionClass);
        }
    }


    private ExceptionHandleInfo getHandleInfo(Map<String, ExceptionHandleInfo> handleMap, Class exceptionClass) {
        if ("java.lang.Throwable".equals(exceptionClass.getName()) || "java.lang.Object".equals(exceptionClass.getName())) {
            return null;
        }
        ExceptionHandleInfo exceptionHandleInfo = handleMap.get(exceptionClass.getName());
        if (exceptionHandleInfo == null) {
            return getHandleInfo(handleMap, exceptionClass.getSuperclass());
        } else {
            return exceptionHandleInfo;
        }
    }

    private Object[] praseParam(Method method, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Map<String, String[]> requestParams = req.getParameterMap();
        List<Object> paramValues = new ArrayList<>();
        //拿不到参数名
        Parameter[] parameters = method.getParameters();
        Class[] paramTypes = method.getParameterTypes();

        for (int i = 0; i < paramTypes.length; i++) {
            Annotation[] annotatins = parameters[i].getAnnotations();
            if (Validate.isEmpty(annotatins)) {
                //TODO 无注解时使用asm获取方法参数名解析
                paramValues.add(castServletApi(paramTypes[i], req, resp));
            } else {
                for (Annotation annotation : annotatins) {
                    if (annotation.annotationType() == PathVariable.class) {
                        PathVariable pathVariable = (PathVariable) annotation;
                        Object path = req.getAttribute("pathParams");
                        if (Validate.isEmpty(path)) {
                            throw new RuntimeException(String.format("not get pathParma: %s", pathVariable.value()));
                        } else {
                            String paramValue = ((Map<String, String>) path).get(pathVariable.value());
                            paramValues.add(baseCast(paramTypes[i], paramValue));
                        }
                    } else if (annotation.annotationType() == RequestParam.class) {
                        // 解析参数,通过注解获取参数名
                        paramValues.add(cast(paramTypes[i], ((RequestParam) annotation).value(), requestParams, req, resp));
                    } else if (annotation.annotationType() == RequestBody.class) {
                        //json解析
                        try (InputStream in = req.getInputStream()) {
                            paramValues.add(JsonUtil.objectFromJson(IOUtils.toString(in, "UTF-8"), paramTypes[i]));
                        }
                    }
                }
            }
        }
        return paramValues.toArray();
    }


    private Object castServletApi(Class type, HttpServletRequest req, HttpServletResponse resp) {
        if ("javax.servlet.http.HttpServletRequest".equals(type.getName())) {
            return req;
        } else if ("javax.servlet.http.HttpServletResponse".equals(type.getName()))
            return resp;
        return null;
    }

    private Object cast(Class type, String name, Map<String, String[]> requestParams, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Object o = castServletApi(type, req, resp);
        if (o != null) return o;
        String[] value = requestParams.get(name);
        if (o == null && !Validate.isEmpty(value)) {
            o = baseCast(type, value[0]);
            if (value.length > 1) {
                log.warn(String.format("you have %d value in param '%s'", value.length, name));
            }
        }
        if (o == null) {
            o = castObj(type, requestParams);
        }
        if (o == null) {
            o = baseTypeDefault(type);
        }
        return o;
    }

    private Object castObj(Class type, Map<String, String[]> requestParams) throws Exception {
        //TODO 暂时,判断这个类是否是自定义的实体类,大多数情况下不是就可以直接不解析了
        if (type.getClassLoader() == null) {
            return null;
        }
        Object obj = type.newInstance();
        Field[] fields = type.getDeclaredFields();
        for (Field f : fields) {
            String[] strValue = requestParams.get(f.getName());
            Object v = null;
            if (Validate.isEmpty(strValue)) {
                castObj(f.getType(), requestParams);
            } else {
                v = baseCast(f.getType(), strValue[0]);
            }
            if (v != null) {
                f.setAccessible(true);
                Method m = null;
                try {
                    m = type.getMethod("set" + WordUtils.capitalize(f.getName()), f.getType());
                    m.invoke(obj, v);
                } catch (NoSuchMethodException e) {
                    log.warn(String.format("class '%s' has no set property for '%s'", type, f.getName()), e);
                } catch (Exception e) {
                    try {
                        f.set(obj, v);
                    } catch (IllegalAccessException e1) {
                        //ignore
                    }
                }
            }
        }
        return obj;
    }

    private Object baseCast(Class type, String value) {
        if (type == String.class) {
            return value;
        } else if (type == Integer.class || type == int.class) {
            return Integer.parseInt(value);
        } else if (type == Long.class || type == long.class) {
            return Long.parseLong(value);
        } else if (type == Double.class || type == double.class) {
            return Double.parseDouble(value);
        } else if (type == Float.class || type == float.class) {
            return Float.parseFloat(value);
        } else if (type == Short.class || type == short.class) {
            return Short.parseShort(value);
        } else if (type == Boolean.class || type == boolean.class) {
            return Boolean.parseBoolean(value);
        } else if (type == Byte.class || type == byte.class) {
            return Byte.parseByte(value);
        } else if (type == Character.class || type == char.class) {
            return value.toCharArray()[0];
        }
        return null;
    }

    private int intDefault;
    private long longDefault;
    private double doubleDefault;
    private float floatDefault;
    private short shortDefault;
    private boolean boolDefault;
    private byte byteDefault;
    private char charDefault;

    private Object baseTypeDefault(Class type) {
        if (type == Integer.class || type == int.class) {
            return intDefault;
        } else if (type == Long.class || type == long.class) {
            return longDefault;
        } else if (type == Double.class || type == double.class) {
            return doubleDefault;
        } else if (type == Float.class || type == float.class) {
            return floatDefault;
        } else if (type == Short.class || type == short.class) {
            return shortDefault;
        } else if (type == Boolean.class || type == boolean.class) {
            return boolDefault;
        } else if (type == Byte.class || type == byte.class) {
            return byteDefault;
        } else if (type == Character.class || type == char.class) {
            return charDefault;
        }
        return null;
    }
}