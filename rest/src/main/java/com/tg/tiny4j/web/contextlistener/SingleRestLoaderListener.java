package com.tg.tiny4j.web.contextlistener;

import com.tg.tiny4j.web.metadata.ControllerInfo;
import com.tg.tiny4j.web.reader.ClassScanner;
import com.tg.tiny4j.web.reader.Reader;
import com.tg.tiny4j.web.reader.WebControllerReader;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by twogoods on 16/11/2.
 */
public class SingleRestLoaderListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        String packages = servletContextEvent.getServletContext().getAttribute("component-scan").toString();
        WebControllerReader webControllerReader = new WebControllerReader();
        webControllerReader.loadClass(packages);
        servletContextEvent.getServletContext().setAttribute("run_mode","withcontainer");
        servletContextEvent.getServletContext().setAttribute("webController",webControllerReader.getApis());
        servletContextEvent.getServletContext().setAttribute("requestHandle",webControllerReader.getRequestHandleMap());
    }

    private void setControllerInstance(Reader reader) throws Exception {
        Map<String, ControllerInfo> controllerInfoMap = reader.getApis();
        for (String key : controllerInfoMap.keySet()) {
            ControllerInfo controller = controllerInfoMap.get(key);
            controller.setObject(controller.getClazz().newInstance());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}