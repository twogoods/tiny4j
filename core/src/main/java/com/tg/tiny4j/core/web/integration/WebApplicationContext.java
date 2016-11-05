package com.tg.tiny4j.core.web.integration;

import com.tg.tiny4j.core.ioc.beans.factory.AutoBeanFactory;
import com.tg.tiny4j.core.ioc.beans.reader.BeanDefinitionReader;
import com.tg.tiny4j.core.ioc.context.AbstractApplicationContext;

/**
 * Created by twogoods on 16/11/2.
 */
public class WebApplicationContext extends AbstractApplicationContext {
    private HandleAnnotation handle;

    public WebApplicationContext(HandleAnnotation handle) throws Exception {
        this.handle=handle;
        refresh();
    }

    protected void registerBeans() throws Exception {
        BeanDefinitionReader beanDefinitionReader = new WebAppBeanDefinitionReader(handle);
        //读取bean信息
        beanDefinitionReader.loadResource();
        beanFactory = new AutoBeanFactory();
        beanFactory.addBeanDefinition(beanDefinitionReader.getRegisterBeans());
    }


}