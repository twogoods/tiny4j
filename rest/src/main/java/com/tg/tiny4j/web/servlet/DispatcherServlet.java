package com.tg.tiny4j.web.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by twogoods on 16/11/7.
 */
public class DispatcherServlet extends HttpServlet{

    @Override
    public void init() throws ServletException {
        ServletContext servletContext=getServletContext();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
