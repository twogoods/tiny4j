package com.tg.tiny4j.core.ioc.beans.reader;

import com.tg.tiny4j.commons.utils.StringUtil;
import com.tg.tiny4j.core.ioc.beans.BeanDefinition;
import com.tg.tiny4j.core.ioc.exception.ConfigurationException;
import com.tg.tiny4j.core.ioc.resource.ResourceLoad;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Created by twogoods on 16/10/26.
 */
public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader {
    private static Logger log= LogManager.getLogger(AbstractBeanDefinitionReader.class);

    private Map<String, BeanDefinition> registerBeans;

    private Map<String, String> configMap;


    public AbstractBeanDefinitionReader() {
        registerBeans = new HashMap<>();
        configMap = new HashMap<>();
    }

    public void loadConfig(String filePath) throws Exception {
        Properties prop = null;
        try {
            prop = ResourceLoad.getInstance().loadConfig(filePath);
            prop.propertyNames();
            Enumeration en = prop.propertyNames();
            while (en.hasMoreElements()) {
                String strKey = (String) en.nextElement();
                configMap.put(strKey, prop.getProperty(strKey));
                log.debug("get config : {} = {}",strKey , prop.getProperty(strKey));
            }
        } catch (ConfigurationException e) {
            log.warn(e.getMessage());
        }catch (Exception e1){
            throw e1;
        }
    }

    @Override
    public Map<String, BeanDefinition> getRegisterBeans() {
        return registerBeans;
    }


    protected String getConfigValue(PlaceholderPraser.KeyAndDefault valueAndDefault){
        String value=configMap.get(valueAndDefault.getName());
        if(value==null){
            return valueAndDefault.getDefaultValue();
        }
        return value;
    }
    /**
     * 通过全类名得到bean名
     *
     * @param classname
     * @return
     */
    protected static String getBeanNameByClassName(String classname) {
        String[] path = classname.split("\\.");
        return StringUtil.firstCharLowercase(path[path.length - 1]);
    }

    /**
     * 未写注解参数,类名首字母小写
     */
    protected String getBeanName(String annotName, Class clazz) {
        if (StringUtil.isEmpty(annotName)) {
            return StringUtil.firstCharLowercase(clazz.getSimpleName());
        }
        return annotName;
    }
    protected String getBeanName(String annotName, String defaultName) {
        if (StringUtil.isEmpty(annotName)) {
            return StringUtil.firstCharLowercase(defaultName);
        }
        return annotName;
    }

}
