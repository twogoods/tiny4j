package com.tg.tiny4j.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by twogoods on 16/11/7.
 */
public interface HandlerInterceptor {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response);
}
