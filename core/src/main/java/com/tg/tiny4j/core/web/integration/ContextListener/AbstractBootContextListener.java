package com.tg.tiny4j.core.web.integration.ContextListener;

import com.tg.tiny4j.commons.constants.WebApplicationEnvironment;
import com.tg.tiny4j.core.web.integration.HandleRegistry;
import com.tg.tiny4j.core.web.integration.context.BootApplicationContext;
import com.tg.tiny4j.core.web.integration.context.WebApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Description:
 *
 * @author twogoods
 * @version 0.1
 * @since 2017-03-31
 */
public abstract class AbstractBootContextListener implements ServletContextListener {
    private static Logger log = LoggerFactory.getLogger(AbstractBootContextListener.class);

    private WebApplicationContext webApplicationContext;
    private HandleRegistry registry = new HandleRegistry();

    public abstract void registerHandle(HandleRegistry registry);

    public abstract void requestMapInitialized(ServletContextEvent servletContextEvent, WebApplicationContext applicationContext) throws Exception;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            registerHandle(registry);
            webApplicationContext = new BootApplicationContext(registry.getHandle());
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
