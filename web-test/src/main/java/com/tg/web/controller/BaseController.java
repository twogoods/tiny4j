package com.tg.web.controller;

import com.tg.tiny4j.web.annotation.*;

/**
 * Created by twogoods on 16/11/4.
 */
public class BaseController {
    @ExceptionHandler(Exception.class)
    public String handleException(){
        return "error";
    }


}
