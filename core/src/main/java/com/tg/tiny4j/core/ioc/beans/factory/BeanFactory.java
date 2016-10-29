package com.tg.tiny4j.core.ioc.beans.factory;

/**
 * Created by twogoods on 16/10/25.
 */
public interface BeanFactory {
    Object getBean(String name) throws Exception;

    <T> T getBean(String name, Class<T> clazz) throws Exception;

    boolean containsBean(String name) throws Exception;

}
