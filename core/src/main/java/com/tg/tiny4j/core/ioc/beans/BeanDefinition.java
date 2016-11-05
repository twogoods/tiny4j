package com.tg.tiny4j.core.ioc.beans;

import com.tg.tiny4j.core.ioc.exception.PropertyDuplicateException;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by twogoods on 16/10/26.
 */
public class BeanDefinition {
    private Object bean;

    private String id;

    private String classname;

    private Class clazz;

    private Set<BeanPropertyValue> properties=new HashSet<BeanPropertyValue>();

    public BeanDefinition() {

    }

    public BeanDefinition(String id, String classname) throws ClassNotFoundException {
        this.id = id;
        this.classname = classname;
        clazz=Class.forName(classname);
    }

    public BeanDefinition(String id, String classname, Class clazz) {
        this.id = id;
        this.classname = classname;
        this.clazz = clazz;
    }

    public BeanDefinition(String id, Class clazz) {
        this.id = id;
        this.clazz = clazz;
        this.classname=clazz.getCanonicalName();
    }

    public void addProperty(BeanPropertyValue beanProperty) throws PropertyDuplicateException {
        if (properties.contains(beanProperty)) {
            throw new PropertyDuplicateException(String.format("class:%s , property '%s' is duplicate",classname,beanProperty.getName()));
        }
        properties.add(beanProperty);
    }

    public Set<BeanPropertyValue> getProperties() {
        return properties;
    }

    public String getId() {
        return id;
    }

    public String getClassname() {
        return classname;
    }

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public Class getClazz() {
        return clazz;
    }
}
