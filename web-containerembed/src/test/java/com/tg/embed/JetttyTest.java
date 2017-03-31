package com.tg.embed;

import com.tg.tiny4j.web.contextlistener.SingleRestLoaderListener;
import com.tg.tiny4j.web.servlet.DispatcherServlet;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Description:
 *
 * @author twogoods
 * @version 0.1
 * @since 2017-03-31
 */
public class JetttyTest {
    @Test
    public void test() throws Exception {
        //http://www.eclipse.org/jetty/documentation/9.4.x/embedding-jetty.html
        Server server = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/twogoods");
        context.addEventListener(new SingleRestLoaderListener());
        context.addServlet(DispatcherServlet.class, "/*");
        server.setHandler(context);
        server.start();
        server.join();
    }


    public static class HelloServlet extends HttpServlet {
        @Override
        public void init() throws ServletException {
            System.out.println("HelloServlet init...");
        }

        @Override
        protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("<h1>Hello from HelloServlet</h1>");
        }
    }

    public static class DefaultServlet extends HttpServlet {
        @Override
        protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("<h1>default</h1>");
        }
    }

    public static class HelloHandler extends AbstractHandler {
        public void handle(String target,
                           Request baseRequest,
                           HttpServletRequest request,
                           HttpServletResponse response) throws IOException,
                ServletException {
            response.setContentType("text/html; charset=utf-8");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            PrintWriter out = response.getWriter();
            out.println("<h1>" + 404 + "</h1>");
            baseRequest.setHandled(true);
        }
    }
}