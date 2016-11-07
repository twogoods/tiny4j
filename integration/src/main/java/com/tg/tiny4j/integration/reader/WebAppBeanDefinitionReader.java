package com.tg.tiny4j.integration.reader;

import com.tg.tiny4j.core.ioc.beans.BeanDefinition;
import com.tg.tiny4j.core.ioc.beans.reader.XmlBeanDefinitionReader;
import com.tg.tiny4j.web.metadata.ControllerInfo;
import com.tg.tiny4j.web.reader.Reader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Created by twogoods on 16/11/1.
 */
public class WebAppBeanDefinitionReader extends XmlBeanDefinitionReader {
    private static Logger log= LogManager.getLogger(WebAppBeanDefinitionReader.class);
    private Reader reader;

    public WebAppBeanDefinitionReader(String location, Reader reader) {
        super(location);
        this.reader = reader;
    }

    public WebAppBeanDefinitionReader(Reader reader) {
        super("application.xml");
        this.reader = reader;
    }

    @Override
    public BeanDefinition handleIntegrationAnnotation(Class clazz) throws ClassNotFoundException {
        log.info("do prase:{}",clazz);

        ControllerInfo controllerInfo = reader.read(clazz);
        return new BeanDefinition(controllerInfo.getName(), controllerInfo.getClassName());
    }
}
