package com.tg.tiny4j.core.ioc.beans.factory;

import com.tg.tiny4j.core.aop.AopProxy;
import com.tg.tiny4j.core.aop.AutoSetterCglibAopProxy;
import com.tg.tiny4j.core.aop.CglibAopProxy;
import com.tg.tiny4j.core.aop.advice.AopAdvice;
import com.tg.tiny4j.core.aop.advice.BeanAnnotatedAopInterceptor;
import com.tg.tiny4j.core.aop.advice.Target;
import com.tg.tiny4j.core.ioc.beans.*;
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
            log.debug("beans :{}", beans);
            for (String beanName : beans.keySet()) {
                String classname = beans.get(beanName).getClassname();
                if (BeanPostProcessor.class.isAssignableFrom(Class.forName(classname))) {
                    postProcessorList.add((BeanPostProcessor) getBean(beanName));
                }
            }
        }
    }

    public void preInstantiateSingletons() throws Exception {
        selectBeanPostProcessor();
        for (Iterator it = beans.keySet().iterator(); it.hasNext(); ) {
            String beanName = (String) it.next();
            getBean(beanName);
        }
    }

    private Object createBeanAnnotatedBean(BeanAnnotatedDefinition beanAnnotatedDefinition) throws Exception {
        /**
         * 1.创建一个普通对象,把属性都付好值
         * 2.设置aopadvice
         * 3.创建cglib代理对象
         * 4.为这个代理对象附上1中得到对象的属性值
         * 5.调用这个代理对象的方法,aop的拦截器里会调真正的那个方法,创建第一个对象出来,这个时候把这个对象放到容器里
         */
        Object obj = createBean(beanAnnotatedDefinition);

        AopAdvice beanAnnotatedAopAdvice = new AopAdvice();
        beanAnnotatedAopAdvice.setTarget(new Target(beanAnnotatedDefinition.getClazz()));
        beanAnnotatedAopAdvice.setInterceptor(new BeanAnnotatedAopInterceptor(beanAnnotatedDefinition.getMethodInfos(),beans, this));

        AopProxy aopProxy = new AutoSetterCglibAopProxy(beanAnnotatedAopAdvice);
        Object proxyObj = aopProxy.getProxy();

        Class proxyClass = proxyObj.getClass();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field f : fields) {
            try {
                f.setAccessible(true);
                Method setter = proxyClass.getDeclaredMethod(getSetterName(f.getName()), f.getType());
                setter.invoke(proxyObj, f.get(obj));
            } catch (NoSuchMethodException e) {
                log.warn(e.getMessage());
            }
        }


        Map<String, String> annotatedMethods = beanAnnotatedDefinition.getMethodInfos();
        log.debug("bean have @Bean method:{}",annotatedMethods);
        for (String methodName : annotatedMethods.keySet()) {
            Object bean = proxyClass.getDeclaredMethod(methodName).invoke(proxyObj);
        }
        return proxyObj;
    }

    private String getSetterName(String fieldName) {
        return "set" + StringUtils.firstCharUpperCase(fieldName);
    }

    @Override
    public Object getBean(String name) throws Exception {
        BeanDefinition beanDefinition = beans.get(name);
        if (Validate.isEmpty(beanDefinition)) {
            throw new BeanException(String.format("no bean named %s is define", name));
        }
        Object bean = beanDefinition.getBean();
        if (Validate.isEmpty(bean)) {
            if (beanDefinition instanceof BeanAnnotatedDefinition) {
                bean=  createBeanAnnotatedBean((BeanAnnotatedDefinition) beanDefinition);
                bean = postProcess(bean, name);
                beanDefinition.setBean(bean);
            } else {
                bean = createBean(beans.get(name));
                bean = postProcess(bean, name);
                beanDefinition.setBean(bean);
            }
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
        //TODO 不支持构造函数设置属性值
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
