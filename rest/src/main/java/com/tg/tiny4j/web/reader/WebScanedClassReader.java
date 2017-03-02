package com.tg.tiny4j.web.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by twogoods on 16/11/2.
 */
public class WebScanedClassReader extends AbstractClassReader {
    private static Logger log = LoggerFactory.getLogger(WebScanedClassReader.class);

    Set<Class> classSet = new HashSet<>();

    public void loadClass(String packageConfig) {
        try {
            for (String p : packageConfig.split(",")) {
                ClassScanner.getClasses(ClassScanner.getPathByPackage(p), classSet);
            }

            log.debug("get class:{}", classSet);
            for (Class clazz : classSet) {
                read(clazz);
            }
        } catch (Exception e) {
            log.error("loadclass error:{}", e);
            throw new RuntimeException(e);
        }
    }
}
