package com.tg.tiny4j.core.web.integration;

import com.tg.tiny4j.core.ioc.context.ApplicationContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by twogoods on 16/11/5.
 */
public abstract class AbstractWebContextListener implements ServletContextListener {

    private static Logger log = LogManager.getLogger(AbstractWebContextListener.class);

    private HandleRegistry registry=new HandleRegistry();

    public abstract void registerHandle(HandleRegistry registry);

    public abstract void requestMapInitialized(ServletContextEvent servletContextEvent,ApplicationContext applicationContext) throws Exception;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        WebApplicationContext webApplicationContext;
        try {
            registerHandle(registry);
            webApplicationContext = new WebApplicationContext(registry.getHandle());

            requestMapInitialized(servletContextEvent,webApplicationContext);

            servletContextEvent.getServletContext().setAttribute("run_mode", "withcontainer");
            servletContextEvent.getServletContext().setAttribute("root_applicationcontext", webApplicationContext);
        } catch (Exception e) {
            log.error("Context initialization failed", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
