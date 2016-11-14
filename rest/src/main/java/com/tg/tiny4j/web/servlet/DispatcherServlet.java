package com.tg.tiny4j.web.servlet;

import com.tg.tiny4j.commons.utils.StringUtils;
import com.tg.tiny4j.web.annotation.PathVariable;
import com.tg.tiny4j.web.annotation.RequestBody;
import com.tg.tiny4j.web.metadata.InterceptorInfo;
import com.tg.tiny4j.web.metadata.RequestHandleInfo;
import com.tg.tiny4j.web.metadata.RequestMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by twogoods on 16/11/7.
 */
public class DispatcherServlet extends HttpServlet {
    private static Logger log = LogManager.getLogger(DispatcherServlet.class);

    private RequestMapper requestMapper;

    private Map<String, RequestHandleInfo> requestHandleMap;

    @Override
    public void init() throws ServletException {
//        ServletContext servletContext = getServletContext();
//        String mode = servletContext.getAttribute("run_mode").toString();
        requestMapper = (RequestMapper) getServletContext().getAttribute("webrequestmapper");
        requestHandleMap = requestMapper.getRequestHandleMap();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getRequestURI();
        RequestHandleInfo requestHandleInfo = requestHandleMap.get(pathInfo);
        if (requestHandleInfo != null) {
            try {
                handle(req, resp, requestHandleInfo);
            } catch (Exception e) {
                log.error(e);
            }
            return;
        }
        resp.sendError(404);
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

    private void doRequestHandle(HttpServletRequest req, HttpServletResponse resp, RequestHandleInfo requestHandleInfo) {
        Method method = requestHandleInfo.getMethod();
        method.setAccessible(true);
        Object result=null;
        try {
            result=method.invoke(requestHandleInfo.getInstance(), praseParam(method, req, resp));
        } catch (Exception e) {
            //TODO 异常处理
        }
        //TODO 处理json返回
        Class returnType = method.getReturnType();

    }

    private Object[] praseParam(Method method, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        Map<String, String[]> requestParams = req.getParameterMap();
        List<Object> paramValues=new ArrayList<>();
        Parameter[] parameters = method.getParameters();
        Class[] paramTypes = method.getParameterTypes();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            //TODO 参数是servlet相关的api,如HttpServletRequest

            if (parameterAnnotations[i].length == 0) {
                //TODO 无注解,类型转化
                paramValues.add(stringCast(paramTypes[i], parameters[i].getName(), requestParams));
            } else {
                for (Annotation annotation : parameterAnnotations[i]) {
                    if (annotation.annotationType() == PathVariable.class) {
                        //占位符的匹配
                    } else if (annotation.annotationType() == RequestBody.class) {
                        //json解析
                    }
                }
            }
        }
        return paramValues.toArray();
    }

    private Object stringCast(Class type, String name, Map<String, String[]> requestParams) throws Exception {
        String[] value = requestParams.get(name);
        if (value == null || value.length == 0) {
            return stringCastObj(type, requestParams);
        } else if (value.length > 1) {
            log.warn(String.format("you have %d value in param '%s'", value.length, name));
        }
        return baseCast(type, value[0]);
    }

    private Object stringCastObj(Class type, Map<String, String[]> requestParams) throws Exception {
        //TODO 判断这个类是否是自定义的实体类,大多数情况下不是就可以直接不解析了? jdk的类和自定义的类的类加载器不同?
        Object obj = type.newInstance();
        Field[] fields = type.getDeclaredFields();
        for (Field f : fields) {
            String[] strValue = requestParams.get(f.getName());
            if (strValue == null || strValue.length == 0) {
                //TODO 这里只做基本类型,引用类型考虑递归 stringCastObj(f.getType(), requestParams);
            } else {
                Object v = baseCast(f.getType(), strValue[0]);
                f.setAccessible(true);
                Method m = null;
                try {
                    m = type.getMethod("set" + StringUtils.firstCharUpperCase(f.getName()), f.getType());
                    m.invoke(obj, v);
                } catch (NoSuchMethodException e) {
                    log.warn(String.format("class '%s' have no set property for '%s'", type, f.getName()), e);
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
        } else if (type == Character[].class || type == char[].class) {
            return value.toCharArray();
        }
        return null;
    }

}