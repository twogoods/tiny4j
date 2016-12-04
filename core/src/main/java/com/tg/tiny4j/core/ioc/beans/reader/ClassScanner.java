package com.tg.tiny4j.core.ioc.beans.reader;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Set;

/**
 * Created by twogoods on 16/10/28.
 */
public class ClassScanner {

    private static final Logger log = LogManager.getLogger(ClassScanner.class);

    private static String basePath;

    /**
     * 得到根目录,自己编写的类部署的时候会放在一个 classes目录下
     * 跟classes同目录下会有一个lib目录
     *
     * @return
     */
    public static String getBasePath() {
        if (StringUtils.isEmpty(basePath)) {
            synchronized (ClassScanner.class) {
                if (StringUtils.isEmpty(basePath)) {
                    String baseFilePath = Thread.currentThread().getContextClassLoader().getResource("").toString();
                    basePath = baseFilePath.replace("file:", "");
                }
            }
        }
        return basePath;
    }

    public static String getPathByPackage(String packageName) {
        return getBasePath() + packageName.replace(".", File.separator);
    }

    private static String getWholeClassName(String filePath) {
        String classFile = filePath.replace(getBasePath(), "");
        return classFile.substring(0, classFile.indexOf(".class")).replace(File.separator,".");

    }

    public static Set<Class> getClasses(String filePath, Set<Class> classSet) throws Exception {
        File file = new File(filePath);
        if(!file.exists()){
            throw new FileNotFoundException("check component-scan config, file or directory not found, path is "+filePath);
        }
        for (String fileName : file.list()) {
            String childFilePath = filePath + File.separator + fileName;
            if (new File(childFilePath).isDirectory()) {
                getClasses(childFilePath, classSet);
            } else if (fileName.endsWith(".class")) {
                String className = getWholeClassName(childFilePath);
                classSet.add(Class.forName(className));
            } else {
                //other file ignore
            }
        }
        return classSet;
    }
}
