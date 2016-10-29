package com.tg.tiny4j.core.ioc.beans;

/**
 * Created by twogoods on 16/10/26.
 */
public class BeanReference {
    private String name;
    private Object bean;

    public BeanReference() {
    }

    public BeanReference(String ref) {
        this.name = ref;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }
}
