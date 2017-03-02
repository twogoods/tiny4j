package com.tg.tiny4j.core.web.integration;

import com.tg.tiny4j.commons.constants.WebApplicationEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by twogoods on 16/11/5.
 */
public abstract class AbstractWebContextListener implements ServletContextListener {

    private static Logger log = LoggerFactory.getLogger(AbstractWebContextListener.class);

    private HandleRegistry registry = new HandleRegistry();

    public abstract void registerHandle(HandleRegistry registry);

    public abstract void requestMapInitialized(ServletContextEvent servletContextEvent, WebApplicationContext applicationContext) throws Exception;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        WebApplicationContext webApplicationContext;
        try {
            registerHandle(registry);
            webApplicationContext = new WebApplicationContext(registry.getHandle());
            requestMapInitialized(servletContextEvent, webApplicationContext);
            servletContextEvent.getServletContext().setAttribute(WebApplicationEnvironment.RUN_MODE, WebApplicationEnvironment.CONTAINER_MODE);
//            servletContextEvent.getServletContext().setAttribute("root_applicationcontext", webApplicationContext);
        } catch (Exception e) {
            log.error("Context initialization failed", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
