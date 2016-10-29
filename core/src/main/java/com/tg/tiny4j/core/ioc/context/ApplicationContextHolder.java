package com.tg.tiny4j.core.ioc.context;

/**
 * Created by twogoods on 16/10/25.
 */
public class ApplicationContextHolder {
    private ApplicationContext applicationContext;

    public ApplicationContextHolder(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
    Object getBean(String name) throws Exception{
        return applicationContext.getBean(name);
    }

    <T> T getBean(String name, Class<T> clazz) throws Exception{
        return applicationContext.getBean(name,clazz);
    }

    boolean containsBean(String name) throws Exception{
        return applicationContext.containsBean(name);
    }
}
