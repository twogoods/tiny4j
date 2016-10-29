package com.tg.tiny4j.core.aop;

/**
 * Created by twogoods on 16/10/24.
 */
public class Add implements Operate{
    @Override
    public int add(int a, int b) {
        return a+b+add(a);
    }

    @Override
    public int add(int a) {
        return a+a;
    }
}
