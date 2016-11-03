package com.tg.tiny4j.web.contextlistener;

import com.tg.tiny4j.core.ioc.context.ApplicationContext;
import com.tg.tiny4j.core.ioc.context.ClassPathXmlApplicationContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by twogoods on 16/10/29.
 */
public class ContextLoaderListener implements ServletContextListener {

    private Logger log = LogManager.getLogger(ContextLoaderListener.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
        ApplicationContext applicationContext;
        try {
            applicationContext= new ClassPathXmlApplicationContext("application.xml");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        servletContext.setAttribute("web_root_context", "twogoods");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
