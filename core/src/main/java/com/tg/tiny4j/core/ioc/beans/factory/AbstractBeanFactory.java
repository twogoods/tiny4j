package com.tg.tiny4j.core.ioc.beans.factory;

import com.tg.tiny4j.core.ioc.beans.BeanDefinition;
import com.tg.tiny4j.core.ioc.beans.BeanPostProcessor;
import com.tg.tiny4j.core.ioc.beans.BeanPropertyValue;
import com.tg.tiny4j.core.ioc.beans.BeanReference;
import com.tg.tiny4j.core.ioc.exception.BeanException;
import com.tg.tiny4j.core.ioc.utils.StringUtils;
import com.tg.tiny4j.core.ioc.utils.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by twogoods on 16/10/25.
 */
public abstract class AbstractBeanFactory implements BeanFactory {

    private static Logger log = LogManager.getLogger(AbstractBeanFactory.class);

    private Map<String, BeanDefinition> beans = new HashMap<>();

    private List<BeanPostProcessor> postProcessorList = new ArrayList<>();

    public AbstractBeanFactory() throws Exception {
    }

    private void selectBeanPostProcessor() throws Exception {
        if (postProcessorList.size() == 0) {
            log.debug("selectBeanPostProcessor...");
            log.debug("beans :{}", beans);
            for (String beanName : beans.keySet()) {
                String classname = beans.get(beanName).getClassname();
                if (BeanPostProcessor.class.isAssignableFrom(Class.forName(classname))) {
                    postProcessorList.add((BeanPostProcessor) getBean(beanName));
                }
            }
            log.debug("count:{}", postProcessorList.size());
        }
    }

    public void preInstantiateSingletons() throws Exception {
        selectBeanPostProcessor();
        for (Iterator it = beans.keySet().iterator(); it.hasNext(); ) {
            String beanName = (String) it.next();
            getBean(beanName);
        }
    }

    @Override
    public Object getBean(String name) throws Exception {
        BeanDefinition beanDefinition = beans.get(name);
        if (Validate.isEmpty(beanDefinition)) {
            throw new BeanException(String.format("no bean named %s is define", name));
        }
        Object bean = beanDefinition.getBean();
        if (Validate.isEmpty(bean)) {
            bean = createBean(beans.get(name));
            bean = postProcess(bean, name);
            beanDefinition.setBean(bean);
        }
        return bean;
    }

    private Object postProcess(Object bean, String name) {
        for (BeanPostProcessor postProcessor : postProcessorList) {
            bean = postProcessor.postProcessBeforeInitialization(bean, name);
        }
        //spring 的bean配置里有一个initmetod,会在这里执行,此处略过
        //TODO 配合aop我们在后置处理器把bean替换成proxy
        for (BeanPostProcessor postProcessor : postProcessorList) {
            bean = postProcessor.postProcessAfterInitialization(bean, name);
        }
        return bean;
    }

    private Object createBean(BeanDefinition beanDefinition) throws Exception {
        Object o = beanDefinition.getClazz().newInstance();
        applyValue(o, beanDefinition);
        return o;
    }

    private void applyValue(Object bean, BeanDefinition beanDefinition) throws Exception {
        Set<BeanPropertyValue> properties = beanDefinition.getProperties();
        Iterator<BeanPropertyValue> iterator = properties.iterator();
        while (iterator.hasNext()) {
            BeanPropertyValue propertyValue = iterator.next();
            String propertyName = propertyValue.getName();
            Object value = propertyValue.getValue();
            if (value instanceof BeanReference) {
                BeanReference beanReference = (BeanReference) value;
                value = getBean(beanReference.getName());
            }
            try {
                /*
                TODO 类型转化,现在只能string
                Field declaredField = bean.getClass().getDeclaredField(propertyValue.getName());
                // int 为 Interger.type
                Class type=declaredField.getType();
                declaredField.setAccessible(true);
                declaredField.set(bean, value);
                 */

                Field declaredField = bean.getClass().getDeclaredField(propertyValue.getName());
                declaredField.setAccessible(true);
                declaredField.set(bean, value);
            } catch (NoSuchFieldException e) {
                Method declaredMethod = bean.getClass().getDeclaredMethod(
                        "set" + StringUtils.firstCharUpperCase(propertyValue.getName()), value.getClass());
                declaredMethod.setAccessible(true);
            }
        }

    }

    public void addPostProcessorList(BeanPostProcessor beanPostProcessor) {
        postProcessorList.add(beanPostProcessor);
    }

    public void addBeanDefinition(String name, BeanDefinition beanDefinition) {
        beans.put(name, beanDefinition);
    }

    public void addBeanDefinition(Map<String, BeanDefinition> beanDefinitionMap) {
        beans.putAll(beanDefinitionMap);
    }

    @Override
    public <T> T getBean(String name, Class<T> clazz) throws Exception {
        Object bean = getBean(name);
        if (bean.getClass().equals(clazz)) {
            return (T) bean;
        }
        log.warn("bean:{},type:{} can not find", name, clazz);
        return null;
    }

    @Override
    public boolean containsBean(String name) throws Exception {
        return !Validate.isEmpty(getBean(name));
    }


}
