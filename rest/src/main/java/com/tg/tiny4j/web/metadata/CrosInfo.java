package com.tg.tiny4j.web.metadata;

/**
 * Created by twogoods on 16/12/3.
 */
public class CrosInfo {
    private String origins;
    private String[] methods;
    private String maxAge;
    private String headers;
    private boolean cookie;

    public CrosInfo() {
    }

    public CrosInfo(String origins, String[] methods, String maxAge, String headers, boolean cookie) {
        this.origins = origins;
        this.methods = methods;
        this.maxAge = maxAge;
        this.headers = headers;
        this.cookie = cookie;
    }

    public String getOrigins() {
        return origins;
    }

    public void setOrigins(String origins) {
        this.origins = origins;
    }

    public String[] getMethods() {
        return methods;
    }

    public void setMethods(String[] methods) {
        this.methods = methods;
    }

    public String getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(String maxAge) {
        this.maxAge = maxAge;
    }

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    public boolean isCookie() {
        return cookie;
    }

    public void setCookie(boolean cookie) {
        this.cookie = cookie;
    }
}
