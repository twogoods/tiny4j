package com.tg.tiny4j.core.aop.advice;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by twogoods on 16/10/31.
 */
public class AutoSetterCglibMethodinvocation extends CglibMethodinvocation {
    private Map<String,String> autoSetter;

    public AutoSetterCglibMethodinvocation(Object target, Method method, Object[] args, MethodProxy methodProxy, Map<String, String> autoSetter) {
        super(target, method, args, methodProxy);
        this.autoSetter = autoSetter;
    }

    public AutoSetterCglibMethodinvocation(Object target, Method method, Object[] args, MethodProxy methodProxy, Class clazz, Map<String, String> autoSetter) {
        super(target, method, args, methodProxy, clazz);
        this.autoSetter = autoSetter;
    }

    public Map<String, String> getAutoSetter() {
        return autoSetter;
    }
}
