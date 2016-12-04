package com.tg.tiny4j.web.annotation;

import com.tg.tiny4j.commons.constants.HttpMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by twogoods on 16/11/1.
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CROS {
    String origins() default "*";
    String[] methods() default {HttpMethod.GET,HttpMethod.POST,HttpMethod.PUT,HttpMethod.PATCH,HttpMethod.DELETE};
    String maxAge() default "3600";
    String headers() default "";
    boolean cookie() default false;
}
