package com.tg.integration.context;

import com.tg.integration.reader.WebAppBeanDefinitionReader;
import com.tg.tiny4j.core.ioc.beans.factory.AutoBeanFactory;
import com.tg.tiny4j.core.ioc.beans.reader.BeanDefinitionReader;
import com.tg.tiny4j.core.ioc.context.AbstractApplicationContext;
import com.tg.tiny4j.web.metadata.ControllerInfo;
import com.tg.tiny4j.web.reader.AbstractControllerReader;
import com.tg.tiny4j.web.reader.WebControllerReader;

import java.util.Map;

/**
 * Created by twogoods on 16/11/2.
 */
public class WebApplicationContext extends AbstractApplicationContext {
    private AbstractControllerReader reader;

    public WebApplicationContext(AbstractControllerReader reader) throws Exception {
        this.reader=reader;
        refresh();
        setControllerInstance();
    }

    protected void registerBeans() throws Exception {
        BeanDefinitionReader beanDefinitionReader = new WebAppBeanDefinitionReader(reader);
        beanDefinitionReader.loadResource();
        beanFactory = new AutoBeanFactory();
        beanFactory.addBeanDefinition(beanDefinitionReader.getRegisterBeans());
    }

    private void setControllerInstance() throws Exception {
        Map<String,ControllerInfo> controllers=reader.getApis();
        for(String key:controllers.keySet()){
            Object instance=this.getBean(key);
            controllers.get(key).setObject(instance);
        }
    }
}