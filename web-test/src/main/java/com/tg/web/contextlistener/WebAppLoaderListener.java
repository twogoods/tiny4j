package com.tg.web.contextlistener;

import com.tg.tiny4j.core.ioc.beans.BeanDefinition;
import com.tg.tiny4j.core.web.integration.AbstractWebContextListener;
import com.tg.tiny4j.core.web.integration.HandleAnnotation;
import com.tg.tiny4j.core.web.integration.HandleRegistry;
import com.tg.tiny4j.core.web.integration.WebApplicationContext;
import com.tg.tiny4j.web.integration.WebAppControllerReader;
import com.tg.tiny4j.web.metadata.ControllerInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by twogoods on 16/11/2.
 */
public class WebAppLoaderListener extends AbstractWebContextListener {
    private static Logger log= LogManager.getLogger(WebAppLoaderListener.class);

    @Override
    public void registerHandle(HandleRegistry registry) {
        final WebAppControllerReader webAppControllerReader=new WebAppControllerReader();
        HandleAnnotation handle=new HandleAnnotation() {
            @Override
            public BeanDefinition handle(Class clazz) throws ClassNotFoundException {
                ControllerInfo controllerInfo=webAppControllerReader.read(clazz);
                return new BeanDefinition(controllerInfo.getName(), controllerInfo.getClassName());
            }
        };
        registry.addHandle(handle);
    }
}
