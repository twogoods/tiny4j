package com.tg.integration.context.listener;

import com.tg.integration.context.WebApplicationContext;
import com.tg.integration.reader.WebAppControllerReader;
import com.tg.tiny4j.web.reader.WebControllerReader;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by twogoods on 16/11/2.
 */
public class WebAppLoaderListener implements ServletContextListener{
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        WebAppControllerReader webAppControllerReader=new WebAppControllerReader();
        WebApplicationContext webApplicationContext;
        try {
            webApplicationContext=new WebApplicationContext(webAppControllerReader);
            servletContextEvent.getServletContext().setAttribute("run_mode","withcontainer");
            servletContextEvent.getServletContext().setAttribute("root_applicationcontext",webApplicationContext);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
