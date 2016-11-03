package com.tg.web.service;

import com.tg.web.dao.UserDao;

/**
 * Created by twogoods on 16/11/1.
 */
public class UserService {
    private String s1;
    private String s2;

    private UserDao userDao;

    public String getS1() {
        return s1;
    }

    public void server(){
        userDao.query();
    }

    public void setS1(String s1) {
        this.s1 = s1;
    }

    public String getS2() {
        return s2;
    }

    public void setS2(String s2) {
        this.s2 = s2;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
