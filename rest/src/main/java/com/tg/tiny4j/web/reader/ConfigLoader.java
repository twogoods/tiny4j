package com.tg.tiny4j.web.reader;

import com.tg.tiny4j.commons.constants.ConfigurationElement;
import com.tg.tiny4j.web.exception.ConfigurationException;
import com.tg.tiny4j.web.jettyembed.Configuration;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Description:
 *
 * @author twogoods
 * @version 0.1
 * @since 2017-03-13
 */
public class ConfigLoader {
    private static final Logger log = LoggerFactory.getLogger(ConfigLoader.class);


    private ConfigLoader() {
    }

    public static Configuration loadConfig() throws Exception {
        return loadConfig(ConfigurationElement.DEFAULT_CONFIG_FILE);
    }

    public static Configuration loadConfig(String name) throws Exception {
        Configuration configuration = new Configuration();
        InputStream in = ConfigLoader.class.getClassLoader().getResourceAsStream(name);
        if (in == null) {
            throw new ConfigurationException(String.format("'%s' is not exist", name));
        }
        try {
            Properties prop = new Properties();
            prop.load(in);
            Enumeration en = prop.propertyNames();
            while (en.hasMoreElements()) {
                String strKey = (String) en.nextElement();
                log.debug("get config : {} = {}", strKey, prop.getProperty(strKey));
                if (ConfigurationElement.COMPONENTSCAN.equals(strKey)) {
                    configuration.setComponentscan(prop.getProperty(strKey));
                } else if (ConfigurationElement.SERVER_CONTEXTPATH.equals(strKey)) {
                    configuration.setContextPath(prop.getProperty(strKey));
                } else if (ConfigurationElement.SERVER_PORT.equals(strKey)) {
                    configuration.setPort(Integer.valueOf(prop.getProperty(strKey)));
                }
            }
        } finally {
            IOUtils.closeQuietly(in);
        }
        return configuration;
    }

}
