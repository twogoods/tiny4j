package com.tg.tiny4j.core.ioc.context;

import com.tg.tiny4j.core.ioc.beans.BeanDefinition;
import com.tg.tiny4j.core.ioc.beans.factory.AbstractBeanFactory;

/**
 * Created by twogoods on 16/10/25.
 */
public abstract class AbstractApplicationContext implements ApplicationContext{
    protected AbstractBeanFactory beanFactory;

    protected void refresh() throws Exception {
        registerBeans();
        //初始化容器上下文类
        instanceContextHolder();
        instanceBeanFactory();
    }

    protected void registerBeans() throws Exception {}

    /**
     *增加一个名为applicationContextHolder的bean保存容器上下文
     */
    private void instanceContextHolder(){
        BeanDefinition beanDefinition=new BeanDefinition("applicationContextHolder",ApplicationContextHolder.class);
        beanDefinition.setBean(new ApplicationContextHolder(this));
        beanFactory.addBeanDefinition("applicationContextHolder",beanDefinition);
    }

    public void instanceBeanFactory() throws Exception {
        beanFactory.preInstantiateSingletons();
    }

    @Override
    public Object getBean(String name) throws Exception {
        return beanFactory.getBean(name);
    }

    @Override
    public <T> T getBean(String name, Class<T> clazz) throws Exception {
        return beanFactory.getBean(name,clazz);
    }
    @Override
    public boolean containsBean(String name) throws Exception {
        return beanFactory.containsBean(name);
    }
}
