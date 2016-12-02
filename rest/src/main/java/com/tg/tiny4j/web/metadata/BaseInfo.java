package com.tg.tiny4j.web.metadata;

/**
 * Created by twogoods on 16/11/9.
 */
public class BaseInfo {
    protected String name;
    protected String className;
    protected Class clazz;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }
}
