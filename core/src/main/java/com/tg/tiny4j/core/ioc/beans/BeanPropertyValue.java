package com.tg.tiny4j.core.ioc.beans;

import java.util.Objects;

/**
 * Created by twogoods on 16/10/26.
 */
public class BeanPropertyValue {
    private String name;
    private Object value;

    public BeanPropertyValue() {
    }

    public BeanPropertyValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BeanPropertyValue that = (BeanPropertyValue) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
