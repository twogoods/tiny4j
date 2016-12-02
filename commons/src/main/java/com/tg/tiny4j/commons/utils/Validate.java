package com.tg.tiny4j.commons.utils;

import java.util.List;
import java.util.Set;

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

    public static boolean isEmpty(List list){
        if(list==null||list.size()==0){
            return true;
        }
        return false;
    }
    public static boolean isEmpty(Set set){
        if(set==null||set.size()==0){
            return true;
        }
        return false;
    }
    public static boolean isEmpty(Object[] strArr){
        if(strArr==null||strArr.length==0){
            return true;
        }
        return false;
    }
}
