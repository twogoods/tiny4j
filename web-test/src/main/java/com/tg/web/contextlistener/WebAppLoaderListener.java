package com.tg.web.contextlistener;

import com.tg.tiny4j.commons.constants.WebApplicationEnvironment;
import com.tg.tiny4j.commons.utils.Validate;
import com.tg.tiny4j.core.ioc.beans.BeanDefinition;
import com.tg.tiny4j.core.web.integration.ContextListener.AbstractWebContextListener;
import com.tg.tiny4j.core.web.integration.HandleAnnotation;
import com.tg.tiny4j.core.web.integration.HandleRegistry;
import com.tg.tiny4j.core.web.integration.context.WebApplicationContext;
import com.tg.tiny4j.web.integration.WebAppControllerReader;
import com.tg.tiny4j.web.metadata.BaseInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.servlet.ServletContextEvent;

/**
 * Created by twogoods on 16/11/2.
 */
public class WebAppLoaderListener extends AbstractWebContextListener {
    private static final Logger log = LoggerFactory.getLogger(WebAppLoaderListener.class);

    private final WebAppControllerReader webAppControllerReader = new WebAppControllerReader();

    @Override
    public void registerHandle(HandleRegistry registry) {
        HandleAnnotation handle = new HandleAnnotation() {
            @Override
            public BeanDefinition handle(Class clazz) throws Exception {
                BaseInfo baseInfo = webAppControllerReader.read(clazz);
                if (Validate.isEmpty(baseInfo)) {
                    return null;
                } else {
                    return new BeanDefinition(baseInfo.getName(), baseInfo.getClassName());
                }
            }
        };
        registry.addHandle(handle);
    }

    @Override
    public void requestMapInitialized(ServletContextEvent servletContextEvent, WebApplicationContext applicationContext) throws Exception {
        webAppControllerReader.initRequestMap();
        webAppControllerReader.setInstances(applicationContext.getBeans(webAppControllerReader.getControllerName()));
        servletContextEvent.getServletContext().setAttribute(WebApplicationEnvironment.WEBREQUESTMAPPER, webAppControllerReader.getRequestMapper());
    }
}