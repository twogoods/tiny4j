package com.tg.tiny4j.core.web.integration.context;

import com.tg.tiny4j.core.ioc.beans.factory.DefaultBeanFactory;
import com.tg.tiny4j.core.web.integration.HandleAnnotation;
import com.tg.tiny4j.core.web.integration.WebAppBeanDefinitionReader;

/**
 * Description:
 *
 * @author twogoods
 * @version 0.1
 * @since 2017-03-31
 */
public class ServletContainerApplicationContext extends WebApplicationContext {
    private HandleAnnotation handle;

    public ServletContainerApplicationContext(HandleAnnotation handle) throws Exception {
        super();
        this.handle = handle;
        refresh();
    }

    protected void registerBeans() throws Exception {
        WebAppBeanDefinitionReader webAppBeanDefinitionReader = new WebAppBeanDefinitionReader(handle);
        //读取bean信息
        webAppBeanDefinitionReader.loadResource();
        beanFactory = new DefaultBeanFactory();
        beanFactory.addBeanDefinition(webAppBeanDefinitionReader.getRegisterBeans());
    }
}