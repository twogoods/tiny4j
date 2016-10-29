package com.tg.tiny4j.core.ioc.utils;

/**
 * Created by twogoods on 16/10/26.
 */
public class Validate {
    public static boolean isEmpty(Object obj){
        if(obj==null||"".equals(obj)){
            return true;
        }
        return false;
    }

}
