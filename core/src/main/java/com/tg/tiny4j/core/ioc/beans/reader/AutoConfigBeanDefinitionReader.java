package com.tg.tiny4j.core.ioc.beans.reader;

import com.tg.tiny4j.core.ioc.beans.BeanDefinition;

/**
 * Created by twogoods on 16/10/29.
 */
public class AutoConfigBeanDefinitionReader extends AnnotationBeanDefinitionReader{

    private void initConfig() throws Exception {
        //默认取application.properties
        loadConfig("application.properties");
    }

    @Override
    public BeanDefinition handleIntegrationAnnotation(Class clazz) {
        return null;
    }
}
