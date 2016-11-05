package com.tg.tiny4j.integration.context.listener;

import com.tg.tiny4j.integration.context.WebApplicationContext;
import com.tg.tiny4j.integration.reader.WebAppControllerReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.jni.*;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.lang.Error;

/**
 * Created by twogoods on 16/11/2.
 */
public class WebAppLoaderListener implements ServletContextListener {
    private static Logger log= LogManager.getLogger(WebAppLoaderListener.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        WebAppControllerReader webAppControllerReader = new WebAppControllerReader();
        WebApplicationContext webApplicationContext;
        try {
            webApplicationContext = new WebApplicationContext(webAppControllerReader);
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
