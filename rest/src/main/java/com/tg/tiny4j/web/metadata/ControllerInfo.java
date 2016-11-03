package com.tg.tiny4j.web.metadata;

/**
 * Created by twogoods on 16/11/2.
 */
public class ControllerInfo {
    private String name;
    private String className;
    private Class clazz;
    private Object object;

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

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
