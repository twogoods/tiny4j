package com.tg.tiny4j.web.reader;

import com.tg.tiny4j.web.metadata.ControllerInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by twogoods on 16/11/2.
 */
public class WebControllerReader extends AbstractControllerReader {
    private static Logger log = LogManager.getLogger(WebControllerReader.class);

    Set<Class> classSet = new HashSet<>();

    public void loadClass(String packageConfig) {
        try {
            for (String p : packageConfig.split(",")) {
                ClassScanner.getClasses(ClassScanner.getPathByPackage(p), classSet);
            }
            for (Class clazz : classSet) {
                read(clazz);
            }
        } catch (Exception e) {
            log.error(e);
            throw new RuntimeException(e);
        }
    }
}
