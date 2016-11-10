package com.tg.tiny4j.core.web.integration;

import com.tg.tiny4j.core.ioc.beans.BeanDefinition;
import com.tg.tiny4j.core.ioc.beans.reader.XmlBeanDefinitionReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Created by twogoods on 16/11/1.
 */
public class WebAppBeanDefinitionReader extends XmlBeanDefinitionReader {
    private static Logger log= LogManager.getLogger(WebAppBeanDefinitionReader.class);
    private HandleAnnotation handle;

    public WebAppBeanDefinitionReader(String location,HandleAnnotation handle) {
        super(location);
        this.handle=handle;
    }

    public WebAppBeanDefinitionReader(HandleAnnotation handle) {
        super("application.xml");
        this.handle=handle;
    }

    @Override
    public BeanDefinition handleIntegrationAnnotation(Class clazz) throws Exception {
        log.info("do prase:{}",clazz);
        return handle.handle(clazz);
    }
}
