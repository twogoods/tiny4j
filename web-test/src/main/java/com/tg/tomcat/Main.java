package com.tg.tomcat;


import com.tg.web.controller.TestController;

import java.lang.reflect.Method;

/**
 * Created by twogoods on 16/11/12.
 */
public class Main {
    public static void main(String[] args) {
        for(Method m:TestController.class.getMethods()){
            System.out.println(m.getName());
        }
    }
}
