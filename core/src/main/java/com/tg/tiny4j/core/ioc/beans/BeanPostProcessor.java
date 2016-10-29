package com.tg.tiny4j.core.ioc.beans;

/**
 * Created by twogoods on 16/10/26.
 */
public interface BeanPostProcessor {
    Object postProcessBeforeInitialization(Object bean, String beanName);

    Object postProcessAfterInitialization(Object bean, String beanName);

}
