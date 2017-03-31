package com.tg.tiny4j.core.web.integration.context;

import com.tg.tiny4j.core.ioc.context.AbstractApplicationContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by twogoods on 16/11/2.
 */
public abstract class WebApplicationContext extends AbstractApplicationContext {
    public Map<String, Object> getBeans(List<String> names) throws Exception {
        Map<String, Object> beans = new HashMap<>();
        for (String name : names) {
            beans.put(name, getBean(name));
        }
        return beans;
    }
}