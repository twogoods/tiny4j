package com.tg.tiny4j.core.ioc.beans.reader;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

/**
 * Created by twogoods on 16/10/28.
 */
public class ClassScanner {
    private static final Logger log = LoggerFactory.getLogger(ClassScanner.class);
    public static Set<String> getClasses(List<String> packages) throws Exception {
        Set<String> classes = new HashSet<String>();
        for (String packageName : packages) {
            if (StringUtils.isEmpty(packageName)) {
                continue;
            }
            getClassByPackage(packageName.replace(".", File.separator), classes);
        }
        return classes;
    }

    private static void getClassByPackage(String packageName, Set<String> classes) throws Exception {
        Enumeration<URL> baseURLs = ClassScanner.class.getClassLoader().getResources(packageName);
        URL baseURL = null;
        while (baseURLs.hasMoreElements()) {
            baseURL = baseURLs.nextElement();
            if (baseURL != null) {
                log.debug("componentscan find: {}",baseURL.toString());
                String protocol = baseURL.getProtocol();
                String basePath = baseURL.getFile();
                if ("jar".equals(protocol)) {
                    //实际运行中看到了这样的形式的jar目录 /BOOT-INF/classes!/com ,classes是一个目录,去掉后面的!,原因还没弄明白.
                    basePath = basePath.replace("classes!", "classes");
                    String[] paths = basePath.split("jar!/");
                    if (paths.length == 2) {
                        findFileInJar(paths[0] + "jar!/", paths[1], classes);
                    } else if (paths.length > 2) {
                        int index = basePath.lastIndexOf("jar!/") + "jar".length();
                        String lastJarPath = basePath.substring(0, index);
                        String packagepath = basePath.substring(index + "!/".length(), basePath.length());
                        findFileInJarWithinJar(lastJarPath, packagepath, classes);
                    }
                } else {
                    getClassesInFile(basePath, packageName, classes);
                }
            }
        }
    }

    private static void findFileInJar(String filePath, String packagePath, Set<String> classes) throws IOException {
        URL url = new URL("jar", null, 0, filePath);
        URLConnection con = url.openConnection();
        if (con instanceof JarURLConnection) {
            JarURLConnection result = (JarURLConnection) con;
            JarFile jarFile = result.getJarFile();
            for (final Enumeration<JarEntry> enumJar = jarFile.entries(); enumJar.hasMoreElements(); ) {
                JarEntry entry = enumJar.nextElement();
                filterClass(entry.getName(), packagePath, classes);
            }
            jarFile.close();
        }
    }

    private static void findFileInJarWithinJar(String filePath, String packagePath, Set<String> classes) throws IOException {
        URL url = new URL("jar", null, 0, filePath);
        URLConnection con = url.openConnection();
        if (con instanceof JarURLConnection) {
            JarURLConnection jarURLConnection = (JarURLConnection) con;
            JarInputStream jarInputStream = new JarInputStream(jarURLConnection.getInputStream());
            JarEntry entry;
            while ((entry = jarInputStream.getNextJarEntry()) != null) {
                filterClass(entry.getName(), packagePath, classes);
            }
            IOUtils.closeQuietly(jarInputStream);
        }
    }

    private static void filterClass(String filePath, String packageName, Set<String> classes) {
        if (filePath.startsWith(packageName) && filePath.endsWith(".class")) {
            classes.add(getWholeClassName(filePath));
        }
    }


    public static void getClassesInFile(String filePath, String packagePath, Set<String> classes) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("check component-scan config, file or directory not found, path is " + filePath);
        }
        for (String fileName : file.list()) {
            String childFilePath = filePath;
            if (filePath.endsWith("/")) {
                childFilePath = childFilePath + fileName;
            } else {
                childFilePath = childFilePath + File.separator + fileName;
            }
            if (new File(childFilePath).isDirectory()) {
                getClassesInFile(childFilePath, packagePath, classes);
            } else if (fileName.endsWith(".class")) {
                String className = getWholeClassName(childFilePath);
                classes.add(className);
            }
        }
    }

    private static String getWholeClassName(String filePath) {
        if (filePath.indexOf("classes/") != -1) {
            return filePath.substring(filePath.indexOf("classes/") + "classes/".length(),
                    filePath.indexOf(".class")).replaceAll("/", ".");
        }
        return filePath.substring(0, filePath.indexOf(".class")).replaceAll("/", ".");
    }
}
