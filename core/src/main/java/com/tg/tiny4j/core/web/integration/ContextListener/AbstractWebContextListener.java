package com.tg.tiny4j.core.web.integration.ContextListener;

import com.tg.tiny4j.commons.constants.WebApplicationEnvironment;
import com.tg.tiny4j.core.web.integration.HandleRegistry;
import com.tg.tiny4j.core.web.integration.context.ServletContainerApplicationContext;
import com.tg.tiny4j.core.web.integration.context.WebApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by twogoods on 16/11/5.
 */
public abstract class AbstractWebContextListener implements ServletContextListener {

    private static Logger log = LoggerFactory.getLogger(AbstractWebContextListener.class);

    private  WebApplicationContext webApplicationContext;
    private HandleRegistry registry = new HandleRegistry();

    public abstract void registerHandle(HandleRegistry registry);

    public abstract void requestMapInitialized(ServletContextEvent servletContextEvent, WebApplicationContext applicationContext) throws Exception;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            registerHandle(registry);
            webApplicationContext = new ServletContainerApplicationContext(registry.getHandle());
            requestMapInitialized(servletContextEvent, webApplicationContext);
            servletContextEvent.getServletContext().setAttribute(WebApplicationEnvironment.RUN_MODE, WebApplicationEnvironment.CONTAINER_MODE);
//            servletContextEvent.getServletContext().setAttribute("root_applicationcontext", webApplicationContext);
        } catch (Exception e) {
            log.error("Context initialization failed", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        log.info("SingleRestLoaderListener destroyed...");
    }
}
