package com.tg.tiny4j.core.web.integration;

/**
 * Created by twogoods on 16/11/5.
 */
public class HandleRegistry {
    private HandleAnnotation handle;

    public void addHandle(HandleAnnotation handle){
        this.handle=handle;
    }

    public HandleAnnotation getHandle() {
        return handle;
    }
}
