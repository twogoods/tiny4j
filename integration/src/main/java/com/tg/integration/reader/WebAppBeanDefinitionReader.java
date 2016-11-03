package com.tg.integration.reader;

import com.tg.tiny4j.core.ioc.beans.BeanDefinition;
import com.tg.tiny4j.core.ioc.beans.reader.XmlBeanDefinitionReader;
import com.tg.tiny4j.web.metadata.ControllerInfo;
import com.tg.tiny4j.web.reader.AbstractControllerReader;
import com.tg.tiny4j.web.reader.Reader;
import com.tg.tiny4j.web.reader.WebControllerReader;


/**
 * Created by twogoods on 16/11/1.
 */
public class WebAppBeanDefinitionReader extends XmlBeanDefinitionReader {
    public WebAppBeanDefinitionReader(String location,WebControllerReader reader) {
        super(location);
        this.reader=reader;
    }

    public WebAppBeanDefinitionReader(Reader reader) {
        super("application.xml");
        this.reader=reader;
    }

    private Reader reader;

    @Override
    public BeanDefinition handleIntegrationAnnotation(Class clazz) throws ClassNotFoundException {
        System.out.println("have unknow annotation...");

        ControllerInfo controllerInfo=reader.read(clazz);
        return new BeanDefinition(controllerInfo.getName(),controllerInfo.getClassName());
    }
}
