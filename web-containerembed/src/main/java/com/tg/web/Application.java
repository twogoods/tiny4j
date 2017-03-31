package com.tg.web;

import com.tg.tiny4j.web.annotation.Api;
import com.tg.tiny4j.web.annotation.RequestMapping;
import com.tg.tiny4j.web.contextlistener.SingleRestLoaderListener;
import com.tg.tiny4j.web.jettyembed.TinyApplication;
import com.tg.web.contextlistener.BootAppLoaderListener;

/**
 * Description:
 *
 * @author twogoods
 * @version 0.1
 * @since 2017-03-31
 */
@Api
public class Application {
    @RequestMapping
    public String home() {
        return "Hello World!";
    }
    public static void main(String[] args) {
        //TinyApplication.run(SingleRestLoaderListener.class, args);
        TinyApplication.run(BootAppLoaderListener.class, args);
    }
}
