package com.tg.tiny4j.core.ioc.beans.reader;

import com.tg.tiny4j.core.ioc.beans.BeanAnnotatedDefinition;
import com.tg.tiny4j.core.ioc.beans.BeanDefinition;
import com.tg.tiny4j.core.ioc.beans.BeanPropertyValue;
import com.tg.tiny4j.core.ioc.beans.BeanReference;
import com.tg.tiny4j.core.ioc.exception.BeanDefinitionException;
import com.tg.tiny4j.core.ioc.annotation.*;
import com.tg.tiny4j.core.ioc.utils.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by twogoods on 16/10/26.
 */
public abstract class AnnotationBeanDefinitionReader extends AbstractBeanDefinitionReader {
    private static Logger log = LogManager.getLogger(AnnotationBeanDefinitionReader.class);

    private List<String> scanPackages = new ArrayList<>();

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
        praseAutoConfigurationClass();
    }

    private void praseAutoConfigurationClass() throws Exception {
        for (Class clazz : classSet) {
            Pair<Boolean, Boolean> result = checkBeanAnnotated(clazz);
            if (result.getL() && result.getR()) {
                praseBeanAnnotatedClass(clazz);
            } else if (result.getL()) {
                praseClass(clazz);
            }
        }
    }


    /**
     * pair的left指明是否是配置的bean,right指是否有@Bean注解
     */
    private Pair<Boolean, Boolean> checkBeanAnnotated(Class clazz) {
        Annotation[] annotations = clazz.getAnnotations();
        if (clazz.isAnnotationPresent(Configuration.class) || clazz.isAnnotationPresent(Component.class)) {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(Bean.class)) {
                    return Pair.of(true, true);
                }
            }
            return Pair.of(true, false);
        }
        return Pair.of(false, false);
    }

    private void praseBeanAnnotatedClass(Class clazz) throws Exception {
        BeanDefinition beanDefinition = handleTypeAnnot(clazz, true);
        if(beanDefinition==null){
            return;
        }
        handleFieldAnnot(clazz, beanDefinition);
        Map<String, String> methodinfos = handleMethodAnnotAndRegister(clazz);
        ((BeanAnnotatedDefinition) beanDefinition).putmethodInfos(methodinfos);
        registerBean(beanDefinition);
    }

    private void praseClass(Class clazz) throws Exception {
        BeanDefinition beanDefinition = handleTypeAnnot(clazz, false);
        if(beanDefinition==null){
            return;
        }
        handleFieldAnnot(clazz, beanDefinition);
        registerBean(beanDefinition);
    }

    public abstract BeanDefinition handleIntegrationAnnotation(Class clazz) throws ClassNotFoundException;

    /**
     * TODO annotation去重
     */
    private Annotation[] deDuplicationAnnotation(Annotation[] annotations) {
        return annotations;
    }

    private BeanDefinition handleTypeAnnot(Class clazz, boolean isBeanAnnotated) throws Exception {
        Annotation[] annotations = clazz.getAnnotations();
        for (Annotation annotation : annotations) {
            System.out.println(annotation + " is? " + annotation.annotationType().isAnnotationPresent(Component.class));
            if (annotation.annotationType() == Component.class) {
                //Component注解
                Component configAnnot = (Component) annotation;
                if (isBeanAnnotated) {
                    return new BeanAnnotatedDefinition(getBeanName(configAnnot.name(), clazz), clazz);
                } else {
                    return new BeanDefinition(getBeanName(configAnnot.name(), clazz), clazz);
                }
            } else if (annotation.annotationType().isAnnotationPresent(Component.class)) {
                //Configuration
                if (annotation.annotationType() == Configuration.class) {
                    Configuration configAnnot = (Configuration) annotation;
                    if (isBeanAnnotated) {
                        return new BeanAnnotatedDefinition(getBeanName(configAnnot.name(), clazz), clazz);
                    } else {
                        return new BeanDefinition(getBeanName(configAnnot.name(), clazz), clazz);
                    }
                }
            }
        }
        //有其他的注解,这些的解析交给子类完成
        return handleIntegrationAnnotation(clazz);
    }

    private void registerBean(BeanDefinition beanDefinition) throws Exception {
        log.debug("bean: {}", beanDefinition);
        //TODO 查重
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

    private Map<String, String> handleMethodAnnotAndRegister(Class clazz) throws BeanDefinitionException {
        Map<String, String> methodInfos = new HashMap<>();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof Bean) {
                    if (method.getParameterCount() > 0) {
                        throw new BeanDefinitionException(String.format("{} ,method annotated by '@Bean' is not support parameter, so let parameter blank", method));
                    }
                    Bean beanAnnot = (Bean) annotation;
                    String beanName = getBeanName(beanAnnot.name(), method.getName());
                    BeanDefinition beanDefinition = new BeanDefinition(beanName, method.getReturnType());
                    methodInfos.put(method.getName(), beanName);
                    getRegisterBeans().putIfAbsent(beanDefinition.getId(), beanDefinition);
                    //TODO 似乎不行???当前这个bean初始化后,执行这个bean方法,类似后置处理器.
                }
            }
        }
        return methodInfos;
    }


    public void setScanPackages(List<String> scanPackages) {
        this.scanPackages.addAll(scanPackages);
    }
}
