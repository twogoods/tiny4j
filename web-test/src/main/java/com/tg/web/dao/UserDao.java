package com.tg.web.dao;

import com.tg.tiny4j.core.ioc.annotation.Component;

/**
 * Created by twogoods on 16/11/1.
 */
@Component
public class UserDao {

    public void query(){
        System.out.println("execute sql....");
    }
}
