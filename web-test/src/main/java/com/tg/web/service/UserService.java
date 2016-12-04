package com.tg.web.service;

import com.tg.tiny4j.core.ioc.annotation.Component;
import com.tg.tiny4j.core.ioc.annotation.Inject;
import com.tg.web.dao.UserDao;

/**
 * Created by twogoods on 16/11/1.
 */
@Component
public class UserService {

    @Inject
    private UserDao userDao;


    public void query(){
        userDao.query();
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
