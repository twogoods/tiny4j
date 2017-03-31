package com.tg.web.controller;

import com.tg.tiny4j.core.ioc.annotation.Inject;
import com.tg.tiny4j.core.ioc.annotation.Value;
import com.tg.tiny4j.web.annotation.Interceptor;
import com.tg.tiny4j.web.interceptor.HandlerInterceptor;
import com.tg.web.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by twogoods on 16/11/9.
 */
@Interceptor(pathPatterns = {"/base/user"},order = 2)
public class AInterceptor implements HandlerInterceptor{

    @Value("${user.name:test}")
    private String name;

    @Inject
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("**********   A  interceptor      ********");
        System.out.println(name);
//        userService.query();
        System.out.println("**********   A  interceptor end  ********");
        return true;
    }
}
