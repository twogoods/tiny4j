package com.tg.tiny4j.core.web.integration;

import com.tg.tiny4j.core.ioc.beans.factory.AutoBeanFactory;
import com.tg.tiny4j.core.ioc.beans.reader.BeanDefinitionReader;
import com.tg.tiny4j.core.ioc.context.AbstractApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by twogoods on 16/11/2.
 */
public class WebApplicationContext extends AbstractApplicationContext {
    private HandleAnnotation handle;

    public WebApplicationContext(HandleAnnotation handle) throws Exception {
        this.handle = handle;
        System.out.println("handle in :" + handle);
        refresh();
    }

    protected void registerBeans() throws Exception {
        BeanDefinitionReader beanDefinitionReader = new WebAppBeanDefinitionReader(handle);
        //读取bean信息
        beanDefinitionReader.loadResource();
        beanFactory = new AutoBeanFactory();
        beanFactory.addBeanDefinition(beanDefinitionReader.getRegisterBeans());
    }

    public Map<String, Object> getBean(List<String> names) throws Exception {
        Map<String, Object> beans = new HashMap<>();
        for (String name : names) {
            beans.put(name, getBean(name));
        }
        return beans;
    }
}