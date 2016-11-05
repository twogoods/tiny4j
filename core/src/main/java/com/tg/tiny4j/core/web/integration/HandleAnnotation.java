package com.tg.tiny4j.core.web.integration;

import com.tg.tiny4j.core.ioc.beans.BeanDefinition;

/**
 * Created by twogoods on 16/11/5.
 */
public interface HandleAnnotation {
    BeanDefinition handle(Class clazz) throws ClassNotFoundException;
}
