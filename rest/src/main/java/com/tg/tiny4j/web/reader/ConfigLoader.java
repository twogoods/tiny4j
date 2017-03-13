package com.tg.tiny4j.web.reader;

import com.tg.tiny4j.commons.constants.Configuration;
import com.tg.tiny4j.web.exception.ConfigurationException;
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

    private static Map<String, String> configMap = new HashMap<>();

    private ConfigLoader() {}

    public static void loadConfig() throws Exception{
        loadConfig(Configuration.DEFAULT_CONFIG_FILE);
    }

    public static void loadConfig(String name) throws Exception {
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
                configMap.put(strKey, prop.getProperty(strKey));
                log.debug("get config : {} = {}", strKey, prop.getProperty(strKey));
            }
        } finally {
            in.close();
        }
    }

    public static Map<String, String> getConfigMap() {
        return configMap;
    }
}
