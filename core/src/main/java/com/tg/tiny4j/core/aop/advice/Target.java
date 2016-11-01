package com.tg.tiny4j.core.aop.advice;

/**
 * Created by twogoods on 16/10/25.
 */
public class Target {
    private Object targetObj;
    private Class<?> clazz;
    private Class<?>[] interfaceClazz;

    public Target(Object target, Class<?> clazz, Class<?>[] interfaceClazz) {
        this.targetObj = target;
        this.clazz = clazz;
        this.interfaceClazz = interfaceClazz;
    }

    public Target(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Object getTargetObj() {
        return targetObj;
    }

    public void setTargetObj(Object targetObj) {
        this.targetObj = targetObj;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Class<?>[] getInterfaceClazz() {
        return interfaceClazz;
    }

    public void setInterfaceClazz(Class<?>[] interfaceClazz) {
        this.interfaceClazz = interfaceClazz;
    }
}
