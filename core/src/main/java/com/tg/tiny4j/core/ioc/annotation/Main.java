package com.tg.tiny4j.core.ioc.annotation;

/**
 * Created by twogoods on 16/11/1.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println(Configuration.class.isAnnotationPresent(Component.class));
    }
}
