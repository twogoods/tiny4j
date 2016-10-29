package com.tg.tiny4j.core.ioc.beans.reader;

import com.tg.tiny4j.core.ioc.beans.BeanDefinition;

import java.util.Map;

/**
 * Created by twogoods on 16/10/26.
 */
public interface BeanDefinitionReader {

    void loadResource() throws Exception;

    Map<String, BeanDefinition> getRegisterBeans();


}
