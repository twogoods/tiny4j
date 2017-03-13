package com.tg.tomcat;

import com.tg.tiny4j.web.contextlistener.SingleRestLoaderListener;
import com.tg.tiny4j.web.reader.ConfigLoader;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description:
 *
 * @author twogoods
 * @version 0.1
 * @since 2017-03-13
 */
public class InnerJetty {

    //http://www.eclipse.org/jetty/documentation/9.4.x/embedding-jetty.html
    public static void main(String[] args) throws Exception {
        ConfigLoader.loadConfig();
        System.out.println(ConfigLoader.getConfigMap());
        Server server = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
//        context.addEventListener(new SingleRestLoaderListener());
        context.addServlet(HelloServlet.class, "/*");
        server.setHandler(context);
        server.start();
        server.join();
    }


    public static class HelloServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("<h1>Hello from HelloServlet</h1>");
        }
    }
}
