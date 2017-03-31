package com.tg.tiny4j.core.ioc.beans.reader;

import com.tg.tiny4j.commons.constants.ConfigurationElement;
import com.tg.tiny4j.core.ioc.beans.BeanDefinition;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * Created by twogoods on 16/10/29.
 */
public class AutoConfigBeanDefinitionReader extends AnnotationBeanDefinitionReader {

    private void initConfig() throws Exception {
        //默认取application.properties
        loadConfig(ConfigurationElement.DEFAULT_CONFIG_FILE);
        //设置扫描的包
        String packages = getConfigValue(ConfigurationElement.COMPONENTSCAN);
        if (!StringUtils.isEmpty(packages)) {
            setScanPackages(Arrays.asList(packages.split(",")));
        }
    }

    @Override
    public void loadResource() throws Exception {
        initConfig();
        super.loadResource();
    }

    @Override
    public BeanDefinition handleIntegrationAnnotation(Class clazz) throws Exception {
        return null;
    }
}
