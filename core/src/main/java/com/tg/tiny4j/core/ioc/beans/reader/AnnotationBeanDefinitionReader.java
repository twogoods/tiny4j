package com.tg.tiny4j.core.ioc.beans.reader;

import com.tg.tiny4j.core.ioc.beans.BeanDefinition;
import com.tg.tiny4j.core.ioc.beans.BeanPropertyValue;
import com.tg.tiny4j.core.ioc.beans.BeanReference;
import com.tg.tiny4j.core.ioc.exception.BeanDefinitionException;
import com.tg.tiny4j.core.ioc.annotation.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by twogoods on 16/10/26.
 */
public class AnnotationBeanDefinitionReader extends AbstractBeanDefinitionReader {
    private static Logger log = LogManager.getLogger(AnnotationBeanDefinitionReader.class);

    private List<String> scanPackages=new ArrayList<>();

    private Set<Class> classSet = new HashSet<>();

    public AnnotationBeanDefinitionReader() {
    }

    public AnnotationBeanDefinitionReader(List<String> scanPackages) {
        this.scanPackages = scanPackages;
    }

    @Override
    public void loadResource() throws Exception {
        for (String p : scanPackages) {
            ClassScanner.getClasses(ClassScanner.getPathByPackage(p), classSet);
        }
        log.debug("AnnotationBeanDefinitionReader load class...");
        praseAutoConfigurationClass();
    }

    private void praseAutoConfigurationClass() throws Exception {
        for (Class clazz : classSet) {
            handleTypeAnnot(clazz);
        }
    }

    private void handleTypeAnnot(Class clazz) throws Exception {
        Annotation[] annotations = clazz.getAnnotations();
        if (annotations.length < 1) {
            return;
        }
        for (Annotation annotation : annotations) {
            //这两个注解功能基本一样
            if (annotation instanceof Configuration) {
                Configuration configAnnot = (Configuration) annotation;
                registerBean(getBeanName(configAnnot.name(), clazz), clazz);
            } else if (annotation.annotationType() == Component.class) {
                Component configAnnot = (Component) annotation;
                registerBean(getBeanName(configAnnot.name(), clazz), clazz);
            } else {
                //ignore
            }
        }
    }

    private void registerBean(String beanName, Class clazz) throws Exception {
        BeanDefinition beanDefinition = new BeanDefinition(beanName, clazz);
        handleFieldAnnot(clazz, beanDefinition);

        log.debug("bean: {}",beanDefinition);
        getRegisterBeans().putIfAbsent(beanDefinition.getId(), beanDefinition);
    }

    private void handleFieldAnnot(Class clazz, BeanDefinition beanDefinition) throws Exception {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof Value) {
                    Value valueAnnot = (Value) annotation;
                    //TODO 类型转化
                    if (field.getType().equals(String.class)) {
                        String configValue = valueAnnot.value();
                        beanDefinition.addProperty(new BeanPropertyValue(field.getName(), getConfigValue(PlaceholderPraser.prase(configValue))));
                    } else {
                        throw new BeanDefinitionException(
                                String.format("sorry, the field '%s', '@Value' only support String!!! , class: %s",
                                        field.getName(), clazz.getName()));
                    }
                } else if (annotation instanceof Inject) {
                    Inject injectAnnot = (Inject) annotation;
                    BeanReference beanReference = new BeanReference(getBeanName(injectAnnot.name(), field.getType()));
                    beanDefinition.addProperty(new BeanPropertyValue(field.getName(), beanReference));
                }
            }
        }
    }

    /**
     * TODO 难处理!
     *
     * @param clazz
     */
    private void handleMethodAnnot(Class clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof Bean) {
                    Bean beanAnnot = (Bean) annotation;
                    getBeanName(beanAnnot.name(), method.getReturnType());
                    //TODO 当前这个bean初始化后,执行这个bean方法,类似后置处理器.
                }
            }
        }

    }



    public void setScanPackages(List<String> scanPackages) {
        this.scanPackages.addAll(scanPackages);
    }
}
