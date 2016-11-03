package com.tg.tiny4j.core.ioc.context;

import com.tg.tiny4j.core.ioc.beans.factory.AutoBeanFactory;
import com.tg.tiny4j.core.ioc.beans.reader.BeanDefinitionReader;
import com.tg.tiny4j.core.ioc.beans.reader.XmlBeanDefinitionReader;

/**
 * Created by twogoods on 16/10/25.
 */
public class ClassPathXmlApplicationContext extends AbstractApplicationContext{
    private String configLocation;

    public ClassPathXmlApplicationContext( String configLocation) throws Exception {
        this.configLocation = configLocation;
        refresh();
    }

    protected void registerBeans() throws Exception {
        BeanDefinitionReader beanDefinitionReader=new XmlBeanDefinitionReader(configLocation);
        beanDefinitionReader.loadResource();
        beanFactory=new AutoBeanFactory();
        beanFactory.addBeanDefinition(beanDefinitionReader.getRegisterBeans());
    }
}
