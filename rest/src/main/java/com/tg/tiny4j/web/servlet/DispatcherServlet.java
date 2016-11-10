package com.tg.tiny4j.web.servlet;

import com.tg.tiny4j.web.annotation.PathVariable;
import com.tg.tiny4j.web.annotation.RequestBody;
import com.tg.tiny4j.web.handle.RequestHandleMapping;
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
import java.lang.reflect.Method;
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


        Class returnType = method.getReturnType();
    }

    private Object[] praseParam(Method method, HttpServletRequest req, HttpServletResponse resp) {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for(int i=0;i<parameterAnnotations.length;i++){
            if(parameterAnnotations[i].length==0){
                //TODO 类型转化
            }else{
                for(Annotation annotation:parameterAnnotations[i]){
                    if(annotation.annotationType()== PathVariable.class){

                    }else if(annotation.annotationType()== RequestBody.class){
                        
                    }
                }
            }
        }


        return null;
    }
}