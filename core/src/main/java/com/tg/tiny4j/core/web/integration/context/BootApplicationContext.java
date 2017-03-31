package com.tg.tiny4j.core.web.integration.context;

import com.tg.tiny4j.core.ioc.beans.factory.DefaultBeanFactory;
import com.tg.tiny4j.core.web.integration.BootAppBeanDefinitionReader;
import com.tg.tiny4j.core.web.integration.HandleAnnotation;
/**
 * Description:
 *
 * @author twogoods
 * @version 0.1
 * @since 2017-03-31
 */
public class BootApplicationContext extends WebApplicationContext {
    private HandleAnnotation handle;

    public BootApplicationContext(HandleAnnotation handle) throws Exception {
        super();
        this.handle = handle;
        refresh();
    }

    protected void registerBeans() throws Exception {
        BootAppBeanDefinitionReader bootAppBeanDefinitionReader = new BootAppBeanDefinitionReader(handle);
        //读取bean信息
        bootAppBeanDefinitionReader.loadResource();
        beanFactory = new DefaultBeanFactory();
        beanFactory.addBeanDefinition(bootAppBeanDefinitionReader.getRegisterBeans());
    }
}