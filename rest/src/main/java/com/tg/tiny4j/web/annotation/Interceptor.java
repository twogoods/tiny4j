package com.tg.tiny4j.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by twogoods on 16/11/7.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Interceptor {
    String name() default "";
    String[] pathPatterns() default {};
    String[] excludePathPatterns() default {};
    int order() default 0;
}
