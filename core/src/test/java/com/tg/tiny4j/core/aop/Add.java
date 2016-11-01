package com.tg.tiny4j.core.aop;

/**
 * Created by twogoods on 16/10/24.
 */
public class Add{

    private String prop;

    public int add(int a, int b) {
        System.out.println("prop:"+prop);
        return a+b+add(a);
    }

    public int add(int a) {
        return a+a;
    }

    public String getProp() {
        return prop;
    }

}
