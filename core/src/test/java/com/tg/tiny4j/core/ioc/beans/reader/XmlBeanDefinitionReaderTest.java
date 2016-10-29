package com.tg.tiny4j.core.ioc.beans.reader;

import org.junit.Test;


/**
 * Created by twogoods on 16/10/26.
 */
public class XmlBeanDefinitionReaderTest {

    @Test
    public void testLoadResource() throws Exception {
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader("test.xml");
        beanDefinitionReader.loadResource();
        System.out.println(beanDefinitionReader.getRegisterBeans());
        assert beanDefinitionReader.getRegisterBeans().size() > 0;
    }
}