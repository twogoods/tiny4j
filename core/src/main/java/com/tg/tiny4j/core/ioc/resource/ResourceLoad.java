package com.tg.tiny4j.core.ioc.resource;

import com.tg.tiny4j.core.ioc.exception.ConfigurationException;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * Created by twogoods on 16/10/25.
 */
public class ResourceLoad {

    private static ResourceLoad resourceLoad = new ResourceLoad();

    public static ResourceLoad getInstance() {
        return resourceLoad;
    }

    private ResourceLoad() {
    }

    public Resource loadResource(String location) {
        URL url = getClass().getClassLoader().getResource(location);
        return new UrlResource(url);
    }

    public Properties loadConfig(String path) throws Exception {
        InputStream in = getClass().getClassLoader().getResourceAsStream(path);
        if(in==null){
            throw new ConfigurationException(String.format("'%s' is not exist",path));
        }
        try {
            Properties prop = new Properties();
            prop.load(in);
            return prop;
        } finally {
            in.close();
        }
    }

}