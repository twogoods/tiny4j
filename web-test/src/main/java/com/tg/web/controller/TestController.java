package com.tg.web.controller;

import com.tg.tiny4j.commons.constants.HttpMethod;
import com.tg.tiny4j.core.ioc.annotation.Inject;
import com.tg.tiny4j.core.ioc.annotation.Value;
import com.tg.tiny4j.web.annotation.*;
import com.tg.web.model.User;
import com.tg.web.service.UserService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by twogoods on 16/12/1.
 */
@Api("/base")
public class TestController extends BaseController {

    @Value("${user.name:test}")
    private String name;

    @Inject
    private UserService userService;

    @RequestMapping
    public String index() {
        userService.query();
        return name;
    }

    @RequestMapping(mapUrl = "/test/{id}", method = HttpMethod.GET)
    @CROS(origins = "www.baidu.com", methods = {HttpMethod.GET}, maxAge = "3600")
    public String patgTest(@PathVariable("id") String id) {
        return id;
    }

    @RequestMapping(mapUrl = "/test", method = HttpMethod.GET)
    @InterceptorSelect(include = {"aInterceptor"}, exclude = {"bInterceptor"})
    public String interceptorTest() {
        return "haha";
    }


    @RequestMapping(mapUrl = "/exception", method = HttpMethod.GET)
    public String exceptionTest() {
        int i = 1 / 0;
        return "haha";
    }


    @RequestMapping(mapUrl = "/index")
    @CROS
    public String paramTest(@RequestParam("id") long id, @RequestParam("name") String name) {
        return name + "---" + id;
    }


    @RequestMapping(mapUrl = "/user")
    @InterceptorExclude(interceptors = {"bInterceptor"})
    public Map<String, Object> modelTest(@RequestParam("name") String name, @RequestParam User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("user", user);
        return map;
    }

    @RequestMapping(mapUrl = "/json", method = HttpMethod.POST)
    public User modelJson(@RequestBody User user) {
        return user;
    }

    @RequestMapping(mapUrl = "/user/{id}", method = HttpMethod.PUT)
    @CROS
    public User insert(@PathVariable("id") long id, @RequestBody User user) {
        return user;
    }

}
