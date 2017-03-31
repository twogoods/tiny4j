package com.tg.tiny4j.core.ioc.beans.reader;

import com.tg.tiny4j.core.ioc.beans.BeanAnnotatedDefinition;
import com.tg.tiny4j.core.ioc.beans.BeanDefinition;
import com.tg.tiny4j.core.ioc.beans.BeanPropertyValue;
import com.tg.tiny4j.core.ioc.beans.BeanReference;
import com.tg.tiny4j.core.ioc.exception.BeanDefinitionException;
import com.tg.tiny4j.core.ioc.annotation.*;
import com.tg.tiny4j.commons.data.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by twogoods on 16/10/26.
 */
public abstract class AnnotationBeanDefinitionReader extends AbstractBeanDefinitionReader {
    private static Logger log = LoggerFactory.getLogger(AnnotationBeanDefinitionReader.class);

    private List<String> scanPackages = new ArrayList<>();

    private Set<Class> classSet = new HashSet<>();

    public AnnotationBeanDefinitionReader() {
    }

    public AnnotationBeanDefinitionReader(List<String> scanPackages) {
        this.scanPackages = scanPackages;
    }

    @Override
    public void loadResource() throws Exception {
        Set<String> classes = ClassScanner.getClasses(scanPackages);
        loadClasses(classes);
        praseAutoConfigurationClass();
    }

    private void loadClasses(Set<String> classes) throws ClassNotFoundException {
        for (String className : classes) {
            classSet.add(Class.forName(className));
        }
    }

    private void praseAutoConfigurationClass() throws Exception {
        for (Class clazz : classSet) {
            Pair<Boolean, Boolean> result = checkBeanAnnotated(clazz);
            if (result.getL() && result.getR()) {
                praseBeanAnnotatedClass(clazz);
            } else if (result.getL()) {
                praseClass(clazz);
            } else {
                //别的注解
                praseUnknowAnnotatedClass(clazz);
            }
        }
    }

    /**
     * pair的left指明是否是配置的bean,right指是否有@Bean注解
     */
    private Pair<Boolean, Boolean> checkBeanAnnotated(Class clazz) {
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
        if (beanDefinition == null) {
            return;
        }
        handleFieldAnnot(clazz, beanDefinition);
        //多一个步骤解析方法
        Map<String, String> methodinfos = handleMethodAnnotAndRegister(clazz);
        ((BeanAnnotatedDefinition) beanDefinition).putmethodInfos(methodinfos);
        registerBean(beanDefinition);
    }

    private void praseClass(Class clazz) throws Exception {
        BeanDefinition beanDefinition = handleTypeAnnot(clazz, false);
        if (beanDefinition == null) {
            return;
        }
        handleFieldAnnot(clazz, beanDefinition);
        registerBean(beanDefinition);
    }

    private void praseUnknowAnnotatedClass(Class clazz) throws Exception {
        BeanDefinition beanDefinition = handleIntegrationAnnotation(clazz);
        if (beanDefinition == null) {
            return;
        }
        handleFieldAnnot(clazz, beanDefinition);
        registerBean(beanDefinition);
    }

    public abstract BeanDefinition handleIntegrationAnnotation(Class clazz) throws Exception;

    /**
     * TODO 重构,annotation去重,去掉多余的 @Component
     * 目前只有@Component一个注解是ElementType.ANNOTATION_TYPE的,且只有@Component和@Configuration可以注解类
     * 那么只针对当前model下定义的这些注解做特殊情况处理
     * 后续重构,支持任一的注解检查
     */
    private Annotation[] deDuplicationAnnotation(Annotation[] annotations) {
        List<Integer> componentIndex = new ArrayList<>(annotations.length);
        boolean flag = false;
        for (int i = 0; i < annotations.length; i++) {
            //有没有Component注解
            if (annotations[i].annotationType() == Component.class) {
                componentIndex.add(i);
            } else {
                //有没有被Component注解的注解
                if (isAnnotated(annotations[i].annotationType(), Component.class)) {
                    flag = true;
                }
            }
        }
        if (flag && componentIndex.size() > 0) {
            //去掉多余的Component注解
            Annotation[] result = new Annotation[annotations.length - componentIndex.size()];
            for (int i = 0, j = 0, index = 0; i < annotations.length; i++) {
                if (index < componentIndex.size()) {
                    if (i == componentIndex.get(index)) {
                        index++;
                    } else {
                        result[j] = annotations[i];
                        j++;
                    }
                } else {
                    result[j] = annotations[i];
                    j++;
                }

            }
            return result;
        }
        return annotations;
    }

    private boolean isAnnotated(Class front, Class after) {
        if (front.isAnnotationPresent(after)) {
            return true;
        } else {
            if (front.isAnnotation()) {
                Annotation[] annotations = front.getAnnotations();
                for (Annotation annotation : annotations) {
                    if (isAnnotated(annotation.annotationType(), after)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private BeanDefinition handleTypeAnnot(Class clazz, boolean isBeanAnnotated) throws Exception {
        Annotation[] annotations = clazz.getAnnotations();
        annotations = deDuplicationAnnotation(annotations);
        for (Annotation annotation : annotations) {
            log.debug(annotation + " is component ? " + annotation.annotationType().isAnnotationPresent(Component.class));
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
        if (getRegisterBeans().putIfAbsent(beanDefinition.getId(), beanDefinition) != null) {
            throw new BeanDefinitionException(String.format("bean is duplicate,bean name is '%s'", beanDefinition.getId()));
        }
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
                }
            }
        }
        return methodInfos;
    }

    public void setScanPackages(List<String> scanPackages) {
        this.scanPackages.addAll(scanPackages);
    }

}
