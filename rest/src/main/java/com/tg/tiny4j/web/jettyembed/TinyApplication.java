package com.tg.tiny4j.web.jettyembed;

import com.tg.tiny4j.web.reader.ConfigLoader;
import com.tg.tiny4j.web.servlet.DispatcherServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import java.util.EventListener;

/**
 * Description:
 *
 * @author twogoods
 * @version 0.1
 * @since 2017-03-31
 */
public class TinyApplication {
    public static void run(Class<? extends EventListener> listener, String[] args) {
        try {
            Configuration configuration = ConfigLoader.loadConfig();
            Server server = new Server(configuration.getPort());
            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath(configuration.getContextPath());
            context.addEventListener(listener.newInstance());
            context.addServlet(DispatcherServlet.class, "/*");
            server.setHandler(context);
            server.start();
            server.join();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
