package com.tg.tiny4j.core.web.integration;

import com.tg.tiny4j.core.ioc.beans.BeanDefinition;
import com.tg.tiny4j.core.ioc.beans.reader.AutoConfigBeanDefinitionReader;

/**
 * Description:
 *
 * @author twogoods
 * @version 0.1
 * @since 2017-03-31
 */
public class BootAppBeanDefinitionReader extends AutoConfigBeanDefinitionReader{
    private HandleAnnotation handle;

    public BootAppBeanDefinitionReader(HandleAnnotation handle) {
        this.handle = handle;
    }

    @Override
    public BeanDefinition handleIntegrationAnnotation(Class clazz) throws Exception {
        return handle.handle(clazz);
    }
}
