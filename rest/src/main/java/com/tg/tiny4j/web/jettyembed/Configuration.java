package com.tg.tiny4j.web.jettyembed;

/**
 * Description:
 *
 * @author twogoods
 * @version 0.1
 * @since 2017-03-31
 */
public class Configuration {
    private String componentscan;
    private String contextPath = "twogoods";
    private int port = 8080;

    public String getComponentscan() {
        return componentscan;
    }

    public void setComponentscan(String componentscan) {
        this.componentscan = componentscan;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
