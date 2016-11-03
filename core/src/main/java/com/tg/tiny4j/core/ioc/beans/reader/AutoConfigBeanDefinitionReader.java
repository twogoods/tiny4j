package com.tg.tiny4j.core.ioc.beans.reader;

import com.tg.tiny4j.core.ioc.beans.BeanDefinition;

/**
 * Created by twogoods on 16/10/29.
 */
public class AutoConfigBeanDefinitionReader extends AnnotationBeanDefinitionReader{

    private void initConfig() throws Exception {
        //TODO 扫描哪个包,传统的配置在web.xml里,或者springboot那样注解
        //默认取application.properties
        loadConfig("application.properties");
    }

    @Override
    public BeanDefinition handleIntegrationAnnotation(Class clazz) {
        return null;
    }
}
