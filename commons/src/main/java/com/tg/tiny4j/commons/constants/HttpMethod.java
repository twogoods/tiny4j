package com.tg.tiny4j.commons.constants;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by twogoods on 16/11/7.
 */
public class HttpMethod {
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";
    //jdk7- not support
    public static final String PATCH = "PATCH";
    //cors
    public static final String OPTIONS = "OPTIONS";
    //not filter
    public static final String HEAD = "HEAD";


    public static boolean support(String httpMethod) {
        if(StringUtils.isEmpty(httpMethod)){
            return true;
        }
        return GET.equals(httpMethod) || POST.equals(httpMethod) || PUT.equals(httpMethod)
                || DELETE.equals(httpMethod) || PATCH.equals(httpMethod);
    }
}
