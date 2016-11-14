package com.tg.web.controller;

import com.tg.tiny4j.commons.constants.HttpMethod;
import com.tg.tiny4j.web.annotation.*;
import com.tg.web.model.User;

/**
 * Created by twogoods on 16/11/4.
 */
@Api("/base")
public class BaseController {

    @RequestMapping(mapUrl = "/test/{id}/haha",method = HttpMethod.GET)
    @ExceptionHandler(Exception.class)
    @CROS
    @InterceptorExclude(interceptors={"aInterceptor"})
    public String test(@PathVariable("id")String id,@RequestBody User user){
        return "";
    }


    @RequestMapping(mapUrl = "/test",method = HttpMethod.GET)
    public String test1(){
        return "";
    }

    @RequestMapping(mapUrl = "/index",method = HttpMethod.GET)
    @InterceptorExclude(interceptors={"bInterceptor"})
    @ExceptionHandler(Exception.class)
    @CROS
    public String index(){
        return "";
    }


}
