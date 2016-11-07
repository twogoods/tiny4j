package com.tg.web.controller;

import com.tg.tiny4j.commons.constants.HttpMethod;
import com.tg.tiny4j.web.annotation.*;
import com.tg.web.model.User;

/**
 * Created by twogoods on 16/11/4.
 */
@Api("/base")
public class BaseController {

    @RequestMapping(mapUrl = "/test",method = HttpMethod.GET)
    @ExceptionHandler(Exception.class)
    @CROS
    @InterceptorExclude(interceptors={"testinterceptor"})
    @InterceptorInclude(interceptors={"testinterceptor"})
    private String test(@PathVariable("id")String id,@RequestBody User user){
        return "";
    }
}
