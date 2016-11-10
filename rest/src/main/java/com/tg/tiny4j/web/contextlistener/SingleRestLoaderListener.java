package com.tg.tiny4j.web.contextlistener;

import com.tg.tiny4j.web.reader.WebScanedClassReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
/**
 * Created by twogoods on 16/11/2.
 */
public class SingleRestLoaderListener implements ServletContextListener {
    private static Logger log = LogManager.getLogger(SingleRestLoaderListener.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        String packages = servletContextEvent.getServletContext().getInitParameter("component-scan").toString();
        log.debug("auto package {}", packages);
        WebScanedClassReader webScanedClassReader = new WebScanedClassReader();
        webScanedClassReader.loadClass(packages);
        try {
            webScanedClassReader.initRequestMap();
            webScanedClassReader.instance4SingleMode();
            servletContextEvent.getServletContext().setAttribute("run_mode", "single");
            servletContextEvent.getServletContext().setAttribute("webrequestmapper", webScanedClassReader.getRequestMapper());
        } catch (Exception e) {
            log.error("start rest failed", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}