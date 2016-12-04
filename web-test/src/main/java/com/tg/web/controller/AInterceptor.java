package com.tg.web.controller;

import com.tg.tiny4j.web.annotation.Interceptor;
import com.tg.tiny4j.web.interceptor.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by twogoods on 16/11/9.
 */
@Interceptor(pathPatterns = {"/base/user"},order = 2)
public class AInterceptor implements HandlerInterceptor{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("do a interceptor");
        return true;
    }
}
