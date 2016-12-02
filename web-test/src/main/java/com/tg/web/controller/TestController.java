package com.tg.web.controller;

import com.tg.tiny4j.commons.constants.HttpMethod;
import com.tg.tiny4j.web.annotation.*;
import com.tg.web.model.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by twogoods on 16/12/1.
 */
@Api("/base")
public class TestController extends BaseController {

    private String meth = "hah";

    @RequestMapping
    public String in() {
        return "index";
    }

    @RequestMapping(mapUrl = "/test/{id}/haha", method = HttpMethod.GET)
    @CROS
    public User test(@PathVariable("id") String id, @RequestBody User user) {
        return user;
    }


    @RequestMapping(mapUrl = "/test", method = HttpMethod.GET)
    @InterceptorExclude(interceptors = {"aInterceptor", "bInterceptor"})
    public String test1() {
        return "haha";
    }

    @RequestMapping(mapUrl = "/exception", method = HttpMethod.GET)
    public String testExc() {
        int i = 1 / 0;
        return "haha";
    }


    @RequestMapping(mapUrl = "/index")
    @CROS
    public String index(@RequestParam("id") long id, @RequestParam("name") String name) {
        return name + "---" + id;
    }


    @RequestMapping(mapUrl = "/user")
    @InterceptorExclude(interceptors = {"bInterceptor"})
    public Map<String, Object> model(@RequestParam("name") String name, @RequestParam User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("user", user);
        return map;
    }

    @RequestMapping(mapUrl = "/json", method = HttpMethod.POST)
    public User modelJson(@RequestBody User user) {
        return user;
    }

}
